package mx.unam.fanaticosfc.pdf;

import com.lowagie.text.*;

import java.awt.*;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import mx.unam.fanaticosfc.model.Venta;
import mx.unam.fanaticosfc.repository.DetalleVentaRepository;
import mx.unam.fanaticosfc.repository.VentaRepository;
import mx.unam.fanaticosfc.service.venta.VentaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TicketPDFGenerator {

    @Autowired
    DetalleVentaRepository detalleRepository;

    @Autowired
    VentaServiceImpl ventaService;

    //Fuentes
    Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
    Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);;
    Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA,11);
    Paragraph enter = new Paragraph("\n");

    public void createticketPdf(String dest,Integer idVenta) throws Exception{

        List<Object[]> ticket = detalleRepository.getDetallesTicket(idVenta);
        List<String> equipos = new ArrayList<>();
        List<String> marcas = new ArrayList<>();
        List<String> colores = new ArrayList<>();
        List<String> tallas = new ArrayList<>();
        List<Integer> cantidades = new ArrayList<>();
        List<Float> preciosVenta = new ArrayList<>();
        Float montoTotal = 0.0F;
        String username = "";
        Boolean ventaCredito = null;
        Timestamp fechaVenta = null;

        for (Object[] t : ticket){
            String equipo = (String) t[0];
            String marca = (String) t[1];
            String color = (String) t[2];
            String talla = (String) t[3];
            Integer cantidad = (Integer) t[4];
            Float precioVenta = (Float) t[5];
            montoTotal = (Float) t[6];
            username = (String) t[7];
            ventaCredito = (Boolean) t[8];
            fechaVenta = (Timestamp) t[9];

            equipos.add(equipo);
            marcas.add(marca);
            colores.add(color);
            tallas.add(talla);
            cantidades.add(cantidad);
            preciosVenta.add(precioVenta);

        }
        //********************

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));

        document.open();

        // Agregar título

        Paragraph title = new Paragraph("Ticket de Venta", paragraphFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        title = new Paragraph("Fanáticos FC", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        // Agregar imagen (logo)
        String imagePath = "src/main/resources/static/img/LogoFANATICOS.png";
        Image logo = Image.getInstance(imagePath);
        logo.scaleToFit(90, 90); // Ajustar el tamaño de la imagen
        logo.setAlignment(Image.ALIGN_CENTER);
        document.add(logo);

        Paragraph paragraph = new Paragraph("Sucursal Centro #123",paragraphFont);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        paragraph = new Paragraph("Tel: (555) 123-4567",paragraphFont);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);
        document.add(enter);
        document.add(enter);

        paragraph = new Paragraph("Ticket: #"+idVenta);
        paragraph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(paragraph);

        paragraph = new Paragraph("Cajero: "+username);
        paragraph.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(paragraph);
        document.add(enter);

        //Crear Tabla
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        // Encabezados
        PdfPCell header1 = new PdfPCell(new Phrase("Producto", headerFont));
        PdfPCell header2 = new PdfPCell(new Phrase("Cantidad", headerFont));
        PdfPCell header3 = new PdfPCell(new Phrase("Precio", headerFont));
        PdfPCell header4 = new PdfPCell(new Phrase("Subtotal", headerFont));
        header1.setHorizontalAlignment(Element.ALIGN_CENTER);
        header2.setHorizontalAlignment(Element.ALIGN_CENTER);
        header3.setHorizontalAlignment(Element.ALIGN_CENTER);
        header4.setHorizontalAlignment(Element.ALIGN_CENTER);
        header1.setVerticalAlignment(Element.ALIGN_CENTER);
        header2.setVerticalAlignment(Element.ALIGN_CENTER);
        header3.setVerticalAlignment(Element.ALIGN_CENTER);
        header4.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(header1);
        table.addCell(header2);
        table.addCell(header3);
        table.addCell(header4);

        //Datos de la tabla
        for (int i = 0 ; i < ticket.size(); i++){
            String strSubTotal = String.valueOf(preciosVenta.get(i)*cantidades.get(i));
            PdfPCell cell1 = new PdfPCell(new Phrase(equipos.get(i) + " - " + marcas.get(i) + " / " + colores.get(i)  + " / " + tallas.get(i)));
            PdfPCell cell2 = new PdfPCell(new Phrase(cantidades.get(i).toString()));
            PdfPCell cell3 = new PdfPCell(new Phrase("$ " + preciosVenta.get(i).toString()));
            PdfPCell cell4 = new PdfPCell(new Phrase("$ " + strSubTotal));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
        }

        document.add(table);
        document.add(enter);

        title = new Paragraph("Total: $ " + montoTotal,titleFont);
        title.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(title);

        document.add(enter);
        document.add(enter);

        String strTipo = "";
        if(Boolean.TRUE.equals(ventaCredito)){
            strTipo = "A Crédito";
        }else {
            strTipo = "De contado";
        }

        paragraph = new Paragraph("Tipo de venta: " + strTipo,paragraphFont);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaFormateada = fechaVenta.toString();

        paragraph = new Paragraph("Fecha: " + fechaVenta, paragraphFont);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        document.add(enter);
        document.add(enter);

        paragraph = new Paragraph("¡Gracias por su compra!", paragraphFont);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        paragraph = new Paragraph("Este documento es un comprobante de venta", paragraphFont);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        document.add(enter);
        document.add(enter);

        imagePath = "src/main/resources/static/img/barcode.png";
        logo = Image.getInstance(imagePath);
        logo.scaleToFit(90,90);
        logo.setAlignment(Element.ALIGN_CENTER);
        document.add(logo);

        paragraph = new Paragraph(idVenta.toString(), paragraphFont);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        document.close();
    }
}
