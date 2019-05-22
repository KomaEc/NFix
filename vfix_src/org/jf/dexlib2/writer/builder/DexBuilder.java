package org.jf.dexlib2.writer.builder;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.Reference;
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
import org.jf.util.ExceptionWithContext;

public class DexBuilder extends DexWriter<BuilderStringReference, BuilderStringReference, BuilderTypeReference, BuilderTypeReference, BuilderMethodProtoReference, BuilderFieldReference, BuilderMethodReference, BuilderClassDef, BuilderAnnotation, BuilderAnnotationSet, BuilderTypeList, BuilderField, BuilderMethod, BuilderEncodedValues.BuilderEncodedValue, BuilderAnnotationElement, BuilderStringPool, BuilderTypePool, BuilderProtoPool, BuilderFieldPool, BuilderMethodPool, BuilderClassPool, BuilderTypeListPool, BuilderAnnotationPool, BuilderAnnotationSetPool> {
   public DexBuilder(@Nonnull Opcodes opcodes) {
      super(opcodes);
   }

   @Nonnull
   protected DexWriter<BuilderStringReference, BuilderStringReference, BuilderTypeReference, BuilderTypeReference, BuilderMethodProtoReference, BuilderFieldReference, BuilderMethodReference, BuilderClassDef, BuilderAnnotation, BuilderAnnotationSet, BuilderTypeList, BuilderField, BuilderMethod, BuilderEncodedValues.BuilderEncodedValue, BuilderAnnotationElement, BuilderStringPool, BuilderTypePool, BuilderProtoPool, BuilderFieldPool, BuilderMethodPool, BuilderClassPool, BuilderTypeListPool, BuilderAnnotationPool, BuilderAnnotationSetPool>.SectionProvider getSectionProvider() {
      return new DexBuilder.DexBuilderSectionProvider();
   }

   @Nonnull
   public BuilderField internField(@Nonnull String definingClass, @Nonnull String name, @Nonnull String type, int accessFlags, @Nullable EncodedValue initialValue, @Nonnull Set<? extends Annotation> annotations) {
      return new BuilderField(((BuilderFieldPool)this.fieldSection).internField(definingClass, name, type), accessFlags, this.internNullableEncodedValue(initialValue), ((BuilderAnnotationSetPool)this.annotationSetSection).internAnnotationSet(annotations));
   }

   @Nonnull
   public BuilderMethod internMethod(@Nonnull String definingClass, @Nonnull String name, @Nullable List<? extends MethodParameter> parameters, @Nonnull String returnType, int accessFlags, @Nonnull Set<? extends Annotation> annotations, @Nullable MethodImplementation methodImplementation) {
      if (parameters == null) {
         parameters = ImmutableList.of();
      }

      return new BuilderMethod(((BuilderMethodPool)this.methodSection).internMethod(definingClass, name, (List)parameters, returnType), this.internMethodParameters((List)parameters), accessFlags, ((BuilderAnnotationSetPool)this.annotationSetSection).internAnnotationSet(annotations), methodImplementation);
   }

   @Nonnull
   public BuilderClassDef internClassDef(@Nonnull String type, int accessFlags, @Nullable String superclass, @Nullable List<String> interfaces, @Nullable String sourceFile, @Nonnull Set<? extends Annotation> annotations, @Nullable Iterable<? extends BuilderField> fields, @Nullable Iterable<? extends BuilderMethod> methods) {
      if (interfaces == null) {
         interfaces = ImmutableList.of();
      } else {
         Set<String> interfaces_copy = Sets.newHashSet((Iterable)interfaces);
         Iterator interfaceIterator = ((List)interfaces).iterator();

         while(interfaceIterator.hasNext()) {
            String iface = (String)interfaceIterator.next();
            if (!interfaces_copy.contains(iface)) {
               interfaceIterator.remove();
            } else {
               interfaces_copy.remove(iface);
            }
         }
      }

      return ((BuilderClassPool)this.classSection).internClass(new BuilderClassDef(((BuilderTypePool)this.typeSection).internType(type), accessFlags, ((BuilderTypePool)this.typeSection).internNullableType(superclass), ((BuilderTypeListPool)this.typeListSection).internTypeList((List)interfaces), ((BuilderStringPool)this.stringSection).internNullableString(sourceFile), ((BuilderAnnotationSetPool)this.annotationSetSection).internAnnotationSet(annotations), fields, methods));
   }

   @Nonnull
   public BuilderStringReference internStringReference(@Nonnull String string) {
      return ((BuilderStringPool)this.stringSection).internString(string);
   }

   @Nullable
   public BuilderStringReference internNullableStringReference(@Nullable String string) {
      return string != null ? this.internStringReference(string) : null;
   }

   @Nonnull
   public BuilderTypeReference internTypeReference(@Nonnull String type) {
      return ((BuilderTypePool)this.typeSection).internType(type);
   }

   @Nullable
   public BuilderTypeReference internNullableTypeReference(@Nullable String type) {
      return type != null ? this.internTypeReference(type) : null;
   }

   @Nonnull
   public BuilderFieldReference internFieldReference(@Nonnull FieldReference field) {
      return ((BuilderFieldPool)this.fieldSection).internField(field);
   }

   @Nonnull
   public BuilderMethodReference internMethodReference(@Nonnull MethodReference method) {
      return ((BuilderMethodPool)this.methodSection).internMethod(method);
   }

   @Nonnull
   public BuilderMethodProtoReference internMethodProtoReference(@Nonnull MethodProtoReference methodProto) {
      return ((BuilderProtoPool)this.protoSection).internMethodProto(methodProto);
   }

   @Nonnull
   public BuilderReference internReference(@Nonnull Reference reference) {
      if (reference instanceof StringReference) {
         return this.internStringReference(((StringReference)reference).getString());
      } else if (reference instanceof TypeReference) {
         return this.internTypeReference(((TypeReference)reference).getType());
      } else if (reference instanceof MethodReference) {
         return this.internMethodReference((MethodReference)reference);
      } else if (reference instanceof FieldReference) {
         return this.internFieldReference((FieldReference)reference);
      } else if (reference instanceof MethodProtoReference) {
         return this.internMethodProtoReference((MethodProtoReference)reference);
      } else {
         throw new IllegalArgumentException("Could not determine type of reference");
      }
   }

   @Nonnull
   private List<BuilderMethodParameter> internMethodParameters(@Nullable List<? extends MethodParameter> methodParameters) {
      return methodParameters == null ? ImmutableList.of() : ImmutableList.copyOf(Iterators.transform(methodParameters.iterator(), new Function<MethodParameter, BuilderMethodParameter>() {
         @Nullable
         public BuilderMethodParameter apply(MethodParameter input) {
            return DexBuilder.this.internMethodParameter(input);
         }
      }));
   }

   @Nonnull
   private BuilderMethodParameter internMethodParameter(@Nonnull MethodParameter methodParameter) {
      return new BuilderMethodParameter(((BuilderTypePool)this.typeSection).internType(methodParameter.getType()), ((BuilderStringPool)this.stringSection).internNullableString(methodParameter.getName()), ((BuilderAnnotationSetPool)this.annotationSetSection).internAnnotationSet(methodParameter.getAnnotations()));
   }

   protected void writeEncodedValue(@Nonnull DexWriter<BuilderStringReference, BuilderStringReference, BuilderTypeReference, BuilderTypeReference, BuilderMethodProtoReference, BuilderFieldReference, BuilderMethodReference, BuilderClassDef, BuilderAnnotation, BuilderAnnotationSet, BuilderTypeList, BuilderField, BuilderMethod, BuilderEncodedValues.BuilderEncodedValue, BuilderAnnotationElement, BuilderStringPool, BuilderTypePool, BuilderProtoPool, BuilderFieldPool, BuilderMethodPool, BuilderClassPool, BuilderTypeListPool, BuilderAnnotationPool, BuilderAnnotationSetPool>.InternalEncodedValueWriter writer, @Nonnull BuilderEncodedValues.BuilderEncodedValue encodedValue) throws IOException {
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
         writer.writeString(((BuilderEncodedValues.BuilderStringEncodedValue)encodedValue).stringReference);
         break;
      case 24:
         writer.writeType(((BuilderEncodedValues.BuilderTypeEncodedValue)encodedValue).typeReference);
         break;
      case 25:
         writer.writeField(((BuilderEncodedValues.BuilderFieldEncodedValue)encodedValue).fieldReference);
         break;
      case 26:
         writer.writeMethod(((BuilderEncodedValues.BuilderMethodEncodedValue)encodedValue).methodReference);
         break;
      case 27:
         writer.writeEnum(((BuilderEncodedValues.BuilderEnumEncodedValue)encodedValue).getValue());
         break;
      case 28:
         BuilderEncodedValues.BuilderArrayEncodedValue arrayEncodedValue = (BuilderEncodedValues.BuilderArrayEncodedValue)encodedValue;
         writer.writeArray(arrayEncodedValue.elements);
         break;
      case 29:
         BuilderEncodedValues.BuilderAnnotationEncodedValue annotationEncodedValue = (BuilderEncodedValues.BuilderAnnotationEncodedValue)encodedValue;
         writer.writeAnnotation(annotationEncodedValue.typeReference, annotationEncodedValue.elements);
         break;
      case 30:
         writer.writeNull();
         break;
      case 31:
         writer.writeBoolean(((BooleanEncodedValue)encodedValue).getValue());
      }

   }

   @Nonnull
   Set<? extends BuilderAnnotationElement> internAnnotationElements(@Nonnull Set<? extends AnnotationElement> elements) {
      return ImmutableSet.copyOf(Iterators.transform(elements.iterator(), new Function<AnnotationElement, BuilderAnnotationElement>() {
         @Nullable
         public BuilderAnnotationElement apply(AnnotationElement input) {
            return DexBuilder.this.internAnnotationElement(input);
         }
      }));
   }

   @Nonnull
   private BuilderAnnotationElement internAnnotationElement(@Nonnull AnnotationElement annotationElement) {
      return new BuilderAnnotationElement(((BuilderStringPool)this.stringSection).internString(annotationElement.getName()), this.internEncodedValue(annotationElement.getValue()));
   }

   @Nullable
   BuilderEncodedValues.BuilderEncodedValue internNullableEncodedValue(@Nullable EncodedValue encodedValue) {
      return encodedValue == null ? null : this.internEncodedValue(encodedValue);
   }

   @Nonnull
   private BuilderEncodedValues.BuilderEncodedValue internEncodedValue(@Nonnull EncodedValue encodedValue) {
      switch(encodedValue.getValueType()) {
      case 0:
         return new BuilderEncodedValues.BuilderByteEncodedValue(((ByteEncodedValue)encodedValue).getValue());
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
         throw new ExceptionWithContext("Unexpected encoded value type: %d", new Object[]{encodedValue.getValueType()});
      case 2:
         return new BuilderEncodedValues.BuilderShortEncodedValue(((ShortEncodedValue)encodedValue).getValue());
      case 3:
         return new BuilderEncodedValues.BuilderCharEncodedValue(((CharEncodedValue)encodedValue).getValue());
      case 4:
         return new BuilderEncodedValues.BuilderIntEncodedValue(((IntEncodedValue)encodedValue).getValue());
      case 6:
         return new BuilderEncodedValues.BuilderLongEncodedValue(((LongEncodedValue)encodedValue).getValue());
      case 16:
         return new BuilderEncodedValues.BuilderFloatEncodedValue(((FloatEncodedValue)encodedValue).getValue());
      case 17:
         return new BuilderEncodedValues.BuilderDoubleEncodedValue(((DoubleEncodedValue)encodedValue).getValue());
      case 23:
         return this.internStringEncodedValue((StringEncodedValue)encodedValue);
      case 24:
         return this.internTypeEncodedValue((TypeEncodedValue)encodedValue);
      case 25:
         return this.internFieldEncodedValue((FieldEncodedValue)encodedValue);
      case 26:
         return this.internMethodEncodedValue((MethodEncodedValue)encodedValue);
      case 27:
         return this.internEnumEncodedValue((EnumEncodedValue)encodedValue);
      case 28:
         return this.internArrayEncodedValue((ArrayEncodedValue)encodedValue);
      case 29:
         return this.internAnnotationEncodedValue((AnnotationEncodedValue)encodedValue);
      case 30:
         return BuilderEncodedValues.BuilderNullEncodedValue.INSTANCE;
      case 31:
         boolean value = ((BooleanEncodedValue)encodedValue).getValue();
         return value ? BuilderEncodedValues.BuilderBooleanEncodedValue.TRUE_VALUE : BuilderEncodedValues.BuilderBooleanEncodedValue.FALSE_VALUE;
      }
   }

   @Nonnull
   private BuilderEncodedValues.BuilderAnnotationEncodedValue internAnnotationEncodedValue(@Nonnull AnnotationEncodedValue value) {
      return new BuilderEncodedValues.BuilderAnnotationEncodedValue(((BuilderTypePool)this.typeSection).internType(value.getType()), this.internAnnotationElements(value.getElements()));
   }

   @Nonnull
   private BuilderEncodedValues.BuilderArrayEncodedValue internArrayEncodedValue(@Nonnull ArrayEncodedValue value) {
      return new BuilderEncodedValues.BuilderArrayEncodedValue(ImmutableList.copyOf(Iterators.transform(value.getValue().iterator(), new Function<EncodedValue, BuilderEncodedValues.BuilderEncodedValue>() {
         @Nullable
         public BuilderEncodedValues.BuilderEncodedValue apply(EncodedValue input) {
            return DexBuilder.this.internEncodedValue(input);
         }
      })));
   }

   @Nonnull
   private BuilderEncodedValues.BuilderEnumEncodedValue internEnumEncodedValue(@Nonnull EnumEncodedValue value) {
      return new BuilderEncodedValues.BuilderEnumEncodedValue(((BuilderFieldPool)this.fieldSection).internField(value.getValue()));
   }

   @Nonnull
   private BuilderEncodedValues.BuilderFieldEncodedValue internFieldEncodedValue(@Nonnull FieldEncodedValue value) {
      return new BuilderEncodedValues.BuilderFieldEncodedValue(((BuilderFieldPool)this.fieldSection).internField(value.getValue()));
   }

   @Nonnull
   private BuilderEncodedValues.BuilderMethodEncodedValue internMethodEncodedValue(@Nonnull MethodEncodedValue value) {
      return new BuilderEncodedValues.BuilderMethodEncodedValue(((BuilderMethodPool)this.methodSection).internMethod(value.getValue()));
   }

   @Nonnull
   private BuilderEncodedValues.BuilderStringEncodedValue internStringEncodedValue(@Nonnull StringEncodedValue string) {
      return new BuilderEncodedValues.BuilderStringEncodedValue(((BuilderStringPool)this.stringSection).internString(string.getValue()));
   }

   @Nonnull
   private BuilderEncodedValues.BuilderTypeEncodedValue internTypeEncodedValue(@Nonnull TypeEncodedValue type) {
      return new BuilderEncodedValues.BuilderTypeEncodedValue(((BuilderTypePool)this.typeSection).internType(type.getValue()));
   }

   protected class DexBuilderSectionProvider extends DexWriter<BuilderStringReference, BuilderStringReference, BuilderTypeReference, BuilderTypeReference, BuilderMethodProtoReference, BuilderFieldReference, BuilderMethodReference, BuilderClassDef, BuilderAnnotation, BuilderAnnotationSet, BuilderTypeList, BuilderField, BuilderMethod, BuilderEncodedValues.BuilderEncodedValue, BuilderAnnotationElement, BuilderStringPool, BuilderTypePool, BuilderProtoPool, BuilderFieldPool, BuilderMethodPool, BuilderClassPool, BuilderTypeListPool, BuilderAnnotationPool, BuilderAnnotationSetPool>.SectionProvider {
      protected DexBuilderSectionProvider() {
         super();
      }

      @Nonnull
      public BuilderStringPool getStringSection() {
         return new BuilderStringPool();
      }

      @Nonnull
      public BuilderTypePool getTypeSection() {
         return new BuilderTypePool(DexBuilder.this);
      }

      @Nonnull
      public BuilderProtoPool getProtoSection() {
         return new BuilderProtoPool(DexBuilder.this);
      }

      @Nonnull
      public BuilderFieldPool getFieldSection() {
         return new BuilderFieldPool(DexBuilder.this);
      }

      @Nonnull
      public BuilderMethodPool getMethodSection() {
         return new BuilderMethodPool(DexBuilder.this);
      }

      @Nonnull
      public BuilderClassPool getClassSection() {
         return new BuilderClassPool(DexBuilder.this);
      }

      @Nonnull
      public BuilderTypeListPool getTypeListSection() {
         return new BuilderTypeListPool(DexBuilder.this);
      }

      @Nonnull
      public BuilderAnnotationPool getAnnotationSection() {
         return new BuilderAnnotationPool(DexBuilder.this);
      }

      @Nonnull
      public BuilderAnnotationSetPool getAnnotationSetSection() {
         return new BuilderAnnotationSetPool(DexBuilder.this);
      }
   }
}
