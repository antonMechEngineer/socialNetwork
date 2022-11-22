package main.service;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
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
        System.out.println(
                cloudinary.config.cloudName);

        try {
            // Get the asset details
            Map params2 = ObjectUtils.asMap(
                    "quality_analysis", true
            );
            path = String.valueOf(cloudinary.api().resource(fileName, params2));

        } catch (Exception e) {
            System.out.println(e.getMessage());
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
            System.out.println(
                    cloudinary.url().transformation(new Transformation()
                                    .crop("pad")
                                    .width(300)
                                    .height(400)
                                    .background("auto:predominant"))
                            .imageTag("coffee_cup"));
            // The code above generates an HTML image tag similar to the following:
            //  <img src='https://res.cloudinary.com/demo/image/upload/b_auto:predominant,c_pad,h_400,w_300/coffee_cup' height='400' width='300'/>

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteImage(String publicId) {
        // Set your Cloudinary credentials
        Dotenv dotenv = Dotenv.load();
        Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
        cloudinary.config.secure = true;
        System.out.println(
                cloudinary.config.cloudName);

        try {
            cloudinary.uploader()
                    .destroy(publicId,
                            ObjectUtils.emptyMap());
        } catch (Exception e) {
            e.getMessage();
        }
    }

}
