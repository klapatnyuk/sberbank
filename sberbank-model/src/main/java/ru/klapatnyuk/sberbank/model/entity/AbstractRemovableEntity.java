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

    @Deprecated
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @author klapatnyuk
     */
    public abstract class AbstractRemovableBuilder extends AbstractBuilder implements RemovableBuilder {

        @Override
        public AbstractRemovableBuilder setActive(boolean active) {
            AbstractRemovableEntity.this.active = active;
            return this;
        }
    }
}
