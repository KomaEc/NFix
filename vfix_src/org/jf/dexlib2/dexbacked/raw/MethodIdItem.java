package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;

public class MethodIdItem {
   public static final int ITEM_SIZE = 8;
   public static final int CLASS_OFFSET = 0;
   public static final int PROTO_OFFSET = 2;
   public static final int NAME_OFFSET = 4;

   @Nonnull
   public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
      return new SectionAnnotator(annotator, mapItem) {
         @Nonnull
         public String getItemName() {
            return "method_id_item";
         }

         public void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
            int classIndex = this.dexFile.readUshort(out.getCursor());
            out.annotate(2, "class_idx = %s", TypeIdItem.getReferenceAnnotation(this.dexFile, classIndex));
            int protoIndex = this.dexFile.readUshort(out.getCursor());
            out.annotate(2, "proto_idx = %s", ProtoIdItem.getReferenceAnnotation(this.dexFile, protoIndex));
            int nameIndex = this.dexFile.readSmallUint(out.getCursor());
            out.annotate(4, "name_idx = %s", StringIdItem.getReferenceAnnotation(this.dexFile, nameIndex));
         }
      };
   }

   @Nonnull
   public static String asString(@Nonnull DexBackedDexFile dexFile, int methodIndex) {
      int methodOffset = dexFile.getMethodIdItemOffset(methodIndex);
      int classIndex = dexFile.readUshort(methodOffset + 0);
      String classType = dexFile.getType(classIndex);
      int protoIndex = dexFile.readUshort(methodOffset + 2);
      String protoString = ProtoIdItem.asString(dexFile, protoIndex);
      int nameIndex = dexFile.readSmallUint(methodOffset + 4);
      String methodName = dexFile.getString(nameIndex);
      return String.format("%s->%s%s", classType, methodName, protoString);
   }

   @Nonnull
   public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int methodIndex) {
      try {
         String methodString = asString(dexFile, methodIndex);
         return String.format("method_id_item[%d]: %s", methodIndex, methodString);
      } catch (Exception var3) {
         var3.printStackTrace(System.err);
         return String.format("method_id_item[%d]", methodIndex);
      }
   }

   public static String[] getMethods(@Nonnull RawDexFile dexFile) {
      MapItem mapItem = dexFile.getMapItemForSection(5);
      if (mapItem == null) {
         return new String[0];
      } else {
         int methodCount = mapItem.getItemCount();
         String[] ret = new String[methodCount];

         for(int i = 0; i < methodCount; ++i) {
            ret[i] = asString(dexFile, i);
         }

         return ret;
      }
   }
}
