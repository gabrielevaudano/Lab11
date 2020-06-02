/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.rivers;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.rivers.model.Model;
import it.polito.tdp.rivers.model.River;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxRiver"
    private ComboBox<River> boxRiver; // Value injected by FXMLLoader

    @FXML // fx:id="txtStartDate"
    private TextField txtStartDate; // Value injected by FXMLLoader

    @FXML // fx:id="txtEndDate"
    private TextField txtEndDate; // Value injected by FXMLLoader

    @FXML // fx:id="txtNumMeasurements"
    private TextField txtNumMeasurements; // Value injected by FXMLLoader

    @FXML // fx:id="txtFMed"
    private TextField txtFMed; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    

    @FXML
    void handleRiver(ActionEvent event) {
		River r = boxRiver.getSelectionModel().getSelectedItem();
    	if (r != null)
    	{
    		model.addFlows(r);
    		txtStartDate.setText(r.getMinDay().toString());
    		txtEndDate.setText(r.getMaxDay().toString());
    		txtFMed.setText("" + r.getFlowAvg());
    		txtNumMeasurements.setText("" + r.getFlows().size());
    	}
    }

    @FXML
    void handleSimula(ActionEvent event) {
    	try {

	    	River r = boxRiver.getSelectionModel().getSelectedItem();
	    	double k = 0.0;
	    	
	    	if (r==null)
	    		txtResult.setText("Non è stato selezionato alcun fiume. Ritenta dopo aver effettuato la scelta.");
    	
    		if (txtK.getText() == "")
    			throw new NullPointerException("Non è stato inserito alcun valore relativo a K. Ritenta dopo aver compiuto la scelta.");
    		k = Double.parseDouble(txtK.getText());
    		
        	
        	if (k== 0.0)
        		txtResult.setText("Inserire un valore maggiore di zero");
        	
        	String simStats  = model.getStats(r, k);
        	
        	txtResult.setText(simStats);
    		
    	} catch (Exception e) {
    		txtResult.setText(e.getMessage());
    	}

    }

    @FXML
    void initialize() {
        assert boxRiver != null : "fx:id=\"boxRiver\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtStartDate != null : "fx:id=\"txtStartDate\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtEndDate != null : "fx:id=\"txtEndDate\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNumMeasurements != null : "fx:id=\"txtNumMeasurements\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtFMed != null : "fx:id=\"txtFMed\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	boxRiver.getItems().addAll(model.getAllRivers());
    }
}
