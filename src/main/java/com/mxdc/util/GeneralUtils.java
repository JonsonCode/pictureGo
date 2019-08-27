package com.mxdc.util;

import com.jfoenix.controls.JFXDialog;
import javafx.animation.FadeTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author MXDC
 * @date 2019/8/26
 **/
public class GeneralUtils {

    private static final String KEY_AES = "AES";
    /**
     * 消息弹窗
     * @param title 标题
     * @param msg 信息
     * @param alertType 弹窗类型
     */
    public static void messageDialog(String title, String msg, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**弹出信息提示动画的函数
     * @param stageStackPane 主舞台底下的Stack(堆)容器
     * @param fadingLabel 信息提示的label
     * */
    public static void toastInfo(StackPane stageStackPane, Label fadingLabel){
        fadingLabel.getStylesheets().add("css/FadingLabelStyle.css");
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3),fadingLabel);
        fadeTransition.setFromValue(1);  //不透明度从1变到0,from 1 to 0.
        fadeTransition.setToValue(0);
        stageStackPane.getChildren().add(fadingLabel);
        //动画完成后移除label组件
        fadeTransition.setOnFinished(fade->{
            stageStackPane.getChildren().remove(1);
        });
        //开始播放渐变动画提示
        fadeTransition.play();
    }

    public static String encrypt(String src, String key) throws Exception {
        if (key == null || key.length() != 16) {
            throw new Exception("key不滿足條件");
        }
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_AES);
        Cipher cipher = Cipher.getInstance(KEY_AES);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(src.getBytes());
        return byte2hex(encrypted);
    }

    public static String decrypt(String src, String key) throws Exception {
        if (key == null || key.length() != 16) {
            throw new Exception("key不滿足條件");
        }
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_AES);
        Cipher cipher = Cipher.getInstance(KEY_AES);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] encrypted1 = hex2byte(src);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original);
        return originalString;
    }

    public static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
                    16);
        }
        return b;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
}
