#!/bin/bash

docker stop demo_jenkins
docker rm demo_jenkins
docker stop demo_slave
docker rm demo_slave
docker stop inspectIT-CMR
docker rm inspectIT-CMR
docker network rm demo_network