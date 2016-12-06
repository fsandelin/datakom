/**
 * @author Fredrik Sandelin
 */
import java.rmi.*;
import java.util.*;

public interface Server extends Remote{
    int[] getGameState() throws RemoteException;
    ArrayList<PlayerInfo> connectToGame(String ip, int port, String alias) throws RemoteException;
    void debugRMI() throws RemoteException;
    ArrayList<PlayerInfo> getPlayerList() throws RemoteException;
}
