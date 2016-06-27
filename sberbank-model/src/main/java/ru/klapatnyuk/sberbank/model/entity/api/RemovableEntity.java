package ru.klapatnyuk.sberbank.model.entity.api;

/**
 * @author klapatnyuk
 */
public interface RemovableEntity extends Entity {

    boolean isActive();

    /**
     * @author klapatnyuk
     */
    interface RemovableBuilder extends EntityBuilder {

        RemovableBuilder setActive(boolean active);
    }
}
