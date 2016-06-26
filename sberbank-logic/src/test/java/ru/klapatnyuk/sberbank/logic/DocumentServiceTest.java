package ru.klapatnyuk.sberbank.logic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.klapatnyuk.sberbank.logic.api.DocumentService;
import ru.klapatnyuk.sberbank.model.entity.Document;
import ru.klapatnyuk.sberbank.model.entity.Field;
import ru.klapatnyuk.sberbank.model.exception.BusinessException;
import ru.klapatnyuk.sberbank.model.handler.DocumentFieldHandler;
import ru.klapatnyuk.sberbank.model.handler.DocumentHandler;

import java.sql.SQLException;
import java.util.Collections;

import static org.mockito.Mockito.*;

/**
 * @author klapatnyuk
 */
public class DocumentServiceTest {

    private DocumentService documentService;

    @Before
    public void before() throws SQLException {

        DocumentHandler documentHandler = mock(DocumentHandler.class);
        when(documentHandler.compareEdited(anyInt(), any())).thenReturn(0);
        doNothing().when(documentHandler).update(any());

        DocumentFieldHandler fieldHandler = mock(DocumentFieldHandler.class);
        doNothing().when(fieldHandler).removeExcept(anyInt(), anyListOf(Integer.class));
        doNothing().when(fieldHandler).createOrUpdate(anyInt(), anyListOf(Field.class));

        documentService = new DocumentServiceImpl(documentHandler, fieldHandler);
    }

    @After
    public void after() {
        documentService = null;
    }

    @Test
    public void update() throws BusinessException {
        Field field = mock(Field.class);
        when(field.getId()).thenReturn(1);

        Document document = mock(Document.class);
        when(document.getFields()).thenReturn(Collections.singletonList(field));

        documentService.update(document);
    }
}
