def call(Map stageParams) { 
    def statusCodeRun = sh label: 'Docker Run Unit Test', returnStatus: true, script: "docker run -p 3001:3000 --name ${env.BUILD_CONTAINER_ID} ${stageParams.imageName} npm run test"
    def statusCodeCp = sh label: 'Docker Run Unit Test', returnStatus: true, script: "docker cp ${env.BUILD_CONTAINER_ID}:/app/coverage ${env.WORKSPACE}/coverage"
    
    // Check results
    if ( statusCodeRun != 0 && statusCodeCp != 0){
        currentBuild.result = 'FAILURE'
        error('Failed to Run Unit Test')
    } else {
        currentBuild.result = 'SUCCESS'
    }
  }