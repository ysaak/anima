package ysaak.anima.data;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "EXTERNAL_SITE")
public class ExternalSite {
    @Id
    @GeneratedValue(generator = "suuid_generator")
    @GenericGenerator(name = "suuid_generator", strategy = "ysaak.anima.dao.SuuidGenerator")
    @Column(name = "EXSI_ID", nullable = false)
    private String id;

    @Column(name = "EXSI_CODE", nullable = false)
    private String code;

    @Column(name = "EXSI_SITE_NAME", nullable = false)
    private String siteName;

    @Column(name = "EXSI_URL_TEMPLATE", nullable = false)
    private String urlTemplate;

    @OneToMany(mappedBy = "element", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ElementRemoteId> elementRemoteIdList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getUrlTemplate() {
        return urlTemplate;
    }

    public void setUrlTemplate(String urlTemplate) {
        this.urlTemplate = urlTemplate;
    }

    public List<ElementRemoteId> getElementRemoteIdList() {
        return elementRemoteIdList;
    }

    public void setElementRemoteIdList(List<ElementRemoteId> elementRemoteIdList) {
        this.elementRemoteIdList = elementRemoteIdList;
    }
}
