package org.codehaus.plexus.interpolation;

import org.codehaus.plexus.interpolation.reflection.ReflectionValueExtractor;

public class ObjectBasedValueSource extends AbstractValueSource {
   private final Object root;

   public ObjectBasedValueSource(Object root) {
      super(true);
      this.root = root;
   }

   public Object getValue(String expression) {
      try {
         return ReflectionValueExtractor.evaluate(expression, this.root, false);
      } catch (Exception var3) {
         this.addFeedback("Failed to extract '" + expression + "' from: " + this.root, var3);
         return null;
      }
   }
}
