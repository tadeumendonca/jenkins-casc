def call(Map pipelineParams) {
    environment {
        IMAGE_NAME = ''
        BUILD_CONTAINER_ID = ''
    }
    pipeline {
        agent any
        options {
            skipDefaultCheckout(false)
            disableConcurrentBuilds()
            timeout(time: 1, unit: 'HOURS') 
        }
        stages {
            stage('Prepare Env') {
                steps {
                    loadProperties()
                    setDockerBuildVars(appName: "${pipelineParams.appName}")
                    verboseEnvVars()
                }
            }
            stage('Build') {
                steps{
                    dockerBuild(appName: "${pipelineParams.appName}", imageName: "${env.IMAGE_NAME}")
                }
            }
            stage('Unit Test') {
                steps{
                    dockerNodeUnitTest(imageName: "${env.IMAGE_NAME}")
                }
                post {
                    always {
                        step([$class: 'CoberturaPublisher', coberturaReportFile: 'coverage/cobertura-coverage.xml'])
                        sh "docker rm ${env.BUILD_CONTAINER_ID}"
                    }
                }
            }
            stage('Promote to STAGE?') {
                steps{
                    promoteEnv(environment: 'STAGE')
                }
            }
            stage('Deploy STAGE') {
                steps{
                    script{
                        sh "./compose-down.sh STAGE"
                        sh "./compose-up.sh STAGE"
                    }
                }
            }
            stage('Promote to PROD?') {
                steps{
                    promoteEnv(environment: 'PROD')
                }
            }
            stage('Deploy PROD') {
                steps{
                    script{
                        sh "./compose-down.sh PROD"
                        sh "./compose-up.sh PROD"
                    }
                }
            }
            stage('Clean Build Items') {
                steps{
                    script{
                        sh "docker save ${env.IMAGE_NAME} | gzip -c > ${env.ARTIFACT_NAME}"
                        dockerRemoveImages(appName: "${pipelineParams.appName}", imageName: "${env.IMAGE_NAME}")
                    }
                }
                post {
                    always {
                        archiveArtifacts artifacts: "${env.ARTIFACT_NAME}", fingerprint: true
                        sh "rm ${env.ARTIFACT_NAME}"
                    }
                }
            }
        }
    }
}