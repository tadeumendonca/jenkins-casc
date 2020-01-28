def call(Map pipelineParams) {
    environment {
        IMAGE_NAME = ''
        BUILD_CONTAINER_ID = ''
    }
    pipeline {
        agent any
        options {
            skipDefaultCheckout(true)
            disableConcurrentBuilds()
            timeout(time: 1, unit: 'HOURS') 
        }
        stages {
            stage('Checkout Git') {
                steps{
                    gitCheckout()
                }
            }
            stage('Prepare Env') {
                steps {
                    loadProperties()
                    setDockerBuildVars(appName: "${pipelineParams.appName}")
                    verboseEnvVars()
                }
            }
            stage('Build') {
                steps{
                    dockerBuild(appName: "${env.IMAGE_NAME}")
                }
            }
            stage('Unit Test') {
                steps{
                    dockerNodeUnitTest(appName: "${env.IMAGE_NAME}")
                }
                post {
                    always {
                        step([$class: 'CoberturaPublisher', coberturaReportFile: 'coverage/cobertura-coverage.xml'])
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
            stage('Clean Items') {
                steps{
                    script{
                        sh "docker save ${env.IMAGE_NAME} | gzip -c > ${env.ARTIFACT_NAME}"
                        sh "docker image rm ${env.IMAGE_NAME}"
                        sh "docker rm ${env.BUILD_CONTAINER_ID}"
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