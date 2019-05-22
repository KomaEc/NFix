package org.apache.maven.scm.repository;

import java.util.Collections;
import java.util.List;
import org.apache.maven.scm.ScmException;

public class ScmRepositoryException extends ScmException {
   static final long serialVersionUID = -2191549774722212492L;
   private List<String> validationMessages = Collections.emptyList();

   public ScmRepositoryException(String msg) {
      super(msg);
   }

   public ScmRepositoryException(String msg, Throwable cause) {
      super(msg, cause);
   }

   public ScmRepositoryException(String msg, List<String> validationMessages) {
      super(msg);
      this.validationMessages = validationMessages;
   }

   public List<String> getValidationMessages() {
      return this.validationMessages;
   }
}
