package github.jhkoder.commerce.image.repository;

import github.jhkoder.commerce.image.repository.request.ImagePathRequest;
import github.jhkoder.commerce.image.repository.request.ImageRequest;

import java.io.IOException;

public interface ImageRepository {

    boolean upload(ImageRequest imageRequest, ImagePathRequest pathRequest) throws IOException;
    boolean  delete(String fileName);
    byte[] read(String path);
}
