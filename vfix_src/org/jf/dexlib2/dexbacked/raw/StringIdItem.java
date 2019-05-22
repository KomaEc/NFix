package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;
import org.jf.util.StringUtils;

public class StringIdItem {
   public static final int ITEM_SIZE = 4;

   @Nonnull
   public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
      return new SectionAnnotator(annotator, mapItem) {
         @Nonnull
         public String getItemName() {
            return "string_id_item";
         }

         public void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
            int stringDataOffset = this.dexFile.readSmallUint(out.getCursor());

            try {
               String stringValue = this.dexFile.getString(itemIndex);
               out.annotate(4, "string_data_item[0x%x]: \"%s\"", stringDataOffset, StringUtils.escapeString(stringValue));
            } catch (Exception var6) {
               System.err.print("Error while resolving string value at index: ");
               System.err.print(itemIndex);
               var6.printStackTrace(System.err);
               out.annotate(4, "string_id_item[0x%x]", stringDataOffset);
            }
         }
      };
   }

   @Nonnull
   public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int stringIndex) {
      return getReferenceAnnotation(dexFile, stringIndex, false);
   }

   public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int stringIndex, boolean quote) {
      try {
         String string = dexFile.getString(stringIndex);
         if (quote) {
            string = String.format("\"%s\"", StringUtils.escapeString(string));
         }

         return String.format("string_id_item[%d]: %s", stringIndex, string);
      } catch (Exception var4) {
         var4.printStackTrace(System.err);
         return String.format("string_id_item[%d]", stringIndex);
      }
   }

   @Nonnull
   public static String getOptionalReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int stringIndex) {
      return getOptionalReferenceAnnotation(dexFile, stringIndex, false);
   }

   public static String getOptionalReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int stringIndex, boolean quote) {
      return stringIndex == -1 ? "string_id_item[NO_INDEX]" : getReferenceAnnotation(dexFile, stringIndex, quote);
   }

   public static String[] getStrings(@Nonnull RawDexFile dexFile) {
      MapItem mapItem = dexFile.getMapItemForSection(1);
      if (mapItem == null) {
         return new String[0];
      } else {
         int stringCount = mapItem.getItemCount();
         String[] ret = new String[stringCount];

         for(int i = 0; i < stringCount; ++i) {
            ret[i] = dexFile.getString(i);
         }

         return ret;
      }
   }
}
