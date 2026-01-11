package com.mondial2030.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.Base64;
import javax.imageio.ImageIO;

/**
 * Utilitaire pour la génération de codes QR.
 * Utilisé pour créer les QR codes des tickets.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class QRCodeGenerator {
    
    private static final Logger logger = LoggerFactory.getLogger(QRCodeGenerator.class);
    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_HEIGHT = 200;
    private static final SecureRandom random = new SecureRandom();
    
    /**
     * Génère un code unique pour un ticket
     * Format: M2030-[MATCH_ID]-[TIMESTAMP]-[RANDOM]
     * 
     * @param matchId ID du match
     * @return Code unique généré
     */
    public static String generateTicketCode(Long matchId) {
        long timestamp = System.currentTimeMillis();
        int randomPart = random.nextInt(9999);
        return String.format("M2030-%d-%d-%04d", matchId, timestamp, randomPart);
    }
    
    /**
     * Génère une image QR code à partir d'un texte
     * 
     * @param text Texte à encoder
     * @return Image JavaFX du QR code
     */
    public static Image generateQRCodeImage(String text) {
        return generateQRCodeImage(text, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    /**
     * Génère une image QR code avec dimensions personnalisées
     * 
     * @param text Texte à encoder
     * @param width Largeur de l'image
     * @param height Hauteur de l'image
     * @return Image JavaFX du QR code
     */
    public static Image generateQRCodeImage(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            return SwingFXUtils.toFXImage(bufferedImage, null);
            
        } catch (WriterException e) {
            logger.error("Erreur lors de la génération du QR code", e);
            return null;
        }
    }
    
    /**
     * Génère un QR code en format Base64 pour stockage en BDD
     * 
     * @param text Texte à encoder
     * @return String Base64 de l'image
     */
    public static String generateQRCodeBase64(String text) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "PNG", baos);
            
            return Base64.getEncoder().encodeToString(baos.toByteArray());
            
        } catch (Exception e) {
            logger.error("Erreur lors de la génération du QR code Base64", e);
            return null;
        }
    }
    
    /**
     * Convertit une chaîne Base64 en Image JavaFX
     * 
     * @param base64 Chaîne Base64
     * @return Image JavaFX
     */
    public static Image base64ToImage(String base64) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64);
            return new Image(new ByteArrayInputStream(imageBytes));
        } catch (Exception e) {
            logger.error("Erreur lors de la conversion Base64 vers Image", e);
            return null;
        }
    }
    
    /**
     * Génère une référence de transaction unique
     * Format: TRX-[TIMESTAMP]-[RANDOM]
     * 
     * @return Référence unique
     */
    public static String generateTransactionReference() {
        long timestamp = System.currentTimeMillis();
        int randomPart = random.nextInt(999999);
        return String.format("TRX-%d-%06d", timestamp, randomPart);
    }
}
