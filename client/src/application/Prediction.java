package application;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Prediction extends Remote {
	public double predictGrade(float grade1, float grade2, float grade3,String email) throws RemoteException;
}


