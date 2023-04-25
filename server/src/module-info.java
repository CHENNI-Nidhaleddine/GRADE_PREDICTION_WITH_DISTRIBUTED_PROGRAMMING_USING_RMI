module Serveur_IRIAD {
	requires javafx.controls;
	requires java.rmi;
	requires commons.math3;
	
	opens application to javafx.graphics, javafx.fxml;
	exports application;
}
