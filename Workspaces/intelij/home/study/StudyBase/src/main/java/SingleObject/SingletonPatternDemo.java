package SingleObject;

/**
 * User: wakkir.muzammil
 * Date: 29/10/13
 * Time: 17:19
 */
public class SingletonPatternDemo
{
    public static void main(String[] args)
    {

        //illegal construct
        //Compile Time Error: The constructor SingleObject() is not visible
        //SingleObject object = new SingleObject();

        //Get the only object available
        SingleObject object = SingleObject.getInstance();

        //show the message
        object.showMessage();

        //show the message
        object.setMessage("DDD");

        //show the message
        object.showMessage();

        //Get the only object available
        SingleObject object1 = SingleObject.getInstance();

        //show the message
        object1.showMessage();
    }
}
