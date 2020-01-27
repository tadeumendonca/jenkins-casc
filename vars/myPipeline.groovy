def call(Map pipelineParams) {

    pipeline {
        agent any
        tools {nodejs "NodeJS"}
        stages {
            stage('Checkout Git') {
                steps{
                    gitCheckout()
                }
            }
            stage('Build') {
                steps{
                    script{
                        sh 'npm install'
                    }
                }
            }
            stage('Test') {
                steps{
                    script{
                        sh 'npm run cover'
                    }
                }
                post {
                    always {
                    publishHTML target: [
                        allowMissing         : false,
                        alwaysLinkToLastBuild: false,
                        keepAll             : true,
                        reportDir            : 'reports/coverage/lcov-report',
                        reportFiles          : 'index.html',
                        reportName           : 'Test Report'
                    ]
                    }
                }
            }
        }
    }
}