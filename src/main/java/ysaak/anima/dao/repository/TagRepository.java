package ysaak.anima.dao.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ysaak.anima.dao.model.TagModel;

@Repository
public interface TagRepository extends PagingAndSortingRepository<TagModel, String> {
}
