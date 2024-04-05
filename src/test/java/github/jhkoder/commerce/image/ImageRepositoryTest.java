package github.jhkoder.commerce.image;

import com.jcraft.jsch.*;
import github.jhkoder.commerce.image.repository.request.ImageRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Vector;

//@SpringBootTest
public class ImageRepositoryTest {
    private Session session = null;
    private ChannelSftp channelSftp = null;

    public void testImageUpload() throws IOException {
        // 테스트용 이미지 파일을 바이트 배열로 읽어옵니다.
        byte[] fileBytes = loadTestImageBytes();

        // MultipartFile 객체를 생성합니다.
        MultipartFile multipartFile = new MockMultipartFile("testImsage.jpg", "testImage.jpg",
                "image/jpeg", fileBytes);

        ImageRequest request = new ImageRequest();
        request.setMultipartFile(multipartFile);

        String host ="";
        String name="";
        String path = "";
        int port = 22;

        JSch jsch = new JSch();

        try {
            jsch.addIdentity(path);
            session = jsch.getSession(name,host,port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            channelSftp = (ChannelSftp) channel;
            System.out.println("--"+channelSftp.isClosed());


            System.out.println(exists("/home"));
            System.out.println(upload("/home/image/",multipartFile.getInputStream(),"te/444.jpg"));
            System.out.println("last");
        }catch (Exception e){
            System.out.println(e);
        }
        channelSftp.quit();
        session.disconnect();
    }

    public boolean upload(String dir,InputStream is,String name ) {
        try {
            channelSftp.cd(dir);
            channelSftp.put(is, name);

            if (this.exists(dir +"/"+name)) {
                return true;
            }
        } catch (SftpException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean exists(String path) {
        Vector res = null;
        try {
            res = channelSftp.ls(path);

        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                return false;
            }
        }
        return res != null && !res.isEmpty();
    }

    private byte[] loadTestImageBytes() throws IOException {
        return "test image bytes".getBytes(StandardCharsets.UTF_8);
    }
}
