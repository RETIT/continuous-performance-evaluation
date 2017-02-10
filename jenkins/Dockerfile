FROM jenkins:2.32.2

# Install sudo so we can access docker on host
USER root
RUN apt-get update \
	&& apt-get install -y sudo \
	&& rm -rf /var/lib/apt/lists/*
RUN echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers

# Download and install maven
RUN wget --no-verbose -O /tmp/apache-maven-3.3.9-bin.tar.gz http://archive.apache.org/dist/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz
RUN tar xzf /tmp/apache-maven-3.3.9-bin.tar.gz -C /opt/ \
	&& ln -s /opt/apache-maven-3.3.9 /opt/maven \
	&& ln -s /opt/maven/bin/mvn /usr/local/bin \
	&& rm -f /tmp/apache-maven-3.3.9-bin.tar.gz
ENV MAVEN_HOME /opt/maven

# Add plugins
USER jenkins
RUN /usr/local/bin/install-plugins.sh docker-plugin docker-commons docker-workflow \
	matrix-auth \
	pipeline-build-step pipeline-graph-analysis pipeline-input-step pipeline-milestone-step \
	pipeline-model-api pipeline-model-declarative-agent pipeline-model-definition pipeline-rest-api \
	pipeline-stage-step pipeline-stage-tags-metadata pipeline-stage-view pipeline-utility-steps

# Add script approval
ADD scriptApproval.xml /var/jenkins_home/

# Add config for docker-slave
ADD nodes/ /var/jenkins_home/nodes/
	
# Add performance test job
ADD jobs/ /var/jenkins_home/jobs/

# Install performance plugin manually because of bugs in pre-built version
ADD plugins/ /usr/share/jenkins/ref/plugins/

USER root
RUN chown -R jenkins /var/jenkins_home/jobs/* \
	&& chgrp -R jenkins /var/jenkins_home/jobs/*

ENV JAVA_OPTS -Xmx512m
	
EXPOSE 8787