package org.jf.dexlib2.rewriter;

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseMethodParameter;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.MethodParameter;

public class MethodParameterRewriter implements Rewriter<MethodParameter> {
   @Nonnull
   protected final Rewriters rewriters;

   public MethodParameterRewriter(@Nonnull Rewriters rewriters) {
      this.rewriters = rewriters;
   }

   @Nonnull
   public MethodParameter rewrite(@Nonnull MethodParameter methodParameter) {
      return new MethodParameterRewriter.RewrittenMethodParameter(methodParameter);
   }

   protected class RewrittenMethodParameter extends BaseMethodParameter {
      @Nonnull
      protected MethodParameter methodParameter;

      public RewrittenMethodParameter(@Nonnull MethodParameter methodParameter) {
         this.methodParameter = methodParameter;
      }

      @Nonnull
      public String getType() {
         return (String)MethodParameterRewriter.this.rewriters.getTypeRewriter().rewrite(this.methodParameter.getType());
      }

      @Nonnull
      public Set<? extends Annotation> getAnnotations() {
         return RewriterUtils.rewriteSet(MethodParameterRewriter.this.rewriters.getAnnotationRewriter(), this.methodParameter.getAnnotations());
      }

      @Nullable
      public String getName() {
         return this.methodParameter.getName();
      }

      @Nullable
      public String getSignature() {
         return this.methodParameter.getSignature();
      }
   }
}
