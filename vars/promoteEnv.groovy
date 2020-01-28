def call(Map stageParams) { 
    def statusCode = input(message: "Promote to ${stageParams.environment}?", ok: 'Proceed')

    // Check result
    if ( statusCode != 'Proceed'){
        currentBuild.result = 'FAILURE'
        error('Failed to Promote Env')
    } else {
        currentBuild.result = 'SUCCESS'
    }
  }