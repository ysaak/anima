package ysaak.anima.dao.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ysaak.anima.data.ExternalSite;

import java.util.Optional;

@Repository
public interface ExternalSiteRepository extends PagingAndSortingRepository<ExternalSite, String> {
    Optional<ExternalSite> findByCode(String code);
}
