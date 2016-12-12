/**
 * @author Fredrik Sandelin
 */

import java.rmi.*;
import java.rmi.registry.*;
import java.util.*;
import java.net.*;

public class ClientNetworkThread extends Thread {
    private int map;
    private int hz;
    private String serverIp;
    private int serverPort;
    private int ownPort;
    private String ownIp;
    private GameThread gamethread;
    private String alias;
    private ArrayList<PlayerInfo> playerList;
    private int myId;

    public ClientNetworkThread(GameThread gamethread, String ip, String alias, boolean debug) {
        super("ClientNetworkThread");
        this.serverIp = ip;
        this.playerList = new ArrayList<PlayerInfo>();
        this.serverPort = 1099;
        if (debug == true) {
            this.ownPort = 1097;
        } else {
            this.ownPort = 1097;
        }
        this.alias = alias;
        this.map = 1;
        this.hz = 32;
        this.gamethread = gamethread;
        try {
            this.ownIp = InetAddress.getLocalHost().getHostAddress().toString();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println("ClientNetworkThread created with own IP: " + ownIp);

    }

     public void setWinState() throws RemoteException{
	this.gamethread.setWin(true);
    }

    /**
     * Den här funktionen kör tråden. Den försöker connecta till en server med rmi. Från server kommer den få en lista över alla spelare
     * på server var den själv befinner sig längst ner på listan. Den tar sedan de koordinaterna och sätter sin egen spelare där.
     * Till sist så lägger den till alla andra spelare till sin gamethread.
     * 
     * @todo Den här tråden "terminatar" ganska snabbt med iden är att man i senare versioner ska kunna använda den för att köra andra saker än connect to game.
     * @todo Refactora detta
     */

    public void run() {
        try {
            System.out.println("LocateRegistry -> serverIP: " + serverIp + " serverPort:" + Integer.toString(serverPort));
            Registry registry = LocateRegistry.getRegistry(serverIp, serverPort);
            System.out.println("Looking stub");
            Server stub = (Server) registry.lookup("Server");
            ArrayList<PlayerInfo> list = stub.connectToGame(ownIp, ownPort, alias, gamethread.getPlayer().getPlayerColor());
            this.playerList = list;
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

            stub.debugRMI();

	    while(!this.gamethread.checkWinState()){
		try {
		    Thread.sleep(1000);
		}catch(InterruptedException e) {
		    System.out.println(e.toString());
		}	
	    }
	    
        } catch (Exception e) {
            System.err.println("Client network got Exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public ArrayList<PlayerInfo> getPlayerList() {
        return this.playerList;
    }

    public int getMyId() {
        return this.myId;
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
