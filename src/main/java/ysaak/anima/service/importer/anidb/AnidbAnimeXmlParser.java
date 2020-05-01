package ysaak.anima.service.importer.anidb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ysaak.anima.dao.model.TagModel;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementSubType;
import ysaak.anima.data.ElementType;
import ysaak.anima.data.Episode;
import ysaak.anima.data.Season;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.exception.error.AnidbErrorCode;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

final class AnidbAnimeXmlParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnidbAnimeXmlParser.class);

    private static final List<String> TITLE_LANG_PRIORITY = Arrays.asList("fr", "en", "x-jat", "jp");

    // TODO save as parameter
    private static final String DEFAULT_SEASON_NAME = "Season";

    private static final String TAG_ERROR = "error";
    private static final String TAG_TITLES = "titles";
    private static final String TAG_TYPE = "type";
    private static final String TAG_START_DATE = "startdate";
    private static final String TAG_SYNOPSIS = "description";
    private static final String TAG_EPISODES = "episodes";
    private static final String TAG_EPISODE = "episode";
    private static final String TAG_TAGS = "tags";
    private static final String TAG_TAG = "tag";
    private static final String TAG_TAG_NAME = "name";

    private static final String TAG_EPI_NUMBER = "epno";
    private static final String TAG_EPI_TITLE = "title";

    private static final String TITLE_TYPE_MAIN = "main";

    static Element parseDocument(final String xmlData, final Map<String, String> tagEquivalenceMap) throws FunctionalException {
        final Document document = createDocument(xmlData);

        final Element element = new Element();
        element.setType(ElementType.ANIME);

        final List<Episode> episodeList = new ArrayList<>();
        final List<String> tagList = new ArrayList<>();

        final org.w3c.dom.Element rootNode = document.getDocumentElement();

        if (TAG_ERROR.equals(rootNode.getNodeName())) {
            throw AnidbErrorCode.IMPORT_API_ERROR.functional(rootNode.getTextContent());
        }

        final NodeList nodeList = rootNode.getChildNodes();
        final int nodeCount = nodeList.getLength();

        for (int i = 0; i < nodeCount; i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = node.getNodeName();

                if (TAG_TITLES.equals(nodeName)) {
                    element.setTitle(extractMainTitle(node.getChildNodes()));
                }
                else if (TAG_TYPE.equals(nodeName)) {
                    element.setSubType(convertType(node.getTextContent()));
                }
                else if (TAG_START_DATE.equals(nodeName)) {
                    LocalDate startDate = parseDate(node.getTextContent());
                    element.setReleaseYear(startDate.getYear());
                }
                else if (TAG_SYNOPSIS.equals(nodeName)) {
                    element.setSynopsis(parseDescription(node.getTextContent()));
                }
                else if (TAG_EPISODES.equals(nodeName)) {
                    extractEpisodes(episodeList, ((org.w3c.dom.Element) node).getElementsByTagName(TAG_EPISODE));
                }
                else if (TAG_TAGS.equals(nodeName)) {
                    extractTags(tagList, ((org.w3c.dom.Element) node).getElementsByTagName(TAG_TAG));
                }
            }
        }

        Season season = new Season(1, DEFAULT_SEASON_NAME);
        season.setEpisodeList(episodeList);

        List<Season> seasonSet = new ArrayList<>();
        seasonSet.add(season);
        element.setSeasonList(seasonSet);

        List<TagModel> elementTagList = tagList.stream()
                .map(tag -> tagEquivalenceMap.getOrDefault(tag, null))
                .filter(Objects::nonNull)
                .map(TagModel::new)
                .collect(Collectors.toList());
        element.setTagList(elementTagList);

        return element;
    }

    private static Document createDocument(String data) throws FunctionalException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setExpandEntityReferences(false);

        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();

            final InputStream dataStream = new ByteArrayInputStream(data.getBytes());

            return builder.parse(dataStream);
        }
        catch (Exception e) {
            throw AnidbErrorCode.IMPORT_XML_READ.functional(e);
        }
    }

    private static String extractMainTitle(NodeList titleList) {
        final int titleCount = titleList.getLength();

        String title = null;

        for (int i = 0; i < titleCount; i++) {
            Node node = titleList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String titleType = ((org.w3c.dom.Element) node).getAttribute("type");
                if (TITLE_TYPE_MAIN.equals(titleType)) {
                    title = node.getTextContent();
                }

                if (title == null) {
                    title = node.getTextContent();
                }
            }
        }

        return title;
    }

    private static ElementSubType convertType(String data) {
        // TV Series
        return ElementSubType.TV;
    }

    private static LocalDate parseDate(String data) {
        return LocalDate.parse(data, DateTimeFormatter.ISO_DATE);
    }

    private static void extractEpisodes(final List<Episode> episodeList, final NodeList nodeList) {
        final int nodeCount = nodeList.getLength();

        for (int i = 0; i < nodeCount; i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                extractEpisode(node.getChildNodes()).ifPresent(episodeList::add);
            }
        }
    }

    private static Optional<Episode> extractEpisode(final NodeList nodeList) {
        final int nodeCount = nodeList.getLength();
        final Map<String, String> titleMap = new HashMap<>();
        String number = null;
        String title = null;

        for (int i = 0; i < nodeCount; i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                org.w3c.dom.Element e = (org.w3c.dom.Element) node;
                final String nodeName = node.getNodeName();

                if (TAG_EPI_NUMBER.equals(nodeName)) {
                    number = node.getTextContent();
                }
                else if (TAG_EPI_TITLE.equals(nodeName)) {
                    titleMap.put(
                            e.getAttribute("xml:lang"),
                            node.getTextContent()
                    );
                }
            }
        }

        if (!titleMap.isEmpty()) {
            for (String langCode : TITLE_LANG_PRIORITY) {
                title = titleMap.get(langCode);

                if (title != null) {
                    break;
                }
            }

            if (title == null) {
                // No title found from preferred languages. Use the first one
                String langCode = titleMap.keySet().iterator().next();
                title = titleMap.get(langCode);
            }
        }
        else {
            LOGGER.warn("No title found for episode {}", number);
            return Optional.empty();
        }

        return Optional.of(new Episode(number, title));
    }

    private static String parseDescription(final String description) {

        String alteredDescription = description;

        final String regex = "((https?:\\/\\/.+?) \\[(.+?)\\])";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        final Matcher matcher = pattern.matcher(description);

        while (matcher.find()) {
            alteredDescription = alteredDescription.replace(matcher.group(0), matcher.group(3));
        }

        return alteredDescription;
    }

    private static void extractTags(final List<String> tagList, final NodeList nodeList) {
        final int nodeCount = nodeList.getLength();

        for (int i = 0; i < nodeCount; i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                extractTag(node.getChildNodes()).ifPresent(tagList::add);
            }
        }
    }

    private static Optional<String> extractTag(final NodeList nodeList) {
        final int nodeCount = nodeList.getLength();
        String tag = null;

        for (int i = 0; i < nodeCount; i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                final String nodeName = node.getNodeName();

                if (TAG_TAG_NAME.equals(nodeName)) {
                    tag = node.getTextContent();
                }
            }
        }

        return Optional.ofNullable(tag);
    }
}
