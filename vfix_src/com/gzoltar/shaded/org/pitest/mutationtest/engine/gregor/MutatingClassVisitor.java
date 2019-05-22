package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.Location;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MethodName;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.analysis.InstructionTrackingMethodVisitor;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.blocks.BlockTrackingMethodDecorator;
import com.gzoltar.shaded.org.pitest.reloc.asm.ClassVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

class MutatingClassVisitor extends ClassVisitor {
   private final F<MethodInfo, Boolean> filter;
   private final ClassContext context;
   private final Set<MethodMutatorFactory> methodMutators = new HashSet();
   private final PremutationClassInfo classInfo;

   public MutatingClassVisitor(ClassVisitor delegateClassVisitor, ClassContext context, F<MethodInfo, Boolean> filter, PremutationClassInfo classInfo, Collection<MethodMutatorFactory> mutators) {
      super(327680, delegateClassVisitor);
      this.context = context;
      this.filter = filter;
      this.methodMutators.addAll(mutators);
      this.classInfo = classInfo;
   }

   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
      super.visit(version, access, name, signature, superName, interfaces);
      this.context.registerClass(new ClassInfo(version, access, name, signature, superName, interfaces));
   }

   public void visitSource(String source, String debug) {
      super.visitSource(source, debug);
      this.context.registerSourceFile(source);
   }

   public MethodVisitor visitMethod(int access, String methodName, String methodDescriptor, String signature, String[] exceptions) {
      MethodMutationContext methodContext = new MethodMutationContext(this.context, Location.location(ClassName.fromString(this.context.getClassInfo().getName()), MethodName.fromString(methodName), methodDescriptor));
      MethodVisitor methodVisitor = this.cv.visitMethod(access, methodName, methodDescriptor, signature, exceptions);
      MethodInfo info = (new MethodInfo()).withOwner(this.context.getClassInfo()).withAccess(access).withMethodName(methodName).withMethodDescriptor(methodDescriptor);
      return (Boolean)this.filter.apply(info) ? this.visitMethodForMutation(methodContext, info, methodVisitor) : methodVisitor;
   }

   private MethodVisitor visitMethodForMutation(MethodMutationContext methodContext, MethodInfo methodInfo, MethodVisitor methodVisitor) {
      MethodVisitor next = methodVisitor;

      MethodMutatorFactory each;
      for(Iterator i$ = this.methodMutators.iterator(); i$.hasNext(); next = each.create(methodContext, methodInfo, next)) {
         each = (MethodMutatorFactory)i$.next();
      }

      return new InstructionTrackingMethodVisitor(this.wrapWithDecorators(methodContext, this.wrapWithFilters(methodContext, next)), methodContext);
   }

   private MethodVisitor wrapWithDecorators(MethodMutationContext methodContext, MethodVisitor mv) {
      return this.wrapWithBlockTracker(methodContext, this.wrapWithLineTracker(methodContext, mv));
   }

   private MethodVisitor wrapWithBlockTracker(MethodMutationContext methodContext, MethodVisitor mv) {
      return new BlockTrackingMethodDecorator(methodContext, mv);
   }

   private MethodVisitor wrapWithLineTracker(MethodMutationContext methodContext, MethodVisitor mv) {
      return new LineTrackingMethodVisitor(methodContext, mv);
   }

   private MethodVisitor wrapWithFilters(MethodMutationContext methodContext, MethodVisitor wrappedMethodVisitor) {
      return this.wrapWithLineFilter(methodContext, this.wrapWithAssertFilter(methodContext, wrappedMethodVisitor));
   }

   private MethodVisitor wrapWithAssertFilter(MethodMutationContext methodContext, MethodVisitor wrappedMethodVisitor) {
      return new AvoidAssertsMethodAdapter(methodContext, wrappedMethodVisitor);
   }

   private MethodVisitor wrapWithLineFilter(MethodMutationContext methodContext, MethodVisitor wrappedMethodVisitor) {
      return new LineFilterMethodAdapter(methodContext, this.classInfo, wrappedMethodVisitor);
   }
}
