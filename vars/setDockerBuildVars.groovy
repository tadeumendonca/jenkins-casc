def call(Map stageParams) { 
    def ARTIFACT_NAME="${stageParams.appName}-${stageParams.buildId}.tar.gz"
    def IMAGE_NAME="${stageParams.appName}:${stageParams.buildId}"
    def BUILD_CONTAINER_ID="docker_build_${stageParams.buildTimestamp}"

    echo "ARTIFACT_NAME=${ARTIFACT_NAME}"
    echo "IMAGE_NAME=${IMAGE_NAME}"
    echo "BUILD_CONTAINER_ID=${BUILD_CONTAINER_ID}"

    currentBuild.result = 'SUCCESS'
  }