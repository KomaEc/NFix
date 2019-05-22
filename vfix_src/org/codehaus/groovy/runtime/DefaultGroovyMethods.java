package org.codehaus.groovy.runtime;

import groovy.io.EncodingAwareBufferedWriter;
import groovy.io.FileType;
import groovy.io.FileVisitResult;
import groovy.io.GroovyPrintWriter;
import groovy.lang.Closure;
import groovy.lang.DelegatingMetaClass;
import groovy.lang.DeprecationException;
import groovy.lang.EmptyRange;
import groovy.lang.ExpandoMetaClass;
import groovy.lang.GString;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovySystem;
import groovy.lang.IntRange;
import groovy.lang.MapWithDefault;
import groovy.lang.MetaClass;
import groovy.lang.MetaClassImpl;
import groovy.lang.MetaClassRegistry;
import groovy.lang.MetaProperty;
import groovy.lang.MissingPropertyException;
import groovy.lang.ObjectRange;
import groovy.lang.PropertyValue;
import groovy.lang.Range;
import groovy.lang.SpreadMap;
import groovy.lang.StringWriterIOException;
import groovy.lang.Writable;
import groovy.sql.GroovyResultSet;
import groovy.sql.GroovyRowResult;
import groovy.util.CharsetToolkit;
import groovy.util.ClosureComparator;
import groovy.util.GroovyCollections;
import groovy.util.OrderBy;
import groovy.util.PermutationGenerator;
import groovy.util.ProxyGenerator;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.reflection.MixinInMetaClass;
import org.codehaus.groovy.reflection.ReflectionCache;
import org.codehaus.groovy.runtime.dgmimpl.NumberNumberDiv;
import org.codehaus.groovy.runtime.dgmimpl.NumberNumberMinus;
import org.codehaus.groovy.runtime.dgmimpl.NumberNumberMultiply;
import org.codehaus.groovy.runtime.dgmimpl.NumberNumberPlus;
import org.codehaus.groovy.runtime.dgmimpl.arrays.BooleanArrayGetAtMetaMethod;
import org.codehaus.groovy.runtime.dgmimpl.arrays.BooleanArrayPutAtMetaMethod;
import org.codehaus.groovy.runtime.dgmimpl.arrays.ByteArrayGetAtMetaMethod;
import org.codehaus.groovy.runtime.dgmimpl.arrays.ByteArrayPutAtMetaMethod;
import org.codehaus.groovy.runtime.dgmimpl.arrays.CharacterArrayGetAtMetaMethod;
import org.codehaus.groovy.runtime.dgmimpl.arrays.CharacterArrayPutAtMetaMethod;
import org.codehaus.groovy.runtime.dgmimpl.arrays.DoubleArrayGetAtMetaMethod;
import org.codehaus.groovy.runtime.dgmimpl.arrays.DoubleArrayPutAtMetaMethod;
import org.codehaus.groovy.runtime.dgmimpl.arrays.FloatArrayGetAtMetaMethod;
import org.codehaus.groovy.runtime.dgmimpl.arrays.FloatArrayPutAtMetaMethod;
import org.codehaus.groovy.runtime.dgmimpl.arrays.IntegerArrayGetAtMetaMethod;
import org.codehaus.groovy.runtime.dgmimpl.arrays.IntegerArrayPutAtMetaMethod;
import org.codehaus.groovy.runtime.dgmimpl.arrays.LongArrayGetAtMetaMethod;
import org.codehaus.groovy.runtime.dgmimpl.arrays.LongArrayPutAtMetaMethod;
import org.codehaus.groovy.runtime.dgmimpl.arrays.ObjectArrayGetAtMetaMethod;
import org.codehaus.groovy.runtime.dgmimpl.arrays.ObjectArrayPutAtMetaMethod;
import org.codehaus.groovy.runtime.dgmimpl.arrays.ShortArrayGetAtMetaMethod;
import org.codehaus.groovy.runtime.dgmimpl.arrays.ShortArrayPutAtMetaMethod;
import org.codehaus.groovy.runtime.metaclass.MetaClassRegistryImpl;
import org.codehaus.groovy.runtime.metaclass.MissingPropertyExceptionNoStack;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.runtime.typehandling.GroovyCastException;
import org.codehaus.groovy.runtime.typehandling.NumberMath;
import org.codehaus.groovy.tools.RootLoader;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DefaultGroovyMethods extends DefaultGroovyMethodsSupport {
   private static final Logger LOG = Logger.getLogger(DefaultGroovyMethods.class.getName());
   private static final Integer ONE = 1;
   private static final BigInteger BI_INT_MAX = BigInteger.valueOf(2147483647L);
   private static final BigInteger BI_INT_MIN = BigInteger.valueOf(-2147483648L);
   private static final BigInteger BI_LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);
   private static final BigInteger BI_LONG_MIN = BigInteger.valueOf(Long.MIN_VALUE);
   public static final Class[] additionals = new Class[]{NumberNumberPlus.class, NumberNumberMultiply.class, NumberNumberMinus.class, NumberNumberDiv.class, ObjectArrayGetAtMetaMethod.class, ObjectArrayPutAtMetaMethod.class, BooleanArrayGetAtMetaMethod.class, BooleanArrayPutAtMetaMethod.class, ByteArrayGetAtMetaMethod.class, ByteArrayPutAtMetaMethod.class, CharacterArrayGetAtMetaMethod.class, CharacterArrayPutAtMetaMethod.class, ShortArrayGetAtMetaMethod.class, ShortArrayPutAtMetaMethod.class, IntegerArrayGetAtMetaMethod.class, IntegerArrayPutAtMetaMethod.class, LongArrayGetAtMetaMethod.class, LongArrayPutAtMetaMethod.class, FloatArrayGetAtMetaMethod.class, FloatArrayPutAtMetaMethod.class, DoubleArrayGetAtMetaMethod.class, DoubleArrayPutAtMetaMethod.class};
   private static int charBufferSize = 4096;
   private static int expectedLineLength = 160;
   private static int EOF = -1;
   static String lineSeparator = null;

   public static boolean is(Object self, Object other) {
      return self == other;
   }

   public static Object identity(Object self, Closure closure) {
      return with(self, closure);
   }

   public static Object with(Object self, Closure closure) {
      Closure clonedClosure = (Closure)closure.clone();
      clonedClosure.setResolveStrategy(1);
      clonedClosure.setDelegate(self);
      return clonedClosure.call(self);
   }

   public static Object getAt(Object self, String property) {
      return InvokerHelper.getProperty(self, property);
   }

   public static void putAt(Object self, String property, Object newValue) {
      InvokerHelper.setProperty(self, property, newValue);
   }

   public static String dump(Object self) {
      if (self == null) {
         return "null";
      } else {
         StringBuilder buffer = new StringBuilder("<");
         Class klass = self.getClass();
         buffer.append(klass.getName());
         buffer.append("@");
         buffer.append(Integer.toHexString(self.hashCode()));

         for(boolean groovyObject = self instanceof GroovyObject; klass != null; klass = klass.getSuperclass()) {
            Field[] arr$ = klass.getDeclaredFields();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               final Field field = arr$[i$];
               if ((field.getModifiers() & 8) == 0 && (!groovyObject || !field.getName().equals("metaClass"))) {
                  AccessController.doPrivileged(new PrivilegedAction() {
                     public Object run() {
                        field.setAccessible(true);
                        return null;
                     }
                  });
                  buffer.append(" ");
                  buffer.append(field.getName());
                  buffer.append("=");

                  try {
                     buffer.append(InvokerHelper.toString(field.get(self)));
                  } catch (Exception var9) {
                     buffer.append(var9);
                  }
               }
            }
         }

         buffer.append(">");
         return buffer.toString();
      }
   }

   public static List<PropertyValue> getMetaPropertyValues(Object self) {
      MetaClass metaClass = InvokerHelper.getMetaClass(self);
      List<MetaProperty> mps = metaClass.getProperties();
      List<PropertyValue> props = new ArrayList(mps.size());
      Iterator i$ = mps.iterator();

      while(i$.hasNext()) {
         MetaProperty mp = (MetaProperty)i$.next();
         props.add(new PropertyValue(self, mp));
      }

      return props;
   }

   public static Map getProperties(Object self) {
      List<PropertyValue> metaProps = getMetaPropertyValues(self);
      Map<String, Object> props = new LinkedHashMap(metaProps.size());
      Iterator i$ = metaProps.iterator();

      while(i$.hasNext()) {
         PropertyValue mp = (PropertyValue)i$.next();

         try {
            props.put(mp.getName(), mp.getValue());
         } catch (Exception var6) {
            LOG.throwing(self.getClass().getName(), "getProperty(" + mp.getName() + ")", var6);
         }
      }

      return props;
   }

   public static Object use(Object self, Class categoryClass, Closure closure) {
      return GroovyCategorySupport.use(categoryClass, closure);
   }

   public static void mixin(MetaClass self, List<Class> categoryClasses) {
      MixinInMetaClass.mixinClassesToMetaClass(self, categoryClasses);
   }

   public static void mixin(Class self, List<Class> categoryClasses) {
      mixin(getMetaClass(self), categoryClasses);
   }

   public static void mixin(Class self, Class categoryClass) {
      mixin(getMetaClass(self), Collections.singletonList(categoryClass));
   }

   public static void mixin(Class self, Class[] categoryClass) {
      mixin(getMetaClass(self), Arrays.asList(categoryClass));
   }

   public static void mixin(MetaClass self, Class categoryClass) {
      mixin(self, Collections.singletonList(categoryClass));
   }

   public static void mixin(MetaClass self, Class[] categoryClass) {
      mixin(self, Arrays.asList(categoryClass));
   }

   public static Object use(Object self, List<Class> categoryClassList, Closure closure) {
      return GroovyCategorySupport.use(categoryClassList, closure);
   }

   public static void addShutdownHook(Object self, Closure closure) {
      Runtime.getRuntime().addShutdownHook(new Thread(closure));
   }

   public static Object use(Object self, Object[] array) {
      if (array.length < 2) {
         throw new IllegalArgumentException("Expecting at least 2 arguments, a category class and a Closure");
      } else {
         Closure closure;
         try {
            closure = (Closure)array[array.length - 1];
         } catch (ClassCastException var8) {
            throw new IllegalArgumentException("Expecting a Closure to be the last argument");
         }

         List<Class> list = new ArrayList(array.length - 1);

         for(int i = 0; i < array.length - 1; ++i) {
            Class categoryClass;
            try {
               categoryClass = (Class)array[i];
            } catch (ClassCastException var7) {
               throw new IllegalArgumentException("Expecting a Category Class for argument " + i);
            }

            list.add(categoryClass);
         }

         return GroovyCategorySupport.use((List)list, closure);
      }
   }

   public static void print(Object self, Object value) {
      if (self instanceof Writer) {
         try {
            ((Writer)self).write(InvokerHelper.toString(value));
         } catch (IOException var3) {
         }
      } else {
         System.out.print(InvokerHelper.toString(value));
      }

   }

   public static void print(PrintWriter self, Object value) {
      self.print(InvokerHelper.toString(value));
   }

   public static void print(PrintStream self, Object value) {
      self.print(InvokerHelper.toString(value));
   }

   public static void print(Closure self, Object value) {
      Object owner = getClosureOwner(self);
      InvokerHelper.invokeMethod(owner, "print", new Object[]{value});
   }

   public static void println(Object self) {
      if (self instanceof Writer) {
         PrintWriter pw = new GroovyPrintWriter((Writer)self);
         pw.println();
      } else {
         System.out.println();
      }

   }

   public static void println(Closure self) {
      Object owner = getClosureOwner(self);
      InvokerHelper.invokeMethod(owner, "println", new Object[0]);
   }

   private static Object getClosureOwner(Closure cls) {
      Object owner;
      for(owner = cls.getOwner(); owner instanceof GeneratedClosure; owner = ((Closure)owner).getOwner()) {
      }

      return owner;
   }

   public static void println(Object self, Object value) {
      if (self instanceof Writer) {
         PrintWriter pw = new GroovyPrintWriter((Writer)self);
         pw.println(value);
      } else {
         System.out.println(InvokerHelper.toString(value));
      }

   }

   public static void println(PrintWriter self, Object value) {
      self.println(InvokerHelper.toString(value));
   }

   public static void println(PrintStream self, Object value) {
      self.println(InvokerHelper.toString(value));
   }

   public static void println(Closure self, Object value) {
      Object owner = getClosureOwner(self);
      InvokerHelper.invokeMethod(owner, "println", new Object[]{value});
   }

   public static void printf(Object self, String format, Object[] values) {
      if (self instanceof PrintStream) {
         ((PrintStream)self).printf(format, values);
      } else {
         System.out.printf(format, values);
      }

   }

   public static String sprintf(Object self, String format, Object[] values) {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream out = new PrintStream(outputStream);
      out.printf(format, values);
      return outputStream.toString();
   }

   public static void printf(Object self, String format, Object arg) {
      if (self instanceof PrintStream) {
         printf((PrintStream)self, format, arg);
      } else if (self instanceof Writer) {
         printf((Writer)self, format, arg);
      } else {
         printf(System.out, format, arg);
      }

   }

   private static void printf(PrintStream self, String format, Object arg) {
      self.print(sprintf(self, format, (Object)arg));
   }

   private static void printf(Writer self, String format, Object arg) {
      try {
         self.write(sprintf(self, format, (Object)arg));
      } catch (IOException var4) {
         printf(System.out, format, arg);
      }

   }

   public static String sprintf(Object self, String format, Object arg) {
      if (arg instanceof Object[]) {
         return sprintf(self, format, (Object[])((Object[])arg));
      } else if (arg instanceof List) {
         return sprintf(self, format, ((List)arg).toArray());
      } else if (!arg.getClass().isArray()) {
         Object[] o = (Object[])((Object[])Array.newInstance(arg.getClass(), 1));
         o[0] = arg;
         return sprintf(self, format, o);
      } else {
         String elemType = arg.getClass().getName();
         Object ans;
         int i;
         if (elemType.equals("[I")) {
            int[] ia = (int[])((int[])arg);
            ans = new Integer[ia.length];

            for(i = 0; i < ia.length; ++i) {
               ((Object[])ans)[i] = ia[i];
            }
         } else if (elemType.equals("[C")) {
            char[] ca = (char[])((char[])arg);
            ans = new Character[ca.length];

            for(i = 0; i < ca.length; ++i) {
               ((Object[])ans)[i] = ca[i];
            }
         } else if (elemType.equals("[Z")) {
            boolean[] ba = (boolean[])((boolean[])arg);
            ans = new Boolean[ba.length];

            for(i = 0; i < ba.length; ++i) {
               ((Object[])ans)[i] = ba[i];
            }
         } else if (elemType.equals("[B")) {
            byte[] ba = (byte[])((byte[])arg);
            ans = new Byte[ba.length];

            for(i = 0; i < ba.length; ++i) {
               ((Object[])ans)[i] = ba[i];
            }
         } else if (elemType.equals("[S")) {
            short[] sa = (short[])((short[])arg);
            ans = new Short[sa.length];

            for(i = 0; i < sa.length; ++i) {
               ((Object[])ans)[i] = sa[i];
            }
         } else if (elemType.equals("[F")) {
            float[] fa = (float[])((float[])arg);
            ans = new Float[fa.length];

            for(i = 0; i < fa.length; ++i) {
               ((Object[])ans)[i] = fa[i];
            }
         } else if (elemType.equals("[J")) {
            long[] la = (long[])((long[])arg);
            ans = new Long[la.length];

            for(i = 0; i < la.length; ++i) {
               ((Object[])ans)[i] = la[i];
            }
         } else {
            if (!elemType.equals("[D")) {
               throw new RuntimeException("sprintf(String," + arg + ")");
            }

            double[] da = (double[])((double[])arg);
            ans = new Double[da.length];

            for(i = 0; i < da.length; ++i) {
               ((Object[])ans)[i] = da[i];
            }
         }

         return sprintf(self, format, (Object[])ans);
      }
   }

   public static String inspect(Object self) {
      return InvokerHelper.inspect(self);
   }

   public static void print(Object self, PrintWriter out) {
      if (out == null) {
         out = new PrintWriter(System.out);
      }

      out.print(InvokerHelper.toString(self));
   }

   public static void println(Object self, PrintWriter out) {
      if (out == null) {
         out = new PrintWriter(System.out);
      }

      out.println(InvokerHelper.toString(self));
   }

   public static Object invokeMethod(Object object, String method, Object arguments) {
      return InvokerHelper.invokeMethod(object, method, arguments);
   }

   public static boolean isCase(Object caseValue, Object switchValue) {
      return caseValue.getClass().isArray() ? isCase(DefaultTypeTransformation.asCollection(caseValue), switchValue) : caseValue.equals(switchValue);
   }

   public static boolean isCase(String caseValue, Object switchValue) {
      if (switchValue == null) {
         return caseValue == null;
      } else {
         return caseValue.equals(switchValue.toString());
      }
   }

   public static boolean isCase(GString caseValue, Object switchValue) {
      return isCase(caseValue.toString(), switchValue);
   }

   public static boolean isCase(Class caseValue, Object switchValue) {
      if (switchValue instanceof Class) {
         Class val = (Class)switchValue;
         return caseValue.isAssignableFrom(val);
      } else {
         return caseValue.isInstance(switchValue);
      }
   }

   public static boolean isCase(Collection caseValue, Object switchValue) {
      return caseValue.contains(switchValue);
   }

   public static boolean isCase(Map caseValue, Object switchValue) {
      return DefaultTypeTransformation.castToBoolean(caseValue.get(switchValue));
   }

   public static boolean isCase(Pattern caseValue, Object switchValue) {
      if (switchValue == null) {
         return caseValue == null;
      } else {
         Matcher matcher = caseValue.matcher(switchValue.toString());
         if (matcher.matches()) {
            RegexSupport.setLastMatcher(matcher);
            return true;
         } else {
            return false;
         }
      }
   }

   public static boolean isCase(Number caseValue, Number switchValue) {
      return NumberMath.compareTo(caseValue, switchValue) == 0;
   }

   public static <T> Iterator<T> unique(Iterator<T> self) {
      return toList(unique((Collection)toList(self))).listIterator();
   }

   public static <T> Collection<T> unique(Collection<T> self) {
      if (self instanceof Set) {
         return self;
      } else {
         List<T> answer = new ArrayList();
         NumberAwareComparator<T> numberAwareComparator = new NumberAwareComparator();
         Iterator i$ = self.iterator();

         while(i$.hasNext()) {
            T t = i$.next();
            boolean duplicated = false;
            Iterator i$ = answer.iterator();

            while(i$.hasNext()) {
               T t2 = i$.next();
               if (numberAwareComparator.compare(t, t2) == 0) {
                  duplicated = true;
                  break;
               }
            }

            if (!duplicated) {
               answer.add(t);
            }
         }

         self.clear();
         self.addAll(answer);
         return self;
      }
   }

   public static int numberAwareCompareTo(Comparable self, Comparable other) {
      NumberAwareComparator<Comparable> numberAwareComparator = new NumberAwareComparator();
      return numberAwareComparator.compare(self, other);
   }

   public static <T> Iterator<T> unique(Iterator<T> self, Closure closure) {
      return toList(unique((Collection)toList(self), (Closure)closure)).listIterator();
   }

   public static <T> Collection<T> unique(Collection<T> self, Closure closure) {
      int params = closure.getMaximumNumberOfParameters();
      if (params == 1) {
         unique((Collection)self, (Comparator)(new OrderBy(closure)));
      } else {
         unique((Collection)self, (Comparator)(new ClosureComparator(closure)));
      }

      return self;
   }

   public static <T> Iterator<T> unique(Iterator<T> self, Comparator<T> comparator) {
      return toList(unique((Collection)toList(self), (Comparator)comparator)).listIterator();
   }

   public static <T> Collection<T> unique(Collection<T> self, Comparator<T> comparator) {
      List<T> answer = new ArrayList();
      Iterator i$ = self.iterator();

      while(i$.hasNext()) {
         T t = i$.next();
         boolean duplicated = false;
         Iterator i$ = answer.iterator();

         while(i$.hasNext()) {
            T t2 = i$.next();
            if (comparator.compare(t, t2) == 0) {
               duplicated = true;
               break;
            }
         }

         if (!duplicated) {
            answer.add(t);
         }
      }

      self.clear();
      self.addAll(answer);
      return self;
   }

   public static <T> T each(T self, Closure closure) {
      each(InvokerHelper.asIterator(self), closure);
      return self;
   }

   public static Object eachWithIndex(Object self, Closure closure) {
      int counter = 0;
      Iterator iter = InvokerHelper.asIterator(self);

      while(iter.hasNext()) {
         closure.call(new Object[]{iter.next(), counter++});
      }

      return self;
   }

   private static <T> Iterator<T> each(Iterator<T> iter, Closure closure) {
      while(iter.hasNext()) {
         closure.call(iter.next());
      }

      return iter;
   }

   public static <K, V> Map<K, V> each(Map<K, V> self, Closure closure) {
      Iterator i$ = self.entrySet().iterator();

      while(i$.hasNext()) {
         Entry entry = (Entry)i$.next();
         callClosureForMapEntry(closure, entry);
      }

      return self;
   }

   public static <K, V> Map<K, V> reverseEach(Map<K, V> self, Closure closure) {
      Iterator entries = reverse(self.entrySet().iterator());

      while(entries.hasNext()) {
         callClosureForMapEntry(closure, (Entry)entries.next());
      }

      return self;
   }

   public static <K, V> Map<K, V> eachWithIndex(Map<K, V> self, Closure closure) {
      int counter = 0;
      Iterator i$ = self.entrySet().iterator();

      while(i$.hasNext()) {
         Entry entry = (Entry)i$.next();
         callClosureForMapEntryAndCounter(closure, entry, counter++);
      }

      return self;
   }

   public static <T> List<T> reverseEach(List<T> self, Closure closure) {
      each((Iterator)(new ReverseListIterator(self)), closure);
      return self;
   }

   public static <T> T[] reverseEach(T[] self, Closure closure) {
      each((Iterator)(new ReverseListIterator(Arrays.asList(self))), closure);
      return self;
   }

   public static <T> T[] reverse(T[] self) {
      return (Object[])toList((Iterator)(new ReverseListIterator(Arrays.asList(self)))).toArray();
   }

   public static boolean every(Object self, Closure closure) {
      Iterator iter = InvokerHelper.asIterator(self);

      do {
         if (!iter.hasNext()) {
            return true;
         }
      } while(DefaultTypeTransformation.castToBoolean(closure.call(iter.next())));

      return false;
   }

   public static <K, V> boolean every(Map<K, V> self, Closure closure) {
      Iterator i$ = self.entrySet().iterator();

      Entry entry;
      do {
         if (!i$.hasNext()) {
            return true;
         }

         entry = (Entry)i$.next();
      } while(DefaultTypeTransformation.castToBoolean(callClosureForMapEntry(closure, entry)));

      return false;
   }

   public static boolean every(Object self) {
      Iterator iter = InvokerHelper.asIterator(self);

      do {
         if (!iter.hasNext()) {
            return true;
         }
      } while(DefaultTypeTransformation.castToBoolean(iter.next()));

      return false;
   }

   public static boolean any(Object self, Closure closure) {
      Iterator iter = InvokerHelper.asIterator(self);

      do {
         if (!iter.hasNext()) {
            return false;
         }
      } while(!DefaultTypeTransformation.castToBoolean(closure.call(iter.next())));

      return true;
   }

   public static <K, V> boolean any(Map<K, V> self, Closure closure) {
      Iterator i$ = self.entrySet().iterator();

      Entry entry;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         entry = (Entry)i$.next();
      } while(!DefaultTypeTransformation.castToBoolean(callClosureForMapEntry(closure, entry)));

      return true;
   }

   public static boolean any(Object self) {
      Iterator iter = InvokerHelper.asIterator(self);

      do {
         if (!iter.hasNext()) {
            return false;
         }
      } while(!DefaultTypeTransformation.castToBoolean(iter.next()));

      return true;
   }

   public static Collection grep(Object self, Object filter) {
      Collection answer = createSimilarOrDefaultCollection(self);
      MetaClass metaClass = InvokerHelper.getMetaClass(filter);
      Iterator iter = InvokerHelper.asIterator(self);

      while(iter.hasNext()) {
         Object object = iter.next();
         if (DefaultTypeTransformation.castToBoolean(metaClass.invokeMethod(filter, "isCase", object))) {
            answer.add(object);
         }
      }

      return answer;
   }

   public static Number count(Iterator self, Object value) {
      long answer = 0L;

      while(self.hasNext()) {
         if (DefaultTypeTransformation.compareEqual(self.next(), value)) {
            ++answer;
         }
      }

      if (answer <= 2147483647L) {
         return (new Long(answer)).intValue();
      } else {
         return answer;
      }
   }

   public static Number count(Collection self, Object value) {
      return count(self.iterator(), value);
   }

   public static Number count(Object[] self, Object value) {
      return count((Collection)Arrays.asList(self), (Object)value);
   }

   public static Number count(int[] self, Object value) {
      return count(InvokerHelper.asIterator(self), value);
   }

   public static Number count(long[] self, Object value) {
      return count(InvokerHelper.asIterator(self), value);
   }

   public static Number count(short[] self, Object value) {
      return count(InvokerHelper.asIterator(self), value);
   }

   public static Number count(char[] self, Object value) {
      return count(InvokerHelper.asIterator(self), value);
   }

   public static Number count(boolean[] self, Object value) {
      return count(InvokerHelper.asIterator(self), value);
   }

   public static Number count(double[] self, Object value) {
      return count(InvokerHelper.asIterator(self), value);
   }

   public static Number count(float[] self, Object value) {
      return count(InvokerHelper.asIterator(self), value);
   }

   public static Number count(byte[] self, Object value) {
      return count(InvokerHelper.asIterator(self), value);
   }

   public static <T> List<T> toList(Collection<T> self) {
      List<T> answer = new ArrayList(self.size());
      answer.addAll(self);
      return answer;
   }

   public static <T> List<T> toList(Iterator<T> self) {
      ArrayList answer = new ArrayList();

      while(self.hasNext()) {
         answer.add(self.next());
      }

      return answer;
   }

   public static <T> List<T> toList(Enumeration<T> self) {
      ArrayList answer = new ArrayList();

      while(self.hasMoreElements()) {
         answer.add(self.nextElement());
      }

      return answer;
   }

   public static List collect(Object self, Closure closure) {
      return (List)collect((Object)self, new ArrayList(), closure);
   }

   public static Collection collect(Object self, Collection collection, Closure closure) {
      Iterator iter = InvokerHelper.asIterator(self);

      while(iter.hasNext()) {
         collection.add(closure.call(iter.next()));
      }

      return collection;
   }

   public static List collect(Collection self, Closure closure) {
      return (List)collect((Collection)self, new ArrayList(self.size()), closure);
   }

   public static Collection collect(Collection self, Collection collection, Closure closure) {
      Iterator iter = self.iterator();

      while(iter.hasNext()) {
         collection.add(closure.call(iter.next()));
         if (closure.getDirective() == 1) {
            break;
         }
      }

      return collection;
   }

   public static List collectAll(Collection self, Closure closure) {
      return (List)collectAll(self, new ArrayList(self.size()), closure);
   }

   public static Collection collectAll(Collection self, Collection collection, Closure closure) {
      Iterator iter = self.iterator();

      while(iter.hasNext()) {
         Object o = iter.next();
         if (o instanceof Collection) {
            Collection c = (Collection)o;
            collection.add(collectAll(c, createSimilarCollection(collection, c.size()), closure));
         } else {
            collection.add(closure.call(o));
         }

         if (closure.getDirective() == 1) {
            break;
         }
      }

      return collection;
   }

   public static Collection collect(Map self, Collection collection, Closure closure) {
      Iterator i$ = self.entrySet().iterator();

      while(i$.hasNext()) {
         Object entry = i$.next();
         collection.add(callClosureForMapEntry(closure, (Entry)entry));
      }

      return collection;
   }

   public static List collect(Map self, Closure closure) {
      return (List)collect((Map)self, new ArrayList(self.size()), closure);
   }

   public static Object find(Object self, Closure closure) {
      Iterator iter = InvokerHelper.asIterator(self);

      Object value;
      do {
         if (!iter.hasNext()) {
            return null;
         }

         value = iter.next();
      } while(!DefaultTypeTransformation.castToBoolean(closure.call(value)));

      return value;
   }

   public static Object findResult(Object self, Object defaultResult, Closure closure) {
      Object result = findResult(self, closure);
      return result == null ? defaultResult : result;
   }

   public static Object findResult(Object self, Closure closure) {
      Iterator iter = InvokerHelper.asIterator(self);

      Object result;
      do {
         if (!iter.hasNext()) {
            return null;
         }

         Object value = iter.next();
         result = closure.call(value);
      } while(result == null);

      return result;
   }

   public static <T> T find(Collection<T> self, Closure closure) {
      Iterator i$ = self.iterator();

      Object value;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         value = i$.next();
      } while(!DefaultTypeTransformation.castToBoolean(closure.call(value)));

      return value;
   }

   public static <T> Object findResult(Collection<T> self, Object defaultResult, Closure closure) {
      Object result = findResult(self, closure);
      return result == null ? defaultResult : result;
   }

   public static <T> Object findResult(Collection<T> self, Closure closure) {
      Iterator i$ = self.iterator();

      Object result;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         T value = i$.next();
         result = closure.call(value);
      } while(result == null);

      return result;
   }

   public static <K, V> Entry<K, V> find(Map<K, V> self, Closure closure) {
      Iterator i$ = self.entrySet().iterator();

      Entry entry;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         entry = (Entry)i$.next();
      } while(!DefaultTypeTransformation.castToBoolean(callClosureForMapEntry(closure, entry)));

      return entry;
   }

   public static <K, V> Object findResult(Map<K, V> self, Object defaultResult, Closure closure) {
      Object result = findResult(self, closure);
      return result == null ? defaultResult : result;
   }

   public static <K, V> Object findResult(Map<K, V> self, Closure closure) {
      Iterator i$ = self.entrySet().iterator();

      Object result;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         Entry<K, V> entry = (Entry)i$.next();
         result = callClosureForMapEntry(closure, entry);
      } while(result == null);

      return result;
   }

   public static <T> Collection<T> findAll(Collection<T> self, Closure closure) {
      Collection<T> answer = createSimilarCollection(self);
      Iterator<T> iter = self.iterator();
      return findAll(closure, answer, iter);
   }

   public static Collection findAll(Object self, Closure closure) {
      List answer = new ArrayList();
      Iterator iter = InvokerHelper.asIterator(self);
      return findAll((Closure)closure, (Collection)answer, (Iterator)iter);
   }

   private static <T> Collection<T> findAll(Closure closure, Collection<T> answer, Iterator<T> iter) {
      while(iter.hasNext()) {
         T value = iter.next();
         if (DefaultTypeTransformation.castToBoolean(closure.call(value))) {
            answer.add(value);
         }
      }

      return answer;
   }

   public static boolean containsAll(Collection self, Object[] items) {
      return self.containsAll(Arrays.asList(items));
   }

   public static <T> boolean removeAll(Collection<T> self, T[] items) {
      return self.removeAll(Arrays.asList(items));
   }

   public static <T> boolean retainAll(Collection<T> self, T[] items) {
      return self.retainAll(Arrays.asList(items));
   }

   public static boolean retainAll(Collection self, Closure closure) {
      Iterator iter = InvokerHelper.asIterator(self);
      boolean result = false;

      while(iter.hasNext()) {
         Object value = iter.next();
         if (!DefaultTypeTransformation.castToBoolean(closure.call(value))) {
            iter.remove();
            result = true;
         }
      }

      return result;
   }

   public static boolean removeAll(Collection self, Closure closure) {
      Iterator iter = InvokerHelper.asIterator(self);
      boolean result = false;

      while(iter.hasNext()) {
         Object value = iter.next();
         if (DefaultTypeTransformation.castToBoolean(closure.call(value))) {
            iter.remove();
            result = true;
         }
      }

      return result;
   }

   public static <T> boolean addAll(Collection<T> self, T[] items) {
      return self.addAll(Arrays.asList(items));
   }

   public static <T> boolean addAll(List<T> self, int index, T[] items) {
      return self.addAll(index, Arrays.asList(items));
   }

   public static Collection split(Object self, Closure closure) {
      List accept = new ArrayList();
      List reject = new ArrayList();
      return split(closure, accept, reject, InvokerHelper.asIterator(self));
   }

   public static <T> Collection<Collection<T>> split(Collection<T> self, Closure closure) {
      Collection<T> accept = createSimilarCollection(self);
      Collection<T> reject = createSimilarCollection(self);
      Iterator<T> iter = self.iterator();
      return split(closure, accept, reject, iter);
   }

   private static <T> Collection<Collection<T>> split(Closure closure, Collection<T> accept, Collection<T> reject, Iterator<T> iter) {
      ArrayList answer = new ArrayList();

      while(iter.hasNext()) {
         T value = iter.next();
         if (DefaultTypeTransformation.castToBoolean(closure.call(value))) {
            accept.add(value);
         } else {
            reject.add(value);
         }
      }

      answer.add(accept);
      answer.add(reject);
      return answer;
   }

   public static List combinations(Collection self) {
      return GroovyCollections.combinations(self);
   }

   public static <T> Set<List<T>> subsequences(List<T> self) {
      return GroovyCollections.subsequences(self);
   }

   public static <T> Set<List<T>> permutations(List<T> self) {
      Set<List<T>> ans = new HashSet();
      PermutationGenerator generator = new PermutationGenerator(self);

      while(generator.hasNext()) {
         ans.add(generator.next());
      }

      return ans;
   }

   public static <T> Iterator<List<T>> eachPermutation(Collection<T> self, Closure closure) {
      PermutationGenerator generator = new PermutationGenerator(self);

      while(generator.hasNext()) {
         closure.call(generator.next());
      }

      return generator;
   }

   public static List transpose(List self) {
      return GroovyCollections.transpose(self);
   }

   public static <K, V> Map<K, V> findAll(Map<K, V> self, Closure closure) {
      Map<K, V> answer = createSimilarMap(self);
      Iterator i$ = self.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<K, V> entry = (Entry)i$.next();
         if (DefaultTypeTransformation.castToBoolean(callClosureForMapEntry(closure, entry))) {
            answer.put(entry.getKey(), entry.getValue());
         }
      }

      return answer;
   }

   public static <T> Map<Object, List<T>> groupBy(Collection<T> self, Closure closure) {
      Map<Object, List<T>> answer = new LinkedHashMap();
      Iterator i$ = self.iterator();

      while(i$.hasNext()) {
         T element = i$.next();
         Object value = closure.call(element);
         groupAnswer(answer, element, value);
      }

      return answer;
   }

   public static <K, V> Map<Object, List<Entry<K, V>>> groupEntriesBy(Map<K, V> self, Closure closure) {
      Map<Object, List<Entry<K, V>>> answer = new LinkedHashMap();
      Iterator i$ = self.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<K, V> entry = (Entry)i$.next();
         Object value = callClosureForMapEntry(closure, entry);
         groupAnswer(answer, entry, value);
      }

      return answer;
   }

   public static <K, V> Map<Object, Map<K, V>> groupBy(Map<K, V> self, Closure closure) {
      Map<Object, List<Entry<K, V>>> initial = groupEntriesBy(self, closure);
      Map<Object, Map<K, V>> answer = new LinkedHashMap();
      Iterator i$ = initial.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<Object, List<Entry<K, V>>> outer = (Entry)i$.next();
         Object key = outer.getKey();
         List<Entry<K, V>> entries = (List)outer.getValue();
         Map<K, V> target = createSimilarMap(self);
         putAll(target, entries);
         answer.put(key, target);
      }

      return answer;
   }

   protected static <T> void groupAnswer(Map<Object, List<T>> answer, T element, Object value) {
      if (answer.containsKey(value)) {
         ((List)answer.get(value)).add(element);
      } else {
         List<T> groupedElements = new ArrayList();
         groupedElements.add(element);
         answer.put(value, groupedElements);
      }

   }

   protected static Object callClosureForMapEntry(Closure closure, Entry entry) {
      return closure.getMaximumNumberOfParameters() == 2 ? closure.call(new Object[]{entry.getKey(), entry.getValue()}) : closure.call((Object)entry);
   }

   protected static Object callClosureForLine(Closure closure, String line, int counter) {
      return closure.getMaximumNumberOfParameters() == 2 ? closure.call(new Object[]{line, counter}) : closure.call((Object)line);
   }

   protected static Object callClosureForMapEntryAndCounter(Closure closure, Entry entry, int counter) {
      if (closure.getMaximumNumberOfParameters() == 3) {
         return closure.call(new Object[]{entry.getKey(), entry.getValue(), counter});
      } else {
         return closure.getMaximumNumberOfParameters() == 2 ? closure.call(new Object[]{entry, counter}) : closure.call((Object)entry);
      }
   }

   public static Object inject(Collection self, Object value, Closure closure) {
      return inject(self.iterator(), value, closure);
   }

   public static Object inject(Iterator self, Object value, Closure closure) {
      for(Object[] params = new Object[2]; self.hasNext(); value = closure.call(params)) {
         Object item = self.next();
         params[0] = value;
         params[1] = item;
      }

      return value;
   }

   public static Object inject(Object self, Object value, Closure closure) {
      Iterator iter = InvokerHelper.asIterator(self);
      return inject(iter, value, closure);
   }

   public static Object inject(Object[] self, Object initialValue, Closure closure) {
      Object[] params = new Object[2];
      Object value = initialValue;
      Object[] arr$ = self;
      int len$ = self.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Object next = arr$[i$];
         params[0] = value;
         params[1] = next;
         value = closure.call(params);
      }

      return value;
   }

   public static Object sum(Collection self) {
      return sum(self, (Object)null, true);
   }

   public static Object sum(Object[] self) {
      return sum(toList(self), (Object)null, true);
   }

   public static Object sum(Iterator<Object> self) {
      return sum(toList(self), (Object)null, true);
   }

   public static Object sum(Collection self, Object initialValue) {
      return sum(self, initialValue, false);
   }

   public static Object sum(Object[] self, Object initialValue) {
      return sum(toList(self), initialValue, false);
   }

   public static Object sum(Iterator<Object> self, Object initialValue) {
      return sum(toList(self), initialValue, false);
   }

   private static Object sum(Collection self, Object initialValue, boolean first) {
      Object result = initialValue;
      Object[] param = new Object[1];
      Iterator i$ = self.iterator();

      while(i$.hasNext()) {
         Object next = i$.next();
         param[0] = next;
         if (first) {
            result = param[0];
            first = false;
         } else {
            MetaClass metaClass = InvokerHelper.getMetaClass(result);
            result = metaClass.invokeMethod(result, "plus", param);
         }
      }

      return result;
   }

   public static Object sum(Collection self, Closure closure) {
      return sum(self, (Object)null, closure, true);
   }

   public static Object sum(Object[] self, Closure closure) {
      return sum(toList(self), (Object)null, closure, true);
   }

   public static Object sum(Iterator<Object> self, Closure closure) {
      return sum(toList(self), (Object)null, closure, true);
   }

   public static Object sum(Collection self, Object initialValue, Closure closure) {
      return sum(self, initialValue, closure, false);
   }

   public static Object sum(Object[] self, Object initialValue, Closure closure) {
      return sum(toList(self), initialValue, closure, false);
   }

   public static Object sum(Iterator<Object> self, Object initialValue, Closure closure) {
      return sum(toList(self), initialValue, closure, false);
   }

   private static Object sum(Collection self, Object initialValue, Closure closure, boolean first) {
      Object result = initialValue;
      Object[] closureParam = new Object[1];
      Object[] plusParam = new Object[1];
      Iterator i$ = self.iterator();

      while(i$.hasNext()) {
         Object next = i$.next();
         closureParam[0] = next;
         plusParam[0] = closure.call(closureParam);
         if (first) {
            result = plusParam[0];
            first = false;
         } else {
            MetaClass metaClass = InvokerHelper.getMetaClass(result);
            result = metaClass.invokeMethod(result, "plus", plusParam);
         }
      }

      return result;
   }

   public static String join(Iterator<Object> self, String separator) {
      return join((Collection)toList(self), separator);
   }

   public static String join(Collection self, String separator) {
      StringBuilder buffer = new StringBuilder();
      boolean first = true;
      if (separator == null) {
         separator = "";
      }

      Object value;
      for(Iterator i$ = self.iterator(); i$.hasNext(); buffer.append(InvokerHelper.toString(value))) {
         value = i$.next();
         if (first) {
            first = false;
         } else {
            buffer.append(separator);
         }
      }

      return buffer.toString();
   }

   public static String join(Object[] self, String separator) {
      StringBuilder buffer = new StringBuilder();
      boolean first = true;
      if (separator == null) {
         separator = "";
      }

      Object[] arr$ = self;
      int len$ = self.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Object next = arr$[i$];
         String value = InvokerHelper.toString(next);
         if (first) {
            first = false;
         } else {
            buffer.append(separator);
         }

         buffer.append(value);
      }

      return buffer.toString();
   }

   public static <T> T min(Collection<T> self) {
      return GroovyCollections.min(self);
   }

   public static <T> T min(Iterator<T> self) {
      return min((Collection)toList(self));
   }

   public static <T> T min(T[] self) {
      return min((Collection)toList(self));
   }

   public static <T> T min(Collection<T> self, Comparator<T> comparator) {
      T answer = null;
      Iterator i$ = self.iterator();

      while(true) {
         Object value;
         do {
            if (!i$.hasNext()) {
               return answer;
            }

            value = i$.next();
         } while(answer != null && comparator.compare(value, answer) >= 0);

         answer = value;
      }
   }

   public static <T> T min(Iterator<T> self, Comparator<T> comparator) {
      return min((Collection)toList(self), (Comparator)comparator);
   }

   public static <T> T min(T[] self, Comparator<T> comparator) {
      return min((Collection)toList(self), (Comparator)comparator);
   }

   public static <T> T min(Collection<T> self, Closure closure) {
      int params = closure.getMaximumNumberOfParameters();
      if (params != 1) {
         return min((Collection)self, (Comparator)(new ClosureComparator(closure)));
      } else {
         T answer = null;
         Object answer_value = null;
         Iterator i$ = self.iterator();

         while(true) {
            Object item;
            Object value;
            do {
               if (!i$.hasNext()) {
                  return answer;
               }

               item = i$.next();
               value = closure.call(item);
            } while(answer != null && !ScriptBytecodeAdapter.compareLessThan(value, answer_value));

            answer = item;
            answer_value = value;
         }
      }
   }

   public static <K, V> Entry<K, V> min(Map<K, V> self, Closure closure) {
      return (Entry)min((Collection)self.entrySet(), (Closure)closure);
   }

   public static <K, V> Entry<K, V> max(Map<K, V> self, Closure closure) {
      return (Entry)max((Collection)self.entrySet(), (Closure)closure);
   }

   public static <T> T min(Iterator<T> self, Closure closure) {
      return min((Collection)toList(self), (Closure)closure);
   }

   public static <T> T min(T[] self, Closure closure) {
      return min((Collection)toList(self), (Closure)closure);
   }

   public static <T> T max(Collection<T> self) {
      return GroovyCollections.max(self);
   }

   public static <T> T max(Iterator<T> self) {
      return max((Collection)toList(self));
   }

   public static <T> T max(T[] self) {
      return max((Collection)toList(self));
   }

   public static <T> T max(Collection<T> self, Closure closure) {
      int params = closure.getMaximumNumberOfParameters();
      if (params != 1) {
         return max((Collection)self, (Comparator)(new ClosureComparator(closure)));
      } else {
         T answer = null;
         Object answerValue = null;
         Iterator i$ = self.iterator();

         while(true) {
            Object item;
            Object value;
            do {
               if (!i$.hasNext()) {
                  return answer;
               }

               item = i$.next();
               value = closure.call(item);
            } while(answer != null && !ScriptBytecodeAdapter.compareLessThan(answerValue, value));

            answer = item;
            answerValue = value;
         }
      }
   }

   public static <T> T max(Iterator<T> self, Closure closure) {
      return max((Collection)toList(self), (Closure)closure);
   }

   public static <T> T max(T[] self, Closure closure) {
      return max((Collection)toList(self), (Closure)closure);
   }

   public static <T> T max(Collection<T> self, Comparator<T> comparator) {
      T answer = null;
      Iterator i$ = self.iterator();

      while(true) {
         Object value;
         do {
            if (!i$.hasNext()) {
               return answer;
            }

            value = i$.next();
         } while(answer != null && comparator.compare(value, answer) <= 0);

         answer = value;
      }
   }

   public static <T> T max(Iterator<T> self, Comparator<T> comparator) {
      return max((Collection)toList(self), (Comparator)comparator);
   }

   public static <T> T max(T[] self, Comparator<T> comparator) {
      return max((Collection)toList(self), (Comparator)comparator);
   }

   public static int size(Iterator self) {
      int count;
      for(count = 0; self.hasNext(); ++count) {
         self.next();
      }

      return count;
   }

   public static int size(String text) {
      return text.length();
   }

   public static int size(StringBuffer buffer) {
      return buffer.length();
   }

   public static long size(File self) {
      return self.length();
   }

   public static long size(Matcher self) {
      return (long)getCount(self);
   }

   public static int size(Object[] self) {
      return self.length;
   }

   public static CharSequence getAt(CharSequence text, int index) {
      index = normaliseIndex(index, text.length());
      return text.subSequence(index, index + 1);
   }

   public static String getAt(String text, int index) {
      index = normaliseIndex(index, text.length());
      return text.substring(index, index + 1);
   }

   public static CharSequence getAt(CharSequence text, Range range) {
      int from = normaliseIndex(DefaultTypeTransformation.intUnbox(range.getFrom()), text.length());
      int to = normaliseIndex(DefaultTypeTransformation.intUnbox(range.getTo()), text.length());
      boolean reverse = range.isReverse();
      if (from > to) {
         int tmp = from;
         from = to;
         to = tmp;
         reverse = !reverse;
      }

      CharSequence sequence = text.subSequence(from, to + 1);
      return (CharSequence)(reverse ? reverse((String)sequence) : sequence);
   }

   public static CharSequence getAt(CharSequence text, IntRange range) {
      return getAt((CharSequence)text, (Range)range);
   }

   public static CharSequence getAt(CharSequence text, EmptyRange range) {
      return "";
   }

   public static String getAt(String text, IntRange range) {
      return getAt((String)text, (Range)range);
   }

   public static String getAt(String text, EmptyRange range) {
      return "";
   }

   public static String getAt(String text, Range range) {
      int from = normaliseIndex(DefaultTypeTransformation.intUnbox(range.getFrom()), text.length());
      int to = normaliseIndex(DefaultTypeTransformation.intUnbox(range.getTo()), text.length());
      boolean reverse = range.isReverse();
      if (from > to) {
         int tmp = to;
         to = from;
         from = tmp;
         reverse = !reverse;
      }

      String answer = text.substring(from, to + 1);
      if (reverse) {
         answer = reverse(answer);
      }

      return answer;
   }

   public static String reverse(String self) {
      return (new StringBuilder(self)).reverse().toString();
   }

   public static String stripMargin(String self) {
      return stripMargin(self, '|');
   }

   public static String stripMargin(String self, String marginChar) {
      return marginChar != null && marginChar.length() != 0 ? stripMargin(self, marginChar.charAt(0)) : stripMargin(self, '|');
   }

   public static String stripMargin(String self, char marginChar) {
      if (self.length() == 0) {
         return self;
      } else {
         try {
            StringBuilder builder = new StringBuilder();
            Iterator i$ = readLines(self).iterator();

            while(i$.hasNext()) {
               String line = (String)i$.next();
               builder.append(stripMarginFromLine(line, marginChar));
               builder.append("\n");
            }

            if (!self.endsWith("\n")) {
               builder.deleteCharAt(builder.length() - 1);
            }

            return builder.toString();
         } catch (IOException var5) {
            return self;
         }
      }
   }

   private static String stripMarginFromLine(String line, char marginChar) {
      int length = line.length();

      int index;
      for(index = 0; index < length && line.charAt(index) <= ' '; ++index) {
      }

      return index < length && line.charAt(index) == marginChar ? line.substring(index + 1) : line;
   }

   public static String stripIndent(String self) {
      if (self.length() == 0) {
         return self;
      } else {
         int runningCount = -1;

         try {
            Iterator i$ = readLines(self).iterator();

            while(i$.hasNext()) {
               String line = (String)i$.next();
               if (!isAllWhitespace(line)) {
                  if (runningCount == -1) {
                     runningCount = line.length();
                  }

                  runningCount = findMinimumLeadingSpaces(line, runningCount);
                  if (runningCount == 0) {
                     break;
                  }
               }
            }
         } catch (IOException var4) {
         }

         return stripIndent(self, runningCount == -1 ? 0 : runningCount);
      }
   }

   public static boolean isAllWhitespace(String self) {
      for(int i = 0; i < self.length(); ++i) {
         if (!Character.isWhitespace(self.charAt(i))) {
            return false;
         }
      }

      return true;
   }

   private static int findMinimumLeadingSpaces(String line, int count) {
      int length = line.length();

      int index;
      for(index = 0; index < length && index < count && Character.isWhitespace(line.charAt(index)); ++index) {
      }

      return index;
   }

   public static String stripIndent(String self, int numChars) {
      if (self.length() != 0 && numChars > 0) {
         try {
            StringBuilder builder = new StringBuilder();

            for(Iterator i$ = readLines(self).iterator(); i$.hasNext(); builder.append("\n")) {
               String line = (String)i$.next();
               if (!isAllWhitespace(line)) {
                  builder.append(stripIndentFromLine(line, numChars));
               }
            }

            if (!self.endsWith("\n")) {
               builder.deleteCharAt(builder.length() - 1);
            }

            return builder.toString();
         } catch (IOException var5) {
            return self;
         }
      } else {
         return self;
      }
   }

   private static String stripIndentFromLine(String line, int numChars) {
      int length = line.length();
      return numChars <= length ? line.substring(numChars) : "";
   }

   public static URL toURL(String self) throws MalformedURLException {
      return new URL(self);
   }

   public static URI toURI(String self) throws URISyntaxException {
      return new URI(self);
   }

   public static Pattern bitwiseNegate(String self) {
      return Pattern.compile(self);
   }

   public static String replaceFirst(String self, Pattern pattern, String replacement) {
      return pattern.matcher(self).replaceFirst(replacement);
   }

   public static String replaceAll(String self, Pattern pattern, String replacement) {
      return pattern.matcher(self).replaceAll(replacement);
   }

   public static String tr(String self, String sourceSet, String replacementSet) throws ClassNotFoundException {
      return (String)InvokerHelper.invokeStaticMethod((String)"org.codehaus.groovy.util.StringUtil", "tr", new Object[]{self, sourceSet, replacementSet});
   }

   public static boolean matches(String self, Pattern pattern) {
      return pattern.matcher(self).matches();
   }

   public static String find(String self, String regex) {
      return find(self, Pattern.compile(regex));
   }

   public static String find(String self, Pattern pattern) {
      Matcher matcher = pattern.matcher(self);
      return matcher.find() ? matcher.group(0) : null;
   }

   public static String find(String self, String regex, Closure closure) {
      return find(self, Pattern.compile(regex), closure);
   }

   public static String find(String self, Pattern pattern, Closure closure) {
      Matcher matcher = pattern.matcher(self);
      if (!matcher.find()) {
         return null;
      } else if (!hasGroup(matcher)) {
         return InvokerHelper.toString(closure.call((Object)matcher.group(0)));
      } else {
         int count = matcher.groupCount();
         List groups = new ArrayList(count);

         for(int i = 0; i <= count; ++i) {
            groups.add(matcher.group(i));
         }

         return InvokerHelper.toString(closure.call((Object)groups));
      }
   }

   public static List findAll(String self, String regex) {
      return findAll(self, Pattern.compile(regex));
   }

   public static List findAll(String self, Pattern pattern) {
      Matcher matcher = pattern.matcher(self);
      List list = new ArrayList();
      Iterator iter = iterator(matcher);

      while(iter.hasNext()) {
         if (hasGroup(matcher)) {
            list.add(((List)iter.next()).get(0));
         } else {
            list.add(iter.next());
         }
      }

      return list;
   }

   public static List findAll(String self, String regex, Closure closure) {
      return findAll(self, Pattern.compile(regex), closure);
   }

   public static List findAll(String self, Pattern pattern, Closure closure) {
      Matcher matcher = pattern.matcher(self);
      return collect((Object)matcher, closure);
   }

   public static String replaceAll(String self, String regex, Closure closure) {
      return replaceAll(self, Pattern.compile(regex), closure);
   }

   private static String getReplacement(Matcher matcher, Closure closure) {
      if (!hasGroup(matcher)) {
         return InvokerHelper.toString(closure.call((Object)matcher.group()));
      } else {
         int count = matcher.groupCount();
         List<String> groups = new ArrayList();

         for(int i = 0; i <= count; ++i) {
            groups.add(matcher.group(i));
         }

         return closure.getParameterTypes().length == 1 && closure.getParameterTypes()[0] == Object[].class ? InvokerHelper.toString(closure.call(groups.toArray())) : InvokerHelper.toString(closure.call((Object)groups));
      }
   }

   public static String replaceAll(String self, Pattern pattern, Closure closure) {
      Matcher matcher = pattern.matcher(self);
      if (!matcher.find()) {
         return self;
      } else {
         StringBuffer sb = new StringBuffer(self.length() + 16);

         do {
            String replacement = getReplacement(matcher, closure);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
         } while(matcher.find());

         matcher.appendTail(sb);
         return sb.toString();
      }
   }

   private static String getPadding(String padding, int length) {
      return padding.length() < length ? multiply((String)padding, (Number)(length / padding.length() + 1)).substring(0, length) : padding.substring(0, length);
   }

   public static String padLeft(String self, Number numberOfChars, String padding) {
      int numChars = numberOfChars.intValue();
      return numChars <= self.length() ? self : getPadding(padding, numChars - self.length()) + self;
   }

   public static String padLeft(String self, Number numberOfChars) {
      return padLeft(self, numberOfChars, " ");
   }

   public static String padRight(String self, Number numberOfChars, String padding) {
      int numChars = numberOfChars.intValue();
      return numChars <= self.length() ? self : self + getPadding(padding, numChars - self.length());
   }

   public static String padRight(String self, Number numberOfChars) {
      return padRight(self, numberOfChars, " ");
   }

   public static String center(String self, Number numberOfChars, String padding) {
      int numChars = numberOfChars.intValue();
      if (numChars <= self.length()) {
         return self;
      } else {
         int charsToAdd = numChars - self.length();
         String semiPad = charsToAdd % 2 == 1 ? getPadding(padding, charsToAdd / 2 + 1) : getPadding(padding, charsToAdd / 2);
         return charsToAdd % 2 == 0 ? semiPad + self + semiPad : semiPad.substring(0, charsToAdd / 2) + self + semiPad;
      }
   }

   public static String center(String self, Number numberOfChars) {
      return center(self, numberOfChars, " ");
   }

   public static Object getAt(Matcher matcher, int idx) {
      try {
         int count = getCount(matcher);
         if (idx >= -count && idx < count) {
            idx = normaliseIndex(idx, count);
            Iterator iter = iterator(matcher);
            Object result = null;

            for(int i = 0; i <= idx; ++i) {
               result = iter.next();
            }

            return result;
         } else {
            throw new IndexOutOfBoundsException("index is out of range " + -count + ".." + (count - 1) + " (index = " + idx + ")");
         }
      } catch (IllegalStateException var6) {
         return null;
      }
   }

   public static void setIndex(Matcher matcher, int idx) {
      int count = getCount(matcher);
      if (idx >= -count && idx < count) {
         if (idx == 0) {
            matcher.reset();
         } else {
            int i;
            if (idx > 0) {
               matcher.reset();

               for(i = 0; i < idx; ++i) {
                  matcher.find();
               }
            } else if (idx < 0) {
               matcher.reset();
               idx += getCount(matcher);

               for(i = 0; i < idx; ++i) {
                  matcher.find();
               }
            }
         }

      } else {
         throw new IndexOutOfBoundsException("index is out of range " + -count + ".." + (count - 1) + " (index = " + idx + ")");
      }
   }

   public static int getCount(Matcher matcher) {
      int counter = 0;
      matcher.reset();

      while(matcher.find()) {
         ++counter;
      }

      return counter;
   }

   public static boolean hasGroup(Matcher matcher) {
      return matcher.groupCount() > 0;
   }

   public static <T> List<T> getAt(List<T> self, Range range) {
      DefaultGroovyMethodsSupport.RangeInfo info = subListBorders(self.size(), range);
      List<T> answer = self.subList(info.from, info.to);
      if (info.reverse) {
         answer = reverse(answer);
      }

      return answer;
   }

   public static <T> List<T> getAt(List<T> self, EmptyRange range) {
      return new ArrayList();
   }

   public static <T> List<T> getAt(List<T> self, Collection indices) {
      List<T> answer = new ArrayList(indices.size());
      Iterator i$ = indices.iterator();

      while(i$.hasNext()) {
         Object value = i$.next();
         if (value instanceof Range) {
            answer.addAll(getAt(self, (Range)value));
         } else if (value instanceof List) {
            answer.addAll(getAt((List)self, (Collection)((List)value)));
         } else {
            int idx = DefaultTypeTransformation.intUnbox(value);
            answer.add(getAt(self, idx));
         }
      }

      return answer;
   }

   public static <T> List<T> getAt(T[] self, Collection indices) {
      List<T> answer = new ArrayList(indices.size());
      Iterator i$ = indices.iterator();

      while(i$.hasNext()) {
         Object value = i$.next();
         if (value instanceof Range) {
            answer.addAll(getAt(self, (Range)value));
         } else if (value instanceof Collection) {
            answer.addAll(getAt(self, (Collection)value));
         } else {
            int idx = DefaultTypeTransformation.intUnbox(value);
            answer.add(getAtImpl(self, idx));
         }
      }

      return answer;
   }

   public static CharSequence getAt(CharSequence self, Collection indices) {
      StringBuilder answer = new StringBuilder();
      Iterator i$ = indices.iterator();

      while(i$.hasNext()) {
         Object value = i$.next();
         if (value instanceof Range) {
            answer.append(getAt(self, (Range)value));
         } else if (value instanceof Collection) {
            answer.append(getAt(self, (Collection)value));
         } else {
            int idx = DefaultTypeTransformation.intUnbox(value);
            answer.append(getAt(self, idx));
         }
      }

      return answer.toString();
   }

   public static String getAt(String self, Collection indices) {
      return (String)getAt((CharSequence)self, (Collection)indices);
   }

   public static List getAt(Matcher self, Collection indices) {
      List result = new ArrayList();
      Iterator i$ = indices.iterator();

      while(i$.hasNext()) {
         Object value = i$.next();
         if (value instanceof Range) {
            result.addAll(getAt((Matcher)self, (Collection)((Range)value)));
         } else {
            int idx = DefaultTypeTransformation.intUnbox(value);
            result.add(getAt(self, idx));
         }
      }

      return result;
   }

   public static <K, V> Map<K, V> subMap(Map<K, V> map, Collection<K> keys) {
      Map<K, V> answer = new LinkedHashMap(keys.size());
      Iterator i$ = keys.iterator();

      while(i$.hasNext()) {
         K key = i$.next();
         answer.put(key, map.get(key));
      }

      return answer;
   }

   public static <K, V> V get(Map<K, V> map, K key, V defaultValue) {
      if (!map.containsKey(key)) {
         map.put(key, defaultValue);
      }

      return map.get(key);
   }

   public static <T> List<T> getAt(T[] array, Range range) {
      List<T> list = Arrays.asList(array);
      return getAt(list, range);
   }

   public static <T> List<T> getAt(T[] array, IntRange range) {
      List<T> list = Arrays.asList(array);
      return getAt((List)list, (Range)range);
   }

   public static <T> List<T> getAt(T[] array, EmptyRange range) {
      return new ArrayList();
   }

   public static <T> List<T> getAt(T[] array, ObjectRange range) {
      List<T> list = Arrays.asList(array);
      return getAt((List)list, (Range)range);
   }

   private static <T> T getAtImpl(T[] array, int idx) {
      return array[normaliseIndex(idx, array.length)];
   }

   public static <T> List<T> toList(T[] array) {
      return new ArrayList(Arrays.asList(array));
   }

   public static <T> T getAt(List<T> self, int idx) {
      int size = self.size();
      int i = normaliseIndex(idx, size);
      return i < size ? self.get(i) : null;
   }

   public static <T> T getAt(Iterator<T> self, int idx) {
      if (idx < 0) {
         List<T> list = toList(self);
         int adjustedIndex = idx + list.size();
         return adjustedIndex >= 0 && adjustedIndex < list.size() ? list.get(adjustedIndex) : null;
      } else {
         int count = 0;

         while(self.hasNext()) {
            if (count == idx) {
               return self.next();
            }

            ++count;
            self.next();
         }

         return null;
      }
   }

   public static <T> void putAt(List<T> self, int idx, T value) {
      int size = self.size();
      idx = normaliseIndex(idx, size);
      if (idx < size) {
         self.set(idx, value);
      } else {
         while(true) {
            if (size >= idx) {
               self.add(idx, value);
               break;
            }

            self.add(size++, (Object)null);
         }
      }

   }

   public static void putAt(StringBuffer self, IntRange range, Object value) {
      DefaultGroovyMethodsSupport.RangeInfo info = subListBorders(self.length(), range);
      self.replace(info.from, info.to, value.toString());
   }

   public static void putAt(StringBuffer self, EmptyRange range, Object value) {
      DefaultGroovyMethodsSupport.RangeInfo info = subListBorders(self.length(), range);
      self.replace(info.from, info.to, value.toString());
   }

   public static void putAt(List self, EmptyRange range, Object value) {
      DefaultGroovyMethodsSupport.RangeInfo info = subListBorders(self.size(), range);
      List sublist = self.subList(info.from, info.to);
      sublist.clear();
      if (value instanceof Collection) {
         Collection col = (Collection)value;
         if (col.isEmpty()) {
            return;
         }

         sublist.addAll(col);
      } else {
         sublist.add(value);
      }

   }

   public static void putAt(List self, EmptyRange range, Collection value) {
      putAt((List)self, (EmptyRange)range, (Object)value);
   }

   private static <T> List<T> resizeListWithRangeAndGetSublist(List<T> self, IntRange range) {
      DefaultGroovyMethodsSupport.RangeInfo info = subListBorders(self.size(), range);
      int size = self.size();
      if (info.to >= size) {
         while(size < info.to) {
            self.add(size++, (Object)null);
         }
      }

      List<T> sublist = self.subList(info.from, info.to);
      sublist.clear();
      return sublist;
   }

   public static void putAt(List self, IntRange range, Collection col) {
      List sublist = resizeListWithRangeAndGetSublist(self, range);
      if (!col.isEmpty()) {
         sublist.addAll(col);
      }
   }

   public static void putAt(List self, IntRange range, Object value) {
      List sublist = resizeListWithRangeAndGetSublist(self, range);
      sublist.add(value);
   }

   public static void putAt(List self, List splice, List values) {
      if (splice.isEmpty()) {
         if (!values.isEmpty()) {
            throw new IllegalArgumentException("Trying to replace 0 elements with " + values.size() + " elements");
         }
      } else {
         Object first = splice.iterator().next();
         if (!(first instanceof Integer)) {
            throw new IllegalArgumentException("Can only index a List with another List of Integers, not a List of " + first.getClass().getName());
         } else if (values.size() != splice.size()) {
            throw new IllegalArgumentException("Trying to replace " + splice.size() + " elements with " + values.size() + " elements");
         } else {
            Iterator<?> valuesIter = values.iterator();
            Iterator i$ = splice.iterator();

            while(i$.hasNext()) {
               Object index = i$.next();
               putAt(self, (Integer)index, valuesIter.next());
            }

         }
      }
   }

   public static void putAt(List self, List splice, Object value) {
      if (!splice.isEmpty()) {
         Object first = splice.iterator().next();
         if (!(first instanceof Integer)) {
            throw new IllegalArgumentException("Can only index a List with another List of Integers, not a List of " + first.getClass().getName());
         } else {
            Iterator i$ = splice.iterator();

            while(i$.hasNext()) {
               Object index = i$.next();
               self.set((Integer)index, value);
            }

         }
      }
   }

   protected static List getSubList(List self, List splice) {
      int right = 0;
      boolean emptyRange = false;
      int left;
      if (splice.size() == 2) {
         left = DefaultTypeTransformation.intUnbox(splice.get(0));
         right = DefaultTypeTransformation.intUnbox(splice.get(1));
      } else if (splice instanceof IntRange) {
         IntRange range = (IntRange)splice;
         left = range.getFromInt();
         right = range.getToInt();
      } else {
         if (!(splice instanceof EmptyRange)) {
            throw new IllegalArgumentException("You must specify a list of 2 indexes to create a sub-list");
         }

         DefaultGroovyMethodsSupport.RangeInfo info = subListBorders(self.size(), (EmptyRange)splice);
         left = info.from;
         emptyRange = true;
      }

      int size = self.size();
      left = normaliseIndex(left, size);
      right = normaliseIndex(right, size);
      List sublist;
      if (!emptyRange) {
         sublist = self.subList(left, right + 1);
      } else {
         sublist = self.subList(left, left);
      }

      return sublist;
   }

   public static <K, V> V getAt(Map<K, V> self, K key) {
      return self.get(key);
   }

   public static <K, V> Map<K, V> plus(Map<K, V> left, Map<K, V> right) {
      Map<K, V> map = cloneSimilarMap(left);
      map.putAll(right);
      return map;
   }

   public static <K, V> V putAt(Map<K, V> self, K key, V value) {
      self.put(key, value);
      return value;
   }

   public static List getAt(Collection coll, String property) {
      List answer = new ArrayList(coll.size());
      Iterator i$ = coll.iterator();

      while(i$.hasNext()) {
         Object item = i$.next();
         if (item != null) {
            Object value;
            try {
               value = InvokerHelper.getProperty(item, property);
            } catch (MissingPropertyExceptionNoStack var8) {
               String causeString = (new MissingPropertyException(var8.getProperty(), var8.getType())).toString();
               throw new MissingPropertyException("Exception evaluating property '" + property + "' for " + coll.getClass().getName() + ", Reason: " + causeString);
            }

            answer.add(value);
         }
      }

      return answer;
   }

   public static <K, V> Map<K, V> asImmutable(Map<? extends K, ? extends V> self) {
      return Collections.unmodifiableMap(self);
   }

   public static <K, V> SortedMap<K, V> asImmutable(SortedMap<K, ? extends V> self) {
      return Collections.unmodifiableSortedMap(self);
   }

   public static <T> List<T> asImmutable(List<? extends T> self) {
      return Collections.unmodifiableList(self);
   }

   public static <T> Set<T> asImmutable(Set<? extends T> self) {
      return Collections.unmodifiableSet(self);
   }

   public static <T> SortedSet<T> asImmutable(SortedSet<T> self) {
      return Collections.unmodifiableSortedSet(self);
   }

   public static <T> Collection<T> asImmutable(Collection<? extends T> self) {
      return Collections.unmodifiableCollection(self);
   }

   public static <K, V> Map<K, V> asSynchronized(Map<K, V> self) {
      return Collections.synchronizedMap(self);
   }

   public static <K, V> SortedMap<K, V> asSynchronized(SortedMap<K, V> self) {
      return Collections.synchronizedSortedMap(self);
   }

   public static <T> Collection<T> asSynchronized(Collection<T> self) {
      return Collections.synchronizedCollection(self);
   }

   public static <T> List<T> asSynchronized(List<T> self) {
      return Collections.synchronizedList(self);
   }

   public static <T> Set<T> asSynchronized(Set<T> self) {
      return Collections.synchronizedSet(self);
   }

   public static <T> SortedSet<T> asSynchronized(SortedSet<T> self) {
      return Collections.synchronizedSortedSet(self);
   }

   public static SpreadMap spread(Map self) {
      return toSpreadMap(self);
   }

   public static SpreadMap toSpreadMap(Map self) {
      if (self == null) {
         throw new GroovyRuntimeException("Fail to convert Map to SpreadMap, because it is null.");
      } else {
         return new SpreadMap(self);
      }
   }

   public static SpreadMap toSpreadMap(Object[] self) {
      if (self == null) {
         throw new GroovyRuntimeException("Fail to convert Object[] to SpreadMap, because it is null.");
      } else if (self.length % 2 != 0) {
         throw new GroovyRuntimeException("Fail to convert Object[] to SpreadMap, because it's size is not even.");
      } else {
         return new SpreadMap(self);
      }
   }

   public static <K, V> Map<K, V> withDefault(Map<K, V> self, Closure init) {
      return MapWithDefault.newInstance(self, init);
   }

   public static <T> List<T> sort(Collection<T> self) {
      List<T> answer = asList(self);
      Collections.sort(answer, new NumberAwareComparator());
      return answer;
   }

   public static <K, V> Map<K, V> sort(Map<K, V> self, Closure closure) {
      Map<K, V> result = new LinkedHashMap();
      List<Entry<K, V>> entries = asList(self.entrySet());
      sort((Collection)entries, (Closure)closure);
      Iterator i$ = entries.iterator();

      while(i$.hasNext()) {
         Entry<K, V> entry = (Entry)i$.next();
         result.put(entry.getKey(), entry.getValue());
      }

      return result;
   }

   public static <K, V> Map<K, V> sort(Map<K, V> self, Comparator<K> comparator) {
      Map<K, V> result = new TreeMap(comparator);
      result.putAll(self);
      return result;
   }

   public static <K, V> Map<K, V> sort(Map<K, V> self) {
      return new TreeMap(self);
   }

   public static <T> T[] sort(T[] self) {
      Arrays.sort(self, new NumberAwareComparator());
      return self;
   }

   public static <T> Iterator<T> sort(Iterator<T> self) {
      return sort((Collection)toList(self)).listIterator();
   }

   public static <T> Iterator<T> sort(Iterator<T> self, Comparator<T> comparator) {
      return sort((Collection)toList(self), (Comparator)comparator).listIterator();
   }

   public static <T> List<T> sort(Collection<T> self, Comparator<T> comparator) {
      List<T> list = asList(self);
      Collections.sort(list, comparator);
      return list;
   }

   public static <T> T[] sort(T[] self, Comparator<T> comparator) {
      Arrays.sort(self, comparator);
      return self;
   }

   public static <T> Iterator<T> sort(Iterator<T> self, Closure closure) {
      return sort((Collection)toList(self), (Closure)closure).listIterator();
   }

   public static <T> T[] sort(T[] self, Closure closure) {
      return (Object[])sort((Collection)toList(self), (Closure)closure).toArray();
   }

   public static <T> List<T> sort(Collection<T> self, Closure closure) {
      List<T> list = asList(self);
      int params = closure.getMaximumNumberOfParameters();
      if (params == 1) {
         Collections.sort(list, new OrderBy(closure));
      } else {
         Collections.sort(list, new ClosureComparator(closure));
      }

      return list;
   }

   public static <T> SortedSet<T> sort(SortedSet<T> self) {
      return self;
   }

   public static <T> T pop(List<T> self) {
      if (self.isEmpty()) {
         throw new NoSuchElementException("Cannot pop() an empty List");
      } else {
         return self.remove(self.size() - 1);
      }
   }

   public static <K, V> Map<K, V> putAll(Map<K, V> self, Collection<Entry<K, V>> entries) {
      Iterator i$ = entries.iterator();

      while(i$.hasNext()) {
         Entry<K, V> entry = (Entry)i$.next();
         self.put(entry.getKey(), entry.getValue());
      }

      return self;
   }

   public static <K, V> Map<K, V> plus(Map<K, V> self, Collection<Entry<K, V>> entries) {
      Map<K, V> map = cloneSimilarMap(self);
      putAll(map, entries);
      return map;
   }

   public static <T> boolean push(List<T> self, T value) {
      return self.add(value);
   }

   public static <T> T last(List<T> self) {
      if (self.isEmpty()) {
         throw new NoSuchElementException("Cannot access last() element from an empty List");
      } else {
         return self.get(self.size() - 1);
      }
   }

   public static <T> T last(T[] self) {
      if (self.length == 0) {
         throw new NoSuchElementException("Cannot access last() element from an empty Array");
      } else {
         return self[self.length - 1];
      }
   }

   public static <T> T first(List<T> self) {
      if (self.isEmpty()) {
         throw new NoSuchElementException("Cannot access first() element from an empty List");
      } else {
         return self.get(0);
      }
   }

   public static <T> T first(T[] self) {
      if (self.length == 0) {
         throw new NoSuchElementException("Cannot access first() element from an empty List");
      } else {
         return self[0];
      }
   }

   public static <T> T head(List<T> self) {
      return first(self);
   }

   public static <T> T head(T[] self) {
      return first(self);
   }

   public static <T> List<T> tail(List<T> self) {
      if (self.isEmpty()) {
         throw new NoSuchElementException("Cannot access tail() for an empty List");
      } else {
         List<T> result = new ArrayList(self);
         result.remove(0);
         return result;
      }
   }

   public static <T> T[] tail(T[] self) {
      if (self.length == 0) {
         throw new NoSuchElementException("Cannot access tail() for an empty Object array");
      } else {
         Class<T> componentType = self.getClass().getComponentType();
         T[] result = (Object[])((Object[])Array.newInstance(componentType, self.length - 1));
         System.arraycopy(self, 1, result, 0, self.length - 1);
         return result;
      }
   }

   public static <T> List<T> asList(Collection<T> self) {
      return self instanceof List ? (List)self : toList(self);
   }

   public static boolean asBoolean(Object object) {
      return object != null;
   }

   public static boolean asBoolean(Boolean bool) {
      return bool;
   }

   public static boolean asBoolean(Matcher matcher) {
      RegexSupport.setLastMatcher(matcher);
      return matcher.find();
   }

   public static boolean asBoolean(Collection collection) {
      return !collection.isEmpty();
   }

   public static boolean asBoolean(Map map) {
      return !map.isEmpty();
   }

   public static boolean asBoolean(Iterator iterator) {
      return iterator.hasNext();
   }

   public static boolean asBoolean(Enumeration enumeration) {
      return enumeration.hasMoreElements();
   }

   public static boolean asBoolean(CharSequence string) {
      return string.length() > 0;
   }

   public static boolean asBoolean(Object[] array) {
      return array.length > 0;
   }

   public static boolean asBoolean(byte[] array) {
      return array.length > 0;
   }

   public static boolean asBoolean(short[] array) {
      return array.length > 0;
   }

   public static boolean asBoolean(int[] array) {
      return array.length > 0;
   }

   public static boolean asBoolean(long[] array) {
      return array.length > 0;
   }

   public static boolean asBoolean(float[] array) {
      return array.length > 0;
   }

   public static boolean asBoolean(double[] array) {
      return array.length > 0;
   }

   public static boolean asBoolean(boolean[] array) {
      return array.length > 0;
   }

   public static boolean asBoolean(char[] array) {
      return array.length > 0;
   }

   public static boolean asBoolean(Character character) {
      return character != 0;
   }

   public static boolean asBoolean(Number number) {
      return number.doubleValue() != 0.0D;
   }

   /** @deprecated */
   @Deprecated
   public static boolean asBoolean(GroovyResultSet grs) {
      return SqlGroovyMethods.asBoolean(grs);
   }

   public static Object asType(Collection col, Class clazz) {
      if (col.getClass() == clazz) {
         return col;
      } else if (clazz == List.class) {
         return asList(col);
      } else if (clazz == Set.class) {
         return col instanceof Set ? col : new HashSet(col);
      } else if (clazz == SortedSet.class) {
         return col instanceof SortedSet ? col : new TreeSet(col);
      } else if (clazz == Queue.class) {
         return col instanceof Queue ? col : new LinkedList(col);
      } else if (clazz == Stack.class) {
         if (col instanceof Stack) {
            return col;
         } else {
            Stack stack = new Stack();
            stack.addAll(col);
            return stack;
         }
      } else {
         Object[] args = new Object[]{col};

         try {
            return InvokerHelper.invokeConstructorOf((Class)clazz, args);
         } catch (Exception var4) {
            return asType((Object)col, clazz);
         }
      }
   }

   public static Object asType(Object[] ary, Class clazz) {
      if (clazz == List.class) {
         return new ArrayList(Arrays.asList(ary));
      } else if (clazz == Set.class) {
         return new HashSet(Arrays.asList(ary));
      } else {
         return clazz == SortedSet.class ? new TreeSet(Arrays.asList(ary)) : asType((Object)ary, clazz);
      }
   }

   public static Object asType(Closure cl, Class clazz) {
      if (clazz.isInterface() && !clazz.isInstance(cl)) {
         return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new ConvertedClosure(cl));
      } else {
         try {
            return asType((Object)cl, clazz);
         } catch (GroovyCastException var5) {
            try {
               return ProxyGenerator.INSTANCE.instantiateAggregateFromBaseClass(cl, clazz);
            } catch (GroovyRuntimeException var4) {
               throw new GroovyCastException("Error casting closure to " + clazz.getName() + ", Reason: " + var4.getMessage());
            }
         }
      }
   }

   public static Object asType(Map map, Class clazz) {
      if (!clazz.isInstance(map) && clazz.isInterface()) {
         return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new ConvertedMap(map));
      } else {
         try {
            return asType((Object)map, clazz);
         } catch (GroovyCastException var5) {
            try {
               return ProxyGenerator.INSTANCE.instantiateAggregateFromBaseClass(map, clazz);
            } catch (GroovyRuntimeException var4) {
               throw new GroovyCastException("Error casting map to " + clazz.getName() + ", Reason: " + var4.getMessage());
            }
         }
      }
   }

   public static <T> List<T> reverse(List<T> self) {
      int size = self.size();
      List<T> answer = new ArrayList(size);
      ListIterator iter = self.listIterator(size);

      while(iter.hasPrevious()) {
         answer.add(iter.previous());
      }

      return answer;
   }

   public static <T> Iterator<T> reverse(Iterator<T> self) {
      return new ReverseListIterator(toList(self));
   }

   public static <T> Collection<T> plus(Collection<T> left, Collection<T> right) {
      Collection<T> answer = cloneSimilarCollection(left, left.size() + right.size());
      answer.addAll(right);
      return answer;
   }

   public static <T> Collection<T> plus(Collection<T> left, T right) {
      Collection<T> answer = cloneSimilarCollection(left, left.size() + 1);
      answer.add(right);
      return answer;
   }

   public static <T> List<T> multiply(Collection<T> self, Number factor) {
      int size = factor.intValue();
      List<T> answer = new ArrayList(self.size() * size);

      for(int i = 0; i < size; ++i) {
         answer.addAll(self);
      }

      return answer;
   }

   public static <T> Collection<T> intersect(Collection<T> left, Collection<T> right) {
      if (left.isEmpty()) {
         return createSimilarCollection(left, 0);
      } else {
         Collection result;
         if (left.size() < right.size()) {
            result = left;
            left = right;
            right = result;
         }

         result = createSimilarCollection(left, left.size());
         Collection<T> pickFrom = new TreeSet(new NumberAwareComparator());
         pickFrom.addAll(left);
         Iterator i$ = right.iterator();

         while(i$.hasNext()) {
            T t = i$.next();
            if (pickFrom.contains(t)) {
               result.add(t);
            }
         }

         return result;
      }
   }

   public static <K, V> Map<K, V> intersect(Map<K, V> left, Map<K, V> right) {
      Map<K, V> ansMap = createSimilarMap(left);
      if (right != null && right.size() > 0) {
         Iterator it1 = left.entrySet().iterator();

         while(it1.hasNext()) {
            Entry<K, V> e1 = (Entry)it1.next();
            Iterator it2 = right.entrySet().iterator();

            while(it2.hasNext()) {
               Entry<K, V> e2 = (Entry)it2.next();
               if (DefaultTypeTransformation.compareEqual(e1, e2)) {
                  ansMap.put(e1.getKey(), e1.getValue());
               }
            }
         }
      }

      return ansMap;
   }

   public static boolean disjoint(Collection left, Collection right) {
      if (!left.isEmpty() && !right.isEmpty()) {
         Collection pickFrom = new TreeSet(new NumberAwareComparator());
         pickFrom.addAll(right);
         Iterator i$ = left.iterator();

         Object o;
         do {
            if (!i$.hasNext()) {
               return true;
            }

            o = i$.next();
         } while(!pickFrom.contains(o));

         return false;
      } else {
         return true;
      }
   }

   public static boolean equals(int[] left, int[] right) {
      if (left == null) {
         return right == null;
      } else if (right == null) {
         return false;
      } else if (left.length != right.length) {
         return false;
      } else {
         for(int i = 0; i < left.length; ++i) {
            if (left[i] != right[i]) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean equals(Object[] left, List right) {
      return coercedEquals(left, right);
   }

   public static boolean equals(List left, Object[] right) {
      return coercedEquals(right, left);
   }

   private static boolean coercedEquals(Object[] left, List right) {
      if (left == null) {
         return right == null;
      } else if (right == null) {
         return false;
      } else if (left.length != right.size()) {
         return false;
      } else {
         NumberAwareComparator numberAwareComparator = new NumberAwareComparator();

         for(int i = left.length - 1; i >= 0; --i) {
            Object o1 = left[i];
            Object o2 = right.get(i);
            if (o1 == null) {
               if (o2 != null) {
                  return false;
               }
            } else if (o1 instanceof Number) {
               if (!(o2 instanceof Number) || numberAwareComparator.compare(o1, o2) != 0) {
                  return false;
               }
            } else if (!DefaultTypeTransformation.compareEqual(o1, o2)) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean equals(List left, List right) {
      if (left == null) {
         return right == null;
      } else if (right == null) {
         return false;
      } else if (left.size() != right.size()) {
         return false;
      } else {
         NumberAwareComparator numberAwareComparator = new NumberAwareComparator();
         Iterator it1 = left.iterator();
         Iterator it2 = right.iterator();

         Object o1;
         Object o2;
         label52:
         do {
            label46:
            do {
               do {
                  if (!it1.hasNext()) {
                     return true;
                  }

                  o1 = it1.next();
                  o2 = it2.next();
                  if (o1 != null) {
                     if (o1 instanceof Number) {
                        continue label52;
                     }
                     continue label46;
                  }
               } while(o2 == null);

               return false;
            } while(DefaultTypeTransformation.compareEqual(o1, o2));

            return false;
         } while(o2 instanceof Number && numberAwareComparator.compare(o1, o2) == 0);

         return false;
      }
   }

   public static <T> Set<T> minus(Set<T> self, Collection operands) {
      Set<T> ansSet = createSimilarSet(self);
      ansSet.addAll(self);
      if (self.size() > 0) {
         ansSet.removeAll(operands);
      }

      return ansSet;
   }

   public static <T> Set<T> minus(Set<T> self, Object operand) {
      Set<T> ansSet = createSimilarSet(self);
      Comparator numberComparator = new NumberAwareComparator();
      Iterator i$ = self.iterator();

      while(i$.hasNext()) {
         T t = i$.next();
         if (numberComparator.compare(t, operand) != 0) {
            ansSet.add(t);
         }
      }

      return ansSet;
   }

   public static <T> T[] minus(T[] self, Collection<T> removeMe) {
      return (Object[])minus(toList(self), removeMe).toArray();
   }

   public static <T> T[] minus(T[] self, T[] removeMe) {
      return (Object[])minus((List)toList(self), (Collection)toList(removeMe)).toArray();
   }

   public static <T> List<T> minus(List<T> self, Collection<T> removeMe) {
      if (self.size() == 0) {
         return new ArrayList();
      } else {
         boolean nlgnSort = sameType(new Collection[]{self, removeMe});
         Comparator<T> numberComparator = new NumberAwareComparator();
         Iterator iter;
         Object t;
         if (nlgnSort && self.get(0) instanceof Comparable) {
            TreeSet answer;
            if (Number.class.isInstance(self.get(0))) {
               answer = new TreeSet(numberComparator);
               answer.addAll(self);
               iter = self.iterator();

               label65:
               while(true) {
                  while(true) {
                     if (!iter.hasNext()) {
                        break label65;
                     }

                     t = iter.next();
                     if (Number.class.isInstance(t)) {
                        Iterator i$ = removeMe.iterator();

                        while(i$.hasNext()) {
                           T t2 = i$.next();
                           if (Number.class.isInstance(t2) && numberComparator.compare(t, t2) == 0) {
                              answer.remove(t);
                           }
                        }
                     } else if (removeMe.contains(t)) {
                        answer.remove(t);
                     }
                  }
               }
            } else {
               answer = new TreeSet(numberComparator);
               answer.addAll(self);
               answer.removeAll(removeMe);
            }

            List<T> ansList = new ArrayList();
            Iterator i$ = self.iterator();

            while(i$.hasNext()) {
               T o = i$.next();
               if (answer.contains(o)) {
                  ansList.add(o);
               }
            }

            return ansList;
         } else {
            List<T> tmpAnswer = new LinkedList(self);
            iter = tmpAnswer.iterator();

            while(iter.hasNext()) {
               t = iter.next();
               boolean elementRemoved = false;
               Iterator iterator = removeMe.iterator();

               while(iterator.hasNext() && !elementRemoved) {
                  T elt = iterator.next();
                  if (numberComparator.compare(t, elt) == 0) {
                     iter.remove();
                     elementRemoved = true;
                  }
               }
            }

            return new ArrayList(tmpAnswer);
         }
      }
   }

   public static <T> List<T> minus(List<T> self, Object operand) {
      Comparator numberComparator = new NumberAwareComparator();
      List<T> ansList = new ArrayList();
      Iterator i$ = self.iterator();

      while(i$.hasNext()) {
         T t = i$.next();
         if (numberComparator.compare(t, operand) != 0) {
            ansList.add(t);
         }
      }

      return ansList;
   }

   public static <T> T[] minus(T[] self, Object operand) {
      return (Object[])minus(toList(self), operand).toArray();
   }

   public static <K, V> Map<K, V> minus(Map<K, V> self, Map<K, V> operands) {
      Map<K, V> ansMap = createSimilarMap(self);
      ansMap.putAll(self);
      if (operands != null && operands.size() > 0) {
         Iterator it1 = self.entrySet().iterator();

         while(it1.hasNext()) {
            Entry<K, V> e1 = (Entry)it1.next();
            Iterator it2 = operands.entrySet().iterator();

            while(it2.hasNext()) {
               Entry<K, V> e2 = (Entry)it2.next();
               if (DefaultTypeTransformation.compareEqual(e1, e2)) {
                  ansMap.remove(e1.getKey());
               }
            }
         }
      }

      return ansMap;
   }

   public static Collection flatten(Collection self) {
      return flatten(self, createSimilarCollection(self));
   }

   public static Collection flatten(Object[] self) {
      return flatten(toList(self), (Collection)(new ArrayList()));
   }

   public static Collection flatten(boolean[] self) {
      return flatten(toList(self), (Collection)(new ArrayList()));
   }

   public static Collection flatten(byte[] self) {
      return flatten(toList(self), (Collection)(new ArrayList()));
   }

   public static Collection flatten(char[] self) {
      return flatten(toList(self), (Collection)(new ArrayList()));
   }

   public static Collection flatten(short[] self) {
      return flatten(toList(self), (Collection)(new ArrayList()));
   }

   public static Collection flatten(int[] self) {
      return flatten(toList(self), (Collection)(new ArrayList()));
   }

   public static Collection flatten(long[] self) {
      return flatten(toList(self), (Collection)(new ArrayList()));
   }

   public static Collection flatten(float[] self) {
      return flatten(toList(self), (Collection)(new ArrayList()));
   }

   public static Collection flatten(double[] self) {
      return flatten(toList(self), (Collection)(new ArrayList()));
   }

   private static Collection flatten(Collection elements, Collection addTo) {
      Iterator i$ = elements.iterator();

      while(true) {
         while(i$.hasNext()) {
            Object element = i$.next();
            if (element instanceof Collection) {
               flatten((Collection)element, addTo);
            } else if (element != null && element.getClass().isArray()) {
               flatten(DefaultTypeTransformation.arrayAsCollection(element), addTo);
            } else {
               addTo.add(element);
            }
         }

         return addTo;
      }
   }

   public static Collection flatten(Collection self, Closure flattenUsing) {
      return flatten(self, createSimilarCollection(self), flattenUsing);
   }

   private static Collection flatten(Collection elements, Collection addTo, Closure flattenUsing) {
      Iterator i$ = elements.iterator();

      while(true) {
         while(i$.hasNext()) {
            Object element = i$.next();
            if (element instanceof Collection) {
               flatten((Collection)element, addTo, flattenUsing);
            } else if (element != null && element.getClass().isArray()) {
               flatten(DefaultTypeTransformation.arrayAsCollection(element), addTo, flattenUsing);
            } else {
               Object flattened = flattenUsing.call(new Object[]{element});
               boolean returnedSelf = flattened == element;
               if (!returnedSelf && flattened instanceof Collection) {
                  List list = toList((Collection)flattened);
                  if (list.size() == 1 && list.get(0) == element) {
                     returnedSelf = true;
                  }
               }

               if (flattened instanceof Collection && !returnedSelf) {
                  flatten((Collection)flattened, addTo, flattenUsing);
               } else {
                  addTo.add(flattened);
               }
            }
         }

         return addTo;
      }
   }

   public static <T> Collection<T> leftShift(Collection<T> self, T value) {
      self.add(value);
      return self;
   }

   public static <T> BlockingQueue<T> leftShift(BlockingQueue<T> self, T value) throws InterruptedException {
      self.put(value);
      return self;
   }

   public static <K, V> Map<K, V> leftShift(Map<K, V> self, Entry<K, V> entry) {
      self.put(entry.getKey(), entry.getValue());
      return self;
   }

   public static <K, V> Map<K, V> leftShift(Map<K, V> self, Map<K, V> other) {
      self.putAll(other);
      return self;
   }

   public static StringBuffer leftShift(String self, Object value) {
      return (new StringBuffer(self)).append(value);
   }

   protected static StringWriter createStringWriter(String self) {
      StringWriter answer = new StringWriter();
      answer.write(self);
      return answer;
   }

   protected static StringBufferWriter createStringBufferWriter(StringBuffer self) {
      return new StringBufferWriter(self);
   }

   public static StringBuffer leftShift(StringBuffer self, Object value) {
      self.append(value);
      return self;
   }

   public static Writer leftShift(Writer self, Object value) throws IOException {
      InvokerHelper.write(self, value);
      return self;
   }

   public static Number leftShift(Number self, Number operand) {
      return NumberMath.leftShift(self, operand);
   }

   public static Number rightShift(Number self, Number operand) {
      return NumberMath.rightShift(self, operand);
   }

   public static Number rightShiftUnsigned(Number self, Number operand) {
      return NumberMath.rightShiftUnsigned(self, operand);
   }

   public static void write(Writer self, Writable writable) throws IOException {
      writable.writeTo(self);
   }

   public static Writer leftShift(OutputStream self, Object value) throws IOException {
      OutputStreamWriter writer = new FlushingStreamWriter(self);
      leftShift((Writer)writer, (Object)value);
      return writer;
   }

   public static void leftShift(ObjectOutputStream self, Object value) throws IOException {
      self.writeObject(value);
   }

   public static OutputStream leftShift(OutputStream self, InputStream in) throws IOException {
      byte[] buf = new byte[1024];

      while(true) {
         int count = in.read(buf, 0, buf.length);
         if (count == -1) {
            self.flush();
            return self;
         }

         if (count == 0) {
            Thread.yield();
         } else {
            self.write(buf, 0, count);
         }
      }
   }

   public static OutputStream leftShift(OutputStream self, byte[] value) throws IOException {
      self.write(value);
      self.flush();
      return self;
   }

   public static List<Byte> getAt(byte[] array, Range range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Character> getAt(char[] array, Range range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Short> getAt(short[] array, Range range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Integer> getAt(int[] array, Range range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Long> getAt(long[] array, Range range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Float> getAt(float[] array, Range range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Double> getAt(double[] array, Range range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Boolean> getAt(boolean[] array, Range range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Byte> getAt(byte[] array, IntRange range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Character> getAt(char[] array, IntRange range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Short> getAt(short[] array, IntRange range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Integer> getAt(int[] array, IntRange range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Long> getAt(long[] array, IntRange range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Float> getAt(float[] array, IntRange range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Double> getAt(double[] array, IntRange range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Boolean> getAt(boolean[] array, IntRange range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Byte> getAt(byte[] array, ObjectRange range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Character> getAt(char[] array, ObjectRange range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Short> getAt(short[] array, ObjectRange range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Integer> getAt(int[] array, ObjectRange range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Long> getAt(long[] array, ObjectRange range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Float> getAt(float[] array, ObjectRange range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Double> getAt(double[] array, ObjectRange range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Boolean> getAt(boolean[] array, ObjectRange range) {
      return primitiveArrayGet(array, (Range)range);
   }

   public static List<Byte> getAt(byte[] array, Collection indices) {
      return primitiveArrayGet(array, (Collection)indices);
   }

   public static List<Character> getAt(char[] array, Collection indices) {
      return primitiveArrayGet(array, (Collection)indices);
   }

   public static List<Short> getAt(short[] array, Collection indices) {
      return primitiveArrayGet(array, (Collection)indices);
   }

   public static List<Integer> getAt(int[] array, Collection indices) {
      return primitiveArrayGet(array, (Collection)indices);
   }

   public static List<Long> getAt(long[] array, Collection indices) {
      return primitiveArrayGet(array, (Collection)indices);
   }

   public static List<Float> getAt(float[] array, Collection indices) {
      return primitiveArrayGet(array, (Collection)indices);
   }

   public static List<Double> getAt(double[] array, Collection indices) {
      return primitiveArrayGet(array, (Collection)indices);
   }

   public static List<Boolean> getAt(boolean[] array, Collection indices) {
      return primitiveArrayGet(array, (Collection)indices);
   }

   public static boolean getAt(BitSet self, int index) {
      return self.get(index);
   }

   public static BitSet getAt(BitSet self, IntRange range) {
      int from = DefaultTypeTransformation.intUnbox(range.getFrom());
      int to = DefaultTypeTransformation.intUnbox(range.getTo());
      BitSet result = new BitSet();
      int numberOfBits = to - from + 1;
      int adjuster = 1;
      int offset = from;
      if (range.isReverse()) {
         adjuster = -1;
         offset = to;
      }

      for(int i = 0; i < numberOfBits; ++i) {
         result.set(i, self.get(offset + adjuster * i));
      }

      return result;
   }

   public static void putAt(BitSet self, IntRange range, boolean value) {
      int from = DefaultTypeTransformation.intUnbox(range.getFrom());
      int to = DefaultTypeTransformation.intUnbox(range.getTo());
      if (from > to) {
         int tmp = to;
         to = from;
         from = tmp;
      }

      self.set(from, to + 1, value);
   }

   public static void putAt(BitSet self, int index, boolean value) {
      self.set(index, value);
   }

   public static int size(boolean[] array) {
      return Array.getLength(array);
   }

   public static int size(byte[] array) {
      return Array.getLength(array);
   }

   public static int size(char[] array) {
      return Array.getLength(array);
   }

   public static int size(short[] array) {
      return Array.getLength(array);
   }

   public static int size(int[] array) {
      return Array.getLength(array);
   }

   public static int size(long[] array) {
      return Array.getLength(array);
   }

   public static int size(float[] array) {
      return Array.getLength(array);
   }

   public static int size(double[] array) {
      return Array.getLength(array);
   }

   public static List<Byte> toList(byte[] array) {
      return DefaultTypeTransformation.primitiveArrayToList(array);
   }

   public static List<Boolean> toList(boolean[] array) {
      return DefaultTypeTransformation.primitiveArrayToList(array);
   }

   public static List<Character> toList(char[] array) {
      return DefaultTypeTransformation.primitiveArrayToList(array);
   }

   public static List<Short> toList(short[] array) {
      return DefaultTypeTransformation.primitiveArrayToList(array);
   }

   public static List<Integer> toList(int[] array) {
      return DefaultTypeTransformation.primitiveArrayToList(array);
   }

   public static List<Long> toList(long[] array) {
      return DefaultTypeTransformation.primitiveArrayToList(array);
   }

   public static List<Float> toList(float[] array) {
      return DefaultTypeTransformation.primitiveArrayToList(array);
   }

   public static List<Double> toList(double[] array) {
      return DefaultTypeTransformation.primitiveArrayToList(array);
   }

   protected static Object primitiveArrayGet(Object self, int idx) {
      return Array.get(self, normaliseIndex(idx, Array.getLength(self)));
   }

   protected static List primitiveArrayGet(Object self, Range range) {
      List answer = new ArrayList();
      Iterator i$ = range.iterator();

      while(i$.hasNext()) {
         Object next = i$.next();
         int idx = DefaultTypeTransformation.intUnbox(next);
         answer.add(primitiveArrayGet(self, idx));
      }

      return answer;
   }

   protected static List primitiveArrayGet(Object self, Collection indices) {
      List answer = new ArrayList();
      Iterator i$ = indices.iterator();

      while(i$.hasNext()) {
         Object value = i$.next();
         if (value instanceof Range) {
            answer.addAll(primitiveArrayGet(self, (Range)value));
         } else if (value instanceof List) {
            answer.addAll(primitiveArrayGet(self, (Collection)((List)value)));
         } else {
            int idx = DefaultTypeTransformation.intUnbox(value);
            answer.add(primitiveArrayGet(self, idx));
         }
      }

      return answer;
   }

   protected static Object primitiveArrayPut(Object self, int idx, Object newValue) {
      Array.set(self, normaliseIndex(idx, Array.getLength(self)), newValue);
      return newValue;
   }

   public static Character toCharacter(String self) {
      return self.charAt(0);
   }

   public static Boolean toBoolean(String self) {
      String trimmed = self.trim();
      return !"true".equalsIgnoreCase(trimmed) && !"y".equalsIgnoreCase(trimmed) && !"1".equals(trimmed) ? Boolean.FALSE : Boolean.TRUE;
   }

   public static Boolean toBoolean(Boolean self) {
      return self;
   }

   public static String[] split(String self) {
      StringTokenizer st = new StringTokenizer(self);
      String[] strings = new String[st.countTokens()];

      for(int i = 0; i < strings.length; ++i) {
         strings[i] = st.nextToken();
      }

      return strings;
   }

   public static String capitalize(String self) {
      return self != null && self.length() != 0 ? Character.toUpperCase(self.charAt(0)) + self.substring(1) : self;
   }

   public static String expand(String self) {
      return expand(self, 8);
   }

   public static String expand(String self, int tabStop) {
      if (self.length() == 0) {
         return self;
      } else {
         try {
            StringBuilder builder = new StringBuilder();
            Iterator i$ = readLines(self).iterator();

            while(i$.hasNext()) {
               String line = (String)i$.next();
               builder.append(expandLine(line, tabStop));
               builder.append("\n");
            }

            if (!self.endsWith("\n")) {
               builder.deleteCharAt(builder.length() - 1);
            }

            return builder.toString();
         } catch (IOException var5) {
            return self;
         }
      }
   }

   public static String expandLine(String self, int tabStop) {
      int index;
      while((index = self.indexOf(9)) != -1) {
         StringBuilder builder = new StringBuilder(self);
         int count = tabStop - index % tabStop;
         builder.deleteCharAt(index);

         for(int i = 0; i < count; ++i) {
            builder.insert(index, " ");
         }

         self = builder.toString();
      }

      return self;
   }

   public static String unexpand(String self) {
      return unexpand(self, 8);
   }

   public static String unexpand(String self, int tabStop) {
      if (self.length() == 0) {
         return self;
      } else {
         try {
            StringBuilder builder = new StringBuilder();
            Iterator i$ = readLines(self).iterator();

            while(i$.hasNext()) {
               String line = (String)i$.next();
               builder.append(unexpandLine(line, tabStop));
               builder.append("\n");
            }

            if (!self.endsWith("\n")) {
               builder.deleteCharAt(builder.length() - 1);
            }

            return builder.toString();
         } catch (IOException var5) {
            return self;
         }
      }
   }

   public static String unexpandLine(String self, int tabStop) {
      StringBuilder builder = new StringBuilder(self);
      int index = 0;

      while(index + tabStop < builder.length()) {
         String piece = builder.substring(index, index + tabStop);

         int count;
         for(count = 0; count < tabStop && Character.isWhitespace(piece.charAt(tabStop - (count + 1))); ++count) {
         }

         if (count > 0) {
            piece = piece.substring(0, tabStop - count) + '\t';
            builder.replace(index, index + tabStop, piece);
            index = index + tabStop - (count - 1);
         } else {
            index += tabStop;
         }
      }

      return builder.toString();
   }

   public static String[] split(GString self) {
      return split(self.toString());
   }

   public static List tokenize(String self, String token) {
      return InvokerHelper.asList(new StringTokenizer(self, token));
   }

   public static List tokenize(String self, Character token) {
      return tokenize(self, token.toString());
   }

   public static List tokenize(String self) {
      return InvokerHelper.asList(new StringTokenizer(self));
   }

   public static String plus(String left, Object value) {
      return left + toString(value);
   }

   public static String plus(Number value, String right) {
      return toString((Object)value) + right;
   }

   public static String plus(StringBuffer left, String value) {
      return left + value;
   }

   public static String minus(String self, Object target) {
      if (target instanceof Pattern) {
         return ((Pattern)target).matcher(self).replaceFirst("");
      } else {
         String text = toString(target);
         int index = self.indexOf(text);
         if (index == -1) {
            return self;
         } else {
            int end = index + text.length();
            return self.length() > end ? self.substring(0, index) + self.substring(end) : self.substring(0, index);
         }
      }
   }

   public static boolean contains(String self, String text) {
      int idx = self.indexOf(text);
      return idx >= 0;
   }

   public static int count(String self, String text) {
      int answer = 0;
      int idx = 0;

      while(true) {
         idx = self.indexOf(text, idx);
         if (idx < 0) {
            return answer;
         }

         ++answer;
         ++idx;
      }
   }

   public static String next(String self) {
      StringBuilder buffer = new StringBuilder(self);
      if (buffer.length() == 0) {
         buffer.append('\u0000');
      } else {
         char last = buffer.charAt(buffer.length() - 1);
         if (last == '\uffff') {
            buffer.append('\u0000');
         } else {
            char next = (char)(last + 1);
            buffer.setCharAt(buffer.length() - 1, next);
         }
      }

      return buffer.toString();
   }

   public static String previous(String self) {
      StringBuilder buffer = new StringBuilder(self);
      if (buffer.length() == 0) {
         throw new IllegalArgumentException("the string is empty");
      } else {
         char last = buffer.charAt(buffer.length() - 1);
         if (last == 0) {
            buffer.deleteCharAt(buffer.length() - 1);
         } else {
            char next = (char)(last - 1);
            buffer.setCharAt(buffer.length() - 1, next);
         }

         return buffer.toString();
      }
   }

   public static Process execute(String self) throws IOException {
      return Runtime.getRuntime().exec(self);
   }

   public static Process execute(String self, String[] envp, File dir) throws IOException {
      return Runtime.getRuntime().exec(self, envp, dir);
   }

   public static Process execute(String self, List envp, File dir) throws IOException {
      return execute(self, stringify(envp), dir);
   }

   public static Process execute(String[] commandArray) throws IOException {
      return Runtime.getRuntime().exec(commandArray);
   }

   public static Process execute(String[] commandArray, String[] envp, File dir) throws IOException {
      return Runtime.getRuntime().exec(commandArray, envp, dir);
   }

   public static Process execute(String[] commandArray, List envp, File dir) throws IOException {
      return Runtime.getRuntime().exec(commandArray, stringify(envp), dir);
   }

   public static Process execute(List commands) throws IOException {
      return execute(stringify(commands));
   }

   public static Process execute(List commands, String[] envp, File dir) throws IOException {
      return Runtime.getRuntime().exec(stringify(commands), envp, dir);
   }

   public static Process execute(List commands, List envp, File dir) throws IOException {
      return Runtime.getRuntime().exec(stringify(commands), stringify(envp), dir);
   }

   private static String[] stringify(List orig) {
      if (orig == null) {
         return null;
      } else {
         String[] result = new String[orig.size()];

         for(int i = 0; i < orig.size(); ++i) {
            result[i] = orig.get(i).toString();
         }

         return result;
      }
   }

   public static String multiply(String self, Number factor) {
      int size = factor.intValue();
      if (size == 0) {
         return "";
      } else if (size < 0) {
         throw new IllegalArgumentException("multiply() should be called with a number of 0 or greater not: " + size);
      } else {
         StringBuilder answer = new StringBuilder(self);

         for(int i = 1; i < size; ++i) {
            answer.append(self);
         }

         return answer.toString();
      }
   }

   public static String toString(boolean[] self) {
      return InvokerHelper.toString(self);
   }

   public static String toString(byte[] self) {
      return InvokerHelper.toString(self);
   }

   public static String toString(char[] self) {
      return InvokerHelper.toString(self);
   }

   public static String toString(short[] self) {
      return InvokerHelper.toString(self);
   }

   public static String toString(int[] self) {
      return InvokerHelper.toString(self);
   }

   public static String toString(long[] self) {
      return InvokerHelper.toString(self);
   }

   public static String toString(float[] self) {
      return InvokerHelper.toString(self);
   }

   public static String toString(double[] self) {
      return InvokerHelper.toString(self);
   }

   public static String toString(AbstractMap self) {
      return toMapString(self);
   }

   public static String toMapString(Map self) {
      return toMapString(self, -1);
   }

   public static String toMapString(Map self, int maxSize) {
      return self == null ? "null" : InvokerHelper.toMapString(self, maxSize);
   }

   public static String toString(AbstractCollection self) {
      return toListString(self);
   }

   public static String toListString(Collection self) {
      return toListString(self, -1);
   }

   public static String toListString(Collection self, int maxSize) {
      return self == null ? "null" : InvokerHelper.toListString(self, maxSize);
   }

   public static String toString(Object[] self) {
      return toArrayString(self);
   }

   public static String toArrayString(Object[] self) {
      return self == null ? "null" : InvokerHelper.toArrayString(self);
   }

   public static String toString(Object value) {
      return InvokerHelper.toString(value);
   }

   public static Character next(Character self) {
      return (char)(self + 1);
   }

   public static Number next(Number self) {
      return NumberNumberPlus.plus(self, ONE);
   }

   public static Character previous(Character self) {
      return (char)(self - 1);
   }

   public static Number previous(Number self) {
      return NumberNumberMinus.minus(self, ONE);
   }

   public static Number plus(Character left, Number right) {
      return NumberNumberPlus.plus(Integer.valueOf(left), right);
   }

   public static Number plus(Number left, Character right) {
      return NumberNumberPlus.plus(left, Integer.valueOf(right));
   }

   public static Number plus(Character left, Character right) {
      return plus((Number)Integer.valueOf(left), (Character)right);
   }

   public static int compareTo(Character left, Number right) {
      return compareTo((Number)Integer.valueOf(left), (Number)right);
   }

   public static int compareTo(Number left, Character right) {
      return compareTo((Number)left, (Number)Integer.valueOf(right));
   }

   public static int compareTo(Character left, Character right) {
      return compareTo((Number)Integer.valueOf(left), (Character)right);
   }

   public static int compareTo(Number left, Number right) {
      return NumberMath.compareTo(left, right);
   }

   public static Number minus(Character left, Number right) {
      return NumberNumberMinus.minus(Integer.valueOf(left), right);
   }

   public static Number minus(Number left, Character right) {
      return NumberNumberMinus.minus(left, Integer.valueOf(right));
   }

   public static Number minus(Character left, Character right) {
      return minus((Number)Integer.valueOf(left), (Character)right);
   }

   public static Number multiply(Character left, Number right) {
      return NumberNumberMultiply.multiply(Integer.valueOf(left), right);
   }

   public static Number multiply(Number left, Character right) {
      return NumberNumberMultiply.multiply(Integer.valueOf(right), left);
   }

   public static Number multiply(Character left, Character right) {
      return multiply((Number)Integer.valueOf(left), (Character)right);
   }

   public static Number multiply(BigDecimal left, Double right) {
      return NumberMath.multiply(left, right);
   }

   public static Number multiply(BigDecimal left, BigInteger right) {
      return NumberMath.multiply(left, right);
   }

   public static Number power(Number self, Number exponent) {
      double base = self.doubleValue();
      double exp = exponent.doubleValue();
      double answer = Math.pow(base, exp);
      if ((double)((int)answer) == answer) {
         return (int)answer;
      } else {
         return (Number)((double)((long)answer) == answer ? (long)answer : answer);
      }
   }

   public static Number power(BigDecimal self, Integer exponent) {
      return (Number)(exponent >= 0 ? self.pow(exponent) : power((Number)self, (Number)((double)exponent)));
   }

   public static Number power(BigInteger self, Integer exponent) {
      return (Number)(exponent >= 0 ? self.pow(exponent) : power((Number)self, (Number)((double)exponent)));
   }

   public static Number power(Integer self, Integer exponent) {
      if (exponent >= 0) {
         BigInteger answer = BigInteger.valueOf((long)self).pow(exponent);
         return (Number)(answer.compareTo(BI_INT_MIN) >= 0 && answer.compareTo(BI_INT_MAX) <= 0 ? answer.intValue() : answer);
      } else {
         return power((Number)self, (Number)((double)exponent));
      }
   }

   public static Number power(Long self, Integer exponent) {
      if (exponent >= 0) {
         BigInteger answer = BigInteger.valueOf(self).pow(exponent);
         return (Number)(answer.compareTo(BI_LONG_MIN) >= 0 && answer.compareTo(BI_LONG_MAX) <= 0 ? answer.longValue() : answer);
      } else {
         return power((Number)self, (Number)((double)exponent));
      }
   }

   public static Number div(Character left, Number right) {
      return NumberNumberDiv.div(Integer.valueOf(left), right);
   }

   public static Number div(Number left, Character right) {
      return NumberNumberDiv.div(left, Integer.valueOf(right));
   }

   public static Number div(Character left, Character right) {
      return div((Number)Integer.valueOf(left), (Character)right);
   }

   public static Number intdiv(Character left, Number right) {
      return intdiv((Number)Integer.valueOf(left), (Number)right);
   }

   public static Number intdiv(Number left, Character right) {
      return intdiv((Number)left, (Number)Integer.valueOf(right));
   }

   public static Number intdiv(Character left, Character right) {
      return intdiv((Number)Integer.valueOf(left), (Character)right);
   }

   public static Number intdiv(Number left, Number right) {
      return NumberMath.intdiv(left, right);
   }

   public static Number or(Number left, Number right) {
      return NumberMath.or(left, right);
   }

   public static Number and(Number left, Number right) {
      return NumberMath.and(left, right);
   }

   public static BitSet and(BitSet left, BitSet right) {
      BitSet result = (BitSet)left.clone();
      result.and(right);
      return result;
   }

   public static BitSet xor(BitSet left, BitSet right) {
      BitSet result = (BitSet)left.clone();
      result.xor(right);
      return result;
   }

   public static BitSet bitwiseNegate(BitSet self) {
      BitSet result = (BitSet)self.clone();
      result.flip(0, result.size() - 1);
      return result;
   }

   public static BitSet or(BitSet left, BitSet right) {
      BitSet result = (BitSet)left.clone();
      result.or(right);
      return result;
   }

   public static Number xor(Number left, Number right) {
      return NumberMath.xor(left, right);
   }

   public static Number mod(Number left, Number right) {
      return NumberMath.mod(left, right);
   }

   public static Number unaryMinus(Number left) {
      return NumberMath.unaryMinus(left);
   }

   public static void times(Number self, Closure closure) {
      int i = 0;

      for(int size = self.intValue(); i < size; ++i) {
         closure.call((Object)i);
         if (closure.getDirective() == 1) {
            break;
         }
      }

   }

   public static void upto(Number self, Number to, Closure closure) {
      int self1 = self.intValue();
      int to1 = to.intValue();
      if (self1 > to1) {
         throw new GroovyRuntimeException("Infinite loop in " + self + ".upto(" + to + ")");
      } else {
         for(int i = self1; i <= to1; ++i) {
            closure.call((Object)i);
         }

      }
   }

   public static void upto(long self, Number to, Closure closure) {
      long to1 = to.longValue();
      if (self > to1) {
         throw new GroovyRuntimeException("Infinite loop in " + self + ".upto(" + to + ")");
      } else {
         for(long i = self; i <= to1; ++i) {
            closure.call((Object)i);
         }

      }
   }

   public static void upto(Long self, Number to, Closure closure) {
      long to1 = to.longValue();
      if (self > to1) {
         throw new GroovyRuntimeException("Infinite loop in " + self + ".upto(" + to + ")");
      } else {
         for(long i = self; i <= to1; ++i) {
            closure.call((Object)i);
         }

      }
   }

   public static void upto(float self, Number to, Closure closure) {
      float to1 = to.floatValue();
      if (self > to1) {
         throw new GroovyRuntimeException("Infinite loop in " + self + ".upto(" + to + ")");
      } else {
         for(float i = self; i <= to1; ++i) {
            closure.call((Object)i);
         }

      }
   }

   public static void upto(Float self, Number to, Closure closure) {
      float to1 = to.floatValue();
      if (self > to1) {
         throw new GroovyRuntimeException("Infinite loop in " + self + ".upto(" + to + ")");
      } else {
         for(float i = self; i <= to1; ++i) {
            closure.call((Object)i);
         }

      }
   }

   public static void upto(double self, Number to, Closure closure) {
      double to1 = to.doubleValue();
      if (self > to1) {
         throw new GroovyRuntimeException("Infinite loop in " + self + ".upto(" + to + ")");
      } else {
         for(double i = self; i <= to1; ++i) {
            closure.call((Object)i);
         }

      }
   }

   public static void upto(Double self, Number to, Closure closure) {
      double to1 = to.doubleValue();
      if (self > to1) {
         throw new GroovyRuntimeException("Infinite loop in " + self + ".upto(" + to + ")");
      } else {
         for(double i = self; i <= to1; ++i) {
            closure.call((Object)i);
         }

      }
   }

   public static void upto(BigInteger self, Number to, Closure closure) {
      if (to instanceof BigDecimal) {
         BigDecimal one = BigDecimal.valueOf(10L, 1);
         BigDecimal self1 = new BigDecimal(self);
         BigDecimal to1 = (BigDecimal)to;
         if (self1.compareTo(to1) > 0) {
            throw new GroovyRuntimeException("Infinite loop in " + self + ".upto(" + to + ")");
         }

         for(BigDecimal i = self1; i.compareTo(to1) <= 0; i = i.add(one)) {
            closure.call((Object)i);
         }
      } else {
         BigInteger one;
         BigInteger to1;
         BigInteger i;
         if (to instanceof BigInteger) {
            one = BigInteger.valueOf(1L);
            to1 = (BigInteger)to;
            if (self.compareTo(to1) > 0) {
               throw new GroovyRuntimeException("Infinite loop in " + self + ".upto(" + to + ")");
            }

            for(i = self; i.compareTo(to1) <= 0; i = i.add(one)) {
               closure.call((Object)i);
            }
         } else {
            one = BigInteger.valueOf(1L);
            to1 = new BigInteger(to.toString());
            if (self.compareTo(to1) > 0) {
               throw new GroovyRuntimeException("Infinite loop in " + self + ".upto(" + to + ")");
            }

            for(i = self; i.compareTo(to1) <= 0; i = i.add(one)) {
               closure.call((Object)i);
            }
         }
      }

   }

   public static void upto(BigDecimal self, Number to, Closure closure) {
      BigDecimal one = BigDecimal.valueOf(10L, 1);
      BigDecimal to1;
      BigDecimal i;
      if (to instanceof BigDecimal) {
         to1 = (BigDecimal)to;
         if (self.compareTo(to1) > 0) {
            throw new GroovyRuntimeException("Infinite loop in " + self + ".upto(" + to + ")");
         }

         for(i = self; i.compareTo(to1) <= 0; i = i.add(one)) {
            closure.call((Object)i);
         }
      } else if (to instanceof BigInteger) {
         to1 = new BigDecimal((BigInteger)to);
         if (self.compareTo(to1) > 0) {
            throw new GroovyRuntimeException("Infinite loop in " + self + ".upto(" + to + ")");
         }

         for(i = self; i.compareTo(to1) <= 0; i = i.add(one)) {
            closure.call((Object)i);
         }
      } else {
         to1 = new BigDecimal(to.toString());
         if (self.compareTo(to1) > 0) {
            throw new GroovyRuntimeException("Infinite loop in " + self + ".upto(" + to + ")");
         }

         for(i = self; i.compareTo(to1) <= 0; i = i.add(one)) {
            closure.call((Object)i);
         }
      }

   }

   public static void downto(Number self, Number to, Closure closure) {
      int self1 = self.intValue();
      int to1 = to.intValue();
      if (self1 < to1) {
         throw new GroovyRuntimeException("Infinite loop in " + self + ".downto(" + to + ")");
      } else {
         for(int i = self1; i >= to1; --i) {
            closure.call((Object)i);
         }

      }
   }

   public static void downto(long self, Number to, Closure closure) {
      long to1 = to.longValue();
      if (self < to1) {
         throw new GroovyRuntimeException("Infinite loop in " + self + ".downto(" + to + ")");
      } else {
         for(long i = self; i >= to1; --i) {
            closure.call((Object)i);
         }

      }
   }

   public static void downto(Long self, Number to, Closure closure) {
      long to1 = to.longValue();
      if (self < to1) {
         throw new GroovyRuntimeException("Infinite loop in " + self + ".downto(" + to + ")");
      } else {
         for(long i = self; i >= to1; --i) {
            closure.call((Object)i);
         }

      }
   }

   public static void downto(float self, Number to, Closure closure) {
      float to1 = to.floatValue();
      if (self < to1) {
         throw new GroovyRuntimeException("Infinite loop in " + self + ".downto(" + to + ")");
      } else {
         for(float i = self; i >= to1; --i) {
            closure.call((Object)i);
         }

      }
   }

   public static void downto(Float self, Number to, Closure closure) {
      float to1 = to.floatValue();
      if (self < to1) {
         throw new GroovyRuntimeException("Infinite loop in " + self + ".downto(" + to + ")");
      } else {
         for(float i = self; i >= to1; --i) {
            closure.call((Object)i);
         }

      }
   }

   public static void downto(double self, Number to, Closure closure) {
      double to1 = to.doubleValue();
      if (self < to1) {
         throw new GroovyRuntimeException("Infinite loop in " + self + ".downto(" + to + ")");
      } else {
         for(double i = self; i >= to1; --i) {
            closure.call((Object)i);
         }

      }
   }

   public static void downto(Double self, Number to, Closure closure) {
      double to1 = to.doubleValue();
      if (self < to1) {
         throw new GroovyRuntimeException("Infinite loop in " + self + ".downto(" + to + ")");
      } else {
         for(double i = self; i >= to1; --i) {
            closure.call((Object)i);
         }

      }
   }

   public static void downto(BigInteger self, Number to, Closure closure) {
      if (to instanceof BigDecimal) {
         BigDecimal one = BigDecimal.valueOf(10L, 1);
         BigDecimal to1 = (BigDecimal)to;
         BigDecimal selfD = new BigDecimal(self);
         if (selfD.compareTo(to1) < 0) {
            throw new GroovyRuntimeException("Infinite loop in " + self + ".downto(" + to + ")");
         }

         for(BigDecimal i = selfD; i.compareTo(to1) >= 0; i = i.subtract(one)) {
            closure.call((Object)i.toBigInteger());
         }
      } else {
         BigInteger one;
         BigInteger to1;
         BigInteger i;
         if (to instanceof BigInteger) {
            one = BigInteger.valueOf(1L);
            to1 = (BigInteger)to;
            if (self.compareTo(to1) < 0) {
               throw new GroovyRuntimeException("Infinite loop in " + self + ".downto(" + to + ")");
            }

            for(i = self; i.compareTo(to1) >= 0; i = i.subtract(one)) {
               closure.call((Object)i);
            }
         } else {
            one = BigInteger.valueOf(1L);
            to1 = new BigInteger(to.toString());
            if (self.compareTo(to1) < 0) {
               throw new GroovyRuntimeException("Infinite loop in " + self + ".downto(" + to + ")");
            }

            for(i = self; i.compareTo(to1) >= 0; i = i.subtract(one)) {
               closure.call((Object)i);
            }
         }
      }

   }

   public static void downto(BigDecimal self, Number to, Closure closure) {
      BigDecimal one = BigDecimal.valueOf(10L, 1);
      BigDecimal to1;
      BigDecimal i;
      if (to instanceof BigDecimal) {
         to1 = (BigDecimal)to;
         if (self.compareTo(to1) < 0) {
            throw new GroovyRuntimeException("Infinite loop in " + self + ".downto(" + to + ")");
         }

         for(i = self; i.compareTo(to1) >= 0; i = i.subtract(one)) {
            closure.call((Object)i);
         }
      } else if (to instanceof BigInteger) {
         to1 = new BigDecimal((BigInteger)to);
         if (self.compareTo(to1) < 0) {
            throw new GroovyRuntimeException("Infinite loop in " + self + ".downto(" + to + ")");
         }

         for(i = self; i.compareTo(to1) >= 0; i = i.subtract(one)) {
            closure.call((Object)i);
         }
      } else {
         to1 = new BigDecimal(to.toString());
         if (self.compareTo(to1) < 0) {
            throw new GroovyRuntimeException("Infinite loop in " + self + ".downto(" + to + ")");
         }

         for(i = self; i.compareTo(to1) >= 0; i = i.subtract(one)) {
            closure.call((Object)i);
         }
      }

   }

   public static void step(Number self, Number to, Number stepNumber, Closure closure) {
      if (!(self instanceof BigDecimal) && !(to instanceof BigDecimal) && !(stepNumber instanceof BigDecimal)) {
         if (!(self instanceof BigInteger) && !(to instanceof BigInteger) && !(stepNumber instanceof BigInteger)) {
            int self1 = self.intValue();
            int to1 = to.intValue();
            int stepNumber1 = stepNumber.intValue();
            int i;
            if (stepNumber1 > 0 && to1 > self1) {
               for(i = self1; i < to1; i += stepNumber1) {
                  closure.call((Object)i);
               }
            } else if (stepNumber1 < 0 && to1 < self1) {
               for(i = self1; i > to1; i += stepNumber1) {
                  closure.call((Object)i);
               }
            } else if (self1 != to1) {
               throw new GroovyRuntimeException("Infinite loop in " + self1 + ".step(" + to1 + ", " + stepNumber1 + ")");
            }
         } else {
            BigInteger zero = BigInteger.valueOf(0L);
            BigInteger self1 = self instanceof BigInteger ? (BigInteger)self : new BigInteger(self.toString());
            BigInteger to1 = to instanceof BigInteger ? (BigInteger)to : new BigInteger(to.toString());
            BigInteger stepNumber1 = stepNumber instanceof BigInteger ? (BigInteger)stepNumber : new BigInteger(stepNumber.toString());
            BigInteger i;
            if (stepNumber1.compareTo(zero) > 0 && to1.compareTo(self1) > 0) {
               for(i = self1; i.compareTo(to1) < 0; i = i.add(stepNumber1)) {
                  closure.call((Object)i);
               }
            } else if (stepNumber1.compareTo(zero) < 0 && to1.compareTo(self1) < 0) {
               for(i = self1; i.compareTo(to1) > 0; i = i.add(stepNumber1)) {
                  closure.call((Object)i);
               }
            } else if (self1.compareTo(to1) != 0) {
               throw new GroovyRuntimeException("Infinite loop in " + self1 + ".step(" + to1 + ", " + stepNumber1 + ")");
            }
         }
      } else {
         BigDecimal zero = BigDecimal.valueOf(0L, 1);
         BigDecimal self1 = self instanceof BigDecimal ? (BigDecimal)self : new BigDecimal(self.toString());
         BigDecimal to1 = to instanceof BigDecimal ? (BigDecimal)to : new BigDecimal(to.toString());
         BigDecimal stepNumber1 = stepNumber instanceof BigDecimal ? (BigDecimal)stepNumber : new BigDecimal(stepNumber.toString());
         BigDecimal i;
         if (stepNumber1.compareTo(zero) > 0 && to1.compareTo(self1) > 0) {
            for(i = self1; i.compareTo(to1) < 0; i = i.add(stepNumber1)) {
               closure.call((Object)i);
            }
         } else if (stepNumber1.compareTo(zero) < 0 && to1.compareTo(self1) < 0) {
            for(i = self1; i.compareTo(to1) > 0; i = i.add(stepNumber1)) {
               closure.call((Object)i);
            }
         } else if (self1.compareTo(to1) != 0) {
            throw new GroovyRuntimeException("Infinite loop in " + self1 + ".step(" + to1 + ", " + stepNumber1 + ")");
         }
      }

   }

   public static int abs(Number number) {
      return Math.abs(number.intValue());
   }

   public static long abs(Long number) {
      return Math.abs(number);
   }

   public static float abs(Float number) {
      return Math.abs(number);
   }

   public static double abs(Double number) {
      return Math.abs(number);
   }

   public static int round(Float number) {
      return Math.round(number);
   }

   public static float round(Float number, int precision) {
      return (float)(Math.floor(number.doubleValue() * Math.pow(10.0D, (double)precision) + 0.5D) / Math.pow(10.0D, (double)precision));
   }

   public static float trunc(Float number, int precision) {
      return (float)(Math.floor(number.doubleValue() * Math.pow(10.0D, (double)precision)) / Math.pow(10.0D, (double)precision));
   }

   public static float trunc(Float number) {
      return (float)Math.floor(number.doubleValue());
   }

   public static long round(Double number) {
      return Math.round(number);
   }

   public static double round(Double number, int precision) {
      return Math.floor(number * Math.pow(10.0D, (double)precision) + 0.5D) / Math.pow(10.0D, (double)precision);
   }

   public static double trunc(Double number) {
      return Math.floor(number);
   }

   public static double trunc(Double number, int precision) {
      return Math.floor(number * Math.pow(10.0D, (double)precision)) / Math.pow(10.0D, (double)precision);
   }

   public static Integer toInteger(String self) {
      return Integer.valueOf(self.trim());
   }

   public static Long toLong(String self) {
      return Long.valueOf(self.trim());
   }

   public static Short toShort(String self) {
      return Short.valueOf(self.trim());
   }

   public static Float toFloat(String self) {
      return Float.valueOf(self.trim());
   }

   public static Double toDouble(String self) {
      return Double.valueOf(self.trim());
   }

   public static BigInteger toBigInteger(String self) {
      return new BigInteger(self.trim());
   }

   public static BigDecimal toBigDecimal(String self) {
      return new BigDecimal(self.trim());
   }

   public static boolean isInteger(String self) {
      try {
         Integer.valueOf(self.trim());
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   public static boolean isLong(String self) {
      try {
         Long.valueOf(self.trim());
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   public static boolean isFloat(String self) {
      try {
         Float.valueOf(self.trim());
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   public static boolean isDouble(String self) {
      try {
         Double.valueOf(self.trim());
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   public static boolean isBigInteger(String self) {
      try {
         new BigInteger(self.trim());
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   public static boolean isBigDecimal(String self) {
      try {
         new BigDecimal(self.trim());
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   public static boolean isNumber(String self) {
      return isBigDecimal(self);
   }

   public static boolean isUpperCase(Character self) {
      return Character.isUpperCase(self);
   }

   public static boolean isLowerCase(Character self) {
      return Character.isLowerCase(self);
   }

   public static boolean isLetter(Character self) {
      return Character.isLetter(self);
   }

   public static boolean isDigit(Character self) {
      return Character.isDigit(self);
   }

   public static boolean isLetterOrDigit(Character self) {
      return Character.isLetterOrDigit(self);
   }

   public static boolean isWhitespace(Character self) {
      return Character.isWhitespace(self);
   }

   public static char toUpperCase(Character self) {
      return Character.toUpperCase(self);
   }

   public static char toLowerCase(Character self) {
      return Character.toLowerCase(self);
   }

   public static Integer toInteger(Number self) {
      return self.intValue();
   }

   public static Long toLong(Number self) {
      return self.longValue();
   }

   public static Float toFloat(Number self) {
      return self.floatValue();
   }

   public static Double toDouble(Number self) {
      return !(self instanceof Double) && !(self instanceof Long) && !(self instanceof Integer) && !(self instanceof Short) && !(self instanceof Byte) ? Double.valueOf(self.toString()) : self.doubleValue();
   }

   public static BigDecimal toBigDecimal(Number self) {
      return !(self instanceof Long) && !(self instanceof Integer) && !(self instanceof Short) && !(self instanceof Byte) ? new BigDecimal(self.toString()) : BigDecimal.valueOf(self.longValue());
   }

   public static Object asType(Number self, Class c) {
      if (c == BigDecimal.class) {
         return toBigDecimal(self);
      } else if (c == BigInteger.class) {
         return toBigInteger(self);
      } else if (c == Double.class) {
         return toDouble(self);
      } else {
         return c == Float.class ? toFloat(self) : asType((Object)self, c);
      }
   }

   public static BigInteger toBigInteger(Number self) {
      if (self instanceof BigInteger) {
         return (BigInteger)self;
      } else if (self instanceof BigDecimal) {
         return ((BigDecimal)self).toBigInteger();
      } else if (self instanceof Double) {
         return (new BigDecimal((Double)self)).toBigInteger();
      } else {
         return self instanceof Float ? (new BigDecimal((double)(Float)self)).toBigInteger() : new BigInteger(Long.toString(self.longValue()));
      }
   }

   public static Boolean and(Boolean left, Boolean right) {
      return left && right;
   }

   public static Boolean or(Boolean left, Boolean right) {
      return left || right;
   }

   public static Boolean xor(Boolean left, Boolean right) {
      return left ^ right;
   }

   public static ObjectOutputStream newObjectOutputStream(File file) throws IOException {
      return new ObjectOutputStream(new FileOutputStream(file));
   }

   public static ObjectOutputStream newObjectOutputStream(OutputStream outputStream) throws IOException {
      return new ObjectOutputStream(outputStream);
   }

   public static Object withObjectOutputStream(File file, Closure closure) throws IOException {
      return withStream((OutputStream)newObjectOutputStream(file), closure);
   }

   public static Object withObjectOutputStream(OutputStream outputStream, Closure closure) throws IOException {
      return withStream((OutputStream)newObjectOutputStream(outputStream), closure);
   }

   public static ObjectInputStream newObjectInputStream(File file) throws IOException {
      return new ObjectInputStream(new FileInputStream(file));
   }

   public static ObjectInputStream newObjectInputStream(InputStream inputStream) throws IOException {
      return new ObjectInputStream(inputStream);
   }

   public static ObjectInputStream newObjectInputStream(InputStream inputStream, final ClassLoader classLoader) throws IOException {
      return new ObjectInputStream(inputStream) {
         protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            return Class.forName(desc.getName(), true, classLoader);
         }
      };
   }

   public static ObjectInputStream newObjectInputStream(File file, ClassLoader classLoader) throws IOException {
      return newObjectInputStream((InputStream)(new FileInputStream(file)), classLoader);
   }

   public static void eachObject(File self, Closure closure) throws IOException, ClassNotFoundException {
      eachObject(newObjectInputStream(self), closure);
   }

   public static void eachObject(ObjectInputStream ois, Closure closure) throws IOException, ClassNotFoundException {
      try {
         while(true) {
            try {
               Object obj = ois.readObject();
               closure.call(obj);
            } catch (EOFException var6) {
               ObjectInputStream temp = ois;
               ois = null;
               temp.close();
               return;
            }
         }
      } finally {
         closeWithWarning(ois);
      }
   }

   public static Object withObjectInputStream(File file, Closure closure) throws IOException {
      return withStream((InputStream)newObjectInputStream(file), closure);
   }

   public static Object withObjectInputStream(File file, ClassLoader classLoader, Closure closure) throws IOException {
      return withStream((InputStream)newObjectInputStream(file, classLoader), closure);
   }

   public static Object withObjectInputStream(InputStream inputStream, Closure closure) throws IOException {
      return withStream((InputStream)newObjectInputStream(inputStream), closure);
   }

   public static Object withObjectInputStream(InputStream inputStream, ClassLoader classLoader, Closure closure) throws IOException {
      return withStream((InputStream)newObjectInputStream(inputStream, classLoader), closure);
   }

   public static Object eachLine(String self, Closure closure) throws IOException {
      return eachLine((String)self, 0, closure);
   }

   public static Object eachLine(String self, int firstLine, Closure closure) throws IOException {
      int count = firstLine;
      Object result = null;

      for(Iterator i$ = readLines(self).iterator(); i$.hasNext(); ++count) {
         String line = (String)i$.next();
         result = callClosureForLine(closure, line, count);
      }

      return result;
   }

   public static Object eachLine(File self, Closure closure) throws IOException {
      return eachLine((File)self, 1, closure);
   }

   public static Object eachLine(File self, String charset, Closure closure) throws IOException {
      return eachLine((File)self, charset, 1, closure);
   }

   public static Object eachLine(File self, int firstLine, Closure closure) throws IOException {
      return eachLine((Reader)newReader(self), firstLine, closure);
   }

   public static Object eachLine(File self, String charset, int firstLine, Closure closure) throws IOException {
      return eachLine((Reader)newReader(self, charset), firstLine, closure);
   }

   public static Object eachLine(InputStream stream, String charset, Closure closure) throws IOException {
      return eachLine((InputStream)stream, charset, 1, closure);
   }

   public static Object eachLine(InputStream stream, String charset, int firstLine, Closure closure) throws IOException {
      return eachLine((Reader)(new InputStreamReader(stream, charset)), firstLine, closure);
   }

   public static Object eachLine(InputStream stream, Closure closure) throws IOException {
      return eachLine((InputStream)stream, 1, closure);
   }

   public static Object eachLine(InputStream stream, int firstLine, Closure closure) throws IOException {
      return eachLine((Reader)(new InputStreamReader(stream)), firstLine, closure);
   }

   public static Object eachLine(URL url, Closure closure) throws IOException {
      return eachLine((URL)url, 1, closure);
   }

   public static Object eachLine(URL url, int firstLine, Closure closure) throws IOException {
      return eachLine(url.openConnection().getInputStream(), firstLine, closure);
   }

   public static Object eachLine(URL url, String charset, Closure closure) throws IOException {
      return eachLine((URL)url, charset, 1, closure);
   }

   public static Object eachLine(URL url, String charset, int firstLine, Closure closure) throws IOException {
      return eachLine((Reader)newReader(url, charset), firstLine, closure);
   }

   public static Object eachLine(Reader self, Closure closure) throws IOException {
      return eachLine((Reader)self, 1, closure);
   }

   public static Object eachLine(Reader self, int firstLine, Closure closure) throws IOException {
      int count = firstLine;
      Object result = null;
      BufferedReader br;
      if (self instanceof BufferedReader) {
         br = (BufferedReader)self;
      } else {
         br = new BufferedReader(self);
      }

      try {
         while(true) {
            String line = br.readLine();
            if (line == null) {
               Reader temp = self;
               self = null;
               temp.close();
               Object var7 = result;
               return var7;
            }

            result = callClosureForLine(closure, line, count);
            ++count;
         }
      } finally {
         closeWithWarning(self);
         closeWithWarning(br);
      }
   }

   public static Object splitEachLine(File self, String regex, Closure closure) throws IOException {
      return splitEachLine((Reader)newReader(self), (String)regex, closure);
   }

   public static Object splitEachLine(File self, Pattern pattern, Closure closure) throws IOException {
      return splitEachLine((Reader)newReader(self), (Pattern)pattern, closure);
   }

   public static Object splitEachLine(File self, String regex, String charset, Closure closure) throws IOException {
      return splitEachLine((Reader)newReader(self, charset), (String)regex, closure);
   }

   public static Object splitEachLine(File self, Pattern pattern, String charset, Closure closure) throws IOException {
      return splitEachLine((Reader)newReader(self, charset), (Pattern)pattern, closure);
   }

   public static Object splitEachLine(URL self, String regex, Closure closure) throws IOException {
      return splitEachLine((Reader)newReader(self), (String)regex, closure);
   }

   public static Object splitEachLine(URL self, Pattern pattern, Closure closure) throws IOException {
      return splitEachLine((Reader)newReader(self), (Pattern)pattern, closure);
   }

   public static Object splitEachLine(URL self, String regex, String charset, Closure closure) throws IOException {
      return splitEachLine((Reader)newReader(self, charset), (String)regex, closure);
   }

   public static Object splitEachLine(URL self, Pattern pattern, String charset, Closure closure) throws IOException {
      return splitEachLine((Reader)newReader(self, charset), (Pattern)pattern, closure);
   }

   public static Object splitEachLine(Reader self, String regex, Closure closure) throws IOException {
      return splitEachLine(self, Pattern.compile(regex), closure);
   }

   public static Object splitEachLine(Reader self, Pattern pattern, Closure closure) throws IOException {
      Object result = null;
      BufferedReader br;
      if (self instanceof BufferedReader) {
         br = (BufferedReader)self;
      } else {
         br = new BufferedReader(self);
      }

      try {
         while(true) {
            String line = br.readLine();
            if (line == null) {
               Reader temp = self;
               self = null;
               temp.close();
               Object var11 = result;
               return var11;
            }

            List vals = Arrays.asList(pattern.split(line));
            result = closure.call((Object)vals);
         }
      } finally {
         closeWithWarning(self);
         closeWithWarning(br);
      }
   }

   public static Object splitEachLine(InputStream stream, String regex, String charset, Closure closure) throws IOException {
      return splitEachLine((Reader)(new BufferedReader(new InputStreamReader(stream, charset))), (String)regex, closure);
   }

   public static Object splitEachLine(InputStream stream, Pattern pattern, String charset, Closure closure) throws IOException {
      return splitEachLine((Reader)(new BufferedReader(new InputStreamReader(stream, charset))), (Pattern)pattern, closure);
   }

   public static Object splitEachLine(InputStream stream, String regex, Closure closure) throws IOException {
      return splitEachLine((Reader)(new BufferedReader(new InputStreamReader(stream))), (String)regex, closure);
   }

   public static Object splitEachLine(InputStream stream, Pattern pattern, Closure closure) throws IOException {
      return splitEachLine((Reader)(new BufferedReader(new InputStreamReader(stream))), (Pattern)pattern, closure);
   }

   public static Object splitEachLine(String self, String regex, Closure closure) throws IOException {
      return splitEachLine(self, Pattern.compile(regex), closure);
   }

   public static Object splitEachLine(String self, Pattern pattern, Closure closure) throws IOException {
      List<String> list = readLines(self);
      Object result = null;

      List vals;
      for(Iterator i$ = list.iterator(); i$.hasNext(); result = closure.call((Object)vals)) {
         String line = (String)i$.next();
         vals = Arrays.asList(pattern.split(line));
      }

      return result;
   }

   public static String readLine(Reader self) throws IOException {
      if (self instanceof BufferedReader) {
         BufferedReader br = (BufferedReader)self;
         return br.readLine();
      } else {
         return self.markSupported() ? readLineFromReaderWithMark(self) : readLineFromReaderWithoutMark(self);
      }
   }

   private static String readLineFromReaderWithMark(Reader input) throws IOException {
      char[] cbuf = new char[charBufferSize];

      try {
         input.mark(charBufferSize);
      } catch (IOException var6) {
         LOG.warning("Caught exception setting mark on supporting reader: " + var6);
         return readLineFromReaderWithoutMark(input);
      }

      int count = input.read(cbuf);
      if (count == EOF) {
         return null;
      } else {
         StringBuffer line = new StringBuffer(expectedLineLength);

         int ls;
         for(ls = lineSeparatorIndex(cbuf, count); ls == -1; ls = lineSeparatorIndex(cbuf, count)) {
            line.append(cbuf, 0, count);
            count = input.read(cbuf);
            if (count == EOF) {
               return line.toString();
            }
         }

         line.append(cbuf, 0, ls);
         int skipLS = 1;
         if (ls + 1 < count) {
            if (cbuf[ls] == '\r' && cbuf[ls + 1] == '\n') {
               ++skipLS;
            }
         } else if (cbuf[ls] == '\r' && input.read() == 10) {
            ++skipLS;
         }

         input.reset();
         input.skip((long)(line.length() + skipLS));
         return line.toString();
      }
   }

   private static String readLineFromReaderWithoutMark(Reader input) throws IOException {
      int c = input.read();
      if (c == -1) {
         return null;
      } else {
         StringBuffer line;
         for(line = new StringBuffer(expectedLineLength); c != EOF && c != 10 && c != 13; c = input.read()) {
            char ch = (char)c;
            line.append(ch);
         }

         return line.toString();
      }
   }

   private static int lineSeparatorIndex(char[] array, int length) {
      for(int k = 0; k < length; ++k) {
         if (isLineSeparator(array[k])) {
            return k;
         }
      }

      return -1;
   }

   private static boolean isLineSeparator(char c) {
      return c == '\n' || c == '\r';
   }

   public static String denormalize(String self) {
      if (lineSeparator == null) {
         StringWriter sw = new StringWriter(2);

         try {
            BufferedWriter bw = new BufferedWriter(sw);
            bw.newLine();
            bw.flush();
            lineSeparator = sw.toString();
         } catch (IOException var5) {
            lineSeparator = "\n";
         }
      }

      int len = self.length();
      if (len < 1) {
         return self;
      } else {
         StringBuilder sb = new StringBuilder(110 * len / 100);
         int i = 0;

         while(i < len) {
            char ch = self.charAt(i++);
            switch(ch) {
            case '\n':
               sb.append(lineSeparator);
               break;
            case '\r':
               sb.append(lineSeparator);
               if (i < len && self.charAt(i) == '\n') {
                  ++i;
               }
               break;
            default:
               sb.append(ch);
            }
         }

         return sb.toString();
      }
   }

   public static String normalize(String self) {
      int nx = self.indexOf(13);
      if (nx < 0) {
         return self;
      } else {
         int len = self.length();
         StringBuilder sb = new StringBuilder(len);
         int i = 0;

         do {
            sb.append(self, i, nx);
            sb.append('\n');
            if ((i = nx + 1) >= len) {
               break;
            }

            if (self.charAt(i) == '\n') {
               ++i;
               if (i >= len) {
                  break;
               }
            }

            nx = self.indexOf(13, i);
         } while(nx > 0);

         sb.append(self, i, len);
         return sb.toString();
      }
   }

   public static List<String> readLines(String self) throws IOException {
      return readLines((Reader)(new StringReader(self)));
   }

   public static List<String> readLines(File file) throws IOException {
      return readLines((Reader)newReader(file));
   }

   public static List<String> readLines(File file, String charset) throws IOException {
      return readLines((Reader)newReader(file, charset));
   }

   public static List<String> readLines(InputStream stream) throws IOException {
      return readLines((Reader)newReader(stream));
   }

   public static List<String> readLines(InputStream stream, String charset) throws IOException {
      return readLines((Reader)newReader(stream, charset));
   }

   public static List<String> readLines(URL self) throws IOException {
      return readLines((Reader)newReader(self));
   }

   public static List<String> readLines(URL self, String charset) throws IOException {
      return readLines((Reader)newReader(self, charset));
   }

   public static List<String> readLines(Reader reader) throws IOException {
      IteratorClosureAdapter closure = new IteratorClosureAdapter(reader);
      eachLine((Reader)reader, closure);
      return closure.asList();
   }

   public static String getText(File file, String charset) throws IOException {
      return getText(newReader(file, charset));
   }

   public static String getText(File file) throws IOException {
      return getText(newReader(file));
   }

   public static String getText(URL url) throws IOException {
      return getText(url, CharsetToolkit.getDefaultSystemCharset().toString());
   }

   public static String getText(URL url, String charset) throws IOException {
      BufferedReader reader = newReader(url, charset);
      return getText(reader);
   }

   public static String getText(InputStream is) throws IOException {
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      return getText(reader);
   }

   public static String getText(InputStream is, String charset) throws IOException {
      BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
      return getText(reader);
   }

   public static String getText(Reader reader) throws IOException {
      BufferedReader bufferedReader = new BufferedReader(reader);
      return getText(bufferedReader);
   }

   public static String getText(BufferedReader reader) throws IOException {
      StringBuilder answer = new StringBuilder();
      char[] charBuffer = new char[8192];

      try {
         int nbCharRead;
         while((nbCharRead = reader.read(charBuffer)) != -1) {
            answer.append(charBuffer, 0, nbCharRead);
         }

         Reader temp = reader;
         reader = null;
         temp.close();
      } finally {
         closeWithWarning(reader);
      }

      return answer.toString();
   }

   public static byte[] getBytes(File file) throws IOException {
      return getBytes((InputStream)(new FileInputStream(file)));
   }

   public static byte[] getBytes(URL url) throws IOException {
      return getBytes(url.openConnection().getInputStream());
   }

   public static byte[] getBytes(InputStream is) throws IOException {
      ByteArrayOutputStream answer = new ByteArrayOutputStream();
      byte[] byteBuffer = new byte[8192];

      int nbByteRead;
      try {
         while((nbByteRead = is.read(byteBuffer)) != -1) {
            answer.write(byteBuffer, 0, nbByteRead);
         }
      } finally {
         closeWithWarning(is);
      }

      return answer.toByteArray();
   }

   public static void setBytes(File file, byte[] bytes) throws IOException {
      setBytes((OutputStream)(new FileOutputStream(file)), bytes);
   }

   public static void setBytes(OutputStream os, byte[] bytes) throws IOException {
      try {
         os.write(bytes);
      } finally {
         closeWithWarning(os);
      }

   }

   public static void writeLine(BufferedWriter writer, String line) throws IOException {
      writer.write(line);
      writer.newLine();
   }

   public static void write(File file, String text) throws IOException {
      BufferedWriter writer = null;

      try {
         writer = newWriter(file);
         writer.write(text);
         writer.flush();
         Writer temp = writer;
         writer = null;
         temp.close();
      } finally {
         closeWithWarning(writer);
      }

   }

   public static void setText(File file, String text) throws IOException {
      write(file, text);
   }

   public static void setText(File file, String text, String charset) throws IOException {
      write(file, text, charset);
   }

   public static File leftShift(File file, Object text) throws IOException {
      append(file, text);
      return file;
   }

   public static File leftShift(File file, byte[] bytes) throws IOException {
      append(file, bytes);
      return file;
   }

   public static File leftShift(File file, InputStream data) throws IOException {
      append(file, data);
      return file;
   }

   public static void write(File file, String text, String charset) throws IOException {
      BufferedWriter writer = null;

      try {
         writer = newWriter(file, charset);
         writer.write(text);
         writer.flush();
         Writer temp = writer;
         writer = null;
         temp.close();
      } finally {
         closeWithWarning(writer);
      }

   }

   public static void append(File file, Object text) throws IOException {
      BufferedWriter writer = null;

      try {
         writer = newWriter(file, true);
         InvokerHelper.write(writer, text);
         writer.flush();
         Writer temp = writer;
         writer = null;
         temp.close();
      } finally {
         closeWithWarning(writer);
      }

   }

   public static void append(File file, byte[] bytes) throws IOException {
      BufferedOutputStream stream = null;

      try {
         stream = new BufferedOutputStream(new FileOutputStream(file, true));
         stream.write(bytes, 0, bytes.length);
         stream.flush();
         OutputStream temp = stream;
         stream = null;
         temp.close();
      } finally {
         closeWithWarning(stream);
      }

   }

   public static void append(File self, InputStream stream) throws IOException {
      FileOutputStream out = new FileOutputStream(self, true);

      try {
         leftShift((OutputStream)out, (InputStream)stream);
      } finally {
         closeWithWarning(out);
      }

   }

   public static void append(File file, Object text, String charset) throws IOException {
      BufferedWriter writer = null;

      try {
         writer = newWriter(file, charset, true);
         InvokerHelper.write(writer, text);
         writer.flush();
         Writer temp = writer;
         writer = null;
         temp.close();
      } finally {
         closeWithWarning(writer);
      }

   }

   private static void checkDir(File dir) throws FileNotFoundException, IllegalArgumentException {
      if (!dir.exists()) {
         throw new FileNotFoundException(dir.getAbsolutePath());
      } else if (!dir.isDirectory()) {
         throw new IllegalArgumentException("The provided File object is not a directory: " + dir.getAbsolutePath());
      }
   }

   public static void eachFile(File self, FileType fileType, Closure closure) throws FileNotFoundException, IllegalArgumentException {
      checkDir(self);
      File[] files = self.listFiles();
      if (files != null) {
         File[] arr$ = files;
         int len$ = files.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            File file = arr$[i$];
            if (fileType == FileType.ANY || fileType != FileType.FILES && file.isDirectory() || fileType != FileType.DIRECTORIES && file.isFile()) {
               closure.call((Object)file);
            }
         }

      }
   }

   public static void eachFile(File self, Closure closure) throws FileNotFoundException, IllegalArgumentException {
      eachFile(self, FileType.ANY, closure);
   }

   public static void eachDir(File self, Closure closure) throws FileNotFoundException, IllegalArgumentException {
      eachFile(self, FileType.DIRECTORIES, closure);
   }

   public static void eachFileRecurse(File self, FileType fileType, Closure closure) throws FileNotFoundException, IllegalArgumentException {
      checkDir(self);
      File[] files = self.listFiles();
      if (files != null) {
         File[] arr$ = files;
         int len$ = files.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            File file = arr$[i$];
            if (file.isDirectory()) {
               if (fileType != FileType.FILES) {
                  closure.call((Object)file);
               }

               eachFileRecurse(file, fileType, closure);
            } else if (fileType != FileType.DIRECTORIES) {
               closure.call((Object)file);
            }
         }

      }
   }

   public static void traverse(File self, Map<String, Object> options, Closure closure) throws FileNotFoundException, IllegalArgumentException {
      Number maxDepthNumber = (Number)asType(options.remove("maxDepth"), Number.class);
      int maxDepth = maxDepthNumber == null ? -1 : maxDepthNumber.intValue();
      Boolean visitRoot = (Boolean)asType(get(options, "visitRoot", false), Boolean.class);
      Boolean preRoot = (Boolean)asType(get(options, "preRoot", false), Boolean.class);
      Boolean postRoot = (Boolean)asType(get(options, "postRoot", false), Boolean.class);
      Closure pre = (Closure)options.get("preDir");
      Closure post = (Closure)options.get("postDir");
      FileType type = (FileType)options.get("type");
      Object filter = options.get("filter");
      Object nameFilter = options.get("nameFilter");
      Object excludeFilter = options.get("excludeFilter");
      Object excludeNameFilter = options.get("excludeNameFilter");
      Object preResult = null;
      if (preRoot && pre != null) {
         preResult = pre.call((Object)self);
      }

      if (preResult != FileVisitResult.TERMINATE && preResult != FileVisitResult.SKIP_SUBTREE) {
         FileVisitResult terminated = traverse(self, options, closure, maxDepth);
         if (type != FileType.FILES && visitRoot && closure != null && notFiltered(self, filter, nameFilter, excludeFilter, excludeNameFilter)) {
            Object closureResult = closure.call((Object)self);
            if (closureResult == FileVisitResult.TERMINATE) {
               return;
            }
         }

         if (postRoot && post != null && terminated != FileVisitResult.TERMINATE) {
            post.call((Object)self);
         }

      }
   }

   private static boolean notFiltered(File file, Object filter, Object nameFilter, Object excludeFilter, Object excludeNameFilter) {
      if (filter == null && nameFilter == null && excludeFilter == null && excludeNameFilter == null) {
         return true;
      } else if (filter != null && nameFilter != null) {
         throw new IllegalArgumentException("Can't set both 'filter' and 'nameFilter'");
      } else if (excludeFilter != null && excludeNameFilter != null) {
         throw new IllegalArgumentException("Can't set both 'excludeFilter' and 'excludeNameFilter'");
      } else {
         Object filterToUse = null;
         Object filterParam = null;
         if (filter != null) {
            filterToUse = filter;
            filterParam = file;
         } else if (nameFilter != null) {
            filterToUse = nameFilter;
            filterParam = file.getName();
         }

         Object excludeFilterToUse = null;
         Object excludeParam = null;
         if (excludeFilter != null) {
            excludeFilterToUse = excludeFilter;
            excludeParam = file;
         } else if (excludeNameFilter != null) {
            excludeFilterToUse = excludeNameFilter;
            excludeParam = file.getName();
         }

         MetaClass filterMC = filterToUse == null ? null : InvokerHelper.getMetaClass(filterToUse);
         MetaClass excludeMC = excludeFilterToUse == null ? null : InvokerHelper.getMetaClass(excludeFilterToUse);
         boolean included = filterToUse == null || filterToUse != null && DefaultTypeTransformation.castToBoolean(filterMC.invokeMethod(filterToUse, "isCase", filterParam));
         boolean excluded = excludeFilterToUse != null && DefaultTypeTransformation.castToBoolean(excludeMC.invokeMethod(excludeFilterToUse, "isCase", excludeParam));
         return included && !excluded;
      }
   }

   public static void traverse(File self, Closure closure) throws FileNotFoundException, IllegalArgumentException {
      traverse(self, new HashMap(), closure);
   }

   public static void traverse(File self, Map<String, Object> options) throws FileNotFoundException, IllegalArgumentException {
      Closure visit = (Closure)options.remove("visit");
      traverse(self, options, visit);
   }

   private static FileVisitResult traverse(File self, Map<String, Object> options, Closure closure, int maxDepth) throws FileNotFoundException, IllegalArgumentException {
      checkDir(self);
      Closure pre = (Closure)options.get("preDir");
      Closure post = (Closure)options.get("postDir");
      FileType type = (FileType)options.get("type");
      Object filter = options.get("filter");
      Object nameFilter = options.get("nameFilter");
      Object excludeFilter = options.get("excludeFilter");
      Object excludeNameFilter = options.get("excludeNameFilter");
      Closure sort = (Closure)options.get("sort");
      File[] origFiles = self.listFiles();
      if (origFiles != null) {
         List<File> files = Arrays.asList(origFiles);
         if (sort != null) {
            files = sort((Collection)files, (Closure)sort);
         }

         Iterator i$ = files.iterator();

         while(i$.hasNext()) {
            File file = (File)i$.next();
            Object preResult;
            if (file.isDirectory()) {
               if (type != FileType.FILES && closure != null && notFiltered(file, filter, nameFilter, excludeFilter, excludeNameFilter)) {
                  preResult = closure.call((Object)file);
                  if (preResult == FileVisitResult.SKIP_SIBLINGS) {
                     break;
                  }

                  if (preResult == FileVisitResult.TERMINATE) {
                     return FileVisitResult.TERMINATE;
                  }
               }

               if (maxDepth != 0) {
                  preResult = null;
                  if (pre != null) {
                     preResult = pre.call((Object)file);
                  }

                  if (preResult == FileVisitResult.SKIP_SIBLINGS) {
                     break;
                  }

                  if (preResult == FileVisitResult.TERMINATE) {
                     return FileVisitResult.TERMINATE;
                  }

                  if (preResult != FileVisitResult.SKIP_SUBTREE) {
                     FileVisitResult terminated = traverse(file, options, closure, maxDepth - 1);
                     if (terminated == FileVisitResult.TERMINATE) {
                        return terminated;
                     }
                  }

                  Object postResult = null;
                  if (post != null) {
                     postResult = post.call((Object)file);
                  }

                  if (postResult == FileVisitResult.SKIP_SIBLINGS) {
                     break;
                  }

                  if (postResult == FileVisitResult.TERMINATE) {
                     return FileVisitResult.TERMINATE;
                  }
               }
            } else if (type != FileType.DIRECTORIES && closure != null && notFiltered(file, filter, nameFilter, excludeFilter, excludeNameFilter)) {
               preResult = closure.call((Object)file);
               if (preResult == FileVisitResult.SKIP_SIBLINGS) {
                  break;
               }

               if (preResult == FileVisitResult.TERMINATE) {
                  return FileVisitResult.TERMINATE;
               }
            }
         }
      }

      return FileVisitResult.CONTINUE;
   }

   public static void eachFileRecurse(File self, Closure closure) throws FileNotFoundException, IllegalArgumentException {
      eachFileRecurse(self, FileType.ANY, closure);
   }

   public static void eachDirRecurse(File self, Closure closure) throws FileNotFoundException, IllegalArgumentException {
      eachFileRecurse(self, FileType.DIRECTORIES, closure);
   }

   public static void eachFileMatch(File self, FileType fileType, Object nameFilter, Closure closure) throws FileNotFoundException, IllegalArgumentException {
      checkDir(self);
      File[] files = self.listFiles();
      if (files != null) {
         MetaClass metaClass = InvokerHelper.getMetaClass(nameFilter);
         File[] arr$ = files;
         int len$ = files.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            File currentFile = arr$[i$];
            if ((fileType != FileType.FILES && currentFile.isDirectory() || fileType != FileType.DIRECTORIES && currentFile.isFile()) && DefaultTypeTransformation.castToBoolean(metaClass.invokeMethod(nameFilter, "isCase", currentFile.getName()))) {
               closure.call((Object)currentFile);
            }
         }

      }
   }

   public static void eachFileMatch(File self, Object nameFilter, Closure closure) throws FileNotFoundException, IllegalArgumentException {
      eachFileMatch(self, FileType.ANY, nameFilter, closure);
   }

   public static void eachDirMatch(File self, Object nameFilter, Closure closure) throws FileNotFoundException, IllegalArgumentException {
      eachFileMatch(self, FileType.DIRECTORIES, nameFilter, closure);
   }

   public static boolean deleteDir(File self) {
      if (!self.exists()) {
         return true;
      } else if (!self.isDirectory()) {
         return false;
      } else {
         File[] files = self.listFiles();
         if (files == null) {
            return false;
         } else {
            boolean result = true;
            File[] arr$ = files;
            int len$ = files.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               File file = arr$[i$];
               if (file.isDirectory()) {
                  if (!deleteDir(file)) {
                     result = false;
                  }
               } else if (!file.delete()) {
                  result = false;
               }
            }

            if (!self.delete()) {
               result = false;
            }

            return result;
         }
      }
   }

   public static boolean renameTo(File self, String newPathName) {
      return self.renameTo(new File(newPathName));
   }

   public static TimerTask runAfter(Timer timer, int delay, final Closure closure) {
      TimerTask timerTask = new TimerTask() {
         public void run() {
            closure.call();
         }
      };
      timer.schedule(timerTask, (long)delay);
      return timerTask;
   }

   public static BufferedReader newReader(File file) throws IOException {
      CharsetToolkit toolkit = new CharsetToolkit(file);
      return toolkit.getReader();
   }

   public static BufferedReader newReader(File file, String charset) throws FileNotFoundException, UnsupportedEncodingException {
      return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
   }

   public static BufferedReader newReader(InputStream self) {
      return new BufferedReader(new InputStreamReader(self));
   }

   public static BufferedReader newReader(InputStream self, String charset) throws UnsupportedEncodingException {
      return new BufferedReader(new InputStreamReader(self, charset));
   }

   public static Object withReader(File file, Closure closure) throws IOException {
      return withReader((Reader)newReader(file), closure);
   }

   public static Object withReader(File file, String charset, Closure closure) throws IOException {
      return withReader((Reader)newReader(file, charset), closure);
   }

   public static BufferedOutputStream newOutputStream(File file) throws IOException {
      return new BufferedOutputStream(new FileOutputStream(file));
   }

   public static DataOutputStream newDataOutputStream(File file) throws IOException {
      return new DataOutputStream(new FileOutputStream(file));
   }

   public static Object withOutputStream(File file, Closure closure) throws IOException {
      return withStream((OutputStream)newOutputStream(file), closure);
   }

   public static Object withInputStream(File file, Closure closure) throws IOException {
      return withStream((InputStream)newInputStream(file), closure);
   }

   public static Object withInputStream(URL url, Closure closure) throws IOException {
      return withStream((InputStream)newInputStream(url), closure);
   }

   public static Object withDataOutputStream(File file, Closure closure) throws IOException {
      return withStream((OutputStream)newDataOutputStream(file), closure);
   }

   public static Object withDataInputStream(File file, Closure closure) throws IOException {
      return withStream((InputStream)newDataInputStream(file), closure);
   }

   public static BufferedWriter newWriter(File file) throws IOException {
      return new BufferedWriter(new FileWriter(file));
   }

   public static BufferedWriter newWriter(File file, boolean append) throws IOException {
      return new BufferedWriter(new FileWriter(file, append));
   }

   public static BufferedWriter newWriter(File file, String charset, boolean append) throws IOException {
      if (append) {
         return new EncodingAwareBufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charset));
      } else {
         FileOutputStream stream = new FileOutputStream(file);
         if ("UTF-16BE".equals(charset)) {
            writeUtf16Bom(stream, true);
         } else if ("UTF-16LE".equals(charset)) {
            writeUtf16Bom(stream, false);
         }

         return new EncodingAwareBufferedWriter(new OutputStreamWriter(stream, charset));
      }
   }

   public static BufferedWriter newWriter(File file, String charset) throws IOException {
      return newWriter(file, charset, false);
   }

   private static void writeUtf16Bom(FileOutputStream stream, boolean bigEndian) throws IOException {
      if (bigEndian) {
         stream.write(-2);
         stream.write(-1);
      } else {
         stream.write(-1);
         stream.write(-2);
      }

   }

   public static Object withWriter(File file, Closure closure) throws IOException {
      return withWriter((Writer)newWriter(file), closure);
   }

   public static Object withWriter(File file, String charset, Closure closure) throws IOException {
      return withWriter((Writer)newWriter(file, charset), closure);
   }

   public static Object withWriterAppend(File file, String charset, Closure closure) throws IOException {
      return withWriter((Writer)newWriter(file, charset, true), closure);
   }

   public static Object withWriterAppend(File file, Closure closure) throws IOException {
      return withWriter((Writer)newWriter(file, true), closure);
   }

   public static PrintWriter newPrintWriter(File file) throws IOException {
      return new GroovyPrintWriter(newWriter(file));
   }

   public static PrintWriter newPrintWriter(File file, String charset) throws IOException {
      return new GroovyPrintWriter(newWriter(file, charset));
   }

   public static PrintWriter newPrintWriter(Writer writer) {
      return new GroovyPrintWriter(writer);
   }

   public static Object withPrintWriter(File file, Closure closure) throws IOException {
      return withWriter((Writer)newPrintWriter(file), closure);
   }

   public static Object withPrintWriter(File file, String charset, Closure closure) throws IOException {
      return withWriter((Writer)newPrintWriter(file, charset), closure);
   }

   public static Object withPrintWriter(Writer writer, Closure closure) throws IOException {
      return withWriter((Writer)newPrintWriter(writer), closure);
   }

   public static Object withWriter(Writer writer, Closure closure) throws IOException {
      Object var4;
      try {
         Object result = closure.call((Object)writer);

         try {
            writer.flush();
         } catch (IOException var8) {
         }

         Writer temp = writer;
         writer = null;
         temp.close();
         var4 = result;
      } finally {
         closeWithWarning(writer);
      }

      return var4;
   }

   public static Object withReader(Reader reader, Closure closure) throws IOException {
      Object var4;
      try {
         Object result = closure.call((Object)reader);
         Reader temp = reader;
         reader = null;
         temp.close();
         var4 = result;
      } finally {
         closeWithWarning(reader);
      }

      return var4;
   }

   public static Object withStream(InputStream stream, Closure closure) throws IOException {
      Object var4;
      try {
         Object result = closure.call((Object)stream);
         InputStream temp = stream;
         stream = null;
         temp.close();
         var4 = result;
      } finally {
         closeWithWarning(stream);
      }

      return var4;
   }

   public static Object withReader(URL url, Closure closure) throws IOException {
      return withReader(url.openConnection().getInputStream(), closure);
   }

   public static Object withReader(URL url, String charset, Closure closure) throws IOException {
      return withReader(url.openConnection().getInputStream(), charset, closure);
   }

   public static Object withReader(InputStream in, Closure closure) throws IOException {
      return withReader((Reader)(new InputStreamReader(in)), closure);
   }

   public static Object withReader(InputStream in, String charset, Closure closure) throws IOException {
      return withReader((Reader)(new InputStreamReader(in, charset)), closure);
   }

   public static Object withWriter(OutputStream stream, Closure closure) throws IOException {
      return withWriter((Writer)(new OutputStreamWriter(stream)), closure);
   }

   public static Object withWriter(OutputStream stream, String charset, Closure closure) throws IOException {
      return withWriter((Writer)(new OutputStreamWriter(stream, charset)), closure);
   }

   public static Object withStream(OutputStream os, Closure closure) throws IOException {
      Object var4;
      try {
         Object result = closure.call((Object)os);
         os.flush();
         OutputStream temp = os;
         os = null;
         temp.close();
         var4 = result;
      } finally {
         closeWithWarning(os);
      }

      return var4;
   }

   public static BufferedInputStream newInputStream(File file) throws FileNotFoundException {
      return new BufferedInputStream(new FileInputStream(file));
   }

   public static BufferedInputStream newInputStream(URL url) throws MalformedURLException, IOException {
      return new BufferedInputStream(url.openConnection().getInputStream());
   }

   public static BufferedReader newReader(URL url) throws MalformedURLException, IOException {
      return newReader(url.openConnection().getInputStream());
   }

   public static BufferedReader newReader(URL url, String charset) throws MalformedURLException, IOException {
      return new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), charset));
   }

   public static DataInputStream newDataInputStream(File file) throws FileNotFoundException {
      return new DataInputStream(new FileInputStream(file));
   }

   public static void eachByte(File self, Closure closure) throws IOException {
      BufferedInputStream is = newInputStream(self);
      eachByte((InputStream)is, closure);
   }

   public static void eachByte(File self, int bufferLen, Closure closure) throws IOException {
      BufferedInputStream is = newInputStream(self);
      eachByte((InputStream)is, bufferLen, closure);
   }

   public static void eachByte(Byte[] self, Closure closure) {
      each((Object)self, closure);
   }

   public static void eachByte(byte[] self, Closure closure) {
      each((Object)self, closure);
   }

   public static void eachByte(InputStream is, Closure closure) throws IOException {
      try {
         while(true) {
            int b = is.read();
            if (b == -1) {
               InputStream temp = is;
               is = null;
               temp.close();
               return;
            }

            closure.call((Object)((byte)b));
         }
      } finally {
         closeWithWarning(is);
      }
   }

   public static void eachByte(InputStream is, int bufferLen, Closure closure) throws IOException {
      byte[] buffer = new byte[bufferLen];
      boolean var4 = false;

      try {
         int bytesRead;
         while((bytesRead = is.read(buffer, 0, bufferLen)) > 0) {
            closure.call(new Object[]{buffer, bytesRead});
         }

         InputStream temp = is;
         is = null;
         temp.close();
      } finally {
         closeWithWarning(is);
      }

   }

   public static void eachByte(URL url, Closure closure) throws IOException {
      InputStream is = url.openConnection().getInputStream();
      eachByte(is, closure);
   }

   public static void eachByte(URL url, int bufferLen, Closure closure) throws IOException {
      InputStream is = url.openConnection().getInputStream();
      eachByte(is, bufferLen, closure);
   }

   public static void transformChar(Reader self, Writer writer, Closure closure) throws IOException {
      try {
         char[] chars = new char[1];

         int c;
         while((c = self.read()) != -1) {
            chars[0] = (char)c;
            writer.write((String)closure.call((Object)(new String(chars))));
         }

         writer.flush();
         Writer temp2 = writer;
         writer = null;
         temp2.close();
         Reader temp1 = self;
         self = null;
         temp1.close();
      } finally {
         closeWithWarning(self);
         closeWithWarning(writer);
      }
   }

   public static void transformLine(Reader reader, Writer writer, Closure closure) throws IOException {
      BufferedReader br = new BufferedReader(reader);
      BufferedWriter bw = new BufferedWriter(writer);

      try {
         String line;
         while((line = br.readLine()) != null) {
            Object o = closure.call((Object)line);
            if (o != null) {
               bw.write(o.toString());
               bw.newLine();
            }
         }

         bw.flush();
         Writer temp2 = writer;
         writer = null;
         temp2.close();
         Reader temp1 = reader;
         reader = null;
         temp1.close();
      } finally {
         closeWithWarning(br);
         closeWithWarning(reader);
         closeWithWarning(bw);
         closeWithWarning(writer);
      }

   }

   public static void filterLine(Reader reader, Writer writer, Closure closure) throws IOException {
      BufferedReader br = new BufferedReader(reader);
      BufferedWriter bw = new BufferedWriter(writer);

      try {
         String line;
         while((line = br.readLine()) != null) {
            if (DefaultTypeTransformation.castToBoolean(closure.call((Object)line))) {
               bw.write(line);
               bw.newLine();
            }
         }

         bw.flush();
         Writer temp2 = writer;
         writer = null;
         temp2.close();
         Reader temp1 = reader;
         reader = null;
         temp1.close();
      } finally {
         closeWithWarning(br);
         closeWithWarning(reader);
         closeWithWarning(bw);
         closeWithWarning(writer);
      }

   }

   public static Writable filterLine(File self, Closure closure) throws IOException {
      return filterLine((Reader)newReader(self), closure);
   }

   public static Writable filterLine(File self, String charset, Closure closure) throws IOException {
      return filterLine((Reader)newReader(self, charset), closure);
   }

   public static void filterLine(File self, Writer writer, Closure closure) throws IOException {
      filterLine((Reader)newReader(self), (Writer)writer, closure);
   }

   public static void filterLine(File self, Writer writer, String charset, Closure closure) throws IOException {
      filterLine((Reader)newReader(self, charset), (Writer)writer, closure);
   }

   public static Writable filterLine(Reader reader, final Closure closure) {
      final BufferedReader br = new BufferedReader(reader);
      return new Writable() {
         public Writer writeTo(Writer out) throws IOException {
            BufferedWriter bw = new BufferedWriter(out);

            String line;
            while((line = br.readLine()) != null) {
               if (DefaultTypeTransformation.castToBoolean(closure.call((Object)line))) {
                  bw.write(line);
                  bw.newLine();
               }
            }

            bw.flush();
            return out;
         }

         public String toString() {
            StringWriter buffer = new StringWriter();

            try {
               this.writeTo(buffer);
            } catch (IOException var3) {
               throw new StringWriterIOException(var3);
            }

            return buffer.toString();
         }
      };
   }

   public static Writable filterLine(InputStream self, Closure predicate) {
      return filterLine((Reader)newReader(self), predicate);
   }

   public static Writable filterLine(InputStream self, String charset, Closure predicate) throws UnsupportedEncodingException {
      return filterLine((Reader)newReader(self, charset), predicate);
   }

   public static void filterLine(InputStream self, Writer writer, Closure predicate) throws IOException {
      filterLine((Reader)newReader(self), (Writer)writer, predicate);
   }

   public static void filterLine(InputStream self, Writer writer, String charset, Closure predicate) throws IOException {
      filterLine((Reader)newReader(self, charset), (Writer)writer, predicate);
   }

   public static Writable filterLine(URL self, Closure predicate) throws IOException {
      return filterLine((Reader)newReader(self), predicate);
   }

   public static Writable filterLine(URL self, String charset, Closure predicate) throws IOException {
      return filterLine((Reader)newReader(self, charset), predicate);
   }

   public static void filterLine(URL self, Writer writer, Closure predicate) throws IOException {
      filterLine((Reader)newReader(self), (Writer)writer, predicate);
   }

   public static void filterLine(URL self, Writer writer, String charset, Closure predicate) throws IOException {
      filterLine((Reader)newReader(self, charset), (Writer)writer, predicate);
   }

   public static byte[] readBytes(File file) throws IOException {
      byte[] bytes = new byte[(int)file.length()];
      FileInputStream fileInputStream = new FileInputStream(file);
      DataInputStream dis = new DataInputStream(fileInputStream);

      try {
         dis.readFully(bytes);
         InputStream temp = dis;
         dis = null;
         temp.close();
      } finally {
         closeWithWarning(dis);
      }

      return bytes;
   }

   public static Object withStreams(Socket socket, Closure closure) throws IOException {
      InputStream input = socket.getInputStream();
      OutputStream output = socket.getOutputStream();

      Object var7;
      try {
         Object result = closure.call(new Object[]{input, output});
         InputStream temp1 = input;
         input = null;
         temp1.close();
         OutputStream temp2 = output;
         output = null;
         temp2.close();
         var7 = result;
      } finally {
         closeWithWarning(input);
         closeWithWarning(output);
      }

      return var7;
   }

   public static Object withObjectStreams(Socket socket, Closure closure) throws IOException {
      InputStream input = socket.getInputStream();
      OutputStream output = socket.getOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(output);
      ObjectInputStream ois = new ObjectInputStream(input);

      Object var9;
      try {
         Object result = closure.call(new Object[]{ois, oos});
         InputStream temp1 = ois;
         ois = null;
         temp1.close();
         InputStream temp1 = input;
         input = null;
         temp1.close();
         OutputStream temp2 = oos;
         oos = null;
         temp2.close();
         OutputStream temp2 = output;
         output = null;
         temp2.close();
         var9 = result;
      } finally {
         closeWithWarning(ois);
         closeWithWarning(input);
         closeWithWarning(oos);
         closeWithWarning(output);
      }

      return var9;
   }

   public static Writer leftShift(Socket self, Object value) throws IOException {
      return leftShift(self.getOutputStream(), value);
   }

   public static OutputStream leftShift(Socket self, byte[] value) throws IOException {
      return leftShift(self.getOutputStream(), value);
   }

   public static Socket accept(ServerSocket serverSocket, Closure closure) throws IOException {
      return accept(serverSocket, true, closure);
   }

   public static Socket accept(ServerSocket serverSocket, boolean runInANewThread, final Closure closure) throws IOException {
      final Socket socket = serverSocket.accept();
      if (runInANewThread) {
         (new Thread(new Runnable() {
            public void run() {
               DefaultGroovyMethods.invokeClosureWithSocket(socket, closure);
            }
         })).start();
      } else {
         invokeClosureWithSocket(socket, closure);
      }

      return socket;
   }

   private static void invokeClosureWithSocket(Socket socket, Closure closure) {
      try {
         closure.call((Object)socket);
      } finally {
         if (socket != null) {
            try {
               socket.close();
            } catch (IOException var8) {
               LOG.warning("Caught exception closing socket: " + var8);
            }
         }

      }

   }

   public static File asWritable(File file) {
      return new WritableFile(file);
   }

   public static Object asType(File f, Class c) {
      return c == Writable.class ? asWritable(f) : asType((Object)f, c);
   }

   public static File asWritable(File file, String encoding) {
      return new WritableFile(file, encoding);
   }

   public static List<String> toList(String self) {
      int size = self.length();
      List<String> answer = new ArrayList(size);

      for(int i = 0; i < size; ++i) {
         answer.add(self.substring(i, i + 1));
      }

      return answer;
   }

   public static char[] getChars(String self) {
      return self.toCharArray();
   }

   public static Object asType(GString self, Class c) {
      if (c == File.class) {
         return new File(self.toString());
      } else {
         return Number.class.isAssignableFrom(c) ? asType(self.toString(), c) : asType((Object)self, c);
      }
   }

   public static Object asType(String self, Class c) {
      if (c == List.class) {
         return toList(self);
      } else if (c == BigDecimal.class) {
         return toBigDecimal(self);
      } else if (c == BigInteger.class) {
         return toBigInteger(self);
      } else if (c != Long.class && c != Long.TYPE) {
         if (c != Integer.class && c != Integer.TYPE) {
            if (c != Short.class && c != Short.TYPE) {
               if (c != Byte.class && c != Byte.TYPE) {
                  if (c != Character.class && c != Character.TYPE) {
                     if (c != Double.class && c != Double.TYPE) {
                        if (c != Float.class && c != Float.TYPE) {
                           if (c == File.class) {
                              return new File(self);
                           } else {
                              return DefaultTypeTransformation.isEnumSubclass(c) ? InvokerHelper.invokeMethod(c, "valueOf", new Object[]{self}) : asType((Object)self, c);
                           }
                        } else {
                           return toFloat(self);
                        }
                     } else {
                        return toDouble(self);
                     }
                  } else {
                     return toCharacter(self);
                  }
               } else {
                  return Byte.valueOf(self.trim());
               }
            } else {
               return toShort(self);
            }
         } else {
            return toInteger(self);
         }
      } else {
         return toLong(self);
      }
   }

   /** @deprecated */
   @Deprecated
   public static InputStream getIn(Process self) {
      return ProcessGroovyMethods.getIn(self);
   }

   /** @deprecated */
   @Deprecated
   public static String getText(Process self) throws IOException {
      return ProcessGroovyMethods.getText(self);
   }

   /** @deprecated */
   @Deprecated
   public static InputStream getErr(Process self) {
      return ProcessGroovyMethods.getErr(self);
   }

   /** @deprecated */
   @Deprecated
   public static OutputStream getOut(Process self) {
      return ProcessGroovyMethods.getOut(self);
   }

   /** @deprecated */
   @Deprecated
   public static Writer leftShift(Process self, Object value) throws IOException {
      return ProcessGroovyMethods.leftShift(self, value);
   }

   /** @deprecated */
   @Deprecated
   public static OutputStream leftShift(Process self, byte[] value) throws IOException {
      return ProcessGroovyMethods.leftShift(self, value);
   }

   /** @deprecated */
   @Deprecated
   public static void waitForOrKill(Process self, long numberOfMillis) {
      ProcessGroovyMethods.waitForOrKill(self, numberOfMillis);
   }

   /** @deprecated */
   @Deprecated
   public static void consumeProcessOutput(Process self) {
      ProcessGroovyMethods.consumeProcessOutput(self);
   }

   /** @deprecated */
   @Deprecated
   public static void consumeProcessOutput(Process self, StringBuffer output, StringBuffer error) {
      ProcessGroovyMethods.consumeProcessOutput(self, (Appendable)output, (Appendable)error);
   }

   /** @deprecated */
   @Deprecated
   public static void consumeProcessOutput(Process self, OutputStream output, OutputStream error) {
      ProcessGroovyMethods.consumeProcessOutput(self, output, error);
   }

   /** @deprecated */
   @Deprecated
   public static void waitForProcessOutput(Process self) {
      ProcessGroovyMethods.waitForProcessOutput(self);
   }

   /** @deprecated */
   @Deprecated
   public static void waitForProcessOutput(Process self, StringBuffer output, StringBuffer error) {
      ProcessGroovyMethods.waitForProcessOutput(self, (Appendable)output, (Appendable)error);
   }

   /** @deprecated */
   @Deprecated
   public static void waitForProcessOutput(Process self, OutputStream output, OutputStream error) {
      ProcessGroovyMethods.waitForProcessOutput(self, output, error);
   }

   /** @deprecated */
   @Deprecated
   public static Thread consumeProcessErrorStream(Process self, OutputStream err) {
      return ProcessGroovyMethods.consumeProcessErrorStream(self, err);
   }

   /** @deprecated */
   @Deprecated
   public static Thread consumeProcessErrorStream(Process self, StringBuffer error) {
      return ProcessGroovyMethods.consumeProcessErrorStream(self, (Appendable)error);
   }

   /** @deprecated */
   @Deprecated
   public static Thread consumeProcessErrorStream(Process self, Writer err) {
      return ProcessGroovyMethods.consumeProcessErrorStream(self, (Appendable)err);
   }

   /** @deprecated */
   @Deprecated
   public static Thread consumeProcessOutputStream(Process self, StringBuffer output) {
      return ProcessGroovyMethods.consumeProcessOutputStream(self, (Appendable)output);
   }

   /** @deprecated */
   @Deprecated
   public static Thread consumeProcessOutputStream(Process self, Writer output) {
      return ProcessGroovyMethods.consumeProcessOutputStream(self, (Appendable)output);
   }

   /** @deprecated */
   @Deprecated
   public static Thread consumeProcessOutputStream(Process self, OutputStream output) {
      return ProcessGroovyMethods.consumeProcessOutputStream(self, output);
   }

   /** @deprecated */
   @Deprecated
   public static void withWriter(Process self, Closure closure) {
      ProcessGroovyMethods.withWriter(self, closure);
   }

   /** @deprecated */
   @Deprecated
   public static void withOutputStream(Process self, Closure closure) {
      ProcessGroovyMethods.withOutputStream(self, closure);
   }

   /** @deprecated */
   @Deprecated
   public static Process pipeTo(Process left, Process right) throws IOException {
      return ProcessGroovyMethods.pipeTo(left, right);
   }

   /** @deprecated */
   @Deprecated
   public static Process or(Process left, Process right) throws IOException {
      return ProcessGroovyMethods.or(left, right);
   }

   public static String eachMatch(String self, String regex, Closure closure) {
      return eachMatch(self, Pattern.compile(regex), closure);
   }

   public static String eachMatch(String self, Pattern pattern, Closure closure) {
      Matcher m = pattern.matcher(self);
      each((Object)m, closure);
      return self;
   }

   public static int findIndexOf(Object self, Closure closure) {
      return findIndexOf(self, 0, closure);
   }

   public static int findIndexOf(Object self, int startIndex, Closure closure) {
      int result = -1;
      int i = 0;

      for(Iterator iter = InvokerHelper.asIterator(self); iter.hasNext(); ++i) {
         Object value = iter.next();
         if (i >= startIndex && DefaultTypeTransformation.castToBoolean(closure.call(value))) {
            result = i;
            break;
         }
      }

      return result;
   }

   public static int findLastIndexOf(Object self, Closure closure) {
      return findLastIndexOf(self, 0, closure);
   }

   public static int findLastIndexOf(Object self, int startIndex, Closure closure) {
      int result = -1;
      int i = 0;

      for(Iterator iter = InvokerHelper.asIterator(self); iter.hasNext(); ++i) {
         Object value = iter.next();
         if (i >= startIndex && DefaultTypeTransformation.castToBoolean(closure.call(value))) {
            result = i;
         }
      }

      return result;
   }

   public static List<Number> findIndexValues(Object self, Closure closure) {
      return findIndexValues(self, 0, closure);
   }

   public static List<Number> findIndexValues(Object self, Number startIndex, Closure closure) {
      List<Number> result = new ArrayList();
      long count = 0L;
      long startCount = startIndex.longValue();

      for(Iterator iter = InvokerHelper.asIterator(self); iter.hasNext(); ++count) {
         Object value = iter.next();
         if (count >= startCount && DefaultTypeTransformation.castToBoolean(closure.call(value))) {
            result.add(count);
         }
      }

      return result;
   }

   public static ClassLoader getRootLoader(ClassLoader self) {
      while(self != null) {
         if (isRootLoaderClassOrSubClass(self)) {
            return self;
         }

         self = self.getParent();
      }

      return null;
   }

   private static boolean isRootLoaderClassOrSubClass(ClassLoader self) {
      for(Class current = self.getClass(); !current.getName().equals(Object.class.getName()); current = current.getSuperclass()) {
         if (current.getName().equals(RootLoader.class.getName())) {
            return true;
         }
      }

      return false;
   }

   public static Object asType(Object obj, Class type) {
      if (String.class == type) {
         return InvokerHelper.toString(obj);
      } else {
         try {
            if (ReflectionCache.isArray(type)) {
               return asArrayType(obj, type);
            }
         } catch (GroovyCastException var6) {
         }

         try {
            return DefaultTypeTransformation.castToType(obj, type);
         } catch (GroovyCastException var8) {
            MetaClass mc = InvokerHelper.getMetaClass(obj);
            if (mc instanceof ExpandoMetaClass) {
               ExpandoMetaClass emc = (ExpandoMetaClass)mc;
               Object mixedIn = emc.castToMixedType(obj, type);
               if (mixedIn != null) {
                  return mixedIn;
               }
            }

            if (type.isInterface()) {
               try {
                  List<Class> interfaces = new ArrayList();
                  interfaces.add(type);
                  return ProxyGenerator.INSTANCE.instantiateDelegate(interfaces, obj);
               } catch (GroovyRuntimeException var7) {
               }
            }

            throw var8;
         }
      }
   }

   private static Object asArrayType(Object object, Class type) {
      if (type.isAssignableFrom(object.getClass())) {
         return object;
      } else {
         Collection list = DefaultTypeTransformation.asCollection(object);
         int size = list.size();
         Class elementType = type.getComponentType();
         Object array = Array.newInstance(elementType, size);
         int idx = 0;
         Iterator iter;
         Object element;
         if (Boolean.TYPE.equals(elementType)) {
            for(iter = list.iterator(); iter.hasNext(); ++idx) {
               element = iter.next();
               Array.setBoolean(array, idx, (Boolean)InvokerHelper.invokeStaticMethod((Class)DefaultGroovyMethods.class, "asType", new Object[]{element, Boolean.TYPE}));
            }
         } else if (Byte.TYPE.equals(elementType)) {
            for(iter = list.iterator(); iter.hasNext(); ++idx) {
               element = iter.next();
               Array.setByte(array, idx, (Byte)InvokerHelper.invokeStaticMethod((Class)DefaultGroovyMethods.class, "asType", new Object[]{element, Byte.TYPE}));
            }
         } else if (Character.TYPE.equals(elementType)) {
            for(iter = list.iterator(); iter.hasNext(); ++idx) {
               element = iter.next();
               Array.setChar(array, idx, (Character)InvokerHelper.invokeStaticMethod((Class)DefaultGroovyMethods.class, "asType", new Object[]{element, Character.TYPE}));
            }
         } else if (Double.TYPE.equals(elementType)) {
            for(iter = list.iterator(); iter.hasNext(); ++idx) {
               element = iter.next();
               Array.setDouble(array, idx, (Double)InvokerHelper.invokeStaticMethod((Class)DefaultGroovyMethods.class, "asType", new Object[]{element, Double.TYPE}));
            }
         } else if (Float.TYPE.equals(elementType)) {
            for(iter = list.iterator(); iter.hasNext(); ++idx) {
               element = iter.next();
               Array.setFloat(array, idx, (Float)InvokerHelper.invokeStaticMethod((Class)DefaultGroovyMethods.class, "asType", new Object[]{element, Float.TYPE}));
            }
         } else if (Integer.TYPE.equals(elementType)) {
            for(iter = list.iterator(); iter.hasNext(); ++idx) {
               element = iter.next();
               Array.setInt(array, idx, (Integer)InvokerHelper.invokeStaticMethod((Class)DefaultGroovyMethods.class, "asType", new Object[]{element, Integer.TYPE}));
            }
         } else if (Long.TYPE.equals(elementType)) {
            for(iter = list.iterator(); iter.hasNext(); ++idx) {
               element = iter.next();
               Array.setLong(array, idx, (Long)InvokerHelper.invokeStaticMethod((Class)DefaultGroovyMethods.class, "asType", new Object[]{element, Long.TYPE}));
            }
         } else if (Short.TYPE.equals(elementType)) {
            for(iter = list.iterator(); iter.hasNext(); ++idx) {
               element = iter.next();
               Array.setShort(array, idx, (Short)InvokerHelper.invokeStaticMethod((Class)DefaultGroovyMethods.class, "asType", new Object[]{element, Short.TYPE}));
            }
         } else {
            for(iter = list.iterator(); iter.hasNext(); ++idx) {
               element = iter.next();
               Array.set(array, idx, InvokerHelper.invokeStaticMethod((Class)DefaultGroovyMethods.class, "asType", new Object[]{element, elementType}));
            }
         }

         return array;
      }
   }

   public static <T> T newInstance(Class<T> c) {
      return InvokerHelper.invokeConstructorOf((Class)c, (Object)null);
   }

   public static <T> T newInstance(Class<T> c, Object[] args) {
      if (args == null) {
         args = new Object[]{null};
      }

      return InvokerHelper.invokeConstructorOf((Class)c, args);
   }

   public static MetaClass getMetaClass(Class c) {
      MetaClassRegistry metaClassRegistry = GroovySystem.getMetaClassRegistry();
      MetaClass mc = metaClassRegistry.getMetaClass(c);
      return (MetaClass)(!(mc instanceof ExpandoMetaClass) && (!(mc instanceof DelegatingMetaClass) || !(((DelegatingMetaClass)mc).getAdaptee() instanceof ExpandoMetaClass)) ? new HandleMetaClass(mc) : mc);
   }

   public static MetaClass getMetaClass(Object obj) {
      MetaClass mc = InvokerHelper.getMetaClass(obj);
      return new HandleMetaClass(mc, obj);
   }

   public static MetaClass getMetaClass(GroovyObject obj) {
      return getMetaClass((Object)obj);
   }

   public static void setMetaClass(Class self, MetaClass metaClass) {
      MetaClassRegistry metaClassRegistry = GroovySystem.getMetaClassRegistry();
      if (metaClass == null) {
         metaClassRegistry.removeMetaClass(self);
      } else {
         if (metaClass instanceof HandleMetaClass) {
            metaClassRegistry.setMetaClass(self, ((HandleMetaClass)metaClass).getAdaptee());
         } else {
            metaClassRegistry.setMetaClass(self, metaClass);
         }

         if (self == NullObject.class) {
            NullObject.getNullObject().setMetaClass(metaClass);
         }
      }

   }

   public static void setMetaClass(Object self, MetaClass metaClass) {
      if (metaClass instanceof HandleMetaClass) {
         metaClass = ((HandleMetaClass)metaClass).getAdaptee();
      }

      if (self instanceof GroovyObject) {
         ((GroovyObject)self).setMetaClass(metaClass);
      } else if (self instanceof Class) {
         ((MetaClassRegistryImpl)GroovySystem.getMetaClassRegistry()).setMetaClass((Class)self, metaClass);
      } else {
         ((MetaClassRegistryImpl)GroovySystem.getMetaClassRegistry()).setMetaClass(self, metaClass);
      }

   }

   public static MetaClass metaClass(Class self, Closure closure) {
      MetaClassRegistry metaClassRegistry = GroovySystem.getMetaClassRegistry();
      MetaClass mc = metaClassRegistry.getMetaClass(self);
      if (mc instanceof ExpandoMetaClass) {
         ((ExpandoMetaClass)mc).define(closure);
         return mc;
      } else if (mc instanceof DelegatingMetaClass && ((DelegatingMetaClass)mc).getAdaptee() instanceof ExpandoMetaClass) {
         ((ExpandoMetaClass)((DelegatingMetaClass)mc).getAdaptee()).define(closure);
         return mc;
      } else if (mc instanceof DelegatingMetaClass && ((DelegatingMetaClass)mc).getAdaptee().getClass() == MetaClassImpl.class) {
         ExpandoMetaClass emc = new ExpandoMetaClass(self, false, true);
         emc.initialize();
         emc.define(closure);
         ((DelegatingMetaClass)mc).setAdaptee(emc);
         return mc;
      } else if (mc.getClass() == MetaClassImpl.class) {
         MetaClass mc = new ExpandoMetaClass(self, false, true);
         mc.initialize();
         ((ExpandoMetaClass)mc).define(closure);
         metaClassRegistry.setMetaClass(self, mc);
         return mc;
      } else {
         throw new GroovyRuntimeException("Can't add methods to custom meta class " + mc);
      }
   }

   public static MetaClass metaClass(Object self, Closure closure) {
      MetaClass emc = hasPerInstanceMetaClass(self);
      if (emc == null) {
         ExpandoMetaClass metaClass = new ExpandoMetaClass(self.getClass(), false, true);
         metaClass.initialize();
         metaClass.define(closure);
         setMetaClass((Object)self, metaClass);
         return metaClass;
      } else if (emc instanceof ExpandoMetaClass) {
         ((ExpandoMetaClass)emc).define(closure);
         return emc;
      } else if (emc instanceof DelegatingMetaClass && ((DelegatingMetaClass)emc).getAdaptee() instanceof ExpandoMetaClass) {
         ((ExpandoMetaClass)((DelegatingMetaClass)emc).getAdaptee()).define(closure);
         return emc;
      } else {
         throw new RuntimeException("Can't add methods to non-ExpandoMetaClass " + emc);
      }
   }

   private static MetaClass hasPerInstanceMetaClass(Object object) {
      if (object instanceof GroovyObject) {
         MetaClass mc = ((GroovyObject)object).getMetaClass();
         return mc != GroovySystem.getMetaClassRegistry().getMetaClass(object.getClass()) && mc.getClass() != MetaClassImpl.class ? mc : null;
      } else {
         ClassInfo info = ClassInfo.getClassInfo(object.getClass());
         info.lock();

         MetaClass var2;
         try {
            var2 = info.getPerInstanceMetaClass(object);
         } finally {
            info.unlock();
         }

         return var2;
      }
   }

   public static <T> Iterator<T> iterator(T[] a) {
      return DefaultTypeTransformation.asCollection(a).iterator();
   }

   public static Iterator iterator(Object o) {
      return DefaultTypeTransformation.asCollection(o).iterator();
   }

   public static <T> Iterator<T> iterator(final Enumeration<T> enumeration) {
      return new Iterator<T>() {
         private T last;

         public boolean hasNext() {
            return enumeration.hasMoreElements();
         }

         public T next() {
            this.last = enumeration.nextElement();
            return this.last;
         }

         public void remove() {
            throw new UnsupportedOperationException("Cannot remove() from an Enumeration");
         }
      };
   }

   public static Iterator iterator(final Matcher matcher) {
      matcher.reset();
      return new Iterator() {
         private boolean found;
         private boolean done;

         public boolean hasNext() {
            if (this.done) {
               return false;
            } else {
               if (!this.found) {
                  this.found = matcher.find();
                  if (!this.found) {
                     this.done = true;
                  }
               }

               return this.found;
            }
         }

         public Object next() {
            if (!this.found && !this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               this.found = false;
               if (!DefaultGroovyMethods.hasGroup(matcher)) {
                  return matcher.group();
               } else {
                  List list = new ArrayList(matcher.groupCount());

                  for(int i = 0; i <= matcher.groupCount(); ++i) {
                     list.add(matcher.group(i));
                  }

                  return list;
               }
            }
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public static Iterator<String> iterator(Reader self) {
      final BufferedReader bufferedReader;
      if (self instanceof BufferedReader) {
         bufferedReader = (BufferedReader)self;
      } else {
         bufferedReader = new BufferedReader(self);
      }

      return new Iterator<String>() {
         String nextVal;
         boolean nextMustRead = true;
         boolean hasNext = true;

         public boolean hasNext() {
            if (this.nextMustRead && this.hasNext) {
               try {
                  this.nextVal = this.readNext();
                  this.nextMustRead = false;
               } catch (IOException var2) {
                  this.hasNext = false;
               }
            }

            return this.hasNext;
         }

         public String next() {
            String retval = null;
            if (this.nextMustRead) {
               try {
                  retval = this.readNext();
               } catch (IOException var3) {
                  this.hasNext = false;
               }
            } else {
               retval = this.nextVal;
            }

            this.nextMustRead = true;
            return retval;
         }

         private String readNext() throws IOException {
            String nv = bufferedReader.readLine();
            if (nv == null) {
               this.hasNext = false;
            }

            return nv;
         }

         public void remove() {
            throw new UnsupportedOperationException("Cannot remove() from a Reader Iterator");
         }
      };
   }

   public static Iterator<Byte> iterator(InputStream self) {
      return iterator(new DataInputStream(self));
   }

   public static Iterator<Byte> iterator(final DataInputStream self) {
      return new Iterator<Byte>() {
         Byte nextVal;
         boolean nextMustRead = true;
         boolean hasNext = true;

         public boolean hasNext() {
            if (this.nextMustRead && this.hasNext) {
               try {
                  this.nextVal = self.readByte();
                  this.nextMustRead = false;
               } catch (IOException var2) {
                  this.hasNext = false;
               }
            }

            return this.hasNext;
         }

         public Byte next() {
            Byte retval = null;
            if (this.nextMustRead) {
               try {
                  retval = self.readByte();
               } catch (IOException var3) {
                  this.hasNext = false;
               }
            } else {
               retval = this.nextVal;
            }

            this.nextMustRead = true;
            return retval;
         }

         public void remove() {
            throw new UnsupportedOperationException("Cannot remove() from a DataInputStream Iterator");
         }
      };
   }

   /** @deprecated */
   public static Iterator iterator(File self) throws IOException {
      throw new DeprecationException("Iterators on files are not supported any more. Use File.eachLine() instead. Alternatively you can use FileReader.iterator() and provide your own exception handling.");
   }

   public static <T> Iterator<T> iterator(Iterator<T> self) {
      return self;
   }

   public static List respondsTo(Object self, String name, Object[] argTypes) {
      return InvokerHelper.getMetaClass(self).respondsTo(self, name, argTypes);
   }

   public static List respondsTo(Object self, String name) {
      return InvokerHelper.getMetaClass(self).respondsTo(self, name);
   }

   public static MetaProperty hasProperty(Object self, String name) {
      return InvokerHelper.getMetaClass(self).hasProperty(self, name);
   }

   /** @deprecated */
   @Deprecated
   public static Iterator<Node> iterator(NodeList nodeList) {
      return XmlGroovyMethods.iterator(nodeList);
   }

   /** @deprecated */
   @Deprecated
   public static GroovyRowResult toRowResult(ResultSet rs) throws SQLException {
      return SqlGroovyMethods.toRowResult(rs);
   }

   /** @deprecated */
   @Deprecated
   public static Writable encodeBase64(Byte[] data, boolean chunked) {
      return EncodingGroovyMethods.encodeBase64(data, chunked);
   }

   /** @deprecated */
   @Deprecated
   public static Writable encodeBase64(Byte[] data) {
      return EncodingGroovyMethods.encodeBase64(data);
   }

   /** @deprecated */
   @Deprecated
   public static Writable encodeBase64(byte[] data, boolean chunked) {
      return EncodingGroovyMethods.encodeBase64(data, chunked);
   }

   /** @deprecated */
   @Deprecated
   public static Writable encodeBase64(byte[] data) {
      return EncodingGroovyMethods.encodeBase64(data);
   }

   /** @deprecated */
   @Deprecated
   public static byte[] decodeBase64(String value) {
      return EncodingGroovyMethods.decodeBase64(value);
   }

   /** @deprecated */
   @Deprecated
   public static Date next(Date self) {
      return DateGroovyMethods.next(self);
   }

   /** @deprecated */
   @Deprecated
   public static java.sql.Date next(java.sql.Date self) {
      return DateGroovyMethods.next(self);
   }

   /** @deprecated */
   @Deprecated
   public static Date previous(Date self) {
      return DateGroovyMethods.previous(self);
   }

   /** @deprecated */
   @Deprecated
   public static java.sql.Date previous(java.sql.Date self) {
      return DateGroovyMethods.previous(self);
   }

   /** @deprecated */
   @Deprecated
   public static Date plus(Date self, int days) {
      return DateGroovyMethods.plus(self, days);
   }

   /** @deprecated */
   @Deprecated
   public static java.sql.Date plus(java.sql.Date self, int days) {
      return DateGroovyMethods.plus(self, days);
   }

   /** @deprecated */
   @Deprecated
   public static Date minus(Date self, int days) {
      return DateGroovyMethods.minus(self, days);
   }

   /** @deprecated */
   @Deprecated
   public static java.sql.Date minus(java.sql.Date self, int days) {
      return DateGroovyMethods.minus(self, days);
   }

   /** @deprecated */
   @Deprecated
   public static int minus(Calendar self, Calendar then) {
      return DateGroovyMethods.minus(self, then);
   }

   /** @deprecated */
   @Deprecated
   public static int minus(Date self, Date then) {
      return DateGroovyMethods.minus(self, then);
   }

   /** @deprecated */
   @Deprecated
   public static String format(Date self, String format) {
      return DateGroovyMethods.format(self, format);
   }

   /** @deprecated */
   @Deprecated
   public static String getDateString(Date self) {
      return DateGroovyMethods.getDateString(self);
   }

   /** @deprecated */
   @Deprecated
   public static String getTimeString(Date self) {
      return DateGroovyMethods.getTimeString(self);
   }

   /** @deprecated */
   @Deprecated
   public static String getDateTimeString(Date self) {
      return DateGroovyMethods.getDateTimeString(self);
   }

   /** @deprecated */
   @Deprecated
   public static void clearTime(Date self) {
      DateGroovyMethods.clearTime(self);
   }

   /** @deprecated */
   @Deprecated
   public static void clearTime(java.sql.Date self) {
      DateGroovyMethods.clearTime(self);
   }

   /** @deprecated */
   @Deprecated
   public static void clearTime(Calendar self) {
      DateGroovyMethods.clearTime(self);
   }

   /** @deprecated */
   @Deprecated
   public static String format(Calendar self, String pattern) {
      return DateGroovyMethods.format(self, pattern);
   }

   /** @deprecated */
   @Deprecated
   public static int getAt(Date self, int field) {
      return DateGroovyMethods.getAt(self, field);
   }
}
