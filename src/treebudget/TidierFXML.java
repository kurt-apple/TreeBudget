package treebudget;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

// :)
public class TidierFXML {
    private FXMLLoader a;
    private Stage      b;
    public TidierFXML(FXMLLoader xLoader, Stage xStage) {
        a = xLoader;
        b = xStage;}
    public TidierFXML(String url, Modality mode) {
        a = new FXMLLoader(utils.class.getResource(url));
        b = new Stage();
        b.initModality(mode);
        try {
            b.setScene(new Scene(a.load()));
        } catch (IOException e) {
            e.printStackTrace(System.out);
            System.exit(699);
    }   }
    public TidierFXML(String url) {
        a = new FXMLLoader(utils.class.getResource(url));
        b = new Stage();
        try {
            b.setScene(new Scene(a.load()));
        } catch (IOException e) {
            e.printStackTrace(System.out);
            System.exit(698);
    }   }
    public FXMLLoader getLoader() { return a; }
    public Stage       getStage() { return b; }
    public <T>T   getController() { return a.getController(); }
}
