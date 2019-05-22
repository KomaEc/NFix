package org.jf.dexlib2.dexbacked.reference;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseMethodProtoReference;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.util.FixedSizeList;

public class DexBackedMethodProtoReference extends BaseMethodProtoReference {
   @Nonnull
   public final DexBackedDexFile dexFile;
   private final int protoIdItemOffset;

   public DexBackedMethodProtoReference(@Nonnull DexBackedDexFile dexFile, int protoIndex) {
      this.dexFile = dexFile;
      this.protoIdItemOffset = dexFile.getProtoIdItemOffset(protoIndex);
   }

   @Nonnull
   public List<String> getParameterTypes() {
      int parametersOffset = this.dexFile.readSmallUint(this.protoIdItemOffset + 8);
      if (parametersOffset > 0) {
         final int parameterCount = this.dexFile.readSmallUint(parametersOffset + 0);
         final int paramListStart = parametersOffset + 4;
         return new FixedSizeList<String>() {
            @Nonnull
            public String readItem(int index) {
               return DexBackedMethodProtoReference.this.dexFile.getType(DexBackedMethodProtoReference.this.dexFile.readUshort(paramListStart + 2 * index));
            }

            public int size() {
               return parameterCount;
            }
         };
      } else {
         return ImmutableList.of();
      }
   }

   @Nonnull
   public String getReturnType() {
      return this.dexFile.getType(this.dexFile.readSmallUint(this.protoIdItemOffset + 4));
   }

   public int getSize() {
      int size = 12;
      List<String> parameters = this.getParameterTypes();
      if (!parameters.isEmpty()) {
         size += 4 + parameters.size() * 2;
      }

      return size;
   }
}
