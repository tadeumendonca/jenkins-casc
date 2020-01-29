# Jenkins Configuration As Code
Jenkins CI docker image using Configuration As Code plugin setting up a Pipeline Library with sample application pipeline. The application code used in this example is available on [GitHub](https://github.com/tadeumendonca/express-app-testing-demo).

This main repository has been set up according to Jenkins Pipeline Library requirements, with following directories:
* **resources** - All files under resources are non-Groovy files. If necessary, resource files can be accessed through libraryResource step in pipelines.
* **src** - The src directory should look like standard Java source directory structure. This directory is added to the classpath when executing Pipelines.
* **vars** - The vars directory hosts script files that are exposed as a variable in Pipelines. The name of the file is the name of the variable in the Pipeline.

## Overview
This example implements the following architecture:
![CI Architecture](/resources/images/JenkinsConfigAsCode-Overview.jpg)

## Getting Started
To spin up the CI Server, just open resources/docker directory and execute the following script that will start your docker compose app.
> ./compose-up.sh