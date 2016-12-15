/**
 * @author Fredrik Sandelin
 */

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;
import java.awt.*;
import java.net.*;

public class ServerNetworkThread extends RemoteServer implements Server {
    private int map;
    private int hz;
    private ArrayList<PlayerInfo> playerList;
    private GameThread gamethread;
    private String ownIp;
    private int ownPort;
    private String ownAlias;
    private int nextId;
    private ArrayList<ClientInfo> clientList;
    private DatagramServerThread UDPsender;
    private DatagramServerThread UDPreceiver;        
    

    public ServerNetworkThread(GameThread gamethread, String ownAlias) {
        super();
        this.map = 1;
        this.hz = 16;
        this.playerList = new ArrayList<PlayerInfo>();
	this.clientList = new ArrayList<ClientInfo>();
        this.gamethread = gamethread;
        this.ownPort = 1099;
        this.ownAlias = ownAlias;
        this.nextId = 0;
        try {
            this.ownIp = InetAddress.getLocalHost().getHostAddress().toString();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println("ServerNetworkThread created with own IP: " + ownIp);
        Player ownPlayer = this.gamethread.getPlayer();
        int ownX = ownPlayer.getPlayerX();
        int ownY = ownPlayer.getPlayerY();
        PlayerInfo player = new PlayerInfo(this.ownIp, this.ownPort, this.ownAlias, ownX, ownY, this.getNextIdAndIncrement(), ownPlayer.getPlayerColor());
        playerList.add(player);
        try {
	    LocateRegistry.createRegistry(this.ownPort);
            Server stub = (Server) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.getRegistry(this.ownPort);
            registry.bind("Server", stub);
            System.err.println("Server RMI setup done");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            System.out.println("IF YOU GOT ALREADY BOUND EXCEPTION!");
            System.out.println("Run rmiregistry & before hosting.");
            System.out.println("Use ps and kill if you already have rmiregistry running and want to kill it.");
	    
            System.exit(0);
        }	
    }

    public ArrayList<PlayerInfo> connectToGame(int port, String alias, Color playerColor) {
        try {
            this.playerList.get(0).setX(this.gamethread.getPlayerX());
            this.playerList.get(0).setY(this.gamethread.getPlayerY());
            int id = this.getNextIdAndIncrement();
            int[] xy = this.gamethread.addPlayerToServer(alias, playerColor, id);
	    String ip = this.getClientHost();
	    System.out.println(ip + " connected.");
	    System.out.println("Assigning ID: " + Integer.toString(id));
            PlayerInfo player = new PlayerInfo(ip, port, alias, xy[0], xy[1], id, playerColor);
            this.playerList.add(player);
	    System.out.println("Gettting reg @ " + ip + " & port: " + Integer.toString(port));
	    Registry registry = LocateRegistry.getRegistry(ip, port);
	    Client stub = (Client) registry.lookup("Client");
	    ClientInfo cInfo = new ClientInfo(id, stub);
	    this.clientList.add(cInfo);
	    System.out.println("Returning");
            this.debugRMI();
            return this.playerList;
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            return null;
        }
    }

    public void shutDown() {
	try {
	    UnicastRemoteObject.unexportObject(this, true);
	    System.out.println("Shut down server RMI");
	}catch(Exception e) {
	    System.out.println("Exception in shutDown: " + e.toString());
	}
    }    
    public void disconnectFromGame(int port) {
	String ip = "";
	try {
	    ip = this.getClientHost();
	}catch(Exception e) {
	    System.out.println(e.toString());
	}
	System.out.println(ip);
	System.out.println(port);	
	for(int i = 0; i < this.playerList.size(); i++) {
	    PlayerInfo pInfo = this.playerList.get(i);
	    if (pInfo.getIp().toString().split("/")[1].equals(ip) && pInfo.getPort() == port) {
		this.playerList.remove(i);
		int id = pInfo.getId();
		this.gamethread.removePlayerById(id);
		for(int j = 0; j < this.clientList.size(); j++) {
		    if (this.clientList.get(j).getId() == id) {
			this.clientList.remove(j);
			System.out.println("Removed index: " + Integer.toString(j));
		    }
		}		
	    }
	}

	this.debugRMI();
    }
    
    public int[] getGameState() {
        int[] state = {this.map, this.hz};
        return state;
    }

    public void debugRMI() {
        ListIterator<PlayerInfo> iterator = this.playerList.listIterator();
        int i = 0;
        System.out.println("==============DEBUGGING LIST PRINT================");
        while (iterator.hasNext()) {
            PlayerInfo cur = iterator.next();
            System.out.println("Player " + Integer.toString(i) + "\n" + cur.toString());
            System.out.println("Id: " + Integer.toString(cur.getId()));
            System.out.println("X: " + Integer.toString(cur.getX()) + "Y: " + Integer.toString(cur.getY()));
            System.out.println("================================================");
            i++;
        }
    }

    public ArrayList<PlayerInfo> getPlayerList() {
        return this.playerList;
    }

    public void setPlayerList(ArrayList<PlayerInfo> list) {
	this.playerList = list;
	this.debugRMI();
    }

    public ArrayList<PlayerInfo> updateGame() {
	return this.playerList;
    }    

    private int getNextIdAndIncrement() {
        int value = this.nextId;
        this.nextId = this.nextId + 1;
        return value;
    }
    public void setSender(DatagramServerThread thread) {
	this.UDPsender = thread;
    }

    public void setReceiver(DatagramServerThread thread) {
	this.UDPreceiver = thread;
    }

    public void sendWin() {
	this.gamethread.setWin(true);
	for(ClientInfo c : clientList) {
	    try {
		c.getStub().setWin(true);
	    }catch(Exception e) {
		System.out.println("Error när server skulle sätta win till clients: " + e.toString());
	    }
	}
    }

    public void askAllInListToChange() {
	//TODO
    }

    public void askSomeoneToTakeOver() {
	if(this.clientList.size() > 0) {
	    try {
		this.clientList.get(0).getStub().changeToServer();
	    }catch(Exception e) {
		System.out.println(e.toString());
	    }
	}
    }
}
