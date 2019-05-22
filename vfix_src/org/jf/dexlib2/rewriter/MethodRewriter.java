package org.jf.dexlib2.rewriter;

import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseMethodReference;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.dexlib2.iface.reference.MethodReference;

public class MethodRewriter implements Rewriter<Method> {
   @Nonnull
   protected final Rewriters rewriters;

   public MethodRewriter(@Nonnull Rewriters rewriters) {
      this.rewriters = rewriters;
   }

   @Nonnull
   public Method rewrite(@Nonnull Method value) {
      return new MethodRewriter.RewrittenMethod(value);
   }

   protected class RewrittenMethod extends BaseMethodReference implements Method {
      @Nonnull
      protected Method method;

      public RewrittenMethod(@Nonnull Method method) {
         this.method = method;
      }

      @Nonnull
      public String getDefiningClass() {
         return ((MethodReference)MethodRewriter.this.rewriters.getMethodReferenceRewriter().rewrite(this.method)).getDefiningClass();
      }

      @Nonnull
      public String getName() {
         return ((MethodReference)MethodRewriter.this.rewriters.getMethodReferenceRewriter().rewrite(this.method)).getName();
      }

      @Nonnull
      public List<? extends CharSequence> getParameterTypes() {
         return ((MethodReference)MethodRewriter.this.rewriters.getMethodReferenceRewriter().rewrite(this.method)).getParameterTypes();
      }

      @Nonnull
      public List<? extends MethodParameter> getParameters() {
         return RewriterUtils.rewriteList(MethodRewriter.this.rewriters.getMethodParameterRewriter(), this.method.getParameters());
      }

      @Nonnull
      public String getReturnType() {
         return ((MethodReference)MethodRewriter.this.rewriters.getMethodReferenceRewriter().rewrite(this.method)).getReturnType();
      }

      public int getAccessFlags() {
         return this.method.getAccessFlags();
      }

      @Nonnull
      public Set<? extends Annotation> getAnnotations() {
         return RewriterUtils.rewriteSet(MethodRewriter.this.rewriters.getAnnotationRewriter(), this.method.getAnnotations());
      }

      @Nullable
      public MethodImplementation getImplementation() {
         return (MethodImplementation)RewriterUtils.rewriteNullable(MethodRewriter.this.rewriters.getMethodImplementationRewriter(), this.method.getImplementation());
      }
   }
}
