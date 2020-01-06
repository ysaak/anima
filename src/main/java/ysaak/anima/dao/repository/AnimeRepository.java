package ysaak.anima.dao.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ysaak.anima.dao.model.AnimeModel;

import java.util.Optional;

@Repository
public interface AnimeRepository extends PagingAndSortingRepository<AnimeModel, String> {
    Optional<AnimeModel> findByAnidbId(String anidbId);
}
