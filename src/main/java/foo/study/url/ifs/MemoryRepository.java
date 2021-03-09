package foo.study.url.ifs;

import java.util.Optional;

public interface MemoryRepository<E, ID>{
    Optional<E> findById(ID id);

    E save(E e);

    void delete(E entity);

    void deleteById(ID id);

    boolean existsById(ID id);

    long count();
}
