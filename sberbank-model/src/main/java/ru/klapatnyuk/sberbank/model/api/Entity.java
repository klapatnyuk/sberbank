package ru.klapatnyuk.sberbank.model.api;

import java.io.Serializable;

/**
 * @author klapatnyuk
 */
public interface Entity extends Serializable {

    int getId();

    void setId(int id);
}
