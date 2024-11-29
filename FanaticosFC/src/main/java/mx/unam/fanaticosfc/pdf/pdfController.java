package mx.unam.fanaticosfc.pdf;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class pdfController {

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generatePdf() throws Exception {
        String filePath = "reporte.pdf";
        new pdfGenerator().createCustomPdf(filePath);

        byte[] pdfBytes = Files.readAllBytes(Path.of(filePath));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
