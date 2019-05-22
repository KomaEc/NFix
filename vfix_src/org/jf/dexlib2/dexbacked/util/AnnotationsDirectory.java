package org.jf.dexlib2.dexbacked.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import org.jf.dexlib2.dexbacked.DexBackedAnnotation;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;

public abstract class AnnotationsDirectory {
   public static final AnnotationsDirectory EMPTY = new AnnotationsDirectory() {
      public int getFieldAnnotationCount() {
         return 0;
      }

      @Nonnull
      public Set<? extends DexBackedAnnotation> getClassAnnotations() {
         return ImmutableSet.of();
      }

      @Nonnull
      public AnnotationsDirectory.AnnotationIterator getFieldAnnotationIterator() {
         return AnnotationsDirectory.AnnotationIterator.EMPTY;
      }

      @Nonnull
      public AnnotationsDirectory.AnnotationIterator getMethodAnnotationIterator() {
         return AnnotationsDirectory.AnnotationIterator.EMPTY;
      }

      @Nonnull
      public AnnotationsDirectory.AnnotationIterator getParameterAnnotationIterator() {
         return AnnotationsDirectory.AnnotationIterator.EMPTY;
      }
   };

   public abstract int getFieldAnnotationCount();

   @Nonnull
   public abstract Set<? extends DexBackedAnnotation> getClassAnnotations();

   @Nonnull
   public abstract AnnotationsDirectory.AnnotationIterator getFieldAnnotationIterator();

   @Nonnull
   public abstract AnnotationsDirectory.AnnotationIterator getMethodAnnotationIterator();

   @Nonnull
   public abstract AnnotationsDirectory.AnnotationIterator getParameterAnnotationIterator();

   @Nonnull
   public static AnnotationsDirectory newOrEmpty(@Nonnull DexBackedDexFile dexFile, int directoryAnnotationsOffset) {
      return (AnnotationsDirectory)(directoryAnnotationsOffset == 0 ? EMPTY : new AnnotationsDirectory.AnnotationsDirectoryImpl(dexFile, directoryAnnotationsOffset));
   }

   @Nonnull
   public static Set<? extends DexBackedAnnotation> getAnnotations(@Nonnull final DexBackedDexFile dexFile, final int annotationSetOffset) {
      if (annotationSetOffset != 0) {
         final int size = dexFile.readSmallUint(annotationSetOffset);
         return new FixedSizeSet<DexBackedAnnotation>() {
            @Nonnull
            public DexBackedAnnotation readItem(int index) {
               int annotationOffset = dexFile.readSmallUint(annotationSetOffset + 4 + 4 * index);
               return new DexBackedAnnotation(dexFile, annotationOffset);
            }

            public int size() {
               return size;
            }
         };
      } else {
         return ImmutableSet.of();
      }
   }

   @Nonnull
   public static List<Set<? extends DexBackedAnnotation>> getParameterAnnotations(@Nonnull final DexBackedDexFile dexFile, final int annotationSetListOffset) {
      if (annotationSetListOffset > 0) {
         final int size = dexFile.readSmallUint(annotationSetListOffset);
         return new FixedSizeList<Set<? extends DexBackedAnnotation>>() {
            @Nonnull
            public Set<? extends DexBackedAnnotation> readItem(int index) {
               int annotationSetOffset = dexFile.readSmallUint(annotationSetListOffset + 4 + index * 4);
               return AnnotationsDirectory.getAnnotations(dexFile, annotationSetOffset);
            }

            public int size() {
               return size;
            }
         };
      } else {
         return ImmutableList.of();
      }
   }

   private static class AnnotationsDirectoryImpl extends AnnotationsDirectory {
      @Nonnull
      public final DexBackedDexFile dexFile;
      private final int directoryOffset;
      private static final int FIELD_COUNT_OFFSET = 4;
      private static final int METHOD_COUNT_OFFSET = 8;
      private static final int PARAMETER_COUNT_OFFSET = 12;
      private static final int ANNOTATIONS_START_OFFSET = 16;
      private static final int FIELD_ANNOTATION_SIZE = 8;
      private static final int METHOD_ANNOTATION_SIZE = 8;

      public AnnotationsDirectoryImpl(@Nonnull DexBackedDexFile dexFile, int directoryOffset) {
         this.dexFile = dexFile;
         this.directoryOffset = directoryOffset;
      }

      public int getFieldAnnotationCount() {
         return this.dexFile.readSmallUint(this.directoryOffset + 4);
      }

      public int getMethodAnnotationCount() {
         return this.dexFile.readSmallUint(this.directoryOffset + 8);
      }

      public int getParameterAnnotationCount() {
         return this.dexFile.readSmallUint(this.directoryOffset + 12);
      }

      @Nonnull
      public Set<? extends DexBackedAnnotation> getClassAnnotations() {
         return getAnnotations(this.dexFile, this.dexFile.readSmallUint(this.directoryOffset));
      }

      @Nonnull
      public AnnotationsDirectory.AnnotationIterator getFieldAnnotationIterator() {
         int fieldAnnotationCount = this.getFieldAnnotationCount();
         return (AnnotationsDirectory.AnnotationIterator)(fieldAnnotationCount == 0 ? AnnotationsDirectory.AnnotationIterator.EMPTY : new AnnotationsDirectory.AnnotationsDirectoryImpl.AnnotationIteratorImpl(this.directoryOffset + 16, fieldAnnotationCount));
      }

      @Nonnull
      public AnnotationsDirectory.AnnotationIterator getMethodAnnotationIterator() {
         int methodCount = this.getMethodAnnotationCount();
         if (methodCount == 0) {
            return AnnotationsDirectory.AnnotationIterator.EMPTY;
         } else {
            int fieldCount = this.getFieldAnnotationCount();
            int methodAnnotationsOffset = this.directoryOffset + 16 + fieldCount * 8;
            return new AnnotationsDirectory.AnnotationsDirectoryImpl.AnnotationIteratorImpl(methodAnnotationsOffset, methodCount);
         }
      }

      @Nonnull
      public AnnotationsDirectory.AnnotationIterator getParameterAnnotationIterator() {
         int parameterAnnotationCount = this.getParameterAnnotationCount();
         if (parameterAnnotationCount == 0) {
            return AnnotationsDirectory.AnnotationIterator.EMPTY;
         } else {
            int fieldCount = this.getFieldAnnotationCount();
            int methodCount = this.getMethodAnnotationCount();
            int parameterAnnotationsOffset = this.directoryOffset + 16 + fieldCount * 8 + methodCount * 8;
            return new AnnotationsDirectory.AnnotationsDirectoryImpl.AnnotationIteratorImpl(parameterAnnotationsOffset, parameterAnnotationCount);
         }
      }

      private class AnnotationIteratorImpl implements AnnotationsDirectory.AnnotationIterator {
         private final int startOffset;
         private final int size;
         private int currentIndex;
         private int currentItemIndex;

         public AnnotationIteratorImpl(int startOffset, int size) {
            this.startOffset = startOffset;
            this.size = size;
            this.currentItemIndex = AnnotationsDirectoryImpl.this.dexFile.readSmallUint(startOffset);
            this.currentIndex = 0;
         }

         public int seekTo(int itemIndex) {
            while(this.currentItemIndex < itemIndex && this.currentIndex + 1 < this.size) {
               ++this.currentIndex;
               this.currentItemIndex = AnnotationsDirectoryImpl.this.dexFile.readSmallUint(this.startOffset + this.currentIndex * 8);
            }

            if (this.currentItemIndex == itemIndex) {
               return AnnotationsDirectoryImpl.this.dexFile.readSmallUint(this.startOffset + this.currentIndex * 8 + 4);
            } else {
               return 0;
            }
         }

         public void reset() {
            this.currentItemIndex = AnnotationsDirectoryImpl.this.dexFile.readSmallUint(this.startOffset);
            this.currentIndex = 0;
         }
      }
   }

   public interface AnnotationIterator {
      AnnotationsDirectory.AnnotationIterator EMPTY = new AnnotationsDirectory.AnnotationIterator() {
         public int seekTo(int key) {
            return 0;
         }

         public void reset() {
         }
      };

      int seekTo(int var1);

      void reset();
   }
}
