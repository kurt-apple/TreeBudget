package treebudget;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import javafx.application.Application;
import javafx.stage.Stage;

public class TreeBudget extends Application {
    private static double InvestmentConfidence;
    private static double InvestmentYield;
    private static ObservableTreeBudgetItem Budget;
    private static ObservableTreeBudgetItem tmpParentBudgetItem;
    private static final String filename = "default.cash";
    private static String line;
    
    public static double getInvestmentConfidence() {
        return InvestmentConfidence; }
    
    public static double getInvestmentYield() {
        return InvestmentYield; }
    
    public static ObservableTreeBudgetItem getBudget() {
        return Budget; }
    
    @Override public void start(Stage primaryStage) {
        loadFromFile();
        TidierFXML mainWindow = new TidierFXML("BudgetOverview.fxml");
        Stage mainStage = mainWindow.getStage();
        mainStage.setTitle("Tree Budget");
        mainStage.setMaximized(true);
        mainStage.show(); }
    
    @Override public void stop() {
        saveToFile(); }
    
    public static void main(String[] args) {
        launch(args); }
    
    public static String readLine(BufferedReader x) {
        try { 
            return x.readLine(); }
        catch(IOException e) { utils.spawn("error reading line.", a -> utils.errorbox(a));
        return null; 
        }
    }
    
    private static int failures = 5;
    public static BufferedReader makeReader() {
        try {
            BufferedReader x = new BufferedReader(new FileReader(filename));
            return x; }
        catch(FileNotFoundException e) {
            File f = new File(filename);
            try {
                f.createNewFile(); }
            catch (IOException g) {
                failures--; }
            failures--;
            if (failures < 0) {
                System.exit(404); }
            return makeReader();
        }
    }
    
    public static void loadFromFile() {
        BufferedReader BudgetReader = makeReader();
        if (BudgetReader != null) {
            String TreeLayerTemp[];
            line = readLine(BudgetReader);
            if(line != null) {
                if(line.startsWith("Budget: Name ")) {
                    TreeLayerTemp = line.substring(13).split("\t");
                    Budget = new ObservableTreeBudgetItem(TreeLayerTemp[0]);
                    InvestmentConfidence = Double.parseDouble(TreeLayerTemp[1].substring(11));
                    InvestmentYield = Double.parseDouble(TreeLayerTemp[2].substring(9)); }
                line = readLine(BudgetReader);
                while(line != null) {
                    if(line.startsWith("p ")) {
                        TreeLayerTemp = line.substring(2).split("\t");
                        tmpParentBudgetItem = new ObservableTreeBudgetItem(TreeLayerTemp[0]);
                        for(int i = 1; i < TreeLayerTemp.length; i++) {
                            tmpParentBudgetItem.addSubItem(Budget.contains(TreeLayerTemp[i])); }
                        Budget.addSubItem(tmpParentBudgetItem); }
                    else if(line.startsWith("i ")) {
                        TreeLayerTemp = line.substring(2).split("\t");
                        ObservableTreeBudgetItem tmpBudgetItem = new ObservableTreeBudgetItem(TreeLayerTemp[0]);
                        tmpBudgetItem.setAmount(Double.parseDouble(TreeLayerTemp[1]));
                        tmpBudgetItem.setARO(Double.parseDouble(TreeLayerTemp[2]));
                        tmpBudgetItem.setImmediacy(Integer.parseInt(TreeLayerTemp[3]));
                        Budget.addSubItem(tmpBudgetItem); }
                    line = readLine(BudgetReader); 
                }
            }
            else {
                Budget = new ObservableTreeBudgetItem("Budget");
                InvestmentConfidence = 0.6;
                InvestmentYield = 0.05;
                saveToFile(); 
            }
        }
        else {
            Budget = new ObservableTreeBudgetItem("Budget");
            InvestmentConfidence = 0.6;
            InvestmentYield = 0.05;
            saveToFile(); 
        }
    }
    
    public static void saveToFile() {
        LinkedList<String> linesToWrite = new LinkedList<>();
        linesToWrite.add("Budget: Name " + Budget.getItemName() + "\tConfidence: " + InvestmentConfidence + "\tExpYield: " + InvestmentYield);
        for(ObservableTreeBudgetItem item : Budget.getChildren()) {
            linesToWrite.addAll(item.toStrings()); }
        BufferedWriter filew;
        try { 
            filew = new BufferedWriter(new FileWriter(filename)); 
            for(String iString : linesToWrite) {
                filew.append(iString);
                filew.newLine(); }
            filew.flush();
            filew.close(); }
        catch(IOException e) {
            return; 
        }
    }
}
