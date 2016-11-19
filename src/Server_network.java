/**
 * @author Fredrik Sandelin
 */
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

public class Server_network implements Server{
    int map;
    int speed;
    int hz;
    
    public Server_network() {
	this.map = 1;
	this.speed = 1;
	this.hz = 16;
    }
    
    public int[] getState() {
	int[] state = {this.map, this.speed, this.hz};
	return state;
    }


    public static void main(String args[]) {
	try {
	    Server_network obj = new Server_network();
	    Server stub = (Server) UnicastRemoteObject.exportObject(obj, 0);

	    Registry registry = LocateRegistry.getRegistry();
	    registry.bind("Server", stub);

	    System.err.println("Server setup done");
	} catch(Exception e) {
	    System.err.println("Server exception: " + e.toString());
	    e.printStackTrace();
	}
	
    }
    
    
}


