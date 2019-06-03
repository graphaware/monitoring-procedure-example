package com.graphaware.neo4j.monitoring;

import org.apache.commons.io.IOUtils;
import org.junit.ClassRule;
import org.junit.Test;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.testcontainers.containers.Neo4jContainer;

import java.net.HttpURLConnection;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.assertj.core.api.Assertions.assertThat;

;

public class MonitorProcedureIT {

    @ClassRule
    public static Neo4jContainer neo4j =
            new CustomNeo4jContainer()
            .withEnterpriseEdition()
                    .withNeo4jConfig("metrics.prometheus.enabled", "true")
                    .withNeo4jConfig("metrics.prometheus.endpoint", "0.0.0.0:2004")
                    .withFileSystemBind("target/monitoring-procedure-example.jar","/var/lib/neo4j/plugins/monitoring-procedure-example.jar");

    @Test
    public void shouldMonitorProcedure() throws Throwable {
        String uri = "bolt://" + neo4j.getContainerIpAddress() + ":" + neo4j.getMappedPort(7687);

        try (Driver driver = GraphDatabase.driver(uri, AuthTokens.basic("neo4j", "password"))) {

            Session session = driver.session();

            session.run("CALL example.monitoring()").consume();

            int prometheusPort = neo4j.getMappedPort(2004);

            URL url = new URL("http://localhost:" + prometheusPort);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            String response = IOUtils.toString(con.getInputStream(), "UTF-8");
            assertThat(responseCode).isEqualTo(HTTP_OK);
            assertThat(response).containsPattern("com_graphaware_neo4j_monitoring_MonitorProcedure_my_timer_count \\d+\\.\\d+");
            assertThat(response).containsPattern("com_graphaware_neo4j_monitoring_MonitorProcedure_my_counter \\d+\\.\\d+");
        }
    }
}