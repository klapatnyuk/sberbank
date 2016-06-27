package ru.klapatnyuk.sberbank.model.entity;

import ru.klapatnyuk.sberbank.model.entity.api.Entity;

/**
 * @author klapatnyuk
 */
public abstract class AbstractEntity implements Entity {

    private int id;
    private String title;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Entity that = (Entity) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }

    /**
     * @author klapatnyuk
     */
    public abstract class AbstractBuilder implements EntityBuilder {

        @Override
        public AbstractBuilder setId(int id) {
            AbstractEntity.this.id = id;
            return this;
        }

        @Override
        public AbstractBuilder setTitle(String title) {
            AbstractEntity.this.title = title;
            return this;
        }
    }
}
