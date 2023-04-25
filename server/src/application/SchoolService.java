package application;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SchoolService extends Remote {
	    public boolean registerUser(String username, String password,String confirmPassword) throws RemoteException;
	    public School loginUser(String username, String password) throws RemoteException;
	    public void logoutUser(String username) throws RemoteException;
	
}
