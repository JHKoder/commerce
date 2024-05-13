package github.jhkoder.commerce.image.repository;

import github.jhkoder.commerce.image.repository.request.ImagePathRequest;
import github.jhkoder.commerce.image.repository.request.ImageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.integration.sftp.session.SftpSession;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


@Deprecated
@Configuration
@RequiredArgsConstructor
public class ImageSftpRepository implements ImageRepository {


    private final SftpRemoteFileTemplate sftpRemoteFileTemplate;

    @Override
    @Deprecated
    public boolean upload(ImageRequest imageRequest, ImagePathRequest pathRequest) throws IOException {
        System.out.println("error");
        try (InputStream inputStream = imageRequest.getFile().getInputStream()) {
            try (SftpSession sftpSession = (SftpSession) sftpRemoteFileTemplate.getSession()) {
                sftpSession.write(inputStream, pathRequest.getFileName());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean upload(ByteArrayInputStream is, ImagePathRequest pathRequest) {
        return false;
    }

    @Override
    public boolean delete(String fileName) {
        return false;
    }

    @Override
    public byte[] read(String path) {
        return new byte[0];
    }
}
