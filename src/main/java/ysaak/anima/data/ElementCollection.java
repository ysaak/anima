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
@Table(name = "ELEMENT_COLLECTION")
public class ElementCollection {
    @Id
    @GeneratedValue(generator = "suuid_generator")
    @GenericGenerator(name = "suuid_generator", strategy = "ysaak.anima.dao.SuuidGenerator")
    @Column(name = "ELCO_ID", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "ELCO_ELEM_ID", nullable = false)
    private Element element;

    @ManyToOne
    @JoinColumn(name = "ELCO_COLL_ID", nullable = false)
    private Collection collection;

    public ElementCollection() {
    }

    public ElementCollection(Element element, Collection collection) {
        this.element = element;
        this.collection = collection;
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

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }
}
