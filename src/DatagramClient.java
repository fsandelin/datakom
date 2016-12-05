import java.net.*;
import java.util.*;
import java.io.*;

public class DatagramClient {

    public static void main(String[] args) throws InterruptedException{
	DatagramClientThread ctr;
	DatagramServerThread str;
	ArrayList<PlayerInfo> list = new ArrayList<PlayerInfo>();
	DatagramServerThread str2;
	Short s = 10;
	Short s2 = 20;
	Short s3 = 100;
	Short s4 = 200;
	Short s5 = 77;
	Short s6 = 33;
	list.add(new PlayerInfo("127.0.0.1", 1097, "Stwosucks", s, s2));
	list.add(new PlayerInfo("127.0.0.1", 1097, "Hltv", s3, s4));
	list.add(new PlayerInfo("127.0.0.1", 1097, "Wanni", s5, s6));
	try {

	    ctr = new DatagramClientThread("127.0.0.1", 1099,s,s2,1);
	    str = new DatagramServerThread(list);
	    str2 = new DatagramServerThread();
	    System.out.println("A");
	    str.start();
	    ctr.start();
	    //ctr.receiveInfo(3);
	    //str.sendInfo();
	    Thread.sleep(10000);

	} catch(IOException e) {
	    System.out.println(e.toString());
	}


	System.exit(0);
    }
    
}
