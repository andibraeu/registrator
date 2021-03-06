Weimarnetz Registrator
====

[![Build Status](https://travis-ci.org/andibraeu/registrator.svg?branch=master)](https://travis-ci.org/andibraeu/registrator) [![codecov](https://codecov.io/gh/andibraeu/registrator/branch/master/graph/badge.svg)](https://codecov.io/gh/andibraeu/registrator)

Prerequisites
----
* jdk installed
* vagrant installed
* docker installed

About
----
This is an Spring-Boot application that is already configured to support a lot of tools and plugins i like to use in my daily business.

Things that are configured already:
* [Actuator](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready)
	* adds health check, jms, metrics, loggers, dependencies, ... endpoints
* [SwaggerUI](https://swagger.io/swagger-ui/)
	* adds a generated living documentation of your api endpoints that will be always up-to-date, 
	with "try out" functionality to make actual requests 
* deployment via [Ansible](https://www.ansible.com/)
	* deploys the application into a vagrant box for testing behaviour - ready to only adjust hosts and ssh config to 
	have a server ready deployment. including serverside installation of:
		* jdk
* shell script for dependency update checks
	* check for maven property updates
	* check for maven plugin updates
	* check for maven dependency updates

Start with ```mvn spring-boot:run```

Use swagger ui to discover ```http://<hostname>:<port>/swagger-ui.html```

Find generated docs: ```http://<hostname>:<port>/docs/index.html```
