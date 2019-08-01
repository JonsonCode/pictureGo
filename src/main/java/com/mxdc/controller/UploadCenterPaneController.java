package com.mxdc.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class UploadCenterPaneController {
    @FXML
    private HBox linkFormatContainer;  //所有类型的链接格式的容器HBox
    @FXML
    private Label labelMarkdown;  //Markdown链接格式的Label
    @FXML
    private Label labelHTML;  //HTML链接格式的Label
    @FXML
    private Label labelURL;   //URL链接格式的Label
    @FXML
    private Label labelUBB;   //UBB链接格式的Label
    @FXML
    private Label labelCustom; //Custom链接格式的Label
    //Markdown链接格式的Label事件处理
    @FXML
    public void onClickedMarkdown(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            this.resetLinkFormatColor();  //调用重设链接格式的背景颜色函数
            labelMarkdown.setBackground(new Background(new BackgroundFill(Color.rgb(64,158,255),new CornerRadii(15,0,0,15,false) ,null)));  //设置选中标记的背景颜色和左圆角背景
        }
    }
    @FXML
    public void onClickedHTML(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            this.resetLinkFormatColor();   //调用重设链接格式的背景颜色函数
            labelHTML.setBackground(new Background(new BackgroundFill(Color.rgb(64,158,255),null,null)));  //设置选中标记的背景颜色
        }
    }
    @FXML
    public void onClickedURL(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            this.resetLinkFormatColor();    //调用重设链接格式的背景颜色函数
            labelURL.setBackground(new Background(new BackgroundFill(Color.rgb(64,158,255),null,null)));  //设置选中标记的背景颜色
        }
    }
    @FXML
    public void onClickedUBB(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            this.resetLinkFormatColor();    //调用重设链接格式的背景颜色函数
            labelUBB.setBackground(new Background(new BackgroundFill(Color.rgb(64,158,255),null,null)));  //设置选中标记的背景颜色
        }
    }
    @FXML
    public void onClickedCustom(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            this.resetLinkFormatColor();    //调用重设链接格式的背景颜色函数
            labelCustom.setBackground(new Background(new BackgroundFill(Color.rgb(64,158,255), new CornerRadii(0,15,15,0,false), null)));  //设置选中标记的背景颜色和右圆角背景
        }
    }

    //重设链接格式的背景颜色函数
    private void resetLinkFormatColor(){
        ObservableList<Node> labels = linkFormatContainer.getChildren();
        for (int i=0;i<labels.size();i++){
            if (i==0){  //第一个”Markdown“格式的label需要另外设置左圆角边框
                ((Label)labels.get(i)).setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(15,0,0,15,false), null)));
            }
            else if (i==labels.size()-1){  //最后一个”Custom“格式的label需要另外设置右圆角边框
                ((Label)labels.get(i)).setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0,15,15,0,false), null)));
            }
            else {
                ((Label)labels.get(i)).setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
            }
        }
    }
}
