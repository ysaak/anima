package ysaak.anima.dao.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ANIME_EPISODE")
public class AnimeEpisodeModel {

    @Id
    @GeneratedValue(generator = "suuid_generator")
    @GenericGenerator(name = "suuid_generator", strategy = "ysaak.anima.dao.SuuidGenerator")
    @Column(name = "ANEP_ID", nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ANEP_ANIM_ID", nullable = false)
    private AnimeModel anime;

    @Column(name = "ANEP_NUMBER", nullable = false)
    private String number;

    @Column(name = "ANEP_TITLE", nullable = false)
    private String title;

    public AnimeEpisodeModel() {
    }

    public AnimeEpisodeModel(AnimeModel anime, String number, String title) {
        this.id = null;
        this.anime = anime;
        this.number = number;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AnimeModel getAnime() {
        return anime;
    }

    public void setAnime(AnimeModel anime) {
        this.anime = anime;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
