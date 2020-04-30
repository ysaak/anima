package ysaak.anima.view.controller.admin;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ysaak.anima.data.Tag;
import ysaak.anima.data.importer.Importer;
import ysaak.anima.data.importer.TagEquivalence;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.service.ExternalSiteService;
import ysaak.anima.service.TagService;
import ysaak.anima.service.importer.TagEquivalenceService;
import ysaak.anima.service.importer.anidb.AnidbConstants;
import ysaak.anima.service.importer.anidb.AnidbImporterService;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.utils.StringUtils;
import ysaak.anima.view.controller.AbstractViewController;
import ysaak.anima.view.dto.KeyValueItem;
import ysaak.anima.view.dto.admin.importer.TagEquivalenceDto;
import ysaak.anima.view.dto.admin.importer.TagEquivalenceEditDto;
import ysaak.anima.view.router.RoutingService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/importer/anidb")
@Transactional
public class AdminAnidbImporterController extends AbstractViewController {
    private static final String ROUTE_INDEX = "admin.importer.anidb.index";
    private static final String ROUTE_TITLES_RELOAD = "admin.importer.anidb.reload-titles";
    private static final String ROUTE_EQUIVALENCE_EDIT = "admin.importer.anidb.equivalence.edit";
    private static final String ROUTE_EQUIVALENCE_UPDATE = "admin.importer.anidb.equivalence.update";

    private final AnidbImporterService anidbImporterService;
    private final ExternalSiteService externalSiteService;
    private final TagService tagService;
    private final TagEquivalenceService tagEquivalenceService;

    public AdminAnidbImporterController(AnidbImporterService anidbImporterService, TranslationService translationService, RoutingService routingService, ExternalSiteService externalSiteService, TagService tagService, TagEquivalenceService tagEquivalenceService) {
        super(translationService, routingService);
        this.anidbImporterService = anidbImporterService;
        this.externalSiteService = externalSiteService;
        this.tagService = tagService;
        this.tagEquivalenceService = tagEquivalenceService;
    }

    @GetMapping(path = "/", name = ROUTE_INDEX)
    public String indexAction(ModelMap model) {
        model.put("title_count", anidbImporterService.getAvailableTitleCount());
        model.put("element_count", externalSiteService.countElementBySite(AnidbConstants.ANIDB_SITE_CODE));

        final List<TagEquivalence> equivalenceList = tagEquivalenceService.findByImporter(Importer.ANIDB);
        final List<String> tagIdList = equivalenceList.stream().map(TagEquivalence::getTagId).collect(Collectors.toList());

        List<Tag> tagList = tagService.findById(tagIdList);
        List<TagEquivalenceDto> equivalenceDtoList = mapEquivalenceToDto(equivalenceList, tagList);
        model.put("equivalenceList", equivalenceDtoList);


        return "admin/anidb/index";
    }

    @PostMapping(path = "/reload-titles", name = ROUTE_TITLES_RELOAD)
    public String reloadTitlesAction(final RedirectAttributes redirectAttributes) {
        try {
            anidbImporterService.refreshTitleList();
            this.addFlashInfoMessage(redirectAttributes, translationService.get("importer.anidb.title-list-reloaded.success"));
        }
        catch (FunctionalException e) {
            this.addFlashErrorMessage(redirectAttributes, translationService.get("importer.anidb.title-list-reloaded.error"));
        }

        return redirect(ROUTE_INDEX);
    }

    @GetMapping(path = "/equivalence", name = ROUTE_EQUIVALENCE_EDIT)
    public String editEquivalenceAction(final ModelMap model) {
        List<Tag> tagList = tagService.findAll();
        final List<KeyValueItem> tagDtoList = tagList.stream()
                .map(t -> new KeyValueItem(t.getId(), t.getName()))
                .sorted(Comparator.comparing(KeyValueItem::getValue))
                .collect(Collectors.toList());
        model.put("tagList", tagDtoList);

        List<TagEquivalence> equivalenceList = tagEquivalenceService.findByImporter(Importer.ANIDB);

        List<TagEquivalenceDto> equivalenceDtoList = mapEquivalenceToDto(equivalenceList, tagList);
        model.put("equivalenceList", equivalenceDtoList);

        return "admin/anidb/edit_tags";
    }

    private List<TagEquivalenceDto> mapEquivalenceToDto(final List<TagEquivalence> equivalenceList, final List<Tag> tagList) {
        final Map<String, String> tagMap = tagList.stream().collect(Collectors.toMap(Tag::getId, Tag::getName));
        final Multimap<String, String> dataMap = ArrayListMultimap.create();

        equivalenceList.forEach(e -> dataMap.put(e.getTagId(), e.getEquivalence()));

        return dataMap.keySet().stream()
                .map(tagId -> new TagEquivalenceDto(
                        tagId,
                        tagMap.get(tagId),
                        new ArrayList<>(dataMap.get(tagId))
                ))
                .sorted(Comparator.comparing(TagEquivalenceDto::getTagName))
                .collect(Collectors.toList());
    }

    @PostMapping(path = "/equivalence", name = ROUTE_EQUIVALENCE_UPDATE)
    public String updateEquivalenceAction(@ModelAttribute final TagEquivalenceEditDto editDto, final RedirectAttributes redirectAttributes) {
        Multimap<String, String> equivalenceMap = ArrayListMultimap.create();

        for (int i=0; i<editDto.getTagId().length; i++) {
            String tagId = editDto.getTagId()[i];
            if (StringUtils.isNotBlank(tagId)) {
                equivalenceMap.put(tagId, editDto.getEquivalence()[i]);
            }
        }

        tagEquivalenceService.save(Importer.ANIDB, equivalenceMap);

        return routingService.redirectUrl(ROUTE_INDEX);
    }
}
