/* ************************************************************************
 * Copyright 2018 Advanced Micro Devices, Inc.
 * ************************************************************************ */

import org.amd.project.*
import org.amd.docker.rocDocker

def call(String nodeLogic, project_paths paths, rocDocker docker1, compiler_data hcc_compiler_args, Closure body)
{   
    node ( nodeLogic )
    {
        
        body()
    }

}

