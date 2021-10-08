# Object-Activation
What is an activatable remote object?
=====================================
An activatable remote object is a remote object that starts
executing when its remote methods are invoked and shuts itself
down when necessary.

How to create an activatable object?
====================================
If a class extends java.rmi.activation.Activatable class and 
has a constructor to accept ActivationID and MarshalledObject
parameters, that class is an activatable object.

Step 1:
-------
Create "RemoteCalculator.java" in C:\ObjectActivation\Server\


import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 */
public interface RemoteCalculator extends Remote{
    public int addition(int a, int b)throws RemoteException;
    public int subraction(int a, int b)throws RemoteException;
    public int multiplication(int a, int b)throws RemoteException;
    public int division(int a, int b)throws RemoteException;
}


Step 2:
-------
Create "RemoteCalculatorImp.java" in C:\ObjectActivation\Server\

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

Description
===========
I used RemoteCalculatorImp as the remote object and let it extend Activatable class
which is required. Based on the RMI specification, I declared a two-argument
constructor passing ActivationID to register the object with the activation system
and a MarshalledObject.. This constructor is required and should throw RemoteException.
The super(id, 0) method calls Activatable constructor to pass an activation ID and a port number. 
In this case, the port number is default 1099. 

Step 3:
-------
Create "Setup.java" in C:\ObjectActivation\Server\

/**
 * 
 */
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

Decription
==========
According to the RMI specification, to make a remote object accessible via an activation identifier over time,
you need to register an activation descriptor for the remote object and include a special constructor that the
RMI system calls when it activates the activatable object.

The following classes are involved with the activation process:

    1. ActivationGroup class -- responsible for creating new instances of activatable objects in its group.
    2. ActivationGroupDesc class -- contains the information necessary to create or re-create an activation group in which to activate 
in the same JVM.
    3. ActivationGroupDesc.CommandEnvironment class -- allows overriding default system properties and specifying implementation-defined options for an ActivationGroup.
    4. ActivationGroupID class -- identifies the group uniquely within the activation system and contains a reference to the group's activation system.
       - getSystem()-- returns an ActivationSystem interface implementation class. 
    5. MarshalledObject class -- a container for an object that allows that object to be passed as a paramter in an RMI call. 

How to create a set-up program
==============================
The purpose of the set-up program is to register information about activable object with rmid and the rmiregistry. It takes the following steps:

    1. Install security manager for the ActivationGroup VM.
    2. Set security policy
    3. Create an instance of ActivationGroupDesc class
    4. Register the instance and get an ActivationGroupID.
    5. Create an instance of ActivationDesc.
    6. Register the instance with rmid.
    7. Bind or rebind the remote object instance with its name
    8. Exit the system.

Step 4:
=======
Create "ActivableClient.java" in C:\ObjectActivation\Client\

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;

/**
 *
 * 
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

Description
===========
Now we are ready to test the functionality of the activatable object. Note that you don't
need to change the existing client class to test RMI server. Since we changed the names of
the remote interface and the remote object, we need to make changes accordingly for the existing class.
The alteration has been highlighted.

Step 5:
=======
Create "yougesh.policy" in C:\ObjectActivation\Client\ & C:\ObjectActivation\Server\

grant {
  Permission java.security.AllPermission;
};

Description
===========
Now we have finished 4 classes. In order to run the code successfully, we need a policy file.
The following policy file is for all permissions. For specific permission, you need to consult
related documentation.


Please follow the following steps to compile and run.
Compile all classes in C:\ObjectActivation\Server\

Step 6:
=======
C:\ObjectActivation\Server\javac *.java
C:\ObjectActivation\Server\rmic RemoteCalculatorImp
C:\ObjectActivation\Server\start rmiregistry
C:\ObjectActivation\Server\start rmid -J-Djava.security.policy=yougesh.policy
C:\ObjectActivation\Server\java Setup

Step 7:
=======
Copy the stub file (RemoteCalculatorImp_Stub.class) & RemoteCalculator.class file into C:\ObjectActivation\Client\

Compile all classes in C:\ObjectActivation\Client\

Step 8:
=======
C:\ObjectActivation\Client\javac *.java
C:\ObjectActivation\Client\java -Djava.security.policy=yougesh.policy ActivableClient

==>Check your skill<==
Excecute above programs.
Make the simple Hello program activable by following the instruction given above. 

Created By: Danish Azeem
Hahahahahahahahahahahahahahahaha