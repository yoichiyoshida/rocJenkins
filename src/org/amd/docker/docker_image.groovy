/* ************************************************************************
 * Copyright 2018 Advanced Micro Devices, Inc.
 * ************************************************************************ */
package org.docker
import org.project.*
import org.docker.*
import java.nio.file.Path;
import org.jenkinsci.plugins.docker.workflow.*


class docker_image implements Serializable
{
    def image
    def script
    String build_image_name
    
    docker_image(def script, docker_data docker_args, project_paths paths)
    {
	this.script = script
	build_image_name = "build-rocblas-hip-artifactory"
    }
/*	
	script {
	dir( paths.project_src_prefix )
	{
	    def user_uid = sh( script: 'id -u', returnStdout: true ).trim()

	    // Docker 17.05 introduced the ability to use ARG values in FROM statements
	    // Docker inspect failing on FROM statements with ARG https://issues.jenkins-ci.org/browse/JENKINS-44836
	    // build_image = docker.build( "${paths.project_name}/${build_image_name}:latest", "--pull -f docker/${build_docker_file} --build-arg user_uid=${user_uid} --build-arg base_image=${from_image} ." )

	    // JENKINS-44836 workaround by using a bash script instead of docker.build()
	    sh "docker build -t ${paths.project_name}/${build_image_name}:latest -f docker/${docker_args.build_docker_file} ${docker_args.docker_build_args} --build-arg user_uid=${user_uid} --build-arg base_image=${docker_args.from_image} ."
	    image = docker.image( "${paths.project_name}/${build_image_name}:latest" )
	    }

	}
*/
    
}
