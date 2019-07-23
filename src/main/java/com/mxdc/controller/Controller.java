package com.mxdc.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    @FXML
    private Label labelMinimize;  //标题栏的最小化Label按钮
    @FXML
    private Label labelMaximize;  //标题栏的最大化Label按钮
    @FXML
    private Label labelExit;  //标题栏的关闭/退出Label按钮

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelMinimize.setCursor(Cursor.DEFAULT);
        labelMaximize.setCursor(Cursor.DEFAULT);
        labelExit.setCursor(Cursor.DEFAULT);
    }

    //最小化Label按钮事件处理
    @FXML
    public synchronized void  onClickedMinimize(MouseEvent mouseEvent){  //最小化按钮鼠标单击事件
        if (mouseEvent.getButton() == MouseButton.PRIMARY){ //如果按下鼠标左键，最小化primaryStage
            Stage primaryStage = (Stage) labelMinimize.getParent().getScene().getWindow();  //窗体primaryStage对象
            primaryStage.setIconified(true);
        }
    }
    @FXML
    public void onEnteredMinimize(MouseEvent mouseEvent){  //最小化按钮鼠标进入事件
        if (labelMinimize.getCursor() == Cursor.DEFAULT){
            labelMinimize.setGraphic(new ImageView(new Image("/image/Minimize.png",46,32,false,false,false)));
        }
    }
    @FXML
    public void onExitedMinimize(MouseEvent mouseEvent){  //最小化按钮鼠标推退出事件
        if (labelMinimize.getCursor() == Cursor.DEFAULT){
            labelMinimize.setGraphic(new ImageView(new Image("/image/MinimizeDefault.png",46,32,false,false,false)));
        }
    }
    @FXML
    public void onClickedMaximize(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){  //如果按下鼠标左键，最大化/最小化primaryStage
            Stage primaryStage = (Stage) labelMaximize.getParent().getScene().getWindow();  //窗体primaryStage对象
            if (!primaryStage.isMaximized()){  //如果primaryStage是最小化，设置成最大化
                primaryStage.setMaximized(true);
                //设置primaryStage高度、宽度为屏幕的可视化高度、宽度（不包括Windows底下的任务栏）
                primaryStage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
                primaryStage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
            }
            else {  //如果primaryStage不是最小化，设置成最小化
                primaryStage.setMaximized(false);
            }
        }
    }
    @FXML
    public void onClickedExit(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){  //如果按下鼠标左键，关闭primaryStage
            Stage primaryStage = (Stage) labelExit.getParent().getScene().getWindow();  //窗体primaryStage对象
            primaryStage.close();
        }
    }


}
