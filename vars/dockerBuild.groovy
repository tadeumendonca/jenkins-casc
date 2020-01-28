def call(Map stageParams) { 
    def statusCode = sh label: 'Docker Build', returnStatus: true, script: "docker build -t ${stageParams.imageName} -t ${stageParams.appName}:latest ."
    // Check result
    if ( statusCode != 0 ){
        currentBuild.result = 'FAILURE'
        error('Failed to Docker Build')
    } else {
        currentBuild.result = 'SUCCESS'
    }
  }