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
	    System.out.println("LocateRegistry -> serverIP: " + serverIp + " serverPort:" + Integer.toString(serverPort));
	    Registry registry = LocateRegistry.getRegistry(serverIp,serverPort);
	    System.out.println("Looking stub");
	    Server stub = (Server) registry.lookup("Server");
	    System.out.println("Get game state");
	    int[] response = stub.getGameState();
	    System.out.println("connect with ownIP: " + ownIp + " ownPort: " + Integer.toString(ownPort) + " alias: " + alias);	    
	    ArrayList<PlayerInfo> list = stub.connectToGame(ownIp, ownPort, alias);
	    this.playerList = list;
	    System.out.println("Satt list till serverns list");
	    this.debugRMI();
	    int x = this.playerList.get(playerList.size() - 1).getX();
	    int y = this.playerList.get(playerList.size() - 1).getY();	    
	    Player player = this.gamethread.getPlayer();
	    player.setX(x);
	    player.setY(y);
	    for(int i = 0; i < this.playerList.size() - 1; i++) {
		int xValue = this.playerList.get(i).getX();
		int yValue = this.playerList.get(i).getY();
		this.gamethread.addPlayerToClient(x,y, "snopp");
	    }
	    stub.debugRMI();
	} catch(Exception e) {
	    System.err.println("Client network got Exception: " + e.toString());
	    e.printStackTrace();
	}
    }
    private void debugRMI() {
	System.out.println("Map: " + this.map);
	System.out.println("Hz: " + this.hz);
	ListIterator<PlayerInfo> iterator = this.playerList.listIterator();
	int i = 0;
	while (iterator.hasNext()) {
	    PlayerInfo cur = iterator.next();
	    System.out.println("Player " + Integer.toString(i) + "\n" +cur.toString());
	}
    }    
}
