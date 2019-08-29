package com.mxdc.controller;

import com.alibaba.fastjson.JSONObject;
import com.mxdc.util.Constants;
import com.mxdc.util.GeneralUtils;
import com.mxdc.util.GithubSetting;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainController implements Initializable {
    @FXML
    /** 主窗体BorderPane */
    private BorderPane root;
    @FXML
    /** 标题栏的最小化Label按钮 */
    private Label labelMinimize;
    @FXML
    /** 标题栏的最大化Label按钮 */
    private Label labelMaximize;
    @FXML
    /** 标题栏的关闭/退出Label按钮 */
    private Label labelExit;
    @FXML
    /** 包裹标题文字和最小化、最大化、关闭/退出按钮的BorderPane */
    private BorderPane titleBar;
    @FXML
    /** 包裹“上传区、相册、图床设置”三个标签的VBox */
    private VBox vBox;
    @FXML
    /** 上传区HBox标签 */
    private HBox hBoxUpload;
    @FXML
    /** 相册HBox标签 */
    private HBox hBoxPhotograph;

    public HBox getHBoxPictureBedSettings() {
        return hBoxPictureBedSettings;
    }

    @FXML
    /** 图床设置HBox标签 */
    private HBox hBoxPictureBedSettings;
    /** 用作“图床设置”是否展开的状态标记 */
    private boolean flag = false;
    /** “图床设置”展开的设置选项容器VBox */
    private VBox settingItemsPane;
    /**图床设置展开的设置选项控制器settingItemsController*/
    private SettingItemsController settingItemsController;

    /** 记录当前的标签是“上传区”、“相册”等 */
    private String currentTagName;

    /** 保存对应的center面板对象 */
    private Map<String,Object> centerPaneMap;
    /** 上传面板控制器 */
    private UploadCenterPaneController upload = new UploadCenterPaneController();

//    private short hotKeyFlag = 0x00;
//    private static final short MASK_CTRL = 1 << 0;
//    private static final short MASK_ALT = 1 << 1;
//    private static final short MASK_U = 1 << 2;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelMinimize.setCursor(Cursor.DEFAULT);
        labelMaximize.setCursor(Cursor.DEFAULT);
        labelExit.setCursor(Cursor.DEFAULT);
        titleBar.setCursor(Cursor.DEFAULT);
        //首次运行的面板是上传区的面板
        currentTagName = "上传区";
        centerPaneMap = new HashMap<>();
        this.saveCenterPane();
        //初始化“图床设置”的选项
        try {
            //加载“图床设置”选项，并设置引用主窗体root
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/settingitems.fxml"));
            settingItemsPane = loader.load();
            settingItemsController =  loader.getController();
            settingItemsController.setRoot(root);
            settingItemsController.setMainController(this);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // 加载配置属性
        loadSetting();

    }



    //最小化Label按钮事件处理
    @FXML
    public void onClickedMinimize(MouseEvent mouseEvent) {  //最小化按钮鼠标单击事件
        //如果按下鼠标左键，最小化primaryStage
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            Stage primaryStage = (Stage) labelMinimize.getParent().getScene().getWindow();  //窗体primaryStage对象
            primaryStage.setIconified(true);
        }
    }

    @FXML
    public void onEnteredMinimize(MouseEvent mouseEvent) {  //最小化按钮鼠标进入事件
        if (labelMinimize.getCursor() == Cursor.DEFAULT) {
            labelMinimize.setGraphic(new ImageView(new Image("/image/Minimize.png", 46, 32, false, false, false)));
        }
    }

    @FXML
    public void onExitedMinimize(MouseEvent mouseEvent) {  //最小化按钮鼠标推退出事件
        if (labelMinimize.getCursor() == Cursor.DEFAULT) {
            labelMinimize.setGraphic(new ImageView(new Image("/image/MinimizeDefault.png", 46, 32, false, false, false)));
        }
    }

    //最大化Label按钮事件处理
    @FXML
    public void onClickedMaximize(MouseEvent mouseEvent) {
        //如果按下鼠标左键，最大化/最小化primaryStage
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            //窗体primaryStage对象
            Stage primaryStage = (Stage) labelMaximize.getParent().getScene().getWindow();
            //如果primaryStage是最小化，设置成最大化
            if (!primaryStage.isMaximized()) {
                primaryStage.setMaximized(true);
                labelMaximize.setGraphic(new ImageView(new Image("/image/MaximizedDefault.png", 46, 32, false, false, false)));
                //设置primaryStage高度、宽度为屏幕的可视化高度、宽度（不包括Windows底下的任务栏）
                primaryStage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
                primaryStage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
            } else {  //如果primaryStage不是最小化，设置成最小化
                primaryStage.setMaximized(false);
                labelMaximize.setGraphic(new ImageView(new Image("/image/MaximizeDefault.png", 46, 32, false, false, false)));
            }
        }
    }

    @FXML
    public void onEnteredMaximize(MouseEvent mouseEvent) {  //最大化按钮鼠标进入事件
        if (labelMaximize.getCursor() == Cursor.DEFAULT) {
            //窗体primaryStage对象
            Stage primaryStage = (Stage) labelMaximize.getParent().getScene().getWindow();
            if (!primaryStage.isMaximized()) {
                labelMaximize.setGraphic(new ImageView(new Image("/image/Maximize.png", 46, 32, false, false, false)));
            } else {
                labelMaximize.setGraphic(new ImageView(new Image("/image/Maximized.png", 46, 32, false, false, false)));
            }
        }
    }

    @FXML
    public void onExitedMaximize(MouseEvent mouseEvent) {  //最大化按钮鼠标推退出事件
        if (labelMaximize.getCursor() == Cursor.DEFAULT) {
            Stage primaryStage = (Stage) labelMaximize.getParent().getScene().getWindow();  //窗体primaryStage对象
            if (!primaryStage.isMaximized()) {
                labelMaximize.setGraphic(new ImageView(new Image("/image/MaximizeDefault.png", 46, 32, false, false, false)));
            } else {
                labelMaximize.setGraphic(new ImageView(new Image("/image/MaximizedDefault.png", 46, 32, false, false, false)));
            }
        }
    }

    //关闭/退出按钮事件处理
    @FXML
    public void onClickedExit(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {  //如果按下鼠标左键，关闭primaryStage
            Stage primaryStage = (Stage) labelExit.getParent().getScene().getWindow();  //窗体primaryStage对象
            primaryStage.close();
        }
    }

    @FXML
    public void onEnteredExit(MouseEvent mouseEvent) {  //关闭/退出按钮鼠标进入事件
        if (labelExit.getCursor() == Cursor.DEFAULT) {
            labelExit.setGraphic(new ImageView(new Image("/image/Exit.png", 46, 32, false, false, false)));
        }
    }

    @FXML
    public void onExitedExit(MouseEvent mouseEvent) {  //关闭/退出按钮鼠标推退出事件
        if (labelExit.getCursor() == Cursor.DEFAULT) {
            labelExit.setGraphic(new ImageView(new Image("/image/ExitDefault.png", 46, 32, false, false, false)));
        }
    }

    //包裹标题文字和最小化、最大化、关闭/退出按钮的BorderPane拖拽事件
    private double titleBarMousePressedX;  //记录titleBar鼠标按下时的X坐标（即SceneX或X）
    private double titleBarMousePressedY;  //记录titleBar鼠标按下时的Y坐标（即SceneY或Y）

    @FXML
    public void onTitleBarPressed(MouseEvent mouseEvent) {  //BorderPane鼠标按下事件
        if (titleBar.getCursor() == Cursor.DEFAULT) {
            //如果按下的位置不是最小化、最大化、关闭/退出按钮的范围，记录按下的X、Y坐标
            if (mouseEvent.getSceneX() < titleBar.getWidth() - (labelMinimize.getWidth() + labelMaximize.getWidth() + labelExit.getWidth())) {
                titleBarMousePressedX = mouseEvent.getX();
                titleBarMousePressedY = mouseEvent.getY();
            }
        }
    }

    @FXML
    public void onTitleBarDragged(MouseEvent mouseEvent) {  //BorderPane鼠标拖拽事件
        if (titleBar.getCursor() == Cursor.DEFAULT) {
            if (!labelMinimize.isPressed() && !labelMaximize.isPressed() && !labelExit.isPressed()) {
                Stage primaryStage = (Stage) titleBar.getParent().getScene().getWindow();
                //如果鼠标的屏幕位置ScreenX、Y在屏幕的可视化区域内，才执行移动窗体操作
                if (0 <= mouseEvent.getScreenX() && mouseEvent.getScreenX() <= Screen.getPrimary().getVisualBounds().getWidth()
                        && 0 <= mouseEvent.getScreenY() && mouseEvent.getScreenY() <= Screen.getPrimary().getVisualBounds().getHeight()) {
                    if (primaryStage.isMaximized()) {  //如果是最大化状态下拖拽，变为未最大化的状态
                        //记录计算按下鼠标时的百分比(Y坐标不需要计算，因为Y坐标本身没有变化)
                        double validTitleBarWidth = primaryStage.getWidth() - labelMinimize.getWidth() - labelMaximize.getWidth() - labelExit.getWidth();
                        double percentageX = titleBarMousePressedX / validTitleBarWidth;
                        //设置成未最大化的状态
                        primaryStage.setMaximized(false);
                        labelMaximize.setGraphic(new ImageView(new Image("/image/MaximizeDefault.png", 46, 32, false, false, false)));
                        //重新计算未最大化的状态的鼠标按下坐标
                        validTitleBarWidth = primaryStage.getWidth() - labelMinimize.getWidth() - labelMaximize.getWidth() - labelExit.getWidth();
                        titleBarMousePressedX = validTitleBarWidth * percentageX;
                        //更新主舞台的坐标
                        primaryStage.setX(mouseEvent.getScreenX() - titleBarMousePressedX);
                        primaryStage.setY(mouseEvent.getScreenY() - titleBarMousePressedY);
                    } else {  //否则为最大化状态，直接更新主舞台的坐标
                        primaryStage.setX(mouseEvent.getScreenX() - this.titleBarMousePressedX);
                        primaryStage.setY(mouseEvent.getScreenY() - this.titleBarMousePressedY);
                    }
                }
            }
        }
    }

    @FXML
    public void onTitleBarClicked(MouseEvent mouseEvent) {
        if (titleBar.getCursor() == Cursor.DEFAULT) {
            //如果鼠标的位置不是最小化、最大化、关闭/退出按钮的范围
            if (mouseEvent.getSceneX() < titleBar.getWidth() - (labelMinimize.getWidth() + labelMaximize.getWidth() + labelExit.getWidth())) {
                if (mouseEvent.getClickCount() == 2) {
                    this.onClickedMaximize(mouseEvent);
                }
            }
        }
    }


    //上传区tag事件
    @FXML
    public void onUploadClicked(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            settingItemsController.resetTagColor(); //重设图床设置项的文字颜色
            this.resetLeftTagStatus();  //调用重设左边tag的图片和文字颜色的函数
            ((Label)hBoxUpload.getChildren().get(0)).setGraphic(new ImageView(new Image("/image/cloud_focused.png",30, 25, false, false, false)));
            ((Label)hBoxUpload.getChildren().get(1)).setTextFill(Color.rgb(64,158,255));
            ((Label)hBoxUpload.getChildren().get(1)).getStyleClass().add("text");
            this.saveCenterPane();  //保存之前的中间面板对象
            currentTagName = "上传区";
            BorderPane centerPane = (BorderPane) centerPaneMap.get(currentTagName);
            root.setCenter(centerPane);
        }
    }
    //相册tag事件
    @FXML
    public void onPhotographClicked(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            settingItemsController.resetTagColor(); //重设图床设置项的文字颜色
            this.resetLeftTagStatus();  //调用重设左边tag的图片和文字颜色的函数
            ((Label)hBoxPhotograph.getChildren().get(0)).setGraphic(new ImageView(new Image("/image/photograph_focused.png",30, 25, false, false, false)));
            ((Label)hBoxPhotograph.getChildren().get(1)).setTextFill(Color.rgb(64,158,255));
            ((Label)hBoxPhotograph.getChildren().get(1)).getStyleClass().add("text");
            this.saveCenterPane();  //保存之前的中间面板对象
            currentTagName = "相册";
            if (centerPaneMap.get(currentTagName)!=null){
                Label label = (Label)centerPaneMap.get(currentTagName);
                root.setCenter(label);
            }
            else {
                root.setCenter(new Label("新的Label"));
            }

        }
    }
    //图床设置tag事件
    @FXML
    public void onPictureBedSettingsClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            this.resetLeftTagStatus();  //调用重设标签的图片和文字颜色函数
            ((Label)hBoxPictureBedSettings.getChildren().get(0)).setGraphic(new ImageView(new Image("/image/picturebedsettings_focused.png",30, 25, false, false, false)));
            ((Label)hBoxPictureBedSettings.getChildren().get(1)).setTextFill(Color.rgb(64,158,255));
            ((Label)hBoxPictureBedSettings.getChildren().get(1)).getStyleClass().add("text");
            if (mouseEvent.getClickCount() == 2){
                this.onFlagIconClicked(mouseEvent);
            }
        }
    }
    @FXML
    public void onFlagIconClicked(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
          /*  if (settingItemsPane == null){
                try {
                    //加载“图床设置”选项，并设置引用主窗体root
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/settingitems.fxml"));
                    settingItemsPane = loader.load();
                    settingItemsController =  loader.getController();
                    settingItemsController.setRoot(root);
                    settingItemsController.setMainController(this);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }*/

            if (flag) {
                flag = false;
                ((Label)hBoxPictureBedSettings.getChildren().get(2)).setGraphic(new ImageView(new Image("/image/fold.png", 10, 10, false, false, false)));
                vBox.getChildren().remove(settingItemsPane);
            } else {
                flag = true;
                ((Label)hBoxPictureBedSettings.getChildren().get(2)).setGraphic(new ImageView(new Image("/image/unfold.png", 10, 10, false, false, false)));
                vBox.getChildren().addAll(settingItemsPane);
            }
        }
    }
    //左边上传区HBox标签、相册HBox标签、图床设置HBox标签的图片和文字重设为默认值的函数
    public void resetLeftTagStatus(){
        ((Label)hBoxUpload.getChildren().get(0)).setGraphic(new ImageView(new Image("/image/cloud.png",30, 25, false, false, false)));
        ((Label)hBoxUpload.getChildren().get(1)).setTextFill(Color.rgb(255,255,255));
        ((Label)hBoxUpload.getChildren().get(1)).getStyleClass().remove("text");
        ((Label)hBoxPhotograph.getChildren().get(0)).setGraphic(new ImageView(new Image("/image/photograph.png",30, 25, false, false, false)));
        ((Label)hBoxPhotograph.getChildren().get(1)).setTextFill(Color.rgb(255,255,255));
        ((Label)hBoxPhotograph.getChildren().get(1)).getStyleClass().remove("text");
        ((Label)hBoxPictureBedSettings.getChildren().get(0)).setGraphic(new ImageView(new Image("/image/picturebedsettings.png",30, 25, false, false, false)));
        ((Label)hBoxPictureBedSettings.getChildren().get(1)).setTextFill(Color.rgb(255,255,255));
        ((Label)hBoxPictureBedSettings.getChildren().get(1)).getStyleClass().remove("text");
    }
    private void saveCenterPane(){
        if (currentTagName.equals("上传区")){
            if (centerPaneMap.get(currentTagName) == null){
                centerPaneMap.put(currentTagName,root.getCenter());
            }
        }
        else if (currentTagName.equals("相册")){
            if (centerPaneMap.get(currentTagName) == null){
                centerPaneMap.put(currentTagName,root.getCenter());
            }
        }
    }

    /**
     * 加载设置
     */
    private void loadSetting(){
        File file = new File("github.properties");
        if (file.exists()) {

            String properties  = null;
            try {
                properties = FileUtils.readFileToString(file, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            JSONObject json = JSONObject.parseObject(properties);
            GithubSetting setting = GithubSetting.getInstance();
            String gitPassword = json.getString("gitPassword");
            if (!StringUtils.isEmpty(gitPassword)) {
                try {
                    setting.saveGitPassword(GeneralUtils.decrypt( gitPassword, Constants.KEY));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            setting.saveGitUsername(json.getString("gitUsername"));
            setting.savePicPath(json.getString("picPath"));
            setting.saveProjectPath(json.getString("projectPath"));
            setting.saveGitRemoteReop(json.getString("gitRemoteReop"));

        }
    }
}