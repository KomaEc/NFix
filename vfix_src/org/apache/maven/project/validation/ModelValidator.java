package org.apache.maven.project.validation;

import org.apache.maven.model.Model;

public interface ModelValidator {
   String ROLE = ModelValidator.class.getName();

   ModelValidationResult validate(Model var1);
}
