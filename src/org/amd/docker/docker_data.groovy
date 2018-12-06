/* ************************************************************************
 * Copyright 2018 Advanced Micro Devices, Inc.
 * ************************************************************************ */
package org.docker

// Docker related variables gathered together to reduce parameter bloat on function calls
class docker_data implements Serializable
{
    String from_image
    String build_docker_file
    String install_docker_file
    String docker_run_args
    String docker_build_args
}
