import java.net.*;
import java.util.*;
import java.io.*;
    
public class DatagramClientThread extends Thread{

    private InetAddress serverIp;
    private int serverPort;
    private short playerX;
    private short playerY;
    private int hz;
    protected DatagramSocket socket;
    
    public DatagramClientThread(String serverIp, int serverPort) throws IOException{
	super("DataClientThread");
	this.serverIp = InetAddress.getByName(serverIp);
	this.serverPort = serverPort;
	this.playerX = 0;
	this.playerY = 0;
	this.hz = 16;
	this.socket = new DatagramSocket(1099);
    }
    public DatagramClientThread(String serverIp, int serverPort, short x, short y) throws IOException{
	super("Data_client_thread");
	this.serverIp = InetAddress.getByName(serverIp);
	this.serverPort = serverPort;
	this.playerX = x;
	this.playerY = y;
	this.hz = 16;
	this.socket = new DatagramSocket(1099);	
    }

    public DatagramClientThread(String serverIp, int serverPort, short x, short y, int hz) throws IOException{
	super("Data_client_thread");
	this.serverIp = InetAddress.getByName(serverIp);
	this.serverPort = serverPort;
	this.playerX = x;
	this.playerY = y;
	this.hz = hz;
	this.socket = new DatagramSocket(1099);	
    }    

    public void update(short x, short y) {
	this.playerX = x;
	this.playerY = y;
    }

    public short getX(){
	return this.playerX;
    }

    public short getY() {
	return this.playerY;	
    }

    public void run(){
	int timeToSleep = 1000/this.hz;
	byte[] sendBuff = new byte[4];	
	while(true){
	    sendBuff[0] = (byte) (this.playerX >> 8);	    
	    sendBuff[1] = (byte) this.playerX;
	    sendBuff[2] = (byte) (this.playerY >> 8);
	    sendBuff[3] = (byte) this.playerY;
	    //this.debugByteArray(sendBuff);
	    try {
		DatagramPacket sendPacket = new DatagramPacket(sendBuff, sendBuff.length, this.serverIp, this.serverPort);
		this.socket.send(sendPacket);
		System.out.println("Client sent from port " + Integer.toString(this.serverPort) + " | " + Integer.toString(this.playerX) + "," + Integer.toString(this.playerY));
	    }catch(IOException e) {
		System.out.println(e.toString());
	    }
	    
	    this.sleep(timeToSleep);
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
