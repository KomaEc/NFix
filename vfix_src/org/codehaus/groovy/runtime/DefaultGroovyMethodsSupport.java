package org.codehaus.groovy.runtime;

import groovy.lang.EmptyRange;
import groovy.lang.Range;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Logger;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class DefaultGroovyMethodsSupport {
   private static final Logger LOG = Logger.getLogger(DefaultGroovyMethodsSupport.class.getName());

   protected static DefaultGroovyMethodsSupport.RangeInfo subListBorders(int size, Range range) {
      int from = normaliseIndex(DefaultTypeTransformation.intUnbox(range.getFrom()), size);
      int to = normaliseIndex(DefaultTypeTransformation.intUnbox(range.getTo()), size);
      boolean reverse = range.isReverse();
      if (from > to) {
         int tmp = to;
         to = from;
         from = tmp;
         reverse = !reverse;
      }

      return new DefaultGroovyMethodsSupport.RangeInfo(from, to + 1, reverse);
   }

   protected static DefaultGroovyMethodsSupport.RangeInfo subListBorders(int size, EmptyRange range) {
      int from = normaliseIndex(DefaultTypeTransformation.intUnbox(range.getFrom()), size);
      return new DefaultGroovyMethodsSupport.RangeInfo(from, from, false);
   }

   protected static int normaliseIndex(int i, int size) {
      int temp = i;
      if (i < 0) {
         i += size;
      }

      if (i < 0) {
         throw new ArrayIndexOutOfBoundsException("Negative array index [" + temp + "] too large for array size " + size);
      } else {
         return i;
      }
   }

   public static void closeWithWarning(Closeable c) {
      if (c != null) {
         try {
            c.close();
         } catch (IOException var2) {
            LOG.warning("Caught exception during close(): " + var2);
         }
      }

   }

   public static void closeQuietly(Closeable c) {
      if (c != null) {
         try {
            c.close();
         } catch (IOException var2) {
         }
      }

   }

   protected static <T> Collection<T> cloneSimilarCollection(Collection<T> orig, int newCapacity) {
      Collection<T> answer = (Collection)cloneObject(orig);
      if (answer != null) {
         return answer;
      } else {
         answer = createSimilarCollection(orig, newCapacity);
         answer.addAll(orig);
         return answer;
      }
   }

   private static Object cloneObject(Object orig) {
      if (orig instanceof Cloneable) {
         try {
            return InvokerHelper.invokeMethod(orig, "clone", new Object[0]);
         } catch (Exception var2) {
         }
      }

      return null;
   }

   protected static Collection createSimilarOrDefaultCollection(Object object) {
      return (Collection)(object instanceof Collection ? createSimilarCollection((Collection)object) : new ArrayList());
   }

   protected static <T> Collection<T> createSimilarCollection(Collection<T> collection) {
      return createSimilarCollection(collection, collection.size());
   }

   protected static <T> Collection<T> createSimilarCollection(Collection<T> orig, int newCapacity) {
      if (orig instanceof Set) {
         return createSimilarSet((Set)orig);
      } else if (orig instanceof List) {
         return createSimilarList((List)orig, newCapacity);
      } else {
         return (Collection)(orig instanceof Queue ? new LinkedList() : new ArrayList(newCapacity));
      }
   }

   protected static <T> List<T> createSimilarList(List<T> orig, int newCapacity) {
      if (orig instanceof LinkedList) {
         return new LinkedList();
      } else if (orig instanceof Stack) {
         return new Stack();
      } else {
         return (List)(orig instanceof Vector ? new Vector() : new ArrayList(newCapacity));
      }
   }

   protected static <T> Set<T> createSimilarSet(Set<T> orig) {
      if (orig instanceof SortedSet) {
         return new TreeSet(((SortedSet)orig).comparator());
      } else {
         return (Set)(orig instanceof LinkedHashSet ? new LinkedHashSet() : new HashSet());
      }
   }

   protected static <K, V> Map<K, V> createSimilarMap(Map<K, V> orig) {
      if (orig instanceof SortedMap) {
         return new TreeMap(((SortedMap)orig).comparator());
      } else if (orig instanceof Properties) {
         return new Properties();
      } else {
         return (Map)(orig instanceof Hashtable ? new Hashtable() : new LinkedHashMap());
      }
   }

   protected static <K, V> Map<K, V> cloneSimilarMap(Map<K, V> orig) {
      Map<K, V> answer = (Map)cloneObject(orig);
      if (answer != null) {
         return answer;
      } else if (orig instanceof TreeMap) {
         return new TreeMap(orig);
      } else if (orig instanceof Properties) {
         Map<K, V> map = new Properties();
         map.putAll(orig);
         return map;
      } else {
         return (Map)(orig instanceof Hashtable ? new Hashtable(orig) : new LinkedHashMap(orig));
      }
   }

   protected static boolean sameType(Collection[] cols) {
      List all = new LinkedList();
      Collection[] arr$ = cols;
      int len$ = cols.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Collection col = arr$[i$];
         all.addAll(col);
      }

      if (all.size() == 0) {
         return true;
      } else {
         Object first = all.get(0);
         Class baseClass;
         if (first instanceof Number) {
            baseClass = Number.class;
         } else if (first == null) {
            baseClass = NullObject.class;
         } else {
            baseClass = first.getClass();
         }

         Collection[] arr$ = cols;
         int len$ = cols.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Collection col = arr$[i$];
            Iterator i$ = col.iterator();

            while(i$.hasNext()) {
               Object o = i$.next();
               if (!baseClass.isInstance(o)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   protected static class RangeInfo {
      public final int from;
      public final int to;
      public final boolean reverse;

      public RangeInfo(int from, int to, boolean reverse) {
         this.from = from;
         this.to = to;
         this.reverse = reverse;
      }
   }
}
