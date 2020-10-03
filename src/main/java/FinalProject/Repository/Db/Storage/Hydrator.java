package FinalProject.Repository.Db.Storage;

import FinalProject.Annotation.StorageField;
import FinalProject.Domain.Entity.AbstractEntity;
import FinalProject.Exception.CannotHydrateException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Hydrator<E extends AbstractEntity> {

    /**
     * Builds an entity of the given class from the given result set data.
     *
     * @return The hydrated entity.
     */
    public E hydrate(Class<E> classType, ResultSet resultSet) {
        try {
            Constructor<E> constructor = classType.getConstructor();
            E entity = constructor.newInstance();

            List<Field> fields = new ArrayList<>(Arrays.asList(classType.getDeclaredFields()));
            fields.addAll(Arrays.asList(classType.getSuperclass().getDeclaredFields()));

            for (Field field : fields) {
                if (!field.isAnnotationPresent(StorageField.class)) {
                    continue;
                }

                field.setAccessible(true);
                String columnName = field.getAnnotation(StorageField.class).field();
                Object resultValue = resultSet.getObject(columnName);
                field.set(entity, resultValue);
            }

            return entity;
        } catch (Exception e) {
            throw new CannotHydrateException("Could not hydrate entity! " + classType.getName());
        }
    }

    /**
     * Extracts field values from the given entity.
     *
     * @return A map of field names => field values.
     */
    public HashMap<String, Object> extract(E entity) {
        HashMap<String, Object> fields = new HashMap<>();

        try {
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (!field.isAnnotationPresent(StorageField.class)) {
                    continue;
                }

                field.setAccessible(true);
                fields.put(field.getAnnotation(StorageField.class).field(), field.get(entity));
            }
        } catch (Exception e) {
            throw new CannotHydrateException("Could not extract entity fields! " + entity.getClass().getName());
        }

        return fields;
    }
}
