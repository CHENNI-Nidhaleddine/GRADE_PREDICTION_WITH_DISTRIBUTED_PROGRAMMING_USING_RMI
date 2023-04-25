package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class PredictionImpl extends UnicastRemoteObject implements Prediction {
	private static final String FILE_NAME = "users.dat";
	private List<School> users;
	protected PredictionImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	  @SuppressWarnings("unchecked")
		public static List<School> loadSchools() {
	        List<School> schools = new ArrayList<>();
	        File file = new File(FILE_NAME);
	        if (!file.exists() || file.length() == 0) {
	            return schools;
	        }
	        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
	            schools = (List<School>) inputStream.readObject();
	        } catch (IOException | ClassNotFoundException e) {
	            e.printStackTrace();
	        }
//	        System.out.print(schools.get(0).getEmail());
	        return schools;
	    }
	    
	// methode pour effectuer la prédiction de la note finale
	public double predictGrade(float grade1, float grade2, float grade3,String email) throws RemoteException {
		 users = loadSchools();
	    //extraction des coefficients du modèle
		 
		  for (School user : users) {
              if (user.getEmail().equals(email)) {    
            	  System.out.print("login");
            	  System.out.println(user.getModelcoeffs()[0]);
            	  double predictedGrade =user.getModelcoeffs()[0] + user.getModelcoeffs()[1]*grade1 + user.getModelcoeffs()[2]*grade2 + user.getModelcoeffs()[3]*grade3;
          	      return (float) predictedGrade;
              }
          }  
		  return 0.0;
	    
	}
}

