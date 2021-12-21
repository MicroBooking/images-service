import beans.ImagesBean;
import classes.Image;
import converters.ImageConverter;
import converters.ImageCreateConverter;
import dto.ImageCreateDto;
import fileuploader.FileUploader;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.charset.StandardCharsets;
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

    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @POST
    @Path("upload")
    public Response uploadImage(@FormDataParam("listing_id") Integer listingId, @FormDataParam("image") InputStream uploadedInputStream) {
        if(listingId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Map<String, String> uploadResults = new HashMap<>();

        try {
            uploadResults = fileUploader.uploadImage(uploadedInputStream);

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        Image image = new Image();
        image.setListingId(listingId);
        image.setUrl(uploadResults.get("url"));
        image.setCloudinaryId(uploadResults.get("publicId"));
        Image addedImage = imagesBean.createImage(image);

        return Response.status(Response.Status.OK).entity(addedImage).build();
    }

    private void saveToFile(InputStream uploadedInputStream,
                            String uploadedFileLocation) {

        try {
            OutputStream out = null;
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }
}
