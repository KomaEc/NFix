package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;

public class FieldIdItem {
   public static final int ITEM_SIZE = 8;
   public static final int CLASS_OFFSET = 0;
   public static final int TYPE_OFFSET = 2;
   public static final int NAME_OFFSET = 4;

   @Nonnull
   public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
      return new SectionAnnotator(annotator, mapItem) {
         @Nonnull
         public String getItemName() {
            return "field_id_item";
         }

         protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
            int classIndex = this.dexFile.readUshort(out.getCursor());
            out.annotate(2, "class_idx = %s", TypeIdItem.getReferenceAnnotation(this.dexFile, classIndex));
            int typeIndex = this.dexFile.readUshort(out.getCursor());
            out.annotate(2, "return_type_idx = %s", TypeIdItem.getReferenceAnnotation(this.dexFile, typeIndex));
            int nameIndex = this.dexFile.readSmallUint(out.getCursor());
            out.annotate(4, "name_idx = %s", StringIdItem.getReferenceAnnotation(this.dexFile, nameIndex));
         }
      };
   }

   @Nonnull
   public static String asString(@Nonnull DexBackedDexFile dexFile, int fieldIndex) {
      int fieldOffset = dexFile.getFieldIdItemOffset(fieldIndex);
      int classIndex = dexFile.readUshort(fieldOffset + 0);
      String classType = dexFile.getType(classIndex);
      int typeIndex = dexFile.readUshort(fieldOffset + 2);
      String fieldType = dexFile.getType(typeIndex);
      int nameIndex = dexFile.readSmallUint(fieldOffset + 4);
      String fieldName = dexFile.getString(nameIndex);
      return String.format("%s->%s:%s", classType, fieldName, fieldType);
   }

   @Nonnull
   public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int fieldIndex) {
      try {
         String fieldString = asString(dexFile, fieldIndex);
         return String.format("field_id_item[%d]: %s", fieldIndex, fieldString);
      } catch (Exception var3) {
         var3.printStackTrace(System.err);
         return String.format("field_id_item[%d]", fieldIndex);
      }
   }

   public static String[] getFields(@Nonnull RawDexFile dexFile) {
      MapItem mapItem = dexFile.getMapItemForSection(4);
      if (mapItem == null) {
         return new String[0];
      } else {
         int fieldCount = mapItem.getItemCount();
         String[] ret = new String[fieldCount];

         for(int i = 0; i < fieldCount; ++i) {
            ret[i] = asString(dexFile, i);
         }

         return ret;
      }
   }
}
