package lab1;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICentrala extends Remote {
    boolean rejestruj(ITablica n) throws RemoteException;

    boolean wyrejestruj(ITablica n) throws RemoteException;

    boolean rejestruj(IMonitor n) throws RemoteException;

    boolean wyrejestruj(IMonitor n) throws RemoteException;
}
