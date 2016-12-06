/**
 * @author Fredrik Sandelin
 */
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;
import java.awt.*;

public class ServerNetworkThread extends Thread implements Server{
    int map;
    int hz;
    ArrayList <PlayerInfo> playerList;
    GameThread gamethread;
    String ownIp;
    int ownPort;
    String ownAlias;
    
    public ServerNetworkThread(GameThread gamethread, String ownIp, int ownPort, String ownAlias) {
	super("ServerNetworkThread");
	this.map = 1;
	this.hz = 16;
	this.playerList = new ArrayList<PlayerInfo>();
	this.gamethread = gamethread;
	this.ownIp = ownIp;
	this.ownPort = ownPort;
	this.ownAlias = ownAlias;
    }
    
    public int[] getGameState() {
	int[] state = {this.map, this.hz};
	return state;
    }

    public void debugRMI() {
	System.out.println("Map: " + this.map);
	System.out.println("Hz: " + this.hz);
	ListIterator<PlayerInfo> iterator = this.playerList.listIterator();
	int i = 0;
	while (iterator.hasNext()) {
	    PlayerInfo cur = iterator.next();
	    System.out.println("Player " + Integer.toString(i) + "\n" +cur.toString());
	}
    }

    public ArrayList<PlayerInfo> getPlayerList() {
	return this.playerList;
    }
    
    public ArrayList<PlayerInfo> connectToGame(String ip, int port, String alias) {
	try {
	    int[] xy = this.gamethread.addPlayerToServer(alias, gamethread.getPlayer().getPlayerColor());
	    PlayerInfo player = new PlayerInfo(ip, port, alias, xy[0], xy[1]);
	    playerList.add(player);
	    this.debugRMI();
	    return this.playerList;
	} catch(Exception e) {
	    System.err.println("Server exception: " + e.toString());
	    return null;
	}
    }

    public void run(){
	Player ownPlayer = this.gamethread.getPlayer();
	int ownX = ownPlayer.getPlayerX();
	int ownY = ownPlayer.getPlayerY();
	PlayerInfo player = new PlayerInfo(this.ownIp, this.ownPort, this.ownAlias, ownX, ownY);
	playerList.add(player);
	System.out.println("Gjorde player i listan");
	try {
	    Server stub = (Server) UnicastRemoteObject.exportObject(this, 0);
	    Registry registry = LocateRegistry.getRegistry();
	    registry.bind("Server", stub);
	    System.out.println("List: \n" + registry.list());
	    System.err.println("Server RMI setup done");
	} catch(Exception e) {
	    System.err.println("Server exception: " + e.toString());
	    System.out.println("IF YOU GOT ALREADY BOUND EXCEPTION!");
	    System.out.println("Run rmiregistry & before hosting.");
	    System.out.println("Use ps and kill if you already have rmiregistry running and want to kill it.");
	    System.exit(0);
	}
	
    }
    
    
}
