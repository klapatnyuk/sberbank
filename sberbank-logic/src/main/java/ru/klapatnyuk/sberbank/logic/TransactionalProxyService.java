package ru.klapatnyuk.sberbank.logic;

import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import ru.klapatnyuk.sberbank.logic.api.BusinessService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * @author klapatnyuk
 */
public class TransactionalProxyService implements InvocationHandler {

    private final BusinessService service;
    private final JDBCConnectionPool connectionPool;

    private TransactionalProxyService(BusinessService service, JDBCConnectionPool connectionPool) {
        this.service = service;
        this.connectionPool = connectionPool;
    }

    @SuppressWarnings("unchecked")
    public static <T extends BusinessService> T newInstance(
            T service, JDBCConnectionPool connectionPool, Class<T> interfaces, ClassLoader classLoader) {
        return (T) Proxy.newProxyInstance(classLoader, new Class[]{interfaces},
                new TransactionalProxyService(service, connectionPool));
    }

    public static <T extends BusinessService> T newInstance(
            T service, JDBCConnectionPool connectionPool, Class<T> interfaces) {
        return newInstance(service, connectionPool, interfaces, ClassLoader.getSystemClassLoader());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
        Connection connection = null;
        try {
            connection = connectionPool.reserveConnection();
            connection.setAutoCommit(false);
            service.setConnection(connection);

            Object invoke = method.invoke(service, args);

            connection.commit();
            return invoke;

        } catch (Throwable t) {
            if (connection != null) {
                connection.rollback();
            }
            throw t;

        } finally {
            connectionPool.releaseConnection(connection);
        }
    }
}
