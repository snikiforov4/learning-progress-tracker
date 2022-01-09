package tracker.validate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class ValidationResult {

    final ValidationStatus status;

    public static ValidationResult fromStatus(ValidationStatus status) {
        return new ValidationResult(status);
    }

}
