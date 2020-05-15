package ysaak.anima.view.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ysaak.anima.data.playlist.PlaylistItemStatus;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.service.PlaylistService;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.utils.AuthenticationHolder;
import ysaak.anima.view.dto.playlist.AddToPlaylistDto;
import ysaak.anima.view.dto.playlist.PlaylistMarkAsDto;
import ysaak.anima.view.dto.playlist.PlaylistProgressDto;
import ysaak.anima.view.router.RoutingService;

@Controller
@RequestMapping("/playlist")
@Transactional
public class PlaylistController extends AbstractViewController {
    private static final String ROUTE_ADD_ITEM = "playlist.item.add";
    private static final String ROUTE_PROGRESS_ITEM = "playlist.item.progress";
    private static final String ROUTE_MARK_AS = "playlist.item.mark-as";

    private final PlaylistService playlistService;

    @Autowired
    public PlaylistController(TranslationService translationService, RoutingService routingService, PlaylistService playlistService) {
        super(translationService, routingService);
        this.playlistService = playlistService;
    }

    @PostMapping(path = "/add", name = ROUTE_ADD_ITEM)
    public String addToPlaylistAction(@ModelAttribute AddToPlaylistDto addDto, final RedirectAttributes redirectAttributes) throws FunctionalException {
        final String userId = AuthenticationHolder.getAuthenticatedUserId()
            .orElseThrow(this::notFound);

        playlistService.addToPlaylist(userId, addDto.getElementId(), false);

        return "redirect:" + addDto.getRedirectUrl();
    }

    @PostMapping(path = "/progress", name = ROUTE_PROGRESS_ITEM)
    public String playlistProgressAction(@ModelAttribute PlaylistProgressDto progressDto, final RedirectAttributes redirectAttributes) throws FunctionalException {
        final String userId = AuthenticationHolder.getAuthenticatedUserId()
            .orElseThrow(this::notFound);

        playlistService.itemProgress(userId, progressDto.getElementId());

        return "redirect:" + progressDto.getRedirectUrl();
    }

    @PostMapping(path = "/markAs", name = ROUTE_MARK_AS)
    public String markAsAction(@ModelAttribute PlaylistMarkAsDto markAsDto, final RedirectAttributes redirectAttributes) throws FunctionalException {
        final String userId = AuthenticationHolder.getAuthenticatedUserId()
            .orElseThrow(this::notFound);

        final PlaylistItemStatus nextStatus;

        try {
            nextStatus = PlaylistItemStatus.valueOf(markAsDto.getStatus());
        }
        catch (IllegalArgumentException e) {
            throw this.badRequest("Invalid status");
        }

        playlistService.markAs(userId, markAsDto.getElementId(), nextStatus);

        return "redirect:" + markAsDto.getRedirectUrl();
    }
}
