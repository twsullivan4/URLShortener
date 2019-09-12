/*
 * UIController.java
 * Serves UI pages for the URL shortening service using Spring MVC
 * 
 * Revision History:
 * 1.0.0   Tim Sullivan   Created
 * 1.1.0   Tim Sullivan   Commented
 */

package cloud.timsullivan.urlshortener.controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Serves UI pages for the URL shortening service using Spring MVC
 * 
 * ----------------------------------------------------------------------------
 * References:
 * Spring Web MVC: Annotated Controllers
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-controller
 */
@Controller
public class UIController {

	/**
	 * Send class logging information to the Spring logger
	 */
	private Logger logger = LoggerFactory.getLogger(UIController.class);

	/**
	 * Holds the address of the host for generating links
	 * Configured in application.properties
	 */
	@Value("${urlshortener.servicehost}")
	private String serviceHost;
	
	/**
	 * 
	 */
	private Hashtable<String, URL> lookupTable = new Hashtable<String, URL>();

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
		} while (lookupTable.containsKey(output.toString()));
		return output.toString();
    }

	/**
	 * HTTP GET "/"
	 * Land the user on an interface page to submit a link for shortening
	 * @return A String pointing to the Thymeleaf "main.html" template
	 */
    @GetMapping("/")
    public String landingPage() {
		// Thymeleaf view: directs the user to the page of this name
        return "main";
	}
	
	/**
	 * HTTP GET "/notFound"
	 * Generates an error page that the requested URL is not registered
	 * @param model A container for data to send to the Thymeleaf template for rendering
	 * @return A String pointing to the Thymeleaf "error.html" template
	 */
	@GetMapping("/notFound")
	public String notFound(Model model) {
		model.addAttribute("message", "The requested URL is not registered with this service");

		// Thymeleaf view: directs the user to the page of this name
		return "error";
	}

	/**
	 * HTTP POST "/shorten"
	 * Request a new shortened URL to redirect to the requested URL
	 * @param stringURL The URL to shorten, encoded as a string
	 * @param model A container for data to send to the Thymeleaf template for rendering
	 * @return A string pointing to the Thymeleaf "result.html" or "error.html" pages
	 */
	@PostMapping("/shorten")
	public String storeURL(@RequestParam("url") String stringURL, Model model) {
		try {
			logger.info("Input string: " + stringURL);
			URL url = new URL(stringURL);
			logger.info("URL object: " + url.toString());
			if (lookupTable.containsValue(url)) {
				model.addAttribute("message", "The requested URL is already registered with this service");

				// Thymeleaf view: directs the user to the page of this name
				return "error";
			} else {
				String shortenedURL = randomShortString();
				lookupTable.put(shortenedURL, url);
				model.addAttribute("url", "http://" + serviceHost + "/" + shortenedURL + "/");

				// Thymeleaf view: directs the user to the page of this name
				return "result";
			}
		} catch (MalformedURLException e) {
			model.addAttribute("message", "The requested URL is malformed");

			// Thymeleaf view: directs the user to the page of this name
			return "error";
		}
	}

	/**
	 * HTTP GET "/{id}/"
	 * Resolve a shortened URL and redirect to the result (or not found)
	 * @param id The shortened ID that indexes the target URL
	 * @return A redirect to the target page, or the not found page
	 */
	@GetMapping("/{id}/")
	@ResponseBody
	public RedirectView resolveURL(@PathVariable("id") String id) {
		if (lookupTable.containsKey(id)) {
			logger.info("Resolved: " + lookupTable.get(id).toString());
			return new RedirectView(lookupTable.get(id).toString());
		} else {
			return new RedirectView("/notFound");
		}
	}
}