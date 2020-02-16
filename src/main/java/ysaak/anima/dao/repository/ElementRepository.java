package ysaak.anima.dao.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ysaak.anima.data.Element;

@Repository
public interface ElementRepository extends PagingAndSortingRepository<Element, String> {
}
