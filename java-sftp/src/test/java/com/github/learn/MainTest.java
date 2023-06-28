package com.github.learn;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class MainTest {

  @Test
  public void test() {
    log.info("Hello, Test");
  }

  @Test
  public void givenJSch_whenSftp_then() throws JSchException, SftpException, IOException {
    JSch jSch = new JSch();
    // jSch.addIdentity("/Volumes/RamDmisk/sftp/id_rsa_pkcs1");
    // String serverPublicKey = "192.168.56.8 ecdsa-sha2-nistp256 AAAAE2VjZHNhLXNoYTItbmlzdHAyNTYAAAAIbmlzdHAyNTYAAABBBD5fJe6Qm8SyF3XPOGaU6LrTomSVvpjrOy5TlrRdB7mKND3uQfHHbUibnUDCrPiuyof/BTACdZG6ph6lML4gzmU=";
    // jSch.setKnownHosts(new ByteArrayInputStream(serverPublicKey.getBytes(StandardCharsets.UTF_8)));
    final Session session = jSch.getSession("feng", "c008");
    session.setConfig("StrictHostKeyChecking", "no");
    session.connect(30000);
    final ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
    sftp.connect(30000);
    // 默认进入服务器上用户主目录
    log.info("lpwd: {}; pwd: {}", sftp.lpwd(), sftp.pwd());
    sftp.cd("aDir");
    String targetDir = "test";
    boolean existTargetDir = false;
    final Vector<ChannelSftp.LsEntry> ls = sftp.ls(".");
    for (ChannelSftp.LsEntry l : ls) {
      if (targetDir.equals(l.getFilename())) {
        existTargetDir = true;
        break;
      }
    }
    if (!existTargetDir) {
      sftp.mkdir(targetDir);
    }
    // jsch 暂不支持 sftp 传输目录能力
    sftp.put("/Volumes/RamDisk/security/some.txt", ".");
    // 重命名
    sftp.put("/Volumes/RamDisk/security/some.txt", "./a.txt");
    // get as stream
    final InputStream atxt = sftp.get("./a.txt");
    // get as file
    final File tempFile = File.createTempFile("sftp", "");
    sftp.get("./a.txt", tempFile.getParent() + "/a.txt");
    log.info("absolutePath: {}", tempFile.getParent());
    // 2: No such file Exception
    // final InputStream notexist = sftp.get("./notexist.txt");
    //
    sftp.disconnect();
    session.disconnect();
  }

}
