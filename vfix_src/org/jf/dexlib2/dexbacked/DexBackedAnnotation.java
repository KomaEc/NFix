package org.jf.dexlib2.dexbacked;

import java.util.Set;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.BaseAnnotation;
import org.jf.dexlib2.dexbacked.util.VariableSizeSet;

public class DexBackedAnnotation extends BaseAnnotation {
   @Nonnull
   public final DexBackedDexFile dexFile;
   public final int visibility;
   public final int typeIndex;
   private final int elementsOffset;

   public DexBackedAnnotation(@Nonnull DexBackedDexFile dexFile, int annotationOffset) {
      this.dexFile = dexFile;
      DexReader reader = dexFile.readerAt(annotationOffset);
      this.visibility = reader.readUbyte();
      this.typeIndex = reader.readSmallUleb128();
      this.elementsOffset = reader.getOffset();
   }

   public int getVisibility() {
      return this.visibility;
   }

   @Nonnull
   public String getType() {
      return this.dexFile.getType(this.typeIndex);
   }

   @Nonnull
   public Set<? extends DexBackedAnnotationElement> getElements() {
      DexReader reader = this.dexFile.readerAt(this.elementsOffset);
      int size = reader.readSmallUleb128();
      return new VariableSizeSet<DexBackedAnnotationElement>(this.dexFile, reader.getOffset(), size) {
         @Nonnull
         protected DexBackedAnnotationElement readNextItem(@Nonnull DexReader reader, int index) {
            return new DexBackedAnnotationElement(reader);
         }
      };
   }
}
