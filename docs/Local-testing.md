To run the full `./gradlew` build locally, [PatterdaleTest.java](https://github.com/tjheslin1/Patterdale/blob/master/src/test/java/endtoend/PatterdaleTest.java)
expects two oracle databases to be running locally.

You can start a pair of default databases with the following commands.
```
docker run -d -p 8081:8080 -p 1522:1521 sath89/oracle-12c
docker run -d -p 8082:8080 -p 1523:1521 sath89/oracle-12c
```

Or you can run the following Docker command: `docker-compose up -d` referencing [docker-compose.yml](https://github.com/tjheslin1/Patterdale/blob/master/docker-compose.yml).
Patterdale requires a the Oracle jdbc driver to be passed in as a volume mount.
It can be downloaded here: [Oracle JDBC Downloads page](http://www.oracle.com/technetwork/database/features/jdbc/jdbc-drivers-12c-download-1958347.html).
I would recommend placing it in a `repo` directory within the project. The jar is ignored in `.gitignore`.

This will start up two Oracle database instances, Prometheus and Patterdale. Prometheus will be available at _http://localhost:9090_. Try searching for the `database_up` metric.

`./gradlew` will require the `ojdbc8.jar` driver. 
By adding the properties `mavenOracleUsername` and `mavenOraclePassword` to your `gradle.properties` file, 
which correspond to a valid Oracle single-sign on account, you'll be able to download the driver. 
If this file doesn't exist, simply create this file in your `.gradle` directory (located in your $HOME dir).

### Building snapshot docker images

```
./gradlew shadowJar docker
```

The following command will generate a fat jar in the `build/libs` directory and then build a docker image based on the 
`Dockerfile` provided. The image built will be named: `tjheslin1/patterdale:DEV-SNAPSHOT`.

### Getting a _java.net.ConnectException_ when running locally?
`patterdale.yml` from _src/test/resources_ may need to have the `jdbcUrl`s updating, if this occurs replace _localhost_ in the `jdbcUrl`s with your local IP address. 


Once the app has started up and created connection pools, the URL to the /metrics page will be logged.
Note: the _sath89/oracle-12c_ containers may take a few minutes to start up.