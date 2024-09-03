package com.pdf;

import io.github.jonathanlink.PDFLayoutTextStripper;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PDFMetodos {
    public static void main(String[] args) {
        String string = null;
        try {
            PDFParser pdfParser = new PDFParser(new RandomAccessFile(new File("C:\\Users\\sergi\\Downloads\\Modelo2.pdf"), "r"));
            pdfParser.parse();
            PDDocument pdDocument = new PDDocument(pdfParser.getDocument());
            PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
            string = pdfTextStripper.getText(pdDocument);

            String[] lines = string.split("\n");
            List<String[]> table = new ArrayList<>();
            StringBuilder value = new StringBuilder();

            boolean isTable = false;
            for (int i = 0; i < lines.length;i++){
                String line = lines[i].replaceAll("^\\s+", "");
                if (line.toLowerCase().contains("original") && line.toLowerCase().contains("aprovada") && line.toLowerCase().contains("observações")){
                    isTable = true;
                    continue;
                }
                if (isTable){
                    if (line.trim() == ""){
                        table.add(value.toString().split(" "));
                        value = new StringBuilder();
                    }
                    value.append(line);
                    table.add(new String[]{});
                }
                System.out.println();
            }

            System.out.println();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        };
        System.out.println(string);
    }

    public void pdfParaImagem() {
        try {
            String pdfFilename = "C:\\Users\\anderson.pereira\\Downloads\\Temp\\a644fbc8758f788dff6a7c08c01d027d_a.pdf";
            PDDocument document = PDDocument.load(new File(pdfFilename));
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            // navega entre as paginas
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                ImageIOUtil.writeImage(bim, "C:\\Users\\Quality\\Desktop\\test.png", 300);
            }
            document.close();
        } catch (Exception e) {

        }
    }
}
