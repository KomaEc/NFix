package org.jf.dexlib2.writer.pool;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.dexlib2.iface.value.AnnotationEncodedValue;
import org.jf.dexlib2.iface.value.ArrayEncodedValue;
import org.jf.dexlib2.iface.value.BooleanEncodedValue;
import org.jf.dexlib2.iface.value.ByteEncodedValue;
import org.jf.dexlib2.iface.value.CharEncodedValue;
import org.jf.dexlib2.iface.value.DoubleEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.EnumEncodedValue;
import org.jf.dexlib2.iface.value.FieldEncodedValue;
import org.jf.dexlib2.iface.value.FloatEncodedValue;
import org.jf.dexlib2.iface.value.IntEncodedValue;
import org.jf.dexlib2.iface.value.LongEncodedValue;
import org.jf.dexlib2.iface.value.MethodEncodedValue;
import org.jf.dexlib2.iface.value.ShortEncodedValue;
import org.jf.dexlib2.iface.value.StringEncodedValue;
import org.jf.dexlib2.iface.value.TypeEncodedValue;
import org.jf.dexlib2.writer.DexWriter;
import org.jf.dexlib2.writer.io.DexDataStore;
import org.jf.dexlib2.writer.io.FileDataStore;
import org.jf.util.ExceptionWithContext;

public class DexPool extends DexWriter<CharSequence, StringReference, CharSequence, TypeReference, MethodProtoReference, FieldReference, MethodReference, PoolClassDef, Annotation, Set<? extends Annotation>, TypeListPool.Key<? extends Collection<? extends CharSequence>>, Field, PoolMethod, EncodedValue, AnnotationElement, StringPool, TypePool, ProtoPool, FieldPool, MethodPool, ClassPool, TypeListPool, AnnotationPool, AnnotationSetPool> {
   private final Markable[] sections;

   public DexPool(Opcodes opcodes) {
      super(opcodes);
      this.sections = new Markable[]{(Markable)this.stringSection, (Markable)this.typeSection, (Markable)this.protoSection, (Markable)this.fieldSection, (Markable)this.methodSection, (Markable)this.classSection, (Markable)this.typeListSection, (Markable)this.annotationSection, (Markable)this.annotationSetSection};
   }

   @Nonnull
   protected DexWriter<CharSequence, StringReference, CharSequence, TypeReference, MethodProtoReference, FieldReference, MethodReference, PoolClassDef, Annotation, Set<? extends Annotation>, TypeListPool.Key<? extends Collection<? extends CharSequence>>, Field, PoolMethod, EncodedValue, AnnotationElement, StringPool, TypePool, ProtoPool, FieldPool, MethodPool, ClassPool, TypeListPool, AnnotationPool, AnnotationSetPool>.SectionProvider getSectionProvider() {
      return new DexPool.DexPoolSectionProvider();
   }

   public static void writeTo(@Nonnull DexDataStore dataStore, @Nonnull DexFile input) throws IOException {
      DexPool dexPool = new DexPool(input.getOpcodes());
      Iterator var3 = input.getClasses().iterator();

      while(var3.hasNext()) {
         ClassDef classDef = (ClassDef)var3.next();
         dexPool.internClass(classDef);
      }

      dexPool.writeTo(dataStore);
   }

   public static void writeTo(@Nonnull String path, @Nonnull DexFile input) throws IOException {
      DexPool dexPool = new DexPool(input.getOpcodes());
      Iterator var3 = input.getClasses().iterator();

      while(var3.hasNext()) {
         ClassDef classDef = (ClassDef)var3.next();
         dexPool.internClass(classDef);
      }

      dexPool.writeTo(new FileDataStore(new File(path)));
   }

   public void internClass(ClassDef classDef) {
      ((ClassPool)this.classSection).intern(classDef);
   }

   public void mark() {
      Markable[] var1 = this.sections;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Markable section = var1[var3];
         section.mark();
      }

   }

   public void reset() {
      Markable[] var1 = this.sections;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Markable section = var1[var3];
         section.reset();
      }

   }

   protected void writeEncodedValue(@Nonnull DexWriter<CharSequence, StringReference, CharSequence, TypeReference, MethodProtoReference, FieldReference, MethodReference, PoolClassDef, Annotation, Set<? extends Annotation>, TypeListPool.Key<? extends Collection<? extends CharSequence>>, Field, PoolMethod, EncodedValue, AnnotationElement, StringPool, TypePool, ProtoPool, FieldPool, MethodPool, ClassPool, TypeListPool, AnnotationPool, AnnotationSetPool>.InternalEncodedValueWriter writer, @Nonnull EncodedValue encodedValue) throws IOException {
      switch(encodedValue.getValueType()) {
      case 0:
         writer.writeByte(((ByteEncodedValue)encodedValue).getValue());
         break;
      case 1:
      case 5:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      default:
         throw new ExceptionWithContext("Unrecognized value type: %d", new Object[]{encodedValue.getValueType()});
      case 2:
         writer.writeShort(((ShortEncodedValue)encodedValue).getValue());
         break;
      case 3:
         writer.writeChar(((CharEncodedValue)encodedValue).getValue());
         break;
      case 4:
         writer.writeInt(((IntEncodedValue)encodedValue).getValue());
         break;
      case 6:
         writer.writeLong(((LongEncodedValue)encodedValue).getValue());
         break;
      case 16:
         writer.writeFloat(((FloatEncodedValue)encodedValue).getValue());
         break;
      case 17:
         writer.writeDouble(((DoubleEncodedValue)encodedValue).getValue());
         break;
      case 23:
         writer.writeString(((StringEncodedValue)encodedValue).getValue());
         break;
      case 24:
         writer.writeType(((TypeEncodedValue)encodedValue).getValue());
         break;
      case 25:
         writer.writeField(((FieldEncodedValue)encodedValue).getValue());
         break;
      case 26:
         writer.writeMethod(((MethodEncodedValue)encodedValue).getValue());
         break;
      case 27:
         writer.writeEnum(((EnumEncodedValue)encodedValue).getValue());
         break;
      case 28:
         ArrayEncodedValue arrayEncodedValue = (ArrayEncodedValue)encodedValue;
         writer.writeArray(arrayEncodedValue.getValue());
         break;
      case 29:
         AnnotationEncodedValue annotationEncodedValue = (AnnotationEncodedValue)encodedValue;
         writer.writeAnnotation(annotationEncodedValue.getType(), annotationEncodedValue.getElements());
         break;
      case 30:
         writer.writeNull();
         break;
      case 31:
         writer.writeBoolean(((BooleanEncodedValue)encodedValue).getValue());
      }

   }

   void internEncodedValue(@Nonnull EncodedValue encodedValue) {
      Iterator var3;
      switch(encodedValue.getValueType()) {
      case 23:
         ((StringPool)this.stringSection).intern(((StringEncodedValue)encodedValue).getValue());
         break;
      case 24:
         ((TypePool)this.typeSection).intern(((TypeEncodedValue)encodedValue).getValue());
         break;
      case 25:
         ((FieldPool)this.fieldSection).intern(((FieldEncodedValue)encodedValue).getValue());
         break;
      case 26:
         ((MethodPool)this.methodSection).intern(((MethodEncodedValue)encodedValue).getValue());
         break;
      case 27:
         ((FieldPool)this.fieldSection).intern(((EnumEncodedValue)encodedValue).getValue());
         break;
      case 28:
         var3 = ((ArrayEncodedValue)encodedValue).getValue().iterator();

         while(var3.hasNext()) {
            EncodedValue element = (EncodedValue)var3.next();
            this.internEncodedValue(element);
         }

         return;
      case 29:
         AnnotationEncodedValue annotationEncodedValue = (AnnotationEncodedValue)encodedValue;
         ((TypePool)this.typeSection).intern(annotationEncodedValue.getType());
         var3 = annotationEncodedValue.getElements().iterator();

         while(var3.hasNext()) {
            AnnotationElement element = (AnnotationElement)var3.next();
            ((StringPool)this.stringSection).intern(element.getName());
            this.internEncodedValue(element.getValue());
         }
      }

   }

   protected class DexPoolSectionProvider extends DexWriter<CharSequence, StringReference, CharSequence, TypeReference, MethodProtoReference, FieldReference, MethodReference, PoolClassDef, Annotation, Set<? extends Annotation>, TypeListPool.Key<? extends Collection<? extends CharSequence>>, Field, PoolMethod, EncodedValue, AnnotationElement, StringPool, TypePool, ProtoPool, FieldPool, MethodPool, ClassPool, TypeListPool, AnnotationPool, AnnotationSetPool>.SectionProvider {
      protected DexPoolSectionProvider() {
         super();
      }

      @Nonnull
      public StringPool getStringSection() {
         return new StringPool(DexPool.this);
      }

      @Nonnull
      public TypePool getTypeSection() {
         return new TypePool(DexPool.this);
      }

      @Nonnull
      public ProtoPool getProtoSection() {
         return new ProtoPool(DexPool.this);
      }

      @Nonnull
      public FieldPool getFieldSection() {
         return new FieldPool(DexPool.this);
      }

      @Nonnull
      public MethodPool getMethodSection() {
         return new MethodPool(DexPool.this);
      }

      @Nonnull
      public ClassPool getClassSection() {
         return new ClassPool(DexPool.this);
      }

      @Nonnull
      public TypeListPool getTypeListSection() {
         return new TypeListPool(DexPool.this);
      }

      @Nonnull
      public AnnotationPool getAnnotationSection() {
         return new AnnotationPool(DexPool.this);
      }

      @Nonnull
      public AnnotationSetPool getAnnotationSetSection() {
         return new AnnotationSetPool(DexPool.this);
      }
   }
}
