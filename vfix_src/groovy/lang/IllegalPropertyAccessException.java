package groovy.lang;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class IllegalPropertyAccessException extends MissingPropertyException {
   private static String makeMessage(String propertyName, Class clazz, int modifiers, boolean isField) {
      String access = "private";
      if (Modifier.isProtected(modifiers)) {
         access = "protected";
      }

      if (Modifier.isPublic(modifiers)) {
         access = "public";
      }

      String propertyType = "property";
      if (isField) {
         propertyType = "field";
      }

      return "Can not access the " + access + " " + propertyType + " " + propertyName + " in class " + clazz.getName();
   }

   public IllegalPropertyAccessException(String propertyName, Class clazz, int modifiers) {
      super(makeMessage(propertyName, clazz, modifiers, false), propertyName, clazz);
   }

   public IllegalPropertyAccessException(Field field, Class clazz) {
      super(makeMessage(field.getName(), clazz, field.getModifiers(), true), field.getName(), clazz);
   }
}
