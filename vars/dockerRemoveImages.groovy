def call(Map stageParams) { 
    def imageList = sh label: 'Docker List Unused Images', returnStdout: true, script: "docker images --filter \"before=${stageParams.imageName}\" --format "{{.ID}}"  ${stageParams.appName}"
    
    for (image in imageList) {
        def statusCode = sh label: 'Remove Image', returnStatus: true, script: "docker image rm ${image}"
        
        // Check result
        if ( statusCode != 0 ){
            currentBuild.result = 'FAILURE'
            error('Failed to Remove Image')
        } 
    }
    
    currentBuild.result = 'SUCCESS'
  }