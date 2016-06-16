package ru.klapatnyuk.sberbank.model.entity;

import ru.klapatnyuk.sberbank.model.entity.api.RemovableEntity;

/**
 * @author klapatnyuk
 */
public abstract class AbstractRemovableEntity extends AbstractEntity implements RemovableEntity {

    private boolean active = true;

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
}
