/*
 * RESTController.java
 * Serves REST endpoints for the URL shortening service using Spring MVC
 * 
 * Revision History:
 * 1.3.0   Tim Sullivan   Created
 * 1.3.1   Tim Sullivan   Added throttling
 */

package cloud.timsullivan.urlshortener.controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.weddini.throttling.Throttling;
import com.weddini.throttling.ThrottlingType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Serves REST endpoints for the URL shortening service using Spring MVC
 * 
 * ----------------------------------------------------------------------------
 * References: Spring Web MVC: Annotated Controllers
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-controller
 */
@RestController
public class RESTController {

    /**
	 * Send class logging information to the Spring logger
	 */
    private Logger logger = LoggerFactory.getLogger(RESTController.class);
    
    /**
	 * Contains our shortened URLS in memory
	 */
    private Hashtable<String, URL> lookupTable = new Hashtable<String, URL>();
    
    /**
	 * Holds the address or name of the host for generating links
	 */
	@Value("${SERVICE_HOST}")
	private String serviceHost;

	/**
	 * Holds the port of the service for generating links
	 * Defaults to an empty string if not configured
	 */
	@Value("${SERVICE_PORT:}")
	private String servicePort;
    
    /**
     * Turns a shortened URL ID into an absolute URL
     * @param id The ID of a shortened URL
     * @return A URL to this service that will redirect to the target page
     */
	private String generateURLFromShortened(String id) {
		if (servicePort != null && !servicePort.isEmpty()) {
			return "http://" + this.serviceHost + ":" + this.servicePort + "/" + id + "/";
		} else {
			return "http://" + this.serviceHost + "/" + id + "/";
		}
	}

	/**
	 * Generate a short string of random alphanumeric characters
	 * The total number of possibilities is 62^8 = alphabet size ^ number of positions
	 * @return A random string that is not currently in the lookup table
	 */
	private String randomShortString() {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder output;
        Random r = new Random();
		do {
			output = new StringBuilder();
			while (output.length() < 8) { // length of the random string.
				int index = r.nextInt(chars.length());
				output.append(chars.charAt(index));
			}
		} while (this.lookupTable.containsKey(output.toString()));
		return output.toString();
    }
    
    /**
	 * HTTP GET "/rest/{id}/"
	 * Resolve a shortened URL and return the original
	 * @param id The shortened ID that indexes the target URL
	 * @return An HTTP response (200 OK, URL), or (404 NOT FOUND, null)
	 */
    @GetMapping("/rest/{id}/")
    @Throttling(limit = 2, timeUnit = TimeUnit.SECONDS, type = ThrottlingType.RemoteAddr)
    public ResponseEntity<URL> getURL(@PathVariable("id") String id) {
        if (this.lookupTable.containsKey(id)) {
			return new ResponseEntity<URL>(this.lookupTable.get(id), HttpStatus.OK);
		} else {
			return new ResponseEntity<URL>((URL)null, HttpStatus.NOT_FOUND);
		}
    }

    /**
	 * HTTP POST "/rest/shorten"
	 * Request a new shortened URL to redirect to the requested URL
	 * @param url The URL to shorten, encoded as a string
	 * @param model A container for data to send to the Thymeleaf template for rendering
	 * @return An HTTP response (200 OK, URL), or (404 NOT FOUND, null), or (500 INTERNAL SERVER ERROR, null)
	 */
    @PostMapping("/rest/shorten")
    @Throttling(limit = 2, timeUnit = TimeUnit.SECONDS, type = ThrottlingType.RemoteAddr)
	public ResponseEntity<URL> storeURL(@RequestBody URL url) {
		try {
			if (this.lookupTable.containsValue(url)) {
				for (Map.Entry<String, URL> entry: this.lookupTable.entrySet()) {
					if(entry.getValue().equals(url)) {
                        return new ResponseEntity<URL>(
                            new URL(this.generateURLFromShortened(entry.getKey())), 
                            HttpStatus.OK
                        );
					}
                }
                logger.error("Could not find already-used URL in the database");
                return new ResponseEntity<URL>((URL)null, HttpStatus.NOT_FOUND);
			} else {
				String shortenedURL = this.randomShortString();
				this.lookupTable.put(shortenedURL, url);
                return new ResponseEntity<URL>(
                    new URL(this.generateURLFromShortened(shortenedURL)), 
                    HttpStatus.OK
                );
			}

		} catch (MalformedURLException e) {
            logger.error("Attempted to generate a malformed URL", e);
            return new ResponseEntity<URL>((URL)null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}