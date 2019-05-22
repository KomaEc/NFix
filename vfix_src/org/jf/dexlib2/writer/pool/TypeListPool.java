package org.jf.dexlib2.writer.pool;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.writer.TypeListSection;

public class TypeListPool extends BaseNullableOffsetPool<TypeListPool.Key<? extends Collection<? extends CharSequence>>> implements TypeListSection<CharSequence, TypeListPool.Key<? extends Collection<? extends CharSequence>>> {
   public TypeListPool(@Nonnull DexPool dexPool) {
      super(dexPool);
   }

   public void intern(@Nonnull Collection<? extends CharSequence> types) {
      if (types.size() > 0) {
         TypeListPool.Key<? extends Collection<? extends CharSequence>> key = new TypeListPool.Key(types);
         Integer prev = (Integer)this.internedItems.put(key, 0);
         if (prev == null) {
            Iterator var4 = types.iterator();

            while(var4.hasNext()) {
               CharSequence type = (CharSequence)var4.next();
               ((TypePool)this.dexPool.typeSection).intern(type);
            }
         }
      }

   }

   @Nonnull
   public Collection<? extends CharSequence> getTypes(TypeListPool.Key<? extends Collection<? extends CharSequence>> typesKey) {
      return (Collection)(typesKey == null ? ImmutableList.of() : typesKey.types);
   }

   public int getNullableItemOffset(@Nullable TypeListPool.Key<? extends Collection<? extends CharSequence>> key) {
      return key != null && key.types.size() != 0 ? super.getNullableItemOffset(key) : 0;
   }

   public static class Key<TypeCollection extends Collection<? extends CharSequence>> implements Comparable<TypeListPool.Key<? extends Collection<? extends CharSequence>>> {
      @Nonnull
      TypeCollection types;

      public Key(@Nonnull TypeCollection types) {
         this.types = types;
      }

      public int hashCode() {
         int hashCode = 1;

         CharSequence type;
         for(Iterator var2 = this.types.iterator(); var2.hasNext(); hashCode = hashCode * 31 + type.toString().hashCode()) {
            type = (CharSequence)var2.next();
         }

         return hashCode;
      }

      public boolean equals(Object o) {
         if (o instanceof TypeListPool.Key) {
            TypeListPool.Key<? extends Collection<? extends CharSequence>> other = (TypeListPool.Key)o;
            if (this.types.size() != other.types.size()) {
               return false;
            } else {
               Iterator<? extends CharSequence> otherTypes = other.types.iterator();
               Iterator var4 = this.types.iterator();

               CharSequence type;
               do {
                  if (!var4.hasNext()) {
                     return true;
                  }

                  type = (CharSequence)var4.next();
               } while(type.toString().equals(((CharSequence)otherTypes.next()).toString()));

               return false;
            }
         } else {
            return false;
         }
      }

      public String toString() {
         StringBuilder sb = new StringBuilder();
         Iterator var2 = this.types.iterator();

         while(var2.hasNext()) {
            CharSequence type = (CharSequence)var2.next();
            sb.append(type.toString());
         }

         return sb.toString();
      }

      public int compareTo(TypeListPool.Key<? extends Collection<? extends CharSequence>> o) {
         Iterator<? extends CharSequence> other = o.types.iterator();
         Iterator var3 = this.types.iterator();

         int comparison;
         do {
            if (!var3.hasNext()) {
               if (other.hasNext()) {
                  return -1;
               }

               return 0;
            }

            CharSequence type = (CharSequence)var3.next();
            if (!other.hasNext()) {
               return 1;
            }

            comparison = type.toString().compareTo(((CharSequence)other.next()).toString());
         } while(comparison == 0);

         return comparison;
      }
   }
}
