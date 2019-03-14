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

<<<<<<< HEAD
    dockerNodes(def jenkinsGPULabels = ['gfx900'], String rocmVersion = 'rocm20', project_paths paths)
=======
    dockerNodes(def jenkinsGPULabels = ['gfx900'], String rocmVersion = 'rocm22', rocProject prj)
>>>>>>> develop
    {
        dockerArray = [:]
        jenkinsGPULabels.each
        {
            dockerArray[it] = new rocDocker(
<<<<<<< HEAD
                            buildImageName:'build-rocprim-hip-artifactory',
                            paths: paths,
=======
                            buildImageName:'build-' + prj.name + '-artifactory',
                            paths: prj.paths,
>>>>>>> develop
                            jenkinsLabel: it + " && " + rocmVersion
                        )
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> develop
