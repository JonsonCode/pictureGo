package com.mxdc.controller;

import com.mxdc.util.Constants;
import com.mxdc.util.GeneralUtils;
import com.mxdc.util.GitUtils;
import com.mxdc.util.GithubSetting;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.lib.Repository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class UploadCenterPaneController {
    /** 所有类型的链接格式的容器HBox */
    @FXML
    private HBox linkFormatContainer;
    /** Markdown链接格式的Label labelType = 1 */
    @FXML
    private Label labelMarkdown;
    /** HTML链接格式的Label  labelType = 2 */
    @FXML
    private Label labelHTML;
    /** URL链接格式的Label  labelType = 3 */
    @FXML
    private Label labelURL;
    /** UBB链接格式的Label labelTyep = 4 */
    @FXML
    private Label labelUBB;
    /** Custom链接格式的Label labelyType = 5 */
    @FXML
    private Label labelCustom;
    /** 初始化labelTyoe为 URL */
    private int labelType = 3;

    /**
     * M arkdown链接格式的Label事件处理
     * @param mouseEvent
     */
    @FXML
    public void onClickedMarkdown(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            // 调用重设链接格式的背景颜色函数
            this.resetLinkFormatColor();
            // 设置选中标记的背景颜色和左圆角背景
            labelMarkdown.setBackground(new Background(new BackgroundFill(Color.rgb(64,158,255),new CornerRadii(15,0,0,15,false) ,null)));
            this.labelType = 1;
        }
    }
    @FXML
    public void onClickedHTML(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            // 调用重设链接格式的背景颜色函数
            this.resetLinkFormatColor();
            // 设置选中标记的背景颜色
            labelHTML.setBackground(new Background(new BackgroundFill(Color.rgb(64,158,255),null,null)));
            this.labelType = 2;
        }
    }
    @FXML
    public void onClickedURL(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            // 调用重设链接格式的背景颜色函数
            this.resetLinkFormatColor();
            // 设置选中标记的背景颜色
            labelURL.setBackground(new Background(new BackgroundFill(Color.rgb(64,158,255),null,null)));
            this.labelType = 3;
        }
    }
    @FXML
    public void onClickedUBB(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            // 调用重设链接格式的背景颜色函数
            this.resetLinkFormatColor();
            // 设置选中标记的背景颜色
            labelUBB.setBackground(new Background(new BackgroundFill(Color.rgb(64,158,255),null,null)));
            this.labelType = 4;
        }
    }
    @FXML
    public void onClickedCustom(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            // 调用重设链接格式的背景颜色函数
            this.resetLinkFormatColor();
            // 设置选中标记的背景颜色和右圆角背景
            labelCustom.setBackground(new Background(new BackgroundFill(Color.rgb(64,158,255), new CornerRadii(0,15,15,0,false), null)));
            this.labelType = 5;
        }
    }

    //重设链接格式的背景颜色函数
    private void resetLinkFormatColor(){
        ObservableList<Node> labels = linkFormatContainer.getChildren();
        for (int i=0;i<labels.size();i++){
            if (i==0){
                // 第一个”Markdown“格式的label需要另外设置左圆角边框
                ((Label)labels.get(i)).setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(15,0,0,15,false), null)));
            }
            else if (i==labels.size()-1){
                // 最后一个”Custom“格式的label需要另外设置右圆角边框
                ((Label)labels.get(i)).setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0,15,15,0,false), null)));
            }
            else {
                ((Label)labels.get(i)).setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
            }
        }
    }

    /**
     * 拖动到目标范围上面拖动的时候，不停执行提示是链接文件模式
     * @param dragEvent
     */
    @FXML
    public void onTargetRegionDragOver(DragEvent dragEvent){
        if (dragEvent.getDragboard().hasFiles()){
            dragEvent.acceptTransferModes(TransferMode.LINK);
        }
    }

    /**
     * 拖动到目标范围上并松开鼠标的时候，执行这个DragDropped事件处理
     * @param dragEvent
     */
    @FXML
    public void onTargetRegionDragDropped(DragEvent dragEvent){
        GithubSetting githubSetting = GithubSetting.getInstance();
        if (StringUtils.isEmpty(githubSetting.getPicPath())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("请先配置图片路径");
            alert.showAndWait();
            return;
        }
        if (dragEvent.getDragboard().hasFiles()){
            System.out.println("You have dragged a file.Location is "+dragEvent.getDragboard().getFiles().get(0).toString());
            File file = dragEvent.getDragboard().getFiles().get(0);
            String extension = StringUtils.substringAfter(file.getName(), ".");
            try {
                FileUtils.copyFile(file,new File(githubSetting.getPicPath() + File.separator + System.currentTimeMillis() + "." +extension));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 点击上传"Label事件处理,打开系统文件选择对话框，选择图片文件
     * @param mouseEvent
     */
    @FXML
    public void onClickedChooseFile(MouseEvent mouseEvent){
        GithubSetting githubSetting = GithubSetting.getInstance();
        if (StringUtils.isEmpty(githubSetting.getPicPath())){
            GeneralUtils.messageDialog("Warning Dialog","请先配置图片路径",Alert.AlertType.WARNING);
            return;
        }
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JPG","*.jpg"),
                    new FileChooser.ExtensionFilter("PNG","*.png"),
                    new FileChooser.ExtensionFilter("All Images","*.*")
            );
            File openFile = chooser.showOpenDialog(linkFormatContainer.getScene().getWindow());
            if (openFile!=null){
                System.out.println("You have opened a file.Location is "+openFile.toString());
                String extension = StringUtils.substringAfter(openFile.getName(), ".");
                try {
                    // 以单前时间戳命名重文件
                    File desFile = new File(githubSetting.getPicPath() + File.separator + System.currentTimeMillis() + "." + extension);
                    // 将上传文件复制到本地项目
                    FileUtils.copyFile(openFile,desFile);
                    // 获取链接
                    String link = createLink(desFile.getName());
                    // 获取系统剪贴板
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    // 封装文本内容
                    Transferable trans = new StringSelection(link);
                    // 把文本内容设置到系统剪贴板
                    clipboard.setContents(trans, null);
                    GeneralUtils.messageDialog("","链接已复制到粘贴版",Alert.AlertType.INFORMATION);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 先提交，后push到远程仓库，最后生成链接
     * @param fileName 文件名
     * @return
     */
    public String createLink(String fileName){
        GithubSetting githubSetting = GithubSetting.getInstance();
        Repository init = GitUtils.init(githubSetting.getProjectPath());
        String username = githubSetting.getGitUsername();
        String password = githubSetting.getGitPassword();
        String urlLink = GitUtils.createURL(fileName);
        String imageDataPath = StringUtils.replace(githubSetting.getProjectPath(), Constants.GIT_PATH, "imageUrl.data");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            GeneralUtils.messageDialog("","请先设置用户和密码",Alert.AlertType.WARNING);
            return null;
        }
        try {
            FileUtils.writeStringToFile(new File(imageDataPath),urlLink,"UTF-8",true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GitUtils.commitAll(init);
        try {
            GitUtils.gitPush(init,username,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String markDownLink = new StringBuilder().append("![")
                .append(fileName).append("]")
                .append("(").append(urlLink).append(")").toString();
        String htmlLink = new StringBuilder("<img src='").append(urlLink).append("'>").toString();
        switch (this.labelType){
            case 1:return markDownLink;
            case 2:return htmlLink;
            case 3:return urlLink;
            case 4:break;
            case 5:break;
            default:
        }
        return null;
    }

    /**
     * 上传粘贴版图片
     * @param mouseEvent
     */
    public void onClickedClipboard(MouseEvent mouseEvent) {
        GithubSetting githubSetting = GithubSetting.getInstance();
        if (StringUtils.isEmpty(githubSetting.getPicPath())){
            GeneralUtils.messageDialog("Warning Dialog","请先配置图片路径",Alert.AlertType.WARNING);
            return;
        }
        //创建剪切板对象
        Clipboard clipboard=Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        if (contents != null){
            if (contents.isDataFlavorSupported(DataFlavor.imageFlavor)){
                try {
                    Image image = (Image) contents.getTransferData(DataFlavor.imageFlavor);
                    String picPath = githubSetting.getPicPath();
                    File desFile = new File(githubSetting.getPicPath() + File.separator + System.currentTimeMillis() + ".jpg");
                    //转成jpg
                    //BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
                    //转成png
                    BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_BGR);
                    Graphics2D g = bufferedImage.createGraphics();
                    g.drawImage(image, null, null);
                    //ImageIO.write((RenderedImage)bufferedImage, "jpg", file);
                    ImageIO.write((RenderedImage)bufferedImage, "jpg", desFile);
                    // 创建链接并复制到粘贴版
                    String link = createLink(desFile.getName());
                    if (StringUtils.isEmpty(link)) {
                        return;
                    }
                    Transferable trans = new StringSelection(link);
                    // 把文本内容设置到系统剪贴板
                    clipboard.setContents(trans, null);
                    GeneralUtils.messageDialog("","链接已复制到粘贴版",Alert.AlertType.INFORMATION);
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
