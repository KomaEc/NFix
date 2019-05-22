package org.jf.dexlib2.writer.pool;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import java.io.IOException;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.builder.MutableMethodImplementation;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.dexlib2.iface.TryBlock;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.debug.EndLocal;
import org.jf.dexlib2.iface.debug.LineNumber;
import org.jf.dexlib2.iface.debug.RestartLocal;
import org.jf.dexlib2.iface.debug.SetSourceFile;
import org.jf.dexlib2.iface.debug.StartLocal;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableEncodedValueFactory;
import org.jf.dexlib2.util.EncodedValueUtils;
import org.jf.dexlib2.util.ReferenceUtil;
import org.jf.dexlib2.writer.ClassSection;
import org.jf.dexlib2.writer.DebugWriter;
import org.jf.util.AbstractForwardSequentialList;
import org.jf.util.CollectionUtils;
import org.jf.util.ExceptionWithContext;

public class ClassPool extends BasePool<String, PoolClassDef> implements ClassSection<CharSequence, CharSequence, TypeListPool.Key<? extends Collection<? extends CharSequence>>, PoolClassDef, Field, PoolMethod, Set<? extends Annotation>, EncodedValue> {
   private ImmutableList<PoolClassDef> sortedClasses = null;
   private static final Predicate<Field> HAS_INITIALIZER = new Predicate<Field>() {
      public boolean apply(Field input) {
         EncodedValue encodedValue = input.getInitialValue();
         return encodedValue != null && !EncodedValueUtils.isDefaultValue(encodedValue);
      }
   };
   private static final Function<Field, EncodedValue> GET_INITIAL_VALUE = new Function<Field, EncodedValue>() {
      public EncodedValue apply(Field input) {
         EncodedValue initialValue = input.getInitialValue();
         return initialValue == null ? ImmutableEncodedValueFactory.defaultValueForType(input.getType()) : initialValue;
      }
   };
   private static final Predicate<MethodParameter> HAS_PARAMETER_ANNOTATIONS = new Predicate<MethodParameter>() {
      public boolean apply(MethodParameter input) {
         return input.getAnnotations().size() > 0;
      }
   };
   private static final Function<MethodParameter, Set<? extends Annotation>> PARAMETER_ANNOTATIONS = new Function<MethodParameter, Set<? extends Annotation>>() {
      public Set<? extends Annotation> apply(MethodParameter input) {
         return input.getAnnotations();
      }
   };

   public ClassPool(@Nonnull DexPool dexPool) {
      super(dexPool);
   }

   public void intern(@Nonnull ClassDef classDef) {
      PoolClassDef poolClassDef = new PoolClassDef(classDef);
      PoolClassDef prev = (PoolClassDef)this.internedItems.put(poolClassDef.getType(), poolClassDef);
      if (prev != null) {
         throw new ExceptionWithContext("Class %s has already been interned", new Object[]{poolClassDef.getType()});
      } else {
         ((TypePool)this.dexPool.typeSection).intern(poolClassDef.getType());
         ((TypePool)this.dexPool.typeSection).internNullable(poolClassDef.getSuperclass());
         ((TypeListPool)this.dexPool.typeListSection).intern(poolClassDef.getInterfaces());
         ((StringPool)this.dexPool.stringSection).internNullable(poolClassDef.getSourceFile());
         HashSet<String> fields = new HashSet();

         Field field;
         for(Iterator var5 = poolClassDef.getFields().iterator(); var5.hasNext(); ((AnnotationSetPool)this.dexPool.annotationSetSection).intern(field.getAnnotations())) {
            field = (Field)var5.next();
            String fieldDescriptor = ReferenceUtil.getShortFieldDescriptor(field);
            if (!fields.add(fieldDescriptor)) {
               throw new ExceptionWithContext("Multiple definitions for field %s->%s", new Object[]{poolClassDef.getType(), fieldDescriptor});
            }

            ((FieldPool)this.dexPool.fieldSection).intern(field);
            EncodedValue initialValue = field.getInitialValue();
            if (initialValue != null) {
               this.dexPool.internEncodedValue(initialValue);
            }
         }

         HashSet<String> methods = new HashSet();
         Iterator var12 = poolClassDef.getMethods().iterator();

         while(var12.hasNext()) {
            PoolMethod method = (PoolMethod)var12.next();
            String methodDescriptor = ReferenceUtil.getMethodDescriptor(method, true);
            if (!methods.add(methodDescriptor)) {
               throw new ExceptionWithContext("Multiple definitions for method %s->%s", new Object[]{poolClassDef.getType(), methodDescriptor});
            }

            ((MethodPool)this.dexPool.methodSection).intern(method);
            this.internCode(method);
            this.internDebug(method);
            ((AnnotationSetPool)this.dexPool.annotationSetSection).intern(method.getAnnotations());
            Iterator var9 = method.getParameters().iterator();

            while(var9.hasNext()) {
               MethodParameter parameter = (MethodParameter)var9.next();
               ((AnnotationSetPool)this.dexPool.annotationSetSection).intern(parameter.getAnnotations());
            }
         }

         ((AnnotationSetPool)this.dexPool.annotationSetSection).intern(poolClassDef.getAnnotations());
      }
   }

   private void internCode(@Nonnull Method method) {
      boolean hasInstruction = false;
      MethodImplementation methodImpl = method.getImplementation();
      if (methodImpl != null) {
         Iterator var4 = methodImpl.getInstructions().iterator();

         while(var4.hasNext()) {
            Instruction instruction = (Instruction)var4.next();
            hasInstruction = true;
            if (instruction instanceof ReferenceInstruction) {
               Reference reference = ((ReferenceInstruction)instruction).getReference();
               switch(instruction.getOpcode().referenceType) {
               case 0:
                  ((StringPool)this.dexPool.stringSection).intern((StringReference)reference);
                  break;
               case 1:
                  ((TypePool)this.dexPool.typeSection).intern((TypeReference)reference);
                  break;
               case 2:
                  ((FieldPool)this.dexPool.fieldSection).intern((FieldReference)reference);
                  break;
               case 3:
                  ((MethodPool)this.dexPool.methodSection).intern((MethodReference)reference);
                  break;
               default:
                  throw new ExceptionWithContext("Unrecognized reference type: %d", new Object[]{instruction.getOpcode().referenceType});
               }
            }
         }

         List<? extends TryBlock> tryBlocks = methodImpl.getTryBlocks();
         if (!hasInstruction && tryBlocks.size() > 0) {
            throw new ExceptionWithContext("Method %s has no instructions, but has try blocks.", new Object[]{ReferenceUtil.getMethodDescriptor(method)});
         }

         Iterator var10 = methodImpl.getTryBlocks().iterator();

         while(var10.hasNext()) {
            TryBlock<? extends ExceptionHandler> tryBlock = (TryBlock)var10.next();
            Iterator var7 = tryBlock.getExceptionHandlers().iterator();

            while(var7.hasNext()) {
               ExceptionHandler handler = (ExceptionHandler)var7.next();
               ((TypePool)this.dexPool.typeSection).internNullable(handler.getExceptionType());
            }
         }
      }

   }

   private void internDebug(@Nonnull Method method) {
      Iterator var2 = method.getParameters().iterator();

      while(var2.hasNext()) {
         MethodParameter param = (MethodParameter)var2.next();
         String paramName = param.getName();
         if (paramName != null) {
            ((StringPool)this.dexPool.stringSection).intern(paramName);
         }
      }

      MethodImplementation methodImpl = method.getImplementation();
      if (methodImpl != null) {
         Iterator var7 = methodImpl.getDebugItems().iterator();

         while(var7.hasNext()) {
            DebugItem debugItem = (DebugItem)var7.next();
            switch(debugItem.getDebugItemType()) {
            case 3:
               StartLocal startLocal = (StartLocal)debugItem;
               ((StringPool)this.dexPool.stringSection).internNullable(startLocal.getName());
               ((TypePool)this.dexPool.typeSection).internNullable(startLocal.getType());
               ((StringPool)this.dexPool.stringSection).internNullable(startLocal.getSignature());
               break;
            case 9:
               ((StringPool)this.dexPool.stringSection).internNullable(((SetSourceFile)debugItem).getSourceFile());
            }
         }
      }

   }

   @Nonnull
   public Collection<? extends PoolClassDef> getSortedClasses() {
      if (this.sortedClasses == null) {
         this.sortedClasses = Ordering.natural().immutableSortedCopy(this.internedItems.values());
      }

      return this.sortedClasses;
   }

   @Nullable
   public Entry<? extends PoolClassDef, Integer> getClassEntryByType(@Nullable CharSequence name) {
      if (name == null) {
         return null;
      } else {
         final PoolClassDef classDef = (PoolClassDef)this.internedItems.get(name.toString());
         return classDef == null ? null : new Entry<PoolClassDef, Integer>() {
            public PoolClassDef getKey() {
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
   public CharSequence getType(@Nonnull PoolClassDef classDef) {
      return classDef.getType();
   }

   public int getAccessFlags(@Nonnull PoolClassDef classDef) {
      return classDef.getAccessFlags();
   }

   @Nullable
   public CharSequence getSuperclass(@Nonnull PoolClassDef classDef) {
      return classDef.getSuperclass();
   }

   @Nullable
   public TypeListPool.Key<List<String>> getInterfaces(@Nonnull PoolClassDef classDef) {
      return classDef.interfaces;
   }

   @Nullable
   public CharSequence getSourceFile(@Nonnull PoolClassDef classDef) {
      return classDef.getSourceFile();
   }

   @Nullable
   public Collection<? extends EncodedValue> getStaticInitializers(@Nonnull PoolClassDef classDef) {
      final SortedSet<Field> sortedStaticFields = classDef.getStaticFields();
      final int lastIndex = CollectionUtils.lastIndexOf(sortedStaticFields, HAS_INITIALIZER);
      return lastIndex > -1 ? new AbstractCollection<EncodedValue>() {
         @Nonnull
         public Iterator<EncodedValue> iterator() {
            return FluentIterable.from((Iterable)sortedStaticFields).limit(lastIndex + 1).transform(ClassPool.GET_INITIAL_VALUE).iterator();
         }

         public int size() {
            return lastIndex + 1;
         }
      } : null;
   }

   @Nonnull
   public Collection<? extends Field> getSortedStaticFields(@Nonnull PoolClassDef classDef) {
      return classDef.getStaticFields();
   }

   @Nonnull
   public Collection<? extends Field> getSortedInstanceFields(@Nonnull PoolClassDef classDef) {
      return classDef.getInstanceFields();
   }

   @Nonnull
   public Collection<? extends Field> getSortedFields(@Nonnull PoolClassDef classDef) {
      return classDef.getFields();
   }

   @Nonnull
   public Collection<PoolMethod> getSortedDirectMethods(@Nonnull PoolClassDef classDef) {
      return classDef.getDirectMethods();
   }

   @Nonnull
   public Collection<PoolMethod> getSortedVirtualMethods(@Nonnull PoolClassDef classDef) {
      return classDef.getVirtualMethods();
   }

   @Nonnull
   public Collection<? extends PoolMethod> getSortedMethods(@Nonnull PoolClassDef classDef) {
      return classDef.getMethods();
   }

   public int getFieldAccessFlags(@Nonnull Field field) {
      return field.getAccessFlags();
   }

   public int getMethodAccessFlags(@Nonnull PoolMethod method) {
      return method.getAccessFlags();
   }

   @Nullable
   public Set<? extends Annotation> getClassAnnotations(@Nonnull PoolClassDef classDef) {
      Set<? extends Annotation> annotations = classDef.getAnnotations();
      return annotations.size() == 0 ? null : annotations;
   }

   @Nullable
   public Set<? extends Annotation> getFieldAnnotations(@Nonnull Field field) {
      Set<? extends Annotation> annotations = field.getAnnotations();
      return annotations.size() == 0 ? null : annotations;
   }

   @Nullable
   public Set<? extends Annotation> getMethodAnnotations(@Nonnull PoolMethod method) {
      Set<? extends Annotation> annotations = method.getAnnotations();
      return annotations.size() == 0 ? null : annotations;
   }

   @Nullable
   public List<? extends Set<? extends Annotation>> getParameterAnnotations(@Nonnull PoolMethod method) {
      final List<? extends MethodParameter> parameters = method.getParameters();
      boolean hasParameterAnnotations = Iterables.any(parameters, HAS_PARAMETER_ANNOTATIONS);
      return hasParameterAnnotations ? new AbstractForwardSequentialList<Set<? extends Annotation>>() {
         @Nonnull
         public Iterator<Set<? extends Annotation>> iterator() {
            return FluentIterable.from((Iterable)parameters).transform(ClassPool.PARAMETER_ANNOTATIONS).iterator();
         }

         public int size() {
            return parameters.size();
         }
      } : null;
   }

   @Nullable
   public Iterable<? extends DebugItem> getDebugItems(@Nonnull PoolMethod method) {
      MethodImplementation impl = method.getImplementation();
      return impl != null ? impl.getDebugItems() : null;
   }

   @Nullable
   public Iterable<CharSequence> getParameterNames(@Nonnull PoolMethod method) {
      return Iterables.transform(method.getParameters(), new Function<MethodParameter, CharSequence>() {
         @Nullable
         public CharSequence apply(MethodParameter input) {
            return input.getName();
         }
      });
   }

   public int getRegisterCount(@Nonnull PoolMethod method) {
      MethodImplementation impl = method.getImplementation();
      return impl != null ? impl.getRegisterCount() : 0;
   }

   @Nullable
   public Iterable<? extends Instruction> getInstructions(@Nonnull PoolMethod method) {
      MethodImplementation impl = method.getImplementation();
      return impl != null ? impl.getInstructions() : null;
   }

   @Nonnull
   public List<? extends TryBlock<? extends ExceptionHandler>> getTryBlocks(@Nonnull PoolMethod method) {
      MethodImplementation impl = method.getImplementation();
      return (List)(impl != null ? impl.getTryBlocks() : ImmutableList.of());
   }

   @Nullable
   public CharSequence getExceptionType(@Nonnull ExceptionHandler handler) {
      return handler.getExceptionType();
   }

   @Nonnull
   public MutableMethodImplementation makeMutableMethodImplementation(@Nonnull PoolMethod poolMethod) {
      return new MutableMethodImplementation(poolMethod.getImplementation());
   }

   public void setEncodedArrayOffset(@Nonnull PoolClassDef classDef, int offset) {
      classDef.encodedArrayOffset = offset;
   }

   public int getEncodedArrayOffset(@Nonnull PoolClassDef classDef) {
      return classDef.encodedArrayOffset;
   }

   public void setAnnotationDirectoryOffset(@Nonnull PoolClassDef classDef, int offset) {
      classDef.annotationDirectoryOffset = offset;
   }

   public int getAnnotationDirectoryOffset(@Nonnull PoolClassDef classDef) {
      return classDef.annotationDirectoryOffset;
   }

   public void setAnnotationSetRefListOffset(@Nonnull PoolMethod method, int offset) {
      method.annotationSetRefListOffset = offset;
   }

   public int getAnnotationSetRefListOffset(@Nonnull PoolMethod method) {
      return method.annotationSetRefListOffset;
   }

   public void setCodeItemOffset(@Nonnull PoolMethod method, int offset) {
      method.codeItemOffset = offset;
   }

   public int getCodeItemOffset(@Nonnull PoolMethod method) {
      return method.codeItemOffset;
   }

   public void writeDebugItem(@Nonnull DebugWriter<CharSequence, CharSequence> writer, DebugItem debugItem) throws IOException {
      switch(debugItem.getDebugItemType()) {
      case 3:
         StartLocal startLocal = (StartLocal)debugItem;
         writer.writeStartLocal(startLocal.getCodeAddress(), startLocal.getRegister(), startLocal.getName(), startLocal.getType(), startLocal.getSignature());
         break;
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
         writer.writeSetSourceFile(setSourceFile.getCodeAddress(), setSourceFile.getSourceFile());
      case 4:
      default:
         throw new ExceptionWithContext("Unexpected debug item type: %d", new Object[]{debugItem.getDebugItemType()});
      case 10:
         LineNumber lineNumber = (LineNumber)debugItem;
         writer.writeLineNumber(lineNumber.getCodeAddress(), lineNumber.getLineNumber());
      }

   }

   public int getItemIndex(@Nonnull PoolClassDef classDef) {
      return classDef.classDefIndex;
   }

   @Nonnull
   public Collection<? extends Entry<PoolClassDef, Integer>> getItems() {
      return new AbstractCollection<Entry<PoolClassDef, Integer>>() {
         @Nonnull
         public Iterator<Entry<PoolClassDef, Integer>> iterator() {
            return new Iterator<Entry<PoolClassDef, Integer>>() {
               Iterator<PoolClassDef> iter;

               {
                  this.iter = ClassPool.this.internedItems.values().iterator();
               }

               public boolean hasNext() {
                  return this.iter.hasNext();
               }

               public Entry<PoolClassDef, Integer> next() {
                  return new MapEntry((PoolClassDef)this.iter.next());
               }

               public void remove() {
                  throw new UnsupportedOperationException();
               }
            };
         }

         public int size() {
            return ClassPool.this.internedItems.size();
         }
      };

      class MapEntry implements Entry<PoolClassDef, Integer> {
         @Nonnull
         private final PoolClassDef classDef;

         public MapEntry(@Nonnull PoolClassDef classDef) {
            this.classDef = classDef;
         }

         public PoolClassDef getKey() {
            return this.classDef;
         }

         public Integer getValue() {
            return this.classDef.classDefIndex;
         }

         public Integer setValue(Integer value) {
            int prev = this.classDef.classDefIndex;
            this.classDef.classDefIndex = value;
            return prev;
         }
      }

   }
}
