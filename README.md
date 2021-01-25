# QuickCase Java logging module
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Build status](https://github.com/quickcase/spring-logging/workflows/Quality%20checks/badge.svg)](https://github.com/quickcase/spring-logging/actions)

A Java module which standardises the logging for the reform projects.

## Prerequisites

- [Java 11](https://www.oracle.com/java)

## User guide

The module provides a `logback.xml` configuration file which configures Logback to use a default quickcase format.
It allows a number of configuration options to customize the logging to your needs.


### Basic usage

Simply add base component as your project's dependency and then one or more of two components discussed below to use it.

Base component dependency, gradle:
```groovy
compile group: 'app.quickcase.logging', name: 'logging', version: '6.0.0'
```

#### java-logging-spring

Use for formatting log output in Spring Boot applications.


Gradle:
```groovy
compile group: 'app.quickcase.logging', name: 'logging-spring', version: '6.0.0'
```

#### java-logging-httpcomponents

Use for adding request IDs to external HTTP / HTTPS requests.

Gradle:
```groovy
compile group: 'app.quickcase.logging', name: 'logging-httpcomponents', version: '6.0.0'
```

**Please note:** You will also need to implement a class that configures an HTTP client with interceptors for outbound HTTP requests and responses. See https://github.com/hmcts/cmc-claim-store/blob/master/src/main/java/uk/gov/hmcts/cmc/claimstore/clients/RestClient.java#L98 for an example.

After that you can log like you would do with any [SLF4J](https://www.slf4j.org/) logger. Define it as a class field:

```java
private static final Logger log = LoggerFactory.getLogger(SomeResource.class);
```

And do the actual logging, e.g.:

```java
log.info("An important business process has finished");
```

### Configuration defaults

By default, the module will use json logging format which can be used out-of-the-box for development:

Log format can be either set to `JSON` or `CONSOLE` by setting the environment variable `LOG_FORMAT`.

JSON
```
{
  "timestamp" : "2021-01-25T15:06:29.957+0000",
  "level" : "INFO",
  "thread" : "main",
  "message" : "Request GET /some/path processed in 0ms",
  "logger_name" : "app.quickcase.logging.filters.RequestStatusLoggingFilter"
}{
  "timestamp" : "2021-01-25T15:06:30.083+0000",
  "level" : "ERROR",
  "thread" : "main",
  "message" : "Request GET /some/path failed in 0ms",
  "exception" : "java.lang.RuntimeException: ..",
  "logger_name" : "app.quickcase.logging.filters.RequestStatusLoggingFilter"
}
```

CONSOLE
```
2017-02-02 12:22:23,647 INFO [main] io.dropwizard.assets.AssetsBundle: Registering AssetBundle with name: swagger-assets for path /swagger-static/*
2017-02-02 12:22:23,806 INFO [main] org.reflections.Reflections: Reflections took 96 ms to scan 1 urls, producing 79 keys and 87 values
2017-02-02 12:22:24,835 INFO [main] io.dropwizard.server.DefaultServerFactory: Registering jersey handler with root path prefix: /
```

The default logging level will be set to `INFO`. It can be adjusted by setting the `LOG_LEVEL` environment variable.
The following log levels are supported
```
ERROR
WARN
INFO
DEBUG
TRACE
```

### Additional Logback configuration:

Additional Logback configuration can be provided by adding a `logback-includes.xml` file to the classpath root
(just drop it in the `main/resources` folder).
This allows to define any configuration allowed by Logback XML config, where a typical usage could be defining more
specific loggers, e.g.:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<included>
  <logger name="app.quickcase.logging" level="DEBUG"/>
  <logger name="app.quickcase.logging.resources" level="WARN"/>
</included>
```

Path to this file can adjusted by setting a `LOGBACK_INCLUDES_FILE` environment variable.

Logback can print additional information while processing its configuration files. This can be enabled by setting
`LOGBACK_CONFIGURATION_DEBUG` variable to `true`.

Log pattern related configurations:

| variable                    | default                     |
| --------------------------- | --------------------------- |
| LOGBACK_DATE_FORMAT         | yyyy-MM-dd'T'HH:mm:ss.SSSZZ |
| EXCEPTION_LENGTH            | 50                          |
| LOGGER_LENGTH               | 50                          |
| CONSOLE_LOG_PATTERN         | %d{${LOGBACK_DATE_FORMAT}} %-5level [%thread] %logger{${LOGGER_LENGTH}}%ex{${EXCEPTION_LENGTH}} %msg%n}                        |

where
 - LOGBACK_DATE_FORMAT: Date format is default logstash encoder date format. `REQUIRE` fields are flags representing show/hide feature.
 - EXCEPTION_LENGTH: how many lines to show in an exception stack trace ( per exception not including causes)
 - LOGGER_LENGTH: how long the logger name can be before logback starts abbreviating the package names

## Development guide

[Gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) will automatically download a
project-local [Gradle](https://gradle.org/) distribution the first time you run any of the `gradlew` commands below.

### Tests and verification

To run all unit tests:

```bash
./gradlew test
```

To execute [Checkstyle](http://checkstyle.sourceforge.net/) and [PMD](http://pmd.sourceforge.net/) checks:

```bash
./gradlew check
```

You can also execute both via:

```bash
./gradlew build
```

### Installing

To install the artifact to a local Maven repository:
```bash
./gradlew install
```

### Exception logging

Since [v1.5.0](https://github.com/hmcts/java-logging/releases/tag/1.5.0) Alert level and error code are required fields for any exception to be logged.
In order to correctly stream log events for all exceptions one must be extended with `AbstractLoggingException`.
Error code is introduced as legacy error group not minding the fact exceptions themselves represent relevant error group.
There is a helper `UnknownErrorCodeException` class which populates the field with `UNKNOWN` as error code.

Alert level is still required.
