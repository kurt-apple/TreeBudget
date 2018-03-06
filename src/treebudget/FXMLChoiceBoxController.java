package treebudget;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class FXMLChoiceBoxController implements Initializable {
    @FXML private Label BoxMessage;
    @FXML private Button yesButton, noButton;
    @Override public void initialize(URL url, ResourceBundle rb) { BoxMessage.setText(utils.msgString); }
    @FXML public void handleNoButton() {
        utils.choice = false;
        utils.closeCurrentWindow(noButton);
    }
    @FXML public void handleYesButton() {
        utils.choice = true;
        utils.closeCurrentWindow(yesButton);
}   }
