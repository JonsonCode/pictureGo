package com.mxdc.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class SettingItemsController {

    private BorderPane root;  //主窗体BorderPane

    public void setRoot(BorderPane root) {
        this.root = root;  //设置引用
    }


    /**
     * 设置github
     */
    @FXML
    public void onSettingGithub() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/setting_github.fxml"));
        BorderPane githubSetting = loader.load();
        SettingGithubController controller = loader.getController();
        controller.init();
        root.setCenter(githubSetting);
        controller.setStageStackPane((StackPane) root.getScene().getRoot());
    }
}
