package application.database.entities;

import javax.persistence.*;

/**
 * Created by DZONI on 30.10.2016.
 */
@Entity
@Table(name = "account_status", schema = "db", catalog = "")
public class AccountStatusEntity {
    private int id;
    private StatusTypeEntity statusTypeEntity;
    private String description;
    private UserEntity userEntity;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "description", nullable = false, length = 500)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public StatusTypeEntity getStatusTypeEntity() {
        return statusTypeEntity;
    }

    public void setStatusTypeEntity(StatusTypeEntity statusTypeEntity) {
        this.statusTypeEntity = statusTypeEntity;
    }

    @OneToOne(fetch = FetchType.LAZY)
    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountStatusEntity that = (AccountStatusEntity) o;

        if (id != that.id) return false;
        if (statusTypeEntity != null ? !statusTypeEntity.equals(that.statusTypeEntity) : that.statusTypeEntity != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return userEntity != null ? userEntity.equals(that.userEntity) : that.userEntity == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (statusTypeEntity != null ? statusTypeEntity.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (userEntity != null ? userEntity.hashCode() : 0);
        return result;
    }
}
