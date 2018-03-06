package treebudget;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Modality;

public class BudgetOverviewController implements Initializable {
    @FXML private Button detailsButton, addButton, deleteButton;
    @FXML private TreeTableView<ObservableTreeBudgetItem> BudgetTreeView;
    @FXML private TreeTableColumn<ObservableTreeBudgetItem, String> BudgetTreeItemName;
    @FXML private TreeTableColumn<ObservableTreeBudgetItem, Integer> BudgetTreeItemImmediacy;
    @FXML private TreeTableColumn<ObservableTreeBudgetItem, Double> BudgetTreeItemARO;
    @FXML private TreeTableColumn<ObservableTreeBudgetItem, Double> BudgetTreeItemAmount;
    @FXML private TreeTableColumn<ObservableTreeBudgetItem, Double> BudgetTreeItemCashedAmount;
    @FXML private TreeTableColumn<ObservableTreeBudgetItem, Double> BudgetTreeItemInvestedAmount;
    @FXML private TreeTableColumn<ObservableTreeBudgetItem, Double> BudgetTreeItemTotalCost;
    
    @Override public void initialize(URL url, ResourceBundle rb) {
        detailsButton.setText("Details");
        addButton.setText("Add SubItem");
        deleteButton.setText("Delete Item");
        BudgetTreeView.setRoot(TreeBudget.getBudget().toTreeItem());
        BudgetTreeItemName = new TreeTableColumn<>("Name");
        BudgetTreeItemImmediacy = new TreeTableColumn<>("Immediacy");
        BudgetTreeItemARO = new TreeTableColumn<>("ARO");
        BudgetTreeItemAmount = new TreeTableColumn<>("Amt");
        BudgetTreeItemCashedAmount = new TreeTableColumn<>("Cashed");
        BudgetTreeItemInvestedAmount = new TreeTableColumn<>("Invested");
        BudgetTreeItemTotalCost = new TreeTableColumn<>("Total Cost");
        BudgetTreeItemImmediacy.setMinWidth(80);
        BudgetTreeItemARO.setMinWidth(80);
        BudgetTreeItemAmount.setMinWidth(80);
        BudgetTreeItemCashedAmount.setMinWidth(80);
        BudgetTreeItemInvestedAmount.setMinWidth(80); //400 not counting name
        BudgetTreeItemTotalCost.setMinWidth(80);
        BudgetTreeItemName.prefWidthProperty().bind(BudgetTreeView.widthProperty().subtract(480));
        BudgetTreeItemName.setCellValueFactory(new TreeItemPropertyValueFactory<>("ItemName"));
        BudgetTreeItemImmediacy.setCellValueFactory(new TreeItemPropertyValueFactory<>("Immediacy"));
        BudgetTreeItemARO.setCellValueFactory(new TreeItemPropertyValueFactory<>("ARO"));
        BudgetTreeItemAmount.setCellValueFactory(new TreeItemPropertyValueFactory<>("Amount"));
        BudgetTreeItemAmount.setCellFactory(tc -> utils.getCurrencyTreeTableCell());
        BudgetTreeItemCashedAmount.setCellValueFactory(new TreeItemPropertyValueFactory<>("CashedAmount"));
        BudgetTreeItemCashedAmount.setCellFactory(tc -> utils.getCurrencyTreeTableCell());
        BudgetTreeItemInvestedAmount.setCellValueFactory(new TreeItemPropertyValueFactory<>("InvestedAmount"));
        BudgetTreeItemInvestedAmount.setCellFactory(tc -> utils.getCurrencyTreeTableCell());
        BudgetTreeItemTotalCost.setCellValueFactory(new TreeItemPropertyValueFactory<>("TotalCost"));
        BudgetTreeItemTotalCost.setCellFactory(tc -> utils.getCurrencyTreeTableCell());
        
        BudgetTreeView.getColumns().setAll(BudgetTreeItemName, BudgetTreeItemAmount, BudgetTreeItemARO, BudgetTreeItemCashedAmount, BudgetTreeItemInvestedAmount, BudgetTreeItemTotalCost, BudgetTreeItemImmediacy);
        BudgetTreeView.setShowRoot(true); }
    
    @FXML public void DetailsButtonClicked() {
        TreeItem<ObservableTreeBudgetItem> selectedTreeItem = BudgetTreeView.getSelectionModel().getSelectedItem();
        if(selectedTreeItem != null) {
            BudgetDetailViewController.passBudgetItem(selectedTreeItem.getValue());
            TidierFXML detailView = new TidierFXML("BudgetDetailView.fxml", Modality.APPLICATION_MODAL);
            detailView.getStage().showAndWait(); }
        else {
            utils.spawn("Select a budget item to view its details or modify.", x -> utils.errorbox(x));
        }
        refreshTree();
    }
    
    @FXML public void AddButtonClicked() {
        TreeItem<ObservableTreeBudgetItem> selectedTreeItem = BudgetTreeView.getSelectionModel().getSelectedItem();
        ObservableTreeBudgetItem selectedBudgetItem = selectedTreeItem == null ? TreeBudget.getBudget() : selectedTreeItem.getValue();
        BudgetDetailViewController.passBudgetItem(new ObservableTreeBudgetItem("New Item"));
        TidierFXML addItemView = new TidierFXML("BudgetDetailView.fxml", Modality.APPLICATION_MODAL);
        addItemView.getStage().showAndWait();
        selectedBudgetItem.addSubItem(BudgetDetailViewController.passBudgetItem());
        refreshTree();
    }
    
    @FXML public void DeleteButtonClicked() {
        TreeItem<ObservableTreeBudgetItem> selectedTreeItem = BudgetTreeView.getSelectionModel().getSelectedItem();
        if(selectedTreeItem != null) {
            if(utils.spawn("Are you sure you want to delete this?", x -> utils.choicebox(x))) {
                ObservableTreeBudgetItem selectedBudgetItem = selectedTreeItem.getValue();
                selectedBudgetItem.getParent().deleteChild(selectedBudgetItem);
                refreshTree();
            }
        }
        else {
            utils.spawn("Select a budget item to delete.", x -> utils.errorbox(x));
        }
    }
    
    @FXML public void refreshTree() {
        BudgetTreeView.setRoot(TreeBudget.getBudget().toTreeItem());
    }
}
