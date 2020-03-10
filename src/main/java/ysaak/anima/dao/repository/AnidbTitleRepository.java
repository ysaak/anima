package ysaak.anima.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ysaak.anima.data.importer.anidb.AnidbTitle;

@Repository
public interface AnidbTitleRepository extends PagingAndSortingRepository<AnidbTitle, String> {

    @Modifying
    @Query(value = "TRUNCATE TABLE ANIDB_TITLE", nativeQuery = true)
    void truncate();

    @Query(
            value = "SELECT * FROM ANIDB_TITLE at WHERE at.ADTI_TYPE = '1' AND LOWER(at.ADTI_TITLE) LIKE :title",
            countQuery = "SELECT COUNT(1) FROM ANIDB_TITLE WHERE ADTI_TYPE = '1' AND LOWER(ADTI_TITLE) LIKE :title",
            nativeQuery = true
    )
    Page<AnidbTitle> searchByPrimaryTitle(String title, Pageable pageable);
}
