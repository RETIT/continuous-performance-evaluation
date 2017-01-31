# Continuous Performance Evaluation

This project contains an example Jenkins pipeline for continuous performance evaluations. All parts of the demo
(except for the InspectIT UI) are defined as Docker containers. Therefore, Docker is required in order to run
the demo. It comes with the following components:

* Pre-configured Jenkins
* A Jenkins slave running in separate Docker container
* InspectIT Central Measurement Repository (CMR)
* A sample application (Cargo Tracker), running on WildFly 10 with PostgreSQL in the background

The Jenkins and InspectIT Docker containers are built and started when the script `startContainers`
(.bat/.sh) is executed. Similarly, they are stopped and removed when `stopContainers` (.bat/.sh) is run. The
containers for the application (WildFly and PostgreSQL) are only created when the pipeline is run. The sample
application is then accessible at [http://localhost:8081/cargo-tracker](http://localhost:8081/cargo-tracker).

By default, the demo Jenkins runs on [http://localhost:8080](http://localhost:8080). It already has a job
configured which can be run in order to start the pipeline. The InspectIT CMR is also started with its
standard ports 8182 and 9070.

In order to access the data of the CMR, the InspectIT UI is required, which needs to be
[downloaded](https://github.com/inspectIT/inspectIT/releases) separately.