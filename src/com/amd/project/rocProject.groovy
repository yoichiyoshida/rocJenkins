/* ************************************************************************
 * Copyright 2019 Advanced Micro Devices, Inc.
 * ************************************************************************ */

package com.amd.project

import com.amd.project.*

// Paths variables bundled together to reduce parameter bloat on function calls
class rocProject implements Serializable
{
    project_paths paths
    compiler_data compile
    rocTests test
    rocPackage package
}
