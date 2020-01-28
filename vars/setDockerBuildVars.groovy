def call(Map stageParams) { 
    env.ARTIFACT_NAME="${stageParams.appName}-${stageParams.buildId}.tar.gz"
    env.IMAGE_NAME="${stageParams.appName}:${stageParams.buildId}"
    env.BUILD_CONTAINER_ID="docker_build_${stageParams.buildTimestamp}"

    echo "ARTIFACT_NAME=${ARTIFACT_NAME}"
    echo "IMAGE_NAME=${IMAGE_NAME}"
    echo "BUILD_CONTAINER_ID=${BUILD_CONTAINER_ID}"

    currentBuild.result = 'SUCCESS'
  }