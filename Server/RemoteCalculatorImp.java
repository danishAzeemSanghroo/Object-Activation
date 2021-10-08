import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationID;

/**
 * @author Yougeshwar Khatri
 */
public class RemoteCalculatorImp extends Activatable implements RemoteCalculator{
    public RemoteCalculatorImp(ActivationID id, MarshalledObject data) throws RemoteException{
        super(id, 0);
    }

    public int addition(int a, int b)throws RemoteException{
        return a + b;
    }
    public int subraction(int a, int b)throws RemoteException{
        return a - b;
    }
    public int multiplication(int a, int b)throws RemoteException{
        return a * b;
    }
    public int division(int a, int b)throws RemoteException{
        return a / b;
    }
}