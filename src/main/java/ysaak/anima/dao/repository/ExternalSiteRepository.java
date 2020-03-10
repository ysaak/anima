package ysaak.anima.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ysaak.anima.data.ElementRemoteId;
import ysaak.anima.data.ExternalSite;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExternalSiteRepository extends PagingAndSortingRepository<ExternalSite, String> {
    Optional<ExternalSite> findByCode(String code);

    @Query("SELECT eri FROM ElementRemoteId eri WHERE eri.externalSite.id = :siteId AND eri.remoteId IN (:remoteIdList)")
    List<ElementRemoteId> findElementForSite(@Param("siteId") String siteId, @Param("remoteIdList") List<String> remoteIdList);
}
