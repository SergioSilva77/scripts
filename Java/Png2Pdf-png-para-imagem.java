package com.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Png2Pdf {
    public static void main(String[] args) {
        PDDocument doc = null;
        doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);
        try{
            BufferedImage awtImage = ImageIO.read( new File( "c://temp//line_chart.png" ) );
            PDImageXObject pdImageXObject = LosslessFactory.createFromImage(doc, awtImage);
            PDPageContentStream contentStream = new PDPageContentStream(doc, page, true, false);
            contentStream.drawImage(pdImageXObject, 200, 300, awtImage.getWidth() / 2, awtImage.getHeight() / 2);
            contentStream.close();
            doc.save( "c://temp//pdf//PDF_image.pdf" );
            doc.close();
        } catch (Exception io){
            System.out.println(" -- fail --" + io);
        }
    }
}
