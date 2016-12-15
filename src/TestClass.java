import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.*;

public class TestClass {

    public static void main(String[] args) {
	DatagramSocket socket;
	try {
	    socket = new DatagramSocket(1099);
	    InetAddress address = socket.getLocalAddress();
	    System.out.println(address.toString());
	    System.exit(0);	    
	}catch(Exception e) {
	    System.out.println(e.toString());
	    System.out.println("Closing application due to exception");
	    System.exit(0);
	}	

    }
}
