package application;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            // création du registre RMI sur le port 50100
        	Registry registry = LocateRegistry.createRegistry(14033);
        	@SuppressWarnings("unused")
			Registry registry_ = LocateRegistry.getRegistry("127.0.0.1", 14033);

            //création de l'objet ExamPredictionImpl et enregistrement dans le registre RMI
            PredictionImpl obj = new PredictionImpl();
            SchoolServiceImpl obj2=new SchoolServiceImpl();
            ModelTrainingImpl obj3=new ModelTrainingImpl();
            System.out.println(" prêt");
            registry.rebind("Prediction", obj);
            registry.rebind("SchoolService", obj2);
            registry.rebind("ModelTraining", obj3);
            System.out.println("Serveur prêt");
        } catch (Exception e) {
            System.err.println("Erreur de serveur : " + e.getMessage());
        }
    }
}
