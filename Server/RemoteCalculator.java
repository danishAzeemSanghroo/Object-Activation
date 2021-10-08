import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Yougeshwar Khatri
 */
public interface RemoteCalculator extends Remote{
    public int addition(int a, int b)throws RemoteException;
    public int subraction(int a, int b)throws RemoteException;
    public int multiplication(int a, int b)throws RemoteException;
    public int division(int a, int b)throws RemoteException;
}