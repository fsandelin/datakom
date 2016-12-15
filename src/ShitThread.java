import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;
import java.awt.*;
import java.net.*;
import java.nio.*;

public class ShitThread extends Thread{
    private int port;
    private int id;
    private String ip;
    private ServerNetworkThread server;
    
    public ShitThread(String ip, int port, int id, ServerNetworkThread server) {
	super("ShitThread");
	this.port = port;
	this.id = id;
	this.server = server;
	this.ip = ip;
    }

    public void run() {
	try {
	    System.out.println("Gettting reg @ " + this.ip + " & port: " + Integer.toString(this.port));	
	    Registry registry = LocateRegistry.getRegistry(this.ip, this.port);
	    System.out.println("Got reg");
	    Client stub = (Client) registry.lookup("Client");
	    System.out.println("Got look");
	    ClientInfo cInfo = new ClientInfo(id, stub);
	    this.server.getClientList().add(cInfo);		    
	}catch(Exception e) {
	    System.out.println("E");
	}
    }
    
}
