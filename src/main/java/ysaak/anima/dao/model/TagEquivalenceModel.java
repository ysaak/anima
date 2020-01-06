package ysaak.anima.dao.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "TAG_EQUIVALENCE")
public class TagEquivalenceModel {

    @Id
    @GeneratedValue(generator = "suuid_generator")
    @GenericGenerator(name = "suuid_generator", strategy = "ysaak.anima.dao.SuuidGenerator")
    @Column(name = "TAEQ_ID", nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TAEQ_TAG_ID", nullable = false)
    private TagModel tag;

    @Column(name = "TAEQ_ORIGIN", nullable = false)
    private String origin;

    @Column(name = "TAEQ_EQUIVALENCE", nullable = false)
    private String equivalence;

    public TagEquivalenceModel() {
    }

    public TagEquivalenceModel(TagModel tag, String origin, String equivalence) {
        this.tag = tag;
        this.origin = origin;
        this.equivalence = equivalence;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TagModel getTag() {
        return tag;
    }

    public void setTag(TagModel tag) {
        this.tag = tag;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getEquivalence() {
        return equivalence;
    }

    public void setEquivalence(String equivalence) {
        this.equivalence = equivalence;
    }
}
