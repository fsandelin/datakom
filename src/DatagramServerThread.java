
import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.*;

public class DatagramServerThread extends Thread {
    protected DatagramSocket socket;
    private int hz;
    private ArrayList<PlayerInfo> playerList;
    private short playerX;
    private short playerY; 
    private GameThread gamethread;
    private ServerNetworkThread RMIthread;
    private boolean listener;
    
    public DatagramServerThread(GameThread gamethread, ServerNetworkThread RMIthread, boolean listener){
	super("DatagramServerThread");
	try {
	    if (listener == true) {
		System.out.println("Creating socket on port 1099");		
		this.socket = new DatagramSocket(1099);
	    }
	    else {
		System.out.println("Creating socket on port 1098");				
		this.socket = new DatagramSocket(1098);
	    }
	}catch(SocketException e) {
	    System.out.println(e.toString());
	    System.exit(0);
	}
	this.hz = 32;
	this.gamethread = gamethread;
	this.RMIthread = RMIthread;
	this.playerList = this.RMIthread.getPlayerList();
	this.playerX = 0;
	this.playerY = 0;
	this.listener = listener;
    }

    public void addPlayer(PlayerInfo info){
        this.playerList.add(info);
    }

    public void removePlayer(int index) {
	this.playerList.remove(index);
    }

    private void updatePlayerList(){
	short x = this.gamethread.getPlayerXShort();
	short y = this.gamethread.getPlayerYShort();
	System.out.println(Short.toString(x) + " | " + Short.toString(y));
    }

    /**
     * @brief Skickar ut info om alla spelares positioner till alla i this.playerList. I playerList finns det info om vilken address och port den ska skicka till.
     *
     * @detailed Funktionen callar först på updatePlayerList i gamethread och skickar med sin playerList. Denna sätter alla x och y till vad gametheaden har.
     * Sen gör den en byte[] som innehåller allas x och y värden samt deras id. Värdena serialiseras i BIG endian
     * Sen skickar den ut den listan till alla utom sig själv. (Servern står alltid högst upp i listan så index 0 hoppar den över)
     */
    public void sendInfo() {
	//System.out.println("updating");
	if(this.gamethread.checkWinState()) {
	    System.out.println("GOT INTO WIN");
	    this.RMIthread.sendWin();
	    this.gamethread.setDrawWin(true);
	    this.gamethread.setWin(false);
	}
	this.gamethread.updatePlayerList(this.playerList);
	int players = this.playerList.size();
	//System.out.println(players);	
	byte[] sendBuff = new byte[4 + (players*8)];
	int size = this.playerList.size();
	sendBuff[0] = (byte) (size >> 24);
	sendBuff[1] = (byte) (size >> 16);
	sendBuff[2] = (byte) (size >> 8);
	sendBuff[3] = (byte) (size);		
	for(int i = 0; i < players; i++) {
	    sendBuff[0 + 4 + (i*8)] = (byte) (this.playerList.get(i).getX() >> 8);
	    sendBuff[1 + 4 + (i*8)] = (byte) (this.playerList.get(i).getX());
	    sendBuff[2 + 4 + (i*8)] = (byte) (this.playerList.get(i).getY() >> 8);
	    sendBuff[3 + 4 + (i*8)] = (byte) (this.playerList.get(i).getY());
	    sendBuff[4 + 4 + (i*8)] = (byte) (this.playerList.get(i).getId() >> 24);
	    sendBuff[5 + 4 + (i*8)] = (byte) (this.playerList.get(i).getId() >> 16);
	    sendBuff[6 + 4 + (i*8)] = (byte) (this.playerList.get(i).getId() >> 8);
	    sendBuff[7 + 4 + (i*8)] = (byte) (this.playerList.get(i).getId());	    
	}
	try {
	    for(int i = 1; i < players; i++) { //börjar vid 1 för att inte skicka till sig själv
		DatagramPacket sendPacket = new DatagramPacket(sendBuff, sendBuff.length, playerList.get(i).getIp(), playerList.get(i).getPort());
		this.socket.send(sendPacket);
		//System.out.println("===============UDP SENT=================");
		//System.out.println("IP: " + playerList.get(i).getIp());
		//System.out.println("Port: " + playerList.get(i).getPort());
		//this.debugByteArray(sendBuff);
	    }
	}catch(Exception e){
	    System.out.println(e.toString());
	}
    }

    
    /**
     * @brief Receives a UDP packet into param receiveBuff and updates the players x and y value with the same ID as was in the packet.
     *
     * The funktion deserializes the received message in BIG endian since that is the standard for this application.
     *
     * @param receiveBuff The byte[] in which to read in the data from the socket.
     */
    private void receiveInfo(byte[] receiveBuff) {
	DatagramPacket receivePacket = new DatagramPacket(receiveBuff, receiveBuff.length);
	try {
	    this.socket.receive(receivePacket);
	}catch(Exception e) {
	    System.out.println(e.toString());
	}
	byte[] receivedData = receivePacket.getData();
	ByteBuffer bb = ByteBuffer.allocate(8);
	bb.order(ByteOrder.BIG_ENDIAN);
	bb.put(receivedData[0]);
	bb.put(receivedData[1]);
	bb.put(receivedData[2]);
	bb.put(receivedData[3]);
	bb.put(receivedData[4]);
	bb.put(receivedData[5]);
	bb.put(receivedData[6]);
	bb.put(receivedData[7]);
	int x = bb.getShort(0);
	int y = bb.getShort(2);
	int id = bb.getInt(4);
	//System.out.println("========================RECEIVED==============================");
	//System.out.println("Server received | " + Integer.toString(x) + " " + Integer.toString(y) + " for ID: " + Integer.toString(id));
	//System.out.println("From ---- IP: "+ receivePacket.getAddress().toString() + " ------ Port: " + Integer.toString(receivePacket.getPort()));
	//System.out.println("===============================================================");
	if (id != 0) {
	    this.gamethread.updatePlayer(x, y, id);
	}
    }

    public void debugByteArray(byte[] bytearray) {
	String array = "";
	for(int i = 0; i < bytearray.length; i++) {
	    String str = String.format("%8s", Integer.toBinaryString(bytearray[i] & 0xFF)).replace(' ','0');
	    array = array + str + " "; 
	}
	System.out.println(array);
    }    
    
    public void run() {
	int timeToSleep = 1000/this.hz;	
	byte[] receiveBuff = new byte[8];
	while(true) {
	    if(this.listener) {
		this.receiveInfo(receiveBuff);
	    }
	    else {
		this.sendInfo();
		sleep(timeToSleep);
	    } 
	}
    }
    
    public void sleep(int time){
	try {
	    Thread.sleep(time);
	}catch(InterruptedException e) {
	    System.out.println(e.toString());
	}	
    }    
}
