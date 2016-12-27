package kz.codingwolves.models;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by sagynysh on 12/17/16.
 */
@Entity
@Table(name="courses")
public class Course extends PersistentUnit {

    private Long registrarId;

    private String code;

    private String title;

    public Long getRegistrarId() {
        return registrarId;
    }

    public void setRegistrarId(Long registrarId) {
        this.registrarId = registrarId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
