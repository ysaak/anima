package ysaak.anima.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ysaak.anima.data.AnimeType;
import ysaak.anima.data.Anime;
import ysaak.anima.data.AnimeEpisode;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AnidbService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnidbService.class);

    private static final String ANIDB_BASE_URL = "https://anidb.net/anime/";

    private static final List<String> TITLE_LANG_PRIORITY = Arrays.asList("fr", "en", "x-jat", "jp");

    private static final String TAG_ERROR = "error";
    private static final String TAG_TITLES = "titles";
    private static final String TAG_TYPE = "type";
    private static final String TAG_START_DATE = "startdate";
    private static final String TAG_END_DATE = "enddate";
    private static final String TAG_SYNOPSIS = "description";
    private static final String TAG_EPISODES = "episodes";
    private static final String TAG_EPISODE = "episode";

    private static final String TAG_EPI_NUMBER = "epno";
    private static final String TAG_EPI_TITLE = "title";

    private static final String TITLE_TYPE_MAIN = "main";

    public String getUrl(String id) {
        return ANIDB_BASE_URL + id;
    }

    private String loadAnimeData() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<anime id=\"1\" restricted=\"false\">\n" +
                "   <type>TV Series</type>\n" +
                "   <episodecount>13</episodecount>\n" +
                "   <startdate>1999-01-04</startdate>\n" +
                "   <enddate>1999-03-29</enddate>\n" +
                "   <titles>\n" +
                "      <title xml:lang=\"x-jat\" type=\"main\">Seikai no Monshou</title>\n" +
                "      <title xml:lang=\"zh-Hans\" type=\"syn\">星界之纹章</title>\n" +
                "      <title xml:lang=\"en\" type=\"short\">CotS</title>\n" +
                "      <title xml:lang=\"x-jat\" type=\"short\">SnM</title>\n" +
                "      <title xml:lang=\"ja\" type=\"official\">星界の紋章</title>\n" +
                "      <title xml:lang=\"en\" type=\"official\">Crest of the Stars</title>\n" +
                "      <title xml:lang=\"fr\" type=\"official\">Crest of the Stars</title>\n" +
                "      <title xml:lang=\"pl\" type=\"official\">Crest of the Stars</title>\n" +
                "   </titles>\n" +
                "   <relatedanime>\n" +
                "      <anime id=\"4\" type=\"Sequel\">Seikai no Senki</anime>\n" +
                "      <anime id=\"6\" type=\"Prequel\">Seikai no Danshou</anime>\n" +
                "      <anime id=\"1623\" type=\"Summary\">Seikai no Monshou Tokubetsuhen</anime>\n" +
                "   </relatedanime>\n" +
                "   <url>http://www.sunrise-inc.co.jp/seikai/</url>\n" +
                "   <creators>\n" +
                "      <name id=\"4495\" type=\"Original Work\">Morioka Hiroyuki</name>\n" +
                "   </creators>\n" +
                "   <description>Jinto Lin`s life changes forever when the Humankind Empire Abh takes over his home planet of Martine without firing a single shot. He is soon sent off to study the Abh language and culture and to prepare himself for his future as a nobleman — a future he never dreamed of. Or wanted.Now, Jinto is entering the next phase of his training, and he is about to meet his first Abh, the lovely Lafiel. But Jinto is about to learn that she is more than she appears to be. And together they will have to fight for their very lives.</description>\n" +
                "   <ratings>\n" +
                "      <permanent count=\"2953\">8.53</permanent>\n" +
                "      <temporary count=\"96\">7.58</temporary>\n" +
                "      <review count=\"11\">8.75</review>\n" +
                "   </ratings>\n" +
                "   <picture>440.jpg</picture>\n" +
                "   <categories>\n" +
                "      <category id=\"174\" parentid=\"8\" hentai=\"false\" weight=\"500\">\n" +
                "         <name>Space Travel</name>\n" +
                "         <description>Space Travel usually refers going to space or travelling there with or without a spacecraft. There are different forms of space travel like Interplanetary travel, Interstellar travel or Intergalactic travel. Since it isn`t possible to travel faster than light, most anime series involve some sort \"sub-space travel mechanism\" as a way to travel faster than light.</description>\n" +
                "      </category>\n" +
                "      <category id=\"274\" parentid=\"268\" hentai=\"false\" weight=\"400\">\n" +
                "         <name>Novel</name>\n" +
                "         <description>Note that novel means book with words in paper, so no comics and no \"visual novels\", which should go under manga and dating-sim - visual Novel/erotic game respectively.&#13;\n" +
                "&#13;\n" +
                "Note that it can be applied to novels as we know them but also the typical light novels for teens and young adults, which are common in Japan and aside from being short include a number of illustrations. </description>\n" +
                "      </category>\n" +
                "   </categories>\n" +
                "   <tags>\n" +
                "      <tag id=\"136\" approval=\"20\" spoiler=\"false\" update=\"2009-05-23\">\n" +
                "         <name>space opera</name>\n" +
                "         <count>71</count>\n" +
                "      </tag>\n" +
                "      <tag id=\"175\" approval=\"17\" spoiler=\"false\" update=\"2008-03-13\">\n" +
                "         <name>space elves</name>\n" +
                "         <count>12</count>\n" +
                "      </tag>\n" +
                "      <tag id=\"389\" approval=\"15\" spoiler=\"false\" update=\"2008-06-03\">\n" +
                "         <name>war setting</name>\n" +
                "         <count>64</count>\n" +
                "      </tag>\n" +
                "      <tag id=\"393\" approval=\"18\" spoiler=\"false\" update=\"2008-06-03\">\n" +
                "         <name>space battles</name>\n" +
                "         <count>43</count>\n" +
                "         <description>There are space battles in this anime. Usually between Earth forces and some alien invader, but ship to ship skirmishes in deep space are just as common.</description>\n" +
                "      </tag>\n" +
                "   </tags>\n" +
                "   <characters>\n" +
                "      <character id=\"7493\" type=\"appears in\" update=\"2009-05-28\">\n" +
                "         <name>Gosroth</name>\n" +
                "         <description>The Resii Gothroth.The first Abh ship lost in the war.</description>\n" +
                "         <episodes>1,1012</episodes>\n" +
                "         <picture>26147.jpg</picture>\n" +
                "      </character>\n" +
                "      <character id=\"7501\" type=\"appears in\" update=\"2009-05-28\">\n" +
                "         <name>Planet Martine</name>\n" +
                "         <description>The homeworld of Jinto</description>\n" +
                "         <episodes>1</episodes>\n" +
                "      </character>\n" +
                "      <character id=\"7503\" type=\"appears in\" update=\"2009-05-28\">\n" +
                "         <name>Rock Lynn</name>\n" +
                "         <description>The former president of Martine, later made count of the Hyde system, after striking a deal with the Abh (in episode 1)</description>\n" +
                "         <episodes>1</episodes>\n" +
                "         <picture>26165.jpg</picture>\n" +
                "      </character>\n" +
                "   </characters>\n" +
                "   <episodes>\n" +
                "      <episode id=\"1\" update=\"2005-03-10\">\n" +
                "         <epno>1</epno>\n" +
                "         <length>24</length>\n" +
                "         <airdate>1999-01-03</airdate>\n" +
                "         <rating votes=\"17\">5.66</rating>\n" +
                "         <title xml:lang=\"ja\">侵略</title>\n" +
                "         <title xml:lang=\"en\">Invasion</title>\n" +
                "         <title xml:lang=\"fr\">Invasion</title>\n" +
                "         <title xml:lang=\"x-jat\">shinryaku</title>\n" +
                "      </episode>\n" +
                "      <episode id=\"2\" update=\"2005-03-11\">\n" +
                "         <epno>2</epno>\n" +
                "         <length>24</length>\n" +
                "         <airdate>1999-01-10</airdate>\n" +
                "         <rating votes=\"12\">6.66</rating>\n" +
                "         <title xml:lang=\"ja\">星たちの眷族</title>\n" +
                "         <title xml:lang=\"en\">Kin of the Stars</title>\n" +
                "         <title xml:lang=\"fr\">Les parents des Étoiles</title>\n" +
                "         <title xml:lang=\"x-jat\">Hoshi-tachi no Kenzoku</title>\n" +
                "      </episode>\n" +
                "   </episodes>\n" +
                "</anime>";
    }

    public Anime importAnime(String id) throws Exception {
        String animeData = loadAnimeData();

        final Anime anime = new Anime();

        final Document document = createDocument(animeData);
        final Element rootNode = document.getDocumentElement();

        if (TAG_ERROR.equals(rootNode.getNodeName())) {
            throw new Exception("Error ! " + rootNode.getTextContent());
        }

        final NodeList nodeList = rootNode.getChildNodes();
        final int nodeCount = nodeList.getLength();

        for (int i = 0; i < nodeCount; i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = node.getNodeName();

                if (TAG_TITLES.equals(nodeName)) {
                    anime.setTitle(extractMainTitle(node.getChildNodes()));
                }
                else if (TAG_TYPE.equals(nodeName)) {
                    anime.setType(convertType(node.getTextContent()));
                }
                else if (TAG_START_DATE.equals(nodeName)) {
                    anime.setStartDate(parseDate(node.getTextContent()));
                }
                else if (TAG_END_DATE.equals(nodeName)) {
                    anime.setEndDate(parseDate(node.getTextContent()));
                }
                else if (TAG_SYNOPSIS.equals(nodeName)) {
                    anime.setSynopsis(node.getTextContent());
                }
                else if (TAG_EPISODES.equals(nodeName)) {
                    anime.setEpisodeList(extractEpisodeList(((Element) node).getElementsByTagName(TAG_EPISODE)));
                }
            }
        }

        anime.setAnidbId(id);

        return anime;
    }

    private static Document createDocument(String data) throws Exception {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setExpandEntityReferences(false);

        final DocumentBuilder builder = factory.newDocumentBuilder();
        final InputStream dataStream = new ByteArrayInputStream(data.getBytes());
        return builder.parse(dataStream);
    }

    private static String extractMainTitle(NodeList titleList) {
        final int titleCount = titleList.getLength();

        String title = null;

        for (int i = 0; i < titleCount; i++) {
            Node node = titleList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String titleType = ((Element) node).getAttribute("type");
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

    private static AnimeType convertType(String data) {
        // TV Series
        return AnimeType.TV;
    }

    private static LocalDate parseDate(String data) {
        return LocalDate.parse(data, DateTimeFormatter.ISO_DATE);
    }

    private static List<AnimeEpisode> extractEpisodeList(final NodeList nodeList) {
        final List<AnimeEpisode> episodeList = new ArrayList<>();
        final int nodeCount = nodeList.getLength();

        for (int i = 0; i < nodeCount; i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                extractEpisode(node.getChildNodes()).ifPresent(episodeList::add);
            }
        }

        return episodeList;
    }

    private static Optional<AnimeEpisode> extractEpisode(final NodeList nodeList) {
        final int nodeCount = nodeList.getLength();
        final Map<String, String> titleMap = new HashMap<>();
        String number = null;
        String title = null;

        for (int i = 0; i < nodeCount; i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) node;
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

        return Optional.of(new AnimeEpisode(number, title));
    }
}
