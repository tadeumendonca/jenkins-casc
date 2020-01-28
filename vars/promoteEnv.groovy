def call(Map stageParams) { 
    def statusCode = input "Promote to ${environment}?"
    // Check result
    if ( statusCode != 0 ){
        currentBuild.result = 'FAILURE'
        error('Failed to Promote Env')
    } else {
        currentBuild.result = 'SUCCESS'
    }
  }