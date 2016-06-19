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
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
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
}
