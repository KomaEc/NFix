package org.codehaus.groovy.transform.powerassert;

import java.util.ArrayList;
import java.util.List;

public class ValueRecorder {
   public static final String RECORD_METHOD_NAME = "record";
   public static final String CLEAR_METHOD_NAME = "clear";
   private final List<Value> values = new ArrayList();

   public void clear() {
      this.values.clear();
   }

   public Object record(Object value, int anchor) {
      this.values.add(new Value(value, anchor));
      return value;
   }

   public List<Value> getValues() {
      return this.values;
   }
}
