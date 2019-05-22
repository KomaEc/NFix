package org.jf.dexlib2.rewriter;

import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseTypeReference;
import org.jf.dexlib2.iface.reference.TypeReference;

public class RewriterUtils {
   @Nullable
   public static <T> T rewriteNullable(@Nonnull Rewriter<T> rewriter, @Nullable T value) {
      return value == null ? null : rewriter.rewrite(value);
   }

   public static <T> Set<T> rewriteSet(@Nonnull final Rewriter<T> rewriter, @Nonnull final Set<? extends T> set) {
      return new AbstractSet<T>() {
         @Nonnull
         public Iterator<T> iterator() {
            final Iterator<? extends T> iterator = set.iterator();
            return new Iterator<T>() {
               public boolean hasNext() {
                  return iterator.hasNext();
               }

               public T next() {
                  return RewriterUtils.rewriteNullable(rewriter, iterator.next());
               }

               public void remove() {
                  iterator.remove();
               }
            };
         }

         public int size() {
            return set.size();
         }
      };
   }

   public static <T> List<T> rewriteList(@Nonnull final Rewriter<T> rewriter, @Nonnull final List<? extends T> list) {
      return new AbstractList<T>() {
         public T get(int i) {
            return RewriterUtils.rewriteNullable(rewriter, list.get(i));
         }

         public int size() {
            return list.size();
         }
      };
   }

   public static <T> Iterable<T> rewriteIterable(@Nonnull final Rewriter<T> rewriter, @Nonnull final Iterable<? extends T> iterable) {
      return new Iterable<T>() {
         public Iterator<T> iterator() {
            final Iterator<? extends T> iterator = iterable.iterator();
            return new Iterator<T>() {
               public boolean hasNext() {
                  return iterator.hasNext();
               }

               public T next() {
                  return RewriterUtils.rewriteNullable(rewriter, iterator.next());
               }

               public void remove() {
                  iterator.remove();
               }
            };
         }
      };
   }

   public static TypeReference rewriteTypeReference(@Nonnull final Rewriter<String> typeRewriter, @Nonnull final TypeReference typeReference) {
      return new BaseTypeReference() {
         @Nonnull
         public String getType() {
            return (String)typeRewriter.rewrite(typeReference.getType());
         }
      };
   }
}
