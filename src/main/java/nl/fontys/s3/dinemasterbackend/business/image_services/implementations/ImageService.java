package nl.fontys.s3.dinemasterbackend.business.image_services.implementations;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.exceptions.OperationNotPossible;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
@AllArgsConstructor
public class ImageService {
    private final Cloudinary cloudinary;

    public Map<String, String> uploadImage(MultipartFile file) {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "items",
                    "invalidate", true
            ));
            String version = String.valueOf(uploadResult.get("version"));
            String publicId = (String) uploadResult.get("public_id");

            // Return a map with the required details
            Map<String, String> result = new HashMap<>();
            result.put("version", version);
            result.put("public_id", publicId);

            return result;
        } catch (IOException e) {
            throw new OperationNotPossible("Failed to upload image");
        }
    }

    public boolean deleteImage(String itemImageUrl) {
        try {
            Map<String, Object> deleteResult = cloudinary.uploader().destroy(itemImageUrl, ObjectUtils.emptyMap());
            return "ok".equals(deleteResult.get("result"));
        } catch (IOException e) {
            throw new OperationNotPossible("Failed to delete image");
        }
    }

}
