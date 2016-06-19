package ru.klapatnyuk.sberbank.model.entity.api;

import java.io.Serializable;

/**
 * @author klapatnyuk
 */
public interface Entity extends Serializable {

    int getId();

    void setId(int id);

    String getTitle();

    void setTitle(String title);
}
