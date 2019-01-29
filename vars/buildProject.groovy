/* ************************************************************************
 * Copyright 2018-2019 Advanced Micro Devices, Inc.
 * ************************************************************************ */

import com.amd.project.*
import com.amd.docker.rocDocker

def call(String nodeLogic, boolean runFormatCheck, boolean buildPackage, project_paths paths, rocDocker docker, compiler_data compiler_args, rocTests libTest, Closure body)
{
pipeline{
    agent none
    //node ( nodeLogic )
    
    stages
    {
        stage ("Building on gfx 900 and 906")
        {
            parallel 
            {
                stage ("gfx900")
                {
                    agent 
                    {
                        label "rocm20" 
                    }
                }
                stages
                {
                    stage ("Checkout source code")
                    {
                        steps 
                        {
                            script
                            {
                                build.checkout(paths)
                            }
                        }
                    }
                    
                    stage ("Build Docker Container")
                    {
                        steps 
                        {
                            script
                            {
                                docker.buildImage(this)
                            }
                        }
                    }
                }
            }
                    
        }
    }
        
/*     stages{
        
        stage ("Checkout source code")
        {
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
                def command = """
                            hostname
                            echo "Format check disabled"
                            """
                docker.runCommand(this, command)
                stage ("testing")
                {
                def command1 = """
                            hostname
                            echo "Format check disabled"
                            """
                docker.runCommand(this, command1)                
                }
            }
        }

        stage ("Compile Library")
        {
            paths.construct_build_prefix()
            docker.image.inside(docker.runArgs)
            {
                withEnv(["CXX=${compiler_args.compiler_path}", 'CLICOLOR_FORCE=1'])
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
                    withEnv(["CXX=${compiler_args.compiler_path}", 'CLICOLOR_FORCE=1'])
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
        }
    } */
       


}