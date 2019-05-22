package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;

public class AnnotationDirectoryItem {
   public static final int CLASS_ANNOTATIONS_OFFSET = 0;
   public static final int FIELD_SIZE_OFFSET = 4;
   public static final int ANNOTATED_METHOD_SIZE_OFFSET = 8;
   public static final int ANNOTATED_PARAMETERS_SIZE = 12;

   @Nonnull
   public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
      return new SectionAnnotator(annotator, mapItem) {
         @Nonnull
         public String getItemName() {
            return "annotation_directory_item";
         }

         public int getItemAlignment() {
            return 4;
         }

         protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
            int classAnnotationsOffset = this.dexFile.readSmallUint(out.getCursor());
            out.annotate(4, "class_annotations_off = %s", AnnotationSetItem.getReferenceAnnotation(this.dexFile, classAnnotationsOffset));
            int fieldsSize = this.dexFile.readSmallUint(out.getCursor());
            out.annotate(4, "fields_size = %d", fieldsSize);
            int annotatedMethodsSize = this.dexFile.readSmallUint(out.getCursor());
            out.annotate(4, "annotated_methods_size = %d", annotatedMethodsSize);
            int annotatedParameterSize = this.dexFile.readSmallUint(out.getCursor());
            out.annotate(4, "annotated_parameters_size = %d", annotatedParameterSize);
            int i;
            int methodIndex;
            int annotationOffset;
            if (fieldsSize > 0) {
               out.annotate(0, "field_annotations:");
               out.indent();

               for(i = 0; i < fieldsSize; ++i) {
                  out.annotate(0, "field_annotation[%d]", i);
                  out.indent();
                  methodIndex = this.dexFile.readSmallUint(out.getCursor());
                  out.annotate(4, "%s", FieldIdItem.getReferenceAnnotation(this.dexFile, methodIndex));
                  annotationOffset = this.dexFile.readSmallUint(out.getCursor());
                  out.annotate(4, "%s", AnnotationSetItem.getReferenceAnnotation(this.dexFile, annotationOffset));
                  out.deindent();
               }

               out.deindent();
            }

            if (annotatedMethodsSize > 0) {
               out.annotate(0, "method_annotations:");
               out.indent();

               for(i = 0; i < annotatedMethodsSize; ++i) {
                  out.annotate(0, "method_annotation[%d]", i);
                  out.indent();
                  methodIndex = this.dexFile.readSmallUint(out.getCursor());
                  out.annotate(4, "%s", MethodIdItem.getReferenceAnnotation(this.dexFile, methodIndex));
                  annotationOffset = this.dexFile.readSmallUint(out.getCursor());
                  out.annotate(4, "%s", AnnotationSetItem.getReferenceAnnotation(this.dexFile, annotationOffset));
                  out.deindent();
               }

               out.deindent();
            }

            if (annotatedParameterSize > 0) {
               out.annotate(0, "parameter_annotations:");
               out.indent();

               for(i = 0; i < annotatedParameterSize; ++i) {
                  out.annotate(0, "parameter_annotation[%d]", i);
                  out.indent();
                  methodIndex = this.dexFile.readSmallUint(out.getCursor());
                  out.annotate(4, "%s", MethodIdItem.getReferenceAnnotation(this.dexFile, methodIndex));
                  annotationOffset = this.dexFile.readSmallUint(out.getCursor());
                  out.annotate(4, "%s", AnnotationSetRefList.getReferenceAnnotation(this.dexFile, annotationOffset));
                  out.deindent();
               }

               out.deindent();
            }

         }
      };
   }
}
