package org.jf.dexlib2.writer.pool;

import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.writer.MethodSection;

public class MethodPool extends BaseIndexPool<MethodReference> implements MethodSection<CharSequence, CharSequence, MethodProtoReference, MethodReference, PoolMethod> {
   public MethodPool(@Nonnull DexPool dexPool) {
      super(dexPool);
   }

   public void intern(@Nonnull MethodReference method) {
      Integer prev = (Integer)this.internedItems.put(method, 0);
      if (prev == null) {
         ((TypePool)this.dexPool.typeSection).intern(method.getDefiningClass());
         ((ProtoPool)this.dexPool.protoSection).intern(new PoolMethodProto(method));
         ((StringPool)this.dexPool.stringSection).intern(method.getName());
      }

   }

   @Nonnull
   public MethodReference getMethodReference(@Nonnull PoolMethod poolMethod) {
      return poolMethod;
   }

   @Nonnull
   public CharSequence getDefiningClass(@Nonnull MethodReference methodReference) {
      return methodReference.getDefiningClass();
   }

   @Nonnull
   public MethodProtoReference getPrototype(@Nonnull MethodReference methodReference) {
      return new PoolMethodProto(methodReference);
   }

   @Nonnull
   public MethodProtoReference getPrototype(@Nonnull PoolMethod poolMethod) {
      return new PoolMethodProto(poolMethod);
   }

   @Nonnull
   public CharSequence getName(@Nonnull MethodReference methodReference) {
      return methodReference.getName();
   }

   public int getMethodIndex(@Nonnull PoolMethod poolMethod) {
      return this.getItemIndex(poolMethod);
   }
}
