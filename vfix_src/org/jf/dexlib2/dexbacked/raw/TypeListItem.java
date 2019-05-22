package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;

public class TypeListItem {
   public static final int SIZE_OFFSET = 0;
   public static final int LIST_OFFSET = 4;

   @Nonnull
   public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
      return new SectionAnnotator(annotator, mapItem) {
         @Nonnull
         public String getItemName() {
            return "type_list";
         }

         protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
            int size = this.dexFile.readSmallUint(out.getCursor());
            out.annotate(4, "size: %d", size);

            for(int i = 0; i < size; ++i) {
               int typeIndex = this.dexFile.readUshort(out.getCursor());
               out.annotate(2, TypeIdItem.getReferenceAnnotation(this.dexFile, typeIndex));
            }

         }

         public int getItemAlignment() {
            return 4;
         }
      };
   }

   @Nonnull
   public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int typeListOffset) {
      if (typeListOffset == 0) {
         return "type_list_item[NO_OFFSET]";
      } else {
         try {
            String typeList = asString(dexFile, typeListOffset);
            return String.format("type_list_item[0x%x]: %s", typeListOffset, typeList);
         } catch (Exception var3) {
            var3.printStackTrace(System.err);
            return String.format("type_list_item[0x%x]", typeListOffset);
         }
      }
   }

   @Nonnull
   public static String asString(@Nonnull DexBackedDexFile dexFile, int typeListOffset) {
      if (typeListOffset == 0) {
         return "";
      } else {
         StringBuilder sb = new StringBuilder();
         int size = dexFile.readSmallUint(typeListOffset);

         for(int i = 0; i < size; ++i) {
            int typeIndex = dexFile.readUshort(typeListOffset + 4 + i * 2);
            String type = dexFile.getType(typeIndex);
            sb.append(type);
         }

         return sb.toString();
      }
   }
}
