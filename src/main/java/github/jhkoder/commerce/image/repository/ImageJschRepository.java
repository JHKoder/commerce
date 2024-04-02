package github.jhkoder.commerce.image.repository;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.image.adapter.JschOutBoundAdapter;
import github.jhkoder.commerce.image.exception.ImageException;
import github.jhkoder.commerce.image.repository.request.ImagePathRequest;
import github.jhkoder.commerce.image.repository.request.ImageRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

@Configuration
public class ImageJschRepository implements ImageRepository {
    private final ChannelSftp channelSftp;
    private final String folderPath;

    public ImageJschRepository(JschOutBoundAdapter adapter,
                               @Value("${cloud.image-repository.folder-path}")String folderPath) {
        this.channelSftp = adapter.getChannelSftp();
        this.folderPath = folderPath;
    }


    @Override
    public boolean upload(ImageRequest imageRequest, ImagePathRequest pathRequest) throws IOException {
        return upload(imageRequest.getInputStream(), pathRequest.getPaths(), pathRequest.getFileName());
    }

    public boolean upload(InputStream is, List<String> paths, String name) {
        try {
            String path = automaticDirectoryAddition(paths);
            channelSftp.cd(path);
            channelSftp.put(is, name);
            if (this.exists(path + name)) {
                return true;
            }
        } catch (SftpException e) {
            throw new ImageException(ErrorCode.IMAGE_REMOTE_UPLOAD);
        }
        return false;
    }

    public String automaticDirectoryAddition(List<String> path) throws SftpException {
        String fullPath = folderPath;
        for (String directory : path) {
            if (!this.exists(fullPath + directory)) {
                channelSftp.cd(fullPath);
                channelSftp.mkdir(directory);
                fullPath += directory + "/";
            } else {
                break;
            }
        }
        return fullPath;
    }

    public boolean exists(String path) {
        try {
            Vector<ChannelSftp.LsEntry> res = channelSftp.ls(path);
            return !res.isEmpty();
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                return false;
            }
            throw new ImageException(ErrorCode.IMAGE_REMOTE_UPLOAD);
        }
    }
}
