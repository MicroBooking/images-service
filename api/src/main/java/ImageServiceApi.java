import beans.ImagesBean;
import classes.Image;
import converters.ImageConverter;
import converters.ImageCreateConverter;
import dto.ImageCreateDto;
import fileuploader.FileUploader;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Consumes({MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
@Produces(MediaType.APPLICATION_JSON)
@Path("images")
public class ImageServiceApi {
    private Logger log = Logger.getLogger(ImageServiceApi.class.getName());

    @Inject
    private ImagesBean imagesBean;

    @Inject
    private FileUploader fileUploader;

    @GET
    public Response getAllImages(){
        List<Image> images = imagesBean.getImages();
        return Response.status(Response.Status.OK).entity(images).build();
    }

    @GET
    @Path("listing/{listingId}")
    public Response getImagesForListing(@PathParam("listingId") Integer listingId){
        List<Image> images = imagesBean.getImagesForListing(listingId);
        return Response.status(Response.Status.OK).entity(images).build();
    }

    @POST
    @Path("upload")
    public Response uploadImage(ImageCreateDto imageCreateDto, @FormDataParam("image") InputStream uploadedInputStream) {
        if(imageCreateDto.getListingId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Map<String, String> uploadResults = new HashMap<>();

        try {
            uploadResults = fileUploader.uploadImage(uploadedInputStream);

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        Image image = ImageCreateConverter.fromCreateDto(imageCreateDto);
        image.setUrl(uploadResults.get("url"));
        image.setCloudinaryId(uploadResults.get("publicId"));
        Image addedImage = imagesBean.createImage(ImageCreateConverter.fromCreateDto(imageCreateDto));

        return Response.status(Response.Status.OK).entity(addedImage).build();
    }
}
