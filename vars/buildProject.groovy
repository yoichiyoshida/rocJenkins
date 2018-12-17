import org.project.project_paths;
import org.project.docker_image;

def call(String nodeLogic, project_paths paths, rocDocker docker, Closure body)
{   
    node ( nodeLogic )
    {
        stage ("Checkout source code")
        {
            build.checkout(paths)
        }
        
        stage ("Build Docker Container")
        {
            // Create/reuse a docker image that represents the rocprim build environment
            docker.node = this.node
            docker.buildImage()
            // Print system information for the log
            docker.image.inside( docker_args.docker_run_args, docker_inside_closure )    
        }
    
    
    body()
    }

}
