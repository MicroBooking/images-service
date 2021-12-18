package converters;

import classes.Image;
import dto.ImageCreateDto;

public class ImageCreateConverter {
    public static Image fromCreateDto(ImageCreateDto imageCreateDto) {
        Image image = new Image();
        image.setListingId(imageCreateDto.getListingId());

        return image;
    }
}
