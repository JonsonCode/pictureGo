package com.mxdc;

import com.jfoenix.assets.JFoenixResources;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root =
                FXMLLoader.load(getClass().getResource("/main.fxml"));
        // 设置标题
        primaryStage.setTitle("Picture Bed");
        //设置图标
        primaryStage.getIcons().add(new Image("/images/tu.png"));
        Scene scene = new Scene(root, 650, 458);
        scene.getStylesheets().addAll(
                JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                JFoenixResources.load("css/jfoenix-design.css").toExternalForm()
        );
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
