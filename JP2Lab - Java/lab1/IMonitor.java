package lab1;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMonitor extends Remote{
	void loguj(String h) throws RemoteException;
}
