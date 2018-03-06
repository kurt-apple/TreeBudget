package treebudget;

import java.text.NumberFormat;
import java.util.function.Predicate;
import javafx.scene.control.Button;
import javafx.scene.control.TreeTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class utils {
    public static boolean choicebox(String newMessage) {
        msgString = newMessage;
        TidierFXML cbLoader = 
            new TidierFXML("FXMLChoiceBox.fxml", Modality.APPLICATION_MODAL);
        cbLoader.getStage().showAndWait();
        return choice; }
    
    public static boolean errorbox(String newMessage) {
        msgString = newMessage;
        TidierFXML ebLoader = 
            new TidierFXML("FXMLErrorBox.fxml", Modality.APPLICATION_MODAL);
        ebLoader.getStage().showAndWait();
        return true; }
    
    public static boolean spawn(String message, Predicate<String> action) { 
        return action.test(message); }
    public static void closeCurrentWindow(Button b) {
        ((Stage) b.getScene().getWindow()).close(); }
    public static String msgString;
    public static boolean choice = false;
    
    private static NumberFormat currencyFormat = 
        NumberFormat.getCurrencyInstance();
    
    public static String cashFormat(double toFormat) {
        return currencyFormat.format(toFormat); }
    
    public static Double cashParse(String toParse) {
        try {
            return (Double)currencyFormat.parse(toParse); }
        catch (Exception e) {
            return null;
        }
    }
    
    public static TreeTableCell<ObservableTreeBudgetItem, Double> 
    getCurrencyTreeTableCell() {
        return  new TreeTableCell<ObservableTreeBudgetItem, Double>() {
            @Override protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if(empty) {
                    setText(null); }
                else {
                    setText(currencyFormat.format(price));
                }
            }
        };
    }
    
    //rounds up, to two places
    public static double rd(double d) {
        return Math.round(d * 100) / 100.0;
    }
}
