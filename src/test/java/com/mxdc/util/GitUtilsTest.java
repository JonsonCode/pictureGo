package com.mxdc.util;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.RemoteConfig;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class GitUtilsTest {

    @Test
    public void init() throws IOException {
        Repository init = GitUtils.init("G:\\images_bed\\");
//        String branch = init.getBranch();
//        System.out.println(branch);
        Git git = new Git(init);
        try {
            List<RemoteConfig> call = git.remoteList().call();

            call.forEach(c->System.out.println(c.getURIs()));
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(git.status().call().getUntracked().toString());
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findGitRepositoryPaty() {
        try {
            File gitRepositoryPaty = GitUtils.findGitRepositoryPaty("G:\\images_bed\\.git\\fdg");
            System.out.println(gitRepositoryPaty.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void commitAll() {
        Repository init = GitUtils.init("G:\\images_bed\\");
        GitUtils.commitAll(init);
    }

    @Test
    public void getRemoteUrl() {
        Repository init = GitUtils.init("G:\\images_bed\\");
        String remoteUrl = GitUtils.getRemoteUrl(init);
        System.out.println(remoteUrl);
    }

    @Test
    public void gitPush() {
        Repository init = GitUtils.init("G:\\images_bed\\");
        try {
            GitUtils.gitPush(init,"1040558191@qq.com","wjy138998");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}