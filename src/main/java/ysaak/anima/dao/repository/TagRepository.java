package ysaak.anima.dao.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ysaak.anima.data.Tag;

import java.util.List;

@Repository
public interface TagRepository extends PagingAndSortingRepository<Tag, String> {
    List<Tag> findByIdIn(List<String> idList);
}
