package io.github.tjheslin1.patterdale;

import io.github.tjheslin1.patterdale.metrics.probe.DatabaseDefinition;
import io.github.tjheslin1.patterdale.metrics.probe.Probe;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static io.github.tjheslin1.patterdale.PatterdaleRuntimeParameters.patterdaleRuntimeParameters;
import static io.github.tjheslin1.patterdale.metrics.probe.DatabaseDefinition.databaseDefinition;
import static io.github.tjheslin1.patterdale.metrics.probe.Probe.probe;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class PatterdaleRuntimeParametersTest implements WithAssertions {

    @Test
    public void extractsRuntimeParametersFromUnmarshalledConfigurationFile() throws Exception {
        PatterdaleRuntimeParameters expectedParameters = new PatterdaleRuntimeParameters(
                HTTP_PORT,
                DATABASES,
                MAX_SIZE,
                MIN_IDLE);

        PatterdaleRuntimeParameters actualParameters = patterdaleRuntimeParameters(exampleConfig());
        assertThat(actualParameters).isEqualTo(expectedParameters);
    }

    private static PatterdaleConfig exampleConfig() {
        PatterdaleConfig config = new PatterdaleConfig();

        config.httpPort = HTTP_PORT;
        config.databases = new DatabaseDefinition[]{
                databaseDefinition(NAME, USER, PASSWORD, JDBC_URL, PROBES),
                databaseDefinition(NAME_2, USER, PASSWORD, JDBC_URL_2, PROBES_2)
        };

        HashMap<String, String> connectionPoolProperties = new HashMap<>();
        connectionPoolProperties.put("maxSize", Integer.toString(MAX_SIZE));
        connectionPoolProperties.put("minIdle", Integer.toString(MIN_IDLE));
        config.connectionPool = connectionPoolProperties;

        return config;
    }

    private static final int HTTP_PORT = 7000;

    private static final String NAME = "test";
    private static final String NAME_2 = "test2";
    private static final String USER = "system";
    private static final String PASSWORD = "oracle";
    private static final String JDBC_URL = "jdbc:oracle:thin:system/oracle@localhost:1521:xe";
    private static final String JDBC_URL_2 = "jdbc:oracle:thin:system/oracle@localhost:1522:xe";
    private static final String TYPE = "exists";
    private static final String METRIC_NAME = "database_up";
    private static final String METRIC_LABELS = "database=\"myDB\"";
    private static final String METRIC_LABELS_2 = "database=\"myDB2\"";
    private static final String QUERY_SQL = "SELECT 1 FROM DUAL";
    private static final List<Probe> PROBES = singletonList(probe(QUERY_SQL, TYPE, METRIC_NAME, METRIC_LABELS));
    private static final List<Probe> PROBES_2 = singletonList(probe(QUERY_SQL, TYPE, METRIC_NAME, METRIC_LABELS_2));
    private static final int MAX_SIZE = 5;
    private static final int MIN_IDLE = 1;

    private static final List<DatabaseDefinition> DATABASES = asList(
            databaseDefinition(NAME, USER, PASSWORD, JDBC_URL, PROBES),
            databaseDefinition(NAME_2, USER, PASSWORD, JDBC_URL_2, PROBES_2)
    );

}