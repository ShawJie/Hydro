package com.sfan.hydro.controller;

import com.sfan.hydro.domain.enumerate.FileType;
import com.sfan.hydro.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
public class FileController {

    @Autowired
    ResourceLoader resourceLoader;

    @RequestMapping(method = RequestMethod.GET, value = "/media/{filename:.+}", produces = "image/jpg")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String filename) throws IOException {
        File file = FileUtil.getFile(FileType.Image, filename);
        if(file != null){
            Resource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok(resource);
        }
        return ResponseEntity.notFound().build();
    }
}
