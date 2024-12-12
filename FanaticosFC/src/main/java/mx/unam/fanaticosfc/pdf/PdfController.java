package mx.unam.fanaticosfc.pdf;

import mx.unam.fanaticosfc.controller.detalleVenta.DetalleVentaController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

@RestController
public class PdfController {

    private static final Logger logger = LoggerFactory.getLogger(DetalleVentaController.class);
    @Autowired
    PdfGenerator pdfGenerator;
    @Autowired
    TicketPDFGenerator ticketGenerator;


    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generatePdf() throws Exception {
        String filePath = "ReporteFanaticosFC.pdf";
        pdfGenerator.createCustomPdf(filePath);

        byte[] pdfBytes = Files.readAllBytes(Path.of(filePath));
        logger.info("Se descargó el reporte de ventas con fecha: " + (LocalDateTime.now()));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/ticketpdf/{idVenta}")
    public ResponseEntity<byte[]> generarTicket(@PathVariable Integer idVenta) throws Exception{
        String filePath = "Ticket.pdf";
        ticketGenerator.createticketPdf(filePath,idVenta);

        byte[] pdfBytes = Files.readAllBytes(Path.of(filePath));
        logger.info("Se descargó el ticket de venta {}",idVenta);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ticket.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);

    }
}
