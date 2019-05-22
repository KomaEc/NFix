package org.jf.dexlib2.dexbacked;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseTypeReference;
import org.jf.dexlib2.dexbacked.util.AnnotationsDirectory;
import org.jf.dexlib2.dexbacked.util.StaticInitialValueIterator;
import org.jf.dexlib2.dexbacked.util.VariableSizeLookaheadIterator;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.immutable.reference.ImmutableFieldReference;
import org.jf.dexlib2.immutable.reference.ImmutableMethodReference;

public class DexBackedClassDef extends BaseTypeReference implements ClassDef {
   @Nonnull
   public final DexBackedDexFile dexFile;
   private final int classDefOffset;
   private final int staticFieldsOffset;
   private int instanceFieldsOffset = 0;
   private int directMethodsOffset = 0;
   private int virtualMethodsOffset = 0;
   private final int staticFieldCount;
   private final int instanceFieldCount;
   private final int directMethodCount;
   private final int virtualMethodCount;
   @Nullable
   private AnnotationsDirectory annotationsDirectory;

   public DexBackedClassDef(@Nonnull DexBackedDexFile dexFile, int classDefOffset) {
      this.dexFile = dexFile;
      this.classDefOffset = classDefOffset;
      int classDataOffset = dexFile.readSmallUint(classDefOffset + 24);
      if (classDataOffset == 0) {
         this.staticFieldsOffset = -1;
         this.staticFieldCount = 0;
         this.instanceFieldCount = 0;
         this.directMethodCount = 0;
         this.virtualMethodCount = 0;
      } else {
         DexReader reader = dexFile.readerAt(classDataOffset);
         this.staticFieldCount = reader.readSmallUleb128();
         this.instanceFieldCount = reader.readSmallUleb128();
         this.directMethodCount = reader.readSmallUleb128();
         this.virtualMethodCount = reader.readSmallUleb128();
         this.staticFieldsOffset = reader.getOffset();
      }

   }

   @Nonnull
   public String getType() {
      return this.dexFile.getType(this.dexFile.readSmallUint(this.classDefOffset + 0));
   }

   @Nullable
   public String getSuperclass() {
      return this.dexFile.getOptionalType(this.dexFile.readOptionalUint(this.classDefOffset + 8));
   }

   public int getAccessFlags() {
      return this.dexFile.readSmallUint(this.classDefOffset + 4);
   }

   @Nullable
   public String getSourceFile() {
      return this.dexFile.getOptionalString(this.dexFile.readOptionalUint(this.classDefOffset + 16));
   }

   @Nonnull
   public List<String> getInterfaces() {
      final int interfacesOffset = this.dexFile.readSmallUint(this.classDefOffset + 12);
      if (interfacesOffset > 0) {
         final int size = this.dexFile.readSmallUint(interfacesOffset);
         return new AbstractList<String>() {
            @Nonnull
            public String get(int index) {
               return DexBackedClassDef.this.dexFile.getType(DexBackedClassDef.this.dexFile.readUshort(interfacesOffset + 4 + 2 * index));
            }

            public int size() {
               return size;
            }
         };
      } else {
         return ImmutableList.of();
      }
   }

   @Nonnull
   public Set<? extends DexBackedAnnotation> getAnnotations() {
      return this.getAnnotationsDirectory().getClassAnnotations();
   }

   @Nonnull
   public Iterable<? extends DexBackedField> getStaticFields() {
      return this.getStaticFields(true);
   }

   @Nonnull
   public Iterable<? extends DexBackedField> getStaticFields(final boolean skipDuplicates) {
      if (this.staticFieldCount > 0) {
         DexReader reader = this.dexFile.readerAt(this.staticFieldsOffset);
         final AnnotationsDirectory annotationsDirectory = this.getAnnotationsDirectory();
         final int staticInitialValuesOffset = this.dexFile.readSmallUint(this.classDefOffset + 28);
         final int fieldsStartOffset = reader.getOffset();
         return new Iterable<DexBackedField>() {
            @Nonnull
            public Iterator<DexBackedField> iterator() {
               final AnnotationsDirectory.AnnotationIterator annotationIterator = annotationsDirectory.getFieldAnnotationIterator();
               final StaticInitialValueIterator staticInitialValueIterator = StaticInitialValueIterator.newOrEmpty(DexBackedClassDef.this.dexFile, staticInitialValuesOffset);
               return new VariableSizeLookaheadIterator<DexBackedField>(DexBackedClassDef.this.dexFile, fieldsStartOffset) {
                  private int count;
                  @Nullable
                  private FieldReference previousField;
                  private int previousIndex;

                  @Nullable
                  protected DexBackedField readNextItem(@Nonnull DexReader reader) {
                     DexBackedField item;
                     FieldReference currentField;
                     ImmutableFieldReference nextField;
                     do {
                        if (++this.count > DexBackedClassDef.this.staticFieldCount) {
                           DexBackedClassDef.this.instanceFieldsOffset = reader.getOffset();
                           return (DexBackedField)this.endOfData();
                        }

                        item = new DexBackedField(reader, DexBackedClassDef.this, this.previousIndex, staticInitialValueIterator, annotationIterator);
                        currentField = this.previousField;
                        nextField = ImmutableFieldReference.of(item);
                        this.previousField = nextField;
                        this.previousIndex = item.fieldIndex;
                     } while(skipDuplicates && currentField != null && currentField.equals(nextField));

                     return item;
                  }
               };
            }
         };
      } else {
         this.instanceFieldsOffset = this.staticFieldsOffset;
         return ImmutableSet.of();
      }
   }

   @Nonnull
   public Iterable<? extends DexBackedField> getInstanceFields() {
      return this.getInstanceFields(true);
   }

   @Nonnull
   public Iterable<? extends DexBackedField> getInstanceFields(final boolean skipDuplicates) {
      if (this.instanceFieldCount > 0) {
         DexReader reader = this.dexFile.readerAt(this.getInstanceFieldsOffset());
         final AnnotationsDirectory annotationsDirectory = this.getAnnotationsDirectory();
         final int fieldsStartOffset = reader.getOffset();
         return new Iterable<DexBackedField>() {
            @Nonnull
            public Iterator<DexBackedField> iterator() {
               final AnnotationsDirectory.AnnotationIterator annotationIterator = annotationsDirectory.getFieldAnnotationIterator();
               return new VariableSizeLookaheadIterator<DexBackedField>(DexBackedClassDef.this.dexFile, fieldsStartOffset) {
                  private int count;
                  @Nullable
                  private FieldReference previousField;
                  private int previousIndex;

                  @Nullable
                  protected DexBackedField readNextItem(@Nonnull DexReader reader) {
                     DexBackedField item;
                     FieldReference currentField;
                     ImmutableFieldReference nextField;
                     do {
                        if (++this.count > DexBackedClassDef.this.instanceFieldCount) {
                           DexBackedClassDef.this.directMethodsOffset = reader.getOffset();
                           return (DexBackedField)this.endOfData();
                        }

                        item = new DexBackedField(reader, DexBackedClassDef.this, this.previousIndex, annotationIterator);
                        currentField = this.previousField;
                        nextField = ImmutableFieldReference.of(item);
                        this.previousField = nextField;
                        this.previousIndex = item.fieldIndex;
                     } while(skipDuplicates && currentField != null && currentField.equals(nextField));

                     return item;
                  }
               };
            }
         };
      } else {
         if (this.instanceFieldsOffset > 0) {
            this.directMethodsOffset = this.instanceFieldsOffset;
         }

         return ImmutableSet.of();
      }
   }

   @Nonnull
   public Iterable<? extends DexBackedField> getFields() {
      return Iterables.concat(this.getStaticFields(), this.getInstanceFields());
   }

   @Nonnull
   public Iterable<? extends DexBackedMethod> getDirectMethods() {
      return this.getDirectMethods(true);
   }

   @Nonnull
   public Iterable<? extends DexBackedMethod> getDirectMethods(final boolean skipDuplicates) {
      if (this.directMethodCount > 0) {
         DexReader reader = this.dexFile.readerAt(this.getDirectMethodsOffset());
         final AnnotationsDirectory annotationsDirectory = this.getAnnotationsDirectory();
         final int methodsStartOffset = reader.getOffset();
         return new Iterable<DexBackedMethod>() {
            @Nonnull
            public Iterator<DexBackedMethod> iterator() {
               final AnnotationsDirectory.AnnotationIterator methodAnnotationIterator = annotationsDirectory.getMethodAnnotationIterator();
               final AnnotationsDirectory.AnnotationIterator parameterAnnotationIterator = annotationsDirectory.getParameterAnnotationIterator();
               return new VariableSizeLookaheadIterator<DexBackedMethod>(DexBackedClassDef.this.dexFile, methodsStartOffset) {
                  private int count;
                  @Nullable
                  private MethodReference previousMethod;
                  private int previousIndex;

                  @Nullable
                  protected DexBackedMethod readNextItem(@Nonnull DexReader reader) {
                     DexBackedMethod item;
                     MethodReference currentMethod;
                     ImmutableMethodReference nextMethod;
                     do {
                        if (++this.count > DexBackedClassDef.this.directMethodCount) {
                           DexBackedClassDef.this.virtualMethodsOffset = reader.getOffset();
                           return (DexBackedMethod)this.endOfData();
                        }

                        item = new DexBackedMethod(reader, DexBackedClassDef.this, this.previousIndex, methodAnnotationIterator, parameterAnnotationIterator);
                        currentMethod = this.previousMethod;
                        nextMethod = ImmutableMethodReference.of(item);
                        this.previousMethod = nextMethod;
                        this.previousIndex = item.methodIndex;
                     } while(skipDuplicates && currentMethod != null && currentMethod.equals(nextMethod));

                     return item;
                  }
               };
            }
         };
      } else {
         if (this.directMethodsOffset > 0) {
            this.virtualMethodsOffset = this.directMethodsOffset;
         }

         return ImmutableSet.of();
      }
   }

   @Nonnull
   public Iterable<? extends DexBackedMethod> getVirtualMethods(final boolean skipDuplicates) {
      if (this.virtualMethodCount > 0) {
         DexReader reader = this.dexFile.readerAt(this.getVirtualMethodsOffset());
         final AnnotationsDirectory annotationsDirectory = this.getAnnotationsDirectory();
         final int methodsStartOffset = reader.getOffset();
         return new Iterable<DexBackedMethod>() {
            final AnnotationsDirectory.AnnotationIterator methodAnnotationIterator = annotationsDirectory.getMethodAnnotationIterator();
            final AnnotationsDirectory.AnnotationIterator parameterAnnotationIterator = annotationsDirectory.getParameterAnnotationIterator();

            @Nonnull
            public Iterator<DexBackedMethod> iterator() {
               return new VariableSizeLookaheadIterator<DexBackedMethod>(DexBackedClassDef.this.dexFile, methodsStartOffset) {
                  private int count;
                  @Nullable
                  private MethodReference previousMethod;
                  private int previousIndex;

                  @Nullable
                  protected DexBackedMethod readNextItem(@Nonnull DexReader reader) {
                     DexBackedMethod item;
                     MethodReference currentMethod;
                     ImmutableMethodReference nextMethod;
                     do {
                        if (++this.count > DexBackedClassDef.this.virtualMethodCount) {
                           return (DexBackedMethod)this.endOfData();
                        }

                        item = new DexBackedMethod(reader, DexBackedClassDef.this, this.previousIndex, methodAnnotationIterator, parameterAnnotationIterator);
                        currentMethod = this.previousMethod;
                        nextMethod = ImmutableMethodReference.of(item);
                        this.previousMethod = nextMethod;
                        this.previousIndex = item.methodIndex;
                     } while(skipDuplicates && currentMethod != null && currentMethod.equals(nextMethod));

                     return item;
                  }
               };
            }
         };
      } else {
         return ImmutableSet.of();
      }
   }

   @Nonnull
   public Iterable<? extends DexBackedMethod> getVirtualMethods() {
      return this.getVirtualMethods(true);
   }

   @Nonnull
   public Iterable<? extends DexBackedMethod> getMethods() {
      return Iterables.concat(this.getDirectMethods(), this.getVirtualMethods());
   }

   private AnnotationsDirectory getAnnotationsDirectory() {
      if (this.annotationsDirectory == null) {
         int annotationsDirectoryOffset = this.dexFile.readSmallUint(this.classDefOffset + 20);
         this.annotationsDirectory = AnnotationsDirectory.newOrEmpty(this.dexFile, annotationsDirectoryOffset);
      }

      return this.annotationsDirectory;
   }

   private int getInstanceFieldsOffset() {
      if (this.instanceFieldsOffset > 0) {
         return this.instanceFieldsOffset;
      } else {
         DexReader reader = new DexReader(this.dexFile, this.staticFieldsOffset);
         DexBackedField.skipFields(reader, this.staticFieldCount);
         this.instanceFieldsOffset = reader.getOffset();
         return this.instanceFieldsOffset;
      }
   }

   private int getDirectMethodsOffset() {
      if (this.directMethodsOffset > 0) {
         return this.directMethodsOffset;
      } else {
         DexReader reader = this.dexFile.readerAt(this.getInstanceFieldsOffset());
         DexBackedField.skipFields(reader, this.instanceFieldCount);
         this.directMethodsOffset = reader.getOffset();
         return this.directMethodsOffset;
      }
   }

   private int getVirtualMethodsOffset() {
      if (this.virtualMethodsOffset > 0) {
         return this.virtualMethodsOffset;
      } else {
         DexReader reader = this.dexFile.readerAt(this.getDirectMethodsOffset());
         DexBackedMethod.skipMethods(reader, this.directMethodCount);
         this.virtualMethodsOffset = reader.getOffset();
         return this.virtualMethodsOffset;
      }
   }

   public int getSize() {
      int size = 32;
      int size = size + 4;
      int interfacesLength = this.getInterfaces().size();
      if (interfacesLength > 0) {
         size += 4;
         size += interfacesLength * 2;
      }

      AnnotationsDirectory directory = this.getAnnotationsDirectory();
      if (!AnnotationsDirectory.EMPTY.equals(directory)) {
         size += 16;
         Set<? extends DexBackedAnnotation> classAnnotations = directory.getClassAnnotations();
         if (!classAnnotations.isEmpty()) {
            size += 4;
            size += classAnnotations.size() * 4;
         }
      }

      int staticInitialValuesOffset = this.dexFile.readSmallUint(this.classDefOffset + 28);
      if (staticInitialValuesOffset != 0) {
         DexReader reader = this.dexFile.readerAt(staticInitialValuesOffset);
         size += reader.peekSmallUleb128Size();
      }

      int classDataOffset = this.dexFile.readSmallUint(this.classDefOffset + 24);
      if (classDataOffset > 0) {
         DexReader reader = this.dexFile.readerAt(classDataOffset);
         reader.readSmallUleb128();
         reader.readSmallUleb128();
         reader.readSmallUleb128();
         reader.readSmallUleb128();
         size += reader.getOffset() - classDataOffset;
      }

      DexBackedField dexBackedField;
      Iterator var11;
      for(var11 = this.getFields().iterator(); var11.hasNext(); size += dexBackedField.getSize()) {
         dexBackedField = (DexBackedField)var11.next();
      }

      DexBackedMethod dexBackedMethod;
      for(var11 = this.getMethods().iterator(); var11.hasNext(); size += dexBackedMethod.getSize()) {
         dexBackedMethod = (DexBackedMethod)var11.next();
      }

      return size;
   }
}
