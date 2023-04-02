package com.example.demo;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class DownloadController {
    private final PhotoService photoService;

    public DownloadController(PhotoService photoService){
        this.photoService = photoService;
    }
    @GetMapping("/download")
    public String getDownloadPage(){
        return "download";
    }
    //    @PostMapping("/download/{id}")
//    public String updateUser(@PathVariable String id) {
//        // do something with the user ID and the user object
//        return "redirect:/download/" + id;
//    }
    @PostMapping("/download")
    @ResponseBody
    public ResponseEntity<byte[]> download(@RequestParam("id") Integer id, Model model){
        Photo photo=photoService.getID(id);

        if(photo == null ) throw new ResponseStatusException((HttpStatus.NOT_FOUND));
        byte[] data=photo.getData();
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.valueOf(photo.getContentType()));
        ContentDisposition build=ContentDisposition.builder("inline")
                .filename(photo.getFileName()).build();
        headers.setContentDisposition(build);
        //----
        model.addAttribute("id",id);
        return new ResponseEntity<>(data,headers, HttpStatus.OK);
    }
}
