def call(Map stageParams) { 
    def imageListString = sh label: 'Docker List Unused Images', returnStdout: true, script: "docker images --filter \"before=${stageParams.imageName}\" --format \"{{.ID}}\"  ${stageParams.appName}"
    
    def list = []

    imageListString.split("\n").each {image ->
        if(image != ""){
            def statusCode = sh label: 'Remove Image', returnStatus: true, script: "docker image rm -f ${image}"
        
            // Check result
            if ( statusCode != 0 ){
                currentBuild.result = 'FAILURE'
                error('Failed to Remove Image')
            } 
        }
    }
    currentBuild.result = 'SUCCESS'
  }