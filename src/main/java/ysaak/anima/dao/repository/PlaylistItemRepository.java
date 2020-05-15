package ysaak.anima.dao.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ysaak.anima.data.playlist.PlaylistItem;
import ysaak.anima.data.playlist.PlaylistItemStatus;

import java.util.List;

@Repository
public interface PlaylistItemRepository extends CrudRepository<PlaylistItem, String> {
    List<PlaylistItem> findByUserIdList(String userId);

    List<PlaylistItem> findByUserIdListAndStatus(String userId, PlaylistItemStatus status);

    List<PlaylistItem> findByUserIdListAndElementId(String userId, String elementId);
}
