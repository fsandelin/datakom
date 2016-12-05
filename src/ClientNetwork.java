/**
 * @author Fredrik Sandelin
 */
import java.rmi.*;
import java.rmi.registry.*;
import java.util.*;

public class ClientNetwork{
    int map;
    int hz;

    private ClientNetwork() {
	this.map = 1;
	this.hz = 32;
    }
    
    public static void main(String args[]) {
	String host = (args.length < 4) ? null : args[0];
	String ownIp = (args.length < 4) ? null : args[1];
	int ownPort = (args.length < 4) ? null : Integer.parseInt(args[2]);
	String ownAlias = (args.length < 4) ? null : args[3];
	System.out.println("Connecting to: " + host + ":1099");
	try {
	    Registry registry = LocateRegistry.getRegistry(host,1099);
	    Server stub = (Server) registry.lookup("Server");
	    int[] response = stub.getGameState();
	    int response2 = stub.connectToGame(ownIp, ownPort, ownAlias);
	    System.out.println(response2);
	    stub.debugRMI();
	} catch(Exception e) {
	    System.err.println("Client network got Exceptiob: " + e.toString());
	    e.printStackTrace();
	}
    }       
}
