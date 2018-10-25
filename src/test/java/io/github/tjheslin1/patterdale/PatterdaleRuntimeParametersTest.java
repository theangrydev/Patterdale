package io.github.tjheslin1.patterdale;

import io.github.tjheslin1.patterdale.config.PatterdaleConfig;
import io.github.tjheslin1.patterdale.config.PatterdaleRuntimeParameters;
import io.github.tjheslin1.patterdale.metrics.probe.DatabaseDefinition;
import io.github.tjheslin1.patterdale.metrics.probe.Probe;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static io.github.tjheslin1.patterdale.config.PatterdaleRuntimeParameters.patterdaleRuntimeParameters;
import static io.github.tjheslin1.patterdale.metrics.probe.DatabaseDefinition.databaseDefinition;
import static io.github.tjheslin1.patterdale.metrics.probe.Probe.probe;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class PatterdaleRuntimeParametersTest implements WithAssertions {

    @Test
    public void extractsRuntimeParametersFromUnmarshalledConfigurationFile() throws Exception {
        PatterdaleRuntimeParameters expectedParameters = new PatterdaleRuntimeParameters(
                HTTP_PORT,
                CACHE_DURATION,
                PROBE_CONNECTION_WAIT_IN_SECONDS,
                DATABASES,
                MAX_SIZE,
                MIN_IDLE,
                asList(PROBE_1, PROBE_2),
                MAX_CONNECTION_RETRIES,
                CONNECTION_RETRY_DELAY_IN_SECONDS);

        PatterdaleRuntimeParameters actualParameters = patterdaleRuntimeParameters(exampleConfig(HTTP_PORT));
        assertThat(actualParameters).isEqualTo(expectedParameters);
    }

    @Test(expected = IllegalArgumentException.class)
    public void httpPortMustBeSet() throws Exception {
        patterdaleRuntimeParameters(exampleConfig(0));
    }

    private static PatterdaleConfig exampleConfig(int httpPort) {
        PatterdaleConfig config = new PatterdaleConfig();

        config.httpPort = httpPort;
        config.cacheDuration = CACHE_DURATION;
        config.probeConnectionWaitInSeconds = PROBE_CONNECTION_WAIT_IN_SECONDS;

        config.databases = new DatabaseDefinition[]{
                databaseDefinition(NAME, USER, JDBC_URL, singletonList(PROBE_NAME_1)),
                databaseDefinition(NAME_2, USER, JDBC_URL_2, singletonList(PROBE_NAME_2))
        };

        config.probes = new Probe[]{
                PROBE_1, PROBE_2
        };

        HashMap<String, String> connectionPoolProperties = new HashMap<>();
        connectionPoolProperties.put("maxSize", Integer.toString(MAX_SIZE));
        connectionPoolProperties.put("minIdle", Integer.toString(MIN_IDLE));
        connectionPoolProperties.put("maxConnectionRetries", Integer.toString(MAX_CONNECTION_RETRIES));
        connectionPoolProperties.put("connectionRetryDelayInSeconds", Integer.toString(CONNECTION_RETRY_DELAY_IN_SECONDS));
        config.connectionPool = connectionPoolProperties;

        return config;
    }


    private static final int HTTP_PORT = 7000;
    private static final long CACHE_DURATION = 60;
    private static final int PROBE_CONNECTION_WAIT_IN_SECONDS = 10;

    private static final String NAME = "test";
    private static final String NAME_2 = "test2";
    private static final String USER = "system";
    private static final String JDBC_URL = "jdbc:oracle:thin:system/oracle@localhost:1521:xe";
    private static final String JDBC_URL_2 = "jdbc:oracle:thin:system/oracle@localhost:1522:xe";
    private static final String TYPE = "exists";
    private static final String METRIC_NAME = "database_up";
    private static final String METRIC_LABELS = "database=\"myDB\"";
    private static final String METRIC_LABELS_2 = "database=\"myDB2\"";
    private static final String QUERY_SQL = "SELECT 1 FROM DUAL";

    private static final String PROBE_NAME_1 = "probe1";
    private static final String PROBE_NAME_2 = "probe2";
    private static final Probe PROBE_1 = probe(PROBE_NAME_1, QUERY_SQL, TYPE, METRIC_NAME, METRIC_LABELS);
    private static final Probe PROBE_2 = probe(PROBE_NAME_2, QUERY_SQL, TYPE, METRIC_NAME, METRIC_LABELS_2);

    private static final int MAX_SIZE = 5;
    private static final int MIN_IDLE = 1;
    private static final int MAX_CONNECTION_RETRIES = 5;
    private static final int CONNECTION_RETRY_DELAY_IN_SECONDS = 60;

    private static final List<DatabaseDefinition> DATABASES = asList(
            databaseDefinition(NAME, USER, JDBC_URL, singletonList(PROBE_NAME_1)),
            databaseDefinition(NAME_2, USER, JDBC_URL_2, singletonList(PROBE_NAME_2))
    );
}