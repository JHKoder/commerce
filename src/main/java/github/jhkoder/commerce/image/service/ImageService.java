package github.jhkoder.commerce.image.service;

import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.image.domain.Images;
import github.jhkoder.commerce.image.exception.ImageException;
import github.jhkoder.commerce.image.repository.ImageJpaRepository;
import github.jhkoder.commerce.image.repository.ImageJschRepository;
import github.jhkoder.commerce.image.repository.ImageRepository;
import github.jhkoder.commerce.image.repository.request.ImagePathRequest;
import github.jhkoder.commerce.image.repository.request.ImageRequest;
import github.jhkoder.commerce.image.service.request.ImageUpdateRequest;
import github.jhkoder.commerce.store.service.request.StoreAddProductRequest;
import github.jhkoder.commerce.user.domain.User;
import github.jhkoder.commerce.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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

    @Transactional
    public Images upload(User user,ImageRequest request) {
        Images images = new Images(user, upload(request));
        return imageJpaRepository.save(images);
    }

    public List<Images> upload(ItemProduct itemProduct, StoreAddProductRequest.CustomMultiPartFile paths) {
        return upload(itemProduct.getUser(), paths
                .getImage()
                .stream()
                .map(ImageRequest::new)
                .toList());
    }

    public Images upload(ItemProduct itemProduct, MultipartFile paths) {
        return upload(itemProduct.getUser(), new ImageRequest(paths));
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
            throw new ImageException(ErrorCode.IMAGE_REMOTE_UPLOAD);
        }
    }


    @Transactional
    public Images upload(String username, String name, ByteArrayInputStream is) {
        filter(name);
        ImagePathRequest adapterRequest = onlyNameChange(name);
        if (imageRepository.upload(is, adapterRequest)) {
            String path = adapterRequest.getFullName();
            imageJpaRepository.save(new Images(findByUserName(username), path));
        }
        throw new ImageException(ErrorCode.IMAGE_REMOTE_UPLOAD);
    }

    @Transactional
    public void change(String username, List<ImageUpdateRequest> links) {
        User user = findByUserName(username);
        for (ImageUpdateRequest link : links) {
            if (!link.isUpdate()) {
                continue;
            }

            Images images = imageJpaRepository.findById(link.getImageId())
                    .orElseThrow(() -> new ApiException(ErrorCode.IMAGE_NOT_ID));

            if (!Objects.equals(images.getUser().getId(), user.getId())) {
                throw new ApiException(ErrorCode.IMAGE_AUTHOR_MISMATCH);
            }

            imageRepository.delete(images.getPath());
            images.updatePath(upload(new ImageRequest(link.getImage())));
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

    public void delete(List<Images> images) {
        for (Images image : images) {
            imageRepository.delete(image.getPath());
            imageJpaRepository.delete(image);
        }
    }
}
