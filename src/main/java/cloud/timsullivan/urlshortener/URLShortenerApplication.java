/*
 * URLShortenerApplication.java
 * The entry point for the Spring application
 * 
 * Revision History:
 * 1.0.0   Tim Sullivan   Created
 */

package cloud.timsullivan.urlshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Starts the application through Spring Framework
 * 
 * ----------------------------------------------------------------------------
 * References:
 * Using the @SpringBootApplication Annotation
 * https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-using-springbootapplication-annotation.html
 */
@SpringBootApplication
public class URLShortenerApplication {

	/**
	 * Java entry point
	 *
	 * @param args Program arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(URLShortenerApplication.class, args);
	}
}
