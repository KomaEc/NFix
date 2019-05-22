package org.apache.commons.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.collections.functors.ChainedClosure;
import org.apache.commons.collections.functors.EqualPredicate;
import org.apache.commons.collections.functors.ExceptionClosure;
import org.apache.commons.collections.functors.ForClosure;
import org.apache.commons.collections.functors.IfClosure;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.functors.NOPClosure;
import org.apache.commons.collections.functors.SwitchClosure;
import org.apache.commons.collections.functors.TransformerClosure;
import org.apache.commons.collections.functors.WhileClosure;

public class ClosureUtils {
   public static Closure exceptionClosure() {
      return ExceptionClosure.INSTANCE;
   }

   public static Closure nopClosure() {
      return NOPClosure.INSTANCE;
   }

   public static Closure asClosure(Transformer transformer) {
      return TransformerClosure.getInstance(transformer);
   }

   public static Closure forClosure(int count, Closure closure) {
      return ForClosure.getInstance(count, closure);
   }

   public static Closure whileClosure(Predicate predicate, Closure closure) {
      return WhileClosure.getInstance(predicate, closure, false);
   }

   public static Closure doWhileClosure(Closure closure, Predicate predicate) {
      return WhileClosure.getInstance(predicate, closure, true);
   }

   public static Closure invokerClosure(String methodName) {
      return asClosure(InvokerTransformer.getInstance(methodName));
   }

   public static Closure invokerClosure(String methodName, Class[] paramTypes, Object[] args) {
      return asClosure(InvokerTransformer.getInstance(methodName, paramTypes, args));
   }

   public static Closure chainedClosure(Closure closure1, Closure closure2) {
      return ChainedClosure.getInstance(closure1, closure2);
   }

   public static Closure chainedClosure(Closure[] closures) {
      return ChainedClosure.getInstance(closures);
   }

   public static Closure chainedClosure(Collection closures) {
      return ChainedClosure.getInstance(closures);
   }

   public static Closure ifClosure(Predicate predicate, Closure trueClosure) {
      return IfClosure.getInstance(predicate, trueClosure);
   }

   public static Closure ifClosure(Predicate predicate, Closure trueClosure, Closure falseClosure) {
      return IfClosure.getInstance(predicate, trueClosure, falseClosure);
   }

   public static Closure switchClosure(Predicate[] predicates, Closure[] closures) {
      return SwitchClosure.getInstance(predicates, closures, (Closure)null);
   }

   public static Closure switchClosure(Predicate[] predicates, Closure[] closures, Closure defaultClosure) {
      return SwitchClosure.getInstance(predicates, closures, defaultClosure);
   }

   public static Closure switchClosure(Map predicatesAndClosures) {
      return SwitchClosure.getInstance(predicatesAndClosures);
   }

   public static Closure switchMapClosure(Map objectsAndClosures) {
      Closure[] trs = null;
      Predicate[] preds = null;
      if (objectsAndClosures == null) {
         throw new IllegalArgumentException("The object and closure map must not be null");
      } else {
         Closure def = (Closure)objectsAndClosures.remove((Object)null);
         int size = objectsAndClosures.size();
         trs = new Closure[size];
         preds = new Predicate[size];
         int i = 0;

         for(Iterator it = objectsAndClosures.entrySet().iterator(); it.hasNext(); ++i) {
            Entry entry = (Entry)it.next();
            preds[i] = EqualPredicate.getInstance(entry.getKey());
            trs[i] = (Closure)entry.getValue();
         }

         return switchClosure(preds, trs, def);
      }
   }
}
