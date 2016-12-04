public class PlayerInfo {
    private String ip;
    private int port;
    private String alias;
    private Short x;
    private Short y;

    public PlayerInfo(String ip, int port, String alias) {
	this.ip = ip;
	this.port = port;
	this.alias = alias;
	this.x = 0;
	this.y = 0;
    }
    
    public PlayerInfo(String ip, int port, String alias, Short x, Short y) {
	this.ip = ip;
	this.port = port;
	this.alias = alias;
	this.x = x;
	this.y = y;
    }    

    public String getIp() {
	return this.ip;
    }

    public int getPort() {
	return this.port;
    }

    public String getAlias() {
	return this.alias;
    }

    public String toString() {
	return ip + "\n" + Integer.toString(port) + "\n" + alias;
    }
} 

