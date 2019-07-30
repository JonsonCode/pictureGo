package com.mxdc.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsItemController implements Initializable {

    private BorderPane root;
    @FXML
    public void onTest(){
        root.setCenter(new Label("tstst"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
