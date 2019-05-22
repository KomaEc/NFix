package org.jf.dexlib2.writer.pool;

import java.util.List;
import org.jf.dexlib2.base.reference.BaseMethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodReference;

public class PoolMethodProto extends BaseMethodProtoReference implements MethodProtoReference {
   private final MethodReference methodReference;

   public PoolMethodProto(MethodReference methodReference) {
      this.methodReference = methodReference;
   }

   public List<? extends CharSequence> getParameterTypes() {
      return this.methodReference.getParameterTypes();
   }

   public String getReturnType() {
      return this.methodReference.getReturnType();
   }
}
