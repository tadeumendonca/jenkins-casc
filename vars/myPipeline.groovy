def call(Map pipelineParams) {
    environment {
        IMAGE_NAME = "${pipelineParams.appName}:${BUILD_ID}"
        BUILD_CONTAINER_ID = "test_${BUILD_TIMESTAMP}"
    }
    pipeline {
        agent any
        stages {
            stage('Verbose Env Vars') {
                agent { label 'master' }
                steps {
                    script{
                        sh "printenv | sort"
                        echo "${env.IMAGE_NAME}"
                        echo "${env.BUILD_CONTAINER_ID}"
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
                        sh "docker rm ${env.BUILD_CONTAINER_ID}"
                    }
                }
                post {
                    always {
                    step([$class: 'CoberturaPublisher', coberturaReportFile: 'coverage/cobertura-coverage.xml'])
                    }
                }
            }
        }
    }
}