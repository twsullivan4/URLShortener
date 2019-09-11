# Example URL Shortening Service

|Version|Date|Author|
|---|---|---|
|1.0.0|09/10/2019|Timothy Sullivan|

This project demonstrates a RESTful API microservice in Java using:
* JDK 8
* Maven
* Spring Boot
* Thymeleaf

The microservice serves as a URL shortener, accepting a valid web URL and returning a shortened URL.

## Running with Maven

1. Ensure that JDK 8 is installed and the ```JAVA_HOME``` environment variable is properly configured to point to its install location. [Download JDK 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html). [Installing JDK 8](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html).
2. Ensure that Maven is installed and the ```PATH``` environment variable is properly configured to point to its install location. [Installing Apache Maven](https://maven.apache.org/install.html).
3. In the home directory of the project, run the command ```mvn spring-boot:run```