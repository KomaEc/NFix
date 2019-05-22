package groovy.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class GroovyCollections {
   public static List combinations(Object[] collections) {
      return combinations((Collection)Arrays.asList(collections));
   }

   public static <T> Set<List<T>> subsequences(List<T> items) {
      Set<List<T>> ans = new HashSet();

      HashSet next;
      for(Iterator i$ = items.iterator(); i$.hasNext(); ans = next) {
         T h = i$.next();
         next = new HashSet();
         Iterator i$ = ans.iterator();

         while(i$.hasNext()) {
            List<T> it = (List)i$.next();
            List<T> sublist = new ArrayList(it);
            sublist.add(h);
            next.add(sublist);
         }

         next.addAll(ans);
         List<T> hlist = new ArrayList();
         hlist.add(h);
         next.add(hlist);
      }

      return ans;
   }

   public static List combinations(Collection collections) {
      List collectedCombos = new ArrayList();
      Iterator outer = collections.iterator();

      while(true) {
         while(outer.hasNext()) {
            Collection items = DefaultTypeTransformation.asCollection(outer.next());
            ArrayList newCombos;
            if (collectedCombos.isEmpty()) {
               Iterator iterator = items.iterator();

               while(iterator.hasNext()) {
                  newCombos = new ArrayList();
                  newCombos.add(iterator.next());
                  collectedCombos.add(newCombos);
               }
            } else {
               List savedCombos = new ArrayList(collectedCombos);
               newCombos = new ArrayList();
               Iterator inner = items.iterator();

               while(inner.hasNext()) {
                  Object value = inner.next();
                  Iterator combos = savedCombos.iterator();

                  while(combos.hasNext()) {
                     List oldlist = new ArrayList((List)combos.next());
                     oldlist.add(value);
                     newCombos.add(oldlist);
                  }
               }

               collectedCombos = newCombos;
            }
         }

         return collectedCombos;
      }
   }

   public static List transpose(Object[] lists) {
      return transpose(Arrays.asList(lists));
   }

   public static List transpose(List lists) {
      List result = new ArrayList();
      if (!lists.isEmpty() && lists.size() != 0) {
         int minSize = Integer.MAX_VALUE;
         Iterator outer = lists.iterator();

         List list;
         while(outer.hasNext()) {
            list = (List)DefaultTypeTransformation.castToType(outer.next(), List.class);
            if (list.size() < minSize) {
               minSize = list.size();
            }
         }

         if (minSize == 0) {
            return result;
         } else {
            for(int i = 0; i < minSize; ++i) {
               result.add(new ArrayList());
            }

            outer = lists.iterator();

            while(outer.hasNext()) {
               list = (List)DefaultTypeTransformation.castToType(outer.next(), List.class);

               for(int i = 0; i < minSize; ++i) {
                  List resultList = (List)result.get(i);
                  resultList.add(list.get(i));
               }
            }

            return result;
         }
      } else {
         return result;
      }
   }

   public static <T> T min(T[] items) {
      return min((Collection)Arrays.asList(items));
   }

   public static <T> T min(Collection<T> items) {
      T answer = null;
      Iterator i$ = items.iterator();

      while(true) {
         Object value;
         do {
            do {
               if (!i$.hasNext()) {
                  return answer;
               }

               value = i$.next();
            } while(value == null);
         } while(answer != null && !ScriptBytecodeAdapter.compareLessThan(value, answer));

         answer = value;
      }
   }

   public static <T> T max(T[] items) {
      return max((Collection)Arrays.asList(items));
   }

   public static <T> T max(Collection<T> items) {
      T answer = null;
      Iterator i$ = items.iterator();

      while(true) {
         Object value;
         do {
            do {
               if (!i$.hasNext()) {
                  return answer;
               }

               value = i$.next();
            } while(value == null);
         } while(answer != null && !ScriptBytecodeAdapter.compareGreaterThan(value, answer));

         answer = value;
      }
   }

   public static Object sum(Object[] items) {
      return DefaultGroovyMethods.sum((Collection)Arrays.asList(items));
   }

   public static Object sum(Collection items) {
      return DefaultGroovyMethods.sum(items);
   }
}
