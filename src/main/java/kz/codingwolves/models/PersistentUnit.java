package kz.codingwolves.models;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by sagynysh on 12/17/16.
 */
@MappedSuperclass
public class PersistentUnit {

    @Id
    @Column(name="id_")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="removed_")
    private Boolean removed;

    @Column(name="createdDate_")
    private Date createdDate;

    @Column(name="modifiedDate_")
    private Date modifiedDate;

    public Boolean getRemoved() {
        return removed;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
