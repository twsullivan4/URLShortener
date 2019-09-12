# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## 1.3.1 - 2019/09/12
## Added
- Service throttling by Remote Address

## 1.3.0 - 2019/09/12
## Changed
- Properly split Controllers into REST and UI

## 1.2.0 - 2019/09/11
### Added
- Can now generate URLS without ports
- AWS Elastic Beanstalk instructions

## 1.1.0 - 2019/09/11
### Added
- ```Dockerfile``` based on the official Maven image
- More complete in-code documentation

### Changed
- Link URL is now customizable via environment variables

## 1.0.0 - 2019/09/10
### Added
- Documentation files: ```README.md```, ```CHANGELOG.md```, and ```TODO.md```
- Spring Boot project files: ```pom.xml```, ```application.properties```, and ```URLShortenerApplication.java``` 
- Spring MVC controller: ```UIController.java``` for pages
- Thymeleaf UI template pages
- Basic in-memory database