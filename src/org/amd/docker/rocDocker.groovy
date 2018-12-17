/* ************************************************************************
 * Copyright 2018 Advanced Micro Devices, Inc.
 * ************************************************************************ */
package org.amd.docker

// Docker related variables gathered together to reduce parameter bloat on function calls
class rocDocker implements Serializable
{
    String baseImage
    String buildDockerfile
    String installDockerfile
    String runArgs
    String buildArgs
    String buildImageName
    
    //def image
    def stage
    def paths
    
    void buildImage(def stage)
    {
//	    echo "Saad"
	    stage.dir( paths.project_src_prefix )
	    {
		def user_uid = stage.sh(script: 'id -u', returnStdout: true ).trim()
		
		// Docker 17.05 introduced the ability to use ARG values in FROM statements
		// Docker inspect failing on FROM statements with ARG https://issues.jenkins-ci.org/browse/JENKINS-44836
		// build_image = docker.build( "${paths.project_name}/${build_image_name}:latest", "--pull -f docker/${build_docker_file} --build-arg user_uid=${user_uid} --build-arg base_image=${from_image} ." )
	    
		// JENKINS-44836 workaround by using a bash script instead of docker.build()
		stage.sh "docker build -t ${paths.project_name}/${buildImageName}:latest -f docker/${docker_args.build_docker_file} ${buildArgs} --build-arg user_uid=${user_uid} --build-arg base_image=${baseImage} ."
		image = stage.docker.image( "${paths.project_name}/${buildImageName}:latest" )
	    }
//	}
    }

/*    
    UploadDockerHub(String RemoteOrg)
    {
	// Do not treat failures to push to docker-hub as a build fail
	try
	{
	    sh  """#!/usr/bin/env bash
          set -x
          echo inside sh
          docker tag ${local_org}/${image_name} ${remote_org}/${image_name}
        """

	    docker_hub_image = docker.image( "${remote_org}/${image_name}" )

	    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-cred' )
	    {
		docker_hub_image.push( "${env.BUILD_NUMBER}" )
		docker_hub_image.push( 'latest' )
	    }
	}
	catch( err )
	{
	    currentBuild.result = 'SUCCESS'
	}
    }
*/
}
