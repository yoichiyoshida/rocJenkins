/* ************************************************************************
 * Copyright 2018-2019 Advanced Micro Devices, Inc.
 * ************************************************************************ */

import com.amd.project.*
import com.amd.docker.rocDocker


import java.nio.file.Path;

def call(project_paths paths, def dockerArray, compiler_data compiler_args, rocTests libTest, Closure body)
{
    script 
    {
        def platforms =[:]

        for (platform in dockerArray)
        {
            platforms[platform.jenkinsLabel] = platform
        }

        def action =
        { key ->
            def platform = platforms[key]

            node (platform.jenkinsLabel)
            {
                stage ("${platform.jenkinsLabel}") 
                {
                    body(platform)
                }
            }
        }

        actions = [:]
        for (platform in platforms)
        {
            actions[platform.key] = action.curry(platform.key)
        }

        parallel actions
    }
}