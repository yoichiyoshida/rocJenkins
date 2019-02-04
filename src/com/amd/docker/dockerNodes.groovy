/* ************************************************************************
 * Copyright 2018-2019 Advanced Micro Devices, Inc.
 * ************************************************************************ */
package com.amd.docker

import com.amd.project.*
import com.amd.docker.rocDocker


import java.nio.file.Path;

class dockerNodes implements Serializable
{
    def dockerArray
    
    dockerNodes(def jenkinsLabels = ['gfx900'], project_paths paths)
    {
        dockerArray = [:]
        jenkinsLabels.each
        {
            dockerArray[it] = new rocDocker(
                            buildImageName:'build-rocprim-hip-artifactory',
                            paths: paths,
                            jenkinsLabel: it
                        )
        }
    }
}