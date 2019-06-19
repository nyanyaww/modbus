package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main2 extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			// 从 mySecen.fxml 加载布局文件
			Parent root = FXMLLoader.load(getClass().getResource("NetAssistView.fxml")); 
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			// 设置 title
			primaryStage.setTitle("NetAssist");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
