package soot.plugins.internal;

public class ReflectionClassLoadingStrategy implements ClassLoadingStrategy {
   public Object create(String className) throws ClassNotFoundException, InstantiationException {
      Class clazz = Class.forName(className);

      try {
         return clazz.newInstance();
      } catch (IllegalAccessException var4) {
         throw new InstantiationException("Failed to create instance of " + className + " due to access restrictions.");
      }
   }
}
