package application;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client extends Application {
	
	

    SchoolService auth;
    ModelTraining train;
  
    float[][] result;
    Prediction pred;
    private         ObservableList<ObservableList<String>> dataToSent ;

    boolean yes=false;
    public void setData(ObservableList<ObservableList<String>> dataToSent) {
    	this.dataToSent=dataToSent;
    }
    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) throws Exception {
    	//Loading XMLs
   	    FXMLLoader loader_conn = new FXMLLoader(getClass().getResource("Auth.fxml"));
        Parent connexion = loader_conn.load();
        FXMLLoader loader_ins = new FXMLLoader(getClass().getResource("Inscrire.fxml"));
        Parent inscription = loader_ins.load();
 	    FXMLLoader loader_file = new FXMLLoader(getClass().getResource("OpenFile.fxml"));
 	    Parent upload = loader_file.load();


        Scene conn_scene=new Scene(connexion);
        Scene ins_scene=new Scene(inscription);
        Scene upload_scene=new Scene(upload);
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1",14033);
            auth = (SchoolService) registry.lookup("SchoolService");
            train=(ModelTraining) registry.lookup("ModelTraining");
            pred=(Prediction) registry.lookup("Prediction");

        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
        
        Connexion(primaryStage,loader_conn,loader_ins,loader_file,conn_scene,ins_scene,upload_scene);
        
        primaryStage.setTitle("Calculateur de notes finales");
        primaryStage.setScene(conn_scene);
        primaryStage.show();

    }
    private void Connexion(Stage primaryStage,FXMLLoader loader_conn,FXMLLoader loader_ins ,FXMLLoader loader_file,Scene connexion,Scene inscription,Scene upload_scene) {
    	  Button conn_btn = (Button) loader_conn.getNamespace().get("connect");
          Button ins_btn = (Button) loader_conn.getNamespace().get("registre");

          TextField nom = (TextField) loader_conn.getNamespace().get("nom");
          TextField mot_de_passe = (TextField) loader_conn.getNamespace().get("pass");
    
          ins_btn.setOnAction(e-> {
        	  primaryStage.close();
        	  Inscription(primaryStage,loader_conn,loader_ins,loader_file,connexion,inscription,upload_scene);
        	  primaryStage.setScene(inscription);
        	  primaryStage.show();
          });
          conn_btn.setOnAction(e-> {
              String nom_txt = nom.getText();
              String mot_de_pass_txt = mot_de_passe.getText();
              
              if(nom_txt.isEmpty() || mot_de_pass_txt.isEmpty()) {
            	   Alert invalidInputAlert = new Alert(Alert.AlertType.ERROR);
                   invalidInputAlert.setTitle("Invalid input");
                   invalidInputAlert.setContentText("Les champs sont vides :(");
                   invalidInputAlert.showAndWait();
              }else {
            	  School s=null;
                  try {
      				s= auth.loginUser(nom_txt, mot_de_pass_txt);
      			} catch (RemoteException e1) {
      				// TODO Auto-generated catch block
      				e1.printStackTrace();
      			}
                  if(s!=null) {
                     	try {
//      					createPredictScene(primaryStage,nom_txt);
      					createPredictScene(primaryStage,nom_txt,loader_conn,loader_ins,loader_file,connexion,inscription,upload_scene);

      				} catch (Exception e1) {
      					// TODO Auto-generated catch block
      					e1.printStackTrace();
      				}             
                  }
                  else {
                      Alert alert = new Alert(Alert.AlertType.ERROR);
                      alert.setTitle("Login Failed");
                      alert.setHeaderText(null);
                      alert.setContentText("Invalid name or password");
                      alert.showAndWait();
                  }  
              }
             
          });
    }
    private void Inscription(Stage primaryStage,FXMLLoader loader_conn,FXMLLoader loader_ins ,FXMLLoader loader_file,Scene connexion,Scene inscription,Scene upload_scene) {
  	    Button conn_btn = (Button) loader_ins.getNamespace().get("connect");
        Button ins_btn = (Button) loader_ins.getNamespace().get("registre");

        TextField nom = (TextField) loader_ins.getNamespace().get("nom");
        TextField mot_de_passe = (TextField) loader_ins.getNamespace().get("pass");
        TextField confirm_mot_de_passe = (TextField) loader_ins.getNamespace().get("confirm");
        
        conn_btn.setOnAction(e-> {	
      	  primaryStage.close();
      	  
      	  Connexion(primaryStage,loader_conn,loader_ins,loader_file,connexion,inscription,upload_scene);
      	  primaryStage.setScene(connexion);
      	  primaryStage.show();
        });
////        
        ins_btn.setOnAction(e-> {
            String nom_txt = nom.getText();
            String mot_de_pass_txt = mot_de_passe.getText();
            String confirm_mot_de_pass_txt = confirm_mot_de_passe.getText();

            if(nom_txt.isEmpty() || mot_de_pass_txt.isEmpty() || confirm_mot_de_pass_txt.isEmpty() ) {
          	   Alert invalidInputAlert = new Alert(Alert.AlertType.ERROR);
                 invalidInputAlert.setTitle("Invalid input");
                 invalidInputAlert.setContentText("Les champs sont vides :(");
                 invalidInputAlert.showAndWait();
            }else {
            	 boolean successRegister=false;
                 try {
     				successRegister= auth.registerUser(nom_txt, mot_de_pass_txt,confirm_mot_de_pass_txt);
     			} catch (RemoteException e1) {
     				// TODO Auto-generated catch block
     				e1.printStackTrace();
     			}
                 if(successRegister) {
                   uploadFileScene(primaryStage,nom_txt,loader_file,upload_scene,loader_conn,loader_ins,connexion,inscription);
                 }
                 else {
                 	   Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Registration Failed");
                        alert.setHeaderText(null);
                        alert.setContentText("All fields are required");
                        alert.showAndWait();
                 }  
          	  
            }
           
        });
  }
  
    private ObservableList<ObservableList<String>>  uploadFileScene(Stage primaryStage,String email,FXMLLoader loader_file,Scene upload_scene,FXMLLoader loader_conn,FXMLLoader loader_ins,Scene connexion,Scene inscription) {
        primaryStage.close();
        Button openButton = (Button) loader_file.getNamespace().get("inserer");
        Button train_btn = (Button) loader_file.getNamespace().get("entrainer");
        @SuppressWarnings("unchecked")
		TableView<ObservableList<String>> tableView=(TableView<ObservableList<String>>) loader_file.getNamespace().get("table");
      
        String[] columnNames = {"Note1","Note2","Note3","Moy"};
        openButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open CSV File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                CSVReader csvReader = new CSVReader(file);
                tableView.setItems(csvReader.getData());
                dataToSent = csvReader.getData();
                int numRows = csvReader.getData().size();
                int numCols = csvReader.getData().get(0).size();
                result = new float[numRows][numCols];

                for (int i = 0; i < numRows; i++) {
                    ObservableList<String> row = csvReader.getData().get(i);
                    for (int j = 0; j < numCols; j++) {
                        result[i][j] = Float.parseFloat(row.get(j));
                        System.out.print("----->");
                        System.out.print(result[i][j]);
                        System.out.print(result[i][2]);
                        System.out.print("\n");

                    }
                }
              
                for (int i = 0; i <csvReader.getData().get(0).size(); i++) {
                        final int colIndex = i;
                        TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames[i]);
                        column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(colIndex)));
                        tableView.getColumns().add(column);
                 }
            }
        });
        train_btn.setOnAction(e->{
        	  try {
                  	train.loadTrainingData(result,email);
  					double[] tt=train.getCoeffs();
  					System.out.print("---------------");
  					System.out.print(tt[0]);
  					System.out.print("---------------");
  					createPredictScene(primaryStage,email,loader_conn,loader_ins,loader_file,connexion,inscription,upload_scene);
  				} catch (Exception e1) {
  					// TODO Auto-generated catch block
  					e1.printStackTrace();
  				}
        });

        primaryStage.setScene(upload_scene);
        primaryStage.show(); 	
        return dataToSent;
    }

    private void createPredictScene(Stage primaryStage,String email,FXMLLoader loader_conn,FXMLLoader loader_ins ,FXMLLoader loader_file,Scene connexion,Scene inscription,Scene upload_scene) throws Exception{
    	 primaryStage.close();
    	 FXMLLoader loader = new FXMLLoader(getClass().getResource("Sample.fxml"));
         Parent root = loader.load();
         FXMLLoader loader2 = new FXMLLoader(getClass().getResource("Result.fxml"));
         @SuppressWarnings("unused")
		Parent root2 = loader2.load();
         // Retrieve the button by fx:id
         Button btn = (Button) loader.getNamespace().get("btn");
         Button reset = (Button) loader.getNamespace().get("reset");
         Button logout=(Button) loader.getNamespace().get("logout");
         TextField grade1 = (TextField) loader.getNamespace().get("grade1");
         TextField grade2 = (TextField) loader.getNamespace().get("grade2");
         TextField grade3 = (TextField) loader.getNamespace().get("grade3");
         grade1.setText("0");grade2.setText("0");grade3.setText("0");
         @SuppressWarnings("unused")
		TextField result = (TextField) loader2.getNamespace().get("result");
         Alert a=new Alert(Alert.AlertType.NONE);
         a.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> a.close());

         @SuppressWarnings("unused")
		ButtonType closeButton = new ButtonType("Close", ButtonData.CANCEL_CLOSE);
         logout.setOnAction(e->{
        	  primaryStage.close();
          	  Connexion(primaryStage,loader_conn,loader_ins,loader_file,connexion,inscription,upload_scene);
          	  primaryStage.setScene(connexion);
          	  primaryStage.show();
        	 });
           // Set the button's action
         btn.setOnAction(e-> {
         	// Récupération des notes saisies par l'utilisateur
             float grade11 = Float.parseFloat(grade1.getText());
             float grade22 = Float.parseFloat(grade2.getText());
             float grade33 = Float.parseFloat(grade3.getText());
             boolean validInput = true;
             try {
              
                 if (grade11 < 0 || grade11 > 20 || grade22 < 0 || grade22 > 20 || grade33 < 0 || grade33 > 20) {
                     validInput = false;
                 }
             } catch (NumberFormatException ex) {
                 validInput = false;
             }

             if (validInput) {

             double prediction=0.0;
          
                 // Appel de la méthode distante pour effectuer la prédiction
                 try {
					prediction = pred.predictGrade(grade11, grade22, grade33,email);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//            	 prediction =0;
                 String grade;
                 if (prediction < 10) {
                     grade = "Failed";
                     DialogPane dialogPane = a.getDialogPane();
                     dialogPane.setStyle("-fx-background-color: #ffd6c9;-fx-font-family: Arial; -fx-font-size: 12px; -fx-font-weight: bold;"); 
                 } else {
                     grade = "Success";
                     DialogPane dialogPane = a.getDialogPane();
                     dialogPane.setStyle("-fx-background-color: #e0ffcc;-fx-font-family: Arial; -fx-font-size: 12px; -fx-font-weight: bold;");

                 }
              // Set the content text alignment to center
                 a.setTitle("Resultats");
              // Set the content text alignment to center
                 Label label1 = new Label(grade);
                 label1.setAlignment(Pos.CENTER);
                 Label label2 = new Label("La note estimée : " + String.format("%.2f", prediction));
                 label2.setAlignment(Pos.CENTER);

                 VBox vbox = new VBox(label1, label2);
                 vbox.setAlignment(Pos.CENTER);
                 vbox.setSpacing(10);
                 a.getDialogPane().setContent(vbox);                       
                 // Show the alert and wait for the user's response
                 a.showAndWait();          
         }  else {
             // Show an alert for invalid input
             Alert invalidInputAlert = new Alert(Alert.AlertType.ERROR);
             invalidInputAlert.setTitle("Invalid input");
             invalidInputAlert.setContentText("Please enter valid input values between 0 and 20.");
             invalidInputAlert.showAndWait();
         }});

         reset.setOnAction(e -> {
             grade1.setText("0");
             grade2.setText("0");
             grade3.setText("0");
         });
       
         primaryStage.setTitle("Calculateur de notes finales");
         primaryStage.setScene(new Scene(root));
         primaryStage.show();
    	
    }
        public static void main(String[] args) {
            launch(args);
        }
}

