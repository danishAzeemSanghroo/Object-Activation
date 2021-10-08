import java.rmi.*;
import java.rmi.activation.*;
import java.util.Properties;

public class Setup {
    public static void main(String[] args) throws Exception {
 	Properties props = System.getProperties(); 
        props.put("java.security.policy", "yougesh.policy");

	System.setSecurityManager(new RMISecurityManager());

        ActivationGroupDesc.CommandEnvironment ace = null; 
 	ActivationGroupDesc exampleGroup = new ActivationGroupDesc(props, ace);
 
        ActivationGroupID agi = ActivationGroup.getSystem().registerGroup(exampleGroup);

	String location = "file:/";

	MarshalledObject data = null;
       	ActivationDesc desc = new ActivationDesc(agi, "RemoteCalculatorImp", location, data);

	RemoteCalculator rc = (RemoteCalculator)Activatable.register(desc);
	System.out.println("Got the stub for RemoteCalculatorImp");
       
	Naming.rebind("calc", rc);
	System.out.println("Exported from registration");

	System.exit(0);
    }
}
