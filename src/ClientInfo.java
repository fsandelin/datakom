import java.awt.*;
import java.net.*;
import java.io.*;

public class ClientInfo implements Serializable{
    private int id;
    private Client clientStub;

    public ClientInfo(int id, Client stub) {
	this.id = id;
	this.clientStub = stub;
    }

    public int getId() {
	return this.id;
    }

    public Client getStub() {
	return this.clientStub;
    }
}
