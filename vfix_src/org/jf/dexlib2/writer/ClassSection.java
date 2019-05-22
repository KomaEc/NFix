package org.jf.dexlib2.writer;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.builder.MutableMethodImplementation;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.TryBlock;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.instruction.Instruction;

public interface ClassSection<StringKey extends CharSequence, TypeKey extends CharSequence, TypeListKey, ClassKey, FieldKey, MethodKey, AnnotationSetKey, EncodedValue> extends IndexSection<ClassKey> {
   @Nonnull
   Collection<? extends ClassKey> getSortedClasses();

   @Nullable
   Entry<? extends ClassKey, Integer> getClassEntryByType(@Nullable TypeKey var1);

   @Nonnull
   TypeKey getType(@Nonnull ClassKey var1);

   int getAccessFlags(@Nonnull ClassKey var1);

   @Nullable
   TypeKey getSuperclass(@Nonnull ClassKey var1);

   @Nullable
   TypeListKey getInterfaces(@Nonnull ClassKey var1);

   @Nullable
   StringKey getSourceFile(@Nonnull ClassKey var1);

   @Nullable
   Collection<? extends EncodedValue> getStaticInitializers(@Nonnull ClassKey var1);

   @Nonnull
   Collection<? extends FieldKey> getSortedStaticFields(@Nonnull ClassKey var1);

   @Nonnull
   Collection<? extends FieldKey> getSortedInstanceFields(@Nonnull ClassKey var1);

   @Nonnull
   Collection<? extends FieldKey> getSortedFields(@Nonnull ClassKey var1);

   @Nonnull
   Collection<? extends MethodKey> getSortedDirectMethods(@Nonnull ClassKey var1);

   @Nonnull
   Collection<? extends MethodKey> getSortedVirtualMethods(@Nonnull ClassKey var1);

   @Nonnull
   Collection<? extends MethodKey> getSortedMethods(@Nonnull ClassKey var1);

   int getFieldAccessFlags(@Nonnull FieldKey var1);

   int getMethodAccessFlags(@Nonnull MethodKey var1);

   @Nullable
   AnnotationSetKey getClassAnnotations(@Nonnull ClassKey var1);

   @Nullable
   AnnotationSetKey getFieldAnnotations(@Nonnull FieldKey var1);

   @Nullable
   AnnotationSetKey getMethodAnnotations(@Nonnull MethodKey var1);

   @Nullable
   List<? extends AnnotationSetKey> getParameterAnnotations(@Nonnull MethodKey var1);

   @Nullable
   Iterable<? extends DebugItem> getDebugItems(@Nonnull MethodKey var1);

   @Nullable
   Iterable<? extends StringKey> getParameterNames(@Nonnull MethodKey var1);

   int getRegisterCount(@Nonnull MethodKey var1);

   @Nullable
   Iterable<? extends Instruction> getInstructions(@Nonnull MethodKey var1);

   @Nonnull
   List<? extends TryBlock<? extends ExceptionHandler>> getTryBlocks(@Nonnull MethodKey var1);

   @Nullable
   TypeKey getExceptionType(@Nonnull ExceptionHandler var1);

   @Nonnull
   MutableMethodImplementation makeMutableMethodImplementation(@Nonnull MethodKey var1);

   void setEncodedArrayOffset(@Nonnull ClassKey var1, int var2);

   int getEncodedArrayOffset(@Nonnull ClassKey var1);

   void setAnnotationDirectoryOffset(@Nonnull ClassKey var1, int var2);

   int getAnnotationDirectoryOffset(@Nonnull ClassKey var1);

   void setAnnotationSetRefListOffset(@Nonnull MethodKey var1, int var2);

   int getAnnotationSetRefListOffset(@Nonnull MethodKey var1);

   void setCodeItemOffset(@Nonnull MethodKey var1, int var2);

   int getCodeItemOffset(@Nonnull MethodKey var1);

   void writeDebugItem(@Nonnull DebugWriter<StringKey, TypeKey> var1, DebugItem var2) throws IOException;
}
