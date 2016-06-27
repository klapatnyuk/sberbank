package ru.klapatnyuk.sberbank.model.entity.api;

import java.io.Serializable;

/**
 * @author klapatnyuk
 */
public interface Entity extends Serializable {

    int getId();

    String getTitle();

    /**
     * @author klapatnyuk
     */
    interface EntityBuilder {

        EntityBuilder setId(int id);

        EntityBuilder setTitle(String title);

        Entity build();
    }
}
