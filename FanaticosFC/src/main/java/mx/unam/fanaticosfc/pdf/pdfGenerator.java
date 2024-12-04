package mx.unam.fanaticosfc.pdf;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import mx.unam.fanaticosfc.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.List;

@Service
public class pdfGenerator {
    @Autowired
    VentaRepository ventaRepository;

    public void createCustomPdf(String dest) throws Exception {

        // Consulta: obtener el empleado del mes
        List<Object[]> empleadoMes = ventaRepository.getEmpleadoDelMes();
        Object[] empleado = empleadoMes.get(0);
        String nombre = (String) empleado[0];
        String paterno = (String) empleado[1];
        Long ventasReal = (Long) empleado[2];

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));

        document.open();

        // Agregar título
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph title = new Paragraph("Fanáticos FC", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph("\n")); // Espacio extra

        // Datos del empleado del mes
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Paragraph empleadoTitulo = new Paragraph("Empleado del Mes: ", headerFont);
        empleadoTitulo.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(empleadoTitulo);

        Paragraph empleadoInfo = new Paragraph(
                nombre + " " + paterno + "     Ventas realizadas: " + ventasReal,
                FontFactory.getFont(FontFactory.HELVETICA, 12)
        );
        empleadoInfo.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(empleadoInfo);
        document.add(new Paragraph("\n")); // Espacio extra


        //Crear Tabla
        PdfPTable table = new PdfPTable(2); // Dos columnas
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        // Encabezados
        Font header2Font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        PdfPCell header1 = new PdfPCell(new Phrase("Equipo", headerFont));
        PdfPCell header2 = new PdfPCell(new Phrase("Ventas (unidades)", header2Font));
        header1.setHorizontalAlignment(Element.ALIGN_CENTER);
        header2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(header1);
        table.addCell(header2);

// Datos de la tabla
        String[][] data = {
                {"Real Madrid", "300"},
                {"Barcelona", "250"},
                {"Manchester United", "200"},
                {"PSG", "180"},
                {"Bayern Munich", "150"}
        };

        for (String[] row : data) {
            PdfPCell cell1 = new PdfPCell(new Phrase(row[0]));
            PdfPCell cell2 = new PdfPCell(new Phrase(row[1]));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);
            table.addCell(cell2);
        }

        // Agregar tabla al documento
        document.add(table);


        // Agregar texto final
        document.add(new Paragraph("\nGracias por su compra!"));

        document.close();
    }
}
