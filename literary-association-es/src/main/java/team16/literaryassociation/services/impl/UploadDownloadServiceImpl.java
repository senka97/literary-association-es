package team16.literaryassociation.services.impl;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import team16.literaryassociation.model.Book;
import team16.literaryassociation.services.es.SearchService;
import team16.literaryassociation.services.interfaces.BookService;
import team16.literaryassociation.services.interfaces.UploadDownloadService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
public class UploadDownloadServiceImpl implements UploadDownloadService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private BookService bookService;

    @Autowired
    private SearchService searchService;

    @Value("${upload_folder}")
    private String uploadFolder;

    @Override
    public String uploadFile(MultipartFile file, String processId, int counter) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if(!Files.exists(Paths.get(uploadFolder + processId))) {
            try {
                Files.createDirectory(Paths.get(uploadFolder + processId));
                System.out.println("Kreira direktorijum ");
            } catch (IOException e1) {
                System.out.println("Fejluje da kreira direktorijum ");
                e1.printStackTrace();
            }
        }

        try {
            Files.copy(file.getInputStream(), Paths.get(uploadFolder + processId).resolve(file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Kreira fajl");
        } catch (Exception e) {
            System.out.println("Ne Kreira fajl");
            throw new RuntimeException("FAIL!");
        }

        String filePath = uploadFolder + processId + "/" + fileName;
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/task/downloadFile").toUriString();
        fileDownloadUri += "?filePath=" + uploadFolder + processId + "/" + fileName;
        System.out.println("U upload file - download URL:");
        System.out.println(fileDownloadUri);
        String pdfDownload = (String) runtimeService.getVariable(processId, "pdfDownload");
        if(counter == 1) { //ovo znaci da se uploaduje prvi fajl ako ih moze biti vise
            pdfDownload = fileDownloadUri;
        }
        if(counter > 1){ //ovo znaci da se uploaduje sledeci fajl ako je multiselect
            pdfDownload += "|" + fileDownloadUri;
        }

        runtimeService.setVariable(processId, "pdfDownload", pdfDownload);
        String postavljeno = (String)runtimeService.getVariable(processId,"pdfDownload");
        System.out.println("pdfDownload je " + postavljeno);
        return filePath;
    }

    //MOCKUP ZA UDD
    @Override
    public String uploadFilePlagiarism(MultipartFile file) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if(!Files.exists(Paths.get(uploadFolder))) {
            try {
                Files.createDirectory(Paths.get(uploadFolder));
                System.out.println("Kreira direktorijum ");
            } catch (IOException e1) {
                System.out.println("Fejluje da kreira direktorijum ");
                e1.printStackTrace();
            }
        }

        try {
            Files.copy(file.getInputStream(), Paths.get(uploadFolder).resolve(file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Kreira fajl");
        } catch (Exception e) {
            System.out.println("Ne Kreira fajl");
            throw new RuntimeException("FAIL!");
        }

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/task/downloadFile").toUriString();
        fileDownloadUri += "?filePath=" + uploadFolder + fileName;
        System.out.println("U upload file - download URL:");
        System.out.println(fileDownloadUri);

        createBook(file,fileName);
        System.out.println("SACUVAO KNJIGU LOKALNO");

        return searchService.checkForPlagiarism(file);
    }


    @Override
    public Resource downloadFile(String filePath) throws MalformedURLException {

        try {
            Path file = Paths.get(filePath);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException();
            }
        }
        catch (MalformedURLException e) {
            throw e;
        }
    }

    private Book createBook(MultipartFile file, String fileName){
        Book book = new Book();

        book.setPlagiarism(false);
        book.setTitle(file.getOriginalFilename());
        book.setSynopsis("Synopsis");
        book.setPublishersAddress("Adresa");
        book.setNumOfPages(232);
        book.setISBN("12345");
        book.setYear("2021.");
        book.setOpenAccess(true);
        book.setPrice(10);
        book.setPdf(uploadFolder + fileName);
        book.setFileName(file.getOriginalFilename());

        return bookService.save(book);
    }
}
