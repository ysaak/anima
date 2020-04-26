package ysaak.anima.data;

import org.hibernate.annotations.GenericGenerator;
import ysaak.anima.utils.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;


@Entity
@Table(name = "EPISODE")
public class Episode implements Comparable<Episode> {
    @Id
    @GeneratedValue(generator = "suuid_generator")
    @GenericGenerator(name = "suuid_generator", strategy = "ysaak.anima.dao.SuuidGenerator")
    @Column(name = "EPIS_ID", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "EPIS_SEAS_ID", nullable = false)
    private Season season;

    @Column(name = "EPIS_NUMBER", nullable = false)
    private String number;

    @Column(name = "EPIS_TITLE", nullable = false)
    private String title;

    public Episode() {
    }

    public Episode(String id, String number, String title) {
        this.id = id;
        this.number = number;
        this.title = title;
    }

    public Episode(String number, String title) {
        this.number = number;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
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

    @Override
    public int compareTo(Episode o) {
        String o1 = StringUtils.getNotNull(this.number);
        String o2 = StringUtils.getNotNull(o.number);

        String o1StringPart = o1.replaceAll("\\d", "");
        String o2StringPart = o2.replaceAll("\\d", "");

        if (o1StringPart.equalsIgnoreCase(o2StringPart)) {
            return StringUtils.extractDigits(o1) - StringUtils.extractDigits(o2);
        }
        return o1.compareTo(o2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Episode episode = (Episode) o;
        return Objects.equals(id, episode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
