# Patterdale [![Build Status](https://travis-ci.org/tjheslin1/Patterdale.svg?branch=master)](https://travis-ci.org/tjheslin1/Patterdale)

[![Docker Pulls](https://img.shields.io/docker/pulls/tjheslin1/patterdale.svg?maxAge=604800)](https://hub.docker.com/r/tjheslin1/patterdale/)

[See here for example `patterdale.yml` configuration file](https://gist.github.com/tjheslin1/5d74d5ad6c4b0dda8662c381590ddbbb#file-patterdale-yml)

[See here for complete documentation](https://tjheslin1.github.io/Patterdale/).

Prometheus Oracle database exporter. Runs provided queries.

`docker run -d -p 7000:7000 -v /your/jdbc/odjbc7.jar:/app/odjbc7.jar -v /your/config/directory:/config -v /your/secrets/directory:/passwords tjheslin1/patterdale:1.2.2`

If a `logback.xml` file is included in the directory passed into the `/config` container volume, this will override your logging configuration.

Note the volume mount: `/your/jdbc/odjbc7.jar:/app/odjbc7.jar` in the example.
This driver can be downloaded manually from the
[Oracle JDBC Downloads page](http://www.oracle.com/technetwork/database/features/jdbc/jdbc-drivers-12c-download-1958347.html)
or, **preferably**, you can provide `mavenOracleUsername` and `mavenOraclePassword` in your `gradle.properties` file when building locally which will fetch the driver from the maven repository.
This requires you register an Oracle single sign-on account.

## Troubleshooting

Getting one of the following exceptions when running locally?:
- `java.sql.SQLRecoverableException`
- `java.net.ConnectException`
- `com.zaxxer.hikari.pool.HikariPool$PoolInitializationException`
- or similar

`patterdale.yml` from _src/test/resources_ will need to have the `jdbcUrl`s changed locally to 
point to your local Oracle database(s), if this occurs replace _localhost_ in 
the `jdbcUrl`s with the local IP address or your databases. For local Docker images this 
will be the IP of your machine.

The `sath89/oracle-12c` docker images take a few minutes to start up. 
Patterdale will retry connections to the database(s) according to the 
`maxConnectionRetries` and `connectionRetryDelayInSeconds` settings.
During this time the `HikariPool$PoolInitializationException` will be logged, 
as will details of the connection retry attempts.

## Example view of Patterdale metrics within Prometheus
![Example view of Patterdale metrics within Prometheus](https://github.com/tjheslin1/Patterdale/blob/master/docs/Patterdale_screenshot.png)
