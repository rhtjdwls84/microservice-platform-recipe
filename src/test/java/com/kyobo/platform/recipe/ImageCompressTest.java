package com.kyobo.platform.recipe;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import javax.imageio.*;
import javax.imageio.stream.*;

public class ImageCompressTest {

  public static void main(String[] args) throws IOException {

    File input = new File("C:\\Users\\sejin\\Downloads\\JIN08417.jpg");
    BufferedImage image = ImageIO.read(input);

    File compressedImageFile = new File("C:\\Users\\sejin\\Downloads\\JIN08417_1.jpg");
    OutputStream os = new FileOutputStream(compressedImageFile);

    Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
    ImageWriter writer = (ImageWriter) writers.next();
 
    ImageOutputStream ios = ImageIO.createImageOutputStream(os);
    writer.setOutput(ios);

    ImageWriteParam param = writer.getDefaultWriteParam();
    

    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    param.setCompressionQuality(0.5f);  // Change the quality value you prefer
    writer.write(null, new IIOImage(image, null, null), param);

    os.close();
    ios.close();
    writer.dispose();
  }
}