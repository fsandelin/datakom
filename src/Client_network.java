/**
 * @author Fredrik Sandelin
 */
import java.rmi.*;
import java.rmi.registry.*;
import java.util.*;

public class Client_network{
    int map;
    int hz;

    private Client_network() {
	this.map = 1;
	this.hz = 32;
    }

    
    public static void main(String args[]) {
	String host = (args.length < 1) ? null : args[0];
	System.out.println("Connecting to: " + host + ":1099");
	try {
	    Registry registry = LocateRegistry.getRegistry(host,1099);
	    Server stub = (Server) registry.lookup("Server");
	    int[] response = stub.getState();
	} catch(Exception e) {
	    System.err.println("Client network got Exceptiob: " + e.toString());
	    e.printStackTrace();
	}
    }       
}
