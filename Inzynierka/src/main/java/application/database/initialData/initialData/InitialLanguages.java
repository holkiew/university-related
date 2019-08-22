package application.database.initialData.initialData;

/**
 * Created by DZONI on 08.11.2016.
 */
public enum InitialLanguages {
    POLISH(1, "Polski"),
    ENGLISH(2, "English");

    private int id;
    private String name;

    InitialLanguages(int id, String name) {
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
