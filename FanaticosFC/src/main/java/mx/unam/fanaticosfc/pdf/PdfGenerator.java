package mx.unam.fanaticosfc.pdf;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import mx.unam.fanaticosfc.repository.DetalleVentaRepository;
import mx.unam.fanaticosfc.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class PdfGenerator {
    @Autowired
    VentaRepository ventaRepository;
    @Autowired
    DetalleVentaRepository detalleRepository;

    //Fuentes
    Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
    Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);;
    Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA,11);
    Paragraph enter = new Paragraph("\n");

    //Fecha
    LocalDateTime fechaActual = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    String fechaFormateada = fechaActual.format(formatter);

    public void createCustomPdf(String dest) throws Exception {

        //************* CONSULTAS ***********
        // Obtener el empleado del mes
        List<Object[]> empleadoMes = ventaRepository.getEmpleadoDelMes();
        Object[] empleado = empleadoMes.get(0);
        String nombre = (String) empleado[0];
        String paterno = (String) empleado[1];
        Long ventasReal = (Long) empleado[2];

        //Playera más vendida
        List<Object[]> playeraMasVendida = detalleRepository.getPlayeraMasVendida();
        Object[] fila = playeraMasVendida.get(0);
        String equipo = (String) fila[0];
        String color = (String) fila[1];
        BigDecimal cantidad = (BigDecimal) fila[2];

        //Ganancia del mes
        BigDecimal gananciaMes = ventaRepository.gananciaMes();
        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("es","MX"));
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        String montoFormateado = numberFormat.format(gananciaMes);

        //Total de ventas en el mes
        Integer ventasMes = ventaRepository.ventasMes();

        //Top 5 playeras más vendidas
        List<Object[]> top5playeras = detalleRepository.getTop5Playeras();
        List<String> equipoPlayera = new ArrayList<>();
        List<BigDecimal> cantidadVendidas = new ArrayList<>();

        for(Object[] obj : top5playeras){
            String equipoP = (String) obj[0];
            BigDecimal cantVendidas = (BigDecimal) obj[1];

            equipoPlayera.add(equipoP);
            cantidadVendidas.add(cantVendidas);
        }

        //Tallas más vendidas
        List<Object[]> tallasVendidas = detalleRepository.getTallasVendidas();
        List<String> tallas = new ArrayList<>();
        List<BigDecimal> tvendidas = new ArrayList<>();

        for(Object[] objs : tallasVendidas){
            String talla = (String) objs[0];
            BigDecimal cant = (BigDecimal) objs[1];

            tallas.add(talla);
            tvendidas.add(cant);
        }

        List<Object[]> tiposDeVentas = ventaRepository.getTiposVentas();
        List<String> tipos = new ArrayList<>();
        List<Long> vecesRealizadas = new ArrayList<>();

        for(Object[] objs : tiposDeVentas){
            String tipoV = (String) objs[0];
            Long veces = (Long) objs[1];

            tipos.add(tipoV);
            vecesRealizadas.add(veces);
        }

        //**********************************

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));

        document.open();

        // Agregar imagen (logo)
        String imagePath = "src/main/resources/static/img/LogoFANATICOS.png";
        Image logo = Image.getInstance(imagePath);
        logo.scaleToFit(90, 90); // Ajustar el tamaño de la imagen
        logo.setAlignment(Image.ALIGN_CENTER);
        document.add(logo);

        // Agregar título

        Paragraph title = new Paragraph("Fanáticos FC", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
        document.add(enter);

        //Total de ventas en el mes
        Paragraph ventas = new Paragraph("Ventas realizadas en el mes: ", headerFont);
        ventas.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(ventas);

        ventas= new Paragraph(ventasMes.toString(),paragraphFont);
        document.add(ventas);

        //Monto vendido en el mes
        Paragraph montoTitulo = new Paragraph("Monto total vendido en el mes: ", headerFont);
        montoTitulo.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(montoTitulo);

        montoTitulo = new Paragraph("$ "+
                montoFormateado, paragraphFont);
        document.add(montoTitulo);

        //Playera más vendida del mes
        Paragraph playeraVendida = new Paragraph("Playera más vendida del mes: ",headerFont);
        playeraVendida.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(playeraVendida);

        playeraVendida = new Paragraph( equipo + ", " + color + ". " + cantidad + " veces vendida.",paragraphFont);
        playeraVendida.setAlignment(Element.ALIGN_LEFT);
        document.add(playeraVendida);

        // Datos del empleado del mes
        Paragraph empleadoTitulo = new Paragraph("Empleado del mes: ", headerFont);
        empleadoTitulo.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(empleadoTitulo);

        Paragraph empleadoInfo = new Paragraph(
                nombre + " " + paterno + ", " + ventasReal + " ventas realizadas.", paragraphFont);
        empleadoInfo.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(empleadoInfo);
        document.add(new Paragraph("\n")); // Espacio extra


        //Crear Tabla
        PdfPTable table = new PdfPTable(2); // Dos columnas
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        // Encabezados
        PdfPCell header1 = new PdfPCell(new Phrase("Equipo", headerFont));
        PdfPCell header2 = new PdfPCell(new Phrase("Ventas (unidades)", headerFont));
        header1.setHorizontalAlignment(Element.ALIGN_CENTER);
        header2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(header1);
        table.addCell(header2);

        // Datos de la tabla
        for (int i = 0; i < equipoPlayera.size(); i++) {
            PdfPCell cell1 = new PdfPCell(new Phrase(equipoPlayera.get(i)));
            PdfPCell cell2 = new PdfPCell(new Phrase(String.valueOf(cantidadVendidas.get(i))));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);
            table.addCell(cell2);
        }

        Paragraph tituloTabla = new Paragraph("Top 5 Equipos con más playeras vendidas", headerFont);
        tituloTabla.setAlignment(Element.ALIGN_CENTER);
        document.add(tituloTabla);
        document.add(table);

        table.deleteBodyRows();

        document.add(enter);

        header1 = new PdfPCell(new Phrase("Talla", headerFont));
        header2 = new PdfPCell(new Phrase("Ventas (unidades)", headerFont));
        header1.setHorizontalAlignment(Element.ALIGN_CENTER);
        header2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(header1);
        table.addCell(header2);

        // Datos de la tabla (Tallas)
        for (int i = 0; i < tallas.size(); i++) {
            PdfPCell cell1 = new PdfPCell(new Phrase(tallas.get(i)));
            PdfPCell cell2 = new PdfPCell(new Phrase(String.valueOf(tvendidas.get(i))));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);
            table.addCell(cell2);
        }

        tituloTabla = new Paragraph("Tallas más vendidas", headerFont);
        tituloTabla.setAlignment(Element.ALIGN_CENTER);
        document.add(tituloTabla);
        document.add(table);

        table.deleteBodyRows();

        document.add(enter);

        header1 = new PdfPCell(new Phrase("Tipo de Venta", headerFont));
        header2 = new PdfPCell(new Phrase("Veces Realizada", headerFont));
        header1.setHorizontalAlignment(Element.ALIGN_CENTER);
        header2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(header1);
        table.addCell(header2);

        // Datos de la tabla (Tallas)
        for (int i = 0; i < tipos.size(); i++) {
            PdfPCell cell1 = new PdfPCell(new Phrase(tipos.get(i)));
            PdfPCell cell2 = new PdfPCell(new Phrase(String.valueOf(vecesRealizadas.get(i))));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);
            table.addCell(cell2);
        }

        tituloTabla = new Paragraph("Tipos de Venta realizados", headerFont);
        tituloTabla.setAlignment(Element.ALIGN_CENTER);
        document.add(tituloTabla);
        document.add(table);

        // Agregar texto final
        Paragraph footer = new Paragraph("\nEl reporte se generó el día " + fechaFormateada,
                FontFactory.getFont(FontFactory.HELVETICA, 8));
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);


        document.close();
    }
}
