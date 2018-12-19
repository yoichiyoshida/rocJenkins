/* ************************************************************************
 * Copyright 2018 Advanced Micro Devices, Inc.
 * ************************************************************************ */

import com.amd.project.*
import com.amd.docker.rocDocker

def call(String nodeLogic, project_paths paths, rocDocker docker, compiler_data hcc_compiler_args, Closure body)
{   
    node ( nodeLogic )
    {
        echo "Starting Jenkins Job"
        
        stage ("Checkout source code")
        {
            build.checkout(paths)
        }
        
        body()
    }

}

