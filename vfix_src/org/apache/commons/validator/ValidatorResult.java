package org.apache.commons.validator;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ValidatorResult implements Serializable {
   protected Map hAction = new HashMap();
   protected Field field = null;

   public ValidatorResult(Field field) {
      this.field = field;
   }

   public void add(String validatorName, boolean result) {
      this.add(validatorName, result, (Object)null);
   }

   public void add(String validatorName, boolean result, Object value) {
      this.hAction.put(validatorName, new ValidatorResult.ResultStatus(result, value));
   }

   public boolean containsAction(String validatorName) {
      return this.hAction.containsKey(validatorName);
   }

   public boolean isValid(String validatorName) {
      ValidatorResult.ResultStatus status = (ValidatorResult.ResultStatus)this.hAction.get(validatorName);
      return status == null ? false : status.isValid();
   }

   public Object getResult(String validatorName) {
      ValidatorResult.ResultStatus status = (ValidatorResult.ResultStatus)this.hAction.get(validatorName);
      return status == null ? null : status.getResult();
   }

   public Map getActionMap() {
      return Collections.unmodifiableMap(this.hAction);
   }

   public Field getField() {
      return this.field;
   }

   protected class ResultStatus implements Serializable {
      private boolean valid = false;
      private Object result = null;

      public ResultStatus(boolean valid, Object result) {
         this.valid = valid;
         this.result = result;
      }

      public boolean isValid() {
         return this.valid;
      }

      public void setValid(boolean valid) {
         this.valid = valid;
      }

      public Object getResult() {
         return this.result;
      }

      public void setResult(Object result) {
         this.result = result;
      }
   }
}
