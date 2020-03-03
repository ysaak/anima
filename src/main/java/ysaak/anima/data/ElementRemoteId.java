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
@Table(name = "ELEMENT_REMOTE_ID")
public class ElementRemoteId {
    @Id
    @GeneratedValue(generator = "suuid_generator")
    @GenericGenerator(name = "suuid_generator", strategy = "ysaak.anima.dao.SuuidGenerator")
    @Column(name = "ELRI_ID", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "ELRI_ELEM_ID", nullable = false)
    private Element element;

    @ManyToOne
    @JoinColumn(name = "ELRI_EXSI_ID", nullable = false)
    private ExternalSite externalSite;

    @Column(name = "ELRI_REMOTE_ID", nullable = false)
    private String remoteId;

    public ElementRemoteId() {
    }

    public ElementRemoteId(Element element, ExternalSite externalSite, String remoteId) {
        this.element = element;
        this.externalSite = externalSite;
        this.remoteId = remoteId;
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

    public ExternalSite getExternalSite() {
        return externalSite;
    }

    public void setExternalSite(ExternalSite externalSite) {
        this.externalSite = externalSite;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }
}
