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

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import javafx.collections.ObservableList;

@SuppressWarnings("serial")
public class ModelTrainingImpl extends UnicastRemoteObject implements ModelTraining {
	private static final String FILE_NAME = "users.dat";

    private List<Float[]> trainingData;
    private OLSMultipleLinearRegression regression;
   
    private double[] coeffs;
    public ModelTrainingImpl() throws RemoteException {
    	   super();
           trainingData = new ArrayList<>();
           regression = new OLSMultipleLinearRegression();
    }
    public ModelTrainingImpl(ObservableList<ObservableList<String>> here) throws RemoteException {
        super();
        trainingData = new ArrayList<>();
        regression = new OLSMultipleLinearRegression();
        try {
//            loadTrainingData(here);
            setCoeffs(regression.estimateRegressionParameters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addGrade(float grade1, float grade2, float grade3, float finalGrade) {
        Float[] data = new Float[]{(float) grade1, (float) grade2, (float) grade3, (float) finalGrade};
        System.out.print("--test--");
        System.out.print(data[0]);
        System.out.print("\n");
        trainingData.add(data);
    }

    public double predictGrade(float grade1, float grade2, float grade3) throws RemoteException {
        double[] coeffs = regression.estimateRegressionParameters();
        double predictedGrade = coeffs[0] + coeffs[1]*grade1 + coeffs[2]*grade2 + coeffs[3]*grade3;
        return (float) predictedGrade;
    }

    public void trainModel()  {
        double[][] x = new double[trainingData.size()][3];
        double[] y = new double[trainingData.size()];
        for (int i = 0; i < trainingData.size(); i++) {
            Float[] data =  trainingData.get(i);
            x[i][0] = data[0];
            x[i][1] = data[1];
            x[i][2] = data[2];
            y[i] = data[3];
        }
        regression.newSampleData(y, x);
        setCoeffs(regression.estimateRegressionParameters());
    }

    public void loadTrainingData( float[][] result,String email) throws Exception {
        System.out.print("--------grade--");
        System.out.print(result.length);
        System.out.print(" ");

        System.out.print(result[1][1]);
        System.out.print(" ");
        System.out.print(result[1][2]);
        System.out.print(" ");
        System.out.print(result[1][3]);
        System.out.print("--------end--");
        for (int i = 0; i <result.length/4; i++) {
            float grade1 = result[i][0];
            float grade2 = result[i][1];
            float grade3 = result[i][2];
            float finalGrade = result[i][3];
            System.out.print("--------grade--");
            System.out.print(i);
            System.out.print("\n");
            System.out.print(grade1);
            System.out.print(" ");
            System.out.print(grade2);
            System.out.print(" ");
            System.out.print(grade3);
            System.out.print("-\n-");

            addGrade(grade1, grade2, grade3, finalGrade);
        }
//        trainModel();
        double[][] x = new double[trainingData.size()][3];
        double[] y = new double[trainingData.size()];
        for (int i = 0; i < trainingData.size(); i++) {
            Float[] data =  trainingData.get(i);
            x[i][0] = data[0];
            x[i][1] = data[1];
            x[i][2] = data[2];
            y[i] = data[3];
        }
        regression.newSampleData(y, x);
        
        System.out.print("-------------ggg--");
//        System.out.print(getCoeffs()[0]);
        System.out.print("-------------ggg--");
        int ii=0;
        List<School> schools = loadSchools();
        for (School user : schools) {
            if (user.getEmail().equals(email)) {
            	ii=schools.indexOf(user);
            	System.out.print(regression.estimateRegressionParameters()[0]);
                setCoeffs(regression.estimateRegressionParameters());

                user.setModelcoeffs(regression.estimateRegressionParameters());
                schools.set(ii, user);
                File file = new File(FILE_NAME);
                if (file.exists()) {
                    file.delete();
                }

                try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                    outputStream.writeObject(schools);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            }
        }
     
               System.out.print("00000 "); 
        System.out.print(schools.get(ii).getModelcoeffs()[0]);  
        System.out.print(" 00000 "); 

    }

	@SuppressWarnings("unchecked")
	public List<School> loadSchools() {
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
        return schools;
    }
	public double[] getCoeffs() {
		return coeffs;
	}

	public void setCoeffs(double[] ds) {
		this.coeffs = ds;
	}

}
