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
    ArrayList <PlayerInfo> playerList;    

    public ClientNetworkThread(GameThread gamethread, String ip, int port, int ownPort, String alias) {
	super("ClientNetworkThread");
	this.serverIp = ip;
	this.playerList = new ArrayList<PlayerInfo>();	
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
	    ArrayList<PlayerInfo> list = stub.connectToGame(ownIp, ownPort, alias);
	    this.playerList = list;
	    int x = this.playerList.get(playerList.size() - 1).getX();
	    int y = this.playerList.get(playerList.size() - 1).getY();	    
	    Player player = this.gamethread.getPlayer();
	    player.setX(x);
	    player.setY(y);
	    for(int i = 0; i < this.playerList.size() - 1; i++) {
		int xValue = this.playerList.get(i).getX();
		int yValue = this.playerList.get(i).getY();
		this.gamethread.addPlayerClient(x,y);
	    }
	    stub.debugRMI();
	} catch(Exception e) {
	    System.err.println("Client network got Exception: " + e.toString());
	    e.printStackTrace();
	}
    }       
}
