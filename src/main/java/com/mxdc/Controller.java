package com.mxdc;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *  @author  mxdc
 *  @date    2019/7/19 17:46
 */
public class Controller implements Initializable {
    @FXML
    private Button myButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void buttonClick(ActionEvent event){
        System.out.println("sdf");
    }


    public void button(javafx.event.ActionEvent actionEvent) {
        System.out.println("sdff");
    }
}
