package application.database.initialData.initialData;

/**
 * Created by DZONI on 08.11.2016.
 */
public enum InitialStatusTypes {
    ACCOUNT_NOT_ACTIVATED(1, "Account not activated"),
    ACCOUNT_ACTIVE(2, "Active account"),
    ACCOUNT_BANNED(3, "Account banned"),
    ACCOUNT_SUSPENDED(4, "Account suspended");

    private int id;
    private String name;

    InitialStatusTypes(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
