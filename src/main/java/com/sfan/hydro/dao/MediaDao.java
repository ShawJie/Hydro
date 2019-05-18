package com.sfan.hydro.dao;

import com.sfan.hydro.domain.model.Media;
import com.sfan.hydro.domain.expand.PageModel;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
public interface MediaDao {
    void saveMedia(Media media);
    void updateMedia(Media media);
    void deleteMedia(int id);
    void deleteMediaByIdList(Collection<Integer> ids);
    List<Media> listMedia(Media param);
    List<Media> listMediaBySkip(Map<String, Object> param);
    List<Media> listMediaByIds(Collection<Integer> ids);
    int countMedia(String fileName);
}
