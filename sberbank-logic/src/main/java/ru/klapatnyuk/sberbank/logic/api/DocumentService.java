package ru.klapatnyuk.sberbank.logic.api;

import ru.klapatnyuk.sberbank.model.entity.Document;

import java.util.Set;

/**
 * @author klapatnyuk
 */
public interface DocumentService extends BusinessService {

    Set<Document> getDocuments();

    Set<Document> getDocuments(boolean active);

    Document getDocument(int id);

    int createDocument(Document document);

    boolean updateDocument(int id, Document document);

    boolean removeDocument(int id);
}
