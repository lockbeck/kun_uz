package com.company.service;

import com.company.dto.AttachDTO;
import com.company.entity.AttachEntity;
import com.company.exp.ItemNotFoundException;
import com.company.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class AttachService {
    @Autowired
    private AttachRepository attachRepository;
    @Value("${attach.folder}")
    private String attachFolder;
    @Value("${server.url}")
    private String serverUrl;
/*
    public String saveToSystem(MultipartFile file) {
        try {
            // zari.jpg
            // asdas-dasdas-dasdasd-adadsd.jpg
            String pathFolder = getYmDString();
            String uuid = UUID.randomUUID().toString();
            String extension = getExtension(file.getOriginalFilename());
            String fileName = uuid + "." + extension;
            File folder = new File( attachFolder+pathFolder );
            if (!folder.exists()) {
                folder.mkdirs();
            }
            byte[] bytes = file.getBytes();
            String replace = pathFolder.replace("/", "_");
            Path path = Paths.get(attachFolder+pathFolder+"/" +replace+"_"+fileName);
            Files.write(path, bytes);
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
*/
public  AttachDTO saveToSystem(MultipartFile file) {
    try {
        // zari.jpg
        String pathFolder = getYmDString(); // 2022/06/20
        String uuid = UUID.randomUUID().toString(); //  asdas-dasdas-dasdasd-adadsd
        String extension = getExtension(file.getOriginalFilename()); // jpg

        String fileName = uuid + "." + extension; //  asdas-dasdas-dasdasd-adadsd.jpg


        File folder = new File(attachFolder + pathFolder); // attaches/2022/06/20
        if (!folder.exists()) {
            folder.mkdirs();
        }
        byte[] bytes = file.getBytes();
        // attaches/2022/06/20/asdas-dasdasd-asdas0asdas.jpg
        Path path = Paths.get(attachFolder + pathFolder + "/" + fileName);
        Files.write(path, bytes);
        AttachEntity entity = new AttachEntity();
        entity.setId(uuid);
        entity.setOriginalName(file.getOriginalFilename());
        entity.setExtension(extension);
        entity.setSize( file.getSize() );
        entity.setPath(pathFolder);
        attachRepository.save(entity);

        AttachDTO dto = new AttachDTO();
        dto.setUrl(serverUrl +"attach/open/"+entity.getId());
        return dto;
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}


    public byte[] loadImage(String id) {
        byte[] imageInByte;
        String path = getFileFullPath(get(id));

        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(new File(path));
        } catch (Exception e) {
            return new byte[0];
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(originalImage, "png", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imageInByte;
    }



    public byte[] open_general(String fileName) {
        byte[] data;
        try {
            // fileName -> zari.jpg
            String path = "attaches/" + fileName;
            Path file = Paths.get( path);
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public byte[] open_general2(String id) {
        byte[] data;
        try {
            // fileName -> zari.jpg
            String path = getFileFullPath(get(id));
            System.out.println(path);
            Path file = Paths.get(path);
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public ResponseEntity<Resource> download(String id) {
        try {
            AttachEntity entity = get(id);
            String path = getFileFullPath(entity);
            Path file = Paths.get(path);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + entity.getOriginalName() + "\"").body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public String delete(String id) {
        Optional<AttachEntity> byId = attachRepository.findById(id);
        if (byId.isEmpty()) {
            throw new ItemNotFoundException("Mazgimisan bunaqa attach yo'q");
        }
        AttachEntity entity = byId.get();
        String path = getFileFullPath(entity);

        try {
            Files.delete(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        attachRepository.deleteById(entity.getId());
        return "successfully deleted";
    }


    private String getExtension(String fileName) { // mp3/jpg/npg/mp4.....
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }
    public String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);

        return year + "/" + month + "/" + day; // 2022/04/23
    }
    public String getTmDasUrlLink(String folder) { //  2022/06/20
        String[] arr = folder.split("/");
        return arr[0] + "_" + arr[1] + "_" + arr[2];
    }
    public String getFolderPathFromUrl(String url) { // 2022_6_20_f978a682-a357-4eaf-ac18-ec9482a4e58b.jpg
        String[] arr = url.split("_");
        return arr[0] + "/" + arr[1] + "/" + arr[2] + "/" + arr[3];
        // 2022/06/20/f978a682-a357-4eaf-ac18-ec9482a4e58b.jpg
    }
    public  AttachEntity get(String id){
        return attachRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException("Attach not found");
        } );

    }
    public String getIdFromFileName(String fileName){
        int lastIndexOf = fileName.lastIndexOf(".");
        return fileName.substring(0, lastIndexOf);
}
    public String getFileFullPath(AttachEntity entity) {
        return attachFolder + entity.getPath() + "/" + entity.getId() + "." + entity.getExtension();
    }
}
