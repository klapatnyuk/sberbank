package ru.klapatnyuk.sberbank.web;

/**
 * @author klapatnyuk
 */
public enum StylePostfix {
    PX("px");

    private String sequence;

    private StylePostfix(String sequence) {
        this.sequence = sequence;
    }

    @Override
    public String toString() {
        return this.sequence;
    }
}
