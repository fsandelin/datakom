/**
 * @author Fredrik Sandelin
 */
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;

public class Server_network implements Server{
    int map;
    int hz;
    ArrayList <PlayerInfo> playerList;
    
    public Server_network() {
	this.map = 1;
	this.hz = 16;
	this.playerList = new ArrayList<PlayerInfo>();
    }
    
    public int[] getState() {
	int[] state = {this.map, this.hz};
	return state;
    }

    public void debug() {
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
	    this.debug();
	    return 1;
	} catch(Exception e) {
	    System.err.println("Server exception: " + e.toString());	    
	    return 0;
	}
    }


    public static void main(String args[]) {
	try {
	    Server_network obj = new Server_network();
	    Server stub = (Server) UnicastRemoteObject.exportObject(obj, 0);
	    Registry registry = LocateRegistry.getRegistry();
	    registry.bind("Server", stub);
	    System.out.println("List: \n" + registry.list());
	    System.err.println("Server setup done");
	    for(int i = 0; i < 10; i++) {
		System.out.println("a");
	    }
	} catch(Exception e) {
	    System.err.println("Server exception: " + e.toString());
	    e.printStackTrace();
	}
	
    }
    
    
}
