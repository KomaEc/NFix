package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;

public class TypeIdItem {
   public static final int ITEM_SIZE = 4;

   @Nonnull
   public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
      return new SectionAnnotator(annotator, mapItem) {
         @Nonnull
         public String getItemName() {
            return "type_id_item";
         }

         protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
            int stringIndex = this.dexFile.readSmallUint(out.getCursor());
            out.annotate(4, StringIdItem.getReferenceAnnotation(this.dexFile, stringIndex));
         }
      };
   }

   @Nonnull
   public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int typeIndex) {
      try {
         String typeString = dexFile.getType(typeIndex);
         return String.format("type_id_item[%d]: %s", typeIndex, typeString);
      } catch (Exception var3) {
         var3.printStackTrace(System.err);
         return String.format("type_id_item[%d]", typeIndex);
      }
   }

   @Nonnull
   public static String getOptionalReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int typeIndex) {
      return typeIndex == -1 ? "type_id_item[NO_INDEX]" : getReferenceAnnotation(dexFile, typeIndex);
   }

   public static String[] getTypes(@Nonnull RawDexFile dexFile) {
      MapItem mapItem = dexFile.getMapItemForSection(2);
      if (mapItem == null) {
         return new String[0];
      } else {
         int typeCount = mapItem.getItemCount();
         String[] ret = new String[typeCount];

         for(int i = 0; i < typeCount; ++i) {
            ret[i] = dexFile.getType(i);
         }

         return ret;
      }
   }
}
