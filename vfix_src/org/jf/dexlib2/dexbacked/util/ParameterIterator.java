package org.jf.dexlib2.dexbacked.util;

import com.google.common.collect.ImmutableSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseMethodParameter;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.MethodParameter;

public class ParameterIterator implements Iterator<MethodParameter> {
   private final Iterator<? extends CharSequence> parameterTypes;
   private final Iterator<? extends Set<? extends Annotation>> parameterAnnotations;
   private final Iterator<String> parameterNames;

   public ParameterIterator(@Nonnull List<? extends CharSequence> parameterTypes, @Nonnull List<? extends Set<? extends Annotation>> parameterAnnotations, @Nonnull Iterator<String> parameterNames) {
      this.parameterTypes = parameterTypes.iterator();
      this.parameterAnnotations = parameterAnnotations.iterator();
      this.parameterNames = parameterNames;
   }

   public boolean hasNext() {
      return this.parameterTypes.hasNext();
   }

   public MethodParameter next() {
      final String type = ((CharSequence)this.parameterTypes.next()).toString();
      final Object annotations;
      if (this.parameterAnnotations.hasNext()) {
         annotations = (Set)this.parameterAnnotations.next();
      } else {
         annotations = ImmutableSet.of();
      }

      final String name;
      if (this.parameterNames.hasNext()) {
         name = (String)this.parameterNames.next();
      } else {
         name = null;
      }

      return new BaseMethodParameter() {
         @Nonnull
         public Set<? extends Annotation> getAnnotations() {
            return (Set)annotations;
         }

         @Nullable
         public String getName() {
            return name;
         }

         @Nonnull
         public String getType() {
            return type;
         }
      };
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }
}
