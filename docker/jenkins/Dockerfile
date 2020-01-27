FROM jenkins/jenkins:lts

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