package com.oneassist.serviceplatform.commons.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.codec.Base64;
import org.springframework.stereotype.Component;

@Component
public class BarcodeGenerator {

    private static final String BARCODE_IMAGE_FORMAT = "jpg";

    public byte[] generateBarcodeBytes(String uniqueCode) throws Exception {
        Barcode barcode = generateBarcode(uniqueCode);
        java.awt.Image image = barcode.createAwtImage(Color.BLACK, Color.WHITE);

        int width = image.getWidth(null);
        int height = image.getHeight(null);
        int type = BufferedImage.TYPE_INT_RGB;
        BufferedImage bi = new BufferedImage(width, height, type);

        Graphics2D g2d = bi.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, BARCODE_IMAGE_FORMAT, baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        return imageInByte;
    }

    public String generateBarcodeInBase64String(String uniqueCode) throws Exception {
        String encodedString = null;
        byte[] imageInByte = generateBarcodeBytes(uniqueCode);
        encodedString = Base64.encodeBytes(imageInByte);
        return encodedString;
    }

    public Barcode generateBarcode(String uniqueCode) {
        Barcode barcode = new Barcode128();
        barcode.setCodeType(Barcode.CODE128);
        barcode.setCode(uniqueCode);
        return barcode;
    }
}
