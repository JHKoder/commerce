package github.jhkoder.commerce.image.service;

import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.image.repository.request.ImagePathRequest;
import github.jhkoder.commerce.image.exception.ImageException;
import github.jhkoder.commerce.image.repository.ImageRepository;
import github.jhkoder.commerce.image.repository.ImageSftpRepository;
import github.jhkoder.commerce.image.repository.request.ImageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class ImageService {
    private final static Pattern extendsPattern = Pattern.compile(".*\\.(peg|png|jpeg|jpg|gif)$");
    private final ImageRepository imageRepository;

    public ImageService(ImageSftpRepository imageSftpRepository) {
        this.imageRepository = imageSftpRepository;
    }

    public String upload(ImageRequest request){
        String name = request.getFileName();
        try {
            filter(name);
            ImagePathRequest adapterRequest = onlyNameChange(name);
            if(imageRepository.upload(request,adapterRequest)){
                return adapterRequest.getFullName();
            }
            throw new ImageException(ErrorCode.IMAGE_REMOTE_UPLOAD);
        } catch (IOException e) {
            throw new ApiException(ErrorCode.IMAGE_REMOTE_UPLOAD);
        }
    }

    private ImagePathRequest onlyNameChange(String name) {
        String uuidString = UUID.randomUUID().toString();
        String uuidPrefix = uuidString.substring(0, 2);
        String uuidSuffix = uuidString.substring(2);
        String fileName = uuidSuffix+"_"+name;
        String fullName = uuidPrefix+"/"+fileName;
        return new ImagePathRequest(List.of(uuidPrefix),fileName,fullName);
    }

    private void filter(String name) {
        if(!extendsPattern.matcher(name).find()){
            throw new ApiException(ErrorCode.IMAGE_REMOTE_SESSION);
        }
    }
}
