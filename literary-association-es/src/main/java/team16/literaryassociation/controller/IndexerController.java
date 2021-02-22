package team16.literaryassociation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team16.literaryassociation.model.Reader;
import team16.literaryassociation.services.es.Indexer;
import team16.literaryassociation.services.interfaces.ReaderService;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

@RestController
@RequestMapping(value = "/api/index")
public class IndexerController {

    @Autowired
    private Indexer indexer;

    @Autowired
    ServletContext context;

    @Autowired
    private ReaderService readerService;

    private static String DATA_DIR_PATH;

    static {
        ResourceBundle rb = ResourceBundle.getBundle("application");
        DATA_DIR_PATH = rb.getString("dataDir");
    }

    @GetMapping("/books")
    public ResponseEntity<String> indexBooks() throws IOException {
        System.out.println("USAO U INDEKSIRANJE KNJIGA");
        File dataDir = getResourceFilePath(DATA_DIR_PATH);
        long start = new Date().getTime();
        int numIndexed = indexer.indexBooks(dataDir);
        long end = new Date().getTime();
        String text = "Indexing " + numIndexed + " files took "
                + (end - start) + " milliseconds";
        System.out.println(text);
        return new ResponseEntity<>(text, HttpStatus.OK);
    }

    @GetMapping(value="/beta-readers")
    public ResponseEntity<String> indexBetaReaders() throws IOException {
        System.out.println("USAO U INDEKSIRANJE BETA READERA");
        List<Reader> readers = readerService.getAllBetaReaders();
        System.out.println("Pronasao beta readere: " + readers.size());
        long start = new Date().getTime();
        int numIndexed = indexer.indexBetaReaders(readers);
        long end = new Date().getTime();
        String text = "Indexing " + numIndexed + " beta readers took "
                + (end - start) + " milliseconds";
        System.out.println(text);
        return new ResponseEntity<>(text, HttpStatus.OK);
    }

    private File getResourceFilePath(String path) {
        URL url = this.getClass().getClassLoader().getResource(path);
        assert url != null;
        System.out.println(url.getPath());
        File file = null;
        try {
            file = new File(url.toURI());
            System.out.println("Napravio fajl i vraca ga");
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
            System.out.println("Nije Napravio fajl");
        }
        return file;
    }
}
