def call(Map pipelineParams) {
    // 
    pipeline {
        agent any
        agent { dockerfile true }
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
                        sh "docker run -p 3001:3000 -v \"${env.WORKSPACE}/coverage:/app/coverage\" express-app-testing-demo npm run test"
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