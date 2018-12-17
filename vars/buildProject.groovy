/* ************************************************************************
 * Copyright 2018 Advanced Micro Devices, Inc.
 * ************************************************************************ */

import org.amd.project.*;
import org.amd.docker.rocDocker;

def call(String nodeLogic, project_paths paths, rocDocker docker, compiler_data hcc_compiler_args, Closure body)
{   
    node ( nodeLogic )
    {
        stage ("Checkout source code")
        {
            build.checkout(paths)
        }
        
        stage ("Build Docker Container")
        {
            // Build a docker image that represents the library build environment
            docker.buildImage(this)
        }
    
        stage ("Compile Library")
        {
        
        }
        
    body()
    }

}
