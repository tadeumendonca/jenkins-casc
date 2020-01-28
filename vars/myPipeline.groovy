def call(Map pipelineParams) {
    // 
    pipeline {
        agent any
        stages {
            stage('Checkout Git') {
                steps{
                    gitCheckout()
                }
            }
            stage('Build') {
                steps{
                    script{
                        sh "docker build -t ${pipelineParams.appName} ."
                    }
                }
            }
            stage('Unit Test') {
                steps{
                    script{
                        sh "docker run -p 3001:3000 --name ephemeral_test express-app-testing-demo npm run test && docker cp ephemeral_test:/app/coverage ${env.WORKSPACE}/coverage && docker rm ephemeral_test"
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