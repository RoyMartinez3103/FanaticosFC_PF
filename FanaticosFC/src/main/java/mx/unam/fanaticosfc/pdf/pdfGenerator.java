package mx.unam.fanaticosfc.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class pdfGenerator {
    public void createCustomPdf(String dest) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));

        document.open();

        // Agregar título
        document.add(new Paragraph("Reporte de Ventas\n\n"));

        // Agregar tabla
        Table table = new Table(3); // 3 columnas
        table.addCell("ID");
        table.addCell("Producto");
        table.addCell("Precio");

        table.addCell("1");
        table.addCell("Playera de fútbol");
        table.addCell("$500");

        table.addCell("2");
        table.addCell("Balón de fútbol");
        table.addCell("$300");

        document.add(table);

        // Agregar texto final
        document.add(new Paragraph("\nGracias por su compra!"));

        document.close();
    }
}
