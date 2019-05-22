package org.jf.dexlib2.analysis.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.analysis.TypeProto;
import org.jf.dexlib2.analysis.UnresolvedClassException;

public class TypeProtoUtils {
   @Nonnull
   public static Iterable<TypeProto> getSuperclassChain(@Nonnull final TypeProto typeProto) {
      return new Iterable<TypeProto>() {
         public Iterator<TypeProto> iterator() {
            return new Iterator<TypeProto>() {
               @Nullable
               private TypeProto type = TypeProtoUtils.getSuperclassAsTypeProto(typeProto);

               public boolean hasNext() {
                  return this.type != null;
               }

               public TypeProto next() {
                  TypeProto type = this.type;
                  if (type == null) {
                     throw new NoSuchElementException();
                  } else {
                     this.type = TypeProtoUtils.getSuperclassAsTypeProto(type);
                     return type;
                  }
               }

               public void remove() {
                  throw new UnsupportedOperationException();
               }
            };
         }
      };
   }

   @Nullable
   public static TypeProto getSuperclassAsTypeProto(@Nonnull TypeProto type) {
      try {
         String next = type.getSuperclass();
         return next != null ? type.getClassPath().getClass(next) : null;
      } catch (UnresolvedClassException var2) {
         return type.getClassPath().getUnknownClass();
      }
   }

   public static boolean extendsFrom(@Nonnull TypeProto candidate, @Nonnull String possibleSuper) {
      if (candidate.getType().equals(possibleSuper)) {
         return true;
      } else {
         Iterator var2 = getSuperclassChain(candidate).iterator();

         TypeProto superProto;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            superProto = (TypeProto)var2.next();
         } while(!superProto.getType().equals(possibleSuper));

         return true;
      }
   }
}
