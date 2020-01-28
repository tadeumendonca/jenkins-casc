def call(Map stageParams) { 
    def props = readProperties file: 'jenkins.properties'

    for(p in props){
        env.p = "${p}"
    }
  }