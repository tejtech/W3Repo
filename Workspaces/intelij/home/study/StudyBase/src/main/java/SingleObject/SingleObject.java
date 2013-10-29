package SingleObject;

/**
 * User: wakkir.muzammil
 * Date: 29/10/13
 * Time: 17:17
 */
public class SingleObject
{
    //create an object of SingleObject
    private static SingleObject instance = new SingleObject();
    private String message;

    //make the constructor private so that this class cannot be
    //instantiated
    private SingleObject()
    {

    }

    //Get the only object available
    public static SingleObject getInstance()
    {
        return instance;
    }

    public void showMessage()
    {
        System.out.println("Hello World! :"+message);
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

}
