package kz.codingwolves.uniquora.models;

import kz.codingwolves.uniquora.enums.Messages;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by sagynysh on 1/3/17.
 */
@Entity
@Table(name="confirmations")
public class Confirmation {

    @Id
    @Column(name="id_")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_")
    private User user;

    @Column(name="password_candidate_")
    private String passwordCandidate;

    @Column(name="code_")
    private String code;

    @Column(name="is_active_")
    private Boolean isActive;

    @Column(name="created_date_")
    private Date createdDate;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPasswordCandidate() {
        return passwordCandidate;
    }

    public void setPasswordCandidate(String passwordCandidate) throws Exception {
        if (passwordCandidate == null || passwordCandidate.length() < 9) {
            throw new Exception(Messages.incorrectformat.toString());
        }
        this.passwordCandidate = passwordCandidate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
