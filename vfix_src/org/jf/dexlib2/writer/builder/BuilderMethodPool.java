package org.jf.dexlib2.writer.builder;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseMethodReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.writer.MethodSection;

class BuilderMethodPool extends BaseBuilderPool implements MethodSection<BuilderStringReference, BuilderTypeReference, BuilderMethodProtoReference, BuilderMethodReference, BuilderMethod> {
   @Nonnull
   private final ConcurrentMap<MethodReference, BuilderMethodReference> internedItems = Maps.newConcurrentMap();

   public BuilderMethodPool(@Nonnull DexBuilder dexBuilder) {
      super(dexBuilder);
   }

   @Nonnull
   public BuilderMethodReference internMethod(@Nonnull MethodReference methodReference) {
      BuilderMethodReference ret = (BuilderMethodReference)this.internedItems.get(methodReference);
      if (ret != null) {
         return ret;
      } else {
         BuilderMethodReference dexPoolMethodReference = new BuilderMethodReference(((BuilderTypePool)this.dexBuilder.typeSection).internType(methodReference.getDefiningClass()), ((BuilderStringPool)this.dexBuilder.stringSection).internString(methodReference.getName()), ((BuilderProtoPool)this.dexBuilder.protoSection).internMethodProto(methodReference));
         ret = (BuilderMethodReference)this.internedItems.putIfAbsent(dexPoolMethodReference, dexPoolMethodReference);
         return ret == null ? dexPoolMethodReference : ret;
      }
   }

   @Nonnull
   public BuilderMethodReference internMethod(@Nonnull String definingClass, @Nonnull String name, @Nonnull List<? extends CharSequence> parameters, @Nonnull String returnType) {
      return this.internMethod(new BuilderMethodPool.MethodKey(definingClass, name, parameters, returnType));
   }

   @Nonnull
   public BuilderMethodReference getMethodReference(@Nonnull BuilderMethod builderMethod) {
      return builderMethod.methodReference;
   }

   @Nonnull
   public BuilderTypeReference getDefiningClass(@Nonnull BuilderMethodReference key) {
      return key.definingClass;
   }

   @Nonnull
   public BuilderMethodProtoReference getPrototype(@Nonnull BuilderMethodReference key) {
      return key.proto;
   }

   @Nonnull
   public BuilderMethodProtoReference getPrototype(@Nonnull BuilderMethod builderMethod) {
      return builderMethod.methodReference.proto;
   }

   @Nonnull
   public BuilderStringReference getName(@Nonnull BuilderMethodReference key) {
      return key.name;
   }

   public int getMethodIndex(@Nonnull BuilderMethod builderMethod) {
      return builderMethod.methodReference.index;
   }

   public int getItemIndex(@Nonnull BuilderMethodReference key) {
      return key.index;
   }

   @Nonnull
   public Collection<? extends Entry<? extends BuilderMethodReference, Integer>> getItems() {
      return new BuilderMapEntryCollection<BuilderMethodReference>(this.internedItems.values()) {
         protected int getValue(@Nonnull BuilderMethodReference key) {
            return key.index;
         }

         protected int setValue(@Nonnull BuilderMethodReference key, int value) {
            int prev = key.index;
            key.index = value;
            return prev;
         }
      };
   }

   public int getItemCount() {
      return this.internedItems.size();
   }

   private static class MethodKey extends BaseMethodReference implements MethodReference {
      @Nonnull
      private final String definingClass;
      @Nonnull
      private final String name;
      @Nonnull
      private final List<? extends CharSequence> parameterTypes;
      @Nonnull
      private final String returnType;

      public MethodKey(@Nonnull String definingClass, @Nonnull String name, @Nonnull List<? extends CharSequence> parameterTypes, @Nonnull String returnType) {
         this.definingClass = definingClass;
         this.name = name;
         this.parameterTypes = parameterTypes;
         this.returnType = returnType;
      }

      @Nonnull
      public String getDefiningClass() {
         return this.definingClass;
      }

      @Nonnull
      public String getName() {
         return this.name;
      }

      @Nonnull
      public List<? extends CharSequence> getParameterTypes() {
         return this.parameterTypes;
      }

      @Nonnull
      public String getReturnType() {
         return this.returnType;
      }
   }
}
