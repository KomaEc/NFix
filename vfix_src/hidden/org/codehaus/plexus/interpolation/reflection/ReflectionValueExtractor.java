package hidden.org.codehaus.plexus.interpolation.reflection;

import hidden.org.codehaus.plexus.interpolation.util.StringUtils;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.WeakHashMap;

public class ReflectionValueExtractor {
   private static final Class[] CLASS_ARGS = new Class[0];
   private static final Object[] OBJECT_ARGS = new Object[0];
   private static final Map classMaps = new WeakHashMap();

   private ReflectionValueExtractor() {
   }

   public static Object evaluate(String expression, Object root) throws Exception {
      return evaluate(expression, root, true);
   }

   public static Object evaluate(String expression, Object root, boolean trimRootToken) throws Exception {
      if (trimRootToken) {
         expression = expression.substring(expression.indexOf(46) + 1);
      }

      Object value = root;

      Method method;
      for(StringTokenizer parser = new StringTokenizer(expression, "."); parser.hasMoreTokens(); value = method.invoke(value, OBJECT_ARGS)) {
         String token = parser.nextToken();
         if (value == null) {
            return null;
         }

         ClassMap classMap = getClassMap(value.getClass());
         String methodBase = StringUtils.capitalizeFirstLetter(token);
         String methodName = "get" + methodBase;
         method = classMap.findMethod(methodName, CLASS_ARGS);
         if (method == null) {
            methodName = "is" + methodBase;
            method = classMap.findMethod(methodName, CLASS_ARGS);
         }

         if (method == null) {
            return null;
         }
      }

      return value;
   }

   private static ClassMap getClassMap(Class clazz) {
      ClassMap classMap = (ClassMap)classMaps.get(clazz);
      if (classMap == null) {
         classMap = new ClassMap(clazz);
         classMaps.put(clazz, classMap);
      }

      return classMap;
   }
}
