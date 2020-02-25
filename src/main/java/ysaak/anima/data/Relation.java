package ysaak.anima.data;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "RELATION")
public class Relation {
    @Id
    @GeneratedValue(generator = "suuid_generator")
    @GenericGenerator(name = "suuid_generator", strategy = "ysaak.anima.dao.SuuidGenerator")
    @Column(name = "RELA_ID", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "RELA_ELEM_ID")
    private Element element;

    @ManyToOne
    @JoinColumn(name = "RELA_ELEM_RELATED_ID")
    private Element relatedElement;

    @Column(name = "RELA_TYPE", nullable = false)
    private RelationType type;

    public Relation() {
    }

    public Relation(String id, Element element, Element relatedElement, RelationType type) {
        this.id = id;
        this.element = element;
        this.relatedElement = relatedElement;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Element getRelatedElement() {
        return relatedElement;
    }

    public void setRelatedElement(Element relatedElement) {
        this.relatedElement = relatedElement;
    }

    public RelationType getType() {
        return type;
    }

    public void setType(RelationType type) {
        this.type = type;
    }
}
