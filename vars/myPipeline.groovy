def call(Map pipelineParams) {
    environment {
        IMAGE_NAME = ''
        BUILD_CONTAINER_ID = ''
    }
    pipeline {
        agent any
        stages {
            stage('Set Build Vars') {
                agent { label 'master' }
                steps {
                    script{
                        env.ARTIFACT_NAME="${pipelineParams.appName}-${BUILD_ID}"
                        env.IMAGE_NAME="${pipelineParams.appName}:${BUILD_ID}"
                        env.BUILD_CONTAINER_ID="test_${BUILD_TIMESTAMP}"
                        echo "ARTIFACT_NAME=${env.ARTIFACT_NAME}"
                        echo "IMAGE_NAME=${env.IMAGE_NAME}"
                        echo "BUILD_CONTAINER_ID=${env.BUILD_CONTAINER_ID}"
                    }   
                }
            }
            stage('Checkout Git') {
                steps{
                    gitCheckout()
                }
            }
            stage('Build') {
                steps{
                    script{
                        sh "docker build -t ${env.IMAGE_NAME} ."
                    }
                }
            }
            stage('Unit Test') {
                steps{
                    script{
                        sh "docker run -p 3001:3000 --name ${env.BUILD_CONTAINER_ID} express-app-testing-demo npm run test"
                        sh "docker cp ${env.BUILD_CONTAINER_ID}:/app/coverage ${env.WORKSPACE}/coverage"
                    }
                }
                post {
                    always {
                        step([$class: 'CoberturaPublisher', coberturaReportFile: 'coverage/cobertura-coverage.xml'])
                    }
                }
            }
            stage('Remove Temp Items') {
                steps{
                    script{
                        sh "docker save ${env.IMAGE_NAME} | gzip -c > ${env.ARTIFACT_NAME}.tar.gz"
                        sh "docker image rm ${env.IMAGE_NAME}"
                        sh "docker rm ${env.BUILD_CONTAINER_ID}"
                    }
                }
                post {
                    always {
                        archiveArtifacts artifacts: '*.tar.gz', fingerprint: true
                    }
                }
            }
        }
    }
}