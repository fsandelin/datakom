/**
 * @author Fredrik Sandelin
 */
import java.awt.*;
import java.rmi.*;
import java.util.*;

public interface Server extends Remote{
    int[] getGameState() throws RemoteException;
    ArrayList<PlayerInfo> connectToGame(int port, String alias, Color PlayerColor) throws RemoteException;
    ArrayList<PlayerInfo> updateGame() throws RemoteException;
    void debugRMI() throws RemoteException;
    ArrayList<PlayerInfo> getPlayerList() throws RemoteException;
    void setWinState() throws RemoteException;
}
