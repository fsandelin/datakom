/**
 * @author Fredrik Sandelin
 */
import java.awt.*;
import java.rmi.*;
import java.util.*;

public interface Client extends Remote{
    void setWin(boolean bool) throws RemoteException;
    void changeToServer() throws RemoteException;
}
