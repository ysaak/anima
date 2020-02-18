package ysaak.anima.service.importer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementSubType;
import ysaak.anima.data.ElementType;
import ysaak.anima.data.Episode;
import ysaak.anima.data.Season;
import ysaak.anima.service.ElementService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class AnidbImportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnidbImportService.class);

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

    private static final String TAG_EPI_NUMBER = "epno";
    private static final String TAG_EPI_TITLE = "title";

    private static final String TITLE_TYPE_MAIN = "main";


    private final ElementService elementService;

    @Autowired
    public AnidbImportService(ElementService elementService) {
        this.elementService = elementService;
    }

    private String loadAnimeData() {
        return "<?xml version=\"1.0\"?>\n" +
                "<anime id=\"1\" restricted=\"false\">\n" +
                "  <type>TV Series</type>\n" +
                "  <episodecount>13</episodecount>\n" +
                "  <startdate>1999-01-03</startdate>\n" +
                "  <enddate>1999-03-28</enddate>\n" +
                "  <titles>\n" +
                "    <title xml:lang=\"x-jat\" type=\"main\">Seikai no Monshou</title>\n" +
                "    <title xml:lang=\"cs\" type=\"synonym\">Hvězdný erb</title>\n" +
                "    <title xml:lang=\"zh-Hans\" type=\"synonym\">星界之纹章</title>\n" +
                "    <title xml:lang=\"en\" type=\"short\">CotS</title>\n" +
                "    <title xml:lang=\"x-jat\" type=\"short\">SnM</title>\n" +
                "    <title xml:lang=\"ja\" type=\"official\">星界の紋章</title>\n" +
                "    <title xml:lang=\"en\" type=\"official\">Crest of the Stars</title>\n" +
                "    <title xml:lang=\"fr\" type=\"official\">Crest of the Stars</title>\n" +
                "    <title xml:lang=\"pl\" type=\"official\">Crest of the Stars</title>\n" +
                "  </titles>\n" +
                "  <relatedanime>\n" +
                "    <anime id=\"4\" type=\"Sequel\">Seikai no Senki</anime>\n" +
                "    <anime id=\"6\" type=\"Prequel\">Seikai no Danshou: Tanjou</anime>\n" +
                "    <anime id=\"1623\" type=\"Summary\">Seikai no Monshou Tokubetsu Hen</anime>\n" +
                "  </relatedanime>\n" +
                "  <similaranime>\n" +
                "    <anime id=\"584\" approval=\"75\" total=\"89\">Ginga Eiyuu Densetsu</anime>\n" +
                "    <anime id=\"2745\" approval=\"52\" total=\"62\">Starship Operators</anime>\n" +
                "    <anime id=\"6005\" approval=\"36\" total=\"52\">Tytania</anime>\n" +
                "    <anime id=\"192\" approval=\"18\" total=\"40\">Mugen no Ryvius</anime>\n" +
                "    <anime id=\"630\" approval=\"14\" total=\"28\">Uchuu no Stellvia</anime>\n" +
                "    <anime id=\"5406\" approval=\"3\" total=\"16\">Ookami to Koushinryou</anime>\n" +
                "    <anime id=\"18\" approval=\"2\" total=\"11\">Musekinin Kanchou Tylor</anime>\n" +
                "  </similaranime>\n" +
                "  <recommendations total=\"20\">\n" +
                "    <recommendation type=\"Recommended\" uid=\"567190\">As has been said, a solid space opera, but where it really shines is the two protagonists and how they interact. They have misunderstandings constantly, but they just talk it out until they come to a better understanding of each other. It`s beautiful.\n" +
                "\n" +
                "The series does have its issues. The art style and animation are fine despite the age, but the direction and editing are incredibly shoddy at times and the pacing is off where subplots are concerned. Not to mention the overly drawn out, drily delivered exposition at the start of every episode.\n" +
                "\n" +
                "That said, at 13 episodes it`s still an easy recommendation. Watching two people work that hard at empathy is such a rare treat. Don`t miss this if you want some refreshingly straightforward romance.</recommendation>\n" +
                "    <recommendation type=\"Recommended\" uid=\"350281\">Space opera with an exciting buildup and a sublime crescendo. 4X players, don`t miss this!</recommendation>\n" +
                "    <recommendation type=\"Recommended\" uid=\"112858\">If you`re into scifi and generally more sophisticated stuff than brainless entertainment, this is a must see. Everyone else at least take a close look, you might be suprised.</recommendation>\n" +
                "    <recommendation type=\"Must See\" uid=\"411532\">Strong characters and Great plot.  One of the best Space Opera`s I`ve seen.</recommendation>\n" +
                "    <recommendation type=\"Must See\" uid=\"284037\">Awesome! Dunno what to say, but it`s just.... awesome!</recommendation>\n" +
                "    <recommendation type=\"Must See\" uid=\"125868\">This is the second best anime of all time, behind seikai no senki II closely followed by seikai no senki II.  This is a must see anime.  Excellenet plots, environment, characters, development, and re-watch value.</recommendation>\n" +
                "    <recommendation type=\"Must See\" uid=\"691547\">An awesome space opera\n" +
                "that tries to answer a simple yet complicated question:\n" +
                "Can humans be friends with aliens?\n" +
                "So if you`re looking for an answer watch this.\n" +
                "Btw think of this series as a small grandchild\n" +
                "of a great EPIC like LoGH\n" +
                "but with Lafiel in the lead</recommendation>\n" +
                "  </recommendations>\n" +
                "  <url>http://www.sunrise-inc.co.jp/seikai/</url>\n" +
                "  <creators>\n" +
                "    <name id=\"4303\" type=\"Music\">Hattori Katsuhisa</name>\n" +
                "    <name id=\"4234\" type=\"Direction\">Nagaoka Yasuchika</name>\n" +
                "    <name id=\"4516\" type=\"Character Design\">Watabe Keisuke</name>\n" +
                "    <name id=\"8924\" type=\"Series Composition\">Yoshinaga Aya</name>\n" +
                "    <name id=\"4495\" type=\"Original Work\">Morioka Hiroyuki</name>\n" +
                "  </creators>\n" +
                "  <description>* Based on the sci-fi novel series by http://anidb.net/cr4495 [Morioka Hiroyuki].\n" +
                "http://anidb.net/ch4081 [Linn Jinto]`s life changes forever when the http://anidb.net/ch7514 [Humankind Empire Abh] takes over his home planet of Martine without firing a single shot. He is soon sent off to study the http://anidb.net/t2324 [Abh] language and culture and to prepare himself for his future as a nobleman — a future he never dreamed of, asked for, or even wanted.\n" +
                "Now, Jinto is entering the next phase of his training, and he is about to meet the first Abh in his life, the lovely http://anidb.net/ch28 [Lafiel]. However, Jinto is about to learn that there is more to her than meets the eye, and together they will have to fight for their very lives.</description>\n" +
                "  <ratings>\n" +
                "    <permanent count=\"4430\">8.16</permanent>\n" +
                "    <temporary count=\"4460\">8.23</temporary>\n" +
                "    <review count=\"12\">8.70</review>\n" +
                "  </ratings>\n" +
                "  <picture>440.jpg</picture>\n" +
                "  <resources>\n" +
                "    <resource type=\"1\">\n" +
                "      <externalentity>\n" +
                "        <identifier>14</identifier>\n" +
                "      </externalentity>\n" +
                "    </resource>\n" +
                "    <resource type=\"2\">\n" +
                "      <externalentity>\n" +
                "        <identifier>290</identifier>\n" +
                "      </externalentity>\n" +
                "    </resource>\n" +
                "    <resource type=\"3\">\n" +
                "      <externalentity>\n" +
                "        <identifier>376</identifier>\n" +
                "        <identifier>xzudnt</identifier>\n" +
                "      </externalentity>\n" +
                "    </resource>\n" +
                "    <resource type=\"4\">\n" +
                "      <externalentity>\n" +
                "        <url>http://www.sunrise-inc.co.jp/seikai/</url>\n" +
                "      </externalentity>\n" +
                "    </resource>\n" +
                "    <resource type=\"6\">\n" +
                "      <externalentity>\n" +
                "        <identifier>Crest_of_the_Stars</identifier>\n" +
                "      </externalentity>\n" +
                "    </resource>\n" +
                "    <resource type=\"7\">\n" +
                "      <externalentity>\n" +
                "        <identifier>星界の紋章</identifier>\n" +
                "      </externalentity>\n" +
                "    </resource>\n" +
                "    <resource type=\"8\">\n" +
                "      <externalentity>\n" +
                "        <identifier>1333</identifier>\n" +
                "      </externalentity>\n" +
                "    </resource>\n" +
                "    <resource type=\"9\">\n" +
                "      <externalentity>\n" +
                "        <identifier>90291</identifier>\n" +
                "      </externalentity>\n" +
                "    </resource>\n" +
                "    <resource type=\"10\">\n" +
                "      <externalentity>\n" +
                "        <identifier>1869</identifier>\n" +
                "      </externalentity>\n" +
                "    </resource>\n" +
                "    <resource type=\"14\">\n" +
                "      <externalentity>\n" +
                "        <identifier>11753</identifier>\n" +
                "        <identifier>v</identifier>\n" +
                "      </externalentity>\n" +
                "    </resource>\n" +
                "    <resource type=\"17\">\n" +
                "      <externalentity>\n" +
                "        <identifier>seikai01</identifier>\n" +
                "      </externalentity>\n" +
                "    </resource>\n" +
                "    <resource type=\"31\">\n" +
                "      <externalentity>\n" +
                "        <identifier>6674</identifier>\n" +
                "      </externalentity>\n" +
                "    </resource>\n" +
                "  </resources>\n" +
                "  <tags>\n" +
                "    <tag id=\"36\" parentid=\"2607\" weight=\"300\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2018-01-21\">\n" +
                "      <name>military</name>\n" +
                "      <description>The military, also known as the armed forces, are forces authorized and legally entitled to use deadly force so as to support the interests of the state and its citizens. The task of the military is usually defined as defence of the state and its citizens and the prosecution of war against foreign powers. The military may also have additional functions within a society, including construction, emergency services, social ceremonies, and guarding critical areas.\n" +
                "Source: Wikipedia</description>\n" +
                "      <picurl>212184.jpg</picurl>\n" +
                "    </tag>\n" +
                "    <tag id=\"1396\" parentid=\"2607\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2014-10-01\">\n" +
                "      <name>immortality</name>\n" +
                "      <description>Immortality is the ability to live in a physical or spiritual form for an infinite or inconceivably vast length of time. Humans, whose life span is limited and who die of age, disease and external trauma, have long sought ways to extend their lives; the Epic of Gilgamesh, considered one of the first great literary works created by mankind, is primarily the quest of a hero seeking to become immortal. The advances of science, in particular medicine and engineering, and especially in the last few centuries, have helped people extend their life span and may yet continue finding ways to do so.\n" +
                "In fiction, immortals are not quite as uncommon as in real life, and the possible implications of endless or very long lives are often explored. Such implications include, but are not limited to: repeatedly accompanying the deaths of those dear to them; having to hide their condition from the mortals surrounding them; becoming disillusioned with their lives and being unable to kill themselves; and even staying forever trapped in the body of a specific physical age, that body most often being that of a child. As such, while many may wish for immortality, it may be little more than a curse for the few who have it.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2008\" parentid=\"2891\" weight=\"200\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2015-08-05\">\n" +
                "      <name>small breasts</name>\n" +
                "      <description>This character has a bust size that is considered small. \n" +
                "The small size is usually a bust that would use a bra which is A or B. In anime, C may rarely enter this category.</description>\n" +
                "      <picurl>162720.jpg</picurl>\n" +
                "    </tag>\n" +
                "    <tag id=\"2604\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2018-07-08\">\n" +
                "      <name>content indicators</name>\n" +
                "      <description>The content indicators branch is intended to be a less geographically specific tool than the `age rating` used by convention, for warning about things that might cause offence. Obviously there is still a degree of subjectivity involved, but hopefully it will prove useful for parents with delicate children, or children with delicate parents.\n" +
                "Refer to further guidance on http://wiki.anidb.net/w/Categories:Content_Indicators [the Wiki page] for how to apply tag weights for content indicators.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2605\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2014-09-28\">\n" +
                "      <name>dynamic</name>\n" +
                "      <description>Some of the central structural elements in anime are: \n" +
                "Plot Continuity \n" +
                "How does the plot or several plots unfold. Are they a strictly linear retelling of one big continuing story, a serial, possibly with several side stories, or is the content chopped into a set of unconnected episodes, only sharing the same setting and characters?\n" +
                "Stereotype Characters\n" +
                "Then there is the question of the character set presentation. Is it completely original, or is it your usual stereotypical character cast. Both choices have their merit, for example it would not really make much sense to reinvent the wheel for a harem anime. Fans of that genre come to expect their favourite stereotype protagonists.\n" +
                "Plot Twists\n" +
                "Finally there is the question of how complex the plot / the story unfolds. Are there side-plots that merge into the main plot leading to unexpected plot twists, or is the anime quite predictable? The latter would not be appropriate for detective stories. In harem anime on the other hand a foreseeable outcome is actually something the fans will come to expect.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2607\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2009-04-29\">\n" +
                "      <name>themes</name>\n" +
                "      <description>Themes describe the very central elements important to the anime stories. They set the backdrop against which the protagonists must face their challenges. Be it School Life, present Daily Life, Military action, Cyberpunk, Law and Order detective work, Sports, or the Underworld. These are only but a few of the more typical backgrounds for anime plots. Add to that a Conspiracy setting with a possible tragic outcome, the Themes span most of the imaginable subject matter relevant to anime.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2608\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2012-12-04\">\n" +
                "      <name>fetishes</name>\n" +
                "      <description>Everything under fetishes (except for Bishounen and Juujin) is strictly only meant for hentai (18 Restricted) and ecchi anime. In case of ecchi, it must be the main element of the show and not when there`s only some of it, for example, there`s some ecchi in Shinseiki Evangelion, but the fact you get to see Asuka`s pantsu doesn`t warrant it the schoolgirl fetish category. So be careful to which ecchi show you add fetishes; it`s fairly straightforward with hentai, though. Overall, this category tree is to satisfy the needs of people with different fetishes, so they can search for what tingles their tangle; hence why this is restricted to hentai and full ecchi shows. </description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2609\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2018-09-01\">\n" +
                "      <name>original work</name>\n" +
                "      <description>What the anime is based on! This is given as the original work credit in the OP. Mostly of academic interest, but a useful bit of info, hinting at the possible depth of story.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2610\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2009-04-22\">\n" +
                "      <name>setting</name>\n" +
                "      <description>The setting describes in what time and place an anime takes place. To a certain extent it describes what you can expect from the world in the anime.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2611\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2014-09-08\">\n" +
                "      <name>elements</name>\n" +
                "      <description>Next to Themes setting the backdrop for the protagonists in anime, there are the more detailed plot Elements that centre on character interactions: \"What do characters do to each other or what is done to them?\". Is it violent Action, an awe-inspiring Adventure in a foreign place, the gripping life of a Detective, a slapstick Comedy, an Ecchi Harem anime, a SciFi epic, or some Fantasy travelling adventure?</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2612\" parentid=\"2610\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2016-09-04\">\n" +
                "      <name>time</name>\n" +
                "      <description>This placeholder lists different epochs in human history and more vague but important timelines such as the future, the present and the past.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2613\" parentid=\"2610\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2016-09-04\">\n" +
                "      <name>place</name>\n" +
                "      <description>The places the anime can take place in. Includes more specific places such as a country on Earth, as well as more general places such as a dystopia or a mirror world.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2619\" parentid=\"2841\" weight=\"200\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2017-12-26\">\n" +
                "      <name>gunfights</name>\n" +
                "      <description>Fights where two or more parties are firing guns at one another.</description>\n" +
                "      <picurl>211280.jpg</picurl>\n" +
                "    </tag>\n" +
                "    <tag id=\"2626\" parentid=\"2612\" weight=\"600\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2009-05-15\">\n" +
                "      <name>future</name>\n" +
                "      <description>Anime that fall under the future category are the ones that have a society that could have originated from ours. In most cases it should go hand to hand with sci-fi since we expect the future to consist of advanced technology, however, in the case where it`s explicitly stated in the story that the anime takes place \"x years into the future\", the state of technology doesn`t matter as long as it`s long into the future. The anime can be set in an alternative universe not tied with our own, as long as it has a futuristic feel to it.\n" +
                "This category sports quite a lot of anime, mostly stemming from SciFi, but not necessarily only from there.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2627\" parentid=\"2613\" weight=\"600\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2009-04-20\">\n" +
                "      <name>space</name>\n" +
                "      <description>The final frontier.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2636\" parentid=\"2846\" weight=\"200\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2015-10-06\">\n" +
                "      <name>human enhancement</name>\n" +
                "      <description>The enhancement of humans can take various forms: It can be something comparatively non-intrusive as a power-up drug, a form of machine implantation or cyberization (\"SAC Solid State Society\"), or outright - possibly atrocious - genetic manipulation (\"Ao no 6-gou\"). Plot-wise enhanced protagonists in anime usually have to pay a price for their enhancements, be it physical or mental.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2639\" parentid=\"2846\" infobox=\"true\" weight=\"500\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2009-04-29\">\n" +
                "      <name>space travel</name>\n" +
                "      <description>Space Travel usually refers going to space or travelling there with or without a spacecraft. There are different forms of space travel like Interplanetary travel, Interstellar travel or Intergalactic travel. Since it isn`t possible to travel faster than light, most anime series involve some sort \"sub-space travel mechanism\" as a way to travel faster than light.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2660\" parentid=\"2627\" weight=\"400\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2009-05-15\">\n" +
                "      <name>other planet</name>\n" +
                "      <description>The anime will fit this category if some or all of the action takes place on another planet which is supposedly in the same universe as Earth, not a complete fantasy world; usually there has to be some concept of space travel. The Latin name for Earth - Terra, would also pass the condition of being in the same universe as Earth. Basically, even mentioning of Earth existing or having existed would be enough. Although Moon is a moon, it also qualifies as Other Planet.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2661\" parentid=\"2627\" weight=\"400\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2009-05-18\">\n" +
                "      <name>shipboard</name>\n" +
                "      <description>Beam me up, Scotty! Space stations (think Death Star) also count. Basically, this category is to tell if the setting takes place onboard a spaceship in space. This category is not applicable if the spaceship lies on a planet and never actually gets into space.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2741\" parentid=\"2891\" weight=\"200\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2015-12-14\">\n" +
                "      <name>large breasts</name>\n" +
                "      <description>Large breasts are breasts a fair notch larger than average, as found in some adolescent girls or fully grown women; however, as anime has no upper limit to breast size, the definition applies somewhat differently. To be considered large, breasts in anime must be bigger than real-life natural ones or natural-seeming fake ones, to the point they`d appear weirdly or even abnormally large in real-life females. On the high end of the spectrum, breasts that are as big as the character`s head or possibly a bit bigger are still considered large; however, breasts that take at least as much space as the rest of the chest or the abdomen, or that are almost as big but have an absurdly unnatural shape, are the less common http://anidb.net/t1774 [gigantic breasts] instead.</description>\n" +
                "      <picurl>161014.jpg</picurl>\n" +
                "    </tag>\n" +
                "    <tag id=\"2749\" parentid=\"2604\" weight=\"100\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2018-07-08\">\n" +
                "      <name>nudity</name>\n" +
                "      <description>Nudity is the state of wearing no clothing. The wearing of clothing is exclusively a human characteristic. The amount of clothing worn depends on functional considerations (such as a need for warmth or protection from the elements) and social considerations. In some situations the minimum amount of clothing (i.e. covering of a person`s genitals) may be socially acceptable, while in others much more clothing is expected.\n" +
                "Refer to further guidance on http://wiki.anidb.net/w/Categories:Content_Indicators [the Wiki page] for how to apply tag weights for content indicators.\n" +
                "People, as individuals and in groups, have varying attitudes towards their own nudity. Some people are relaxed about appearing less than fully clothed in front of others, while others are uncomfortable or inhibited in that regard. People are nude in a variety of situations, and whether they are prepared to disrobe in front of others depends on the social context in which the issue arises. For example, people need to bathe without clothing, some people also sleep in the nude, some prefer to sunbathe in the nude or at least topless, while others are nude in other situations. Some people adopt naturism as a lifestyle.\n" +
                "Though the wearing of clothes is the social norm in most cultures, some cultures, groups and individuals are more relaxed about nudity, though attitudes often depend on context. On the other hand, some people feel uncomfortable in the presence of any nudity, and the presence of a nude person in a public place can give rise to controversy, irrespective of the attitude of the person who is nude. Besides meeting social disapproval, in some places public nudity may constitute a crime of indecent exposure. Many people have strong views on nudity, which to them can involve issues and standards of modesty, decency and morality. Some people have an psychological aversion to nudity, called gymnophobia. Many people regard nudity to be inherently sexual and erotic.\n" +
                "Nudity is to be found in a multitude of media, including art, photography, film and on the Internet. It is a factor in adult entertainment of various types.\n" +
                "Especially gratuitous fanservice anime shows tend to show their (usually female) characters frequently without any clothes, though often hiding genitals through the means of additions like steam.\n" +
                "Source: wiki</description>\n" +
                "      <picurl>60576.jpg</picurl>\n" +
                "    </tag>\n" +
                "    <tag id=\"2778\" parentid=\"2858\" weight=\"300\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2012-11-09\">\n" +
                "      <name>slow when it comes to love</name>\n" +
                "      <description>The main protagonists in this anime have a very hard time trying to express their feelings to each other. They simply will not finally confess their love.\n" +
                "In some cases this romance element is based on unrequited love, or an absurd amount of shyness.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2790\" parentid=\"2605\" weight=\"600\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2010-01-17\">\n" +
                "      <name>plot continuity</name>\n" +
                "      <description>Consistency of the characteristics of persons, plot, objects, places and events seen by the viewer.\n" +
                "A single linear focused plot is highly continuous.\n" +
                "A truly random plot is highly discontinuous.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2799\" parentid=\"2609\" infobox=\"true\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2010-05-14\">\n" +
                "      <name>novel</name>\n" +
                "      <description>Note that novel means book with words in paper, so no comics and no \"visual novels\", which should go under manga and dating-sim - visual Novel/erotic game respectively.\n" +
                "Note that it can be applied to novels as we know them but also the typical light novels for teens and young adults, which are common in Japan and aside from being short include a number of illustrations.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2841\" parentid=\"2611\" infobox=\"true\" weight=\"400\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2017-12-26\">\n" +
                "      <name>action</name>\n" +
                "      <description>Action anime usually involve a fairly straightforward story of good guys versus bad guys, where most disputes are resolved by using physical force. It often contains a lot of shooting, explosions and fighting.</description>\n" +
                "      <picurl>211261.jpg</picurl>\n" +
                "    </tag>\n" +
                "    <tag id=\"2846\" parentid=\"7050\" infobox=\"true\" weight=\"500\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2018-01-21\">\n" +
                "      <name>science fiction</name>\n" +
                "      <description>Science fiction stories usually focus on how situations could be different in the future when we are more technologically advanced; due to either our own achievements or having met with other civilizations whom we have learned from.</description>\n" +
                "      <picurl>212180.jpg</picurl>\n" +
                "    </tag>\n" +
                "    <tag id=\"2849\" parentid=\"7050\" weight=\"100\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2005-05-07\">\n" +
                "      <name>fantasy</name>\n" +
                "      <description>Fantasy is a genre of fiction that uses magic and other supernatural phenomena as a primary element of plot, theme, or setting. Many works within the genre take place in fictional worlds where magic is common. Fantasy is generally distinguished from science fiction and horror by the expectation that it steers clear of (pseudo-)scientific and macabre themes, respectively, though there is a great deal of overlap between the three (which are subgenres of speculative fiction).\n" +
                "In popular culture, the genre of fantasy is dominated by its medievalist form, especially since the worldwide success of The Lord of the Rings books by J. R. R. Tolkien. In its broadest sense however, fantasy comprises works by many writers, artists, filmmakers, and musicians, from ancient myths and legends to many recent works embraced by a wide audience today.\n" +
                "Fantasy is a vibrant area of academic study in a number of disciplines (English, cultural studies, comparative literature, history, medieval studies). Work in this area ranges widely, from the structuralist theory of Tzvetan Todorov, which emphasizes the fantastic as a liminal space, to work on the connections (political, historical, literary) between medievalism and popular culture\n" +
                "Source: Wiki</description>\n" +
                "      <picurl>68768.jpg</picurl>\n" +
                "    </tag>\n" +
                "    <tag id=\"2850\" parentid=\"2611\" infobox=\"true\" weight=\"500\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2015-10-25\">\n" +
                "      <name>adventure</name>\n" +
                "      <description>Adventures are exciting stories, with new experiences or exotic locales. Adventures are designed to provide an action-filled, energetic experience for the viewer. Rather than the predominant emphasis on violence and fighting that is found in pure action anime, however, the viewer of adventures can live vicariously through the travels, conquests, explorations, creation of empires, struggles and situations that confront the main characters, actual historical figures or protagonists. Under the category of adventures, we can include traditional swashbucklers, serialized films, and historical spectacles, searches or expeditions for lost continents, \"jungle\" and \"desert\" epics, treasure hunts and quests, disaster films, and heroic journeys or searches for the unknown. Adventure films are often, but not always, set in an historical period, and may include adapted stories of historical or literary adventure heroes, kings, battles, rebellion, or piracy.</description>\n" +
                "      <picurl>178879.jpg</picurl>\n" +
                "    </tag>\n" +
                "    <tag id=\"2858\" parentid=\"2611\" weight=\"300\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2017-12-01\">\n" +
                "      <name>romance</name>\n" +
                "      <description>Romance describes a story that deals with love between two or more characters typically also having an optimistic ending. Romance is also a difficulty encountered when creating a harem.</description>\n" +
                "      <picurl>210061.jpg</picurl>\n" +
                "    </tag>\n" +
                "    <tag id=\"2868\" parentid=\"2607\" weight=\"200\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2015-12-20\">\n" +
                "      <name>racism</name>\n" +
                "      <description>Racism is prejudice, discrimination, or antagonism directed against someone of an ethnically different background based on the pseudo-scientific belief that biological differences between peoples form so-called \"races\" and that one`s own \"race\" is superior to another, possibly also holding that members of different \"races\" should be treated differently. Some definitions include only consciously malignant forms of discrimination, and some views include discriminatory behaviors and beliefs based on cultural, national, ethnic, caste, or religious stereotypes. In works of fiction, this may also include similar actions against other sentient species.\n" +
                "Source: Wikipedia</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2874\" parentid=\"2607\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2018-05-07\">\n" +
                "      <name>disaster</name>\n" +
                "      <description>A disaster is a large-scale situation that causes great loss of or damage or threat to life, health, property, society, or the environment, irrespective of its source or cause. Disasters have substantial, long-lasting negative repercussions, and great effort must be spent in order to undo or mitigate their effects.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2891\" parentid=\"2608\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2014-12-31\">\n" +
                "      <name>breasts</name>\n" +
                "      <description>The female bust size matters! Or does it? The three subcategories spell out the sizes that relate to fetishes, be they gigantic, large or small. Normal sized breasts are not a fetish, it seems. The tag is not only used for ecchi anime, but also for hentai shows as well.</description>\n" +
                "      <picurl>161015.jpg</picurl>\n" +
                "    </tag>\n" +
                "    <tag id=\"2907\" parentid=\"2636\" infobox=\"true\" weight=\"400\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2015-09-24\">\n" +
                "      <name>genetic modification</name>\n" +
                "      <description>Story-wise, genetic modification is usually centred on humans, probably the most subtle and creepy way of messing with mankind. Playing with the fear of the unknown, genetically altered humans are technically a new species, that may not only surpass but actually replace unaltered humans. The level of modification will vary from subtle changes in \"look\" (e.g. super-human beauty), to mental enhancements (PSI powers), to more or less atrocious adaptations to extreme environments (breath under water, radiation resistance). Note that changes in Aliens would be difficult to pinpoint, after all they are alien.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"2925\" parentid=\"2874\" weight=\"400\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2012-10-07\">\n" +
                "      <name>war</name>\n" +
                "      <description>War is a phenomenon of organized violent conflict, typified by extreme aggression, societal disruption and adaptation, and high mortality. The objective of warfare differs in accord with a group`s role in a conflict: The goals of offensive warfare are typically the submission, assimilation or destruction of another group, while the goals of defensive warfare are simply the repulsion of the offensive force and, often, survival itself. Relative to each other, combatants in warfare are called enemies. The terms military, militant, and militarism each refer to fundamental aspects of war, i.e. the organized group, the combative individual, and the supportive ethos (respectively).\n" +
                "Source: wiki</description>\n" +
                "      <picurl>56441.jpg</picurl>\n" +
                "    </tag>\n" +
                "    <tag id=\"3138\" parentid=\"6151\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2014-10-08\">\n" +
                "      <name>fictional language</name>\n" +
                "      <description>Fictional languages are intended to be the languages of a fictional world and are often designed with the intent of giving more depth and an appearance of plausibility to the fictional worlds with which they are associated, and to have their characters communicate in a fashion which is both alien and dislocated.\n" +
                "Some of these languages, e.g. in worlds of fantasy fiction, alternative universes, Earth`s future, or alternate history, are presented as distorted versions or dialects of modern English or other natural language, while others are independently designed conlangs.\n" +
                "A fictional language often has the least amount of grammar and vocabulary possible, and rarely extends beyond the absolutely necessary. At the same time, some others have developed languages in detail for their own sake, such as J. R. R. Tolkien`s Quenya and Sindarin and Star Trek`s Klingon language which exist as functioning, usable languages. Here \"fictional\" can be a misnomer.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"3489\" parentid=\"2841\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2017-12-26\">\n" +
                "      <name>car chases</name>\n" +
                "      <description>Car chases occur as part of the action setting in this anime.</description>\n" +
                "      <picurl>211263.jpg</picurl>\n" +
                "    </tag>\n" +
                "    <tag id=\"3589\" parentid=\"6230\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2011-03-06\">\n" +
                "      <name>multiple protagonists</name>\n" +
                "    </tag>\n" +
                "    <tag id=\"3683\" parentid=\"2605\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2014-09-28\">\n" +
                "      <name>storytelling</name>\n" +
                "    </tag>\n" +
                "    <tag id=\"3929\" parentid=\"2607\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2016-09-09\">\n" +
                "      <name>sociocultural evolution</name>\n" +
                "      <description>Describes how cultures and societies develop over time, based on both cultural evolution and social evolution.\n" +
                "A post-apocalyptic or a Utopian setting would show very interesting changes in human behaviour, mental state, or simply daily life.</description>\n" +
                "      <picurl>191062.jpg</picurl>\n" +
                "    </tag>\n" +
                "    <tag id=\"3977\" parentid=\"3683\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2008-12-29\">\n" +
                "      <name>dialogue driven</name>\n" +
                "      <description>A dialogue driven show has its story advanced by dialogue, with action being of much lower priority, and relies heavily on conversations between characters.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"4022\" parentid=\"6230\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2010-11-29\">\n" +
                "      <name>strong female lead</name>\n" +
                "      <description>The lead female character has a strong personality and plays a part that is not the typical damsel-in-distress role. \n" +
                "This is the exact opposite of the “damsel in distress” tag.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"4081\" parentid=\"2841\" weight=\"400\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2008-06-02\">\n" +
                "      <name>space battles</name>\n" +
                "      <description>There are space battles in this anime. Usually between Earth forces and some alien invader, but ship to ship skirmishes in deep space are just as common.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"4440\" parentid=\"2858\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2010-02-01\">\n" +
                "      <name>boy meets girl</name>\n" +
                "      <description>A classical type of plot in story writing, where the story begins with a boy meeting a girl leading to romantic development between the two. More often than not the boy loses the girl later on and has to find her again.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"4446\" parentid=\"2639\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2017-12-12\">\n" +
                "      <name>faster-than-light travel</name>\n" +
                "      <description>Faster-than-light travel is taken for grated in this anime. This is achieved by travelling via hyperspace, nullspace, or wormholes.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"4480\" parentid=\"6246\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"true\" verified=\"true\" update=\"2018-08-13\">\n" +
                "      <name>happy ending</name>\n" +
                "      <description>And they lived \"happily ever after\"... *sniff*.\n" +
                "All issues in the anime have been resolved, the love polygons sorted out, the conflicts resolved, the guy gets the girl (or the other way around), and everyone else lives on blissfully.\n" +
                "(Not very realistic, but then again this is anime.)</description>\n" +
                "      <picurl>222185.jpg</picurl>\n" +
                "    </tag>\n" +
                "    <tag id=\"4537\" parentid=\"2605\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2014-02-15\">\n" +
                "      <name>time skip</name>\n" +
                "      <description>The plot features at least one skip or jump in time.\n" +
                "Example: A skip from the present into the past, or time skips in increments, e.g. every hour there is a time skip by a day.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"4665\" parentid=\"2607\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2015-02-26\">\n" +
                "      <name>propaganda</name>\n" +
                "      <description>Propaganda is communication aimed at influencing the attitude of a community toward some cause or position. As opposed to impartially providing information, propaganda in its most basic sense, presents information primarily to influence an audience. Propaganda often presents facts selectively (thus lying by omission) to encourage a particular synthesis, or uses loaded messages to produce an emotional rather than rational response to the information presented. The desired result is a change of the attitude toward the subject in the target audience to further a political agenda.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"5003\" parentid=\"2607\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2015-12-10\">\n" +
                "      <name>imperialism</name>\n" +
                "      <description>Imperialism is a type of advocacy of empire. Its name originated from the Latin word \"imperium\", which means to rule over large territories. Imperialism is \"a policy of extending a country`s power and influence through colonization, use of military force, or other means\".\n" +
                "Source: Wiki</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"5929\" parentid=\"2846\" weight=\"400\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2014-09-10\">\n" +
                "      <name>space opera</name>\n" +
                "      <description>Space opera is a sub-genre of fiction that emphasizes romantic adventures in space. Space opera generally involves large-scale conflicts between opponents with powerful abilities and technology. The genre will often be melodramatic, sometimes playing music that can be nothing short of awesome. Warfare and a battle of wits are usually less emphasized, preferring romance (with exceptions, e.g. Ginga Eiyuu Densetsu). Some space operas tend to be formulaic and sometimes even prosaic, often employing very simple devices and somewhat obvious strategies. An example of this would be the infamous laws of physics in the Macross universe, in which the main male lead will hardly ever get hit in battle.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"6151\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2014-09-10\">\n" +
                "      <name>technical aspects</name>\n" +
                "      <description>It may sometimes be useful to know about technical aspects of a show, such as information about its broadcasting or censorship. Such information can be found here.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"6230\" parentid=\"2605\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2014-10-20\">\n" +
                "      <name>cast</name>\n" +
                "    </tag>\n" +
                "    <tag id=\"6246\" parentid=\"2605\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2018-08-06\">\n" +
                "      <name>ending</name>\n" +
                "      <description>This is a container tag to house meta-like tags relating to how endings of anime play out.</description>\n" +
                "    </tag>\n" +
                "    <tag id=\"7050\" parentid=\"2611\" weight=\"0\" localspoiler=\"false\" globalspoiler=\"false\" verified=\"true\" update=\"2018-01-02\">\n" +
                "      <name>speculative fiction</name>\n" +
                "    </tag>\n" +
                "  </tags>\n" +
                "  <characters>\n" +
                "    <character id=\"28\" type=\"main character in\" update=\"2012-07-25\">\n" +
                "      <rating votes=\"1196\">9.15</rating>\n" +
                "      <name>Abriel Nei Debrusc Borl Paryun Lafiel</name>\n" +
                "      <gender>female</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <description>Ablïarsec néïc Dubreuscr Bœrh Parhynr Lamhirh (a.k.a., Viscountess Paryunu Abriel Nei Dobrusk Lafiel) is the main female protagonist in the anime Crest of the Stars, Banner of the Stars, and Banner of the Stars II, as well as all the novels written by Morioka Hiroyuki on which the shows were based. She is a strong-willed Abh princess (granddaughter of the Abh empress) who has a steely exterior, but ends up befriending Ghintec Linn (Jinto Lynn in the Martinh tongue). Like all Abh, she has bluish hued hair, and has a natural lifespan of over 200 years. Lamhirh also has lapis lazuli colored eyes. As an Ablïarsec, she has pointed ears, yet hers are markedly less so than other Ablïarsec. This is because half her genes (those not from her father) are from someone outside the Abriel clan and her father chose not to make any unnecessary alterations in her genes. She is deemed \"child of love\" (an Abh child with the genes of the parent, and the one the parent loves). Her full name can be roughly translated to Lamhirh (néïc Dubleuscr) Ablïarsec, Viscountess of Parhyn.\n" +
                "Despite being a princess, she rarely acts like one and hates being treated as one. One of the reasons she took a liking towards Ghintec is because when they first met, he neither recognized her as a princess nor treated her as one. Their relationship is so close that she freely allows him to use her real name of Lamhirh when addressing her, something that is very uncommon when addressing those of nobility or royalty.\n" +
                "She acts remarkably older than her age (at her introduction in Crest of the Stars, she is 16 years old) and can, in most cases, logically think her way out of most situations. However, her headstrong nature sometimes clouds her judgement and can lead her to become impulsive. An example of this is when she is reprimanded by Laicch for wishing to stay behind on the Gothlauth instead of continuing her mission of escorting Ghintec to the capital. She believes that she would have been of more use fighting with the crew rather than abandoning them. She is quickly shown how wrong her line of reasoning is and how much more disgraceful it would have been to abandon Ghintec and her mission. She is a remarkably good shot and although she sometimes doubts herself, she proves to be a worthy ship captain (deca-commander) in Banner of the Stars. She shows little emotion throughout Crest of the Stars, but as time goes by became very close friend with Ghintec through Banner of the Stars. This is especially true in later installments, where she more frequently questions how their friendship will last due to the doubt of Ghintec`s lifespan.\n" +
                "She is one of the candidates for the Abh Imperial Throne and, as indicated by her full name, she is the Viscountess of Parhynh, the so-called \"Country, or Nation, of Roses.\"</description>\n" +
                "      <picture>14304.jpg</picture>\n" +
                "      <seiyuu id=\"12\" picture=\"184301.jpg\">Kawasumi Ayako</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"4079\" type=\"secondary cast in\" update=\"2009-02-28\">\n" +
                "      <rating votes=\"19\">6.17</rating>\n" +
                "      <name>Abriel Nei Debrusc Larth Kryb Debeus</name>\n" +
                "      <gender>male</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <description>Lafiel`s father, the King of Kryv and a son of the current reigning empress.</description>\n" +
                "      <picture>20037.jpg</picture>\n" +
                "      <seiyuu id=\"3136\" picture=\"24121.jpg\">Suzuoki Hirotaka</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"4080\" type=\"secondary cast in\" update=\"2009-02-28\">\n" +
                "      <rating votes=\"30\">8.79</rating>\n" +
                "      <name>Lexshu Wef-Robell Plakia</name>\n" +
                "      <gender>female</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <description>The captain of the Gosroth (Gothlauth), she is every bit as smart, beautiful, dedicated and stubborn as Lafiel, but older and wiser. Judging from her name, Lexshu`s rank in the Abh social hierarchy is that of a reucec (knight or dame).</description>\n" +
                "      <picture>20038.jpg</picture>\n" +
                "      <seiyuu id=\"404\" picture=\"151682.jpg\">Takashima Gara</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"4081\" type=\"main character in\" update=\"2012-07-25\">\n" +
                "      <rating votes=\"210\">7.52</rating>\n" +
                "      <name>Linn Syun-Rock Dreu Haider Jinto</name>\n" +
                "      <gender>male</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <description>Born Jinto Lynn on planet Martine (spelt as Martinh in Baronh) of the Hyde star system (Haïdec in Baronh), he was the son of Rock Lynn, a politician who eventually became president of the Hyde star system. It was during his administration that the Abh empire was able to annex the Hyde stellar system into its territory through an invasion. Making his own ideas more valuable than the rights of the people of Martine, Rock Lin made a deal with the leaders of the invading forces that the territorial lord of Hyde shall be chosen amongst the people of Martine. The leader of the invading forces agreed to this however this meant that an election cannot be held making Rock, as the current leader of Martine, the natural choice to become its territorial lord. Because of this, Rock Lynn, and with him, Jinto, rose to the ranks of Abh nobility as Count of Haïdec.</description>\n" +
                "      <picture>20036.jpg</picture>\n" +
                "      <seiyuu id=\"362\" picture=\"74735.jpg\">Imai Yuka</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"4082\" type=\"secondary cast in\" update=\"2009-02-28\">\n" +
                "      <rating votes=\"63\">8.71</rating>\n" +
                "      <name>Abriel Nei Lamsar Larth Balkei Dusanyu</name>\n" +
                "      <gender>male</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <description>The current Dusanyu, or Crown Prince, of the Abh Empire of Mankind, the Baronh name of which is Frybar Gloer Gor Bari (Frybarec Gloer Gor Bari), Also the Commander-in-Chief of the Abh Armed Forces, who personally led the invasion of the Hyde star system.</description>\n" +
                "      <picture>20035.jpg</picture>\n" +
                "      <seiyuu id=\"1819\" picture=\"20694.jpg\">Shiozawa Kaneto</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"4083\" type=\"secondary cast in\" update=\"2009-02-28\">\n" +
                "      <rating votes=\"19\">6.69</rating>\n" +
                "      <name>Trife Boli Yuvdale Remsale</name>\n" +
                "      <gender>male</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <description>Despite verging on excessive caution, he is nonetheless one of the most able tacticians in the Labule. He puts every aspect and factor under consideration, frequently asking his subordinates` opinions and assessments before starting an operation. He is also one of the least loved officers.</description>\n" +
                "      <picture>20034.jpg</picture>\n" +
                "      <seiyuu id=\"220\" picture=\"17038.jpg\">Kosugi Juurouta</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"4084\" type=\"secondary cast in\" update=\"2009-02-28\">\n" +
                "      <rating votes=\"224\">9.08</rating>\n" +
                "      <name>Spoor Aron Sekpadao Letopanyu Peneju</name>\n" +
                "      <gender>female</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <description>This red-eyed Grand Duchess is one of the most eccentric officers in the Labule, which does not change the fact that she is one of its most able admirals. Though complaining of boredom most of the time, she suddenly springs into action when needed. One of her favourite pastimes is mercilessly teasing Kaselia, her chief of staff.</description>\n" +
                "      <picture>20033.jpg</picture>\n" +
                "      <seiyuu id=\"1509\" picture=\"19311.jpg\">Fukami Rika</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"4085\" type=\"secondary cast in\" update=\"2009-02-28\">\n" +
                "      <rating votes=\"16\">2.84</rating>\n" +
                "      <name>Atosryua Syun-Atos Lyuf Raika Febdak Klowal</name>\n" +
                "      <gender>male</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <description>The third Baron of Febdak (Faibdash), an obscure star system where Jinto and Lafiel made a stopover en route to Safugnoff (Sfagnaumh). He had an inferiority complex due to his clan`s origins and brief history, causing him to confine and isolate his own father. He tried to abduct Lafiel and imprison Jinto, which brought dire consequences onto himself.</description>\n" +
                "      <picture>20032.jpg</picture>\n" +
                "      <seiyuu id=\"139\" picture=\"29283.jpg\">Koyasu Takehito</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"4086\" type=\"secondary cast in\" update=\"2009-02-28\">\n" +
                "      <rating votes=\"16\">6.64</rating>\n" +
                "      <name>Atosryua Syun-Atos Lyuf Raika Febdak Srguf</name>\n" +
                "      <gender>male</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <description>The second Baron of Febdak and father of Klowal. Srguf`s mother was the first Baroness, who gained distinction by rising through the Imperial Star Force, or Labule (Laburec). Due to his being a genetic \"grounder,\" his own son despised him resulting in confinement and isolation. He eventually befriended Jinto and aided Lafiel in \"punishing\" Klowal. His daughter, Loïc eventually became the Baroness of Febdak and a commanding officer to Lafiel and Jinto.</description>\n" +
                "      <picture>20039.jpg</picture>\n" +
                "      <seiyuu id=\"39\" picture=\"16629.jpg\">Mugihito</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"4087\" type=\"appears in\" update=\"2009-02-28\">\n" +
                "      <rating votes=\"5\">5.33</rating>\n" +
                "      <name>Entoryua Rei</name>\n" +
                "      <gender>male</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <picture>20044.jpg</picture>\n" +
                "      <seiyuu id=\"339\" picture=\"79312.jpg\">Ishizuka Unshou</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"4088\" type=\"secondary cast in\" update=\"2009-02-28\">\n" +
                "      <rating votes=\"6\">1.41</rating>\n" +
                "      <name>Kaito</name>\n" +
                "      <gender>male</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <picture>20045.jpg</picture>\n" +
                "      <seiyuu id=\"3312\" picture=\"24691.jpg\">Ikemizu Michihiro</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"7493\" type=\"appears in\" update=\"2010-12-03\">\n" +
                "      <rating votes=\"2\">6.14</rating>\n" +
                "      <name>Gosroth</name>\n" +
                "      <gender>unknown</gender>\n" +
                "      <charactertype id=\"4\">Vessel</charactertype>\n" +
                "      <description>The Resii Gothroth.\n" +
                "The first Abh ship lost in the war.</description>\n" +
                "      <picture>60215.jpg</picture>\n" +
                "    </character>\n" +
                "    <character id=\"7501\" type=\"appears in\" update=\"2014-02-15\">\n" +
                "      <rating votes=\"1\">3.80</rating>\n" +
                "      <name>Wakusei Martine</name>\n" +
                "      <gender>unknown</gender>\n" +
                "      <charactertype id=\"3\">Organization</charactertype>\n" +
                "      <description>The homeworld of Jinto</description>\n" +
                "      <picture>150280.jpg</picture>\n" +
                "    </character>\n" +
                "    <character id=\"7503\" type=\"appears in\" update=\"2009-05-27\">\n" +
                "      <rating votes=\"2\">3.35</rating>\n" +
                "      <name>Rock Lynn</name>\n" +
                "      <gender>male</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <description>The former president of Martine, later made count of the Hyde system, after striking a deal with the Abh (in episode 1)</description>\n" +
                "      <picture>26165.jpg</picture>\n" +
                "      <seiyuu id=\"1733\" picture=\"25839.jpg\">Tanaka Hideyuki</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"7512\" type=\"secondary cast in\" update=\"2009-05-27\">\n" +
                "      <rating votes=\"5\">2.98</rating>\n" +
                "      <name>Jinto Lynn</name>\n" +
                "      <gender>male</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <description>Jinto, before he became a noble of the Abh.</description>\n" +
                "      <picture>26167.jpg</picture>\n" +
                "    </character>\n" +
                "    <character id=\"7514\" type=\"appears in\" update=\"2009-05-27\">\n" +
                "      <rating votes=\"12\">8.15</rating>\n" +
                "      <name>Abh Teikoku</name>\n" +
                "      <gender>unknown</gender>\n" +
                "      <charactertype id=\"3\">Organization</charactertype>\n" +
                "      <picture>26171.jpg</picture>\n" +
                "    </character>\n" +
                "    <character id=\"7516\" type=\"appears in\" update=\"2009-05-27\">\n" +
                "      <rating votes=\"3\">1.92</rating>\n" +
                "      <name>Till Corinth</name>\n" +
                "      <gender>male</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <picture>26175.jpg</picture>\n" +
                "      <seiyuu id=\"4193\" picture=\"154158.jpg\">Suzuki Eiichirou</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"21911\" type=\"appears in\" update=\"2010-09-27\">\n" +
                "      <rating votes=\"5\">5.54</rating>\n" +
                "      <name>Lina Clint</name>\n" +
                "      <gender>female</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <picture>55529.jpg</picture>\n" +
                "      <seiyuu id=\"4708\" picture=\"94172.jpg\">Hosono Masayo</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"21943\" type=\"secondary cast in\" update=\"2010-09-28\">\n" +
                "      <rating votes=\"8\">5.97</rating>\n" +
                "      <name>Ku Dorin</name>\n" +
                "      <gender>male</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <picture>55561.jpg</picture>\n" +
                "      <seiyuu id=\"2799\" picture=\"23198.jpg\">Matsuno Taiki</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"21944\" type=\"secondary cast in\" update=\"2010-09-28\">\n" +
                "      <rating votes=\"3\">4.80</rating>\n" +
                "      <name>Gyumuryua</name>\n" +
                "      <gender>female</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <picture>55562.jpg</picture>\n" +
                "      <seiyuu id=\"17008\" picture=\"46192.jpg\">Fuji Takako</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"21945\" type=\"secondary cast in\" update=\"2010-09-28\">\n" +
                "      <rating votes=\"2\">7.15</rating>\n" +
                "      <name>Deesh</name>\n" +
                "      <gender>male</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <picture>55563.jpg</picture>\n" +
                "      <seiyuu id=\"1913\" picture=\"186187.jpg\">Chiba Isshin</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"21946\" type=\"secondary cast in\" update=\"2010-09-28\">\n" +
                "      <rating votes=\"2\">5.14</rating>\n" +
                "      <name>Lairia</name>\n" +
                "      <gender>male</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <picture>55564.jpg</picture>\n" +
                "      <seiyuu id=\"370\" picture=\"204461.jpg\">Yusa Kouji</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"21947\" type=\"secondary cast in\" update=\"2010-09-28\">\n" +
                "      <rating votes=\"2\">6.16</rating>\n" +
                "      <name>Leshikuna</name>\n" +
                "      <gender>male</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <picture>55566.jpg</picture>\n" +
                "      <seiyuu id=\"2899\" picture=\"79024.jpg\">Miyazaki Issei</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"21948\" type=\"secondary cast in\" update=\"2010-09-28\">\n" +
                "      <rating votes=\"2\">4.19</rating>\n" +
                "      <name>Saryush</name>\n" +
                "      <gender>male</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <picture>55567.jpg</picture>\n" +
                "      <seiyuu id=\"399\" picture=\"17244.jpg\">Suyama Akio</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"21949\" type=\"secondary cast in\" update=\"2010-09-28\">\n" +
                "      <rating votes=\"3\">4.80</rating>\n" +
                "      <name>Yunseryua</name>\n" +
                "      <gender>female</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <picture>55568.jpg</picture>\n" +
                "      <seiyuu id=\"21499\" picture=\"188514.jpg\">Tomokawa Mari</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"21954\" type=\"secondary cast in\" update=\"2011-04-08\">\n" +
                "      <rating votes=\"7\">5.38</rating>\n" +
                "      <name>Abriel Nei Debrusc Spunej Ramaj</name>\n" +
                "      <gender>female</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <description>The 37th and current reigning empress of the Abh Empire, she is also Lafiel`s grandmother. She also holds the title \"Dreuc Ablïarser\" (Countess of Ablïarsec; Ablïarsec is also the name of the stellar system where Lakfakalle (Lacmhacarh), the imperial capital, is located).</description>\n" +
                "      <picture>55576.jpg</picture>\n" +
                "      <seiyuu id=\"542\" picture=\"60897.jpg\">Doi Mika</seiyuu>\n" +
                "    </character>\n" +
                "    <character id=\"21958\" type=\"appears in\" update=\"2015-02-07\">\n" +
                "      <rating votes=\"3\">8.05</rating>\n" +
                "      <name>Futune</name>\n" +
                "      <gender>none/does not apply</gender>\n" +
                "      <charactertype id=\"4\">Vessel</charactertype>\n" +
                "      <picture>55580.jpg</picture>\n" +
                "    </character>\n" +
                "    <character id=\"40209\" type=\"appears in\" update=\"2012-01-04\">\n" +
                "      <rating votes=\"15\">5.34</rating>\n" +
                "      <name>Diaho</name>\n" +
                "      <gender>male</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <description>Jinto`s cat.\n" +
                "\n" +
                "Lafiel gave him to Jinto at the end of http://anidb.net/a1 [Seikai no Monshou].\n" +
                "He is the son of Zanelia who is the daughter of Holia.\n" +
                "</description>\n" +
                "      <picture>79471.jpg</picture>\n" +
                "    </character>\n" +
                "    <character id=\"49519\" type=\"appears in\" update=\"2012-09-30\">\n" +
                "      <rating votes=\"2\">2.49</rating>\n" +
                "      <name>Seelnay</name>\n" +
                "      <gender>female</gender>\n" +
                "      <charactertype id=\"1\">Character</charactertype>\n" +
                "      <picture>50186.jpg</picture>\n" +
                "      <seiyuu id=\"7\" picture=\"186500.jpg\">Ootani Ikue</seiyuu>\n" +
                "    </character>\n" +
                "  </characters>\n" +
                "  <episodes>\n" +
                "    <episode id=\"1\" update=\"2011-07-01\">\n" +
                "      <epno type=\"1\">1</epno>\n" +
                "      <length>25</length>\n" +
                "      <airdate>1999-01-03</airdate>\n" +
                "      <rating votes=\"28\">3.31</rating>\n" +
                "      <title xml:lang=\"ja\">侵略</title>\n" +
                "      <title xml:lang=\"en\">Invasion</title>\n" +
                "      <title xml:lang=\"fr\">Invasion</title>\n" +
                "      <title xml:lang=\"x-jat\">Shinryaku</title>\n" +
                "    </episode>\n" +
                "    <episode id=\"2\" update=\"2011-07-01\">\n" +
                "      <epno type=\"1\">2</epno>\n" +
                "      <length>25</length>\n" +
                "      <airdate>1999-01-10</airdate>\n" +
                "      <rating votes=\"22\">4.94</rating>\n" +
                "      <title xml:lang=\"ja\">星たちの眷族</title>\n" +
                "      <title xml:lang=\"en\">Kin of the Stars</title>\n" +
                "      <title xml:lang=\"fr\">Les parents des Étoiles</title>\n" +
                "      <title xml:lang=\"x-jat\">Hoshi-tachi no Kenzoku</title>\n" +
                "    </episode>\n" +
                "    <episode id=\"1009\" update=\"2011-07-01\">\n" +
                "      <epno type=\"1\">10</epno>\n" +
                "      <length>25</length>\n" +
                "      <airdate>1999-03-07</airdate>\n" +
                "      <rating votes=\"17\">6.86</rating>\n" +
                "      <title xml:lang=\"ja\">二人だけの逃亡</title>\n" +
                "      <title xml:lang=\"en\">Escape: Just the Two of Us</title>\n" +
                "      <title xml:lang=\"fr\">Fuite à deux</title>\n" +
                "      <title xml:lang=\"x-jat\">Futari dake no Toubou</title>\n" +
                "    </episode>\n" +
                "    <episode id=\"1010\" update=\"2011-07-01\">\n" +
                "      <epno type=\"1\">5</epno>\n" +
                "      <length>25</length>\n" +
                "      <airdate>1999-01-31</airdate>\n" +
                "      <rating votes=\"20\">8.06</rating>\n" +
                "      <title xml:lang=\"ja\">ゴースロスの戦い</title>\n" +
                "      <title xml:lang=\"en\">The Battle of Gosroth</title>\n" +
                "      <title xml:lang=\"fr\">La bataille du Gothlauth</title>\n" +
                "      <title xml:lang=\"x-jat\">Gosroth no Tatakai</title>\n" +
                "    </episode>\n" +
                "    <episode id=\"1011\" update=\"2011-07-01\">\n" +
                "      <epno type=\"1\">4</epno>\n" +
                "      <length>25</length>\n" +
                "      <airdate>1999-01-24</airdate>\n" +
                "      <rating votes=\"20\">7.19</rating>\n" +
                "      <title xml:lang=\"ja\">奇襲</title>\n" +
                "      <title xml:lang=\"en\">Surprise Attack</title>\n" +
                "      <title xml:lang=\"fr\">Attaque surprise</title>\n" +
                "      <title xml:lang=\"x-jat\">Kishuu</title>\n" +
                "    </episode>\n" +
                "    <episode id=\"1012\" update=\"2011-07-01\">\n" +
                "      <epno type=\"1\">3</epno>\n" +
                "      <length>25</length>\n" +
                "      <airdate>1999-01-17</airdate>\n" +
                "      <rating votes=\"19\">7.31</rating>\n" +
                "      <title xml:lang=\"ja\">愛の娘</title>\n" +
                "      <title xml:lang=\"en\">Daughter of Love</title>\n" +
                "      <title xml:lang=\"fr\">La fille d`Amour</title>\n" +
                "      <title xml:lang=\"x-jat\">Ai no Musume</title>\n" +
                "    </episode>\n" +
                "    <episode id=\"1013\" update=\"2011-07-01\">\n" +
                "      <epno type=\"1\">9</epno>\n" +
                "      <length>25</length>\n" +
                "      <airdate>1999-02-28</airdate>\n" +
                "      <rating votes=\"17\">6.84</rating>\n" +
                "      <title xml:lang=\"ja\">戦場へ</title>\n" +
                "      <title xml:lang=\"en\">To the Battlefield</title>\n" +
                "      <title xml:lang=\"fr\">Vers le champ de bataille</title>\n" +
                "      <title xml:lang=\"x-jat\">Senjou e</title>\n" +
                "    </episode>\n" +
                "    <episode id=\"1014\" update=\"2011-07-01\">\n" +
                "      <epno type=\"1\">13</epno>\n" +
                "      <length>40</length>\n" +
                "      <airdate>1999-03-28</airdate>\n" +
                "      <rating votes=\"16\">8.53</rating>\n" +
                "      <title xml:lang=\"ja\">天翔る迷惑</title>\n" +
                "      <title xml:lang=\"en\">Trouble Soaring Through Heaven</title>\n" +
                "      <title xml:lang=\"fr\">Des gêneurs dans le firmament</title>\n" +
                "      <title xml:lang=\"x-jat\">Amagakeru Meiwaku</title>\n" +
                "    </episode>\n" +
                "    <episode id=\"1015\" update=\"2011-07-01\">\n" +
                "      <epno type=\"1\">8</epno>\n" +
                "      <length>25</length>\n" +
                "      <airdate>1999-02-21</airdate>\n" +
                "      <rating votes=\"17\">7.19</rating>\n" +
                "      <title xml:lang=\"ja\">アーヴの流儀</title>\n" +
                "      <title xml:lang=\"en\">The Style of the Abh</title>\n" +
                "      <title xml:lang=\"fr\">La méthode Abh</title>\n" +
                "      <title xml:lang=\"x-jat\">Abh no Ryuugi</title>\n" +
                "    </episode>\n" +
                "    <episode id=\"1016\" update=\"2011-07-01\">\n" +
                "      <epno type=\"1\">12</epno>\n" +
                "      <length>25</length>\n" +
                "      <airdate>1999-03-21</airdate>\n" +
                "      <rating votes=\"17\">6.67</rating>\n" +
                "      <title xml:lang=\"ja\">惑乱の淑女</title>\n" +
                "      <title xml:lang=\"en\">Lady of Chaos</title>\n" +
                "      <title xml:lang=\"fr\">La Dame du Chaos</title>\n" +
                "      <title xml:lang=\"x-jat\">Wakuran no Shukujo</title>\n" +
                "    </episode>\n" +
                "    <episode id=\"1017\" update=\"2011-07-01\">\n" +
                "      <epno type=\"1\">7</epno>\n" +
                "      <length>25</length>\n" +
                "      <airdate>1999-02-14</airdate>\n" +
                "      <rating votes=\"17\">6.93</rating>\n" +
                "      <title xml:lang=\"ja\">幸せな叛逆</title>\n" +
                "      <title xml:lang=\"en\">Fortunate Revolt</title>\n" +
                "      <title xml:lang=\"fr\">Heureuse révolte</title>\n" +
                "      <title xml:lang=\"x-jat\">Shiawase na Hangyaku</title>\n" +
                "    </episode>\n" +
                "    <episode id=\"1018\" update=\"2011-07-01\">\n" +
                "      <epno type=\"1\">11</epno>\n" +
                "      <length>25</length>\n" +
                "      <airdate>1999-03-14</airdate>\n" +
                "      <rating votes=\"18\">6.00</rating>\n" +
                "      <title xml:lang=\"ja\">スファグノーフ門沖会戦</title>\n" +
                "      <title xml:lang=\"en\">Sufugnoff Gateway Battle</title>\n" +
                "      <title xml:lang=\"fr\">La bataille du portail de Sfagnaumh</title>\n" +
                "      <title xml:lang=\"x-jat\">Sufugnoff Mon Oki Kaisen</title>\n" +
                "    </episode>\n" +
                "    <episode id=\"1019\" update=\"2011-07-01\">\n" +
                "      <epno type=\"1\">6</epno>\n" +
                "      <length>25</length>\n" +
                "      <airdate>1999-02-07</airdate>\n" +
                "      <rating votes=\"17\">6.61</rating>\n" +
                "      <title xml:lang=\"ja\">不可解な陰謀</title>\n" +
                "      <title xml:lang=\"en\">Mysterious Conspiracy</title>\n" +
                "      <title xml:lang=\"fr\">Mystérieuses intrigues</title>\n" +
                "      <title xml:lang=\"x-jat\">Fukakai na Inbou</title>\n" +
                "    </episode>\n" +
                "    <episode id=\"120767\" update=\"2010-08-05\">\n" +
                "      <epno type=\"3\">C3</epno>\n" +
                "      <length>0</length>\n" +
                "      <rating votes=\"1\">3.80</rating>\n" +
                "      <title xml:lang=\"en\">Ending 2</title>\n" +
                "    </episode>\n" +
                "    <episode id=\"120768\" update=\"2010-08-05\">\n" +
                "      <epno type=\"3\">C2</epno>\n" +
                "      <length>0</length>\n" +
                "      <rating votes=\"1\">5.69</rating>\n" +
                "      <title xml:lang=\"en\">Ending 1</title>\n" +
                "    </episode>\n" +
                "    <episode id=\"120769\" update=\"2010-08-05\">\n" +
                "      <epno type=\"3\">C1</epno>\n" +
                "      <length>0</length>\n" +
                "      <rating votes=\"1\">3.80</rating>\n" +
                "      <title xml:lang=\"en\">Opening</title>\n" +
                "    </episode>\n" +
                "  </episodes>\n" +
                "</anime>";
    }


    public Element importEntity(String aniDbId) throws Exception {
        String animeData = loadAnimeData();
        Element element = importAnime(animeData);

        return elementService.save(element);

        /*
        Element element = elementService.save(importData.element);

        importData.episodeList.forEach(e -> e.setSeason(element.getId()));
        episodeService.saveAll(importData.episodeList);
*/
    }


    private Element importAnime(String animeData) throws Exception {

        final Element element = new Element();
        element.setType(ElementType.ANIME);

        final List<Episode> episodeList = new ArrayList<>();

        final Document document = createDocument(animeData);
        final org.w3c.dom.Element rootNode = document.getDocumentElement();

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
                    element.setSynopsis(node.getTextContent());
                }
                else if (TAG_EPISODES.equals(nodeName)) {
                    extractEpisodes(episodeList, ((org.w3c.dom.Element) node).getElementsByTagName(TAG_EPISODE));
                }
            }
        }

        Season season = new Season(1, DEFAULT_SEASON_NAME);
        season.setEpisodeList(episodeList);

        List<Season> seasonSet = new ArrayList<>();
        seasonSet.add(season);
        element.setSeasonList(seasonSet);

        //entity.setAnidbId(id);

        return element;
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
}
