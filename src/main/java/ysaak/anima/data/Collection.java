package ysaak.anima.data;

import org.hibernate.annotations.GenericGenerator;
import ysaak.anima.service.validation.ValidationMessages;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "COLLECTION")
public class Collection {
    @Id
    @GeneratedValue(generator = "suuid_generator")
    @GenericGenerator(name = "suuid_generator", strategy = "ysaak.anima.dao.SuuidGenerator")
    @Column(name = "COLL_ID", nullable = false)
    private String id;

    @Column(name = "COLL_NAME", nullable = false)
    @Size(max = 250, message = ValidationMessages.MAX_LENGTH)
    private String name;

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
}
