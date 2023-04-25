package application;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ModelTraining  extends Remote {
	public void loadTrainingData( float[][] result,String email) throws Exception;
    public double[] getCoeffs() throws RemoteException;
}
