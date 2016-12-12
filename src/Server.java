/**
 * @author Fredrik Sandelin
 */
import java.awt.*;
import java.rmi.*;
import java.util.*;

public interface Server extends Remote{
    int[] getGameState() throws RemoteException;
    ArrayList<PlayerInfo> connectToGame(String ip, int port, String alias, Color PlayerColor) throws RemoteException;
    void debugRMI() throws RemoteException;
    ArrayList<PlayerInfo> getPlayerList() throws RemoteException;
}
