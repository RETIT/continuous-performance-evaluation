cd %~dp0

REM Create network
docker network create --driver bridge demo_network

REM Create Jenkins container
cd jenkins
docker build -t demo_jenkins .
docker create -p 8080:8080 -p 50000:50000 -p 8787:8787 --name demo_jenkins --hostname demo_jenkins --network demo_network -v /var/run/docker.sock:/var/run/docker.sock -v /usr/local/bin/docker:/usr/bin/docker -v %~dp0:/data demo_jenkins

REM Create Slave container
cd ../docker-jenkins-slave
docker build -t demo_slave .
docker create --name demo_slave --network demo_network -v %~dp0:/data demo_slave

REM Create InspectIT CMR
docker create --name inspectIT-CMR --network demo_network -p 8182:8182 -p 9070:9070 -e BUFFER_SIZE=512 inspectit/cmr

REM Run Containers
docker start demo_jenkins
docker start inspectIT-CMR
timeout 25
docker start demo_slave