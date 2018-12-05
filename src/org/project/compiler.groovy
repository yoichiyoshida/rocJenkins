/* ************************************************************************
 * Copyright 2018 Advanced Micro Devices, Inc.
 * ************************************************************************ */

package org.project

// Docker related variables gathered together to reduce parameter bloat on function calls
class compiler_data implements Serializable
{
    String compiler_name
    String build_config
    String compiler_path
}
