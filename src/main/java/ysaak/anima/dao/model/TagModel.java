package ysaak.anima.dao.model;

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
@Table(name = "TAG")
public class TagModel {

    @Id
    @GeneratedValue(generator = "suuid_generator")
    @GenericGenerator(name = "suuid_generator", strategy = "ysaak.anima.dao.SuuidGenerator")
    @Column(name = "TAG_ID", nullable = false)
    private String id;

    @Column(name = "TAG_NAME", nullable = false)
    private String name;

    @Column(name = "TAG_DESCRIPTION", nullable = false)
    private String description;

    @OneToMany(mappedBy="tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TagEquivalenceModel> equivalenceList;

    public TagModel() {
    }

    public TagModel(String id) {
        this.id = id;
    }

    public TagModel(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TagEquivalenceModel> getEquivalenceList() {
        return equivalenceList;
    }

    public void setEquivalenceList(List<TagEquivalenceModel> equivalenceList) {
        this.equivalenceList = equivalenceList;
    }
}
