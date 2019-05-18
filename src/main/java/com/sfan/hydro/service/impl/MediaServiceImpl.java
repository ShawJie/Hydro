package com.sfan.hydro.service.impl;

import com.sfan.hydro.dao.MediaDao;
import com.sfan.hydro.domain.model.Media;
import com.sfan.hydro.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("mediaService")
public class MediaServiceImpl implements MediaService {

    @Autowired
    private MediaDao mediaDao;

    @Override
    public void saveMedia(Media media){
        mediaDao.saveMedia(media);
    }

    @Override
    public void updateMedia(Media media) {
        mediaDao.updateMedia(media);
    }

    @Override
    public void deleteMedia(int id) {
        mediaDao.deleteMedia(id);
    }

    @Override
    public void deleteMedia(Collection<Integer> ids) {
        mediaDao.deleteMediaByIdList(ids);
    }

    @Override
    public List<Media> listMedia(Media param) {
        return mediaDao.listMedia(param);
    }

    @Override
    public List<Media> listMedia(int skipCount, int loadCount, String mediaName) {
        Map<String, Object> param = new HashMap<>();
        param.put("skipCount", skipCount);
        param.put("loadCount", loadCount);
        param.put("mediaName", mediaName);
        return mediaDao.listMediaBySkip(param);
    }

    @Override
    public List<Media> listMedia(Collection<Integer> ids) {
        return mediaDao.listMediaByIds(ids);
    }

    @Override
    public int countMedia(String fileName) {
        return mediaDao.countMedia(fileName);
    }
}
