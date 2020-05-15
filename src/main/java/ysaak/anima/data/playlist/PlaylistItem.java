package ysaak.anima.data.playlist;

import org.hibernate.annotations.GenericGenerator;
import ysaak.anima.data.Element;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "PLAYLIST_ITEM")
public class PlaylistItem {
    @Id
    @GeneratedValue(generator = "suuid_generator")
    @GenericGenerator(name = "suuid_generator", strategy = "ysaak.anima.dao.SuuidGenerator")
    @Column(name = "PLIT_ID", nullable = false)
    private String id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "PLAYLIST_ITEM_USER", joinColumns = @JoinColumn(name = "PLIU_PLIT_ID"))
    @Column(name = "PLIU_USER_ID")
    private List<String> userIdList;

    @ManyToOne
    @JoinColumn(name = "PLIT_ELEM_ID", nullable = false)
    private Element element;

    @Column(name = "PLIT_STATUS", nullable = false)
    private PlaylistItemStatus status;

    @Column(name = "PLIT_START_DATE")
    private LocalDate startDate;

    @Column(name = "PLIT_END_DATE")
    private LocalDate endDate;

    @Column(name = "PLIT_CURRENT_EPISODE")
    private Integer currentEpisode;

    @Column(name = "PLIT_LAST_UPDATE")
    private LocalDateTime lastUpdateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public PlaylistItemStatus getStatus() {
        return status;
    }

    public void setStatus(PlaylistItemStatus status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getCurrentEpisode() {
        return currentEpisode;
    }

    public void setCurrentEpisode(Integer currentEpisode) {
        this.currentEpisode = currentEpisode;
    }

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
