package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {
    private ObservableList<ObservableList<String>> data;

    public CSVReader(File file) {
        data = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                ObservableList<String> row = FXCollections.observableArrayList();
                for (String field : fields) {
                    row.add(field);
                }
                data.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<ObservableList<String>> getData() {
        return data;
    }
}
