/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.poweroutages;

import java.net.URL;
import java.util.ResourceBundle;
import it.polito.tdp.poweroutages.model.Model;
import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="cmbNerc"
    private ComboBox<Nerc> cmbNerc; // Value injected by FXMLLoader

    @FXML // fx:id="txtYears"
    private TextField txtYears; // Value injected by FXMLLoader

    @FXML // fx:id="txtHours"
    private TextField txtHours; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    private Model model;
    
    @FXML
    void doRun(ActionEvent event) {
    	txtResult.clear();
    	int anni = 0;
    	int ore = 0;
    	Nerc nerc = cmbNerc.getValue();
    	
    	try {
    		anni = Integer.parseInt(txtYears.getText());
    		ore = Integer.parseInt(txtHours.getText());
    	} catch(NumberFormatException e) {
    		e.printStackTrace();
    		System.out.println("Inserire dei valori numerici per anni e ore");
    	}
    	
    	if(anni>14 || anni<1) {
    		System.out.println("Inserire un numero di anni compreso tra 1 e 14");
    	}
    	
    	if(ore<0) {
    		System.out.println("Inserire un numero di ore positivo");
    	}
    	
    	if(nerc==null) {
    		System.out.println("Selezionare un Nerc dalla lista");
    	}
    	
    	int count = 0;
    	for(PowerOutage p : this.model.trovaSequenza(anni, ore, nerc)) {
    		if(count==0) {
    			txtResult.appendText("Totale utenti coinvolti: " + this.model.getMaxCustomers() + "\n");
    			count++;
    		}
    		txtResult.appendText(p+"\n");
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert cmbNerc != null : "fx:id=\"cmbNerc\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtYears != null : "fx:id=\"txtYears\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtHours != null : "fx:id=\"txtHours\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        
        // Utilizzare questo font per incolonnare correttamente i dati;
        txtResult.setStyle("-fx-font-family: monospace");
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	for(Nerc n : this.model.getNercList()) {
        	cmbNerc.getItems().add(n);
        }
    }
}
