# Example URL Shortening Service

|Version|Date|Author|
|---|---|---|
|1.1.0|09/11/2019|Timothy Sullivan|

This project demonstrates a RESTful API microservice in Java using:
* JDK 8
* Maven
* Spring Boot
* Thymeleaf

The microservice serves as a URL shortener, accepting a valid web URL and returning a shortened URL.

## 1. Running with Maven

1. Ensure that JDK 8 is installed and the ```JAVA_HOME``` environment variable is properly configured to point to its install location. [Download JDK 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html). [Installing JDK 8](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html).
2. Ensure that Maven is installed and the ```PATH``` environment variable is properly configured to point to its install location. [Installing Apache Maven](https://maven.apache.org/install.html).
3. In the home directory of the project, run the command:
    ```
    mvn -DSERVICE_HOST=<desired host address> -DSERVICE_PORT=<desired host port> spring-boot:run
    ```

## 2a. Running with Docker

1. Ensure that Docker is installed and properly configured. [Installing Docker](https://docs.docker.com/install/).
2. Build the Docker image with the following command in the project root directory (containing the ```Dockerfile```):
    ```
    docker build -t urlshortener .
    ```
3. Run the image with the following command:
    ```
    docker run \
        -e SERVICE_HOST=<desired host address> \
        -e SERVICE_PORT=<desired host port> \
        -h <desired host address> \
        -p 8080:<desired host port> \
        urlshortener
    ```

## 3. Using the Service

### 3.1 Submitting a URL

1. In a web browser, navigate to the service's host name and port (as configured above). For example:
    ```
    http://localhost:8080
    ```
2. Submit a URL using the presented form
3. Copy or click the link that is returned to be redirected to the shortened URL's target page

NOTE: Submission will fail if the URL is already registered