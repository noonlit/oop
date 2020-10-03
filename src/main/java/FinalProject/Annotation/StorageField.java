package FinalProject.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

public @interface StorageField {
    /**
     * Used to annotate entity fields that have corresponding storage fields.
     *
     * Example: @StorageField(field = "field_name_in_storage")
     */
    String field();
}