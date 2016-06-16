package ru.klapatnyuk.sberbank.model;

/**
 * @author klapatnyuk
 */
public class Document extends Template {

    private Template template;
    private User owner;

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
