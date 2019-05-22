package soot.jimple.toolkits.typing.fast;

import java.util.Collection;
import java.util.Collections;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.IntType;
import soot.IntegerType;
import soot.ShortType;
import soot.Type;

public class AugHierarchy implements IHierarchy {
   public Collection<Type> lcas(Type a, Type b) {
      return lcas_(a, b);
   }

   public static Collection<Type> lcas_(Type a, Type b) {
      if (TypeResolver.typesEqual(a, b)) {
         return Collections.singletonList(a);
      } else if (a instanceof BottomType) {
         return Collections.singletonList(b);
      } else if (b instanceof BottomType) {
         return Collections.singletonList(a);
      } else if (a instanceof IntegerType && b instanceof IntegerType) {
         if (a instanceof Integer1Type) {
            return Collections.singletonList(b);
         } else if (b instanceof Integer1Type) {
            return Collections.singletonList(a);
         } else if (!(a instanceof BooleanType) && !(b instanceof BooleanType)) {
            if ((!(a instanceof ByteType) || !(b instanceof Integer32767Type)) && (!(b instanceof ByteType) || !(a instanceof Integer32767Type))) {
               if (a instanceof CharType && (b instanceof ShortType || b instanceof ByteType) || b instanceof CharType && (a instanceof ShortType || a instanceof ByteType)) {
                  return Collections.singletonList(IntType.v());
               } else {
                  return ancestor_(a, b) ? Collections.singletonList(a) : Collections.singletonList(b);
               }
            } else {
               return Collections.singletonList(ShortType.v());
            }
         } else {
            return Collections.emptyList();
         }
      } else {
         return (Collection)(!(a instanceof IntegerType) && !(b instanceof IntegerType) ? BytecodeHierarchy.lcas_(a, b) : Collections.emptyList());
      }
   }

   public boolean ancestor(Type ancestor, Type child) {
      return ancestor_(ancestor, child);
   }

   public static boolean ancestor_(Type ancestor, Type child) {
      if (TypeResolver.typesEqual(ancestor, child)) {
         return true;
      } else if (ancestor instanceof Integer1Type) {
         return child instanceof BottomType;
      } else if (ancestor instanceof BooleanType) {
         return child instanceof BottomType || child instanceof Integer1Type;
      } else if (ancestor instanceof Integer127Type) {
         return child instanceof BottomType || child instanceof Integer1Type;
      } else if (!(ancestor instanceof ByteType) && !(ancestor instanceof Integer32767Type)) {
         if (ancestor instanceof CharType) {
            return child instanceof BottomType || child instanceof Integer1Type || child instanceof Integer127Type || child instanceof Integer32767Type;
         } else if (ancestor instanceof ShortType) {
            return child instanceof BottomType || child instanceof Integer1Type || child instanceof Integer127Type || child instanceof Integer32767Type || child instanceof ByteType;
         } else if (ancestor instanceof IntType) {
            return child instanceof BottomType || child instanceof Integer1Type || child instanceof Integer127Type || child instanceof Integer32767Type || child instanceof ByteType || child instanceof CharType || child instanceof ShortType;
         } else {
            return child instanceof IntegerType ? false : BytecodeHierarchy.ancestor_(ancestor, child);
         }
      } else {
         return child instanceof BottomType || child instanceof Integer1Type || child instanceof Integer127Type;
      }
   }
}
