/* ************************************************************************
 * Copyright 2018-2019 Advanced Micro Devices, Inc.
 * ************************************************************************ */

import com.amd.project.*
import com.amd.docker.rocDocker

def call(String nodeLogic, boolean runFormatCheck, boolean buildPackage, project_paths paths, rocDocker docker, compiler_data hcc_compiler_args, rocTests libTest, Closure body)
{   
    node ( nodeLogic )
    {
        echo "Starting Jenkins Job"
        
        stage ("Checkout source code")
        {
            def print_version_closure = {
              sh  """
                  set -x
                  /opt/rocm/bin/hcc --version
                  pwd
                  dkms status
              whoami
              id
                """
            }        
            build.checkout(paths)
        }

        stage ("Build Docker Container")
        {
            // Build a docker image that represents the library build environment
            docker.buildImage(this)
        }

        if (runFormatCheck)
        {
            stage ("Format Check")
            {
		command = """hostname"""
		docker.runCommand(command)
            }
        }
        
        stage ("Compile Library")
        {
            paths.construct_build_prefix()
            docker.image.inside(docker.runArgs)
            {
                withEnv(["CXX=${hcc_compiler_args.compiler_path}", 'CLICOLOR_FORCE=1'])
                {
                  // Build library & clients
                  sh  """#!/usr/bin/env bash
                      set -x
                      cd ${paths.project_build_prefix}
                      LD_LIBRARY_PATH=/opt/rocm/hcc/lib ${paths.build_command}
                    """
                }                
            }
        }

	stage ("Test Library")
	{
	    docker.image.inside(docker.runArgs)
	    {
	    // Cap the maximum amount of testing to be a few hours; assume failure if the time limit is hit
            timeout(time: 4, unit: 'HOURS')
            {
                
             sh """#!/usr/bin/env bash
                set -x
                cd ${paths.project_build_prefix}
                cd ${libTest.testDirectory}
                LD_LIBRARY_PATH=/opt/rocm/hcc/lib ${libTest.testCommand}
            """
		    
	    }
        }
    }

    if (buildPackage)
    {
        stage ("Build Package")
        {
            docker.image.inside(docker.runArgs)
            {
            withEnv(["CXX=${hcc_compiler_args.compiler_path}", 'CLICOLOR_FORCE=1'])
            {
                String docker_context = "${compiler_args.build_config}/${compiler_args.compiler_name}"
                if( compiler_args.compiler_name.toLowerCase( ).startsWith( 'hcc-' ) )
                {
                    sh  """#!/usr/bin/env bash
                        set -x
                        cd ${paths.project_build_prefix}/build/release
                        make package
                      """

                    if( paths.project_name.equalsIgnoreCase( 'rocblas-ubuntu' ) )
                    {
                      sh  """#!/usr/bin/env bash
                          set -x
                          rm -rf ${docker_context} && mkdir -p ${docker_context}
                          mv ${paths.project_build_prefix}/build/release/*.deb ${docker_context}
                          dpkg -c ${docker_context}/*.deb
                      """
                      archiveArtifacts artifacts: "${docker_context}/*.deb", fingerprint: true
                    }
                    else if( paths.project_name.equalsIgnoreCase( 'rocblas-fedora' ) )
                    {
                      sh  """#!/usr/bin/env bash
                          set -x
                          rm -rf ${docker_context} && mkdir -p ${docker_context}
                          mv ${paths.project_build_prefix}/build/release/*.rpm ${docker_context}
                          rpm -qlp ${docker_context}/*.rpm
                      """
                      archiveArtifacts artifacts: "${docker_context}/*.rpm", fingerprint: true
                    }
                }

            }
        }
    }

	body()
    }

 
    
}

