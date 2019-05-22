package org.apache.commons.validator;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ValidatorResults implements Serializable {
   protected Map hResults = new HashMap();

   public void merge(ValidatorResults results) {
      this.hResults.putAll(results.hResults);
   }

   public void add(Field field, String validatorName, boolean result) {
      this.add(field, validatorName, result, (Object)null);
   }

   public void add(Field field, String validatorName, boolean result, Object value) {
      ValidatorResult validatorResult = this.getValidatorResult(field.getKey());
      if (validatorResult == null) {
         validatorResult = new ValidatorResult(field);
         this.hResults.put(field.getKey(), validatorResult);
      }

      validatorResult.add(validatorName, result, value);
   }

   public void clear() {
      this.hResults.clear();
   }

   public boolean isEmpty() {
      return this.hResults.isEmpty();
   }

   public ValidatorResult getValidatorResult(String key) {
      return (ValidatorResult)this.hResults.get(key);
   }

   public Set getPropertyNames() {
      return Collections.unmodifiableSet(this.hResults.keySet());
   }

   public Map getResultValueMap() {
      Map results = new HashMap();
      Iterator i = this.hResults.keySet().iterator();

      while(i.hasNext()) {
         String propertyKey = (String)i.next();
         ValidatorResult vr = this.getValidatorResult(propertyKey);
         Map actions = vr.getActionMap();
         Iterator x = actions.keySet().iterator();

         while(x.hasNext()) {
            String actionKey = (String)x.next();
            ValidatorResult.ResultStatus rs = (ValidatorResult.ResultStatus)actions.get(actionKey);
            if (rs != null) {
               Object result = rs.getResult();
               if (result != null && !(result instanceof Boolean)) {
                  results.put(propertyKey, result);
               }
            }
         }
      }

      return results;
   }
}
