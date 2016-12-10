import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.*;
    
public class DatagramClientThread extends Thread{

    private InetAddress serverIp;
    private int serverPort;
    private short playerX;
    private short playerY;
    private int hz;
    protected DatagramSocket socket;
    private GameThread gamethread;
    private boolean listener;
    
    
    public DatagramClientThread(GameThread gamethread, String serverIp, int serverPort, int ownPort, boolean listener){
	super("DataClientThread");	
	try {
	    this.serverIp = InetAddress.getByName(serverIp);
	    this.socket = new DatagramSocket(ownPort);
	}catch(Exception e) {
	    System.out.println(e.toString());
	}
	this.serverPort = serverPort;
	this.playerX = 0;
	this.playerY = 0;
	this.hz = 16;
	this.gamethread = gamethread;
	this.listener = listener;
    }
    
    public DatagramClientThread(GameThread gamethread, String serverIp, int serverPort, short x, short y) throws IOException{
	super("Data_client_thread");
	this.serverIp = InetAddress.getByName(serverIp);
	this.serverPort = serverPort;
	this.playerX = x;
	this.playerY = y;
	this.hz = 16;
	this.socket = new DatagramSocket(1097);
	this.gamethread = gamethread;	
    }

    public DatagramClientThread(GameThread gamethread, String serverIp, int serverPort, short x, short y, int hz) throws IOException{
	super("Data_client_thread");
	this.serverIp = InetAddress.getByName(serverIp);
	this.serverPort = serverPort;
	this.playerX = x;
	this.playerY = y;
	this.hz = hz;
	this.socket = new DatagramSocket(1097);
	this.gamethread = gamethread;
    }    

    private void updatePlayerPos() {
	Player player = this.gamethread.getPlayer();
	this.playerX = player.getPlayerXShort();
	this.playerY = player.getPlayerYShort();
    }

    public short getX(){
	return this.playerX;
    }

    public short getY() {
	return this.playerY;	
    }

    public void sendInfo(byte[] sendBuff) {
	this.updatePlayerPos();
	sendBuff[0] = (byte) (this.playerX >> 8);	    
	sendBuff[1] = (byte) this.playerX;
	sendBuff[2] = (byte) (this.playerY >> 8);
	sendBuff[3] = (byte) this.playerY;
	//this.debugByteArray(sendBuff);
	try {
	    DatagramPacket sendPacket = new DatagramPacket(sendBuff, sendBuff.length, this.serverIp, this.serverPort);
	    this.socket.send(sendPacket);
	}catch(IOException e) {
	    System.out.println(e.toString());
	}	
    }

    public void receiveInfo(int players) {
	byte[] receiveBuff = new byte[players*4];
	DatagramPacket receivePacket = new DatagramPacket(receiveBuff, receiveBuff.length);
	try {
	    this.socket.receive(receivePacket);
	}catch(Exception e) {
	    System.out.println(e.toString());
	}
	byte[] receivedData = receivePacket.getData();
	ByteBuffer bb = ByteBuffer.allocate(4*players);
	bb.order(ByteOrder.BIG_ENDIAN);
	for(int i = 0; i < players; i++) {
	    bb.put(receivedData[0 + (i*4)]);
	    bb.put(receivedData[1 + (i*4)]);
	    bb.put(receivedData[2 + (i*4)]);
	    bb.put(receivedData[3 + (i*4)]);
	    short x = bb.getShort(0 + (i*4));
	    short y = bb.getShort(2 + (i*4));
	    System.out.println("Client received | " + Short.toString(x) + " " + Short.toString(y));	    
	}	
    }

    public void run(){
	int timeToSleep = 1000/this.hz;
	byte[] sendBuff = new byte[4];	
	while(true){
	    if(listener) {
		this.receiveInfo(1);
	    }
	    else {
		this.sendInfo(sendBuff);
		sleep(timeToSleep);
	    }
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

    public void sleep(int time){
	try {
	    Thread.sleep(time);
	}catch(InterruptedException e) {
	    System.out.println(e.toString());
	}	
    }
}
