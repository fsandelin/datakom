import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.*;
    
public class DatagramClientThread extends Thread{
    
    private int hz;                   //Amount of time per second to send updates
    private InetAddress serverIp;     //Server IP to send to
    private int serverPort;           //Server port to send to
    private short playerX;            //Value X to send
    private short playerY;            //Value Y to send
    protected DatagramSocket socket;  //Own socket
    private GameThread gamethread;    //Own Gamethread
    private ClientNetworkThread RMIthread; //Own RMIthread
    private boolean listener;         //If it is a receiver or sender
    private ArrayList<PlayerInfo> playerList; //Referens to the list created by RMI thread
    private int myId; //Same as the RMI thread got.
    private boolean run;
    
    
    
    public DatagramClientThread(GameThread gamethread, ClientNetworkThread RMIthread, String serverIp, boolean listener, boolean debug){
	super("DataClientThread");	
	try {
	    this.serverIp = InetAddress.getByName(serverIp);
	    if (debug == true) {
		if (listener == true) {
		    System.out.println("Creating socket on port 1097");
		    try {
			this.socket = new DatagramSocket(1097);
		    }catch(Exception e) {
			System.out.println(e.toString());
			System.out.println("Closing application due to exception");
			System.exit(0);
		    }
		}
		else {
		    System.out.println("Creating socket on port 1096");
		    try {
			this.socket = new DatagramSocket(1096);
		    }catch(Exception e) {
			System.out.println(e.toString());
			System.out.println("Closing application due to exception");
			System.exit(0);			
		    }
		}
	    }
	    else {
		if (listener == true) {
		    System.out.println("Creating socket on port 1099");
		    try {
			this.socket = new DatagramSocket(1099);
		    }catch(Exception e) {
			System.out.println(e.toString());
			System.out.println("Closing application due to exception");
			System.exit(0);			
		    }
		}
		else {
		    System.out.println("Creating socket on port 1098");		    
		    try {
			this.socket = new DatagramSocket(1098);
		    }catch(Exception e) {
			System.out.println(e.toString());
			System.out.println("Closing application due to exception");
			System.exit(0);			
		    }
		}
	    }
	}catch(Exception e) {
	    System.out.println(e.toString());
	}
	this.serverPort = 1099;
	this.playerX = 0;
	this.playerY = 0;
	this.hz = 32;
	this.gamethread = gamethread;
	this.RMIthread = RMIthread;
	this.myId = RMIthread.getMyId();
	this.playerList = this.RMIthread.getPlayerList();	
	this.listener = listener;
	this.run = true;
    }
    

    private void updatePlayerPos() {
	this.playerX = this.gamethread.getPlayerXShort();
	this.playerY = this.gamethread.getPlayerYShort();
    }

    public short getX(){
	return this.playerX;
    }

    public short getY() {
	return this.playerY;	
    }

    private void checkDisconnect() {
	if (this.gamethread.getDisconnect()) {
	    this.RMIthread.disconnect();
	}
    }

    private void checkWinState() {
	if(this.gamethread.checkWinState()) {
	    this.RMIthread.sendWin();
	    this.gamethread.setDrawWin(true);
	    this.gamethread.setWin(false);
	}	
    }

    
    /**
     * @brief "Populates" the buffer with info on it's own x and y values and it's id. Sends it to this.serverIp at this.serverPort
     *
     * Serializes the data in BIG endian.
     *
     * @param sendBuff The buffer in which to send info from
     */
    public void sendInfo(byte[] sendBuff) {
	this.checkDisconnect();
	this.checkWinState();
	this.updatePlayerPos();
	sendBuff[0] = (byte) (this.playerX >> 8);	    
	sendBuff[1] = (byte) (this.playerX);
	sendBuff[2] = (byte) (this.playerY >> 8);
	sendBuff[3] = (byte) (this.playerY);
	sendBuff[4] = (byte) (this.myId >> 24);
	sendBuff[5] = (byte) (this.myId >> 16);
	sendBuff[6] = (byte) (this.myId >> 8);
	sendBuff[7] = (byte) (this.myId);
	try {
	    DatagramPacket sendPacket = new DatagramPacket(sendBuff, sendBuff.length, this.serverIp, this.serverPort);
	    this.socket.send(sendPacket);
	    //System.out.println("==================UDP SENDING======================");
	    //this.debugByteArray(sendBuff);
	    //System.out.println("Server: " + this.serverIp);
	    //System.out.println("Port: " + Integer.toString(this.serverPort));
	    //System.out.println("==================================================");		    
	    
	}catch(IOException e) {
	    System.out.println(e.toString());
	}	
    }

    /**
     * @brief Receives UDP packet which contains all connected players to the game id, x and y valure. Updates the current game state with what it got.
     *
     * This funktion calls this.gamethread.updatePlayer and will only do so for player whos Ids isn't it's own, this.myId.
     * Deserializes in BIG endian since it is the standard of this application.
     *
     * @param players Amount of players it expect to receive.
     */
    public void receiveInfo(int players) {
	byte[] receiveBuff = new byte[4 + (players*8)];
	DatagramPacket receivePacket = new DatagramPacket(receiveBuff, receiveBuff.length);
	try {
	    this.socket.receive(receivePacket);
	}catch(Exception e) {
	    System.out.println(e.toString());
	}
	byte[] receivedData = receivePacket.getData();
	ByteBuffer bb = ByteBuffer.allocate(4 + (8*players));
	bb.order(ByteOrder.BIG_ENDIAN);
	bb.put(receivedData[0]);
	bb.put(receivedData[1]);
	bb.put(receivedData[2]);
	bb.put(receivedData[3]);
	int playersInDatagram = bb.getInt(0);
	//System.out.println("=======================UDP RECEIVED==================");
	if (playersInDatagram>players) {
	    this.RMIthread.updateList();
	    this.playerList = this.RMIthread.getPlayerList();
	    PlayerInfo pInfo = this.playerList.get(this.playerList.size() - 1);
	    this.gamethread.addPlayerToClient(pInfo.getX(), pInfo.getY(), pInfo.getAlias(), pInfo.getId(), pInfo.getColor());
	    return;
	}
	if (playersInDatagram<players) {
	    this.RMIthread.updateList();
	    this.playerList = this.RMIthread.getPlayerList();
	    this.gamethread.removePlayerByList(this.playerList);
	}
	//System.out.println("Players in datagram: " + Integer.toString(playersInDatagram));
	for(int i = 0; i < players; i++) {
	    bb.put(receivedData[0 + 4 + (i*8)]);
	    bb.put(receivedData[1 + 4 + (i*8)]);
	    bb.put(receivedData[2 + 4 + (i*8)]);
	    bb.put(receivedData[3 + 4 + (i*8)]);
	    bb.put(receivedData[4 + 4 + (i*8)]);
	    bb.put(receivedData[5 + 4 + (i*8)]);
	    bb.put(receivedData[6 + 4 + (i*8)]);
	    bb.put(receivedData[7 + 4 + (i*8)]);	    
	    short x = bb.getShort(0 + 4 + (i*8));
	    short y = bb.getShort(2 + 4 + (i*8));
	    int id = bb.getInt(4 + 4 + (i*8));
	    //System.out.println("Player ID: " + Integer.toString(id) + " x: " + Short.toString(x) + " y: " + Short.toString(y));
	    if (id != this.RMIthread.getMyId()) {
		int xInt = x;
		int yInt = y;
		this.gamethread.updatePlayer(xInt, yInt, id);
	    }
	    //System.out.println("============================================");

	}	
    }

    public void run(){
	int timeToSleep = 1000/this.hz;
	byte[] sendBuff = new byte[8];	
	while(this.run){
	    if(this.listener) {
		this.receiveInfo(this.playerList.size());
	    }
	    else {
		this.sendInfo(sendBuff);
		sleep(timeToSleep);
	    }
	}
	this.socket.close();
	
    }

    public void setRun(boolean bool) {
	this.run = bool;
    }
    
    public void debugByteArray(byte[] bytearray) {
	String array = "";
	for(int i = 0; i < bytearray.length; i++) {
	    String str = String.format("%8s", Integer.toBinaryString(bytearray[i] & 0xFF)).replace(' ','0');
	    array = array + str + " "; 
	}
	System.out.println(array);
    }

    public void sleep(int time){
	try {
	    Thread.sleep(time);
	}catch(InterruptedException e) {
	    System.out.println(e.toString());
	}	
    }
}
