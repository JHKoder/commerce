package github.jhkoder.commerce.image.adapter;

import com.jcraft.jsch.*;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.image.exception.ImageException;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JschOutBoundAdapter implements OutboundAdapter {
    private final Session session;
    private final ChannelSftp channelSftp;
    private final String host;
    private final String username;
    private final String path;

    public JschOutBoundAdapter(@Value("${cloud.image-repository.host}") String host, @Value("${cloud.image-repository.username}") String username, @Value("${cloud.image-repository.ppk-path}") String path) {
        this.session = jschSession();
        this.channelSftp = jschChannelSftpBean();
        this.host = host;
        this.username = username;
        this.path = path;
    }

    public ChannelSftp jschChannelSftpBean() {
        try {
            Channel channel = jschSession().openChannel("sftp");
            channel.connect();
            return (ChannelSftp) channel;
        } catch (JSchException e) {
            throw new ImageException(ErrorCode.IMAGE_FILTER_NAME);
        }
    }

    private Session jschSession() {
        try {
            JSch jsch = new JSch();
            jsch.addIdentity(path);
            Session session = jsch.getSession(username, host, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            return session;
        } catch (JSchException e) {
            throw new ImageException(ErrorCode.IMAGE_FILTER_NAME);
        }
    }


    @PreDestroy
    public void close() {
        channelSftp.quit();
        session.disconnect();
    }
}
