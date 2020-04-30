package ysaak.anima.dao.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ysaak.anima.dao.model.TagModel;
import ysaak.anima.data.Tag;

import java.util.List;

@Repository
public interface TagRepository extends PagingAndSortingRepository<TagModel, String> {
    List<TagModel> findByIdIn(List<String> idList);
}
