/* ************************************************************************
 * Copyright 2018 Advanced Micro Devices, Inc.
 * ************************************************************************ */
import org.project.project_paths;

////////////////////////////////////////////////////////////////////////
// -- BUILD RELATED FUNCTIONS

////////////////////////////////////////////////////////////////////////
// Checkout source code, source dependencies and update version number numbers
// Returns a relative path to the directory where the source exists in the workspace
void checkout_and_version( project_paths paths )
{
    paths.project_src_prefix = paths.src_prefix + '/' + paths.project_name

    dir( paths.project_src_prefix )
    {
	// checkout project
	checkout([
		$class: 'GitSCM',
		branches: scm.branches,
		doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
		extensions: scm.extensions + [[$class: 'CleanCheckout']],
		userRemoteConfigs: scm.userRemoteConfigs
	    ])
    }
}
