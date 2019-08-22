package application.database.initialData.initialData;

/**
 * Created by DZONI on 08.11.2016.
 */
public enum InitialAccountTypes {
    STANDARD_USER(1, "Standard user"),
    PREMIUM_USER(2, "Premium user"),
    STANDARD_COMPANY(3, "Standard company"),
    PREMIUM_COMPANY(4, "Premium company");

    private int id;
    private String name;

    InitialAccountTypes(int id, String name) {
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
