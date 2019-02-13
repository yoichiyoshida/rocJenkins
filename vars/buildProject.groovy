/* ************************************************************************
 * Copyright 2018-2019 Advanced Micro Devices, Inc.
 * ************************************************************************ */

import com.amd.project.*
import com.amd.docker.rocDocker
import java.nio.file.Path;

def call(rocProject project, def dockerArray, def compileCommand, def testCommand, def packageCommand)
{
    def action =
    { key ->
        def platform = dockerArray[key]

        node (platform.jenkinsLabel)
        {
            stage ("${platform.jenkinsLabel}" + "Docker") 
            {
                build.checkout(project.paths)
                platform.buildImage(this)
            }
        }
    }

    actions = [:]
    for (platform in dockerArray)
    {
        actions[platform.key] = action.curry(platform.key)
    }

    parallel actions
/*    pipeline
    {
        agent { label "master"}

        stages
        {
            stage ("Docker")
            {
                steps
                {
                    script
                    {
                        runParallelStage(project, dockerArray)
                        {
                            platform, runCommand ->
                            build.checkout(project.paths)
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
                        runParallelStage(project, dockerArray, compileCommand)
                        {
                            platform, runCommand ->
                            runCommand.call(platform,project)
                        }
                    }
                }
            }

            stage ("Test")
            {
                steps
                {
                    script
                    {
                        runParallelStage(project, dockerArray, testCommand)
                        {
                            platform, runCommand ->
                            runCommand.call(platform,project)
                        }
                    }
                }
            }

            stage ("Package")
            {
                steps
                {
                    script
                    {
                        runParallelStage(project, dockerArray, packageCommand)
                        {
                            platform, runCommand ->
                            runCommand.call(platform, project)
                        }
                    }
                }
            }
        }
    }
*/
}