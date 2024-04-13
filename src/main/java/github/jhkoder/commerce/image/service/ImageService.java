package github.jhkoder.commerce.image.service;

import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.image.domain.Images;
import github.jhkoder.commerce.image.repository.ImageJpaRepository;
import github.jhkoder.commerce.image.repository.ImageJschRepository;
import github.jhkoder.commerce.image.repository.request.ImagePathRequest;
import github.jhkoder.commerce.image.exception.ImageException;
import github.jhkoder.commerce.image.repository.ImageRepository;
import github.jhkoder.commerce.image.repository.ImageSftpRepository;
import github.jhkoder.commerce.image.repository.request.ImageRequest;
import github.jhkoder.commerce.store.service.request.StoreUpdateProductRequest;
import github.jhkoder.commerce.user.domain.User;
import github.jhkoder.commerce.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class ImageService {
    private final static Pattern extendsPattern = Pattern.compile(".*\\.(png|jpeg|jpg|gif)$");
    private final ImageRepository imageRepository;
    private final ImageJpaRepository imageJpaRepository;
    private final UserRepository userRepository;

    public ImageService(ImageJschRepository imageJschRepository, ImageJpaRepository imageJpaRepository, UserRepository userRepository) {
        this.imageRepository = imageJschRepository;
        this.imageJpaRepository = imageJpaRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<Images> upload(User user, List<ImageRequest> paths) {
        List<Images> images = new ArrayList<>();
        for (ImageRequest request : paths) {
            images.add(new Images(user, upload(request)));
        }
        return imageJpaRepository.saveAll(images);
    }


    public String upload(ImageRequest request) {
        String name = request.getFileName();
        try {
            filter(name);
            ImagePathRequest adapterRequest = onlyNameChange(name);
            if (imageRepository.upload(request, adapterRequest)) {
                return adapterRequest.getFullName();
            }
            throw new ImageException(ErrorCode.IMAGE_REMOTE_UPLOAD);
        } catch (IOException e) {
            throw new ApiException(ErrorCode.IMAGE_REMOTE_UPLOAD);
        }
    }


    @Transactional
    public void change(String username, List<StoreUpdateProductRequest.StoreImageRequest> links) {
        User user = findByUserName(username);
        for (StoreUpdateProductRequest.StoreImageRequest link : links) {
            if (!link.isUpdate()) {
                return;
            }

            Images images = imageJpaRepository.findById(link.imageId())
                    .orElseThrow(() -> new ApiException(ErrorCode.IMAGE_NOT_ID));

            if (!Objects.equals(images.getUser().getId(), user.getId())) {
                throw new ApiException(ErrorCode.IMAGE_AUTHOR_MISMATCH);
            }

            imageRepository.delete(images.getPath());
            images.updatePath(upload(new ImageRequest(link.multipartFile())));
            imageJpaRepository.save(images);
        }
    }

    public void delete(String path) {
        imageRepository.delete(path);
    }

    public byte[] read(String path) {
        return imageRepository.read(path);
    }

    private ImagePathRequest onlyNameChange(String name) {
        String uuidString = UUID.randomUUID().toString();
        String uuidPrefix = uuidString.substring(0, 2);
        String uuidSuffix = uuidString.substring(2);
        String fileName = uuidSuffix + "_" + name;
        String fullName = uuidPrefix + "/" + fileName;
        return new ImagePathRequest(List.of(uuidPrefix), fileName, fullName);
    }

    private void filter(String name) {
        if (!extendsPattern.matcher(name).find()) {
            throw new ApiException(ErrorCode.IMAGE_REMOTE_SESSION);
        }
    }

    private User findByUserName(String username) {
        return userRepository.findByUserId(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    }
}
