package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;

public class AnnotationSetRefList {
   public static final int SIZE_OFFSET = 0;
   public static final int LIST_OFFSET = 4;

   @Nonnull
   public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
      return new SectionAnnotator(annotator, mapItem) {
         @Nonnull
         public String getItemName() {
            return "annotation_set_ref_list";
         }

         protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
            int size = this.dexFile.readSmallUint(out.getCursor());
            out.annotate(4, "size = %d", size);

            for(int i = 0; i < size; ++i) {
               int annotationSetOffset = this.dexFile.readSmallUint(out.getCursor());
               out.annotate(4, "annotation_set_item[0x%x]", annotationSetOffset);
            }

         }

         public int getItemAlignment() {
            return 4;
         }
      };
   }

   @Nonnull
   public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int annotationSetRefListOffset) {
      return annotationSetRefListOffset == 0 ? "annotation_set_ref_list[NO_OFFSET]" : String.format("annotation_set_ref_list[0x%x]", annotationSetRefListOffset);
   }
}
