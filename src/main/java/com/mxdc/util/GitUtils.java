package com.mxdc.util;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.*;

import java.io.File;
import java.util.List;

/**
 * @author MXDC
 * @date 2019/7/19
 **/
public class GitUtils {

    /**
     * 加载git项目
     * @param gitPath 本地项目路径
     * @return Repository
     */
    public static Repository init(String gitPath){
        try {
            return new FileRepositoryBuilder().setGitDir(findGitRepositoryPaty(gitPath)).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本地git文件夹
     * @param projectPath
     * @return
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
     * @param repository
     */
    public static void commitAll(Repository repository ){
        Git git = new Git(repository);
        String msg = "add file : ";
        try {
//            PullResult call = git.pull().setRemote().call();
            git.add().addFilepattern(".").call();
            msg += git.status().call().getAdded().toString();
            msg += "\nchanged file : " + git.status().call().getChanged().toString();
            msg += "\nremoved file :" + git.status().call().getRemoved().toString();
            msg += "\nmodified file :" + git.status().call().getModified().toString().toString();
            System.out.println(msg);
            git.commit().setMessage(msg).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }


    /**
     * 推送到远程仓库
     * @param repository
     * @throws Exception
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
     * @param repository
     * @return
     */
    public static String getRemoteUrl(Repository repository){
        Git git = new Git(repository);
        try {
            List<RemoteConfig> remoteConfigList = git.remoteList().call();
            if (null != remoteConfigList && remoteConfigList.size() > 0){
                RemoteConfig remoteConfig = remoteConfigList.stream().filter(url -> url.getURIs().toString().contains("github.com")).findFirst().get();
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
     *
     * @param repository
     * @param username
     * @param password
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
     *
     * @param result
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

}
