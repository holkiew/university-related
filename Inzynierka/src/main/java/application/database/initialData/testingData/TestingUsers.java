package application.database.initialData.testingData;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by DZONI on 09.11.2016.
 */
public enum TestingUsers {
    USER1("username", "password", "olkiewiczhubert@gmail.com", false, AccessTokens.TOKEN1, RefreshTokens.TOKEN1);

    private String username;
    private String password;
    private String email;
    private boolean suspended;
    private AccessTokens accessToken;
    private RefreshTokens refreshToken;

    TestingUsers(String username, String password, String email, boolean suspended, AccessTokens accessToken, RefreshTokens refreshToken) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.suspended = suspended;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public AccessTokens getAccessToken() {
        return accessToken;
    }

    public RefreshTokens getRefreshToken() {
        return refreshToken;
    }

    public enum AccessTokens {
        TOKEN1("AccessToken1", new Date(Calendar.getInstance().getTimeInMillis()));
        private String token;
        private Date expireDate;

        AccessTokens(String token, Date expireDate) {
            this.token = token;
            this.expireDate = expireDate;
        }

        public String getToken() {
            return token;
        }

        public Date getExpireDate() {
            return expireDate;
        }
    }

    public enum RefreshTokens {
        TOKEN1("RefreshToken1", new Date(Calendar.getInstance().getTimeInMillis()));
        private String token;
        private Date expireDate;

        RefreshTokens(String token, Date expireDate) {
            this.token = token;
            this.expireDate = expireDate;
        }

        public String getToken() {
            return token;
        }

        public Date getExpireDate() {
            return expireDate;
        }
    }
}
