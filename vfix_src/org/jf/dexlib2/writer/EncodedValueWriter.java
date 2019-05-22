package org.jf.dexlib2.writer;

import com.google.common.collect.Ordering;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.BaseAnnotationElement;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;

public abstract class EncodedValueWriter<StringKey, TypeKey, FieldRefKey extends FieldReference, MethodRefKey extends MethodReference, AnnotationElement extends AnnotationElement, EncodedValue> {
   @Nonnull
   private final DexDataWriter writer;
   @Nonnull
   private final StringSection<StringKey, ?> stringSection;
   @Nonnull
   private final TypeSection<?, TypeKey, ?> typeSection;
   @Nonnull
   private final FieldSection<?, ?, FieldRefKey, ?> fieldSection;
   @Nonnull
   private final MethodSection<?, ?, ?, MethodRefKey, ?> methodSection;
   @Nonnull
   private final AnnotationSection<StringKey, TypeKey, ?, AnnotationElement, EncodedValue> annotationSection;

   public EncodedValueWriter(@Nonnull DexDataWriter writer, @Nonnull StringSection<StringKey, ?> stringSection, @Nonnull TypeSection<?, TypeKey, ?> typeSection, @Nonnull FieldSection<?, ?, FieldRefKey, ?> fieldSection, @Nonnull MethodSection<?, ?, ?, MethodRefKey, ?> methodSection, @Nonnull AnnotationSection<StringKey, TypeKey, ?, AnnotationElement, EncodedValue> annotationSection) {
      this.writer = writer;
      this.stringSection = stringSection;
      this.typeSection = typeSection;
      this.fieldSection = fieldSection;
      this.methodSection = methodSection;
      this.annotationSection = annotationSection;
   }

   protected abstract void writeEncodedValue(@Nonnull EncodedValue var1) throws IOException;

   public void writeAnnotation(TypeKey annotationType, Collection<? extends AnnotationElement> elements) throws IOException {
      this.writer.writeEncodedValueHeader(29, 0);
      this.writer.writeUleb128(this.typeSection.getItemIndex(annotationType));
      this.writer.writeUleb128(elements.size());
      Collection<? extends AnnotationElement> sortedElements = Ordering.from(BaseAnnotationElement.BY_NAME).immutableSortedCopy(elements);
      Iterator var4 = sortedElements.iterator();

      while(var4.hasNext()) {
         AnnotationElement element = (AnnotationElement)var4.next();
         this.writer.writeUleb128(this.stringSection.getItemIndex(this.annotationSection.getElementName(element)));
         this.writeEncodedValue(this.annotationSection.getElementValue(element));
      }

   }

   public void writeArray(Collection<? extends EncodedValue> elements) throws IOException {
      this.writer.writeEncodedValueHeader(28, 0);
      this.writer.writeUleb128(elements.size());
      Iterator var2 = elements.iterator();

      while(var2.hasNext()) {
         EncodedValue element = var2.next();
         this.writeEncodedValue(element);
      }

   }

   public void writeBoolean(boolean value) throws IOException {
      this.writer.writeEncodedValueHeader(31, value ? 1 : 0);
   }

   public void writeByte(byte value) throws IOException {
      this.writer.writeEncodedInt(0, value);
   }

   public void writeChar(char value) throws IOException {
      this.writer.writeEncodedUint(3, value);
   }

   public void writeDouble(double value) throws IOException {
      this.writer.writeEncodedDouble(17, value);
   }

   public void writeEnum(@Nonnull FieldRefKey value) throws IOException {
      this.writer.writeEncodedUint(27, this.fieldSection.getItemIndex(value));
   }

   public void writeField(@Nonnull FieldRefKey value) throws IOException {
      this.writer.writeEncodedUint(25, this.fieldSection.getItemIndex(value));
   }

   public void writeFloat(float value) throws IOException {
      this.writer.writeEncodedFloat(16, value);
   }

   public void writeInt(int value) throws IOException {
      this.writer.writeEncodedInt(4, value);
   }

   public void writeLong(long value) throws IOException {
      this.writer.writeEncodedLong(6, value);
   }

   public void writeMethod(@Nonnull MethodRefKey value) throws IOException {
      this.writer.writeEncodedUint(26, this.methodSection.getItemIndex(value));
   }

   public void writeNull() throws IOException {
      this.writer.write(30);
   }

   public void writeShort(int value) throws IOException {
      this.writer.writeEncodedInt(2, value);
   }

   public void writeString(@Nonnull StringKey value) throws IOException {
      this.writer.writeEncodedUint(23, this.stringSection.getItemIndex(value));
   }

   public void writeType(@Nonnull TypeKey value) throws IOException {
      this.writer.writeEncodedUint(24, this.typeSection.getItemIndex(value));
   }
}
