package com.mxdc;

import com.mxdc.util.ResizeUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        primaryStage.setTitle("Picture Bed"); // 设置标题
        primaryStage.getIcons().add(new Image("/image/ApplicationIcon.png")); //设置图标
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED); //去掉默认的标题栏
        ResizeUtils.addResizable(primaryStage,858,570);  //为primaryStage添加自由缩放
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(Main.class,args);
    }
}
