package ysaak.anima.view.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ysaak.anima.data.User;
import ysaak.anima.data.playlist.PlaylistItem;
import ysaak.anima.data.playlist.PlaylistItemStatus;
import ysaak.anima.data.storage.StorageFormat;
import ysaak.anima.data.storage.StorageType;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.service.ElementService;
import ysaak.anima.service.PlaylistService;
import ysaak.anima.service.StorageService;
import ysaak.anima.service.UserService;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.view.dto.user.UserPlaylistItemDto;
import ysaak.anima.view.router.RoutingService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractViewController {
    public static final String ROUTE_USER_VIEW = "user.view";
    private static final String ROUTE_USER_IMAGE = "user.image";
    private static final String ROUTE_USER_IMAGE_EDIT = "user.image.edit";
    private static final String ROUTE_USER_IMAGE_UPDATE = "user.image.update";

    private final UserService userService;
    private final StorageService storageService;
    private final PlaylistService playlistService;
    private final ElementService elementService;

    @Autowired
    public UserController(TranslationService translationService, RoutingService routingService, UserService userService, StorageService storageService, PlaylistService playlistService, ElementService elementService) {
        super(translationService, routingService);
        this.userService = userService;
        this.storageService = storageService;
        this.playlistService = playlistService;
        this.elementService = elementService;
    }

    @GetMapping(path = "/{id}", name = ROUTE_USER_VIEW)
    public String viewAction(final ModelMap model, @PathVariable("id") final String id) {
        final User user = userService.findById(id).orElseThrow(this::notFound);
        model.put("user", user);


        List<PlaylistItem> watchingItemList = playlistService.getPlaylistForUserByStatus(id, PlaylistItemStatus.STARTED);
        List<UserPlaylistItemDto> watchingItemDtoList = watchingItemList.stream().map(item ->
            new UserPlaylistItemDto(
                item.getElement().getId(),
                item.getElement().getTitle(),
                item.getStatus().name(),
                item.getStartDate(),
                item.getCurrentEpisode()
            )
        ).collect(Collectors.toList());
        model.put("watchingList", watchingItemDtoList);

        return "users/view";
    }

    @GetMapping(path = "/{id}/{size}.png", name = ROUTE_USER_IMAGE)
    public ResponseEntity<Resource> viewAction(final ModelMap model, @PathVariable("id") final String id, @PathVariable("size") final String size) throws FunctionalException {
        final StorageFormat format = StorageFormat.valueOf(size.toUpperCase());

        final Resource file = storageService.getImage(StorageType.USER, format, id);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping(path = "/{id}/image/edit", name = ROUTE_USER_IMAGE_EDIT)
    public String editImageAction(final ModelMap model, @PathVariable("id") final String id) {
        model.put("userId", id);
        return "users/edit_image";
    }

    @PostMapping(path = "/{id}/image/", name = ROUTE_USER_IMAGE_UPDATE)
    public String updateImageAction(@PathVariable("id") final String id, @RequestParam("file") final MultipartFile file) throws FunctionalException {
        final User user = userService.findById(id).orElseThrow(this::notFound);

        storageService.store(StorageType.USER, user.getId(), file);

        return redirect(ROUTE_USER_VIEW, Collections.singletonMap("id", user.getId()));
    }
}
