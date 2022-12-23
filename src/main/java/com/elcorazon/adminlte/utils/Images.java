package com.elcorazon.adminlte.utils;

import com.elcorazon.adminlte.model.settings.main.Layer;
import com.elcorazon.adminlte.model.settings.main.Settings;
import com.elcorazon.adminlte.model.settings.Watermark;
import com.elcorazon.adminlte.repository.WatermarkRepository;
import org.springframework.core.env.Environment;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**********************************************************************************************************************/
public class Images {
    /******************************************************************************************************************/
    public static Path path = Paths.get("").toAbsolutePath();
    /******************************************************************************************************************/
    private static  Environment current_environment;
    /******************************************************************************************************************/
    public Images(Environment environment) {
        current_environment =  environment;
    }
    /******************************************************************************************************************/
    public static void setEnvironment(Environment environment) {
        current_environment =  environment;
    }
    /******************************************************************************************************************/
    private static BufferedImage resize(BufferedImage inputImage, int scaledWidth, int scaledHeight) {

        BufferedImage bufferedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = bufferedImage.createGraphics();

        graphics2D.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);

        graphics2D.dispose();

        return bufferedImage;
    }
    /******************************************************************************************************************/
    public static String getPath() {
        return path + (new com.elcorazon.adminlte.model.Settings().path);
    }
    /******************************************************************************************************************/
    public static BufferedImage getWatermark(Layer layer) throws IOException {
        if (layer.uuid.equals("")) {
            return null;
        }

        BufferedImage bufferedImage = ImageIO.read(new File(path + (new com.elcorazon.adminlte.utils.Settings(current_environment).getWatermark()) + layer.uuid + ".png"));

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        if (layer.scale != null) {
            if (layer.scale > 0) {
                bufferedImage = resize(bufferedImage, layer.scale, layer.scale);
            }
        }

        BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = combined.createGraphics();

        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, layer.alpha));

        graphics2D.drawImage(bufferedImage, 0, 0, null);

        graphics2D.dispose();

        return combined;
    }
    /******************************************************************************************************************/
    public static BufferedImage mergeImage(Settings settings) {

        BufferedImage bufferedImage = new BufferedImage(settings.width, settings.height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = bufferedImage.createGraphics();

        graphics2D.drawImage(settings.bottom.image, settings.bottom.width, settings.bottom.height, null);
        graphics2D.drawImage(settings.image, 0, 0, null);
        graphics2D.drawImage(settings.top.image, settings.top.width, settings.top.height, null);

        graphics2D.dispose();

        return bufferedImage;
    }
    /******************************************************************************************************************/
    public static String getImage(BufferedImage image) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        ImageIO.write(image, "png", byteArrayOutputStream);

        byte[] bytes = byteArrayOutputStream.toByteArray();

        return Base64Utils.encodeToString(bytes);
    }
    /******************************************************************************************************************/
    public static BufferedImage loadImage(String uuid, Boolean isWatermark) throws IOException {
        if (isWatermark) {
            return ImageIO.read(new File( Images.path + (new com.elcorazon.adminlte.utils.Settings(current_environment).getWatermark()) + uuid + ".png"));
        }

        return ImageIO.read(new File( Images.path + (new com.elcorazon.adminlte.utils.Settings(current_environment).getWatermark()) + uuid + ".png"));
    }
    /******************************************************************************************************************/
    public static Settings getSettings(String id, String top_id, String bottom_id) throws IOException {
        Settings settings = new Settings();

        try {
            settings.image = ImageIO.read(new File(Images.path + (new com.elcorazon.adminlte.utils.Settings(current_environment).getPath()) + id + ".png"));

            settings.width = settings.image.getWidth();
            settings.height = settings.image.getHeight();
        } catch (Exception e) {
            settings.image = ImageIO.read(new File(Images.path + (new com.elcorazon.adminlte.utils.Settings(current_environment).getPath()) + "\\none.png"));

            settings.width = settings.image.getWidth();
            settings.height = settings.image.getHeight();
        }

        settings.uuid = id;
        settings.name = "";

        Layer top = new Layer();

        top.uuid = top_id;
        top.scale = 100;
        top.alpha = 0.5f;
        top.width = 100;
        top.height = 100;

        settings.top = top;

        settings.top.image = getWatermark(settings.top);

        Layer bottom = new Layer();

        bottom.uuid = bottom_id;
        bottom.scale = 50;
        bottom.alpha = 0.5f;
        bottom.width = 100;
        bottom.height = 100;

        settings.bottom = bottom;

        settings.bottom.image = getWatermark(settings.bottom);

        return settings;
    }
    /******************************************************************************************************************/
    public static Settings appendSettings(Settings settings) throws IOException {

        try {
            settings.image = ImageIO.read(new File( Images.path + (new com.elcorazon.adminlte.utils.Settings(current_environment).getPath()) + settings.uuid + ".png"));
        } catch (Exception e) {
            settings.image = ImageIO.read(new File(Images.path + (new com.elcorazon.adminlte.utils.Settings(current_environment).getPath()) + "\\none.png"));
        }

        BufferedImage top = getWatermark(settings.top);

        if (top != null) {
            settings.top.image = top;
        }

        if (settings.top.alpha == 0.0) {
            settings.top.alpha = 0.5f;
        }

        if (settings.top.height == null) {
            settings.top.height = 100;
        }

        if (settings.top.width == null) {
            settings.top.width = 100;
        }

        if (settings.top.scale == null) {
            settings.top.scale = 50;
        }

        BufferedImage bottom = getWatermark(settings.bottom);

        if (bottom != null) {
            settings.bottom.image = bottom;
        }

        if (settings.bottom.alpha == 0.0) {
            settings.bottom.alpha = 0.5f;
        }

        if (settings.bottom.height == null) {
            settings.bottom.height = 100;
        }

        if (settings.bottom.width == null) {
            settings.bottom.width = 100;
        }

        if (settings.bottom.scale == null) {
            settings.bottom.scale = 50;
        }

        return settings;
    }
    /******************************************************************************************************************/
    public static List<Watermark> getWatermarks(WatermarkRepository watermarkRepository) {
        List<com.elcorazon.adminlte.model.database.Watermark> watermarks = watermarkRepository.findAll();

        List<Watermark> list = new ArrayList<>();

        for (com.elcorazon.adminlte.model.database.Watermark watermark: watermarks) {
            list.add(new Watermark(watermark.uuid, watermark.name, false));
        }

        return list;
    }
    /******************************************************************************************************************/
    public static String getCurrentWatermarks(List<Watermark> watermarks) {
        String uuid = "";

        for (Watermark watermark:watermarks) {
            if (watermark.checked) {
                uuid = watermark.uuid;
            }
        }

        return uuid;
    }
    /******************************************************************************************************************/
    public static Boolean haveWatemark(List<Watermark> watermarks) {
        for (Watermark watermark:watermarks) {
            if (watermark.checked) {
                return true;
            }
        }

        return false;
    }
}
