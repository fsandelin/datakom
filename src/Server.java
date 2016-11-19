/**
 * @author Fredrik Sandelin
 */
import java.rmi.*;

public interface Server extends Remote{
    int[] getState() throws RemoteException;
}
