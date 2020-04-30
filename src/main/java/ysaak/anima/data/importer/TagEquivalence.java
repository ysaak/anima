package ysaak.anima.data.importer;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "TAG_EQUIVALENCE")
public class TagEquivalence {

    @Id
    @GeneratedValue(generator = "suuid_generator")
    @GenericGenerator(name = "suuid_generator", strategy = "ysaak.anima.dao.SuuidGenerator")
    @Column(name = "TAEQ_ID", nullable = false)
    private String id;

    @Column(name = "TAEQ_TAG_ID", nullable = false)
    private String tagId;

    @Column(name = "TAEQ_ORIGIN", nullable = false)
    private Importer importer;

    @Column(name = "TAEQ_EQUIVALENCE", nullable = false)
    private String equivalence;

    public TagEquivalence() {
    }

    public TagEquivalence(String tagId, Importer importer, String equivalence) {
        this.tagId = tagId;
        this.importer = importer;
        this.equivalence = equivalence;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public Importer getImporter() {
        return importer;
    }

    public void setImporter(Importer importer) {
        this.importer = importer;
    }

    public String getEquivalence() {
        return equivalence;
    }

    public void setEquivalence(String equivalence) {
        this.equivalence = equivalence;
    }
}
