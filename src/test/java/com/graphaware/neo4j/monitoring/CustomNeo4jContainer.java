package com.graphaware.neo4j.monitoring;

import org.testcontainers.containers.Neo4jContainer;

public class CustomNeo4jContainer extends Neo4jContainer<CustomNeo4jContainer>  {

    @Override
    protected void configure() {
        super.configure();
        addExposedPorts(2004);
    }

}
