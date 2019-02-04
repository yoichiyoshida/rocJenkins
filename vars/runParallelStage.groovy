/* ************************************************************************
 * Copyright 2018-2019 Advanced Micro Devices, Inc.
 * ************************************************************************ */

import com.amd.docker.rocDocker


import java.nio.file.Path;

def call(def dockerArray, Closure body)
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
                body()
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