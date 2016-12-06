/**
 * @author Fredrik Sandelin
 */
import java.rmi.*;
import java.rmi.registry.*;
import java.util.*;
import java.net.*;

public class ClientNetworkThread extends Thread{
    int map;
    int hz;
    String serverIp;
    int serverPort;
    int ownPort;
    String ownIp;
    GameThread gamethread;
    String alias;

    public ClientNetworkThread(GameThread gamethread, String ip, int port, int ownPort, String alias) {
	super("ClientNetworkThread");
	this.serverIp = ip;
	this.serverPort = port;
	this.ownPort = ownPort;
	this.alias = alias;
	this.map = 1;
	this.hz = 32;
	this.gamethread = gamethread;
	try {
	    this.ownIp = InetAddress.getLocalHost().getHostAddress().toString();
	}catch(Exception e) {
	    System.out.println(e.toString());
	}
	System.out.println("ClientNetworkThread created with own IP: " + ownIp);
	
    }
    
    public void run() {
	try {
	    Registry registry = LocateRegistry.getRegistry(serverIp,serverPort);
	    Server stub = (Server) registry.lookup("Server");
	    int[] response = stub.getGameState();
	    int response2 = stub.connectToGame(ownIp, ownPort, alias);
	    System.out.println(response2);
	    stub.debugRMI();
	} catch(Exception e) {
	    System.err.println("Client network got Exception: " + e.toString());
	    e.printStackTrace();
	}
    }       
}
