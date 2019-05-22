package org.jf.dexlib2.writer.builder;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import java.io.IOException;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.builder.MutableMethodImplementation;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.TryBlock;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.debug.EndLocal;
import org.jf.dexlib2.iface.debug.LineNumber;
import org.jf.dexlib2.iface.debug.RestartLocal;
import org.jf.dexlib2.iface.debug.SetSourceFile;
import org.jf.dexlib2.iface.debug.StartLocal;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.util.EncodedValueUtils;
import org.jf.dexlib2.writer.ClassSection;
import org.jf.dexlib2.writer.DebugWriter;
import org.jf.util.AbstractForwardSequentialList;
import org.jf.util.CollectionUtils;
import org.jf.util.ExceptionWithContext;

public class BuilderClassPool extends BaseBuilderPool implements ClassSection<BuilderStringReference, BuilderTypeReference, BuilderTypeList, BuilderClassDef, BuilderField, BuilderMethod, BuilderAnnotationSet, BuilderEncodedValues.BuilderEncodedValue> {
   @Nonnull
   private final ConcurrentMap<String, BuilderClassDef> internedItems = Maps.newConcurrentMap();
   private ImmutableList<BuilderClassDef> sortedClasses = null;
   private static final Predicate<Field> HAS_INITIALIZER = new Predicate<Field>() {
      public boolean apply(Field input) {
         EncodedValue encodedValue = input.getInitialValue();
         return encodedValue != null && !EncodedValueUtils.isDefaultValue(encodedValue);
      }
   };
   private static final Function<BuilderField, BuilderEncodedValues.BuilderEncodedValue> GET_INITIAL_VALUE = new Function<BuilderField, BuilderEncodedValues.BuilderEncodedValue>() {
      public BuilderEncodedValues.BuilderEncodedValue apply(BuilderField input) {
         BuilderEncodedValues.BuilderEncodedValue initialValue = input.getInitialValue();
         return initialValue == null ? BuilderEncodedValues.defaultValueForType(input.getType()) : initialValue;
      }
   };
   private static final Predicate<BuilderMethodParameter> HAS_PARAMETER_ANNOTATIONS = new Predicate<BuilderMethodParameter>() {
      public boolean apply(BuilderMethodParameter input) {
         return input.getAnnotations().size() > 0;
      }
   };
   private static final Function<BuilderMethodParameter, BuilderAnnotationSet> PARAMETER_ANNOTATIONS = new Function<BuilderMethodParameter, BuilderAnnotationSet>() {
      public BuilderAnnotationSet apply(BuilderMethodParameter input) {
         return input.getAnnotations();
      }
   };

   public BuilderClassPool(@Nonnull DexBuilder dexBuilder) {
      super(dexBuilder);
   }

   @Nonnull
   BuilderClassDef internClass(@Nonnull BuilderClassDef classDef) {
      BuilderClassDef prev = (BuilderClassDef)this.internedItems.put(classDef.getType(), classDef);
      if (prev != null) {
         throw new ExceptionWithContext("Class %s has already been interned", new Object[]{classDef.getType()});
      } else {
         return classDef;
      }
   }

   @Nonnull
   public Collection<? extends BuilderClassDef> getSortedClasses() {
      if (this.sortedClasses == null) {
         this.sortedClasses = Ordering.natural().immutableSortedCopy(this.internedItems.values());
      }

      return this.sortedClasses;
   }

   @Nullable
   public Entry<? extends BuilderClassDef, Integer> getClassEntryByType(@Nullable BuilderTypeReference type) {
      if (type == null) {
         return null;
      } else {
         final BuilderClassDef classDef = (BuilderClassDef)this.internedItems.get(type.getType());
         return classDef == null ? null : new Entry<BuilderClassDef, Integer>() {
            public BuilderClassDef getKey() {
               return classDef;
            }

            public Integer getValue() {
               return classDef.classDefIndex;
            }

            public Integer setValue(Integer value) {
               return classDef.classDefIndex = value;
            }
         };
      }
   }

   @Nonnull
   public BuilderTypeReference getType(@Nonnull BuilderClassDef builderClassDef) {
      return builderClassDef.type;
   }

   public int getAccessFlags(@Nonnull BuilderClassDef builderClassDef) {
      return builderClassDef.accessFlags;
   }

   @Nullable
   public BuilderTypeReference getSuperclass(@Nonnull BuilderClassDef builderClassDef) {
      return builderClassDef.superclass;
   }

   @Nullable
   public BuilderTypeList getInterfaces(@Nonnull BuilderClassDef builderClassDef) {
      return builderClassDef.interfaces;
   }

   @Nullable
   public BuilderStringReference getSourceFile(@Nonnull BuilderClassDef builderClassDef) {
      return builderClassDef.sourceFile;
   }

   @Nullable
   public Collection<? extends BuilderEncodedValues.BuilderEncodedValue> getStaticInitializers(@Nonnull BuilderClassDef classDef) {
      final SortedSet<BuilderField> sortedStaticFields = classDef.getStaticFields();
      final int lastIndex = CollectionUtils.lastIndexOf(sortedStaticFields, HAS_INITIALIZER);
      return lastIndex > -1 ? new AbstractCollection<BuilderEncodedValues.BuilderEncodedValue>() {
         @Nonnull
         public Iterator<BuilderEncodedValues.BuilderEncodedValue> iterator() {
            return FluentIterable.from((Iterable)sortedStaticFields).limit(lastIndex + 1).transform(BuilderClassPool.GET_INITIAL_VALUE).iterator();
         }

         public int size() {
            return lastIndex + 1;
         }
      } : null;
   }

   @Nonnull
   public Collection<? extends BuilderField> getSortedStaticFields(@Nonnull BuilderClassDef builderClassDef) {
      return builderClassDef.getStaticFields();
   }

   @Nonnull
   public Collection<? extends BuilderField> getSortedInstanceFields(@Nonnull BuilderClassDef builderClassDef) {
      return builderClassDef.getInstanceFields();
   }

   @Nonnull
   public Collection<? extends BuilderField> getSortedFields(@Nonnull BuilderClassDef builderClassDef) {
      return builderClassDef.getFields();
   }

   @Nonnull
   public Collection<? extends BuilderMethod> getSortedDirectMethods(@Nonnull BuilderClassDef builderClassDef) {
      return builderClassDef.getDirectMethods();
   }

   @Nonnull
   public Collection<? extends BuilderMethod> getSortedVirtualMethods(@Nonnull BuilderClassDef builderClassDef) {
      return builderClassDef.getVirtualMethods();
   }

   @Nonnull
   public Collection<? extends BuilderMethod> getSortedMethods(@Nonnull BuilderClassDef builderClassDef) {
      return builderClassDef.getMethods();
   }

   public int getFieldAccessFlags(@Nonnull BuilderField builderField) {
      return builderField.accessFlags;
   }

   public int getMethodAccessFlags(@Nonnull BuilderMethod builderMethod) {
      return builderMethod.accessFlags;
   }

   @Nullable
   public BuilderAnnotationSet getClassAnnotations(@Nonnull BuilderClassDef builderClassDef) {
      return builderClassDef.annotations.isEmpty() ? null : builderClassDef.annotations;
   }

   @Nullable
   public BuilderAnnotationSet getFieldAnnotations(@Nonnull BuilderField builderField) {
      return builderField.annotations.isEmpty() ? null : builderField.annotations;
   }

   @Nullable
   public BuilderAnnotationSet getMethodAnnotations(@Nonnull BuilderMethod builderMethod) {
      return builderMethod.annotations.isEmpty() ? null : builderMethod.annotations;
   }

   @Nullable
   public List<? extends BuilderAnnotationSet> getParameterAnnotations(@Nonnull BuilderMethod method) {
      final List<? extends BuilderMethodParameter> parameters = method.getParameters();
      boolean hasParameterAnnotations = Iterables.any(parameters, HAS_PARAMETER_ANNOTATIONS);
      return hasParameterAnnotations ? new AbstractForwardSequentialList<BuilderAnnotationSet>() {
         @Nonnull
         public Iterator<BuilderAnnotationSet> iterator() {
            return FluentIterable.from((Iterable)parameters).transform(BuilderClassPool.PARAMETER_ANNOTATIONS).iterator();
         }

         public int size() {
            return parameters.size();
         }
      } : null;
   }

   @Nullable
   public Iterable<? extends DebugItem> getDebugItems(@Nonnull BuilderMethod builderMethod) {
      MethodImplementation impl = builderMethod.getImplementation();
      return impl == null ? null : impl.getDebugItems();
   }

   @Nullable
   public Iterable<? extends BuilderStringReference> getParameterNames(@Nonnull BuilderMethod method) {
      return Iterables.transform(method.getParameters(), new Function<BuilderMethodParameter, BuilderStringReference>() {
         @Nullable
         public BuilderStringReference apply(BuilderMethodParameter input) {
            return input.name;
         }
      });
   }

   public int getRegisterCount(@Nonnull BuilderMethod builderMethod) {
      MethodImplementation impl = builderMethod.getImplementation();
      return impl == null ? 0 : impl.getRegisterCount();
   }

   @Nullable
   public Iterable<? extends Instruction> getInstructions(@Nonnull BuilderMethod builderMethod) {
      MethodImplementation impl = builderMethod.getImplementation();
      return impl == null ? null : impl.getInstructions();
   }

   @Nonnull
   public List<? extends TryBlock<? extends ExceptionHandler>> getTryBlocks(@Nonnull BuilderMethod builderMethod) {
      MethodImplementation impl = builderMethod.getImplementation();
      return (List)(impl == null ? ImmutableList.of() : impl.getTryBlocks());
   }

   @Nullable
   public BuilderTypeReference getExceptionType(@Nonnull ExceptionHandler handler) {
      return this.checkTypeReference(handler.getExceptionTypeReference());
   }

   @Nonnull
   public MutableMethodImplementation makeMutableMethodImplementation(@Nonnull BuilderMethod builderMethod) {
      MethodImplementation impl = builderMethod.getImplementation();
      return impl instanceof MutableMethodImplementation ? (MutableMethodImplementation)impl : new MutableMethodImplementation(impl);
   }

   public void setEncodedArrayOffset(@Nonnull BuilderClassDef builderClassDef, int offset) {
      builderClassDef.encodedArrayOffset = offset;
   }

   public int getEncodedArrayOffset(@Nonnull BuilderClassDef builderClassDef) {
      return builderClassDef.encodedArrayOffset;
   }

   public void setAnnotationDirectoryOffset(@Nonnull BuilderClassDef builderClassDef, int offset) {
      builderClassDef.annotationDirectoryOffset = offset;
   }

   public int getAnnotationDirectoryOffset(@Nonnull BuilderClassDef builderClassDef) {
      return builderClassDef.annotationDirectoryOffset;
   }

   public void setAnnotationSetRefListOffset(@Nonnull BuilderMethod builderMethod, int offset) {
      builderMethod.annotationSetRefListOffset = offset;
   }

   public int getAnnotationSetRefListOffset(@Nonnull BuilderMethod builderMethod) {
      return builderMethod.annotationSetRefListOffset;
   }

   public void setCodeItemOffset(@Nonnull BuilderMethod builderMethod, int offset) {
      builderMethod.codeItemOffset = offset;
   }

   public int getCodeItemOffset(@Nonnull BuilderMethod builderMethod) {
      return builderMethod.codeItemOffset;
   }

   @Nullable
   private BuilderStringReference checkStringReference(@Nullable StringReference stringReference) {
      if (stringReference == null) {
         return null;
      } else {
         try {
            return (BuilderStringReference)stringReference;
         } catch (ClassCastException var3) {
            throw new IllegalStateException("Only StringReference instances returned by DexBuilder.internStringReference or DexBuilder.internNullableStringReference may be used.");
         }
      }
   }

   @Nullable
   private BuilderTypeReference checkTypeReference(@Nullable TypeReference typeReference) {
      if (typeReference == null) {
         return null;
      } else {
         try {
            return (BuilderTypeReference)typeReference;
         } catch (ClassCastException var3) {
            throw new IllegalStateException("Only TypeReference instances returned by DexBuilder.internTypeReference or DexBuilder.internNullableTypeReference may be used.");
         }
      }
   }

   public void writeDebugItem(@Nonnull DebugWriter<BuilderStringReference, BuilderTypeReference> writer, DebugItem debugItem) throws IOException {
      switch(debugItem.getDebugItemType()) {
      case 3:
         StartLocal startLocal = (StartLocal)debugItem;
         writer.writeStartLocal(startLocal.getCodeAddress(), startLocal.getRegister(), this.checkStringReference(startLocal.getNameReference()), this.checkTypeReference(startLocal.getTypeReference()), this.checkStringReference(startLocal.getSignatureReference()));
         break;
      case 4:
      default:
         throw new ExceptionWithContext("Unexpected debug item type: %d", new Object[]{debugItem.getDebugItemType()});
      case 5:
         EndLocal endLocal = (EndLocal)debugItem;
         writer.writeEndLocal(endLocal.getCodeAddress(), endLocal.getRegister());
         break;
      case 6:
         RestartLocal restartLocal = (RestartLocal)debugItem;
         writer.writeRestartLocal(restartLocal.getCodeAddress(), restartLocal.getRegister());
         break;
      case 7:
         writer.writePrologueEnd(debugItem.getCodeAddress());
         break;
      case 8:
         writer.writeEpilogueBegin(debugItem.getCodeAddress());
         break;
      case 9:
         SetSourceFile setSourceFile = (SetSourceFile)debugItem;
         writer.writeSetSourceFile(setSourceFile.getCodeAddress(), this.checkStringReference(setSourceFile.getSourceFileReference()));
         break;
      case 10:
         LineNumber lineNumber = (LineNumber)debugItem;
         writer.writeLineNumber(lineNumber.getCodeAddress(), lineNumber.getLineNumber());
      }

   }

   public int getItemIndex(@Nonnull BuilderClassDef builderClassDef) {
      return builderClassDef.classDefIndex;
   }

   @Nonnull
   public Collection<? extends Entry<? extends BuilderClassDef, Integer>> getItems() {
      return new BuilderMapEntryCollection<BuilderClassDef>(this.internedItems.values()) {
         protected int getValue(@Nonnull BuilderClassDef key) {
            return key.classDefIndex;
         }

         protected int setValue(@Nonnull BuilderClassDef key, int value) {
            int prev = key.classDefIndex;
            key.classDefIndex = value;
            return prev;
         }
      };
   }

   public int getItemCount() {
      return this.internedItems.size();
   }
}
