/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtCalorie"
    private TextField txtCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="txtPassi"
    private TextField txtPassi; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCorrelate"
    private Button btnCorrelate; // Value injected by FXMLLoader

    @FXML // fx:id="btnCammino"
    private Button btnCammino; // Value injected by FXMLLoader

    @FXML // fx:id="boxPorzioni"
    private ComboBox<String> boxPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCammino(ActionEvent event) {
    	txtResult.clear();
    	//txtResult.appendText("Cerco cammino peso massimo...");
    	String porzione = boxPorzioni.getValue() ;
    	
    	if(porzione==null) {
    		txtResult.appendText("ERRORE: devi selezionare una porzione\n");
    		return ;
    	}
    	Integer n;
    	try {
    		n=Integer.parseInt(txtPassi.getText());
    	}catch(NumberFormatException e) {
    		txtResult.appendText("ERRORE: devi inserire un numero\n");
    		return;
    	}
    	model.cercaCammino(porzione, n);
    	if(model.getCamminoMax()==null) {
    		txtResult.appendText("Non esiste un cammino di lunghezza N");
    	} else {
    		txtResult.appendText("Trovato un cammino di peso "+model.getPesoMax()+"\n");
    		for(String vertice:model.getCamminoMax()) {
    			txtResult.appendText(vertice+"\n");
    		}
    	}
    	
    
    }

    @FXML
    void doCorrelate(ActionEvent event) {
    	txtResult.clear();
    	String porzione = boxPorzioni.getValue() ;
    	
    	if(porzione==null) {
    		txtResult.appendText("ERRORE: devi selezionare una porzione\n");
    		return ;
    	}
    	
    	txtResult.appendText("Porzioni correlate a: "+porzione+"\n");
    	List<Adiacenza> adiacenti = model.getAdiacenti(porzione);
    	for(Adiacenza a : adiacenti) {
    		if(porzione.equals(a.getName1())){
    			txtResult.appendText(String.format("%s  %d\n", a.getName2(), a.getPeso()));
    	} else if(porzione.equals(a.getName2())) {
    		txtResult.appendText(String.format("%s  %d\n", a.getName1(), a.getPeso()));
    		}
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Creazione grafo...");
    	txtResult.clear();
    	//txtResult.appendText("Creazione grafo...");
    	String p=txtCalorie.getText();
    	Integer calories;
    	try {
    		calories=Integer.parseInt(p);
    		
    		
    	}catch(NumberFormatException e) {
    		txtResult.appendText("Devi inserire un numero\n");
    		return;
    	}
    	model.creaGrafo(calories);
    	String msg = model.creaGrafo(calories);
    	txtResult.appendText(msg);
    	boxPorzioni.getItems().clear();
    	boxPorzioni.getItems().addAll(model.getVertici());
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtCalorie != null : "fx:id=\"txtCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtPassi != null : "fx:id=\"txtPassi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCorrelate != null : "fx:id=\"btnCorrelate\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCammino != null : "fx:id=\"btnCammino\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxPorzioni != null : "fx:id=\"boxPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
