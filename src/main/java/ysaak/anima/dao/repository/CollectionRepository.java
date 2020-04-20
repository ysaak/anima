package ysaak.anima.dao.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ysaak.anima.data.Collection;

@Repository
public interface CollectionRepository extends PagingAndSortingRepository<Collection, String> {
}
