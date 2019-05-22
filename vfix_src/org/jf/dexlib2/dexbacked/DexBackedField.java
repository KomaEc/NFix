package org.jf.dexlib2.dexbacked;

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseFieldReference;
import org.jf.dexlib2.dexbacked.reference.DexBackedFieldReference;
import org.jf.dexlib2.dexbacked.util.AnnotationsDirectory;
import org.jf.dexlib2.dexbacked.util.StaticInitialValueIterator;
import org.jf.dexlib2.dexbacked.value.DexBackedEncodedValue;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.value.EncodedValue;

public class DexBackedField extends BaseFieldReference implements Field {
   @Nonnull
   public final DexBackedDexFile dexFile;
   @Nonnull
   public final ClassDef classDef;
   public final int accessFlags;
   @Nullable
   public final EncodedValue initialValue;
   public final int annotationSetOffset;
   public final int fieldIndex;
   private final int startOffset;
   private final int initialValueOffset;
   private int fieldIdItemOffset;

   public DexBackedField(@Nonnull DexReader reader, @Nonnull DexBackedClassDef classDef, int previousFieldIndex, @Nonnull StaticInitialValueIterator staticInitialValueIterator, @Nonnull AnnotationsDirectory.AnnotationIterator annotationIterator) {
      this.dexFile = (DexBackedDexFile)reader.dexBuf;
      this.classDef = classDef;
      this.startOffset = reader.getOffset();
      int fieldIndexDiff = reader.readLargeUleb128();
      this.fieldIndex = fieldIndexDiff + previousFieldIndex;
      this.accessFlags = reader.readSmallUleb128();
      this.annotationSetOffset = annotationIterator.seekTo(this.fieldIndex);
      this.initialValueOffset = staticInitialValueIterator.getReaderOffset();
      this.initialValue = staticInitialValueIterator.getNextOrNull();
   }

   public DexBackedField(@Nonnull DexReader reader, @Nonnull DexBackedClassDef classDef, int previousFieldIndex, @Nonnull AnnotationsDirectory.AnnotationIterator annotationIterator) {
      this.dexFile = (DexBackedDexFile)reader.dexBuf;
      this.classDef = classDef;
      this.startOffset = reader.getOffset();
      int fieldIndexDiff = reader.readLargeUleb128();
      this.fieldIndex = fieldIndexDiff + previousFieldIndex;
      this.accessFlags = reader.readSmallUleb128();
      this.annotationSetOffset = annotationIterator.seekTo(this.fieldIndex);
      this.initialValueOffset = 0;
      this.initialValue = null;
   }

   @Nonnull
   public String getName() {
      return this.dexFile.getString(this.dexFile.readSmallUint(this.getFieldIdItemOffset() + 4));
   }

   @Nonnull
   public String getType() {
      return this.dexFile.getType(this.dexFile.readUshort(this.getFieldIdItemOffset() + 2));
   }

   @Nonnull
   public String getDefiningClass() {
      return this.classDef.getType();
   }

   public int getAccessFlags() {
      return this.accessFlags;
   }

   @Nullable
   public EncodedValue getInitialValue() {
      return this.initialValue;
   }

   @Nonnull
   public Set<? extends DexBackedAnnotation> getAnnotations() {
      return AnnotationsDirectory.getAnnotations(this.dexFile, this.annotationSetOffset);
   }

   public static void skipFields(@Nonnull DexReader reader, int count) {
      for(int i = 0; i < count; ++i) {
         reader.skipUleb128();
         reader.skipUleb128();
      }

   }

   private int getFieldIdItemOffset() {
      if (this.fieldIdItemOffset == 0) {
         this.fieldIdItemOffset = this.dexFile.getFieldIdItemOffset(this.fieldIndex);
      }

      return this.fieldIdItemOffset;
   }

   public int getSize() {
      int size = 0;
      DexReader reader = this.dexFile.readerAt(this.startOffset);
      reader.readLargeUleb128();
      reader.readSmallUleb128();
      int size = size + (reader.getOffset() - this.startOffset);
      Set<? extends DexBackedAnnotation> annotations = this.getAnnotations();
      if (!annotations.isEmpty()) {
         size += 8;
      }

      if (this.initialValueOffset > 0) {
         reader.setOffset(this.initialValueOffset);
         if (this.initialValue != null) {
            DexBackedEncodedValue.skipFrom(reader);
            size += reader.getOffset() - this.initialValueOffset;
         }
      }

      DexBackedFieldReference fieldRef = new DexBackedFieldReference(this.dexFile, this.fieldIndex);
      size += fieldRef.getSize();
      return size;
   }
}
