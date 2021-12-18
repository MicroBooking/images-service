package converters;

import classes.Image;
import entities.ImageEntity;

public class ImageConverter {
    public static Image toDto(ImageEntity entity) {
        Image dto = new Image();
        dto.setImageId(entity.getId());
        dto.setListingId(entity.getListingId());
        dto.setCloudinaryId(entity.getCloudinaryId());
        dto.setUrl(entity.getUrl());

        return dto;
    }

    public static ImageEntity toEntity(Image dto) {
        ImageEntity entity = new ImageEntity();
        entity.setId(dto.getImageId());
        entity.setListingId(dto.getListingId());
        entity.setCloudinaryId(dto.getCloudinaryId());
        entity.setUrl(dto.getUrl());

        return entity;
    }
}
