package app.repository;

import java.util.List;

public interface GenericRepository<T> {
    void save(T entity);
    List<T> findAll();
    void updateSchema(); // Reflection-based table sync
}