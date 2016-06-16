package ru.klapatnyuk.sberbank.web;

/**
 * @author klapatnyuk
 */
public interface RemoteServiceSession {

    /**
     * @return current backend sessionId
     */
    String getBackendSessionId();
}
