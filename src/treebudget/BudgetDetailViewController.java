package treebudget;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;

public class BudgetDetailViewController implements Initializable {
    @FXML private Label nameLabel, immediacyLabel, AROLabel, AmountLabel, ALELabel, CashedAmountLabel, InvestedAmountLabel;
    @FXML private TextField nameField, immediacyField, AROField, AmountField, ALEField, CashedAmountField, InvestedAmountField;
    @FXML private Button OKButton, CancelButton;
    
    @FXML private TextField parentSearchBox;
    @FXML private Button parentSearchButton;
    @FXML private ListView<ObservableTreeBudgetItem> parentSearchResultsListView;
    @FXML private Label parentNameLabel;
    
    private static ObservableTreeBudgetItem budgetItem;
    
    @Override public void initialize(URL url, ResourceBundle rb) {
        if(budgetItem.getParent() == null) parentNameLabel.setText("Parent: is Root Item");
        else parentNameLabel.setText("Parent: " + budgetItem.getParent().getItemName());
        OKButton.setText("OK");
        CancelButton.setText("Cancel");
        nameLabel.setText("Name");
        immediacyLabel.setText("Immediacy");
        AROLabel.setText("ARO");
        AmountLabel.setText("Amount");
        ALELabel.setText("ALE");
        CashedAmountLabel.setText("Cashed");
        InvestedAmountLabel.setText("Invested");
        if(budgetItem.hasChildren()) {
            immediacyField.setEditable(false);
            immediacyField.setStyle("-fx-background-color: #808080;");
            AROField.setEditable(false);
            AROField.setStyle("-fx-background-color: #808080;");
            AmountField.setEditable(false);
            AmountField.setStyle("-fx-background-color: #808080;"); }
        ALEField.setEditable(false);
        ALEField.setStyle("-fx-background-color: #808080;");
        CashedAmountField.setEditable(false);
        CashedAmountField.setStyle("-fx-background-color: #808080;");
        InvestedAmountField.setEditable(false);
        InvestedAmountField.setStyle("-fx-background-color: #808080;");
        nameField.setText(budgetItem.getItemName());
        immediacyField.setText(Integer.toString(budgetItem.getImmediacy()));
        AROField.setText(Double.toString(utils.rd(budgetItem.getARO())));
        AmountField.setText(utils.cashFormat(budgetItem.getAmount()));
        ALEField.setText(utils.cashFormat(budgetItem.getALE()));
        CashedAmountField.setText(utils.cashFormat(budgetItem.getCashedAmount()));
        InvestedAmountField.setText(utils.cashFormat(budgetItem.getInvestedAmount())); }
    
    @FXML public void OKButtonPressed() {
        ObservableTreeBudgetItem item = TreeBudget.getBudget().contains(nameField.getText());
        if(item != null && !budgetItem.equals(item)) {
            utils.spawn("Another item exists with that name.", x -> utils.errorbox(x));
            return; }
        else {
            budgetItem.setItemName(nameField.getText());
            budgetItem.setImmediacy(Integer.parseInt(immediacyField.getText()));
            budgetItem.setARO(Double.parseDouble(AROField.getText()));
            String s = AmountField.getText();
            s = s.replace("$", "");
            s = s.replace(",", "");
            s = s.replace("(", "-");
            s = s.replace(")", "");
            budgetItem.setAmount(Double.parseDouble(s));
            utils.closeCurrentWindow(OKButton);
        }
    }
    
    @FXML public void CancelButtonPressed() {
        if(utils.spawn("Are you sure you want to cancel?", x -> utils.choicebox(x))) {
            utils.closeCurrentWindow(CancelButton);
        }
    }
    
    @FXML public void ParentSearchButtonPressed() {
        if(!parentSearchBox.getText().isEmpty()) {
            parentSearchResultsListView.getItems().setAll(TreeBudget.getBudget().query(parentSearchBox.getText()));
            parentSearchResultsListView.setOnMouseClicked(x -> {
                if(x.getButton().equals(MouseButton.PRIMARY)) {
                    if(x.getClickCount() == 2) {
                        ObservableTreeBudgetItem y = parentSearchResultsListView.getSelectionModel().getSelectedItem();
                        y.addSubItem(budgetItem);
                        parentNameLabel.setText("Current Parent: " + y.getItemName());
                    }
                }
            });
        }
    }
    
    public static void passBudgetItem(ObservableTreeBudgetItem item) {
        if(item != null) {
            budgetItem = item;
        }
    }
    
    public static ObservableTreeBudgetItem passBudgetItem() {
        return budgetItem;
    }
}