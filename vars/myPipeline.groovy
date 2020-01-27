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
            stage('Unit Test') {
                steps{
                    script{
                        sh 'npm run cover'
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