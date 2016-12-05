import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.*;

public class DatagramServerThread extends Thread {
    protected DatagramSocket socket;
    private int hz;
    ArrayList<PlayerInfo> playerList;
    private short playerX;
    private short playerY;
    private GameThread gamethread;  
    
    public DatagramServerThread(GameThread gamethread){
	super("DatagramServerThread");
	try {
	    this.socket = new DatagramSocket(1099);
	}catch(SocketException e) {
	    System.out.println(e.toString());
	}
	this.hz = 16;
	this.gamethread = gamethread;
	this.playerList = new ArrayList<PlayerInfo>();
	this.playerX = 0;
	this.playerY = 0;
    }
    
    public DatagramServerThread(short playerX, short playerY, GameThread gamethread, ArrayList<PlayerInfo> list) throws IOException{
	super("DatagramServerThread");
	this.socket = new DatagramSocket(1098);
	this.playerList = list;
	this.hz = 16;
	this.gamethread = gamethread;
	this.playerX = playerX;
	this.playerY = playerY;
    }

    public void addPlayer(PlayerInfo info){
        this.playerList.add(info);
    }

    public void removePlayer(int index) {
	this.playerList.remove(index);
    }

    private void updatePlayerList(){
	short x = this.gamethread.getPlayer().getPlayerXShort();
	short y = this.gamethread.getPlayer().getPlayerYShort();
	System.out.println(Short.toString(x) + " | " + Short.toString(y));
    }
    public void sendInfo() {
	this.updatePlayerList();
	int players = this.playerList.size();
	byte[] sendBuff = new byte[players*4];
	for(int i = 0; i < players; i++) {
	    sendBuff[0 + (i*4)] = (byte) (this.playerList.get(i).getX() >> 8);
	    sendBuff[1 + (i*4)] = (byte) (this.playerList.get(i).getX());
	    sendBuff[2 + (i*4)] = (byte) (this.playerList.get(i).getY() >> 8);
	    sendBuff[3 + (i*4)] = (byte) (this.playerList.get(i).getY());
	}
	//this.debugByteArray(sendBuff);
	try {
	    for(int i = 0; i < players; i++) {
		DatagramPacket sendPacket = new DatagramPacket(sendBuff, sendBuff.length, playerList.get(i).getIp(), playerList.get(i).getPort());
		this.socket.send(sendPacket);
	    }
	}catch(Exception e){
	    System.out.println(e.toString());
	}
    }

    private void receiveInfo(byte[] receiveBuff) {
	DatagramPacket receivePacket = new DatagramPacket(receiveBuff, receiveBuff.length);
	try {
	    this.socket.receive(receivePacket);
	}catch(Exception e) {
	    System.out.println(e.toString());
	}
	byte[] receivedData = receivePacket.getData();
	ByteBuffer bb = ByteBuffer.allocate(4);
	bb.order(ByteOrder.BIG_ENDIAN);
	bb.put(receivedData[0]);
	bb.put(receivedData[1]);
	bb.put(receivedData[2]);
	bb.put(receivedData[3]);
	short x = bb.getShort(0);
	short y = bb.getShort(2);
	System.out.println("Server received | " + Short.toString(x) + " " + Short.toString(y));
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
	byte[] receiveBuff = new byte[4];
	while(true) {
	    this.sendInfo();
	    this.sleep(timeToSleep);
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
