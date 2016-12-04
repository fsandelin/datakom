import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.*;

public class DatagramServerThread extends Thread {
    protected DatagramSocket socket;
    ArrayList <PlayerInfo> playerList;
    
    public DatagramServerThread() throws IOException{
	super("DatagramServerThread");
	this.socket = new DatagramSocket(1098);
	this.playerList = new ArrayList<PlayerInfo>();
    }
    
    public DatagramServerThread(ArrayList<PlayerInfo> list) throws IOException{
	super("DatagramServerThread");
	this.socket = new DatagramSocket(1098);
	this.playerList = list;

    }    

    public void addPlayer(PlayerInfo info){
        this.playerList.add(info);
    }

    public void removePlayer(int index) {
	this.playerList.remove(index);
    }
    
    public void run() {
	byte[] receiveBuff = new byte[4];
	while(true) {
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
	    Short x = bb.getShort(0);
	    Short y = bb.getShort(2);
	    System.out.println("Server received | " + Short.toString(x) + " " + Short.toString(y));
	}
    }
}
