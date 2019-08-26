package com.mxdc.util;

import java.io.Serializable;
import java.util.Properties;

/**
 * github配置文件
 * @author MXDC
 * @date 2019/8/23
 **/
public class GithubSetting implements Serializable {
    private static final GithubSetting INSTANCE = new GithubSetting();

    private String projectPath;

    private String picPath;

    private String gitUsername;

    private String gitPassword;

    private String gitRemoteReop;

    public static GithubSetting getInstance() {
        return INSTANCE;
    }



    public void saveProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public void savePicPath(String picPath) {
        this.picPath = picPath;
    }

    public void saveGitUsername(String username) {
        this.gitUsername = username;
    }

    public void saveGitPassword(String password) {
     this.gitPassword = password;
    }

    public void saveGitRemoteReop(String gitRemoteReop){
        this.gitRemoteReop = gitRemoteReop;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public String getPicPath() {
        return picPath;
    }

    public String getGitUsername() {
        return gitUsername;
    }

    public String getGitPassword()  {
        return gitPassword;
    }

    public String getGitRemoteReop(){
        return gitRemoteReop;
    }
}
