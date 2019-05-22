package org.jf.dexlib2.rewriter;

import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.dexlib2.iface.TryBlock;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.value.EncodedValue;

public interface Rewriters {
   @Nonnull
   Rewriter<ClassDef> getClassDefRewriter();

   @Nonnull
   Rewriter<Field> getFieldRewriter();

   @Nonnull
   Rewriter<Method> getMethodRewriter();

   @Nonnull
   Rewriter<MethodParameter> getMethodParameterRewriter();

   @Nonnull
   Rewriter<MethodImplementation> getMethodImplementationRewriter();

   @Nonnull
   Rewriter<Instruction> getInstructionRewriter();

   @Nonnull
   Rewriter<TryBlock<? extends ExceptionHandler>> getTryBlockRewriter();

   @Nonnull
   Rewriter<ExceptionHandler> getExceptionHandlerRewriter();

   @Nonnull
   Rewriter<DebugItem> getDebugItemRewriter();

   @Nonnull
   Rewriter<String> getTypeRewriter();

   @Nonnull
   Rewriter<FieldReference> getFieldReferenceRewriter();

   @Nonnull
   Rewriter<MethodReference> getMethodReferenceRewriter();

   @Nonnull
   Rewriter<Annotation> getAnnotationRewriter();

   @Nonnull
   Rewriter<AnnotationElement> getAnnotationElementRewriter();

   @Nonnull
   Rewriter<EncodedValue> getEncodedValueRewriter();
}
