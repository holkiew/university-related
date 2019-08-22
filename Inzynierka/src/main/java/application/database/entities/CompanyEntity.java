package application.database.entities;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by DZONI on 30.10.2016.
 */
@Entity
@Table(name = "company", schema = "db", catalog = "")
public class CompanyEntity {
    private int id;
    private String username;
    private String password;
    private byte suspended;
    private Set<UserEntity> userEntities;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username", nullable = false, length = 20)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 30)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "suspended", nullable = false)
    public byte getSuspended() {
        return suspended;
    }

    public void setSuspended(byte suspended) {
        this.suspended = suspended;
    }

    @OneToMany(mappedBy = "companyEntity", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    public Set<UserEntity> getUserEntities() {
        return userEntities;
    }

    public void setUserEntities(Set<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanyEntity that = (CompanyEntity) o;

        if (id != that.id) return false;
        if (suspended != that.suspended) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (int) suspended;
        return result;
    }
}
