package ysaak.anima.service.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ysaak.anima.exception.DataValidationException;
import ysaak.anima.utils.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class ValidationService {

    private final Validator validator;

    @Autowired
    public ValidationService(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T object) throws DataValidationException {
        final Set<ConstraintViolation<T>> violationSet = validator.validate(object);

        if (CollectionUtils.isNotEmpty(violationSet)) {
            final Map<String, String> validationErrorMap = new HashMap<>();

            for (ConstraintViolation<T> violation : violationSet) {
                validationErrorMap.put(violation.getPropertyPath().toString(), violation.getMessage());
            }

            throw new DataValidationException(validationErrorMap);
        }
    }
}
