def call(Map stageParams) { 
    for(e in env){
        echo e + "=" + ${e}
    }
  }