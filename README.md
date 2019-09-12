# Example URL Shortening Service

|Version|Date|Author|
|---|---|---|
|1.3.1|09/12/2019|Timothy Sullivan|

This project demonstrates a RESTful API microservice in Java using:
* JDK 8
* Maven
* Spring Boot
* Thymeleaf

The microservice serves as a URL shortener, accepting a valid web URL and returning a shortened URL.

## 1. Installation and Running

### 1.1 Running with Maven

1. Ensure that JDK 8 is installed and the ```JAVA_HOME``` environment variable is properly configured to point to its install location. [Download JDK 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html). [Installing JDK 8](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html).
2. Ensure that Maven is installed and the ```PATH``` environment variable is properly configured to point to its install location. [Installing Apache Maven](https://maven.apache.org/install.html).
3. In the home directory of the project, run the command:
    ```
    mvn -DSERVICE_HOST="<desired host name/address>" -DSERVICE_PORT="<desired host port>" spring-boot:run
    ```

NOTE: If the host name resolves correctly without a port (i.e. through DNS), ```-DSERVICE_PORT``` may be omitted.

### 1.2 Running with Docker

1. Ensure that Docker is installed and properly configured. [Installing Docker](https://docs.docker.com/install/).
2. Build the Docker image with the following command in the project root directory (containing the ```Dockerfile```):
    ```
    docker build -t urlshortener .
    ```
3. Run the image with the following command:
    ```
    docker run \
        -e SERVICE_HOST="<desired host name/address>" \
        -e SERVICE_PORT="<desired host port>" \
        -h <desired host name/address> \
        -p 8080:<desired host port> \
        urlshortener
    ```

NOTE: If the host name resolves correctly without a port (i.e. through DNS), ```-DSERVICE_PORT``` may be omitted.

### 1.3 Running with AWS Elastic Beanstalk

1. Zip the following files and directories into ```URLShortener.zip```:
    ```
    src/
    Dockerfile
    pom.xml
    ```
2. Log into AWS Management Console and navigate to AWS Elastic Beanstalk
3. Select ```Create new environment``` and follow the prompts to configure a "Web Server Environment".
    1. Choose the ```Docker``` generic platform and upload ```URLShortener.zip```.
    2. Select ```Configure more options```, navigate to ```Software``` and add ```SERVICE_HOST``` to ```Environment properties```. If using Elastic Beanstalk's default URL to access the service, enter that value. Or, if using a custom domain name, enter it.
    3. Click ```Create environment```.

## 2. Using the Service

### 2.1 Submitting a URL

1. In a web browser, navigate to the service's host name and port on a local device, or simply the host name for resolved names. For example:
    ```
    http://localhost:8080
    ```
2. Submit a URL using the presented form
3. Copy or click the link that is returned to be redirected to the shortened URL's target page

NOTE: Submission will fail if the URL is already registered