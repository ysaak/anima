package ysaak.anima.service;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ysaak.anima.IAnimaComponent;
import ysaak.anima.dao.repository.ElementRepository;
import ysaak.anima.data.Element;
import ysaak.anima.data.Episode;
import ysaak.anima.data.Season;
import ysaak.anima.exception.DataValidationException;
import ysaak.anima.exception.NoDataFoundException;
import ysaak.anima.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class ElementService implements IAnimaComponent {

    private final ElementRepository elementRepository;

    @Autowired
    public ElementService(ElementRepository elementRepository) {
        this.elementRepository = elementRepository;
    }

    public Element save(final Element data) throws DataValidationException {
        validate(data);

        if (CollectionUtils.isNotEmpty(data.getSeasonSet())) {
            data.getSeasonSet().forEach(s -> {
                s.setElement(data);

                if (CollectionUtils.isNotEmpty(s.getEpisodeSet())) {
                    s.getEpisodeSet().forEach(e -> e.setSeason(s));
                }
            });
        }

        return elementRepository.save(data);
    }

    public Element findById(String id) throws NoDataFoundException {
        return elementRepository.findById(id)
                .orElseThrow(() -> new NoDataFoundException("No element found for id " + id));
    }

    public List<Element> findAll() {
        Iterable<Element> entityIterable = elementRepository.findAll();
        return CollectionUtils.toList(entityIterable);
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

        Set<Season> seasonSet = element.getSeasonSet();

        int seasonNumber = seasonSet.stream().mapToInt(Season::getNumber).max().orElse(1);

        Season season = new Season(seasonNumber, title);
        validate(season);

        season.setElement(element);
        seasonSet.add(season);

        return elementRepository.save(element);
    }

    public Element deleteSeason(String elementId, String seasonId) throws NoDataFoundException {
        Preconditions.checkNotNull(elementId, "elementId is null");
        Preconditions.checkNotNull(seasonId, "seasonId is null");

        final Element element = findById(elementId);

        for (Season season : new ArrayList<>(element.getSeasonSet())) {
            if (seasonId.equals(season.getId())) {
                element.getSeasonSet().remove(season);
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

        Season season = CollectionUtils.getNotNull(element.getSeasonSet())
                .stream()
                .filter(s -> s.getId().equals(seasonId))
                .findAny()
                .orElseThrow(() -> new DataValidationException("seasonId '" + seasonId + "' is not found for element '" + elementId + "'"));

        validate(episode);

        episode.setSeason(season);
        season.getEpisodeSet().add(episode);

        return elementRepository.save(element);
    }

    public Element updateEpisode(String elementId, String seasonId, Episode episode) throws NoDataFoundException, DataValidationException {
        Preconditions.checkNotNull(elementId, "elementId is null");
        Preconditions.checkNotNull(seasonId, "seasonId is null");
        Preconditions.checkNotNull(episode, "episode is null");

        Element element = findById(elementId);

        final Episode storedEpisode = element.getSeasonSet()
                .stream()
                .map(Season::getEpisodeSet)
                .flatMap(Collection::stream)
                .filter(e -> e.getId().equals(episode.getId()))
                .findFirst()
                .orElseThrow(() -> new NoDataFoundException("No episode found with id " + episode.getId() + " in element " + elementId));

        storedEpisode.setNumber(episode.getNumber());
        storedEpisode.setTitle(episode.getTitle());

        validate(storedEpisode);

        if (!storedEpisode.getSeason().getId().equals(seasonId)) {
            final String oldSeason = storedEpisode.getSeason().getId();

            for (Season season : element.getSeasonSet()) {

                if (seasonId.equals(season.getId())) {
                    storedEpisode.setSeason(season);
                    season.getEpisodeSet().add(storedEpisode);
                }

                if (oldSeason.equals(season.getId())) {
                    season.getEpisodeSet().remove(storedEpisode);
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

        for (Season season : element.getSeasonSet()) {
            for (Episode episode : new ArrayList<>(season.getEpisodeSet())) {
                if (episodeId.equals(episode.getId())) {
                    season.getEpisodeSet().remove(episode);
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
}
