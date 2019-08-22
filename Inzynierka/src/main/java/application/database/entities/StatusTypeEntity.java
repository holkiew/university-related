package application.database.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by DZONI on 30.10.2016.
 */
@Entity
@Table(name = "account_status_type", schema = "db", catalog = "")
public class StatusTypeEntity {
    private int id;
    private String name;
    private Set<AccountStatusEntity> accountStatusEntities;

    public StatusTypeEntity() {
    }

    public StatusTypeEntity(int id, String name) {
        this.id = id;
        this.name = name;
        this.accountStatusEntities = new HashSet<>();
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
    @Column(name = "name", nullable = false, length = 25)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "statusTypeEntity", cascade = CascadeType.REFRESH)
    public Set<AccountStatusEntity> getAccountStatusEntities() {
        return accountStatusEntities;
    }

    public void setAccountStatusEntities(Set<AccountStatusEntity> accountStatusEntities) {
        this.accountStatusEntities = accountStatusEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatusTypeEntity that = (StatusTypeEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return accountStatusEntities != null ? accountStatusEntities.equals(that.accountStatusEntities) : that.accountStatusEntities == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (accountStatusEntities != null ? accountStatusEntities.hashCode() : 0);
        return result;
    }
}
