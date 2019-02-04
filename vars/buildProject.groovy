/* ************************************************************************
 * Copyright 2018-2019 Advanced Micro Devices, Inc.
 * ************************************************************************ */

import com.amd.project.*
import com.amd.docker.rocDocker


import java.nio.file.Path;

def call(boolean runFormatCheck, boolean buildPackage, project_paths paths, def dockerArray, compiler_data compiler_args, rocTests libTest, Closure body)
{
     pipeline
    {
        agent { label "master"}

         stages
        {
            stage ("Build Docker Container")
            {
                steps
                {
                    script
                    {
                        def dummy
                        runParallelStage(paths, dockerArray, compiler_args, libTest, dummy)
                        {
                            platform ->
                            build.checkout(paths)
                            platform.buildImage(this)
                        }
                    }
                }
            }

            stage ("Compile")
            {
                steps
                {
                    script
                    {
                        def compile = {
                            paths.construct_build_prefix()
                            def command = """#!/usr/bin/env bash
                                      set -x
                                      cd ${paths.project_build_prefix}
                                      LD_LIBRARY_PATH=/opt/rocm/hcc/lib CXX=${compiler_args.compiler_path} ${paths.build_command}
                                    """
                            platform.runCommand(this, command)
                        }
                        
                        runParallelStage(paths, dockerArray, compiler_args, libTest, runCode)
                        {
                            platform ->
                            runCode
                            /*paths.construct_build_prefix()
                            def command = """#!/usr/bin/env bash
                                      set -x
                                      cd ${paths.project_build_prefix}
                                      LD_LIBRARY_PATH=/opt/rocm/hcc/lib CXX=${compiler_args.compiler_path} ${paths.build_command}
                                    """
                            */
                            //platform.runCommand(this, command)
                        }
                    }
                }
            }
        }
    }
/*             stage ("Compile Library")
            {
                parallel
                {
                    stage ("gfx900")
                    {
                        agent
                        {
                            label "gfx900"
                        }
                        steps
                        {
                            script
                            {
                                paths.construct_build_prefix()
                                def command = """#!/usr/bin/env bash
                                          set -x
                                          cd ${paths.project_build_prefix}
                                          LD_LIBRARY_PATH=/opt/rocm/hcc/lib CXX=${compiler_args.compiler_path} ${paths.build_command}
                                        """

                                docker.runCommand(this, command)
                            }
                        }
                    }
                }
            }
            stage ("Test Library")
            {
                parallel
                {
                    stage ("gfx900")
                    {
                        agent
                        {
                            label "gfx900"
                        }
                        steps
                        {
                            script
                            {
                                def command = """#!/usr/bin/env bash
                                                    set -x
                                                    cd ${paths.project_build_prefix}
                                                    cd ${libTest.testDirectory}
                                                    LD_LIBRARY_PATH=/opt/rocm/hcc/lib ${libTest.testCommand}
                                              """
                                timeout(time: 4, unit: 'HOURS')
                                {
                                    docker.runCommand(this, command)
                                }
                            }
                        }
                    }
                }
            }
            stage ("Build Package")
                {
                    parallel
                    {
                        stage ("gfx900")
                        {
                            agent
                            {
                                label "gfx900"
                            }
                            steps
                            {
                                script
                                {
                                    String docker_context = "${compiler_args.build_config}/${compiler_args.compiler_name}"
                                    def command = """#!/usr/bin/env bash
                                                        set -x
                                                        cd ${paths.project_build_prefix}
                                                        cd ${libTest.testDirectory}
                                                        LD_LIBRARY_PATH=/opt/rocm/hcc/lib ${libTest.testCommand}
                                                  """
                                    timeout(time: 4, unit: 'HOURS')
                                    {
                                        docker.runCommand(this, command)
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }  */

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
            def command = """#!/usr/bin/env bash
                      set -x
                      cd ${paths.project_build_prefix}
                      LD_LIBRARY_PATH=/opt/rocm/hcc/lib CXX=${compiler_args.compiler_path} ${paths.build_command}
                    """

            docker.runCommand(this, command)

            docker.image.inside(docker.runArgs)
            {
                  // Build library & clients
                  sh  """#!/usr/bin/env bash
                      set -x
                      cd ${paths.project_build_prefix}
                      LD_LIBRARY_PATH=/opt/rocm/hcc/lib CXX=${compiler_args.compiler_path} ${paths.build_command}
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