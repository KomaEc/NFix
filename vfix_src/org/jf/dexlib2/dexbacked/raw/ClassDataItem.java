package org.jf.dexlib2.dexbacked.raw;

import com.google.common.base.Joiner;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.AccessFlags;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;

public class ClassDataItem {
   @Nonnull
   public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
      return new SectionAnnotator(annotator, mapItem) {
         private SectionAnnotator codeItemAnnotator = null;

         public void annotateSection(@Nonnull AnnotatedBytes out) {
            this.codeItemAnnotator = this.annotator.getAnnotator(8193);
            super.annotateSection(out);
         }

         @Nonnull
         public String getItemName() {
            return "class_data_item";
         }

         protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
            DexReader reader = this.dexFile.readerAt(out.getCursor());
            int staticFieldsSize = reader.readSmallUleb128();
            out.annotateTo(reader.getOffset(), "static_fields_size = %d", staticFieldsSize);
            int instanceFieldsSize = reader.readSmallUleb128();
            out.annotateTo(reader.getOffset(), "instance_fields_size = %d", instanceFieldsSize);
            int directMethodsSize = reader.readSmallUleb128();
            out.annotateTo(reader.getOffset(), "direct_methods_size = %d", directMethodsSize);
            int virtualMethodsSize = reader.readSmallUleb128();
            out.annotateTo(reader.getOffset(), "virtual_methods_size = %d", virtualMethodsSize);
            int previousIndex = 0;
            int i;
            if (staticFieldsSize > 0) {
               out.annotate(0, "static_fields:");
               out.indent();

               for(i = 0; i < staticFieldsSize; ++i) {
                  out.annotate(0, "static_field[%d]", i);
                  out.indent();
                  previousIndex = this.annotateEncodedField(out, this.dexFile, reader, previousIndex);
                  out.deindent();
               }

               out.deindent();
            }

            if (instanceFieldsSize > 0) {
               out.annotate(0, "instance_fields:");
               out.indent();
               previousIndex = 0;

               for(i = 0; i < instanceFieldsSize; ++i) {
                  out.annotate(0, "instance_field[%d]", i);
                  out.indent();
                  previousIndex = this.annotateEncodedField(out, this.dexFile, reader, previousIndex);
                  out.deindent();
               }

               out.deindent();
            }

            if (directMethodsSize > 0) {
               out.annotate(0, "direct_methods:");
               out.indent();
               previousIndex = 0;

               for(i = 0; i < directMethodsSize; ++i) {
                  out.annotate(0, "direct_method[%d]", i);
                  out.indent();
                  previousIndex = this.annotateEncodedMethod(out, this.dexFile, reader, previousIndex);
                  out.deindent();
               }

               out.deindent();
            }

            if (virtualMethodsSize > 0) {
               out.annotate(0, "virtual_methods:");
               out.indent();
               previousIndex = 0;

               for(i = 0; i < virtualMethodsSize; ++i) {
                  out.annotate(0, "virtual_method[%d]", i);
                  out.indent();
                  previousIndex = this.annotateEncodedMethod(out, this.dexFile, reader, previousIndex);
                  out.deindent();
               }

               out.deindent();
            }

         }

         private int annotateEncodedField(@Nonnull AnnotatedBytes out, @Nonnull RawDexFile dexFile, @Nonnull DexReader reader, int previousIndex) {
            int indexDelta = reader.readLargeUleb128();
            int fieldIndex = previousIndex + indexDelta;
            out.annotateTo(reader.getOffset(), "field_idx_diff = %d: %s", indexDelta, FieldIdItem.getReferenceAnnotation(dexFile, fieldIndex));
            int accessFlags = reader.readSmallUleb128();
            out.annotateTo(reader.getOffset(), "access_flags = 0x%x: %s", accessFlags, Joiner.on('|').join((Object[])AccessFlags.getAccessFlagsForField(accessFlags)));
            return fieldIndex;
         }

         private int annotateEncodedMethod(@Nonnull AnnotatedBytes out, @Nonnull RawDexFile dexFile, @Nonnull DexReader reader, int previousIndex) {
            int indexDelta = reader.readLargeUleb128();
            int methodIndex = previousIndex + indexDelta;
            out.annotateTo(reader.getOffset(), "method_idx_diff = %d: %s", indexDelta, MethodIdItem.getReferenceAnnotation(dexFile, methodIndex));
            int accessFlags = reader.readSmallUleb128();
            out.annotateTo(reader.getOffset(), "access_flags = 0x%x: %s", accessFlags, Joiner.on('|').join((Object[])AccessFlags.getAccessFlagsForMethod(accessFlags)));
            int codeOffset = reader.readSmallUleb128();
            if (codeOffset == 0) {
               out.annotateTo(reader.getOffset(), "code_off = code_item[NO_OFFSET]");
            } else {
               out.annotateTo(reader.getOffset(), "code_off = code_item[0x%x]", codeOffset);
               this.addCodeItemIdentity(codeOffset, MethodIdItem.asString(dexFile, methodIndex));
            }

            return methodIndex;
         }

         private void addCodeItemIdentity(int codeItemOffset, String methodString) {
            if (this.codeItemAnnotator != null) {
               this.codeItemAnnotator.setItemIdentity(codeItemOffset, methodString);
            }

         }
      };
   }
}
