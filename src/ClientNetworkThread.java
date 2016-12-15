/**
 * @author Fredrik Sandelin
 */

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;
import java.net.*;

public class ClientNetworkThread extends RemoteServer implements Client{
    private int map;
    private int hz;
    private String serverIp;
    private int serverPort;
    private int ownPort;
    private GameThread gamethread;
    private String alias;
    private ArrayList<PlayerInfo> playerList;
    private int myId;
    private Server serverStub;
    private DatagramClientThread UDPsender;
    private DatagramClientThread UDPreceiver;
    private boolean changeToServer;

    public ClientNetworkThread(GameThread gamethread, String ip, String alias, boolean debug) {
        super();
        if (debug == true) {
            this.ownPort = 1097;
        } else {
            this.ownPort = 1099; //var 1097 förr
        }
	try {
	    LocateRegistry.createRegistry(this.ownPort);
            Client stub = (Client) UnicastRemoteObject.exportObject(this, 0);
	    System.out.println("Creating reg @ port: " + Integer.toString(ownPort));
            Registry registry = LocateRegistry.getRegistry(this.ownPort);
            registry.bind("Client", stub);
            System.err.println("Client RMI setup done");
	}catch(Exception e) {
	    System.out.println(e.toString());
	    System.exit(0);
	}	
	this.changeToServer = false;
        this.serverIp = ip;
        this.playerList = new ArrayList<PlayerInfo>();
        this.serverPort = 1099;
        this.alias = alias;
        this.map = 1;
        this.hz = 32;
        this.gamethread = gamethread;


    }

    public void setWin(boolean bool){
	this.gamethread.setWin(bool);
    }

    public void sendWin() {
	try {
	    this.serverStub.sendWin();
	}catch(Exception e) {
	    System.out.println("Exception when client sendWin to server " + e.toString());
	}
    }
    
    public void shutDown() {
	try {
	    UnicastRemoteObject.unexportObject(this, true);
	}catch(Exception e) {
	    System.out.println("Exception in shutDown: " + e.toString());
	}
    }    


    public void connectToGame() {
	try {
	    System.out.println("Connectar till " + this.serverIp);
	    Registry registry = LocateRegistry.getRegistry(serverIp, serverPort);
	    System.out.println("Got reg");
	    Server stub = (Server) registry.lookup("Server");
	    this.serverStub = stub;
	    this.playerList = this.serverStub.connectToGame(this.ownPort, this.alias, this.gamethread.getPlayer().getPlayerColor());
	    System.out.println("GOT THE FAKING LIST");
	}catch(Exception e) {
	    System.out.println(e.toString());
	}
	this.debugRMI();
	int x = this.playerList.get(playerList.size() - 1).getX();
	int y = this.playerList.get(playerList.size() - 1).getY();
	this.myId = this.playerList.get(playerList.size() - 1).getId();
	this.gamethread.setPlayerId(this.myId);
	this.gamethread.setPlayerX(x);
	this.gamethread.setPlayerY(y);
	for (int i = 0; i < this.playerList.size() - 1; i++) {
	    int xValue = this.playerList.get(i).getX();
	    int yValue = this.playerList.get(i).getY();
	    String alias = this.playerList.get(i).getAlias();
	    int id = this.playerList.get(i).getId();
	    this.gamethread.addPlayerToClient(xValue, yValue, alias, id, this.playerList.get(i).getColor());
	}	
    }


    
    public void updateList() {
	try {
	    this.playerList = serverStub.updateGame();
	} catch (Exception e) {
            System.err.println("Client got exception when updateList() was called." + e.toString());
        }
    }
    
    public ArrayList<PlayerInfo> getPlayerList() {
        return this.playerList;
    }

    public void disconnect() {
	try {
	    this.serverStub.disconnectFromGame(this.ownPort);
	}catch(Exception e) {
	    System.out.println(e.toString());
	}
	System.exit(0);
    }

    public int getMyId() {
        return this.myId;
    }
    
    public void setSender(DatagramClientThread thread) {
	this.UDPsender = thread;
    }

    public void setReceiver(DatagramClientThread thread) {
	this.UDPreceiver = thread;
    }

    public void changeToServer() {
	this.changeToServer = true;
    }

    public boolean getChangeToServer() {
	return this.changeToServer;
    }

    private void debugRMI() {
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
}
