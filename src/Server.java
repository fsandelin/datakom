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
    void disconnectFromGame(int port) throws RemoteException;
    void sendWin() throws RemoteException;
    void getMyStub(int port, int id) throws RemoteException;
}
