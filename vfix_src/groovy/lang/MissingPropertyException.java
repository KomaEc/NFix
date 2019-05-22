package groovy.lang;

import org.codehaus.groovy.runtime.MethodRankHelper;

public class MissingPropertyException extends GroovyRuntimeException {
   public static final Object MPE = new Object();
   private final String property;
   private final Class type;

   public MissingPropertyException(String property, Class type) {
      this.property = property;
      this.type = type;
   }

   public MissingPropertyException(String property, Class type, Throwable t) {
      super(t);
      this.property = property;
      this.type = type;
   }

   public MissingPropertyException(String message) {
      super(message);
      this.property = null;
      this.type = null;
   }

   public MissingPropertyException(String message, String property, Class type) {
      super(message);
      this.property = property;
      this.type = type;
   }

   public String getMessageWithoutLocationText() {
      Throwable cause = this.getCause();
      if (cause == null) {
         return super.getMessageWithoutLocationText() != null ? super.getMessageWithoutLocationText() : "No such property: " + this.property + " for class: " + this.type.getName() + MethodRankHelper.getPropertySuggestionString(this.property, this.type);
      } else {
         return "No such property: " + this.property + " for class: " + this.type.getName() + ". Reason: " + cause;
      }
   }

   public String getProperty() {
      return this.property;
   }

   public Class getType() {
      return this.type;
   }
}
