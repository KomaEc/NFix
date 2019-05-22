package org.codehaus.groovy.runtime;

import groovy.lang.Closure;
import groovy.lang.EmptyRange;
import groovy.lang.GroovyInterceptable;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovySystem;
import groovy.lang.IntRange;
import groovy.lang.MetaClass;
import groovy.lang.MissingMethodException;
import groovy.lang.MissingPropertyException;
import groovy.lang.ObjectRange;
import groovy.lang.Tuple;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.groovy.runtime.metaclass.MissingMethodExceptionNoStack;
import org.codehaus.groovy.runtime.metaclass.MissingMethodExecutionFailed;
import org.codehaus.groovy.runtime.metaclass.MissingPropertyExceptionNoStack;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.runtime.wrappers.GroovyObjectWrapper;
import org.codehaus.groovy.runtime.wrappers.PojoWrapper;
import org.codehaus.groovy.runtime.wrappers.Wrapper;

public class ScriptBytecodeAdapter {
   public static final Object[] EMPTY_ARGS = new Object[0];
   private static final Integer ZERO = 0;
   private static final Integer MINUS_ONE = -1;
   private static final Integer ONE = 1;

   public static Throwable unwrap(GroovyRuntimeException gre) {
      if (gre instanceof MissingPropertyExceptionNoStack) {
         MissingPropertyExceptionNoStack noStack = (MissingPropertyExceptionNoStack)gre;
         return new MissingPropertyException(noStack.getProperty(), noStack.getType());
      } else if (gre instanceof MissingMethodExceptionNoStack) {
         MissingMethodExceptionNoStack noStack = (MissingMethodExceptionNoStack)gre;
         return new MissingMethodException(noStack.getMethod(), noStack.getType(), noStack.getArguments(), noStack.isStatic());
      } else {
         Throwable th = gre;
         if (gre.getCause() != null && gre.getCause() != gre) {
            th = gre.getCause();
         }

         return (Throwable)(th != gre && th instanceof GroovyRuntimeException ? unwrap((GroovyRuntimeException)th) : th);
      }
   }

   public static Object invokeMethodOnCurrentN(Class senderClass, GroovyObject receiver, String messageName, Object[] messageArguments) throws Throwable {
      Object result = null;
      boolean intercepting = receiver instanceof GroovyInterceptable;

      try {
         try {
            if (intercepting) {
               result = receiver.invokeMethod(messageName, messageArguments);
            } else {
               result = receiver.getMetaClass().invokeMethod(senderClass, receiver, messageName, messageArguments, false, true);
            }
         } catch (MissingMethodException var7) {
            if (var7 instanceof MissingMethodExecutionFailed) {
               throw (MissingMethodException)var7.getCause();
            }

            if (intercepting || receiver.getClass() != var7.getType() || !var7.getMethod().equals(messageName)) {
               throw var7;
            }

            result = receiver.invokeMethod(messageName, messageArguments);
         }

         return result;
      } catch (GroovyRuntimeException var8) {
         throw unwrap(var8);
      }
   }

   public static Object invokeMethodOnCurrentNSafe(Class senderClass, GroovyObject receiver, String messageName, Object[] messageArguments) throws Throwable {
      return invokeMethodOnCurrentN(senderClass, receiver, messageName, messageArguments);
   }

   public static Object invokeMethodOnCurrentNSpreadSafe(Class senderClass, GroovyObject receiver, String messageName, Object[] messageArguments) throws Throwable {
      List answer = new ArrayList();
      Iterator it = InvokerHelper.asIterator(receiver);

      while(it.hasNext()) {
         answer.add(invokeMethodNSafe(senderClass, it.next(), messageName, messageArguments));
      }

      return answer;
   }

   public static Object invokeMethodOnCurrent0(Class senderClass, GroovyObject receiver, String messageName) throws Throwable {
      return invokeMethodOnCurrentN(senderClass, receiver, messageName, EMPTY_ARGS);
   }

   public static Object invokeMethodOnCurrent0Safe(Class senderClass, GroovyObject receiver, String messageName, Object[] messageArguments) throws Throwable {
      return invokeMethodOnCurrentNSafe(senderClass, receiver, messageName, EMPTY_ARGS);
   }

   public static Object invokeMethodOnCurrent0SpreadSafe(Class senderClass, GroovyObject receiver, String messageName, Object[] messageArguments) throws Throwable {
      return invokeMethodOnCurrentNSpreadSafe(senderClass, receiver, messageName, EMPTY_ARGS);
   }

   public static Object invokeMethodOnSuperN(Class senderClass, GroovyObject receiver, String messageName, Object[] messageArguments) throws Throwable {
      MetaClass metaClass = receiver.getMetaClass();
      Object result = null;

      try {
         result = metaClass.invokeMethod(senderClass, receiver, messageName, messageArguments, true, true);
         return result;
      } catch (GroovyRuntimeException var7) {
         throw unwrap(var7);
      }
   }

   public static Object invokeMethodOnSuperNSafe(Class senderClass, GroovyObject receiver, String messageName, Object[] messageArguments) throws Throwable {
      return invokeMethodOnSuperN(senderClass, receiver, messageName, messageArguments);
   }

   public static Object invokeMethodOnSuperNSpreadSafe(Class senderClass, GroovyObject receiver, String messageName, Object[] messageArguments) throws Throwable {
      List answer = new ArrayList();
      Iterator it = InvokerHelper.asIterator(receiver);

      while(it.hasNext()) {
         answer.add(invokeMethodNSafe(senderClass, it.next(), messageName, messageArguments));
      }

      return answer;
   }

   public static Object invokeMethodOnSuper0(Class senderClass, GroovyObject receiver, String messageName) throws Throwable {
      return invokeMethodOnSuperN(senderClass, receiver, messageName, EMPTY_ARGS);
   }

   public static Object invokeMethodOnSuper0Safe(Class senderClass, GroovyObject receiver, String messageName, Object[] messageArguments) throws Throwable {
      return invokeMethodOnSuperNSafe(senderClass, receiver, messageName, EMPTY_ARGS);
   }

   public static Object invokeMethodOnSuper0SpreadSafe(Class senderClass, GroovyObject receiver, String messageName, Object[] messageArguments) throws Throwable {
      return invokeMethodOnSuperNSpreadSafe(senderClass, receiver, messageName, EMPTY_ARGS);
   }

   public static Object invokeMethodN(Class senderClass, Object receiver, String messageName, Object[] messageArguments) throws Throwable {
      try {
         return InvokerHelper.invokeMethod(receiver, messageName, messageArguments);
      } catch (GroovyRuntimeException var5) {
         throw unwrap(var5);
      }
   }

   public static Object invokeMethodNSafe(Class senderClass, Object receiver, String messageName, Object[] messageArguments) throws Throwable {
      return receiver == null ? null : invokeMethodN(senderClass, receiver, messageName, messageArguments);
   }

   public static Object invokeMethodNSpreadSafe(Class senderClass, Object receiver, String messageName, Object[] messageArguments) throws Throwable {
      if (receiver == null) {
         return null;
      } else {
         List answer = new ArrayList();
         Iterator it = InvokerHelper.asIterator(receiver);

         while(it.hasNext()) {
            answer.add(invokeMethodNSafe(senderClass, it.next(), messageName, messageArguments));
         }

         return answer;
      }
   }

   private static Object[] getBoxedItems(Object receiver) {
      return DefaultTypeTransformation.primitiveArrayToList(receiver).toArray();
   }

   public static Object invokeMethod0(Class senderClass, Object receiver, String messageName) throws Throwable {
      return invokeMethodN(senderClass, receiver, messageName, EMPTY_ARGS);
   }

   public static Object invokeMethod0Safe(Class senderClass, Object receiver, String messageName) throws Throwable {
      return receiver == null ? null : invokeMethodNSafe(senderClass, receiver, messageName, EMPTY_ARGS);
   }

   public static Object invokeMethod0SpreadSafe(Class senderClass, Object receiver, String messageName) throws Throwable {
      return invokeMethodNSpreadSafe(senderClass, receiver, messageName, EMPTY_ARGS);
   }

   public static Object invokeStaticMethodN(Class senderClass, Class receiver, String messageName, Object[] messageArguments) throws Throwable {
      try {
         return InvokerHelper.invokeStaticMethod((Class)receiver, messageName, messageArguments);
      } catch (GroovyRuntimeException var5) {
         throw unwrap(var5);
      }
   }

   public static Object invokeStaticMethod0(Class senderClass, Class receiver, String messageName) throws Throwable {
      return invokeStaticMethodN(senderClass, receiver, messageName, EMPTY_ARGS);
   }

   public static Object invokeNewN(Class senderClass, Class receiver, Object arguments) throws Throwable {
      try {
         return InvokerHelper.invokeConstructorOf(receiver, arguments);
      } catch (GroovyRuntimeException var4) {
         throw unwrap(var4);
      }
   }

   public static Object invokeNew0(Class senderClass, Class receiver) throws Throwable {
      return invokeNewN(senderClass, receiver, EMPTY_ARGS);
   }

   public static int selectConstructorAndTransformArguments(Object[] arguments, int numberOfConstructors, Class which) throws Throwable {
      MetaClass metaClass = GroovySystem.getMetaClassRegistry().getMetaClass(which);

      try {
         return metaClass.selectConstructorAndTransformArguments(numberOfConstructors, arguments);
      } catch (GroovyRuntimeException var5) {
         throw unwrap(var5);
      }
   }

   public static Object getFieldOnSuper(Class senderClass, Object receiver, String messageName) throws Throwable {
      try {
         if (receiver instanceof Class) {
            return InvokerHelper.getAttribute(receiver, messageName);
         } else {
            MetaClass mc = ((GroovyObject)receiver).getMetaClass();
            return mc.getAttribute(senderClass, receiver, messageName, true);
         }
      } catch (GroovyRuntimeException var4) {
         throw unwrap(var4);
      }
   }

   public static Object getFieldOnSuperSafe(Class senderClass, Object receiver, String messageName) throws Throwable {
      return getFieldOnSuper(senderClass, receiver, messageName);
   }

   public static Object getFieldOnSuperSpreadSafe(Class senderClass, Object receiver, String messageName) throws Throwable {
      List answer = new ArrayList();
      Iterator it = InvokerHelper.asIterator(receiver);

      while(it.hasNext()) {
         answer.add(getFieldOnSuper(senderClass, it.next(), messageName));
      }

      return answer;
   }

   public static void setFieldOnSuper(Object messageArgument, Class senderClass, Object receiver, String messageName) throws Throwable {
      try {
         if (receiver instanceof Class) {
            InvokerHelper.setAttribute(receiver, messageName, messageArgument);
         } else {
            MetaClass mc = ((GroovyObject)receiver).getMetaClass();
            mc.setAttribute(senderClass, receiver, messageName, messageArgument, true, true);
         }

      } catch (GroovyRuntimeException var5) {
         throw unwrap(var5);
      }
   }

   public static void setFieldOnSuperSafe(Object messageArgument, Class senderClass, Object receiver, String messageName) throws Throwable {
      setFieldOnSuper(messageArgument, senderClass, receiver, messageName);
   }

   public static void setFieldOnSuperSpreadSafe(Object messageArgument, Class senderClass, Object receiver, String messageName) throws Throwable {
      Iterator it = InvokerHelper.asIterator(receiver);

      while(it.hasNext()) {
         setFieldOnSuper(messageArgument, senderClass, it.next(), messageName);
      }

   }

   public static Object getField(Class senderClass, Object receiver, String messageName) throws Throwable {
      try {
         return InvokerHelper.getAttribute(receiver, messageName);
      } catch (GroovyRuntimeException var4) {
         throw unwrap(var4);
      }
   }

   public static Object getFieldSafe(Class senderClass, Object receiver, String messageName) throws Throwable {
      return receiver == null ? null : getField(senderClass, receiver, messageName);
   }

   public static Object getFieldSpreadSafe(Class senderClass, Object receiver, String messageName) throws Throwable {
      if (receiver == null) {
         return null;
      } else {
         List answer = new ArrayList();
         Iterator it = InvokerHelper.asIterator(receiver);

         while(it.hasNext()) {
            answer.add(getFieldSafe(senderClass, it.next(), messageName));
         }

         return answer;
      }
   }

   public static void setField(Object messageArgument, Class senderClass, Object receiver, String messageName) throws Throwable {
      try {
         InvokerHelper.setAttribute(receiver, messageName, messageArgument);
      } catch (GroovyRuntimeException var5) {
         throw unwrap(var5);
      }
   }

   public static void setFieldSafe(Object messageArgument, Class senderClass, Object receiver, String messageName) throws Throwable {
      if (receiver != null) {
         setField(messageArgument, senderClass, receiver, messageName);
      }
   }

   public static void setFieldSpreadSafe(Object messageArgument, Class senderClass, Object receiver, String messageName) throws Throwable {
      if (receiver != null) {
         Iterator it = InvokerHelper.asIterator(receiver);

         while(it.hasNext()) {
            setFieldSafe(messageArgument, senderClass, it.next(), messageName);
         }

      }
   }

   public static Object getGroovyObjectField(Class senderClass, GroovyObject receiver, String messageName) throws Throwable {
      try {
         return receiver.getMetaClass().getAttribute(receiver, messageName);
      } catch (GroovyRuntimeException var4) {
         throw unwrap(var4);
      }
   }

   public static Object getGroovyObjectFieldSafe(Class senderClass, GroovyObject receiver, String messageName) throws Throwable {
      if (receiver == null) {
         return null;
      } else {
         try {
            return receiver.getMetaClass().getAttribute(receiver, messageName);
         } catch (GroovyRuntimeException var4) {
            throw unwrap(var4);
         }
      }
   }

   public static Object getGroovyObjectFieldSpreadSafe(Class senderClass, GroovyObject receiver, String messageName) throws Throwable {
      if (receiver == null) {
         return null;
      } else {
         List answer = new ArrayList();
         Iterator it = InvokerHelper.asIterator(receiver);

         while(it.hasNext()) {
            answer.add(getFieldSafe(senderClass, it.next(), messageName));
         }

         return answer;
      }
   }

   public static void setGroovyObjectField(Object messageArgument, Class senderClass, GroovyObject receiver, String messageName) throws Throwable {
      try {
         receiver.getMetaClass().setAttribute(receiver, messageName, messageArgument);
      } catch (GroovyRuntimeException var5) {
         throw unwrap(var5);
      }
   }

   public static void setGroovyObjectFieldSafe(Object messageArgument, Class senderClass, GroovyObject receiver, String messageName) throws Throwable {
      if (receiver != null) {
         try {
            receiver.getMetaClass().setAttribute(receiver, messageName, messageArgument);
         } catch (GroovyRuntimeException var5) {
            throw unwrap(var5);
         }
      }
   }

   public static void setGroovyObjectFieldSpreadSafe(Object messageArgument, Class senderClass, GroovyObject receiver, String messageName) throws Throwable {
      if (receiver != null) {
         Iterator it = InvokerHelper.asIterator(receiver);

         while(it.hasNext()) {
            setFieldSafe(messageArgument, senderClass, it.next(), messageName);
         }

      }
   }

   public static Object getPropertyOnSuper(Class senderClass, GroovyObject receiver, String messageName) throws Throwable {
      return invokeMethodOnSuperN(senderClass, receiver, "getProperty", new Object[]{messageName});
   }

   public static Object getPropertyOnSuperSafe(Class senderClass, GroovyObject receiver, String messageName) throws Throwable {
      return getPropertyOnSuper(senderClass, receiver, messageName);
   }

   public static Object getPropertyOnSuperSpreadSafe(Class senderClass, GroovyObject receiver, String messageName) throws Throwable {
      List answer = new ArrayList();
      Iterator it = InvokerHelper.asIterator(receiver);

      while(it.hasNext()) {
         answer.add(getPropertySafe(senderClass, it.next(), messageName));
      }

      return answer;
   }

   public static void setPropertyOnSuper(Object messageArgument, Class senderClass, GroovyObject receiver, String messageName) throws Throwable {
      try {
         InvokerHelper.setAttribute(receiver, messageName, messageArgument);
      } catch (GroovyRuntimeException var5) {
         throw unwrap(var5);
      }
   }

   public static void setPropertyOnSuperSafe(Object messageArgument, Class senderClass, GroovyObject receiver, String messageName) throws Throwable {
      setPropertyOnSuper(messageArgument, senderClass, receiver, messageName);
   }

   public static void setPropertyOnSuperSpreadSafe(Object messageArgument, Class senderClass, GroovyObject receiver, String messageName) throws Throwable {
      Iterator it = InvokerHelper.asIterator(receiver);

      while(it.hasNext()) {
         setPropertySafe(messageArgument, senderClass, it.next(), messageName);
      }

   }

   public static Object getProperty(Class senderClass, Object receiver, String messageName) throws Throwable {
      try {
         return InvokerHelper.getProperty(receiver, messageName);
      } catch (GroovyRuntimeException var4) {
         throw unwrap(var4);
      }
   }

   public static Object getPropertySafe(Class senderClass, Object receiver, String messageName) throws Throwable {
      return receiver == null ? null : getProperty(senderClass, receiver, messageName);
   }

   public static Object getPropertySpreadSafe(Class senderClass, Object receiver, String messageName) throws Throwable {
      if (receiver == null) {
         return null;
      } else {
         List answer = new ArrayList();
         Iterator it = InvokerHelper.asIterator(receiver);

         while(it.hasNext()) {
            answer.add(getPropertySafe(senderClass, it.next(), messageName));
         }

         return answer;
      }
   }

   public static void setProperty(Object messageArgument, Class senderClass, Object receiver, String messageName) throws Throwable {
      try {
         if (receiver == null) {
            receiver = NullObject.getNullObject();
         }

         InvokerHelper.setProperty(receiver, messageName, messageArgument);
      } catch (GroovyRuntimeException var5) {
         throw unwrap(var5);
      }
   }

   public static void setPropertySafe(Object messageArgument, Class senderClass, Object receiver, String messageName) throws Throwable {
      if (receiver != null) {
         setProperty(messageArgument, senderClass, receiver, messageName);
      }
   }

   public static void setPropertySpreadSafe(Object messageArgument, Class senderClass, Object receiver, String messageName) throws Throwable {
      if (receiver != null) {
         Iterator it = InvokerHelper.asIterator(receiver);

         while(it.hasNext()) {
            setPropertySafe(messageArgument, senderClass, it.next(), messageName);
         }

      }
   }

   public static Object getGroovyObjectProperty(Class senderClass, GroovyObject receiver, String messageName) throws Throwable {
      return receiver.getProperty(messageName);
   }

   public static Object getGroovyObjectPropertySafe(Class senderClass, GroovyObject receiver, String messageName) throws Throwable {
      return receiver == null ? null : getGroovyObjectProperty(senderClass, receiver, messageName);
   }

   public static Object getGroovyObjectPropertySpreadSafe(Class senderClass, GroovyObject receiver, String messageName) throws Throwable {
      if (receiver == null) {
         return null;
      } else {
         List answer = new ArrayList();
         Iterator it = InvokerHelper.asIterator(receiver);

         while(it.hasNext()) {
            answer.add(getPropertySafe(senderClass, it.next(), messageName));
         }

         return answer;
      }
   }

   public static void setGroovyObjectProperty(Object messageArgument, Class senderClass, GroovyObject receiver, String messageName) throws Throwable {
      try {
         receiver.setProperty(messageName, messageArgument);
      } catch (GroovyRuntimeException var5) {
         throw unwrap(var5);
      }
   }

   public static void setGroovyObjectPropertySafe(Object messageArgument, Class senderClass, GroovyObject receiver, String messageName) throws Throwable {
      if (receiver != null) {
         receiver.setProperty(messageName, messageArgument);
      }
   }

   public static void setGroovyObjectPropertySpreadSafe(Object messageArgument, Class senderClass, GroovyObject receiver, String messageName) throws Throwable {
      if (receiver != null) {
         Iterator it = InvokerHelper.asIterator(receiver);

         while(it.hasNext()) {
            setPropertySafe(messageArgument, senderClass, it.next(), messageName);
         }

      }
   }

   public static Closure getMethodPointer(Object object, String methodName) {
      return InvokerHelper.getMethodPointer(object, methodName);
   }

   public static Object invokeClosure(Object closure, Object[] arguments) throws Throwable {
      return invokeMethodN(closure.getClass(), closure, "call", arguments);
   }

   public static Object asType(Object object, Class type) throws Throwable {
      if (object == null) {
         object = NullObject.getNullObject();
      }

      return invokeMethodN(object.getClass(), object, "asType", new Object[]{type});
   }

   public static Object castToType(Object object, Class type) throws Throwable {
      return DefaultTypeTransformation.castToType(object, type);
   }

   public static Tuple createTuple(Object[] array) {
      return new Tuple(array);
   }

   public static List createList(Object[] values) {
      return InvokerHelper.createList(values);
   }

   public static Wrapper createPojoWrapper(Object val, Class clazz) {
      return new PojoWrapper(val, clazz);
   }

   public static Wrapper createGroovyObjectWrapper(GroovyObject val, Class clazz) {
      return new GroovyObjectWrapper(val, clazz);
   }

   public static Map createMap(Object[] values) {
      return InvokerHelper.createMap(values);
   }

   public static List createRange(Object from, Object to, boolean inclusive) throws Throwable {
      if (from instanceof Integer && to instanceof Integer) {
         int ito = (Integer)to;
         int ifrom = (Integer)from;
         if (!inclusive) {
            if (ifrom == ito) {
               return new EmptyRange((Comparable)from);
            }

            if (ifrom > ito) {
               ++ito;
            } else {
               --ito;
            }
         }

         return new IntRange(ifrom, ito);
      } else {
         if (!inclusive) {
            if (compareEqual(from, to)) {
               return new EmptyRange((Comparable)from);
            }

            if (compareGreaterThan(from, to)) {
               to = invokeMethod0(ScriptBytecodeAdapter.class, to, "next");
            } else {
               to = invokeMethod0(ScriptBytecodeAdapter.class, to, "previous");
            }
         }

         return (List)(from instanceof Integer && to instanceof Integer ? new IntRange(DefaultTypeTransformation.intUnbox(from), DefaultTypeTransformation.intUnbox(to)) : new ObjectRange((Comparable)from, (Comparable)to));
      }
   }

   public static void assertFailed(Object expression, Object message) {
      InvokerHelper.assertFailed(expression, message);
   }

   public static boolean isCase(Object switchValue, Object caseExpression) throws Throwable {
      if (caseExpression == null) {
         return switchValue == null;
      } else {
         return DefaultTypeTransformation.castToBoolean(invokeMethodN(caseExpression.getClass(), caseExpression, "isCase", new Object[]{switchValue}));
      }
   }

   public static boolean compareIdentical(Object left, Object right) {
      return left == right;
   }

   public static boolean compareNotIdentical(Object left, Object right) {
      return left != right;
   }

   public static boolean compareEqual(Object left, Object right) {
      return DefaultTypeTransformation.compareEqual(left, right);
   }

   public static boolean compareNotEqual(Object left, Object right) {
      return !compareEqual(left, right);
   }

   public static Integer compareTo(Object left, Object right) {
      int answer = DefaultTypeTransformation.compareTo(left, right);
      if (answer == 0) {
         return ZERO;
      } else {
         return answer > 0 ? ONE : MINUS_ONE;
      }
   }

   public static boolean compareLessThan(Object left, Object right) {
      return compareTo(left, right) < 0;
   }

   public static boolean compareLessThanEqual(Object left, Object right) {
      return compareTo(left, right) <= 0;
   }

   public static boolean compareGreaterThan(Object left, Object right) {
      return compareTo(left, right) > 0;
   }

   public static boolean compareGreaterThanEqual(Object left, Object right) {
      return compareTo(left, right) >= 0;
   }

   public static Pattern regexPattern(Object regex) {
      return DefaultGroovyMethods.bitwiseNegate(regex.toString());
   }

   public static Matcher findRegex(Object left, Object right) throws Throwable {
      return InvokerHelper.findRegex(left, right);
   }

   public static boolean matchRegex(Object left, Object right) {
      return InvokerHelper.matchRegex(left, right);
   }

   public static Object[] despreadList(Object[] args, Object[] spreads, int[] positions) {
      List ret = new ArrayList();
      int argsPos = 0;
      int spreadPos = 0;

      for(int pos = 0; pos < positions.length; ++pos) {
         while(argsPos < positions[pos]) {
            ret.add(args[argsPos]);
            ++argsPos;
         }

         Object value = spreads[spreadPos];
         if (value == null) {
            ret.add((Object)null);
         } else if (value instanceof List) {
            ret.addAll((List)value);
         } else {
            if (!value.getClass().isArray()) {
               throw new IllegalArgumentException("cannot spread the type " + value.getClass().getName() + " with value " + value);
            }

            ret.addAll(DefaultTypeTransformation.primitiveArrayToList(value));
         }

         ++spreadPos;
      }

      while(argsPos < args.length) {
         ret.add(args[argsPos]);
         ++argsPos;
      }

      return ret.toArray();
   }

   public static Object spreadMap(Object value) {
      return InvokerHelper.spreadMap(value);
   }

   public static Object unaryMinus(Object value) throws Throwable {
      return InvokerHelper.unaryMinus(value);
   }

   public static Object unaryPlus(Object value) throws Throwable {
      try {
         return InvokerHelper.unaryPlus(value);
      } catch (GroovyRuntimeException var2) {
         throw unwrap(var2);
      }
   }

   public static Object bitwiseNegate(Object value) throws Throwable {
      try {
         return InvokerHelper.bitwiseNegate(value);
      } catch (GroovyRuntimeException var2) {
         throw unwrap(var2);
      }
   }

   public static MetaClass initMetaClass(Object object) {
      return InvokerHelper.getMetaClass(object.getClass());
   }
}
