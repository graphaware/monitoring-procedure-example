

This is a repository accompanying a blog post about how to create custom metrics in Neo4j and publish them to Prometheus

This repository contains a single module, where we have an example procedure with our own custom metrics published to Prometheus on the same URl as the Neo4j metrics.
The integration test starts a [Neo4j Testcontainer](https://www.testcontainers.org/modules/databases/neo4j/) where we have Neo4j Enterprise edition.
If you want to read more about Neo4j and Testcontainers then read [this blog post](https://graphaware.com/docker,/testing/2018/12/16/integration-testing-with-docker-neo4j-image-and-testcontainers.html).
 
We use a Neo4jContainer and modified it to expose the Prometheus port.
To be able to use Prometheus with Neo4j we need to call the withEnterpriseEdition method,
and you have to add a container-license-acceptance.txt file to the root directory of your test resources, containing the text
"neo4j:3.5.0-enterprise" in one line. With this you will accept the license terms and conditions.
You'll find more information about licensing Neo4j here: [About Neo4j Licenses](https://neo4j.com/licensing/).
If you are interested in a license, you can contact us directly at [GraphAware](mailto:info@graphaware.com).
