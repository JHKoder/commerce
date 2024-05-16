package github.jhkoder.commerce.image.adapter;

import org.apache.sshd.sftp.client.SftpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;


@Configuration
public class SftpOutboundAdapter implements OutboundAdapter {
    @Value("${cloud.image-repository.host}")
    private String host;

    @Value("${cloud.image-repository.username}")
    private String username;

    @Value("${cloud.image-repository.ppk-path}")
    private String path;

    @Bean
    public SessionFactory<SftpClient.DirEntry> sftpSessionFactory() {
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();
        factory.setUser(username);
        factory.setHost(host);
        factory.setPort(22);
        factory.setPrivateKey(getPrivateKey());

        return factory;
    }

    @Bean
    public SftpRemoteFileTemplate sftpRemoteFileTemplate() {
        return new SftpRemoteFileTemplate(sftpSessionFactory());
    }

    private Resource getPrivateKey() {
        return  new FileSystemResource(path);
    }
}
