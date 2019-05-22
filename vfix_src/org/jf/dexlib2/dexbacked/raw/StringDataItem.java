package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;
import org.jf.util.StringUtils;

public class StringDataItem {
   @Nonnull
   public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
      return new SectionAnnotator(annotator, mapItem) {
         @Nonnull
         public String getItemName() {
            return "string_data_item";
         }

         protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
            DexReader reader = this.dexFile.readerAt(out.getCursor());
            int utf16Length = reader.readSmallUleb128();
            out.annotateTo(reader.getOffset(), "utf16_size = %d", utf16Length);
            String value = reader.readString(utf16Length);
            out.annotateTo(reader.getOffset() + 1, "data = \"%s\"", StringUtils.escapeString(value));
         }
      };
   }
}
