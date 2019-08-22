package application.database.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by DZONI on 10.12.2016.
 */

@Entity
@Table(name = "token_access", schema = "db", catalog = "")
public class TokenAccessEntity {
    private int id;
    private String token;
    private Date expirationDate;
    private UserEntity userEntity;

    @GenericGenerator(name = "userGenerator", strategy = "foreign",
            parameters = @org.hibernate.annotations.Parameter(name = "property", value = "userEntity"))
    @Id
    @GeneratedValue(generator = "userGenerator")
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "token", nullable = false)
    public String getToken() {
        return token;
    }

    public void setToken(String id) {
        this.token = id;
    }

    @Basic
    @Column(name = "expirationDate", nullable = false, columnDefinition = "DATETIME")
    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
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

        TokenAccessEntity that = (TokenAccessEntity) o;

        if (id != that.id) return false;
        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        if (expirationDate != null ? !expirationDate.equals(that.expirationDate) : that.expirationDate != null)
            return false;
        return userEntity != null ? userEntity.equals(that.userEntity) : that.userEntity == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (expirationDate != null ? expirationDate.hashCode() : 0);
        result = 31 * result + (userEntity != null ? userEntity.hashCode() : 0);
        return result;
    }
}
