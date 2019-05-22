package org.jf.dexlib2.dexbacked;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseMethodReference;
import org.jf.dexlib2.dexbacked.reference.DexBackedMethodReference;
import org.jf.dexlib2.dexbacked.util.AnnotationsDirectory;
import org.jf.dexlib2.dexbacked.util.FixedSizeList;
import org.jf.dexlib2.dexbacked.util.ParameterIterator;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.util.AbstractForwardSequentialList;

public class DexBackedMethod extends BaseMethodReference implements Method {
   @Nonnull
   public final DexBackedDexFile dexFile;
   @Nonnull
   public final DexBackedClassDef classDef;
   public final int accessFlags;
   private final int codeOffset;
   private final int parameterAnnotationSetListOffset;
   private final int methodAnnotationSetOffset;
   public final int methodIndex;
   private final int startOffset;
   private int methodIdItemOffset;
   private int protoIdItemOffset;
   private int parametersOffset = -1;

   public DexBackedMethod(@Nonnull DexReader reader, @Nonnull DexBackedClassDef classDef, int previousMethodIndex) {
      this.dexFile = (DexBackedDexFile)reader.dexBuf;
      this.classDef = classDef;
      this.startOffset = reader.getOffset();
      int methodIndexDiff = reader.readLargeUleb128();
      this.methodIndex = methodIndexDiff + previousMethodIndex;
      this.accessFlags = reader.readSmallUleb128();
      this.codeOffset = reader.readSmallUleb128();
      this.methodAnnotationSetOffset = 0;
      this.parameterAnnotationSetListOffset = 0;
   }

   public DexBackedMethod(@Nonnull DexReader reader, @Nonnull DexBackedClassDef classDef, int previousMethodIndex, @Nonnull AnnotationsDirectory.AnnotationIterator methodAnnotationIterator, @Nonnull AnnotationsDirectory.AnnotationIterator paramaterAnnotationIterator) {
      this.dexFile = (DexBackedDexFile)reader.dexBuf;
      this.classDef = classDef;
      this.startOffset = reader.getOffset();
      int methodIndexDiff = reader.readLargeUleb128();
      this.methodIndex = methodIndexDiff + previousMethodIndex;
      this.accessFlags = reader.readSmallUleb128();
      this.codeOffset = reader.readSmallUleb128();
      this.methodAnnotationSetOffset = methodAnnotationIterator.seekTo(this.methodIndex);
      this.parameterAnnotationSetListOffset = paramaterAnnotationIterator.seekTo(this.methodIndex);
   }

   public int getMethodIndex() {
      return this.methodIndex;
   }

   @Nonnull
   public String getDefiningClass() {
      return this.classDef.getType();
   }

   public int getAccessFlags() {
      return this.accessFlags;
   }

   @Nonnull
   public String getName() {
      return this.dexFile.getString(this.dexFile.readSmallUint(this.getMethodIdItemOffset() + 4));
   }

   @Nonnull
   public String getReturnType() {
      return this.dexFile.getType(this.dexFile.readSmallUint(this.getProtoIdItemOffset() + 4));
   }

   @Nonnull
   public List<? extends MethodParameter> getParameters() {
      int parametersOffset = this.getParametersOffset();
      if (parametersOffset > 0) {
         final List<String> parameterTypes = this.getParameterTypes();
         return new AbstractForwardSequentialList<MethodParameter>() {
            @Nonnull
            public Iterator<MethodParameter> iterator() {
               return new ParameterIterator(parameterTypes, DexBackedMethod.this.getParameterAnnotations(), DexBackedMethod.this.getParameterNames());
            }

            public int size() {
               return parameterTypes.size();
            }
         };
      } else {
         return ImmutableList.of();
      }
   }

   @Nonnull
   public List<? extends Set<? extends DexBackedAnnotation>> getParameterAnnotations() {
      return AnnotationsDirectory.getParameterAnnotations(this.dexFile, this.parameterAnnotationSetListOffset);
   }

   @Nonnull
   public Iterator<String> getParameterNames() {
      DexBackedMethodImplementation methodImpl = this.getImplementation();
      return (Iterator)(methodImpl != null ? methodImpl.getParameterNames((DexReader)null) : ImmutableSet.of().iterator());
   }

   @Nonnull
   public List<String> getParameterTypes() {
      int parametersOffset = this.getParametersOffset();
      if (parametersOffset > 0) {
         final int parameterCount = this.dexFile.readSmallUint(parametersOffset + 0);
         final int paramListStart = parametersOffset + 4;
         return new FixedSizeList<String>() {
            @Nonnull
            public String readItem(int index) {
               return DexBackedMethod.this.dexFile.getType(DexBackedMethod.this.dexFile.readUshort(paramListStart + 2 * index));
            }

            public int size() {
               return parameterCount;
            }
         };
      } else {
         return ImmutableList.of();
      }
   }

   @Nonnull
   public Set<? extends Annotation> getAnnotations() {
      return AnnotationsDirectory.getAnnotations(this.dexFile, this.methodAnnotationSetOffset);
   }

   @Nullable
   public DexBackedMethodImplementation getImplementation() {
      return this.codeOffset > 0 ? new DexBackedMethodImplementation(this.dexFile, this, this.codeOffset) : null;
   }

   private int getMethodIdItemOffset() {
      if (this.methodIdItemOffset == 0) {
         this.methodIdItemOffset = this.dexFile.getMethodIdItemOffset(this.methodIndex);
      }

      return this.methodIdItemOffset;
   }

   private int getProtoIdItemOffset() {
      if (this.protoIdItemOffset == 0) {
         int protoIndex = this.dexFile.readUshort(this.getMethodIdItemOffset() + 2);
         this.protoIdItemOffset = this.dexFile.getProtoIdItemOffset(protoIndex);
      }

      return this.protoIdItemOffset;
   }

   private int getParametersOffset() {
      if (this.parametersOffset == -1) {
         this.parametersOffset = this.dexFile.readSmallUint(this.getProtoIdItemOffset() + 8);
      }

      return this.parametersOffset;
   }

   public static void skipMethods(@Nonnull DexReader reader, int count) {
      for(int i = 0; i < count; ++i) {
         reader.skipUleb128();
         reader.skipUleb128();
         reader.skipUleb128();
      }

   }

   public int getSize() {
      int size = 0;
      DexReader reader = this.dexFile.readerAt(this.startOffset);
      reader.readLargeUleb128();
      reader.readSmallUleb128();
      reader.readSmallUleb128();
      int size = size + (reader.getOffset() - this.startOffset);
      DexBackedMethodImplementation impl = this.getImplementation();
      if (impl != null) {
         size += impl.getSize();
      }

      DexBackedMethodReference methodRef = new DexBackedMethodReference(this.dexFile, this.methodIndex);
      size += methodRef.getSize();
      return size;
   }
}
