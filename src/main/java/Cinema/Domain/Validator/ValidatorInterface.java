package Cinema.Domain.Validator;

import Cinema.Domain.AbstractEntity;

/**
 * Interface for all entity validators.
 */
public interface ValidatorInterface {
    /**
     * Validates the given entity.
     * @param entity
     */
    void validate(AbstractEntity entity);
}
