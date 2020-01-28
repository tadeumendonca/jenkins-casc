def call(Map stageParams) { 
    input(message: "Promote to ${stageParams.environment}?", ok: 'Promote')
  }