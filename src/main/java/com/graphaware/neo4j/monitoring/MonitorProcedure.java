package com.graphaware.neo4j.monitoring;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Procedure;

import static com.codahale.metrics.MetricRegistry.name;

public class MonitorProcedure {

    //Container for our application’s metrics
    private static final MetricRegistry METRIC_REGISTRY = new MetricRegistry();

    //Custom Timer metric registration
    private static final Timer myTimer = METRIC_REGISTRY.timer(name(MonitorProcedure.class, "my_timer"));

    //Custom Counter metric registration
    private static final Counter myCounter = METRIC_REGISTRY.counter(name(MonitorProcedure.class, "my_counter"));

    @Context
    public GraphDatabaseService db;

    static {
        //We use default registry of the Prometheus client's CollectorRegistry to register our MetricRegistry
        //This way our metrics will be published on the Neo4j's Prometheus URL
        CollectorRegistry.defaultRegistry.register(new DropwizardExports(METRIC_REGISTRY));
    }

    @Procedure(value = "example.monitoring", mode=Mode.READ)
    @Description("Example procedure to define some custom metrics and publish to Prometheus")
    public void monitoring()
    {
        Timer.Context timerContext = myTimer.time();

        //Do some business logic here
        db.getAllLabels();

        //Use our custom counter
        myCounter.inc();

        //Close Timer context
        timerContext.close();
    }

}
