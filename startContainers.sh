#!/bin/bash

directory="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $directory

# Create network
docker network create --driver bridge demo_network

# Create Jenkins container
cd jenkins
docker build -t demo_jenkins .
docker create -p 8080:8080 -p 50000:50000 -p 8787:8787 --name demo_jenkins --hostname demo_jenkins --network demo_network -v /var/run/docker.sock:/var/run/docker.sock -v /usr/bin/docker:/usr/bin/docker -v $directory:/data demo_jenkins

# Create Slave container
cd ../docker-jenkins-slave
docker build -t demo_slave .
docker create --name demo_slave --network demo_network -v $directory:/data demo_slave

# Create InspectIT CMR
docker create --name inspectIT-CMR --network demo_network -p 8182:8182 -p 9070:9070 inspectit/cmr

# Run Containers
docker start demo_jenkins
docker start inspectIT-CMR
sleep 25
docker start demo_slave