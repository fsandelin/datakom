import java.net.*;
public class PlayerInfo {
    private InetAddress ip;
    private int port;
    private String alias;
    private short x;
    private short y;

    public PlayerInfo(String ip, int port, String alias) {
	try {
	    this.ip = InetAddress.getByName(ip);
	}catch(Exception e) {
	    System.out.println(e.toString());
	}	
	this.port = port;
	this.alias = alias;
	this.x = 0;
	this.y = 0;
    }
    
    public PlayerInfo(String ip, int port, String alias, Short x, Short y) {
	try {
	    this.ip = InetAddress.getByName(ip);
	}catch(Exception e) {
	    System.out.println(e.toString());
	}
	this.port = port;
	this.alias = alias;
	this.x = x;
	this.y = y;
    }    

    public InetAddress getIp() {
	return this.ip;
    }

    public int getPort() {
	return this.port;
    }

    public String getAlias() {
	return this.alias;
    }

    public short getX() {
	return this.x;
    }
    public short getY() {
	return this.y;
    }

    public String toString() {
	return ip + "\n" + Integer.toString(port) + "\n" + alias;
    }
} 

