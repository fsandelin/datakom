import java.awt.*;
import java.net.*;
import java.io.*;

public class PlayerInfo implements Serializable {
    private InetAddress ip;
    private int port;
    private String alias;
    private int x;
    private int y;
    private int id;
    private Color playerColor;

    public PlayerInfo(String ip, int port, String alias, Color playerColor) {
        try {
            this.ip = InetAddress.getByName(ip);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        this.port = port;
        this.alias = alias;
        this.x = 0;
        this.y = 0;
        this.playerColor = playerColor;
    }

    public PlayerInfo(String ip, int port, String alias, int x, int y, int id, Color playerColor) {
        try {
            this.ip = InetAddress.getByName(ip);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        this.port = port;
        this.alias = alias;
        this.x = x;
        this.y = y;
        this.id = id;
        this.playerColor = playerColor;
    }

    public InetAddress getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public int getId() {
        return this.id;
    }

    public String getAlias() {
        return this.alias;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String toString() {
        return ip + "\n" + Integer.toString(port) + "\n" + alias;
    }

    public Color getColor() {
        return this.playerColor;
    }
} 

