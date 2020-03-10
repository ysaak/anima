package ysaak.anima.data.importer.anidb;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ANIDB_TITLE")
public class AnidbTitle {
    @Id
    @GeneratedValue(generator = "suuid_generator")
    @GenericGenerator(name = "suuid_generator", strategy = "ysaak.anima.dao.SuuidGenerator")
    @Column(name = "ADTI_ID", nullable = false)
    private String id;

    @Column(name = "ADTI_AID", nullable = false)
    private String anidbId;

    @Column(name = "ADTI_TYPE", nullable = false)
    private AnidbTitleType type;

    @Column(name = "ADTI_LANG", nullable = false)
    private String lang;

    @Column(name = "ADTI_TITLE", nullable = false)
    private String title;

    public AnidbTitle() {
    }

    public AnidbTitle(String anidbId, AnidbTitleType type, String lang, String title) {
        this.anidbId = anidbId;
        this.type = type;
        this.lang = lang;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnidbId() {
        return anidbId;
    }

    public void setAnidbId(String anidbId) {
        this.anidbId = anidbId;
    }

    public AnidbTitleType getType() {
        return type;
    }

    public void setType(AnidbTitleType type) {
        this.type = type;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
