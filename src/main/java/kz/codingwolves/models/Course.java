package kz.codingwolves.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by sagynysh on 12/17/16.
 */
@Entity
@Table(name="courses")
public class Course extends PersistentUnit {

    @Column(name="registrar_id_")
    private Long registrarId;

    @Column(name="course_code_")
    private String code;

    @Column(name="title_")
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
