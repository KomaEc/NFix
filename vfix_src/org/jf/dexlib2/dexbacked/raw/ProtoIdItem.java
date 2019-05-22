package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;

public class ProtoIdItem {
   public static final int ITEM_SIZE = 12;
   public static final int SHORTY_OFFSET = 0;
   public static final int RETURN_TYPE_OFFSET = 4;
   public static final int PARAMETERS_OFFSET = 8;

   @Nonnull
   public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
      return new SectionAnnotator(annotator, mapItem) {
         @Nonnull
         public String getItemName() {
            return "proto_id_item";
         }

         protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
            int shortyIndex = this.dexFile.readSmallUint(out.getCursor());
            out.annotate(4, "shorty_idx = %s", StringIdItem.getReferenceAnnotation(this.dexFile, shortyIndex));
            int returnTypeIndex = this.dexFile.readSmallUint(out.getCursor());
            out.annotate(4, "return_type_idx = %s", TypeIdItem.getReferenceAnnotation(this.dexFile, returnTypeIndex));
            int parametersOffset = this.dexFile.readSmallUint(out.getCursor());
            out.annotate(4, "parameters_off = %s", TypeListItem.getReferenceAnnotation(this.dexFile, parametersOffset));
         }
      };
   }

   @Nonnull
   public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int protoIndex) {
      try {
         String protoString = asString(dexFile, protoIndex);
         return String.format("proto_id_item[%d]: %s", protoIndex, protoString);
      } catch (Exception var3) {
         var3.printStackTrace(System.err);
         return String.format("proto_id_item[%d]", protoIndex);
      }
   }

   @Nonnull
   public static String asString(@Nonnull DexBackedDexFile dexFile, int protoIndex) {
      int offset = dexFile.getProtoIdItemOffset(protoIndex);
      StringBuilder sb = new StringBuilder();
      sb.append("(");
      int parametersOffset = dexFile.readSmallUint(offset + 8);
      sb.append(TypeListItem.asString(dexFile, parametersOffset));
      sb.append(")");
      int returnTypeIndex = dexFile.readSmallUint(offset + 4);
      String returnType = dexFile.getType(returnTypeIndex);
      sb.append(returnType);
      return sb.toString();
   }

   public static String[] getProtos(@Nonnull RawDexFile dexFile) {
      MapItem mapItem = dexFile.getMapItemForSection(3);
      if (mapItem == null) {
         return new String[0];
      } else {
         int protoCount = mapItem.getItemCount();
         String[] ret = new String[protoCount];

         for(int i = 0; i < protoCount; ++i) {
            ret[i] = asString(dexFile, i);
         }

         return ret;
      }
   }
}
