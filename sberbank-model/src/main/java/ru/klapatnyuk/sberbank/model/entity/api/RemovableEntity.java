package ru.klapatnyuk.sberbank.model.entity.api;

/**
 * @author klapatnyuk
 */
public interface RemovableEntity extends Entity {

    boolean isActive();

    void setActive(boolean active);
}
