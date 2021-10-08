import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;

/**
 *
 * @author Yougeshwar Khatri
 */
public class ActivableClient {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException{
	System.setSecurityManager(new RMISecurityManager());
        
        RemoteCalculator stub = (RemoteCalculator) Naming.lookup("rmi://localhost:1099/calc");
        
        int add = stub.addition(10, 5);
        int sub = stub.subraction(10, 5);
        int multi = stub.multiplication(10, 5);
        int div = stub.division(10, 5);
        
        System.out.println("Addition : " + add);
        System.out.println("Subtraction : " + sub);
        System.out.println("Multiplication : " + multi);
        System.out.println("Division : " + div);
    }
}
