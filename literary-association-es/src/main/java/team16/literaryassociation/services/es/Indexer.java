package team16.literaryassociation.services.es;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.handler.PDFHandler;
import team16.literaryassociation.model.Book;
import team16.literaryassociation.model.Reader;
import team16.literaryassociation.model.es.BetaReaderES;
import team16.literaryassociation.model.es.BookES;
import team16.literaryassociation.repository.BookRepository;
import team16.literaryassociation.repository.es.BetaReaderESRepository;
import team16.literaryassociation.repository.es.BookESRepository;

import java.io.File;
import java.util.List;

@Service
public class Indexer {

    @Autowired
    private BookESRepository bookESRepository;

    @Autowired
    private BetaReaderESRepository betaReaderESRepository;

    @Autowired
    private BookRepository bookRepository;

    public Indexer() {
    }

    public boolean addBook(BookES unit) {

        try{
            bookESRepository.save(unit);
            System.out.println("Sacuvao knjigu");
            return true;
        }catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean addBetaReader(BetaReaderES unit) {
        try{
            betaReaderESRepository.save(unit);
            System.out.println("Sacuvao beta readera");
            return true;
        }catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public int indexBooks(File file) {
        PDFHandler handler = new PDFHandler();
        String fileName = null;
        int retVal = 0;
        try {
            File[] files;
            if (file.isDirectory()) {
                files = file.listFiles();
            } else {
                files = new File[1];
                files[0] = file;
            }
            for (File newFile : files) {
                if (newFile.isFile()) {
                    fileName = newFile.getName();
                    Book b = bookRepository.findByFileName(fileName);
                    System.out.println("Nasao knjigu");
                    if (handler == null) {
                        System.out.println("Nije moguce indeksirati dokument sa nazivom: " + fileName);
                        continue;
                    }
                    String text = handler.getText(newFile);
                    System.out.println("Izvukao tekst iz fajla");
                    BookES bookES = new BookES(b, text);

                    if (addBook(bookES))
                        retVal++;
                } else if (newFile.isDirectory()) {
                    retVal += indexBooks(newFile);
                }
            }
            System.out.println("indexing done");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("indexing NOT done");
        }
        return retVal;
    }

    public int indexBetaReaders(List<Reader> readers)
    {
        int retVal = 0;
        for(Reader r : readers){
            BetaReaderES br = new BetaReaderES(r);
            if(addBetaReader(br)){
                retVal++;
            }
            else{
                System.out.println("Nije napravio beta readera");
            }
        }
        return retVal;
    }


}
