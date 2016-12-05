/**
 * @author Fredrik Sandelin
 */
import java.rmi.*;

public interface Server extends Remote{
    int[] getGameState() throws RemoteException;
    int connectToGame(String ip, int port, String alias) throws RemoteException;
    void debugRMI() throws RemoteException;;
}
