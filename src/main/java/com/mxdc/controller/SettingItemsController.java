package com.mxdc.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;

public class SettingItemsController {

    private BorderPane root;  //主窗体BorderPane
    private MainController mainController; //主界面的控制器mainController
    @FXML
    private Label labelGithub;
    @FXML
    private Label labelSevenCattle;
    @FXML
    private VBox vBoxContainer;
    /**设置最底下显示BorderPane的引用*/
    public void setRoot(BorderPane root) {
        this.root = root;  //设置引用
    }
    /**设置MainController的引用*/
    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    /**
     * 设置github
     */
    @FXML
    public void onClickedSettingGithub(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            this.resetTagColor();
            labelGithub.setTextFill(Color.rgb(64,158,255));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/setting_github.fxml"));
            BorderPane githubSetting = loader.load();
            SettingGithubController controller = loader.getController();
            controller.init();
            root.setCenter(githubSetting);
            controller.setStageStackPane((StackPane) root.getScene().getRoot());
        }
    }

    @FXML
    public void onClickedSevenCattle(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            this.resetTagColor();
            labelSevenCattle.setTextFill(Color.rgb(64,158,255));
            root.setCenter(new Label("七牛图床"));
        }
    }

    /**重设标签颜色为默认白色颜色的函数
     * */
    public void resetTagColor(){
        mainController.resetLeftTagStatus();
        HBox hBoxPictureBedSettings = mainController.getHBoxPictureBedSettings();
        ((Label)hBoxPictureBedSettings.getChildren().get(0)).setGraphic(new ImageView(new Image("/image/picturebedsettings_focused.png",30, 25, false, false, false)));
        ((Label)hBoxPictureBedSettings.getChildren().get(1)).setTextFill(Color.rgb(64,158,255));
        ((Label)hBoxPictureBedSettings.getChildren().get(1)).getStyleClass().add("text");
        ObservableList<Node> hboxs = vBoxContainer.getChildren();
        for (Node hBox:hboxs){
            ((Label)((HBox)hBox).getChildren().get(0)).setTextFill(Color.rgb(255,255,255));
        }
    }
}
