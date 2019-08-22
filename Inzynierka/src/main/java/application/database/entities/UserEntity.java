package application.database.entities;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by DZONI on 30.10.2016.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = UserEntity.GET_USER_BY_ID, query = "FROM UserEntity user WHERE user.id = :id"),
        @NamedQuery(name = UserEntity.GET_USER_BY_PASSWORD_AND_LOGIN, query = "FROM UserEntity user WHERE user.username = :username AND user.password = :password"),
        @NamedQuery(name = UserEntity.GET_USER_BY_TOKEN_REFRESH, query = "FROM TokenRefreshEntity token WHERE token.token = :token"),
        @NamedQuery(name = UserEntity.GET_USER_BY_TOKEN_ACCESS, query = "FROM TokenAccessEntity token WHERE token.token = :token")
})
@Table(name = "user", schema = "db", catalog = "")
public class UserEntity {
    public static final String GET_USER_BY_ID = "GET_USER_BY_ID";
    public static final String GET_USER_BY_PASSWORD_AND_LOGIN = "GET_USER_BY_PASSWORD_AND_LOGIN";
    public static final String GET_USER_BY_TOKEN_REFRESH = "GET_USER_BY_TOKEN_REFRESH";
    public static final String GET_USER_BY_TOKEN_ACCESS = "GET_USER_BY_TOKEN_ACCESS";
    private int id;
    private String username;
    private String password;
    private boolean suspended;
    private String email;
    private UserDataEntity userDataEntity;
    private CompanyEntity companyEntity;
    private AccountTypeEntity accountTypeEntity;
    private LanguageEntity languageEntity;
    private TokenRefreshEntity refreshTokenEntity;
    private TokenAccessEntity accessTokenEntity;
    private AccountTemporaryDataEntity accountTemporaryDataEntity;
    private AccountStatusEntity accountStatusEntity;
    private Set<AuctionEntity> wonAuctionEntities;
    private Set<AuctionEntity> offeredAuctionEntities;
    private Set<BidsEntity> bidsEntities;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "hibernate_user_generator", allocationSize = 1)
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

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "userEntity", cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    public AccountTemporaryDataEntity getAccountTemporaryDataEntity() {
        return accountTemporaryDataEntity;
    }

    public void setAccountTemporaryDataEntity(AccountTemporaryDataEntity accountTemporaryDataEntity) {
        this.accountTemporaryDataEntity = accountTemporaryDataEntity;
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
    public boolean getSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    @Basic
    @Column(name = "email", nullable = false, length = 30)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "userEntity", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    public UserDataEntity getUserDataEntity() {
        return userDataEntity;
    }

    public void setUserDataEntity(UserDataEntity userDataEntity) {
        this.userDataEntity = userDataEntity;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public CompanyEntity getCompanyEntity() {
        return companyEntity;
    }

    public void setCompanyEntity(CompanyEntity companyEntity) {
        this.companyEntity = companyEntity;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public AccountTypeEntity getAccountTypeEntity() {
        return accountTypeEntity;
    }

    public void setAccountTypeEntity(AccountTypeEntity accountTypeEntity) {
        this.accountTypeEntity = accountTypeEntity;
    }

    @ManyToOne
    public LanguageEntity getLanguageEntity() {
        return languageEntity;
    }

    public void setLanguageEntity(LanguageEntity languageEntity) {
        this.languageEntity = languageEntity;
    }

    @OneToMany(mappedBy = "wonUserEntity", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    public Set<AuctionEntity> getWonAuctionEntities() {
        return wonAuctionEntities;
    }

    public void setWonAuctionEntities(Set<AuctionEntity> wonAuctionEntities) {
        this.wonAuctionEntities = wonAuctionEntities;
    }

    @OneToMany(mappedBy = "ownerUserEntity", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    public Set<AuctionEntity> getOfferedAuctionEntities() {
        return offeredAuctionEntities;
    }

    public void setOfferedAuctionEntities(Set<AuctionEntity> offeredAuctionEntities) {
        this.offeredAuctionEntities = offeredAuctionEntities;
    }

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    public Set<BidsEntity> getBidsEntities() {
        return bidsEntities;
    }

    public void setBidsEntities(Set<BidsEntity> bidsEntities) {
        this.bidsEntities = bidsEntities;
    }


    @OneToOne(fetch = FetchType.LAZY, mappedBy = "userEntity", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public AccountStatusEntity getAccountStatusEntity() {
        return accountStatusEntity;
    }

    public void setAccountStatusEntity(AccountStatusEntity accountStatusEntity) {
        this.accountStatusEntity = accountStatusEntity;
    }

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "userEntity", cascade = {CascadeType.ALL})
    public TokenRefreshEntity getRefreshTokenEntity() {
        return refreshTokenEntity;
    }

    public void setRefreshTokenEntity(TokenRefreshEntity refreshTokenEntity) {
        this.refreshTokenEntity = refreshTokenEntity;
    }

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "userEntity", cascade = {CascadeType.ALL})
    public TokenAccessEntity getAccessTokenEntity() {
        return accessTokenEntity;
    }

    public void setAccessTokenEntity(TokenAccessEntity accessTokenEntity) {
        this.accessTokenEntity = accessTokenEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (id != that.id) return false;
        if (suspended != that.suspended) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (userDataEntity != null ? !userDataEntity.equals(that.userDataEntity) : that.userDataEntity != null)
            return false;
        if (companyEntity != null ? !companyEntity.equals(that.companyEntity) : that.companyEntity != null)
            return false;
        if (accountTypeEntity != null ? !accountTypeEntity.equals(that.accountTypeEntity) : that.accountTypeEntity != null)
            return false;
        if (languageEntity != null ? !languageEntity.equals(that.languageEntity) : that.languageEntity != null)
            return false;
        if (refreshTokenEntity != null ? !refreshTokenEntity.equals(that.refreshTokenEntity) : that.refreshTokenEntity != null)
            return false;
        if (accessTokenEntity != null ? !accessTokenEntity.equals(that.accessTokenEntity) : that.accessTokenEntity != null)
            return false;
        if (accountTemporaryDataEntity != null ? !accountTemporaryDataEntity.equals(that.accountTemporaryDataEntity) : that.accountTemporaryDataEntity != null)
            return false;
        if (accountStatusEntity != null ? !accountStatusEntity.equals(that.accountStatusEntity) : that.accountStatusEntity != null)
            return false;
        if (wonAuctionEntities != null ? !wonAuctionEntities.equals(that.wonAuctionEntities) : that.wonAuctionEntities != null)
            return false;
        if (offeredAuctionEntities != null ? !offeredAuctionEntities.equals(that.offeredAuctionEntities) : that.offeredAuctionEntities != null)
            return false;
        return bidsEntities != null ? bidsEntities.equals(that.bidsEntities) : that.bidsEntities == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (suspended ? 1 : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (userDataEntity != null ? userDataEntity.hashCode() : 0);
        result = 31 * result + (companyEntity != null ? companyEntity.hashCode() : 0);
        result = 31 * result + (accountTypeEntity != null ? accountTypeEntity.hashCode() : 0);
        result = 31 * result + (languageEntity != null ? languageEntity.hashCode() : 0);
        result = 31 * result + (refreshTokenEntity != null ? refreshTokenEntity.hashCode() : 0);
        result = 31 * result + (accessTokenEntity != null ? accessTokenEntity.hashCode() : 0);
        result = 31 * result + (accountTemporaryDataEntity != null ? accountTemporaryDataEntity.hashCode() : 0);
        result = 31 * result + (accountStatusEntity != null ? accountStatusEntity.hashCode() : 0);
        result = 31 * result + (wonAuctionEntities != null ? wonAuctionEntities.hashCode() : 0);
        result = 31 * result + (offeredAuctionEntities != null ? offeredAuctionEntities.hashCode() : 0);
        result = 31 * result + (bidsEntities != null ? bidsEntities.hashCode() : 0);
        return result;
    }
}
