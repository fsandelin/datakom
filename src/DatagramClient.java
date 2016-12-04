import java.net.*;
import java.util.*;
import java.io.*;

public class DatagramClient {

    public static void main(String[] args) throws InterruptedException{
	DatagramClientThread dtr;
	DatagramServerThread str;
	try {
	    Short s = 10;
	    Short s2 = 20;	    
	    dtr = new DatagramClientThread("127.0.0.1", 1098,s,s2,1);
	    str = new DatagramServerThread();
	    str.start();
	    dtr.start();
	    Thread.sleep(10000);

	} catch(IOException e) {
	    System.out.println(e.toString());
	}


	System.exit(0);
    }
    
}
