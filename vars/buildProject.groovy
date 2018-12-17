import org.project.project_paths;

def call(String nodeLogic, project_paths paths, Closure body)
{
    node ( nodeLogic )
    {
	stage ("Checkout source code")
	{
	    build.checkout(paths)
	}
	body()
    }

}
