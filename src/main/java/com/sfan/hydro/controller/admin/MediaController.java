package com.sfan.hydro.controller.admin;

import com.sfan.hydro.domain.enumerate.FileType;
import com.sfan.hydro.attach.MessagesResource;
import com.sfan.hydro.domain.expand.ResponseModel;
import com.sfan.hydro.domain.model.Media;
import com.sfan.hydro.service.MediaService;
import com.sfan.hydro.util.FileUtil;
import com.sfan.hydro.domain.expand.PageModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/admin/media")
public class MediaController {

    private final String PAHT_PREFIX = "/admin/media/";

    private final String[] FILE_SUFFIX = new String[]{
        ".jpg", ".png", ".jpeg"
    };

    @Autowired
    private MediaService mediaService;
    @Autowired
    MessagesResource messagesResource;

    @RequestMapping
    public String mediaView(){
        return PAHT_PREFIX + "mediaMain";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel listMedia(int skipCount, int loadCount, String fileName){
        List<Media> mediaList = mediaService.listMedia(skipCount, loadCount, fileName);
        int count = 0;
        if(mediaList.size() > 0){
            count = mediaService.countMedia(fileName);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("data", mediaList);
        result.put("count", count);
        return new ResponseModel(true, null, result);
    }

    @RequestMapping(value = "/save", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseModel saveMedia(Media media, @RequestParam("mediaFile") MultipartFile mediaFile){
        if (mediaFile.isEmpty()){
            return new ResponseModel(false, "MediaFile is required", null);
        }

        String originFileName = mediaFile.getOriginalFilename();
        String suffix = originFileName.substring(originFileName.lastIndexOf('.'));
        if(Arrays.asList(FILE_SUFFIX).contains(suffix)){
            String randomFileName = RandomStringUtils.randomAlphabetic(8);
            try {
                FileUtil.writeInFile(mediaFile.getBytes(), FileType.Image.getPath(), String.format("%s%s", randomFileName, suffix));
            }catch (IOException e){
                return new ResponseModel(false, messagesResource.getMessage("Media.file_upload_failed"), null);
            }
            media.setFilePath(randomFileName + suffix);
            media.setFileType(FileType.Image.getVal());
            media.setUploadDate(Calendar.getInstance().getTime());
            mediaService.saveMedia(media);
        }else{
            return new ResponseModel(false, messagesResource.getMessage("Media.illegal_type "), null);
        }
        return new ResponseModel(true, messagesResource.getMessage("Media.file_upload_success"), media);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseModel deleteMedia(@RequestParam(value = "mediaIds[]") Integer[] mediaIds){
        List<Media> datas = mediaService.listMedia(Arrays.asList(mediaIds));
        datas.forEach(e -> {
            try {
                FileUtil.deleteFile(FileType.get(e.getFileType()), e.getFilePath());
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
        mediaService.deleteMedia(Arrays.asList(mediaIds));
        return new ResponseModel(true, messagesResource.getMessage("Media.delete_success"), mediaIds);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel updateMedia(Media media){
        mediaService.updateMedia(media);
        return new ResponseModel(true, messagesResource.getMessage("Media.update_success"), media);
    }
}
