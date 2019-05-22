package org.jf.dexlib2.writer;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.Adler32;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.AccessFlags;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.base.BaseAnnotation;
import org.jf.dexlib2.base.BaseAnnotationElement;
import org.jf.dexlib2.builder.MutableMethodImplementation;
import org.jf.dexlib2.builder.instruction.BuilderInstruction31c;
import org.jf.dexlib2.dexbacked.raw.HeaderItem;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.TryBlock;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.debug.LineNumber;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.instruction.VariableRegisterInstruction;
import org.jf.dexlib2.iface.instruction.formats.ArrayPayload;
import org.jf.dexlib2.iface.instruction.formats.Instruction10t;
import org.jf.dexlib2.iface.instruction.formats.Instruction10x;
import org.jf.dexlib2.iface.instruction.formats.Instruction11n;
import org.jf.dexlib2.iface.instruction.formats.Instruction11x;
import org.jf.dexlib2.iface.instruction.formats.Instruction12x;
import org.jf.dexlib2.iface.instruction.formats.Instruction20bc;
import org.jf.dexlib2.iface.instruction.formats.Instruction20t;
import org.jf.dexlib2.iface.instruction.formats.Instruction21c;
import org.jf.dexlib2.iface.instruction.formats.Instruction21ih;
import org.jf.dexlib2.iface.instruction.formats.Instruction21lh;
import org.jf.dexlib2.iface.instruction.formats.Instruction21s;
import org.jf.dexlib2.iface.instruction.formats.Instruction21t;
import org.jf.dexlib2.iface.instruction.formats.Instruction22b;
import org.jf.dexlib2.iface.instruction.formats.Instruction22c;
import org.jf.dexlib2.iface.instruction.formats.Instruction22cs;
import org.jf.dexlib2.iface.instruction.formats.Instruction22s;
import org.jf.dexlib2.iface.instruction.formats.Instruction22t;
import org.jf.dexlib2.iface.instruction.formats.Instruction22x;
import org.jf.dexlib2.iface.instruction.formats.Instruction23x;
import org.jf.dexlib2.iface.instruction.formats.Instruction30t;
import org.jf.dexlib2.iface.instruction.formats.Instruction31c;
import org.jf.dexlib2.iface.instruction.formats.Instruction31i;
import org.jf.dexlib2.iface.instruction.formats.Instruction31t;
import org.jf.dexlib2.iface.instruction.formats.Instruction32x;
import org.jf.dexlib2.iface.instruction.formats.Instruction35c;
import org.jf.dexlib2.iface.instruction.formats.Instruction35mi;
import org.jf.dexlib2.iface.instruction.formats.Instruction35ms;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rc;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rmi;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rms;
import org.jf.dexlib2.iface.instruction.formats.Instruction45cc;
import org.jf.dexlib2.iface.instruction.formats.Instruction4rcc;
import org.jf.dexlib2.iface.instruction.formats.Instruction51l;
import org.jf.dexlib2.iface.instruction.formats.PackedSwitchPayload;
import org.jf.dexlib2.iface.instruction.formats.SparseSwitchPayload;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.dexlib2.util.InstructionUtil;
import org.jf.dexlib2.util.MethodUtil;
import org.jf.dexlib2.util.ReferenceUtil;
import org.jf.dexlib2.writer.io.DeferredOutputStream;
import org.jf.dexlib2.writer.io.DeferredOutputStreamFactory;
import org.jf.dexlib2.writer.io.DexDataStore;
import org.jf.dexlib2.writer.io.MemoryDeferredOutputStream;
import org.jf.dexlib2.writer.util.TryListBuilder;
import org.jf.util.CollectionUtils;
import org.jf.util.ExceptionWithContext;

public abstract class DexWriter<StringKey extends CharSequence, StringRef extends StringReference, TypeKey extends CharSequence, TypeRef extends TypeReference, ProtoRefKey extends MethodProtoReference, FieldRefKey extends FieldReference, MethodRefKey extends MethodReference, ClassKey extends Comparable<? super ClassKey>, AnnotationKey extends Annotation, AnnotationSetKey, TypeListKey, FieldKey, MethodKey, EncodedValue, AnnotationElement extends AnnotationElement, StringSectionType extends StringSection<StringKey, StringRef>, TypeSectionType extends TypeSection<StringKey, TypeKey, TypeRef>, ProtoSectionType extends ProtoSection<StringKey, TypeKey, ProtoRefKey, TypeListKey>, FieldSectionType extends FieldSection<StringKey, TypeKey, FieldRefKey, FieldKey>, MethodSectionType extends MethodSection<StringKey, TypeKey, ProtoRefKey, MethodRefKey, MethodKey>, ClassSectionType extends ClassSection<StringKey, TypeKey, TypeListKey, ClassKey, FieldKey, MethodKey, AnnotationSetKey, EncodedValue>, TypeListSectionType extends TypeListSection<TypeKey, TypeListKey>, AnnotationSectionType extends AnnotationSection<StringKey, TypeKey, AnnotationKey, AnnotationElement, EncodedValue>, AnnotationSetSectionType extends AnnotationSetSection<AnnotationKey, AnnotationSetKey>> {
   public static final int NO_INDEX = -1;
   public static final int NO_OFFSET = 0;
   protected final Opcodes opcodes;
   protected int stringIndexSectionOffset = 0;
   protected int typeSectionOffset = 0;
   protected int protoSectionOffset = 0;
   protected int fieldSectionOffset = 0;
   protected int methodSectionOffset = 0;
   protected int classIndexSectionOffset = 0;
   protected int stringDataSectionOffset = 0;
   protected int classDataSectionOffset = 0;
   protected int typeListSectionOffset = 0;
   protected int encodedArraySectionOffset = 0;
   protected int annotationSectionOffset = 0;
   protected int annotationSetSectionOffset = 0;
   protected int annotationSetRefSectionOffset = 0;
   protected int annotationDirectorySectionOffset = 0;
   protected int debugSectionOffset = 0;
   protected int codeSectionOffset = 0;
   protected int mapSectionOffset = 0;
   protected int numEncodedArrayItems = 0;
   protected int numAnnotationSetRefItems = 0;
   protected int numAnnotationDirectoryItems = 0;
   protected int numDebugInfoItems = 0;
   protected int numCodeItemItems = 0;
   protected int numClassDataItems = 0;
   public final StringSectionType stringSection;
   public final TypeSectionType typeSection;
   public final ProtoSectionType protoSection;
   public final FieldSectionType fieldSection;
   public final MethodSectionType methodSection;
   public final ClassSectionType classSection;
   public final TypeListSectionType typeListSection;
   public final AnnotationSectionType annotationSection;
   public final AnnotationSetSectionType annotationSetSection;
   private static Comparator<Entry> toStringKeyComparator = new Comparator<Entry>() {
      public int compare(Entry o1, Entry o2) {
         return o1.getKey().toString().compareTo(o2.getKey().toString());
      }
   };

   protected DexWriter(Opcodes opcodes) {
      this.opcodes = opcodes;
      DexWriter<StringKey, StringRef, TypeKey, TypeRef, ProtoRefKey, FieldRefKey, MethodRefKey, ClassKey, AnnotationKey, AnnotationSetKey, TypeListKey, FieldKey, MethodKey, EncodedValue, AnnotationElement, StringSectionType, TypeSectionType, ProtoSectionType, FieldSectionType, MethodSectionType, ClassSectionType, TypeListSectionType, AnnotationSectionType, AnnotationSetSectionType>.SectionProvider sectionProvider = this.getSectionProvider();
      this.stringSection = sectionProvider.getStringSection();
      this.typeSection = sectionProvider.getTypeSection();
      this.protoSection = sectionProvider.getProtoSection();
      this.fieldSection = sectionProvider.getFieldSection();
      this.methodSection = sectionProvider.getMethodSection();
      this.classSection = sectionProvider.getClassSection();
      this.typeListSection = sectionProvider.getTypeListSection();
      this.annotationSection = sectionProvider.getAnnotationSection();
      this.annotationSetSection = sectionProvider.getAnnotationSetSection();
   }

   @Nonnull
   protected abstract DexWriter<StringKey, StringRef, TypeKey, TypeRef, ProtoRefKey, FieldRefKey, MethodRefKey, ClassKey, AnnotationKey, AnnotationSetKey, TypeListKey, FieldKey, MethodKey, EncodedValue, AnnotationElement, StringSectionType, TypeSectionType, ProtoSectionType, FieldSectionType, MethodSectionType, ClassSectionType, TypeListSectionType, AnnotationSectionType, AnnotationSetSectionType>.SectionProvider getSectionProvider();

   protected abstract void writeEncodedValue(@Nonnull DexWriter<StringKey, StringRef, TypeKey, TypeRef, ProtoRefKey, FieldRefKey, MethodRefKey, ClassKey, AnnotationKey, AnnotationSetKey, TypeListKey, FieldKey, MethodKey, EncodedValue, AnnotationElement, StringSectionType, TypeSectionType, ProtoSectionType, FieldSectionType, MethodSectionType, ClassSectionType, TypeListSectionType, AnnotationSectionType, AnnotationSetSectionType>.InternalEncodedValueWriter var1, @Nonnull EncodedValue var2) throws IOException;

   private static <T extends Comparable<? super T>> Comparator<Entry<? extends T, ?>> comparableKeyComparator() {
      return new Comparator<Entry<? extends T, ?>>() {
         public int compare(Entry<? extends T, ?> o1, Entry<? extends T, ?> o2) {
            return ((Comparable)o1.getKey()).compareTo(o2.getKey());
         }
      };
   }

   private int getDataSectionOffset() {
      return 112 + this.stringSection.getItemCount() * 4 + this.typeSection.getItemCount() * 4 + this.protoSection.getItemCount() * 12 + this.fieldSection.getItemCount() * 8 + this.methodSection.getItemCount() * 8 + this.classSection.getItemCount() * 32;
   }

   @Nonnull
   public List<String> getMethodReferences() {
      List<String> methodReferences = Lists.newArrayList();
      Iterator var2 = this.methodSection.getItems().iterator();

      while(var2.hasNext()) {
         Entry<? extends MethodRefKey, Integer> methodReference = (Entry)var2.next();
         methodReferences.add(ReferenceUtil.getMethodDescriptor((MethodReference)methodReference.getKey()));
      }

      return methodReferences;
   }

   @Nonnull
   public List<String> getFieldReferences() {
      List<String> fieldReferences = Lists.newArrayList();
      Iterator var2 = this.fieldSection.getItems().iterator();

      while(var2.hasNext()) {
         Entry<? extends FieldRefKey, Integer> fieldReference = (Entry)var2.next();
         fieldReferences.add(ReferenceUtil.getFieldDescriptor((FieldReference)fieldReference.getKey()));
      }

      return fieldReferences;
   }

   @Nonnull
   public List<String> getTypeReferences() {
      List<String> classReferences = Lists.newArrayList();
      Iterator var2 = this.typeSection.getItems().iterator();

      while(var2.hasNext()) {
         Entry<? extends TypeKey, Integer> typeReference = (Entry)var2.next();
         classReferences.add(((CharSequence)typeReference.getKey()).toString());
      }

      return classReferences;
   }

   public boolean hasOverflowed() {
      return this.methodSection.getItemCount() > 65536 || this.typeSection.getItemCount() > 65536 || this.fieldSection.getItemCount() > 65536;
   }

   public void writeTo(@Nonnull DexDataStore dest) throws IOException {
      this.writeTo(dest, MemoryDeferredOutputStream.getFactory());
   }

   public void writeTo(@Nonnull DexDataStore dest, @Nonnull DeferredOutputStreamFactory tempFactory) throws IOException {
      try {
         int dataSectionOffset = this.getDataSectionOffset();
         DexDataWriter headerWriter = outputAt(dest, 0);
         DexDataWriter indexWriter = outputAt(dest, 112);
         DexDataWriter offsetWriter = outputAt(dest, dataSectionOffset);

         try {
            this.writeStrings(indexWriter, offsetWriter);
            this.writeTypes(indexWriter);
            this.writeTypeLists(offsetWriter);
            this.writeProtos(indexWriter);
            this.writeFields(indexWriter);
            this.writeMethods(indexWriter);
            this.writeEncodedArrays(offsetWriter);
            this.writeAnnotations(offsetWriter);
            this.writeAnnotationSets(offsetWriter);
            this.writeAnnotationSetRefs(offsetWriter);
            this.writeAnnotationDirectories(offsetWriter);
            this.writeDebugAndCodeItems(offsetWriter, tempFactory.makeDeferredOutputStream());
            this.writeClasses(indexWriter, offsetWriter);
            this.writeMapItem(offsetWriter);
            this.writeHeader(headerWriter, dataSectionOffset, offsetWriter.getPosition());
         } finally {
            headerWriter.close();
            indexWriter.close();
            offsetWriter.close();
         }

         this.updateSignature(dest);
         this.updateChecksum(dest);
      } finally {
         dest.close();
      }

   }

   private void updateSignature(@Nonnull DexDataStore dataStore) throws IOException {
      MessageDigest md;
      try {
         md = MessageDigest.getInstance("SHA-1");
      } catch (NoSuchAlgorithmException var8) {
         throw new RuntimeException(var8);
      }

      byte[] buffer = new byte[4096];
      InputStream input = dataStore.readAt(32);

      for(int bytesRead = input.read(buffer); bytesRead >= 0; bytesRead = input.read(buffer)) {
         md.update(buffer, 0, bytesRead);
      }

      byte[] signature = md.digest();
      if (signature.length != 20) {
         throw new RuntimeException("unexpected digest write: " + signature.length + " bytes");
      } else {
         OutputStream output = dataStore.outputAt(12);
         output.write(signature);
         output.close();
      }
   }

   private void updateChecksum(@Nonnull DexDataStore dataStore) throws IOException {
      Adler32 a32 = new Adler32();
      byte[] buffer = new byte[4096];
      InputStream input = dataStore.readAt(12);

      for(int bytesRead = input.read(buffer); bytesRead >= 0; bytesRead = input.read(buffer)) {
         a32.update(buffer, 0, bytesRead);
      }

      OutputStream output = dataStore.outputAt(8);
      DexDataWriter.writeInt(output, (int)a32.getValue());
      output.close();
   }

   private static DexDataWriter outputAt(DexDataStore dataStore, int filePosition) throws IOException {
      return new DexDataWriter(dataStore.outputAt(filePosition), filePosition);
   }

   private void writeStrings(@Nonnull DexDataWriter indexWriter, @Nonnull DexDataWriter offsetWriter) throws IOException {
      this.stringIndexSectionOffset = indexWriter.getPosition();
      this.stringDataSectionOffset = offsetWriter.getPosition();
      int index = 0;
      List<Entry<? extends StringKey, Integer>> stringEntries = Lists.newArrayList((Iterable)this.stringSection.getItems());
      Collections.sort(stringEntries, toStringKeyComparator);
      Iterator var5 = stringEntries.iterator();

      while(var5.hasNext()) {
         Entry<? extends StringKey, Integer> entry = (Entry)var5.next();
         entry.setValue(index++);
         indexWriter.writeInt(offsetWriter.getPosition());
         String stringValue = ((CharSequence)entry.getKey()).toString();
         offsetWriter.writeUleb128(stringValue.length());
         offsetWriter.writeString(stringValue);
         offsetWriter.write(0);
      }

   }

   private void writeTypes(@Nonnull DexDataWriter writer) throws IOException {
      this.typeSectionOffset = writer.getPosition();
      int index = 0;
      List<Entry<? extends TypeKey, Integer>> typeEntries = Lists.newArrayList((Iterable)this.typeSection.getItems());
      Collections.sort(typeEntries, toStringKeyComparator);
      Iterator var4 = typeEntries.iterator();

      while(var4.hasNext()) {
         Entry<? extends TypeKey, Integer> entry = (Entry)var4.next();
         entry.setValue(index++);
         writer.writeInt(this.stringSection.getItemIndex(this.typeSection.getString(entry.getKey())));
      }

   }

   private void writeProtos(@Nonnull DexDataWriter writer) throws IOException {
      this.protoSectionOffset = writer.getPosition();
      int index = 0;
      List<Entry<? extends ProtoRefKey, Integer>> protoEntries = Lists.newArrayList((Iterable)this.protoSection.getItems());
      Collections.sort(protoEntries, comparableKeyComparator());
      Iterator var4 = protoEntries.iterator();

      while(var4.hasNext()) {
         Entry<? extends ProtoRefKey, Integer> entry = (Entry)var4.next();
         entry.setValue(index++);
         ProtoRefKey key = (MethodProtoReference)entry.getKey();
         writer.writeInt(this.stringSection.getItemIndex(this.protoSection.getShorty(key)));
         writer.writeInt(this.typeSection.getItemIndex(this.protoSection.getReturnType(key)));
         writer.writeInt(this.typeListSection.getNullableItemOffset(this.protoSection.getParameters(key)));
      }

   }

   private void writeFields(@Nonnull DexDataWriter writer) throws IOException {
      this.fieldSectionOffset = writer.getPosition();
      int index = 0;
      List<Entry<? extends FieldRefKey, Integer>> fieldEntries = Lists.newArrayList((Iterable)this.fieldSection.getItems());
      Collections.sort(fieldEntries, comparableKeyComparator());
      Iterator var4 = fieldEntries.iterator();

      while(var4.hasNext()) {
         Entry<? extends FieldRefKey, Integer> entry = (Entry)var4.next();
         entry.setValue(index++);
         FieldRefKey key = (FieldReference)entry.getKey();
         writer.writeUshort(this.typeSection.getItemIndex(this.fieldSection.getDefiningClass(key)));
         writer.writeUshort(this.typeSection.getItemIndex(this.fieldSection.getFieldType(key)));
         writer.writeInt(this.stringSection.getItemIndex(this.fieldSection.getName(key)));
      }

   }

   private void writeMethods(@Nonnull DexDataWriter writer) throws IOException {
      this.methodSectionOffset = writer.getPosition();
      int index = 0;
      List<Entry<? extends MethodRefKey, Integer>> methodEntries = Lists.newArrayList((Iterable)this.methodSection.getItems());
      Collections.sort(methodEntries, comparableKeyComparator());
      Iterator var4 = methodEntries.iterator();

      while(var4.hasNext()) {
         Entry<? extends MethodRefKey, Integer> entry = (Entry)var4.next();
         entry.setValue(index++);
         MethodRefKey key = (MethodReference)entry.getKey();
         writer.writeUshort(this.typeSection.getItemIndex(this.methodSection.getDefiningClass(key)));
         writer.writeUshort(this.protoSection.getItemIndex(this.methodSection.getPrototype(key)));
         writer.writeInt(this.stringSection.getItemIndex(this.methodSection.getName(key)));
      }

   }

   private void writeClasses(@Nonnull DexDataWriter indexWriter, @Nonnull DexDataWriter offsetWriter) throws IOException {
      this.classIndexSectionOffset = indexWriter.getPosition();
      this.classDataSectionOffset = offsetWriter.getPosition();
      List<Entry<? extends ClassKey, Integer>> classEntries = Lists.newArrayList((Iterable)this.classSection.getItems());
      Collections.sort(classEntries, comparableKeyComparator());
      int index = 0;

      Entry key;
      for(Iterator var5 = classEntries.iterator(); var5.hasNext(); index = this.writeClass(indexWriter, offsetWriter, index, key)) {
         key = (Entry)var5.next();
      }

   }

   private int writeClass(@Nonnull DexDataWriter indexWriter, @Nonnull DexDataWriter offsetWriter, int nextIndex, @Nullable Entry<? extends ClassKey, Integer> entry) throws IOException {
      if (entry == null) {
         return nextIndex;
      } else if ((Integer)entry.getValue() != -1) {
         return nextIndex;
      } else {
         ClassKey key = (Comparable)entry.getKey();
         entry.setValue(0);
         Entry<? extends ClassKey, Integer> superEntry = this.classSection.getClassEntryByType(this.classSection.getSuperclass(key));
         nextIndex = this.writeClass(indexWriter, offsetWriter, nextIndex, superEntry);

         Entry interfaceEntry;
         for(Iterator var7 = this.typeListSection.getTypes(this.classSection.getInterfaces(key)).iterator(); var7.hasNext(); nextIndex = this.writeClass(indexWriter, offsetWriter, nextIndex, interfaceEntry)) {
            TypeKey interfaceTypeKey = (CharSequence)var7.next();
            interfaceEntry = this.classSection.getClassEntryByType(interfaceTypeKey);
         }

         entry.setValue(nextIndex++);
         indexWriter.writeInt(this.typeSection.getItemIndex(this.classSection.getType(key)));
         indexWriter.writeInt(this.classSection.getAccessFlags(key));
         indexWriter.writeInt(this.typeSection.getNullableItemIndex(this.classSection.getSuperclass(key)));
         indexWriter.writeInt(this.typeListSection.getNullableItemOffset(this.classSection.getInterfaces(key)));
         indexWriter.writeInt(this.stringSection.getNullableItemIndex(this.classSection.getSourceFile(key)));
         indexWriter.writeInt(this.classSection.getAnnotationDirectoryOffset(key));
         Collection<? extends FieldKey> staticFields = this.classSection.getSortedStaticFields(key);
         Collection<? extends FieldKey> instanceFields = this.classSection.getSortedInstanceFields(key);
         Collection<? extends MethodKey> directMethods = this.classSection.getSortedDirectMethods(key);
         Collection<? extends MethodKey> virtualMethods = this.classSection.getSortedVirtualMethods(key);
         boolean classHasData = staticFields.size() > 0 || instanceFields.size() > 0 || directMethods.size() > 0 || virtualMethods.size() > 0;
         if (classHasData) {
            indexWriter.writeInt(offsetWriter.getPosition());
         } else {
            indexWriter.writeInt(0);
         }

         indexWriter.writeInt(this.classSection.getEncodedArrayOffset(key));
         if (classHasData) {
            ++this.numClassDataItems;
            offsetWriter.writeUleb128(staticFields.size());
            offsetWriter.writeUleb128(instanceFields.size());
            offsetWriter.writeUleb128(directMethods.size());
            offsetWriter.writeUleb128(virtualMethods.size());
            this.writeEncodedFields(offsetWriter, staticFields);
            this.writeEncodedFields(offsetWriter, instanceFields);
            this.writeEncodedMethods(offsetWriter, directMethods);
            this.writeEncodedMethods(offsetWriter, virtualMethods);
         }

         return nextIndex;
      }
   }

   private void writeEncodedFields(@Nonnull DexDataWriter writer, @Nonnull Collection<? extends FieldKey> fields) throws IOException {
      int prevIndex = 0;

      int index;
      for(Iterator var4 = fields.iterator(); var4.hasNext(); prevIndex = index) {
         FieldKey key = var4.next();
         index = this.fieldSection.getFieldIndex(key);
         writer.writeUleb128(index - prevIndex);
         writer.writeUleb128(this.classSection.getFieldAccessFlags(key));
      }

   }

   private void writeEncodedMethods(@Nonnull DexDataWriter writer, @Nonnull Collection<? extends MethodKey> methods) throws IOException {
      int prevIndex = 0;

      int index;
      for(Iterator var4 = methods.iterator(); var4.hasNext(); prevIndex = index) {
         MethodKey key = var4.next();
         index = this.methodSection.getMethodIndex(key);
         writer.writeUleb128(index - prevIndex);
         writer.writeUleb128(this.classSection.getMethodAccessFlags(key));
         writer.writeUleb128(this.classSection.getCodeItemOffset(key));
      }

   }

   private void writeTypeLists(@Nonnull DexDataWriter writer) throws IOException {
      writer.align();
      this.typeListSectionOffset = writer.getPosition();
      Iterator var2 = this.typeListSection.getItems().iterator();

      while(var2.hasNext()) {
         Entry<? extends TypeListKey, Integer> entry = (Entry)var2.next();
         writer.align();
         entry.setValue(writer.getPosition());
         Collection<? extends TypeKey> types = this.typeListSection.getTypes(entry.getKey());
         writer.writeInt(types.size());
         Iterator var5 = types.iterator();

         while(var5.hasNext()) {
            TypeKey typeKey = (CharSequence)var5.next();
            writer.writeUshort(this.typeSection.getItemIndex(typeKey));
         }
      }

   }

   private void writeEncodedArrays(@Nonnull DexDataWriter writer) throws IOException {
      DexWriter<StringKey, StringRef, TypeKey, TypeRef, ProtoRefKey, FieldRefKey, MethodRefKey, ClassKey, AnnotationKey, AnnotationSetKey, TypeListKey, FieldKey, MethodKey, EncodedValue, AnnotationElement, StringSectionType, TypeSectionType, ProtoSectionType, FieldSectionType, MethodSectionType, ClassSectionType, TypeListSectionType, AnnotationSectionType, AnnotationSetSectionType>.InternalEncodedValueWriter encodedValueWriter = new DexWriter.InternalEncodedValueWriter(writer);
      this.encodedArraySectionOffset = writer.getPosition();
      HashMap<DexWriter.EncodedArrayKey<EncodedValue>, Integer> internedItems = Maps.newHashMap();
      DexWriter.EncodedArrayKey<EncodedValue> key = new DexWriter.EncodedArrayKey();
      Iterator var5 = this.classSection.getSortedClasses().iterator();

      while(true) {
         while(true) {
            Comparable classKey;
            Collection elements;
            do {
               do {
                  if (!var5.hasNext()) {
                     return;
                  }

                  classKey = (Comparable)var5.next();
                  elements = this.classSection.getStaticInitializers(classKey);
               } while(elements == null);
            } while(elements.size() <= 0);

            key.elements = elements;
            Integer prev = (Integer)internedItems.get(key);
            if (prev != null) {
               this.classSection.setEncodedArrayOffset(classKey, prev);
            } else {
               int offset = writer.getPosition();
               internedItems.put(key, offset);
               this.classSection.setEncodedArrayOffset(classKey, offset);
               key = new DexWriter.EncodedArrayKey();
               ++this.numEncodedArrayItems;
               writer.writeUleb128(elements.size());
               Iterator var10 = elements.iterator();

               while(var10.hasNext()) {
                  EncodedValue value = var10.next();
                  this.writeEncodedValue(encodedValueWriter, value);
               }
            }
         }
      }
   }

   private void writeAnnotations(@Nonnull DexDataWriter writer) throws IOException {
      DexWriter<StringKey, StringRef, TypeKey, TypeRef, ProtoRefKey, FieldRefKey, MethodRefKey, ClassKey, AnnotationKey, AnnotationSetKey, TypeListKey, FieldKey, MethodKey, EncodedValue, AnnotationElement, StringSectionType, TypeSectionType, ProtoSectionType, FieldSectionType, MethodSectionType, ClassSectionType, TypeListSectionType, AnnotationSectionType, AnnotationSetSectionType>.InternalEncodedValueWriter encodedValueWriter = new DexWriter.InternalEncodedValueWriter(writer);
      this.annotationSectionOffset = writer.getPosition();
      Iterator var3 = this.annotationSection.getItems().iterator();

      while(var3.hasNext()) {
         Entry<? extends AnnotationKey, Integer> entry = (Entry)var3.next();
         entry.setValue(writer.getPosition());
         AnnotationKey key = (Annotation)entry.getKey();
         writer.writeUbyte(this.annotationSection.getVisibility(key));
         writer.writeUleb128(this.typeSection.getItemIndex(this.annotationSection.getType(key)));
         Collection<? extends AnnotationElement> elements = Ordering.from(BaseAnnotationElement.BY_NAME).immutableSortedCopy(this.annotationSection.getElements(key));
         writer.writeUleb128(elements.size());
         Iterator var7 = elements.iterator();

         while(var7.hasNext()) {
            AnnotationElement element = (AnnotationElement)var7.next();
            writer.writeUleb128(this.stringSection.getItemIndex(this.annotationSection.getElementName(element)));
            this.writeEncodedValue(encodedValueWriter, this.annotationSection.getElementValue(element));
         }
      }

   }

   private void writeAnnotationSets(@Nonnull DexDataWriter writer) throws IOException {
      writer.align();
      this.annotationSetSectionOffset = writer.getPosition();
      if (this.shouldCreateEmptyAnnotationSet()) {
         writer.writeInt(0);
      }

      Iterator var2 = this.annotationSetSection.getItems().iterator();

      while(var2.hasNext()) {
         Entry<? extends AnnotationSetKey, Integer> entry = (Entry)var2.next();
         Collection<? extends AnnotationKey> annotations = Ordering.from(BaseAnnotation.BY_TYPE).immutableSortedCopy(this.annotationSetSection.getAnnotations(entry.getKey()));
         writer.align();
         entry.setValue(writer.getPosition());
         writer.writeInt(annotations.size());
         Iterator var5 = annotations.iterator();

         while(var5.hasNext()) {
            AnnotationKey annotationKey = (Annotation)var5.next();
            writer.writeInt(this.annotationSection.getItemOffset(annotationKey));
         }
      }

   }

   private void writeAnnotationSetRefs(@Nonnull DexDataWriter writer) throws IOException {
      writer.align();
      this.annotationSetRefSectionOffset = writer.getPosition();
      HashMap<List<? extends AnnotationSetKey>, Integer> internedItems = Maps.newHashMap();
      Iterator var3 = this.classSection.getSortedClasses().iterator();

      label44:
      while(var3.hasNext()) {
         ClassKey classKey = (Comparable)var3.next();
         Iterator var5 = this.classSection.getSortedMethods(classKey).iterator();

         while(true) {
            while(true) {
               Object methodKey;
               List parameterAnnotations;
               do {
                  if (!var5.hasNext()) {
                     continue label44;
                  }

                  methodKey = var5.next();
                  parameterAnnotations = this.classSection.getParameterAnnotations(methodKey);
               } while(parameterAnnotations == null);

               Integer prev = (Integer)internedItems.get(parameterAnnotations);
               if (prev != null) {
                  this.classSection.setAnnotationSetRefListOffset(methodKey, prev);
               } else {
                  writer.align();
                  int position = writer.getPosition();
                  this.classSection.setAnnotationSetRefListOffset(methodKey, position);
                  internedItems.put(parameterAnnotations, position);
                  ++this.numAnnotationSetRefItems;
                  writer.writeInt(parameterAnnotations.size());
                  Iterator var10 = parameterAnnotations.iterator();

                  while(var10.hasNext()) {
                     AnnotationSetKey annotationSetKey = var10.next();
                     if (this.annotationSetSection.getAnnotations(annotationSetKey).size() > 0) {
                        writer.writeInt(this.annotationSetSection.getItemOffset(annotationSetKey));
                     } else if (this.shouldCreateEmptyAnnotationSet()) {
                        writer.writeInt(this.annotationSetSectionOffset);
                     } else {
                        writer.writeInt(0);
                     }
                  }
               }
            }
         }
      }

   }

   private void writeAnnotationDirectories(@Nonnull DexDataWriter writer) throws IOException {
      writer.align();
      this.annotationDirectorySectionOffset = writer.getPosition();
      HashMap<AnnotationSetKey, Integer> internedItems = Maps.newHashMap();
      ByteBuffer tempBuffer = ByteBuffer.allocate(65536);
      tempBuffer.order(ByteOrder.LITTLE_ENDIAN);
      Iterator var4 = this.classSection.getSortedClasses().iterator();

      while(true) {
         while(var4.hasNext()) {
            ClassKey key = (Comparable)var4.next();
            Collection<? extends FieldKey> fields = this.classSection.getSortedFields(key);
            Collection<? extends MethodKey> methods = this.classSection.getSortedMethods(key);
            int maxSize = fields.size() * 8 + methods.size() * 16;
            if (maxSize > tempBuffer.capacity()) {
               tempBuffer = ByteBuffer.allocate(maxSize);
               tempBuffer.order(ByteOrder.LITTLE_ENDIAN);
            }

            tempBuffer.clear();
            int fieldAnnotations = 0;
            int methodAnnotations = 0;
            int parameterAnnotations = 0;
            Iterator var12 = fields.iterator();

            Object method;
            Object methodAnnotationsKey;
            while(var12.hasNext()) {
               method = var12.next();
               methodAnnotationsKey = this.classSection.getFieldAnnotations(method);
               if (methodAnnotationsKey != null) {
                  ++fieldAnnotations;
                  tempBuffer.putInt(this.fieldSection.getFieldIndex(method));
                  tempBuffer.putInt(this.annotationSetSection.getItemOffset(methodAnnotationsKey));
               }
            }

            var12 = methods.iterator();

            while(var12.hasNext()) {
               method = var12.next();
               methodAnnotationsKey = this.classSection.getMethodAnnotations(method);
               if (methodAnnotationsKey != null) {
                  ++methodAnnotations;
                  tempBuffer.putInt(this.methodSection.getMethodIndex(method));
                  tempBuffer.putInt(this.annotationSetSection.getItemOffset(methodAnnotationsKey));
               }
            }

            var12 = methods.iterator();

            while(var12.hasNext()) {
               method = var12.next();
               int offset = this.classSection.getAnnotationSetRefListOffset(method);
               if (offset != 0) {
                  ++parameterAnnotations;
                  tempBuffer.putInt(this.methodSection.getMethodIndex(method));
                  tempBuffer.putInt(offset);
               }
            }

            AnnotationSetKey classAnnotationKey = this.classSection.getClassAnnotations(key);
            if (fieldAnnotations == 0 && methodAnnotations == 0 && parameterAnnotations == 0) {
               if (classAnnotationKey == null) {
                  continue;
               }

               Integer directoryOffset = (Integer)internedItems.get(classAnnotationKey);
               if (directoryOffset != null) {
                  this.classSection.setAnnotationDirectoryOffset(key, directoryOffset);
                  continue;
               }

               internedItems.put(classAnnotationKey, writer.getPosition());
            }

            ++this.numAnnotationDirectoryItems;
            this.classSection.setAnnotationDirectoryOffset(key, writer.getPosition());
            writer.writeInt(this.annotationSetSection.getNullableItemOffset(classAnnotationKey));
            writer.writeInt(fieldAnnotations);
            writer.writeInt(methodAnnotations);
            writer.writeInt(parameterAnnotations);
            writer.write(tempBuffer.array(), 0, tempBuffer.position());
         }

         return;
      }
   }

   private void writeDebugAndCodeItems(@Nonnull DexDataWriter offsetWriter, @Nonnull DeferredOutputStream temp) throws IOException {
      ByteArrayOutputStream ehBuf = new ByteArrayOutputStream();
      this.debugSectionOffset = offsetWriter.getPosition();
      DebugWriter<StringKey, TypeKey> debugWriter = new DebugWriter(this.stringSection, this.typeSection, offsetWriter);
      DexDataWriter codeWriter = new DexDataWriter(temp, 0);
      List<DexWriter.CodeItemOffset<MethodKey>> codeOffsets = Lists.newArrayList();
      Iterator var7 = this.classSection.getSortedClasses().iterator();

      while(var7.hasNext()) {
         ClassKey classKey = (Comparable)var7.next();
         Collection<? extends MethodKey> directMethods = this.classSection.getSortedDirectMethods(classKey);
         Collection<? extends MethodKey> virtualMethods = this.classSection.getSortedVirtualMethods(classKey);
         Iterable<MethodKey> methods = Iterables.concat(directMethods, virtualMethods);
         Iterator var12 = methods.iterator();

         while(var12.hasNext()) {
            MethodKey methodKey = var12.next();
            List<? extends TryBlock<? extends ExceptionHandler>> tryBlocks = this.classSection.getTryBlocks(methodKey);
            Iterable<? extends Instruction> instructions = this.classSection.getInstructions(methodKey);
            Iterable<? extends DebugItem> debugItems = this.classSection.getDebugItems(methodKey);
            if (instructions != null && this.stringSection.hasJumboIndexes()) {
               boolean needsFix = false;
               Iterator var18 = ((Iterable)instructions).iterator();

               while(var18.hasNext()) {
                  Instruction instruction = (Instruction)var18.next();
                  if (instruction.getOpcode() == Opcode.CONST_STRING && this.stringSection.getItemIndex((StringReference)((ReferenceInstruction)instruction).getReference()) >= 65536) {
                     needsFix = true;
                     break;
                  }
               }

               if (needsFix) {
                  MutableMethodImplementation mutableMethodImplementation = this.classSection.makeMutableMethodImplementation(methodKey);
                  this.fixInstructions(mutableMethodImplementation);
                  instructions = mutableMethodImplementation.getInstructions();
                  tryBlocks = mutableMethodImplementation.getTryBlocks();
                  debugItems = mutableMethodImplementation.getDebugItems();
               }
            }

            int debugItemOffset = this.writeDebugItem(offsetWriter, debugWriter, this.classSection.getParameterNames(methodKey), debugItems);

            int codeItemOffset;
            try {
               codeItemOffset = this.writeCodeItem(codeWriter, ehBuf, methodKey, tryBlocks, (Iterable)instructions, debugItemOffset);
            } catch (RuntimeException var20) {
               throw new ExceptionWithContext(var20, "Exception occurred while writing code_item for method %s", new Object[]{this.methodSection.getMethodReference(methodKey)});
            }

            if (codeItemOffset != -1) {
               codeOffsets.add(new DexWriter.CodeItemOffset(methodKey, codeItemOffset));
            }
         }
      }

      offsetWriter.align();
      this.codeSectionOffset = offsetWriter.getPosition();
      codeWriter.close();
      temp.writeTo(offsetWriter);
      temp.close();
      var7 = codeOffsets.iterator();

      while(var7.hasNext()) {
         DexWriter.CodeItemOffset<MethodKey> codeOffset = (DexWriter.CodeItemOffset)var7.next();
         this.classSection.setCodeItemOffset(codeOffset.method, this.codeSectionOffset + codeOffset.codeOffset);
      }

   }

   private void fixInstructions(@Nonnull MutableMethodImplementation methodImplementation) {
      List<? extends Instruction> instructions = methodImplementation.getInstructions();

      for(int i = 0; i < instructions.size(); ++i) {
         Instruction instruction = (Instruction)instructions.get(i);
         if (instruction.getOpcode() == Opcode.CONST_STRING && this.stringSection.getItemIndex((StringReference)((ReferenceInstruction)instruction).getReference()) >= 65536) {
            methodImplementation.replaceInstruction(i, new BuilderInstruction31c(Opcode.CONST_STRING_JUMBO, ((OneRegisterInstruction)instruction).getRegisterA(), ((ReferenceInstruction)instruction).getReference()));
         }
      }

   }

   private int writeDebugItem(@Nonnull DexDataWriter writer, @Nonnull DebugWriter<StringKey, TypeKey> debugWriter, @Nullable Iterable<? extends StringKey> parameterNames, @Nullable Iterable<? extends DebugItem> debugItems) throws IOException {
      int parameterCount = 0;
      int lastNamedParameterIndex = -1;
      int debugItemOffset;
      if (parameterNames != null) {
         parameterCount = Iterables.size(parameterNames);
         debugItemOffset = 0;

         for(Iterator var8 = parameterNames.iterator(); var8.hasNext(); ++debugItemOffset) {
            StringKey parameterName = (CharSequence)var8.next();
            if (parameterName != null) {
               lastNamedParameterIndex = debugItemOffset;
            }
         }
      }

      if (lastNamedParameterIndex == -1 && (debugItems == null || Iterables.isEmpty(debugItems))) {
         return 0;
      } else {
         ++this.numDebugInfoItems;
         debugItemOffset = writer.getPosition();
         int startingLineNumber = 0;
         DebugItem debugItem;
         Iterator var13;
         if (debugItems != null) {
            var13 = debugItems.iterator();

            while(var13.hasNext()) {
               debugItem = (DebugItem)var13.next();
               if (debugItem instanceof LineNumber) {
                  startingLineNumber = ((LineNumber)debugItem).getLineNumber();
                  break;
               }
            }
         }

         writer.writeUleb128(startingLineNumber);
         writer.writeUleb128(parameterCount);
         if (parameterNames != null) {
            int index = 0;
            Iterator var15 = parameterNames.iterator();

            while(var15.hasNext()) {
               StringKey parameterName = (CharSequence)var15.next();
               if (index == parameterCount) {
                  break;
               }

               ++index;
               writer.writeUleb128(this.stringSection.getNullableItemIndex(parameterName) + 1);
            }
         }

         if (debugItems != null) {
            debugWriter.reset(startingLineNumber);
            var13 = debugItems.iterator();

            while(var13.hasNext()) {
               debugItem = (DebugItem)var13.next();
               this.classSection.writeDebugItem(debugWriter, debugItem);
            }
         }

         writer.write(0);
         return debugItemOffset;
      }
   }

   private int writeCodeItem(@Nonnull DexDataWriter writer, @Nonnull ByteArrayOutputStream ehBuf, @Nonnull MethodKey methodKey, @Nonnull List<? extends TryBlock<? extends ExceptionHandler>> tryBlocks, @Nullable Iterable<? extends Instruction> instructions, int debugItemOffset) throws IOException {
      if (instructions == null && debugItemOffset == 0) {
         return -1;
      } else {
         ++this.numCodeItemItems;
         writer.align();
         int codeItemOffset = writer.getPosition();
         writer.writeUshort(this.classSection.getRegisterCount(methodKey));
         boolean isStatic = AccessFlags.STATIC.isSet(this.classSection.getMethodAccessFlags(methodKey));
         Collection<? extends TypeKey> parameters = this.typeListSection.getTypes(this.protoSection.getParameters(this.methodSection.getPrototype(methodKey)));
         writer.writeUshort(MethodUtil.getParameterRegisterCount(parameters, isStatic));
         if (instructions != null) {
            tryBlocks = TryListBuilder.massageTryBlocks(tryBlocks);
            int outParamCount = 0;
            int codeUnitCount = 0;
            Iterator var12 = instructions.iterator();

            int startAddress;
            while(var12.hasNext()) {
               Instruction instruction = (Instruction)var12.next();
               codeUnitCount += instruction.getCodeUnits();
               if (instruction.getOpcode().referenceType == 3) {
                  ReferenceInstruction refInsn = (ReferenceInstruction)instruction;
                  MethodReference methodRef = (MethodReference)refInsn.getReference();
                  Opcode opcode = instruction.getOpcode();
                  if (InstructionUtil.isInvokePolymorphic(opcode)) {
                     startAddress = ((VariableRegisterInstruction)instruction).getRegisterCount();
                  } else {
                     startAddress = MethodUtil.getParameterRegisterCount(methodRef, InstructionUtil.isInvokeStatic(opcode));
                  }

                  if (startAddress > outParamCount) {
                     outParamCount = startAddress;
                  }
               }
            }

            writer.writeUshort(outParamCount);
            writer.writeUshort(tryBlocks.size());
            writer.writeInt(debugItemOffset);
            InstructionWriter instructionWriter = InstructionWriter.makeInstructionWriter(this.opcodes, writer, this.stringSection, this.typeSection, this.fieldSection, this.methodSection, this.protoSection);
            writer.writeInt(codeUnitCount);
            int codeOffset = 0;

            Instruction instruction;
            for(Iterator var30 = instructions.iterator(); var30.hasNext(); codeOffset += instruction.getCodeUnits()) {
               instruction = (Instruction)var30.next();

               try {
                  switch(instruction.getOpcode().format) {
                  case Format10t:
                     instructionWriter.write((Instruction10t)instruction);
                     break;
                  case Format10x:
                     instructionWriter.write((Instruction10x)instruction);
                     break;
                  case Format11n:
                     instructionWriter.write((Instruction11n)instruction);
                     break;
                  case Format11x:
                     instructionWriter.write((Instruction11x)instruction);
                     break;
                  case Format12x:
                     instructionWriter.write((Instruction12x)instruction);
                     break;
                  case Format20bc:
                     instructionWriter.write((Instruction20bc)instruction);
                     break;
                  case Format20t:
                     instructionWriter.write((Instruction20t)instruction);
                     break;
                  case Format21c:
                     instructionWriter.write((Instruction21c)instruction);
                     break;
                  case Format21ih:
                     instructionWriter.write((Instruction21ih)instruction);
                     break;
                  case Format21lh:
                     instructionWriter.write((Instruction21lh)instruction);
                     break;
                  case Format21s:
                     instructionWriter.write((Instruction21s)instruction);
                     break;
                  case Format21t:
                     instructionWriter.write((Instruction21t)instruction);
                     break;
                  case Format22b:
                     instructionWriter.write((Instruction22b)instruction);
                     break;
                  case Format22c:
                     instructionWriter.write((Instruction22c)instruction);
                     break;
                  case Format22cs:
                     instructionWriter.write((Instruction22cs)instruction);
                     break;
                  case Format22s:
                     instructionWriter.write((Instruction22s)instruction);
                     break;
                  case Format22t:
                     instructionWriter.write((Instruction22t)instruction);
                     break;
                  case Format22x:
                     instructionWriter.write((Instruction22x)instruction);
                     break;
                  case Format23x:
                     instructionWriter.write((Instruction23x)instruction);
                     break;
                  case Format30t:
                     instructionWriter.write((Instruction30t)instruction);
                     break;
                  case Format31c:
                     instructionWriter.write((Instruction31c)instruction);
                     break;
                  case Format31i:
                     instructionWriter.write((Instruction31i)instruction);
                     break;
                  case Format31t:
                     instructionWriter.write((Instruction31t)instruction);
                     break;
                  case Format32x:
                     instructionWriter.write((Instruction32x)instruction);
                     break;
                  case Format35c:
                     instructionWriter.write((Instruction35c)instruction);
                     break;
                  case Format35mi:
                     instructionWriter.write((Instruction35mi)instruction);
                     break;
                  case Format35ms:
                     instructionWriter.write((Instruction35ms)instruction);
                     break;
                  case Format3rc:
                     instructionWriter.write((Instruction3rc)instruction);
                     break;
                  case Format3rmi:
                     instructionWriter.write((Instruction3rmi)instruction);
                     break;
                  case Format3rms:
                     instructionWriter.write((Instruction3rms)instruction);
                     break;
                  case Format45cc:
                     instructionWriter.write((Instruction45cc)instruction);
                     break;
                  case Format4rcc:
                     instructionWriter.write((Instruction4rcc)instruction);
                     break;
                  case Format51l:
                     instructionWriter.write((Instruction51l)instruction);
                     break;
                  case ArrayPayload:
                     instructionWriter.write((ArrayPayload)instruction);
                     break;
                  case PackedSwitchPayload:
                     instructionWriter.write((PackedSwitchPayload)instruction);
                     break;
                  case SparseSwitchPayload:
                     instructionWriter.write((SparseSwitchPayload)instruction);
                     break;
                  default:
                     throw new ExceptionWithContext("Unsupported instruction format: %s", new Object[]{instruction.getOpcode().format});
                  }
               } catch (RuntimeException var27) {
                  throw new ExceptionWithContext(var27, "Error while writing instruction at code offset 0x%x", new Object[]{codeOffset});
               }
            }

            if (tryBlocks.size() > 0) {
               writer.align();
               Map<List<? extends ExceptionHandler>, Integer> exceptionHandlerOffsetMap = Maps.newHashMap();
               Iterator var33 = tryBlocks.iterator();

               TryBlock tryBlock;
               while(var33.hasNext()) {
                  tryBlock = (TryBlock)var33.next();
                  exceptionHandlerOffsetMap.put(tryBlock.getExceptionHandlers(), 0);
               }

               DexDataWriter.writeUleb128(ehBuf, exceptionHandlerOffsetMap.size());
               var33 = tryBlocks.iterator();

               while(true) {
                  while(var33.hasNext()) {
                     tryBlock = (TryBlock)var33.next();
                     startAddress = tryBlock.getStartCodeAddress();
                     int endAddress = startAddress + tryBlock.getCodeUnitCount();
                     int tbCodeUnitCount = endAddress - startAddress;
                     writer.writeInt(startAddress);
                     writer.writeUshort(tbCodeUnitCount);
                     if (tryBlock.getExceptionHandlers().size() == 0) {
                        throw new ExceptionWithContext("No exception handlers for the try block!", new Object[0]);
                     }

                     Integer offset = (Integer)exceptionHandlerOffsetMap.get(tryBlock.getExceptionHandlers());
                     if (offset != 0) {
                        writer.writeUshort(offset);
                     } else {
                        offset = ehBuf.size();
                        writer.writeUshort(offset);
                        exceptionHandlerOffsetMap.put(tryBlock.getExceptionHandlers(), offset);
                        int ehSize = tryBlock.getExceptionHandlers().size();
                        ExceptionHandler ehLast = (ExceptionHandler)tryBlock.getExceptionHandlers().get(ehSize - 1);
                        if (ehLast.getExceptionType() == null) {
                           ehSize = ehSize * -1 + 1;
                        }

                        DexDataWriter.writeSleb128(ehBuf, ehSize);
                        Iterator var23 = tryBlock.getExceptionHandlers().iterator();

                        while(var23.hasNext()) {
                           ExceptionHandler eh = (ExceptionHandler)var23.next();
                           TypeKey exceptionTypeKey = this.classSection.getExceptionType(eh);
                           int codeAddress = eh.getHandlerCodeAddress();
                           if (exceptionTypeKey != null) {
                              DexDataWriter.writeUleb128(ehBuf, this.typeSection.getItemIndex(exceptionTypeKey));
                              DexDataWriter.writeUleb128(ehBuf, codeAddress);
                           } else {
                              DexDataWriter.writeUleb128(ehBuf, codeAddress);
                           }
                        }
                     }
                  }

                  if (ehBuf.size() > 0) {
                     ehBuf.writeTo(writer);
                     ehBuf.reset();
                  }
                  break;
               }
            }
         } else {
            writer.writeUshort(0);
            writer.writeUshort(0);
            writer.writeInt(debugItemOffset);
            writer.writeInt(0);
         }

         return codeItemOffset;
      }
   }

   private int calcNumItems() {
      int numItems = 0;
      int numItems = numItems + 1;
      if (this.stringSection.getItems().size() > 0) {
         numItems += 2;
      }

      if (this.typeSection.getItems().size() > 0) {
         ++numItems;
      }

      if (this.protoSection.getItems().size() > 0) {
         ++numItems;
      }

      if (this.fieldSection.getItems().size() > 0) {
         ++numItems;
      }

      if (this.methodSection.getItems().size() > 0) {
         ++numItems;
      }

      if (this.typeListSection.getItems().size() > 0) {
         ++numItems;
      }

      if (this.numEncodedArrayItems > 0) {
         ++numItems;
      }

      if (this.annotationSection.getItems().size() > 0) {
         ++numItems;
      }

      if (this.annotationSetSection.getItems().size() > 0 || this.shouldCreateEmptyAnnotationSet()) {
         ++numItems;
      }

      if (this.numAnnotationSetRefItems > 0) {
         ++numItems;
      }

      if (this.numAnnotationDirectoryItems > 0) {
         ++numItems;
      }

      if (this.numDebugInfoItems > 0) {
         ++numItems;
      }

      if (this.numCodeItemItems > 0) {
         ++numItems;
      }

      if (this.classSection.getItems().size() > 0) {
         ++numItems;
      }

      if (this.numClassDataItems > 0) {
         ++numItems;
      }

      ++numItems;
      return numItems;
   }

   private void writeMapItem(@Nonnull DexDataWriter writer) throws IOException {
      writer.align();
      this.mapSectionOffset = writer.getPosition();
      int numItems = this.calcNumItems();
      writer.writeInt(numItems);
      this.writeMapItem(writer, 0, 1, 0);
      this.writeMapItem(writer, 1, this.stringSection.getItems().size(), this.stringIndexSectionOffset);
      this.writeMapItem(writer, 2, this.typeSection.getItems().size(), this.typeSectionOffset);
      this.writeMapItem(writer, 3, this.protoSection.getItems().size(), this.protoSectionOffset);
      this.writeMapItem(writer, 4, this.fieldSection.getItems().size(), this.fieldSectionOffset);
      this.writeMapItem(writer, 5, this.methodSection.getItems().size(), this.methodSectionOffset);
      this.writeMapItem(writer, 6, this.classSection.getItems().size(), this.classIndexSectionOffset);
      this.writeMapItem(writer, 8194, this.stringSection.getItems().size(), this.stringDataSectionOffset);
      this.writeMapItem(writer, 4097, this.typeListSection.getItems().size(), this.typeListSectionOffset);
      this.writeMapItem(writer, 8197, this.numEncodedArrayItems, this.encodedArraySectionOffset);
      this.writeMapItem(writer, 8196, this.annotationSection.getItems().size(), this.annotationSectionOffset);
      this.writeMapItem(writer, 4099, this.annotationSetSection.getItems().size() + (this.shouldCreateEmptyAnnotationSet() ? 1 : 0), this.annotationSetSectionOffset);
      this.writeMapItem(writer, 4098, this.numAnnotationSetRefItems, this.annotationSetRefSectionOffset);
      this.writeMapItem(writer, 8198, this.numAnnotationDirectoryItems, this.annotationDirectorySectionOffset);
      this.writeMapItem(writer, 8195, this.numDebugInfoItems, this.debugSectionOffset);
      this.writeMapItem(writer, 8193, this.numCodeItemItems, this.codeSectionOffset);
      this.writeMapItem(writer, 8192, this.numClassDataItems, this.classDataSectionOffset);
      this.writeMapItem(writer, 4096, 1, this.mapSectionOffset);
   }

   private void writeMapItem(@Nonnull DexDataWriter writer, int type, int size, int offset) throws IOException {
      if (size > 0) {
         writer.writeUshort(type);
         writer.writeUshort(0);
         writer.writeInt(size);
         writer.writeInt(offset);
      }

   }

   private void writeHeader(@Nonnull DexDataWriter writer, int dataOffset, int fileSize) throws IOException {
      writer.write(HeaderItem.getMagicForApi(this.opcodes.api));
      writer.writeInt(0);
      writer.write(new byte[20]);
      writer.writeInt(fileSize);
      writer.writeInt(112);
      writer.writeInt(305419896);
      writer.writeInt(0);
      writer.writeInt(0);
      writer.writeInt(this.mapSectionOffset);
      this.writeSectionInfo(writer, this.stringSection.getItems().size(), this.stringIndexSectionOffset);
      this.writeSectionInfo(writer, this.typeSection.getItems().size(), this.typeSectionOffset);
      this.writeSectionInfo(writer, this.protoSection.getItems().size(), this.protoSectionOffset);
      this.writeSectionInfo(writer, this.fieldSection.getItems().size(), this.fieldSectionOffset);
      this.writeSectionInfo(writer, this.methodSection.getItems().size(), this.methodSectionOffset);
      this.writeSectionInfo(writer, this.classSection.getItems().size(), this.classIndexSectionOffset);
      writer.writeInt(fileSize - dataOffset);
      writer.writeInt(dataOffset);
   }

   private void writeSectionInfo(DexDataWriter writer, int numItems, int offset) throws IOException {
      writer.writeInt(numItems);
      if (numItems > 0) {
         writer.writeInt(offset);
      } else {
         writer.writeInt(0);
      }

   }

   private boolean shouldCreateEmptyAnnotationSet() {
      return this.opcodes.api < 17;
   }

   public abstract class SectionProvider {
      @Nonnull
      public abstract StringSectionType getStringSection();

      @Nonnull
      public abstract TypeSectionType getTypeSection();

      @Nonnull
      public abstract ProtoSectionType getProtoSection();

      @Nonnull
      public abstract FieldSectionType getFieldSection();

      @Nonnull
      public abstract MethodSectionType getMethodSection();

      @Nonnull
      public abstract ClassSectionType getClassSection();

      @Nonnull
      public abstract TypeListSectionType getTypeListSection();

      @Nonnull
      public abstract AnnotationSectionType getAnnotationSection();

      @Nonnull
      public abstract AnnotationSetSectionType getAnnotationSetSection();
   }

   private static class CodeItemOffset<MethodKey> {
      @Nonnull
      MethodKey method;
      int codeOffset;

      private CodeItemOffset(@Nonnull MethodKey method, int codeOffset) {
         this.codeOffset = codeOffset;
         this.method = method;
      }

      // $FF: synthetic method
      CodeItemOffset(Object x0, int x1, Object x2) {
         this(x0, x1);
      }
   }

   private static class EncodedArrayKey<EncodedValue> {
      @Nonnull
      Collection<? extends EncodedValue> elements;

      public EncodedArrayKey() {
      }

      public int hashCode() {
         return CollectionUtils.listHashCode(this.elements);
      }

      public boolean equals(Object o) {
         if (o instanceof DexWriter.EncodedArrayKey) {
            DexWriter.EncodedArrayKey other = (DexWriter.EncodedArrayKey)o;
            return this.elements.size() != other.elements.size() ? false : Iterables.elementsEqual(this.elements, other.elements);
         } else {
            return false;
         }
      }
   }

   protected class InternalEncodedValueWriter extends EncodedValueWriter<StringKey, TypeKey, FieldRefKey, MethodRefKey, AnnotationElement, EncodedValue> {
      private InternalEncodedValueWriter(@Nonnull DexDataWriter writer) {
         super(writer, DexWriter.this.stringSection, DexWriter.this.typeSection, DexWriter.this.fieldSection, DexWriter.this.methodSection, DexWriter.this.annotationSection);
      }

      protected void writeEncodedValue(@Nonnull EncodedValue encodedValue) throws IOException {
         DexWriter.this.writeEncodedValue(this, encodedValue);
      }

      // $FF: synthetic method
      InternalEncodedValueWriter(DexDataWriter x1, Object x2) {
         this(x1);
      }
   }
}
