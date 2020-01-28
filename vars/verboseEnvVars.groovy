def call(Map stageParams) { 
    sh(script: 'env|sort', returnStdout: true)
  }