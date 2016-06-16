package ru.klapatnyuk.sberbank.model.api;

/**
 * @author klapatnyuk
 */
public interface RemovableEntity extends Entity {

    boolean isActive();

    void setActive(boolean active);
}
