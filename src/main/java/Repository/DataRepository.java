package Repository;

import java.util.Collection;

public interface DataRepository<T extends Collection<?>> {

    T findAll();
}
