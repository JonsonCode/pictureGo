package com.mxdc.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialItem;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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
    public void userPassword(){
        UsernamePasswordCredentialsProvider provider = new UsernamePasswordCredentialsProvider("1040558191@qq.com", "wjy138998");
    }

    @Test
    public void filepath(){
        File file = new File("");
        System.out.println(this.getClass().getResource("/").getPath());
    }

    @Test
    public void dialoag() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
//        GeneralUtils.messageDialog("haha");
        // 生成密钥对
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey)keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey)keyPair.getPrivate();
        System.out.println(rsaPublicKey.toString());
// 格式化私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        Cipher cipher = Cipher.getInstance("RSA");  // 确定算法
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);  // 确定加密密钥
        byte[] result = cipher.doFinal("原文".getBytes());  // 加密
        System.out.println(Base64.encodeBase64String(result));

// 格式化公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
        keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        cipher = Cipher.getInstance("RSA"); // 确定算法
        cipher.init(Cipher.DECRYPT_MODE, publicKey);  // 确定公钥
        System.out.println(new String(cipher.doFinal(result))); // 解密
    }

}