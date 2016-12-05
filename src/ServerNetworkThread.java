/**
 * @author Fredrik Sandelin
 */
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;

public class ServerNetworkThread extends Thread implements Server{
    int map;
    int hz;
    ArrayList <PlayerInfo> playerList;
    GameThread gamethread;
    
    public ServerNetworkThread(GameThread gamethread) {
	super("ServerNetworkThread");
	this.map = 1;
	this.hz = 16;
	this.playerList = new ArrayList<PlayerInfo>();
	this.gamethread = gamethread;
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

    public int connectToGame(String ip, int port, String alias) {
	try {
	    PlayerInfo player = new PlayerInfo(ip, port, alias);
	    playerList.add(player);
	    this.debugRMI();
	    return 1;
	} catch(Exception e) {
	    System.err.println("Server exception: " + e.toString());	    
	    return 0;
	}
    }


    public void run(){
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
