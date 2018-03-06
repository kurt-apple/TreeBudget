package treebudget;

import java.util.LinkedList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class ObservableTreeBudgetItem {
    private StringProperty ItemName; 	//"foobar"
    private IntegerProperty Immediacy; 	//0-10
    private DoubleProperty ARO;
    private DoubleProperty Amount;         //$
    private DoubleProperty ALE;            //$, calculated
    private DoubleProperty CashedAmount;   //$, calculated
    private DoubleProperty InvestedAmount; //$, calculated
    private DoubleProperty TotalCost;
    private ObservableTreeBudgetItem ParentItem;
    private LinkedList<ObservableTreeBudgetItem> SubItems;
    
    public ObservableTreeBudgetItem(String name) {
        ItemName = new SimpleStringProperty(name);
        Immediacy = new SimpleIntegerProperty();
        ARO = new SimpleDoubleProperty();
        Amount = new SimpleDoubleProperty();
        ALE = new SimpleDoubleProperty();
        CashedAmount = new SimpleDoubleProperty();
        InvestedAmount = new SimpleDoubleProperty();
        SubItems = new LinkedList<>(); 
        TotalCost = new SimpleDoubleProperty(); }
    
    public String getItemName() { 
    	return this.ItemName.get(); }
    
    public Integer getImmediacy() { 
    	return Immediacy.getValue(); }
    
    public Double getARO() { 
    	return ARO.getValue(); }
    
    public Double getAmount() { 
        //if(this.getItemName().equalsIgnoreCase(
            //TreeBudget.getBudget().getItemName())) {
                //System.out.println(this.Amount.get()); }
    	return Amount.getValue(); }
    
    public Double getALE() {	
    	ALE.set(utils.rd(/*Confidence.get()**/ARO.get()*Amount.get()));
    	return ALE.getValue(); }
    
    public Double getCashedAmount() { 
    	if(SubItems.isEmpty()) {
            CashedAmount.set(
                utils.rd(
                    Immediacy.get()*0.1*ARO.get()*Amount.get() 
                    + (1-TreeBudget.getInvestmentConfidence())
                        * (10-Immediacy.get())*0.1*ARO.get()*Amount.get())); }
    	return CashedAmount.getValue(); }
    
    public Double getInvestedAmount() { 
    	if(SubItems.isEmpty()) {
            InvestedAmount.set(utils.rd(ALE.get()*(10-Immediacy.get())*0.1)); }
    	return InvestedAmount.getValue(); }
    
    public Double getTotalCost() {
        TotalCost.set(getInvestedAmount() + getCashedAmount());
        return TotalCost.getValue(); }
    
    public void setItemName(String value) { 
    	if(value != null && value != "") {
            this.ItemName.set(value); 
    	}
    }
    
    public void setImmediacy(int value) { 
    	if(value <= 10 && value >= 0) {
            Immediacy.set(value); 
            refreshData();
    	}
    }
    
    public void setARO(double value) { 
    	if(value != 0) {
            ARO.set(value); 
            refreshData();
    	}
    }
    
    public void setAmount(double value) { 
    	if(value != 0) {
            Amount.set(utils.rd(value)); 
            refreshData();
    	}
    }
    
    public void setALE(double value){}
    
    public void setCashedAmount(double value){}
    
    public void setInvestedAmount(double value){}
    
    public void setTotalCost(double value) {}
    
    public StringProperty ItemNameProperty() {
    	return this.ItemName; }
    
    public IntegerProperty ImmediacyProperty() {
    	return Immediacy; }
    
    public DoubleProperty AROProperty() {
    	return ARO; }
    
    public DoubleProperty AmountProperty() {
    	return Amount; }
    
    public DoubleProperty ALEProperty() {
    	return ALE; }
    
    public DoubleProperty CashedAmountProperty() {
    	return CashedAmount; }
    
    public DoubleProperty InvestedAmountProperty() {
    	return InvestedAmount; }
    
    public DoubleProperty TotalCostProperty() {
        return TotalCost; }
    
    public void refreshData() {
    	if(SubItems.size() > 0) {
            Immediacy.set(10);
            ARO.set(1.0);
            double sumamount = 0.0;
            for(ObservableTreeBudgetItem Item : SubItems) {
                sumamount += Item.getALE(); }
            Amount.set(sumamount);
            //if(this.getItemName().equalsIgnoreCase(TreeBudget.getBudget()
                //.getItemName())) System.out.println(this.Amount.get());
            ALE.set(sumamount);
            sumamount = 0.0;
            for(ObservableTreeBudgetItem Item : SubItems) {
                sumamount += Item.getCashedAmount(); }
            CashedAmount.set(sumamount);
            sumamount = 0.0;
            for(ObservableTreeBudgetItem Item : SubItems) {
                sumamount += Item.getInvestedAmount(); }
            InvestedAmount.set(sumamount);
            TotalCost.set(InvestedAmount.get() + CashedAmount.get());
            if(ParentItem != null) {
                ParentItem.refreshData(); 
            }
        }
        else {
            ALE.set(/*Confidence.get()**/ARO.get()*Amount.get());
            CashedAmount.set(Immediacy.get()*0.1*ARO.get()*Amount.get() 
                + (1-TreeBudget.getInvestmentConfidence())
                    * (10-Immediacy.get())*0.1*ARO.get()*Amount.get());
            InvestedAmount.set(ALE.get()*(10-Immediacy.get())*0.1);
            TotalCost.set(InvestedAmount.get() + CashedAmount.get());
            if(ParentItem != null) {
                ParentItem.refreshData(); 
            } 
        }
    }
    
    public TreeItem<ObservableTreeBudgetItem> toTreeItem() {
        TreeItem<ObservableTreeBudgetItem> treeItem = new TreeItem<>(this);
        this.SubItems.forEach(x -> treeItem.getChildren().add(x.toTreeItem()));
        return treeItem; }
    
    public boolean hasChildren() {
        return !SubItems.isEmpty(); }
    
    public ObservableTreeBudgetItem contains(String query) {
        if(ItemName.get().equalsIgnoreCase(query)) {
            return this; }
        else {
            ObservableTreeBudgetItem item2 = null;
            for(ObservableTreeBudgetItem item : SubItems) {
                item2 = item.contains(query);
                if(item2 != null) {
                    return item2;
                }
            }
            return null;
        }
    }
    
    public void addSubItem(ObservableTreeBudgetItem subItem) {
        if(subItem != null) {
            SubItems.add(subItem);
            if(subItem.getParent() != null) {
                subItem.getParent().getChildren().remove(subItem); }
            subItem.addParent(this);
            subItem.refreshData();
        }
    }
    
    private void addParent(ObservableTreeBudgetItem parentItem) {
        if(parentItem != null) {
            this.ParentItem = parentItem;
        }
    }
    
    public LinkedList<ObservableTreeBudgetItem> getChildren() {
        return SubItems; }
    
    public ObservableTreeBudgetItem getParent() {
        return ParentItem; }
    
    public LinkedList<String> toStrings() {
        LinkedList<String> linesToWrite = new LinkedList<>();
        if(SubItems.isEmpty()) {
            linesToWrite.add("i " + this.ItemName.get() + "\t" 
                + this.Amount.get() + "\t" + this.ARO.get() 
                + "\t" + this.Immediacy.get());
            return linesToWrite; }
        else {
            SubItems.forEach(x -> linesToWrite.addAll(x.toStrings()));
            StringBuilder sb = new StringBuilder("p " + this.ItemName.get());
            SubItems.forEach(x -> sb.append("\t" + x.getItemName()));
            linesToWrite.add(sb.toString()); }
        return linesToWrite; }
    
    public ObservableList<ObservableTreeBudgetItem> query(String query) {
        ObservableList<ObservableTreeBudgetItem> resultsList = 
            FXCollections.observableList(
                new LinkedList<ObservableTreeBudgetItem>());
        if(this.getItemName().toLowerCase().contains(query.toLowerCase())) {
            resultsList.add(this); }
        SubItems.forEach(x -> resultsList.addAll(x.query(query)));
        return resultsList; }
    
    @Override public String toString() { 
        return ItemName.get(); }
    
    public void deleteChild(ObservableTreeBudgetItem toDelete) {
        SubItems.remove(toDelete);
        toDelete.getChildren().forEach(x -> addSubItem(x));
        refreshData();
    }
}
