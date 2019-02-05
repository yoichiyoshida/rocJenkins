/* ************************************************************************
 * Copyright 2019 Advanced Micro Devices, Inc.
 * ************************************************************************ */

package com.amd.project

//import com.amd.project.*
import com.amd.project.project_paths
//import com.amd.project.compiler_data

class rocProject implements Serializable
{
    def paths
    //compiler_data compiler
    String name
    
    String testDirectory = 'build/release'
    
    rocProject(String name)
    {
        this.name = name
        paths = new project_paths(
            project_name: name+'-ubuntu' )
        compiler = new compiler_data()
        
    }
}
