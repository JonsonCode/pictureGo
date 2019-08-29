package com.mxdc;

import com.mxdc.util.GeneralUtils;
import com.mxdc.util.ResizeUtils;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static com.sun.jna.platform.win32.WinUser.GWL_STYLE;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        primaryStage.setTitle("Picture Bed"); // 设置标题
        primaryStage.getIcons().add(new Image("/image/ApplicationIcon.png")); //设置图标
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED); //去掉默认的标题栏
        ResizeUtils.addResizable(primaryStage,777,490);  //为primaryStage添加自由缩放
        primaryStage.show();

        // 获取屏幕可视化的宽高（Except TaskBar），把窗体设置在可视化的区域中间
        primaryStage.setX((Screen.getPrimary().getVisualBounds().getWidth() - primaryStage.getWidth()) / 2.0);
        primaryStage.setY((Screen.getPrimary().getVisualBounds().getHeight() - primaryStage.getHeight()) / 2.0);

        primaryStage.iconifiedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                //确保窗体在最大化状态下最小化后，单击任务栏图标显示时占据的屏幕大小是可视化的全屏
                if (primaryStage.isMaximized()){
                    primaryStage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
                    primaryStage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
                }
                //修复窗体在非最大化状态下单击最小化按钮最小化窗体后再恢复窗体时最小化按钮图片没有更新的问题
                else {
                    ObservableList<Node> labelList = ((HBox)(((BorderPane)(((BorderPane)(((StackPane)primaryStage.getScene().getRoot()).getChildren().get(0))).getTop())).getRight())).getChildren();
                    ((Label)labelList.get(0)).setGraphic(new ImageView(new Image("/image/MinimizeDefault.png",46,32,false,false,false)));
                }
            }
        });
        primaryStage.maximizedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                //最大化去掉主窗体的边框样式，去掉class名实现
                BorderPane root = (BorderPane) ((StackPane) primaryStage.getScene().getRoot()).getChildren().get(0);
                if (newValue){
                    root.getStyleClass().remove("bordercolor");
                }
                else {
                    root.getStyleClass().add("bordercolor");
                }
            }
        });



        /**
         * 下面这段代码是使任务栏图标响应单击事件，当stage的initStyle设置成UNDECORATED时，任务栏图标单击无法最小化窗体
         * 参见StackOverflow的提问：https://stackoverflow.com/questions/26972683/javafx-minimizing-undecorated-stage
         * **/
        if (System.getProperties().getProperty("os.name").contains("Windows")){  //判断当前os是否为Windows，如果是才执行
            long lhwnd = com.sun.glass.ui.Window.getWindows().get(0).getNativeWindow();
            Pointer lpVoid = new Pointer(lhwnd);
            WinDef.HWND hwnd = new WinDef.HWND(lpVoid);
            final User32 user32 = User32.INSTANCE;
            int oldStyle = user32.GetWindowLong(hwnd, GWL_STYLE);
//          System.out.println(Integer.toBinaryString(oldStyle));
            int newStyle = oldStyle | 0x00020000;//WS_MINIMIZEBOX
//          System.out.println(Integer.toBinaryString(newStyle));
            user32.SetWindowLong(hwnd, GWL_STYLE, newStyle);
//		    user32.SetWindowLong(hwnd,0x00020000,0x00080000);
        }


        /**
         * **********************************************
         * 添加快捷键
         * **********************************************
         */
        GeneralUtils.setRoot((StackPane) primaryStage.getScene().getRoot());
        GeneralUtils.addShortcutKey();
    }

    public static void main(String[] args) {
        Application.launch(Main.class,args);
    }
}
