package lab1;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ITablica extends Remote{
	boolean ustaw (Haslo h) throws RemoteException;
	List<Haslo> pobierz() throws RemoteException;
	boolean kasuj(Haslo h) throws RemoteException;
	boolean wyczysc() throws RemoteException;
	boolean start() throws RemoteException;
	boolean stop() throws RemoteException;
	void setNazwa(String n) throws RemoteException;
	String getNazwa() throws RemoteException;
}
