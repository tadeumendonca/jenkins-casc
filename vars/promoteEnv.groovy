def call(Map stageParams) { 
    def statusCode = input message: "Promote to ${stageParams.environment}?", ok: 'true'

    // Check result
    if ( statusCode != 'true'){
        currentBuild.result = 'FAILURE'
        error('Failed to Promote Env')
    } else {
        currentBuild.result = 'SUCCESS'
    }
  }