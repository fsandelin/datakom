
import java.net.*;
import java.util.*;


public class Game{
    private static void runAsClient(String alias, String serverIp, int serverPort ,int ownPort) {
	GameThread game = new GameThread(800, 600, alias);
	System.out.println("Starting Client RMI thread...");
	ClientNetworkThread clientRMI = new ClientNetworkThread(game, serverIp, serverPort, ownPort, alias);
	clientRMI.start();
	System.out.println("Starting client UDP thread...");
	DatagramClientThread clientUDPSender = new DatagramClientThread(game, serverIp, serverPort, ownPort, false);
	clientUDPSender.start();
	DatagramClientThread clientUDPReceiver = new DatagramClientThread(game, serverIp, serverPort, ownPort, true);
	clientUDPReceiver.start();
	System.out.println("UDP up and running");
	game.start();
	
    }
    private static void runAsServer(String alias, String ip, int port) {
	GameThread game = new GameThread(800, 600, alias);
	System.out.println("Starting Server RMI thread...");
	ServerNetworkThread serverRMI = new ServerNetworkThread(game, ip, port, alias);
	serverRMI.start();
        sleep(500);
	System.out.println("Starting Server UDP thread...");
	DatagramServerThread serverUDP = new DatagramServerThread(game, port);
	serverUDP.start();
	System.out.println("UDP up and running");
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
	System.out.println("0.Exit  | 1.TestHost | 2.Test-Client | 3.Continue");
	int response = reader.nextInt();

	String TestAliasServer = "TestServer";
	String TestIp = "127.0.0.1";
	int TestPortServer = 1099;

	 String TestAliasClient = "TestClient";
	 int TestPortClient = 1098;

	switch(response) {
	case 1: {
	    System.out.println("Running with:\n Alias = "+TestAliasServer+
			       "\n ServerIp = "+TestIp+"\n ServerPort = "+TestPortServer);
	    runAsServer(TestAliasServer, TestIp, TestPortServer);
	    break;
	}
	case 2: {
	   
	    System.out.println("Running with:\n Alias = "+TestAliasClient+"\n ServerIp = "+TestIp+
			       "\n ServerPort = "+TestPortServer+"\n ServerPort = "+TestPortClient);
	    runAsClient(TestAliasClient, TestIp, TestPortServer, TestPortClient);
	    break;
	}
	case 3: {
	
	    System.out.println("Enter your alias:");
	    alias = reader.next();
	    System.out.println("Enter port to use (recommended 1099):");;
	    port = reader.nextInt();
	
	    System.out.println("0.Exit  |  1.Host  |  2.Client");
	    int response2 = reader.nextInt();

	    switch (response2) {
	    case 1: {
		System.out.println("Enter your IP:");
		ip = reader.next();
		try {	
		    InetAddress check = InetAddress.getByName(ip);
		}catch(UnknownHostException e) {
		    System.out.println("UnknowHostException. Försök igen och kotrollera IPn.");
		    System.out.println(e.toString());
		    System.exit(0);
		}
		runAsServer(alias, ip, port); //VIKTIG
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
		System.out.println("Enter port to connect to:");
		int serverPort = reader.nextInt();
		runAsClient(alias, ip, serverPort, port); //VIKTIG
		break;
	    }
	    default:
		System.exit(0);
		break;
	    }
	}
	default:
	    System.exit(0);
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
