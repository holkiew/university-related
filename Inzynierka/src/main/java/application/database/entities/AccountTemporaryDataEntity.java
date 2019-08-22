package application.database.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;

/**
 * Created by DZONI on 06.11.2016.
 */
@NamedQueries({
        @NamedQuery(name = AccountTemporaryDataEntity.GET_USER_TEMP_DATA_BY_ID, query = "FROM AccountTemporaryDataEntity atde WHERE atde.id = :userId")
})
@Entity
@Table(name = "account_temporary_data", schema = "db", catalog = "")
public class AccountTemporaryDataEntity {
    private int id;
    private String activationCode;
    private UserEntity userEntity;

    public static final String GET_USER_TEMP_DATA_BY_ID = "GET_USER_TEMP_DATA_BY_ID";

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
    @Column(name = "activationCode", nullable = false, length = 20)
    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
