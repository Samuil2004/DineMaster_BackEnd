package nl.fontys.s3.dinemasterbackend.business.image_services;

import nl.fontys.s3.dinemasterbackend.business.dtos.create.items.SaveImageRequest;

public interface UpdateImage {
    void updateImage(SaveImageRequest request);
}
