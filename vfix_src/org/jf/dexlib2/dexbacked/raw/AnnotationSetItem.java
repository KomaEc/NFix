package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;

public class AnnotationSetItem {
   public static final int SIZE_OFFSET = 0;
   public static final int LIST_OFFSET = 4;

   @Nonnull
   public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
      return new SectionAnnotator(annotator, mapItem) {
         @Nonnull
         public String getItemName() {
            return "annotation_set_item";
         }

         public void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
            int size = this.dexFile.readSmallUint(out.getCursor());
            out.annotate(4, "size = %d", size);

            for(int i = 0; i < size; ++i) {
               int annotationOffset = this.dexFile.readSmallUint(out.getCursor());
               out.annotate(4, AnnotationItem.getReferenceAnnotation(this.dexFile, annotationOffset));
            }

         }

         public int getItemAlignment() {
            return 4;
         }
      };
   }

   @Nonnull
   public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int annotationSetOffset) {
      return annotationSetOffset == 0 ? "annotation_set_item[NO_OFFSET]" : String.format("annotation_set_item[0x%x]", annotationSetOffset);
   }
}
