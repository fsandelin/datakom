
import java.net.*;
import java.util.*;


public class Game{
    private static void runAsClient(String alias, String serverIp, boolean debug) {
	GameThread game = new GameThread(800, 600, alias);
	System.out.println("Starting Client RMI thread...");
	ClientNetworkThread clientRMI = new ClientNetworkThread(game, serverIp, alias, debug);
	clientRMI.start();
	sleep(500);
	System.out.println("Starting client UDP thread...");
	DatagramClientThread clientUDPSender = new DatagramClientThread(game, clientRMI, serverIp, false, debug);
	DatagramClientThread clientUDPReceiver = new DatagramClientThread(game, clientRMI, serverIp, true, debug);	
	//clientUDPSender.start();
	//clientUDPReceiver.start();
	//System.out.println("UDP up and running");
	game.start();
	
    }
    private static void runAsServer(String alias) {
	GameThread game = new GameThread(800, 600, alias);
	System.out.println("Starting Server RMI thread...");
	ServerNetworkThread serverRMI = new ServerNetworkThread(game, alias);
	serverRMI.start();
	sleep(500);
	System.out.println("Starting Server UDP thread...");
	DatagramServerThread serverUDPsender = new DatagramServerThread(game, serverRMI, false);
	DatagramServerThread serverUDPreceiver = new DatagramServerThread(game, serverRMI, true);	
	//serverUDP.start();
	//System.out.println("UDP up and running");
	game.start();
	
    }

    /**
     * Main method
     *
     * @param args
     */
    public static void main(String[] args) {
	int port;
	String ip;
	String alias;
	Scanner reader = new Scanner(System.in);
	System.out.println("Debug mode (0/1)?");
	int dbug = reader.nextInt();
	if (dbug==1) {
	    System.out.println("Server (0/1)?");
	    int s = reader.nextInt();
	    if (s==1) {
		runAsServer("1"); 
	    }
	    else {
		runAsClient("2", "127.0.0.1", true);
	    }
	}
	else {
	    System.out.println("Enter your alias:");
	    alias = reader.next();
	    System.out.println("Enter port to use (recommended 1099):");;
	    port = reader.nextInt();
	    System.out.println("0.Exit  |  1.Host  |  2.Client");
	    int response = reader.nextInt();
	    switch (response) {
	    case 1: {
		runAsServer(alias); //VIKTIG
		break;
	    }
	    case 2: {
		System.out.println("Enter IP to connect to:");
		ip = reader.next();
		try {	
		    InetAddress check = InetAddress.getByName(ip);
		}catch(UnknownHostException e) {
		    System.out.println("UnknowHostException. Försök igen och kotrollera IPn.");
		    System.out.println(e.toString());
		    System.exit(0);
		}
		runAsClient(alias, ip, false); //VIKTIG
		break;
	    }
	    default:
		System.exit(0);
		break;
	    }
	}
    }
    private static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
	}
    }
}
