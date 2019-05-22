package org.codehaus.groovy.runtime;

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GString;
import groovy.lang.GroovyInterceptable;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovySystem;
import groovy.lang.MetaClass;
import groovy.lang.MetaClassRegistry;
import groovy.lang.MissingMethodException;
import groovy.lang.MissingPropertyException;
import groovy.lang.Range;
import groovy.lang.Script;
import groovy.lang.SpreadMap;
import groovy.lang.SpreadMapEvaluatingException;
import groovy.lang.Tuple;
import groovy.lang.Writable;
import groovy.xml.XmlUtil;
import java.beans.Introspector;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.groovy.runtime.metaclass.MetaClassRegistryImpl;
import org.codehaus.groovy.runtime.metaclass.MissingMethodExecutionFailed;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.runtime.wrappers.PojoWrapper;
import org.codehaus.groovy.transform.powerassert.PowerAssertionError;
import org.codehaus.groovy.util.ReleaseInfo;
import org.w3c.dom.Element;

public class InvokerHelper {
   private static final Object[] EMPTY_MAIN_ARGS = new Object[]{new String[0]};
   public static final Object[] EMPTY_ARGS = new Object[0];
   protected static final Object[] EMPTY_ARGUMENTS;
   protected static final Class[] EMPTY_TYPES;
   public static final MetaClassRegistry metaRegistry;

   public static void removeClass(Class clazz) {
      metaRegistry.removeMetaClass(clazz);
      Introspector.flushFromCaches(clazz);
   }

   public static Object invokeMethodSafe(Object object, String methodName, Object arguments) {
      return object != null ? invokeMethod(object, methodName, arguments) : null;
   }

   public static Object invokeStaticMethod(String klass, String methodName, Object arguments) throws ClassNotFoundException {
      Class type = Class.forName(klass);
      return invokeStaticMethod(type, methodName, arguments);
   }

   public static Object invokeStaticNoArgumentsMethod(Class type, String methodName) {
      return invokeStaticMethod((Class)type, methodName, EMPTY_ARGS);
   }

   public static Object invokeConstructorOf(String klass, Object arguments) throws ClassNotFoundException {
      Class type = Class.forName(klass);
      return invokeConstructorOf(type, arguments);
   }

   public static Object invokeNoArgumentsConstructorOf(Class type) {
      return invokeConstructorOf((Class)type, EMPTY_ARGS);
   }

   public static Object invokeClosure(Object closure, Object arguments) {
      return invokeMethod(closure, "doCall", arguments);
   }

   public static List asList(Object value) {
      if (value == null) {
         return Collections.EMPTY_LIST;
      } else if (value instanceof List) {
         return (List)value;
      } else if (value.getClass().isArray()) {
         return Arrays.asList((Object[])((Object[])value));
      } else if (!(value instanceof Enumeration)) {
         return Collections.singletonList(value);
      } else {
         List answer = new ArrayList();
         Enumeration e = (Enumeration)value;

         while(e.hasMoreElements()) {
            answer.add(e.nextElement());
         }

         return answer;
      }
   }

   public static String toString(Object arguments) {
      if (arguments instanceof Object[]) {
         return toArrayString((Object[])((Object[])arguments));
      } else if (arguments instanceof Collection) {
         return toListString((Collection)arguments);
      } else {
         return arguments instanceof Map ? toMapString((Map)arguments) : format(arguments, false);
      }
   }

   public static String inspect(Object self) {
      return format(self, true);
   }

   public static Object getAttribute(Object object, String attribute) {
      if (object == null) {
         object = NullObject.getNullObject();
      }

      if (object instanceof Class) {
         return metaRegistry.getMetaClass((Class)object).getAttribute(object, attribute);
      } else {
         return object instanceof GroovyObject ? ((GroovyObject)object).getMetaClass().getAttribute(object, attribute) : metaRegistry.getMetaClass(object.getClass()).getAttribute(object, attribute);
      }
   }

   public static void setAttribute(Object object, String attribute, Object newValue) {
      if (object == null) {
         object = NullObject.getNullObject();
      }

      if (object instanceof Class) {
         metaRegistry.getMetaClass((Class)object).setAttribute(object, attribute, newValue);
      } else if (object instanceof GroovyObject) {
         ((GroovyObject)object).getMetaClass().setAttribute(object, attribute, newValue);
      } else {
         metaRegistry.getMetaClass(object.getClass()).setAttribute(object, attribute, newValue);
      }

   }

   public static Object getProperty(Object object, String property) {
      if (object == null) {
         object = NullObject.getNullObject();
      }

      if (object instanceof GroovyObject) {
         GroovyObject pogo = (GroovyObject)object;
         return pogo.getProperty(property);
      } else if (object instanceof Class) {
         Class c = (Class)object;
         return metaRegistry.getMetaClass(c).getProperty(object, property);
      } else {
         return ((MetaClassRegistryImpl)metaRegistry).getMetaClass(object).getProperty(object, property);
      }
   }

   public static Object getPropertySafe(Object object, String property) {
      return object != null ? getProperty(object, property) : null;
   }

   public static void setProperty(Object object, String property, Object newValue) {
      if (object == null) {
         object = NullObject.getNullObject();
      }

      if (object instanceof GroovyObject) {
         GroovyObject pogo = (GroovyObject)object;
         pogo.setProperty(property, newValue);
      } else if (object instanceof Class) {
         metaRegistry.getMetaClass((Class)object).setProperty((Class)object, property, newValue);
      } else {
         ((MetaClassRegistryImpl)GroovySystem.getMetaClassRegistry()).getMetaClass(object).setProperty(object, property, newValue);
      }

   }

   public static void setProperty2(Object newValue, Object object, String property) {
      setProperty(object, property, newValue);
   }

   public static void setGroovyObjectProperty(Object newValue, GroovyObject object, String property) {
      object.setProperty(property, newValue);
   }

   public static Object getGroovyObjectProperty(GroovyObject object, String property) {
      return object.getProperty(property);
   }

   public static void setPropertySafe2(Object newValue, Object object, String property) {
      if (object != null) {
         setProperty2(newValue, object, property);
      }

   }

   public static Closure getMethodPointer(Object object, String methodName) {
      if (object == null) {
         throw new NullPointerException("Cannot access method pointer for '" + methodName + "' on null object");
      } else {
         return new MethodClosure(object, methodName);
      }
   }

   public static Object unaryMinus(Object value) {
      if (value instanceof Integer) {
         Integer number = (Integer)value;
         return -number;
      } else if (value instanceof Long) {
         Long number = (Long)value;
         return -number;
      } else if (value instanceof BigInteger) {
         return ((BigInteger)value).negate();
      } else if (value instanceof BigDecimal) {
         return ((BigDecimal)value).negate();
      } else if (value instanceof Double) {
         Double number = (Double)value;
         return -number;
      } else if (value instanceof Float) {
         Float number = (Float)value;
         return -number;
      } else if (value instanceof Short) {
         Short number = (Short)value;
         return (short)(-number);
      } else if (value instanceof Byte) {
         Byte number = (Byte)value;
         return (byte)(-number);
      } else if (!(value instanceof ArrayList)) {
         return invokeMethod(value, "negative", EMPTY_ARGS);
      } else {
         List newlist = new ArrayList();
         Iterator it = ((ArrayList)value).iterator();

         while(it.hasNext()) {
            newlist.add(unaryMinus(it.next()));
         }

         return newlist;
      }
   }

   public static Object unaryPlus(Object value) {
      if (!(value instanceof Integer) && !(value instanceof Long) && !(value instanceof BigInteger) && !(value instanceof BigDecimal) && !(value instanceof Double) && !(value instanceof Float) && !(value instanceof Short) && !(value instanceof Byte)) {
         if (!(value instanceof ArrayList)) {
            return invokeMethod(value, "positive", EMPTY_ARGS);
         } else {
            List newlist = new ArrayList();
            Iterator it = ((ArrayList)value).iterator();

            while(it.hasNext()) {
               newlist.add(unaryPlus(it.next()));
            }

            return newlist;
         }
      } else {
         return value;
      }
   }

   public static Matcher findRegex(Object left, Object right) {
      String stringToCompare;
      if (left instanceof String) {
         stringToCompare = (String)left;
      } else {
         stringToCompare = toString(left);
      }

      String regexToCompareTo;
      if (right instanceof String) {
         regexToCompareTo = (String)right;
      } else {
         if (right instanceof Pattern) {
            Pattern pattern = (Pattern)right;
            return pattern.matcher(stringToCompare);
         }

         regexToCompareTo = toString(right);
      }

      return Pattern.compile(regexToCompareTo).matcher(stringToCompare);
   }

   public static boolean matchRegex(Object left, Object right) {
      if (left != null && right != null) {
         Pattern pattern;
         if (right instanceof Pattern) {
            pattern = (Pattern)right;
         } else {
            pattern = Pattern.compile(toString(right));
         }

         String stringToCompare = toString(left);
         Matcher matcher = pattern.matcher(stringToCompare);
         RegexSupport.setLastMatcher(matcher);
         return matcher.matches();
      } else {
         return false;
      }
   }

   public static Tuple createTuple(Object[] array) {
      return new Tuple(array);
   }

   public static SpreadMap spreadMap(Object value) {
      if (!(value instanceof Map)) {
         throw new SpreadMapEvaluatingException("Cannot spread the map " + value.getClass().getName() + ", value " + value);
      } else {
         Object[] values = new Object[((Map)value).keySet().size() * 2];
         int index = 0;

         Object key;
         for(Iterator it = ((Map)value).keySet().iterator(); it.hasNext(); values[index++] = ((Map)value).get(key)) {
            key = it.next();
            values[index++] = key;
         }

         return new SpreadMap(values);
      }
   }

   public static List createList(Object[] values) {
      List answer = new ArrayList(values.length);
      answer.addAll(Arrays.asList(values));
      return answer;
   }

   public static Map createMap(Object[] values) {
      Map answer = new LinkedHashMap(values.length / 2);
      int i = 0;

      while(true) {
         while(i < values.length - 1) {
            if (values[i] instanceof SpreadMap && values[i + 1] instanceof Map) {
               Map smap = (Map)values[i + 1];
               Iterator iter = smap.keySet().iterator();

               while(iter.hasNext()) {
                  Object key = iter.next();
                  answer.put(key, smap.get(key));
               }

               i += 2;
            } else {
               answer.put(values[i++], values[i++]);
            }
         }

         return answer;
      }
   }

   public static void assertFailed(Object expression, Object message) {
      if (message != null && !"".equals(message)) {
         throw new AssertionError(message + ". Expression: " + expression);
      } else {
         throw new PowerAssertionError(expression.toString());
      }
   }

   public static Object runScript(Class scriptClass, String[] args) {
      Binding context = new Binding(args);
      Script script = createScript(scriptClass, context);
      return invokeMethod(script, "run", EMPTY_ARGS);
   }

   public static Script createScript(Class scriptClass, Binding context) {
      Script script = null;
      if (scriptClass == null) {
         script = new Script() {
            public Object run() {
               return null;
            }
         };
      } else {
         try {
            final GroovyObject object = (GroovyObject)scriptClass.newInstance();
            if (object instanceof Script) {
               script = (Script)object;
            } else {
               script = new Script() {
                  public Object run() {
                     Object args = this.getBinding().getVariables().get("args");
                     Object argsToPass = InvokerHelper.EMPTY_MAIN_ARGS;
                     if (args != null && args instanceof String[]) {
                        argsToPass = args;
                     }

                     object.invokeMethod("main", argsToPass);
                     return null;
                  }
               };
               setProperties(object, context.getVariables());
            }
         } catch (Exception var4) {
            throw new GroovyRuntimeException("Failed to create Script instance for class: " + scriptClass + ". Reason: " + var4, var4);
         }
      }

      script.setBinding(context);
      return script;
   }

   public static void setProperties(Object object, Map map) {
      MetaClass mc = getMetaClass(object);
      Iterator i$ = map.entrySet().iterator();

      while(i$.hasNext()) {
         Object o = i$.next();
         Entry entry = (Entry)o;
         String key = entry.getKey().toString();
         Object value = entry.getValue();

         try {
            mc.setProperty(object, key, value);
         } catch (MissingPropertyException var9) {
         }
      }

   }

   /** @deprecated */
   public static String getVersion() {
      return ReleaseInfo.getVersion();
   }

   public static void write(Writer out, Object object) throws IOException {
      if (object instanceof String) {
         out.write((String)object);
      } else if (object instanceof Object[]) {
         out.write(toArrayString((Object[])((Object[])object)));
      } else if (object instanceof Map) {
         out.write(toMapString((Map)object));
      } else if (object instanceof Collection) {
         out.write(toListString((Collection)object));
      } else if (object instanceof Writable) {
         Writable writable = (Writable)object;
         writable.writeTo(out);
      } else if (!(object instanceof InputStream) && !(object instanceof Reader)) {
         out.write(toString(object));
      } else {
         Object reader;
         if (object instanceof InputStream) {
            reader = new InputStreamReader((InputStream)object);
         } else {
            reader = (Reader)object;
         }

         char[] chars = new char[8192];

         int i;
         while((i = ((Reader)reader).read(chars)) != -1) {
            out.write(chars, 0, i);
         }

         ((Reader)reader).close();
      }

   }

   public static Iterator<Object> asIterator(Object o) {
      return (Iterator)invokeMethod(o, "iterator", EMPTY_ARGS);
   }

   protected static String format(Object arguments, boolean verbose) {
      return format(arguments, verbose, -1);
   }

   protected static String format(Object arguments, boolean verbose, int maxSize) {
      if (arguments == null) {
         NullObject nullObject = NullObject.getNullObject();
         return (String)nullObject.getMetaClass().invokeMethod(nullObject, "toString", EMPTY_ARGS);
      } else if (arguments.getClass().isArray()) {
         return arguments instanceof char[] ? new String((char[])((char[])arguments)) : format(DefaultTypeTransformation.asCollection(arguments), verbose);
      } else if (arguments instanceof Range) {
         Range range = (Range)arguments;
         return verbose ? range.inspect() : range.toString();
      } else if (arguments instanceof Collection) {
         return formatList((Collection)arguments, verbose, maxSize);
      } else if (arguments instanceof Map) {
         return formatMap((Map)arguments, verbose, maxSize);
      } else if (arguments instanceof Element) {
         return XmlUtil.serialize((Element)arguments);
      } else if (arguments instanceof String) {
         if (verbose) {
            String arg = ((String)arguments).replaceAll("\\n", "\\\\n");
            arg = arg.replaceAll("\\r", "\\\\r");
            arg = arg.replaceAll("\\t", "\\\\t");
            arg = arg.replaceAll("\\f", "\\\\f");
            arg = arg.replaceAll("\\\"", "\\\\\"");
            arg = arg.replaceAll("\\\\", "\\\\");
            return "\"" + arg + "\"";
         } else {
            return (String)arguments;
         }
      } else {
         return arguments.toString();
      }
   }

   private static String formatMap(Map map, boolean verbose, int maxSize) {
      if (map.isEmpty()) {
         return "[:]";
      } else {
         StringBuffer buffer = new StringBuffer("[");
         boolean first = true;
         Iterator i$ = map.entrySet().iterator();

         while(i$.hasNext()) {
            Object o = i$.next();
            if (first) {
               first = false;
            } else {
               buffer.append(", ");
            }

            if (maxSize != -1 && buffer.length() > maxSize) {
               buffer.append("...");
               break;
            }

            Entry entry = (Entry)o;
            buffer.append(format(entry.getKey(), verbose));
            buffer.append(":");
            if (entry.getValue() == map) {
               buffer.append("(this Map)");
            } else {
               buffer.append(format(entry.getValue(), verbose, sizeLeft(maxSize, buffer)));
            }
         }

         buffer.append("]");
         return buffer.toString();
      }
   }

   private static int sizeLeft(int maxSize, StringBuffer buffer) {
      return maxSize == -1 ? maxSize : Math.max(0, maxSize - buffer.length());
   }

   private static String formatList(Collection collection, boolean verbose, int maxSize) {
      StringBuffer buffer = new StringBuffer("[");
      boolean first = true;
      Iterator i$ = collection.iterator();

      while(i$.hasNext()) {
         Object item = i$.next();
         if (first) {
            first = false;
         } else {
            buffer.append(", ");
         }

         if (maxSize != -1 && buffer.length() > maxSize) {
            buffer.append("...");
            break;
         }

         if (item == collection) {
            buffer.append("(this Collection)");
         } else {
            buffer.append(format(item, verbose, sizeLeft(maxSize, buffer)));
         }
      }

      buffer.append("]");
      return buffer.toString();
   }

   public static String toTypeString(Object[] arguments) {
      if (arguments == null) {
         return "null";
      } else {
         StringBuffer argBuf = new StringBuffer();

         for(int i = 0; i < arguments.length; ++i) {
            if (i > 0) {
               argBuf.append(", ");
            }

            argBuf.append(arguments[i] != null ? arguments[i].getClass().getName() : "null");
         }

         return argBuf.toString();
      }
   }

   public static String toMapString(Map arg) {
      return toMapString(arg, -1);
   }

   public static String toMapString(Map arg, int maxSize) {
      return formatMap(arg, false, maxSize);
   }

   public static String toListString(Collection arg) {
      return toListString(arg, -1);
   }

   public static String toListString(Collection arg, int maxSize) {
      return formatList(arg, false, maxSize);
   }

   public static String toArrayString(Object[] arguments) {
      if (arguments == null) {
         return "null";
      } else {
         String sbdry = "[";
         String ebdry = "]";
         StringBuffer argBuf = new StringBuffer(sbdry);

         for(int i = 0; i < arguments.length; ++i) {
            if (i > 0) {
               argBuf.append(", ");
            }

            argBuf.append(format(arguments[i], false));
         }

         argBuf.append(ebdry);
         return argBuf.toString();
      }
   }

   public static List createRange(Object from, Object to, boolean inclusive) {
      try {
         return ScriptBytecodeAdapter.createRange(from, to, inclusive);
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Error var5) {
         throw var5;
      } catch (Throwable var6) {
         throw new RuntimeException(var6);
      }
   }

   public static Object bitwiseNegate(Object value) {
      if (value instanceof Integer) {
         Integer number = (Integer)value;
         return ~number;
      } else if (value instanceof Long) {
         Long number = (Long)value;
         return ~number;
      } else if (value instanceof BigInteger) {
         return ((BigInteger)value).not();
      } else if (value instanceof String) {
         return DefaultGroovyMethods.bitwiseNegate(value.toString());
      } else if (value instanceof GString) {
         return DefaultGroovyMethods.bitwiseNegate(value.toString());
      } else if (!(value instanceof ArrayList)) {
         return invokeMethod(value, "bitwiseNegate", EMPTY_ARGS);
      } else {
         List newlist = new ArrayList();
         Iterator it = ((ArrayList)value).iterator();

         while(it.hasNext()) {
            newlist.add(bitwiseNegate(it.next()));
         }

         return newlist;
      }
   }

   public static MetaClassRegistry getMetaRegistry() {
      return metaRegistry;
   }

   public static MetaClass getMetaClass(Object object) {
      return object instanceof GroovyObject ? ((GroovyObject)object).getMetaClass() : ((MetaClassRegistryImpl)GroovySystem.getMetaClassRegistry()).getMetaClass(object);
   }

   public static MetaClass getMetaClass(Class cls) {
      return metaRegistry.getMetaClass(cls);
   }

   public static Object invokeMethod(Object object, String methodName, Object arguments) {
      if (object == null) {
         object = NullObject.getNullObject();
      }

      if (object instanceof Class) {
         Class theClass = (Class)object;
         MetaClass metaClass = metaRegistry.getMetaClass(theClass);
         return metaClass.invokeStaticMethod(object, methodName, asArray(arguments));
      } else {
         return !(object instanceof GroovyObject) ? invokePojoMethod(object, methodName, arguments) : invokePogoMethod(object, methodName, arguments);
      }
   }

   static Object invokePojoMethod(Object object, String methodName, Object arguments) {
      MetaClass metaClass = getMetaClass(object);
      return metaClass.invokeMethod(object, methodName, asArray(arguments));
   }

   static Object invokePogoMethod(Object object, String methodName, Object arguments) {
      GroovyObject groovy = (GroovyObject)object;
      boolean intercepting = groovy instanceof GroovyInterceptable;

      try {
         return intercepting ? groovy.invokeMethod(methodName, asUnwrappedArray(arguments)) : groovy.getMetaClass().invokeMethod(object, methodName, asArray(arguments));
      } catch (MissingMethodException var6) {
         if (var6 instanceof MissingMethodExecutionFailed) {
            throw (MissingMethodException)var6.getCause();
         } else if (!intercepting && var6.getMethod().equals(methodName) && object.getClass() == var6.getType()) {
            return groovy.invokeMethod(methodName, asUnwrappedArray(arguments));
         } else {
            throw var6;
         }
      }
   }

   public static Object invokeSuperMethod(Object object, String methodName, Object arguments) {
      if (object == null) {
         throw new NullPointerException("Cannot invoke method " + methodName + "() on null object");
      } else {
         Class theClass = object.getClass();
         MetaClass metaClass = metaRegistry.getMetaClass(theClass.getSuperclass());
         return metaClass.invokeMethod(object, methodName, asArray(arguments));
      }
   }

   public static Object invokeStaticMethod(Class type, String method, Object arguments) {
      MetaClass metaClass = metaRegistry.getMetaClass(type);
      return metaClass.invokeStaticMethod(type, method, asArray(arguments));
   }

   public static Object invokeConstructorOf(Class type, Object arguments) {
      MetaClass metaClass = metaRegistry.getMetaClass(type);
      return metaClass.invokeConstructor(asArray(arguments));
   }

   public static Object[] asArray(Object arguments) {
      if (arguments == null) {
         return EMPTY_ARGUMENTS;
      } else {
         return arguments instanceof Object[] ? (Object[])((Object[])arguments) : new Object[]{arguments};
      }
   }

   public static Object[] asUnwrappedArray(Object arguments) {
      Object[] args = asArray(arguments);

      for(int i = 0; i < args.length; ++i) {
         if (args[i] instanceof PojoWrapper) {
            args[i] = ((PojoWrapper)args[i]).unwrap();
         }
      }

      return args;
   }

   static {
      EMPTY_ARGUMENTS = EMPTY_ARGS;
      EMPTY_TYPES = new Class[0];
      metaRegistry = GroovySystem.getMetaClassRegistry();
   }
}
