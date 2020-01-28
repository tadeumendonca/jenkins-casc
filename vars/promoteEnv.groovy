def call(Map stageParams) { 
    def statusCode = input message: "Promote to ${stageParams.environment}?", ok: 0

    echo "${statusCode}"
    // Check result
    if ( statusCode != 0 ){
        currentBuild.result = 'FAILURE'
        error('Failed to Promote Env')
    } else {
        currentBuild.result = 'SUCCESS'
    }
  }