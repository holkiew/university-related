package application.database.initialData.initialData;

/**
 * Created by DZONI on 08.11.2016.
 */
public enum InitialCategories {
    HEAVY_TRANSPORT(1, "Heavy transport"),
    LIGHT_TRANSPORT(2, "Light transport"),
    STOCK(3, "Stock"),
    LIVING(4, "Stock"),
    FRAGILE(5, "Fragile"),
    SPECIAL_ENVIRONMENT(6, "Special environment");

    private int id;
    private String name;

    InitialCategories(int id, String name) {
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