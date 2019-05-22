package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;

public class AnnotationItem {
   public static final int VISIBILITY_OFFSET = 0;
   public static final int ANNOTATION_OFFSET = 1;

   @Nonnull
   public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
      return new SectionAnnotator(annotator, mapItem) {
         @Nonnull
         public String getItemName() {
            return "annotation_item";
         }

         protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
            int visibility = this.dexFile.readUbyte(out.getCursor());
            out.annotate(1, "visibility = %d: %s", visibility, AnnotationItem.getAnnotationVisibility(visibility));
            DexReader reader = this.dexFile.readerAt(out.getCursor());
            EncodedValue.annotateEncodedAnnotation(out, reader);
         }
      };
   }

   private static String getAnnotationVisibility(int visibility) {
      switch(visibility) {
      case 0:
         return "build";
      case 1:
         return "runtime";
      case 2:
         return "system";
      default:
         return "invalid visibility";
      }
   }

   public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int annotationItemOffset) {
      try {
         DexReader reader = dexFile.readerAt(annotationItemOffset);
         reader.readUbyte();
         int typeIndex = reader.readSmallUleb128();
         String annotationType = dexFile.getType(typeIndex);
         return String.format("annotation_item[0x%x]: %s", annotationItemOffset, annotationType);
      } catch (Exception var5) {
         var5.printStackTrace(System.err);
         return String.format("annotation_item[0x%x]", annotationItemOffset);
      }
   }
}
