package application.database.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by DZONI on 30.10.2016.
 */
@Entity
@Table(name = "language", schema = "db", catalog = "")
public class LanguageEntity {
    private int id;
    private String name;
    private Set<UserEntity> userEntities;

    public LanguageEntity() {
    }

    public LanguageEntity(int id, String name) {
        this.id = id;
        this.name = name;
        this.userEntities = new HashSet<>();
    }

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "languageEntity", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
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

        LanguageEntity that = (LanguageEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return userEntities != null ? userEntities.equals(that.userEntities) : that.userEntities == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (userEntities != null ? userEntities.hashCode() : 0);
        return result;
    }
}
