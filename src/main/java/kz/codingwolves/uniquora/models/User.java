package kz.codingwolves.uniquora.models;

import kz.codingwolves.uniquora.enums.Message;
import kz.codingwolves.uniquora.enums.School;

import javax.persistence.*;

/**
 * Created by sagynysh on 12/17/16.
 */
@Entity
@Table(name="users")
public class User extends PersistentUnit {

    @Column(name="fullname_")
    private String fullname;

    @Column(name="id_number_")
    private String registrarId;

    @Column(name="password_")
    private String password;

    @Column(name="email_")
    private String email;

    @Column(name="rating_")
    private Integer rating;

    @Column(name="school")
    @Enumerated(EnumType.STRING)
    private School school;

    @Column(name="is_registered_")
    private Boolean registered;

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public String getRegistrarId() {
        return registrarId;
    }

    public void setRegistrarId(String registrarId) {
        this.registrarId = registrarId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isRegistered() {
        return registered;
    }

    public void setRegistered(Boolean registered) {
        this.registered = registered;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws Exception {
        if (password == null || password.length() < 8) {
            throw new Exception(Message.incorrectformat.toString());
        }
        this.password = password;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}