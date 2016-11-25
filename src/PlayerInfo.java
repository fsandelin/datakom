public class PlayerInfo {
    private String ip;
    private int port;
    private String alias;

    public PlayerInfo(String ip, int port, String alias) {
	this.ip = ip;
	this.port = port;
	this.alias = alias;
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

