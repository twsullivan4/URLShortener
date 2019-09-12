/*
 * UIController.java
 * Serves UI pages for the URL shortening service using Spring MVC
 * 
 * Revision History:
 * 1.0.0   Tim Sullivan   Created
 * 1.1.0   Tim Sullivan   Commented
 * 1.2.0   Tim Sullivan   Added more complex link generation logic
 * 1.3.0   Tim Sullivan   Removed REST endpoints into RESTController.java
 * 1.3.1   Tim Sullivan   Added throttling
 */

package cloud.timsullivan.urlshortener.controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import com.weddini.throttling.Throttling;
import com.weddini.throttling.ThrottlingType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
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
	 * HTTP GET "/"
	 * Land the user on an interface page to submit a link for shortening
	 * @return A String pointing to the Thymeleaf "main.html" template
	 */
	@GetMapping("/")
	@Throttling(limit = 2, timeUnit = TimeUnit.SECONDS, type = ThrottlingType.RemoteAddr)
    public String landingPage() {
        return "main";
	}
	
	/**
	 * HTTP GET "/notFound"
	 * Generates an error page that the requested URL is not registered
	 * @param model A container for data to send to the Thymeleaf template for rendering
	 * @return A String pointing to the Thymeleaf "error.html" template
	 */
	@GetMapping("/notFound")
	@Throttling(limit = 2, timeUnit = TimeUnit.SECONDS, type = ThrottlingType.RemoteAddr)
	public String notFound(Model model) {
		model.addAttribute("message", "The requested URL is not registered with this service");
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
	@Throttling(limit = 2, timeUnit = TimeUnit.SECONDS, type = ThrottlingType.RemoteAddr)
	public String storeURL(@RequestParam("url") String stringURL, Model model) {
		try {

			logger.info("Input string: " + stringURL);
			URL url = new URL(stringURL);
			logger.info("URL object: " + url.toString());

			RestTemplate restService = new RestTemplate();
			HttpEntity<URL> request = new HttpEntity<URL>(url);
			ResponseEntity<URL> result = restService.postForEntity("http://localhost:8080/rest/shorten", request, URL.class);

			if (result.getStatusCode() == HttpStatus.OK) {
				model.addAttribute("url", result.getBody().toString());
				return "result";
			} else {
				model.addAttribute("message", "An internal server error occurred");
				return "error";
			}

		} catch (MalformedURLException e) {
			model.addAttribute("message", "The requested URL is malformed");
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
	@Throttling(limit = 2, timeUnit = TimeUnit.SECONDS, type = ThrottlingType.RemoteAddr)
	@ResponseBody
	public RedirectView resolveURL(@PathVariable("id") String id) {
		RestTemplate restService = new RestTemplate();
		ResponseEntity<URL> result = restService.getForEntity("http://localhost:8080/rest/{id}/", URL.class, id);

		if (result.getStatusCode() == HttpStatus.OK) {
			return new RedirectView(result.getBody().toString());
		} else {
			return new RedirectView("/notFound");
		}
	}
}