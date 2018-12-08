def call(String nodeLogic, Closure body)
{
    node ( nodeLogic )
    {
	body()
    }

}
