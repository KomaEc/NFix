package org.jf.dexlib2.rewriter;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseMethodReference;
import org.jf.dexlib2.iface.reference.MethodReference;

public class MethodReferenceRewriter implements Rewriter<MethodReference> {
   @Nonnull
   protected final Rewriters rewriters;

   public MethodReferenceRewriter(@Nonnull Rewriters rewriters) {
      this.rewriters = rewriters;
   }

   @Nonnull
   public MethodReference rewrite(@Nonnull MethodReference methodReference) {
      return new MethodReferenceRewriter.RewrittenMethodReference(methodReference);
   }

   protected class RewrittenMethodReference extends BaseMethodReference {
      @Nonnull
      protected MethodReference methodReference;

      public RewrittenMethodReference(@Nonnull MethodReference methodReference) {
         this.methodReference = methodReference;
      }

      @Nonnull
      public String getDefiningClass() {
         return (String)MethodReferenceRewriter.this.rewriters.getTypeRewriter().rewrite(this.methodReference.getDefiningClass());
      }

      @Nonnull
      public String getName() {
         return this.methodReference.getName();
      }

      @Nonnull
      public List<? extends CharSequence> getParameterTypes() {
         return RewriterUtils.rewriteList(MethodReferenceRewriter.this.rewriters.getTypeRewriter(), Lists.transform(this.methodReference.getParameterTypes(), new Function<CharSequence, String>() {
            @Nonnull
            public String apply(CharSequence input) {
               return input.toString();
            }
         }));
      }

      @Nonnull
      public String getReturnType() {
         return (String)MethodReferenceRewriter.this.rewriters.getTypeRewriter().rewrite(this.methodReference.getReturnType());
      }
   }
}
