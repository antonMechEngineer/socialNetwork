package main.service;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.Map;


@Service
@AllArgsConstructor
public class CloudaryService {
    public void uploadImage(File file) {
        // Set your Cloudinary credentials
        Dotenv dotenv = Dotenv.load();
        Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
        cloudinary.config.secure = true;
        System.out.println(
                cloudinary.config.cloudName);

        try {
            // Upload the image
            Map params1 = ObjectUtils.asMap(
                    "use_filename", true,
                    "unique_filename", false,
                    "overwrite", true
            );
                    cloudinary.uploader().upload(file, params1);
       //     cloudinary.uploader().rename(file.toString(), newFileName, params1);
        } catch (Exception e) {
            e.getMessage();
        }
    }
    public String getImage(String fileName) {
        String path="";
        // Set your Cloudinary credentials
        Dotenv dotenv = Dotenv.load();
        Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
        cloudinary.config.secure = true;

        try {
            // Get the asset details
            Map params2 = ObjectUtils.asMap(
                    "quality_analysis", true
            );
            path = (String) cloudinary.api().resource(fileName, params2).get("url");
        } catch (Exception e) {
            e.getMessage();
        }
        return path;
    }

    public void transformImage() {
        // Set your Cloudinary credentials
        Dotenv dotenv = Dotenv.load();
        Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
        cloudinary.config.secure = true;
        System.out.println(
                cloudinary.config.cloudName);

        try {
            // Create the image tag with the transformed image and log it to the console
                    cloudinary.url().transformation(new Transformation()
                                    .crop("pad")
                                    .width(300)
                                    .height(400)
                                    .background("auto:predominant"))
                            .imageTag("coffee_cup");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteImage(String publicId) {
        // Set your Cloudinary credentials
        Dotenv dotenv = Dotenv.load();
        Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
        cloudinary.config.secure = true;

        try {
            cloudinary.uploader()
                    .destroy(publicId,
                            ObjectUtils.emptyMap());
        } catch (Exception e) {
            e.getMessage();
        }
    }

}
