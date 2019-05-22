package org.jf.dexlib2.writer.builder;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.immutable.reference.ImmutableMethodProtoReference;
import org.jf.dexlib2.util.MethodUtil;
import org.jf.dexlib2.writer.ProtoSection;

class BuilderProtoPool extends BaseBuilderPool implements ProtoSection<BuilderStringReference, BuilderTypeReference, BuilderMethodProtoReference, BuilderTypeList> {
   @Nonnull
   private final ConcurrentMap<MethodProtoReference, BuilderMethodProtoReference> internedItems = Maps.newConcurrentMap();

   public BuilderProtoPool(@Nonnull DexBuilder dexBuilder) {
      super(dexBuilder);
   }

   @Nonnull
   public BuilderMethodProtoReference internMethodProto(@Nonnull MethodProtoReference methodProto) {
      BuilderMethodProtoReference ret = (BuilderMethodProtoReference)this.internedItems.get(methodProto);
      if (ret != null) {
         return ret;
      } else {
         BuilderMethodProtoReference protoReference = new BuilderMethodProtoReference(((BuilderStringPool)this.dexBuilder.stringSection).internString(MethodUtil.getShorty(methodProto.getParameterTypes(), methodProto.getReturnType())), ((BuilderTypeListPool)this.dexBuilder.typeListSection).internTypeList(methodProto.getParameterTypes()), ((BuilderTypePool)this.dexBuilder.typeSection).internType(methodProto.getReturnType()));
         ret = (BuilderMethodProtoReference)this.internedItems.putIfAbsent(protoReference, protoReference);
         return ret == null ? protoReference : ret;
      }
   }

   @Nonnull
   public BuilderMethodProtoReference internMethodProto(@Nonnull MethodReference methodReference) {
      return this.internMethodProto((MethodProtoReference)(new ImmutableMethodProtoReference(methodReference.getParameterTypes(), methodReference.getReturnType())));
   }

   @Nonnull
   public BuilderStringReference getShorty(@Nonnull BuilderMethodProtoReference proto) {
      return proto.shorty;
   }

   @Nonnull
   public BuilderTypeReference getReturnType(@Nonnull BuilderMethodProtoReference proto) {
      return proto.returnType;
   }

   @Nullable
   public BuilderTypeList getParameters(@Nonnull BuilderMethodProtoReference proto) {
      return proto.parameterTypes;
   }

   public int getItemIndex(@Nonnull BuilderMethodProtoReference proto) {
      return proto.getIndex();
   }

   @Nonnull
   public Collection<? extends Entry<? extends BuilderMethodProtoReference, Integer>> getItems() {
      return new BuilderMapEntryCollection<BuilderMethodProtoReference>(this.internedItems.values()) {
         protected int getValue(@Nonnull BuilderMethodProtoReference key) {
            return key.index;
         }

         protected int setValue(@Nonnull BuilderMethodProtoReference key, int value) {
            int prev = key.index;
            key.index = value;
            return prev;
         }
      };
   }

   public int getItemCount() {
      return this.internedItems.size();
   }
}
