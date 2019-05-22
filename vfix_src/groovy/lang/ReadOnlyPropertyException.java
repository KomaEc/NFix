package groovy.lang;

public class ReadOnlyPropertyException extends MissingPropertyException {
   public ReadOnlyPropertyException(String property, Class type) {
      super("Cannot set readonly property: " + property + " for class: " + type.getName(), property, type);
   }

   public ReadOnlyPropertyException(String property, String classname) {
      super("Cannot set readonly property: " + property + " for class: " + classname);
   }
}
