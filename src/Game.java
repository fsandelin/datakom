import java.net.*;
import java.util.*;


public class Game {

    private static void startNewServer(ArrayList<PlayerInfo> playerlist, GameThread game, String alias) {
	ServerNetworkThread serverRMI = new ServerNetworkThread(game, alias);
        DatagramServerThread serverUDPsender = new DatagramServerThread(game, serverRMI, false);
        DatagramServerThread serverUDPreceiver = new DatagramServerThread(game, serverRMI, true);
	serverRMI.setPlayerList(playerlist);
	serverUDPsender.setListFromRMI();
	serverUDPreceiver.setListFromRMI();
	game.setPlayersAccordingToList(playerlist); //TODO
	serverRMI.askAllInListToChange(); //TODO
	//serverRMI.setNextId();
	//TODO MAKE SURE MY ID OCH NEXT ID FÖLJER MED
	//serverUDPsender.start();
	//serverUDPreceiver.start();
	//System.out.println("UDP up and running");
    }
    private static void runAsClient(String alias, String serverIp, boolean debug) {
        GameThread game = new GameThread(800, 600, alias);
        System.out.println("Starting Client RMI thread...");
        ClientNetworkThread clientRMI = new ClientNetworkThread(game, serverIp, alias, debug);
	clientRMI.connectToGame();
        System.out.println("Starting client UDP thread...");
        DatagramClientThread clientUDPsender = new DatagramClientThread(game, clientRMI, serverIp, false, debug);
        DatagramClientThread clientUDPreceiver = new DatagramClientThread(game, clientRMI, serverIp, true, debug);
	//clientRMI.setSender(clientUDPsender);
	//clientRMI.setReceiver(clientUDPreceiver);    
        clientUDPreceiver.start();
        clientUDPsender.start();
        System.out.println("UDP up and running");
	game.start();
	while(!clientRMI.getChangeToServer()) {
	    sleep(1000);
	}
	System.out.println("Changing to server");
	ArrayList<PlayerInfo> playerlist = clientRMI.getPlayerList();
	playerlist.remove(0);
	clientUDPsender.setRun(false);
	clientUDPreceiver.setRun(false);
	try {
	    clientUDPsender.join();
	    clientUDPreceiver.join();
	}catch(Exception e) {
	    System.out.println(e.toString());
	}
	System.out.println("UDP closed");
	clientRMI.shutDown();	
	startNewServer(playerlist, game, alias);
    }

    private static void runAsServer(String alias) {
        GameThread game = new GameThread(800, 600, alias);
        System.out.println("Starting Server RMI thread...");
        ServerNetworkThread serverRMI = new ServerNetworkThread(game, alias);
        System.out.println("Starting Server UDP thread...");
        DatagramServerThread serverUDPsender = new DatagramServerThread(game, serverRMI, false);
        DatagramServerThread serverUDPreceiver = new DatagramServerThread(game, serverRMI, true);
	//serverRMI.setSender(serverUDPsender);
	//serverRMI.setReceiver(serverUDPreceiver);
        serverUDPsender.start();
        serverUDPreceiver.start();
        System.out.println("UDP up and running");
        game.start();
	while(!game.getDisconnect()) {
	    sleep(1000);
	}
	serverUDPsender.setRun(false);
	serverUDPreceiver.setRun(false);
	try {
	    serverUDPsender.join();
	    serverUDPreceiver.join();
	}catch(Exception e) {
	    System.out.println(e.toString());
	}	
	serverRMI.shutDown();
	serverRMI.askSomeoneToTakeOver();
	System.out.println("Server disc");
	System.exit(0);

    }

    /**
     * Main method
     *
     * @param args
     */
    public static void main(String[] args) {
	if (args.length == 3) {
	    boolean debug = Boolean.valueOf(args[2]);
	    runAsClient(args[0], args[1], debug);
	}
	else if (args.length == 1) {
	    runAsServer(args[0]);	    
	}
	else if (args.length != 1 && args.length != 3 && args.length > 0) {
	    System.out.println("=========USAGE===========");
	    System.out.println("Running as server: java Game <alias>");
	    System.out.println("Running as client: java Game <alias> <IP to connect to> <debug mode>");
	    System.out.println("Running in debug mode creates port at 1097 and 1096. Otherwise ports are created at 1099 and 1098, like the server does.");
	    System.out.println("Running only java Game lets user input their choices.");
	    System.exit(0);
	}
	else {
	    int port;
	    String ip;
	    String alias;
	    Scanner reader = new Scanner(System.in);
	    //System.out.println("Debug mode (0/1)?");
	    //int dbug = reader.nextInt();
	    int dbug = 0;
	    if (dbug == 1) {
		System.out.println("Server (0/1)?");
		int s = reader.nextInt();
		if (s == 1) {
		    runAsServer("1");
		} else {
		    runAsClient("2", "127.0.0.1", true);
		}
	    } else {
		System.out.println("Enter your alias:");
		alias = reader.next();
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
                    } catch (UnknownHostException e) {
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
    }

    private static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }
}
