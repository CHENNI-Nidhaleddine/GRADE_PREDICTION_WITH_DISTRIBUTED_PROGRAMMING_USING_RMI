module GUI_IRIAD {
	requires javafx.controls;
	requires java.rmi;
	requires javafx.fxml;
	
	opens application to javafx.graphics, javafx.fxml;
}
