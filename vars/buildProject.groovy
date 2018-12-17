import org.amd.project.project_paths;
import org.amd.docker.rocDocker;

def call(String nodeLogic, project_paths paths, rocDocker docker, Closure body)
{   
    node ( nodeLogic )
    {
        stage ("Checkout source code")
        {
            echo "Saa"
            build.checkout(paths)
        }
        
        stage ("Build Docker Container")
        {
            echo "Boo"
            // Create/reuse a docker image that represents the rocprim build environment
            docker.node = stage
            docker.buildImage()
            // Print system information for the log
            docker.image.inside( docker_args.docker_run_args, docker_inside_closure )    
        }
    
    
    body()
    }

}
