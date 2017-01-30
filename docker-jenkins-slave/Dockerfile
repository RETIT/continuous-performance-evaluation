FROM jenkinsci/slave:latest

# Download and install maven
USER root
RUN wget --no-verbose -O /tmp/apache-maven-3.3.9-bin.tar.gz http://archive.apache.org/dist/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz
RUN tar xzf /tmp/apache-maven-3.3.9-bin.tar.gz -C /opt/ \
	&& ln -s /opt/apache-maven-3.3.9 /opt/maven \
	&& ln -s /opt/maven/bin/mvn /usr/local/bin \
	&& rm -f /tmp/apache-maven-3.3.9-bin.tar.gz
ENV MAVEN_HOME /opt/maven

COPY jenkins-slave /usr/local/bin/jenkins-slave

ENTRYPOINT ["jenkins-slave"]