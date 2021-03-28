package foo.study.url.ifs;

import java.util.List;
import java.util.Optional;

public interface MemoryRepository<E, ID> {
    Optional<E> findById(ID id);

    List<E> findAll();

    E save(E e);

    void delete(E entity);

    void deleteById(ID id);

    void deleteAll();

    boolean existsById(ID id);

    long count();
}
