package org.example.adds.QRcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Hashtable;

public class QrCodeGenerator {
    public static BufferedImage generateQRCodeImage(String text) throws Exception {
        // Set the encoding hints
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.MARGIN, 1);

        // Create the BitMatrix for the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200, hints);

        // Convert BitMatrix to BufferedImage
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 200; j++) {
                image.setRGB(i, j, bitMatrix.get(i, j) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }
        return image;
    }

    // Method to get QR code as byte array
    public static byte[] generateQRCodeImageAsByteArray(String text) throws Exception {
        BufferedImage image = generateQRCodeImage(text);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }
}
