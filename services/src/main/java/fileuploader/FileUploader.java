package fileuploader;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.google.common.io.ByteStreams;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class FileUploader {
    private final Cloudinary cloudinary;
    public FileUploader(){
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", System.getenv("CLOUDINARY_CLOUD_NAME"),
                "api_key", System.getenv("CLOUDINARY_API_KEY"),
                "api_secret", System.getenv("CLOUDINARY_API_SECRET")));
    }

    public Map<String, String> uploadImage(InputStream uploadedInputStream) throws IOException {
        byte[] uploadedBytes = ByteStreams.toByteArray(uploadedInputStream);
        Map uploadResult = this.cloudinary.uploader().upload(uploadedBytes, ObjectUtils.emptyMap());
        System.out.println(uploadResult.toString());
        String imageUri = uploadResult.get("url").toString();
        String cloudinaryId = uploadResult.get("public_id").toString();
        Map<String, String> uploadData = new HashMap<>();
        uploadData.put("uri", imageUri);
        uploadData.put("public_id", cloudinaryId);

        return uploadData;
    }


}
