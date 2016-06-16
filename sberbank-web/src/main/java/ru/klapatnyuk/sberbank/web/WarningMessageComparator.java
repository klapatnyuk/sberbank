package ru.klapatnyuk.sberbank.web;

import java.util.Comparator;

/**
 * @author klapatnyuk
 */
public class WarningMessageComparator implements Comparator<WarningMessage> {

    @Override
    public int compare(WarningMessage o1, WarningMessage o2) {
        return o2.getTime().compareTo(o1.getTime());
    }
}
