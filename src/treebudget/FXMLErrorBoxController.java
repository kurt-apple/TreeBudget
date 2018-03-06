package treebudget;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class FXMLErrorBoxController implements Initializable {
    @FXML private Label boxMessage;
    @FXML private Button okButton;
    @FXML public void handleOkButtonAction() { 
        utils.closeCurrentWindow(okButton); }
    @Override public void initialize(URL url, ResourceBundle rb) { 
        boxMessage.setText(utils.msgString); }     
}
