package ru.klapatnyuk.sberbank.logic;

import ru.klapatnyuk.sberbank.logic.api.DocumentService;
import ru.klapatnyuk.sberbank.model.entity.AbstractEntity;
import ru.klapatnyuk.sberbank.model.entity.Document;
import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.model.entity.User;
import ru.klapatnyuk.sberbank.model.exception.BusinessException;
import ru.klapatnyuk.sberbank.model.handler.DocumentHandler;
import ru.klapatnyuk.sberbank.model.handler.FieldHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author klapatnyuk
 */
public class DocumentServiceImpl implements DocumentService {

    private final DocumentHandler documentHandler;
    private final FieldHandler fieldHandler;

    public DocumentServiceImpl(DocumentHandler documentHandler, FieldHandler fieldHandler) {
        this.documentHandler = documentHandler;
        this.fieldHandler = fieldHandler;
    }

    @Override
    public List<Document> getAll() throws BusinessException {
        try {
            return documentHandler.findAll();
        } catch (SQLException e) {
            throw new BusinessException("Documents finding error", e);
        }
    }

    @Override
    public List<Document> get(User owner) throws BusinessException {
        try {
            return documentHandler.findByOwnerId(owner.getId());
        } catch (SQLException e) {
            throw new BusinessException("Documents finding error", e);
        }
    }

    @Override
    public List<Field> getFields(int id) throws BusinessException {
        try {
            return fieldHandler.findByDocumentId(id);
        } catch (SQLException e) {
            throw new BusinessException("Document fields finding error", e);
        }
    }

    @Override
    public void create(Document document) throws BusinessException {
        try {
            int id = documentHandler.createDocument(document);
            fieldHandler.createDocumentFields(id, document.getFields());

        } catch (SQLException e) {
            throw new BusinessException("Document creation error", e);
        }
    }

    @Override
    public void update(Document document) throws BusinessException {
        try {
            // compare edited
            if (documentHandler.compareEdited(document.getId(), document.getEdited()) > 0) {
                throw new SQLException("Concurrency editing detected");
            }

            // update document
            documentHandler.updateDocument(document);

            // remove fields
            List<Integer> ids = document.getFields().stream().map(AbstractEntity::getId).collect(Collectors.toList());
            if (ids.isEmpty()) {
                fieldHandler.removeDocumentFields(document.getId());
            } else {
                fieldHandler.removeDocumentFieldsExcept(document.getId(), ids);
            }

            // update fields
            fieldHandler.insertAndUpdateDocumentFields(document.getId(), document.getFields());

        } catch (SQLException e) {
            throw new BusinessException("Document edition error", e);
        }
    }

    @Override
    public void remove(int id) throws BusinessException {
        try {
            documentHandler.removeDocument(id);
        } catch (SQLException e) {
            throw new BusinessException("Document removing error", e);
        }
    }

    @Override
    public void setConnection(Connection connection) {
        if (documentHandler != null) {
            documentHandler.setConnection(connection);
        }
        if (fieldHandler != null) {
            fieldHandler.setConnection(connection);
        }
    }
}
