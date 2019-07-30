package com.mxdc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class SettingsItemController{

    private BorderPane root;  //主窗体BorderPane

    public void setRoot(BorderPane root) {
        this.root = root;  //设置引用
    }

    @FXML
    public void onTest(){
        root.setCenter(new Label("tstst"));
    }

}
