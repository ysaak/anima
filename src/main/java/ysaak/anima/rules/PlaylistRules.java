package ysaak.anima.rules;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import ysaak.anima.data.Element;
import ysaak.anima.data.playlist.PlaylistItem;
import ysaak.anima.data.playlist.PlaylistItemStatus;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.exception.error.PlaylistErrorCode;
import ysaak.anima.utils.Validate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class PlaylistRules {
    private PlaylistRules() { /**/ }

    public static final List<PlaylistItemStatus> FINAL_STATUS_LIST = Arrays.asList(
        PlaylistItemStatus.FINISHED,
        PlaylistItemStatus.DROPPED
    );

    public static final Multimap<PlaylistItemStatus, PlaylistItemStatus> ALLOWED_NEXT_STATUS = ImmutableListMultimap.<PlaylistItemStatus, PlaylistItemStatus>builder()
        .put(PlaylistItemStatus.PLAN_TO_WATCH, PlaylistItemStatus.STARTED)
        .put(PlaylistItemStatus.PLAN_TO_WATCH, PlaylistItemStatus.DROPPED)
        .put(PlaylistItemStatus.STARTED, PlaylistItemStatus.FINISHED)
        .put(PlaylistItemStatus.STARTED, PlaylistItemStatus.DROPPED)
        .build();

    public static boolean isItemInProgress(PlaylistItem playlistItem) {
        Preconditions.checkNotNull(playlistItem, "playlistItem is null");
        return !FINAL_STATUS_LIST.contains(playlistItem.getStatus());
    }

    /**
     * Determine if the element can have a progress in playlist
     * @param element Element to analyse
     * @return true if the element support progress - false otherwise
     */
    public static boolean hasProgressManagement(Element element) {
        return element.getType().hasPlaylistProgress();
    }

    /**
     * Check if the next status is allowed
     * @param currentStatus Current status
     * @param nextStatus Next status
     * @throws FunctionalException If the next status is not allowed
     */
    public static void canChangeStatus(PlaylistItemStatus currentStatus, PlaylistItemStatus nextStatus) throws FunctionalException {
        final Collection<PlaylistItemStatus> allowedNextStatusList = ALLOWED_NEXT_STATUS.get(currentStatus);
        Validate.isTrue(allowedNextStatusList.contains(nextStatus), PlaylistErrorCode.CANNOT_CHANGE_STATUS, currentStatus, nextStatus);
    }

    public static PlaylistItem markAsStarted(PlaylistItem item) {
        item.setStatus(PlaylistItemStatus.STARTED);
        item.setStartDate(LocalDate.now());

        if (PlaylistRules.hasProgressManagement(item.getElement())) {
            item.setCurrentEpisode(1);
        }

        return item;
    }

    public static PlaylistItem markAsDropped(PlaylistItem item) {
        item.setStatus(PlaylistItemStatus.DROPPED);
        item.setEndDate(LocalDate.now());
        return item;
    }

    public static PlaylistItem markAsFinished(PlaylistItem item) {
        item.setStatus(PlaylistItemStatus.FINISHED);
        item.setEndDate(LocalDate.now());
        return item;
    }
}
