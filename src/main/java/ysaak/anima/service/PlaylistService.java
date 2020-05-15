package ysaak.anima.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ysaak.anima.dao.repository.PlaylistItemRepository;
import ysaak.anima.data.Element;
import ysaak.anima.data.playlist.PlaylistItem;
import ysaak.anima.data.playlist.PlaylistItemStatus;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.exception.error.PlaylistErrorCode;
import ysaak.anima.rules.PlaylistRules;
import ysaak.anima.utils.Validate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {

    private final ElementService elementService;

    private final PlaylistItemRepository playlistItemRepository;

    @Autowired
    public PlaylistService(ElementService elementService, PlaylistItemRepository playlistItemRepository) {
        this.elementService = elementService;
        this.playlistItemRepository = playlistItemRepository;
    }

    public List<PlaylistItem> getPlaylistForUser(String userId) {
        return this.playlistItemRepository.findByUserIdList(userId);
    }

    public List<PlaylistItem> getPlaylistForUserByStatus(String userId, PlaylistItemStatus status) {
        return this.playlistItemRepository.findByUserIdListAndStatus(userId, status);
    }

    public Optional<PlaylistItem> getItem(String userId, String elementId) {
        List<PlaylistItem> itemList = playlistItemRepository.findByUserIdListAndElementId(userId, elementId);
        return itemList.stream().filter(PlaylistRules::isItemInProgress).max(Comparator.comparing(PlaylistItem::getLastUpdateTime));
    }

    public PlaylistItem addToPlaylist(String userId, String elementId, boolean planToWatch) throws FunctionalException {
        List<PlaylistItem> itemList = this.playlistItemRepository.findByUserIdListAndElementId(userId, elementId);

        boolean alreadyAdded = itemList.stream()
            .anyMatch(item -> !PlaylistRules.FINAL_STATUS_LIST.contains(item.getStatus()));

        Validate.isFalse(alreadyAdded, PlaylistErrorCode.ALREADY_IN_LIST);

        final Element element = elementService.findById2(elementId)
            .orElseThrow(() -> PlaylistErrorCode.ELEMENT_NOT_FOUND.functional(elementId));

        PlaylistItem item = new PlaylistItem();
        item.setUserIdList(Collections.singletonList(userId));
        item.setElement(element);
        item.setLastUpdateTime(LocalDateTime.now(ZoneOffset.UTC));

        if (planToWatch) {
            item.setStatus(PlaylistItemStatus.PLAN_TO_WATCH);
        }
        else {
            item.setStatus(PlaylistItemStatus.STARTED);
            item.setStartDate(LocalDate.now());

            if (PlaylistRules.hasProgressManagement(element)) {
                item.setCurrentEpisode(1);
            }
        }

        return playlistItemRepository.save(item);
    }

    public PlaylistItem itemProgress(String userId, String elementId) throws FunctionalException {
        final PlaylistItem item = getItem(userId, elementId)
            .orElseThrow(() -> PlaylistErrorCode.NOT_IN_PROGRESS.functional(elementId, userId));

        final Element element = elementService.findById2(elementId)
            .orElseThrow(() -> PlaylistErrorCode.NOT_IN_PROGRESS.functional(elementId));

        Validate.isTrue(PlaylistRules.hasProgressManagement(element), PlaylistErrorCode.NO_PROGRESS_FOR_ELEMENT, elementId);
        Validate.isTrue(item.getCurrentEpisode() != null, PlaylistErrorCode.NO_PROGRESS_FOR_ELEMENT, elementId);

        int nextEpisode = item.getCurrentEpisode() + 1;

        PlaylistItem updatedItem = item;

        if (element.getEpisodeCount() != null && nextEpisode > element.getEpisodeCount()) {
            updatedItem = PlaylistRules.markAsFinished(item);
        }
        else {
            updatedItem.setCurrentEpisode(nextEpisode);
        }

        updatedItem.setLastUpdateTime(LocalDateTime.now(ZoneOffset.UTC));

        return playlistItemRepository.save(updatedItem);
    }

    public void markAs(String userId, String elementId, PlaylistItemStatus nextStatus) throws FunctionalException {
        final PlaylistItem item = getItem(userId, elementId)
            .orElseThrow(() -> PlaylistErrorCode.NOT_IN_PROGRESS.functional(elementId, userId));

        PlaylistRules.canChangeStatus(item.getStatus(), nextStatus);

        final PlaylistItem updatedItem;
        switch (nextStatus) {
            case STARTED:
                updatedItem = PlaylistRules.markAsStarted(item);
                break;
            case FINISHED:
                updatedItem = PlaylistRules.markAsFinished(item);
                break;
            case DROPPED:
                updatedItem = PlaylistRules.markAsDropped(item);
                break;
            default:
                return;
        }

        updatedItem.setLastUpdateTime(LocalDateTime.now(ZoneOffset.UTC));

        playlistItemRepository.save(updatedItem);
    }
}
