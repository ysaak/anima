package ysaak.anima.service;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ysaak.anima.IAnimaComponent;
import ysaak.anima.config.ElementConstants;
import ysaak.anima.dao.repository.ElementRepository;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementRemoteId;
import ysaak.anima.data.ElementType;
import ysaak.anima.data.Episode;
import ysaak.anima.data.ExternalSite;
import ysaak.anima.data.Season;
import ysaak.anima.exception.DataValidationException;
import ysaak.anima.exception.NoDataFoundException;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.service.validation.ValidationMessages;
import ysaak.anima.utils.CollectionUtils;
import ysaak.anima.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class ElementService implements IAnimaComponent {

    private final ExternalSiteService externalSiteService;
    private final TranslationService translationService;

    private final ElementRepository elementRepository;

    @Autowired
    public ElementService(ExternalSiteService externalSiteService, TranslationService translationService, ElementRepository elementRepository) {
        this.externalSiteService = externalSiteService;
        this.translationService = translationService;
        this.elementRepository = elementRepository;
    }

    public Element create(final Element data) throws DataValidationException {
        validate(data);

        if (CollectionUtils.isNotEmpty(data.getSeasonList())) {
            data.getSeasonList().forEach(s -> {
                s.setElement(data);

                if (CollectionUtils.isNotEmpty(s.getEpisodeList())) {
                    s.getEpisodeList().forEach(e -> e.setSeason(s));
                }
            });
        }

        return elementRepository.save(data);
    }

    public Element update(final Element data) throws NoDataFoundException, DataValidationException {
        Element storedElement = findById(data.getId());
        validate(data);

        data.setSeasonList(storedElement.getSeasonList());

        return elementRepository.save(data);
    }

    public List<String> listUsedLetters() {
        return elementRepository.listUsedLetters();
    }

    public List<Element> findByTypeAndLetter(final ElementType type, final String firstLetter) {
        if (ElementConstants.NON_ALPHA_LETTER.equals(firstLetter)) {
            return elementRepository.findAllByTypeAndFirstLetterNonAlpha(type);
        }
        else {
            return elementRepository.findAllByTypeAndFistLetterAlpha(type, firstLetter);
        }
    }

    public Element findById(String id) throws NoDataFoundException {
        return elementRepository.findById(id)
                .orElseThrow(() -> new NoDataFoundException("No element found for id " + id));
    }

    public List<Element> searchByTitle(String title) {
        return elementRepository.findByTitleContainingIgnoreCaseOrderByTitle(title);
    }

    private <T> void validate(T data) throws DataValidationException {
        Preconditions.checkNotNull(data);
        validator().validate(data);
    }

    public void delete(final String elementId) {
        Preconditions.checkNotNull(elementId, "elementId is null");
        elementRepository.deleteById(elementId);
    }

    /* ----- Season management ----- */

    public Element addSeason(String elementId, String title) throws NoDataFoundException, DataValidationException {
        Preconditions.checkNotNull(elementId, "elementId is null");

        final Element element = findById(elementId);

        List<Season> seasonSet = element.getSeasonList();

        int seasonNumber = 1 + seasonSet.stream().mapToInt(Season::getNumber).max().orElse(0);

        Season season = new Season(seasonNumber, title);
        validate(season);

        season.setElement(element);
        seasonSet.add(season);

        return elementRepository.save(element);
    }

    public Element updateSeasonTitle(String elementId, String seasonId, String seasonTitle) throws NoDataFoundException {
        Preconditions.checkNotNull(elementId, "elementId is null");
        Preconditions.checkNotNull(elementId, "seasonId is null");
        Preconditions.checkNotNull(elementId, "seasonTitle is null");

        final Element element = findById(elementId);

        for (Season storedSeason : element.getSeasonList()) {
            if (storedSeason.getId().equals(seasonId)) {
                storedSeason.setTitle(seasonTitle);
                break;
            }
        }

        return elementRepository.save(element);
    }

    public Element deleteSeason(String elementId, String seasonId) throws NoDataFoundException {
        Preconditions.checkNotNull(elementId, "elementId is null");
        Preconditions.checkNotNull(seasonId, "seasonId is null");

        final Element element = findById(elementId);

        for (Season season : new ArrayList<>(element.getSeasonList())) {
            if (seasonId.equals(season.getId())) {
                element.getSeasonList().remove(season);
                break;
            }
        }

        return elementRepository.save(element);
    }

    /* ----- Episode management ----- */

    public Element addEpisode(String elementId, String seasonId, Episode episode) throws NoDataFoundException, DataValidationException {
        Preconditions.checkNotNull(elementId, "elementId is null");
        Preconditions.checkNotNull(seasonId, "seasonId is null");
        Preconditions.checkNotNull(episode, "episode is null");

        Element element = findById(elementId);

        Season season = CollectionUtils.getNotNull(element.getSeasonList())
                .stream()
                .filter(s -> s.getId().equals(seasonId))
                .findAny()
                .orElseThrow(() -> new DataValidationException("seasonId", "seasonId '" + seasonId + "' is not found for element '" + elementId + "'"));

        validate(episode);

        episode.setSeason(season);
        season.getEpisodeList().add(episode);

        return elementRepository.save(element);
    }

    public Element addEpisode(String elementId, String seasonId, List<Episode> episodeList) throws NoDataFoundException, DataValidationException {
        Preconditions.checkNotNull(elementId, "elementId is null");
        Preconditions.checkNotNull(seasonId, "seasonId is null");
        Preconditions.checkNotNull(episodeList, "episodeList is null");

        Element element = findById(elementId);

        Season season = CollectionUtils.getNotNull(element.getSeasonList())
                .stream()
                .filter(s -> s.getId().equals(seasonId))
                .findAny()
                .orElseThrow(() -> new DataValidationException("seasonId", "seasonId '" + seasonId + "' is not found for element '" + elementId + "'"));

        for (Episode episode : episodeList) {
            episode.setSeason(season);
            season.getEpisodeList().add(episode);
        }

        return elementRepository.save(element);
    }

    public Element updateEpisode(String elementId, String seasonId, Episode episode) throws NoDataFoundException, DataValidationException {
        Preconditions.checkNotNull(elementId, "elementId is null");
        Preconditions.checkNotNull(seasonId, "seasonId is null");
        Preconditions.checkNotNull(episode, "episode is null");

        Element element = findById(elementId);

        final Episode storedEpisode = element.getSeasonList()
                .stream()
                .map(Season::getEpisodeList)
                .flatMap(Collection::stream)
                .filter(e -> e.getId().equals(episode.getId()))
                .findFirst()
                .orElseThrow(() -> new NoDataFoundException("No episode found with id " + episode.getId() + " in element " + elementId));

        storedEpisode.setNumber(episode.getNumber());
        storedEpisode.setTitle(episode.getTitle());

        validate(storedEpisode);

        if (!storedEpisode.getSeason().getId().equals(seasonId)) {
            for (Season season : element.getSeasonList()) {
                if (seasonId.equals(season.getId())) {
                    storedEpisode.setSeason(season);
                    season.getEpisodeList().add(storedEpisode);
                }
            }
        }

        return elementRepository.save(element);
    }

    public Element deleteEpisode(String elementId, String episodeId) throws NoDataFoundException {
        Preconditions.checkNotNull(elementId, "elementId is null");
        Preconditions.checkNotNull(episodeId, "episodeId is null");

        final Element element = findById(elementId);

        boolean found = false;

        for (Season season : element.getSeasonList()) {
            for (Episode episode : new ArrayList<>(season.getEpisodeList())) {
                if (episodeId.equals(episode.getId())) {
                    season.getEpisodeList().remove(episode);
                    found = true;
                    break;
                }
            }

            if (found) {
                break;
            }
        }

        return elementRepository.save(element);
    }

    /* ----- Remote id management ----- */

    public void addRemoteId(String elementId, String externalSiteId, String remoteId) throws NoDataFoundException, DataValidationException {
        Preconditions.checkNotNull(elementId, "elementId is null");
        //Preconditions.checkNotNull(externalSiteId, "externalSiteId is null");
        Preconditions.checkNotNull(remoteId, "remoteId is null");

        final Element element = findById(elementId);
        final ExternalSite externalSite;

        if (StringUtils.isNotEmpty(externalSiteId)) {
            try {
                externalSite = externalSiteService.findById(externalSiteId);
            }
            catch (NoDataFoundException e) {
                throw new DataValidationException(Collections.singletonMap(
                        "externalSiteId", translationService.get("elements.remote-id.error.external-site-not-found")
                ));
            }
        }
        else {
            throw new DataValidationException(Collections.singletonMap(
                    "externalSiteId", translationService.get(ValidationMessages.NOT_EMPTY_KEY)
            ));
        }

        final ElementRemoteId elementRemoteId = new ElementRemoteId(
                element,
                externalSite,
                remoteId
        );

        // TODO Uniqueness check

        element.getRemoteIdList().add(elementRemoteId);

        elementRepository.save(element);
    }

    public void deleteRemoteId(String elementId, String remoteId) throws NoDataFoundException {
        Preconditions.checkNotNull(elementId, "elementId is null");
        Preconditions.checkNotNull(remoteId, "remoteId is null");

        final Element element = findById(elementId);

        ElementRemoteId idToRemove = null;
        for (ElementRemoteId storedId : element.getRemoteIdList()) {
            if (remoteId.equals(storedId.getId())) {
                idToRemove = storedId;
                break;
            }
        }

        element.getRemoteIdList().remove(idToRemove);
        elementRepository.save(element);
    }
}
