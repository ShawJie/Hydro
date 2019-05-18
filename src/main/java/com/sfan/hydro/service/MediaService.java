package com.sfan.hydro.service;

import com.sfan.hydro.domain.model.Media;
import com.sfan.hydro.domain.expand.PageModel;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public interface MediaService {
    void saveMedia(Media media);
    void updateMedia(Media media);
    void deleteMedia(int id);
    void deleteMedia(Collection<Integer> ids);
    List<Media> listMedia(Media param);
    List<Media> listMedia(int skipCount, int loadCount, String fileName);
    List<Media> listMedia(Collection<Integer> ids);
    int countMedia(String fileName);
}
