package com.mxdc.util;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.util.List;

/**
 * @author MXDC
 * @date 2019/7/19
 **/
public class GitUtils {

    private static final Logger logger = LoggerFactory.getLogger(GitUtils.class);
    /**
     * 加载git项目
     * @param gitPath 本地项目路径
     * @return Repository
     */
    public static Repository init(String gitPath){
        try {
            File gitRepository = findGitRepositoryPaty(gitPath);
            return new FileRepositoryBuilder().setGitDir(gitRepository).build();
        } catch (Exception e) {
            logger.info("初始化失败: "+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本地git文件夹
     * @param projectPath 项目路径
     * @return 返回.git文件
     */
    public static File findGitRepositoryPaty(String projectPath) throws Exception {
        File file = new File(projectPath);
        if (!file.isDirectory()){
            throw new Exception("git项目必须为文件夹");
        }
        int len = projectPath.length();
        // 如果选择的是.git文件夹
        if (Constants.GIT_PATH.equals(projectPath.substring(len - 4, len))){
            return file;
        } else { // 如果选择的是git项目文件夹
            projectPath += File.separator + Constants.GIT_PATH;
            file = new File(projectPath);
            if (file.exists() && file.isDirectory()){
                return file;
            }
        }
        throw new Exception("该目录不存在git项目");
    }


    /**
     * 提交到本地仓库
     * @param repository git仓库
     */
    public static void commitAll(Repository repository ){
        Git git = new Git(repository);
        try {
            git.add().addFilepattern(".").call();
            String addMsg = git.status().call().getAdded().toString();
            logger.info("add : "+ addMsg);
            logger.info("changed : " + git.status().call().getChanged().toString());
            logger.info("removed :" + git.status().call().getRemoved().toString());
            logger.info("modified :" + git.status().call().getModified().toString());
            git.commit().setMessage(addMsg).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }


    /**
     * 推送到远程仓库
     * @param repository g本地it仓库
     * @throws Exception push异常
     */
    public static void gitPush(Repository repository) throws Exception {
        String remoteUrl = getRemoteUrl(repository);
        if (remoteUrl == null || "".equals(remoteUrl)){
            throw new Exception("github远程仓库不存在");
        }
        Git git = new Git(repository);
        Iterable<PushResult> pushResults = git.push().setRemote(remoteUrl).call();
        PushResult pushResult = pushResults.iterator().next();
        validPushResult(pushResult);
    }

    /**
     * 获取远程仓库地址
     * @param repository 本地git仓库
     * @return Sring 获取远程仓库地址
     */
    public static String getRemoteUrl(Repository repository){
        Git git = new Git(repository);
        try {
            List<RemoteConfig> remoteConfigList = git.remoteList().call();
            if (null != remoteConfigList && remoteConfigList.size() > 0){
                RemoteConfig remoteConfig;
                remoteConfig = remoteConfigList.stream().filter(url -> url.getURIs().toString().contains("github.com")).findFirst().get();
                String remoteUrl = remoteConfig.getURIs().toString();
                return remoteUrl.substring(1,remoteUrl.length()-1);
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * git push
     * @param repository 本地git仓库
     * @param username 用户名
     * @param password 密码
     */
    public static void gitPush(Repository repository, String username, String password) throws Exception {
        Git git = new Git(repository);
        String remoteUrl = getRemoteUrl(repository);
        if (remoteUrl == null || "".equals(remoteUrl)){
            throw new Exception("github远程仓库不存在");
        }
        try {
            CredentialsProvider cp = new UsernamePasswordCredentialsProvider(username, password);
            Iterable<PushResult> results = git.push().setRemote(remoteUrl).setCredentialsProvider(cp).call();
            PushResult result = results.iterator().next();
            validPushResult(result);
        } catch (TransportException e) {

            throw new  Exception("验证失败");
        } catch (GitAPIException e) {
            e.printStackTrace();
            throw new  Exception("git push 异常, message:" + e.getMessage());
        }
    }

    /**
     * 验证push结果
     * @param result push返回的结果
     */
    public static void validPushResult(PushResult result) throws Exception {
        String msg = "未知原因";
        if (null == result) {
            throw new  Exception(("push失败: " + msg));
        }
        RemoteRefUpdate.Status status = result.getRemoteUpdate(Constants.GIT_MASTER_HEAD).getStatus();
        switch (status) {
            case OK:
                return;
            case NOT_ATTEMPTED:
                msg = "Push process hasn't yet attempted to update this ref. This is the default status, prior to push process execution.";
                break;
            case UP_TO_DATE:
                msg = "Remote ref was up to date, there was no need to update anything.";
                break;
            case REJECTED_NONFASTFORWARD:
                msg = "Remote ref update was rejected, as it would cause non fast-forward  update.";
                break;
            case REJECTED_NODELETE:
                msg = "Remote ref update was rejected, because remote side doesn't support/allow deleting refs.";
                break;
            case REJECTED_REMOTE_CHANGED:
                msg = "Remote ref update was rejected, because old object id on remote repository wasn't the same as defined expected old object.";
                break;
            case REJECTED_OTHER_REASON:
                msg = "Remote ref update was rejected for other reason";
                break;
            case NON_EXISTING:
                msg = "Remote ref didn't exist. Can occur on delete request of a non existing ref.";
                break;
            case AWAITING_REPORT:
                msg = "Push process is awaiting update report from remote repository. This is a temporary state or state after critical error in push process.";
                break;
            default:
                msg = "未知原因";
                break;
        }
        throw new  Exception("push失败: " + msg);
    }


    /**
     * 根据文件名返回，图片在github的URL地址
     * @param fileName 文件名
     * @return String 生成的url链接
     */
    public static String createURL(String fileName){
        // 获取github配置
        GithubSetting githubSetting = GithubSetting.getInstance();
        // 本地项目路径
        String projectBase = StringUtils.substringBefore(githubSetting.getProjectPath(), Constants.GIT_PATH);
        // 本地图片路径
        String picPath = githubSetting.getPicPath();
        // 相对于本地项目路径的 本地图片路径
        String subPath = StringUtils.substringAfter(picPath, projectBase);
        String raw = StringUtils.replace(githubSetting.getGitRemoteReop(), Constants.GIT_PATH, "/master/");
        raw = StringUtils.replace(raw, Constants.GIT_DOMAIN, Constants.GIT_RAW_URL);
        return raw + subPath + "/" + fileName;
    }

}
