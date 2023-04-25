package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;


public class SchoolServiceImpl extends UnicastRemoteObject implements SchoolService {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String FILE_NAME = "users.dat";
	private List<School> users;
	protected SchoolServiceImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

    public static void saveUsers(List<School> users) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            outputStream.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
//        System.out.print(schools.get(0).getEmail());
        return schools;
    }
    
    
	public boolean registerUser(String email, String password,String confirmPassword) throws RemoteException {
        users = loadSchools();
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) { return false;}
        if (!password.equals(confirmPassword)) {return false;}
	      if(users.isEmpty()) {
	    	  double[] coeffs= {0.0,0.0,0.0,0.0};
        	  School newUser = new School(email, password,coeffs);
              System.out.print(newUser.getEmail());
              users.add(newUser);
              saveUsers(users);
              return true;
              
        }
        else {
        for (School user : users) {
            if (user.getEmail().equals(email)) {
                return false;
                
            }
        }
        double[] coeffs= {0.0,0.0,0.0,0.0};
  	    School newUser = new School(email, password,coeffs);
            users.add(newUser);
            saveUsers(users);
            return true;

        
    }    }

    public School loginUser(String email, String password) throws RemoteException {
    	users=loadSchools();
    	  for (School user : users) {
              if (user.getEmail().equals(email) && user.getPassword().equals(password)) {    
            	  System.out.print("login");
            	  System.out.println(user.getModelcoeffs()[0]);
                  return user;
              }
          }  
    	  return null;
    	  }

    public void logoutUser(String username) throws RemoteException {
        // Code to log out the user
    }
}