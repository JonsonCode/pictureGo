package com.mxdc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mxdc.util.Constants;
import com.mxdc.util.GeneralUtils;
import com.mxdc.util.GitUtils;
import com.mxdc.util.GithubSetting;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.event.FocusListener;
import java.io.*;
import java.util.Collection;

/**
 * 配置github相关信息
 * @author MXDC
 * @date 2019/8/23
 **/
public class SettingGithubController {

    /** github用户名 */
    @FXML
    private JFXTextField user;
    /** github用户密码 */
    @FXML
    private JFXPasswordField password;
    /** 本地项目路径 */
    @FXML
    private JFXTextField projectPath;
    /** 本地图片路径 */
    @FXML
    private JFXTextField imgPath;

    /** 主舞台底下的Stack(堆)容器，最底下是默认显示的borderpane容器，往上面加做操作的信息提示（通过添加label组件） */
    private StackPane stageStackPane;

    /**设置stageStackPane引用的函数*/
    public void setStageStackPane(StackPane stageStackPane){
        this.stageStackPane = stageStackPane;
    }

    public void init() {

    GithubSetting setting = GithubSetting.getInstance();
    this.user.setText(setting.getGitUsername());
    this.password.setText(setting.getGitPassword());
    this.imgPath.setText(setting.getPicPath());
    this.projectPath.setText(setting.getProjectPath());
    }

    /**
     * 选择git项目路径
     * @param mouseEvent
     */
    public void chooseProjectPath(MouseEvent mouseEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择项目目录");
        File file = directoryChooser.showDialog(new Stage());
        if (file == null || !file.isDirectory()) {
            return;
        }
        this.projectPath.setText(file.getAbsolutePath());
        String projectPath = this.projectPath.getText();
        if (StringUtils.isEmpty(projectPath)) {
            return;
        }
        try {
            File gitRepositoryPaty = GitUtils.findGitRepositoryPaty(projectPath);
            GithubSetting.getInstance().saveProjectPath(gitRepositoryPaty.getCanonicalPath());
            String remoteUrl = GitUtils.getRemoteUrl(GitUtils.init(gitRepositoryPaty.getAbsolutePath()));
            GithubSetting.getInstance().saveGitRemoteReop(remoteUrl);
        } catch (Exception e) {
         /*   Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText(e.getMessage());
            alert.showAndWait();*/
            GeneralUtils.toastInfo((StackPane) user.getScene().getRoot(),new Label(e.getMessage()));
            this.projectPath.setText("");
        }
    }

    /**
     * 选择图片上传文件夹
     * @param mouseEvent
     */
    public void chooseImgPath(MouseEvent mouseEvent) throws IOException {
        String projectPath = GithubSetting.getInstance().getProjectPath();

        if (StringUtils.isEmpty(projectPath)){
           /* Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("请先选择项目目录");
            alert.showAndWait();*/
            GeneralUtils.toastInfo((StackPane) user.getScene().getRoot(),new Label("请先选择项目目录"));
            return;
        }
        File projectFile = new File(projectPath);
        DirectoryChooser directoryChooser = new DirectoryChooser();
        String imgPathText = imgPath.getText();
        if (StringUtils.isNotEmpty(imgPathText)) {
            File file = new File(imgPathText);
            directoryChooser.setInitialDirectory(file);
        }
        directoryChooser.setTitle("选择图片保存目录");
        File file = directoryChooser.showDialog(new Stage());
        if (file == null || !file.isDirectory()) {
            return;
        }
        boolean isContains  = FileUtils.directoryContains(projectFile.getParentFile(), file);
        boolean isEquals = file.equals(projectFile.getParentFile());
        if (isContains || isEquals){
            imgPath.setText(file.getAbsolutePath());
            GithubSetting.getInstance().savePicPath(file.getAbsolutePath());
        }else {
           /* Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("图片路径不在git项目中");
            alert.showAndWait();*/
            GeneralUtils.toastInfo((StackPane) user.getScene().getRoot(),new Label("图片路径不在git项目中"));
        }
    }
    /**
     * 保存githu设置
     * @param mouseEvent
     */
    public void saveConfigure(MouseEvent mouseEvent) {
        String userName = this.user.getText();
        String password = this.password.getText();
        // 如果账号或密码为空弹窗警告
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            /*Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("账号或密码不为空");
            alert.showAndWait();*/
            GeneralUtils.toastInfo((StackPane) user.getScene().getRoot(),new Label("账号或密码不为空"));
            return;
        }
        String projectPath = GithubSetting.getInstance().getProjectPath();
        String picPath = GithubSetting.getInstance().getPicPath();
        if (StringUtils.isEmpty(projectPath) || StringUtils.isEmpty(picPath)){
           /* Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("项目路径和图片存储路径不为空");
            alert.showAndWait();*/
            GeneralUtils.toastInfo((StackPane) user.getScene().getRoot(),new Label("项目路径和图片存储路径不为空"));
            return;
        }
        GithubSetting.getInstance().saveGitUsername(userName);
        try {
            GithubSetting.getInstance().saveGitPassword(GeneralUtils.encrypt(password, Constants.KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        GithubSetting githubProperties = GithubSetting.getInstance();
        String properties = JSONObject.toJSONString(githubProperties);
        githubProperties.saveGitPassword(password);
        try {
            IOUtils.write(properties,new FileOutputStream("github.properties"),"UTF-8");
//            GeneralUtils.messageDialog("Scess Dialog","保存成功",Alert.AlertType.INFORMATION);
            GeneralUtils.toastInfo((StackPane) user.getScene().getRoot(),new Label("保存成功"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
