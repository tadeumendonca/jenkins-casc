FROM jenkins/jenkins:lts

# Installing Docker
USER root

RUN apt-get update && \
apt-get -y install apt-transport-https \
    ca-certificates \
    curl \
    gnupg2 \
    software-properties-common && \
curl -fsSL https://download.docker.com/linux/$(. /etc/os-release; echo "$ID")/gpg > /tmp/dkey; apt-key add /tmp/dkey && \
add-apt-repository \
    "deb [arch=amd64] https://download.docker.com/linux/$(. /etc/os-release; echo "$ID") \
    $(lsb_release -cs) \
    stable" && \
apt-get update && \
apt-get -y install docker-ce

RUN apt-get install -y docker-ce

COPY ./daemon.json /etc/docker/daemon.json

RUN usermod -a -G docker jenkins

USER jenkins

# Automate Jenkins Plugin Installation
COPY ./plugins.ini /usr/share/jenkins/plugins.ini
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/plugins.ini

ENV JENKINS_USER admin
ENV JENKINS_PASS admin

# Skip Jenkins Wizard on Startup
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false

COPY ./default-user.groovy /usr/share/jenkins/ref/init.groovy.d/

# Setup Jenkins Configuration As Code
ENV CASC_JENKINS_CONFIG=/usr/share/jenkins/jenkins.yaml
COPY ./jenkins.yaml /usr/share/jenkins/jenkins.yaml

# Jenkins Home Volume
VOLUME /var/jenkins_home