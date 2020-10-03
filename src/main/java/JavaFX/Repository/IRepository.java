package JavaFX.Repository;

import JavaFX.Domain.Entity;

import java.util.List;

public interface IRepository<T extends Entity> {

     void create(T entity);
     void recreate(T entity);

     T read(int entityId);

     List<T> read();

     void update(T entity);

     void delete(int entityId);

}
