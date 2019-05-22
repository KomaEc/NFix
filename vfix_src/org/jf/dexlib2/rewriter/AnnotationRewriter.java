package org.jf.dexlib2.rewriter;

import java.util.Set;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.BaseAnnotation;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.AnnotationElement;

public class AnnotationRewriter implements Rewriter<Annotation> {
   @Nonnull
   protected final Rewriters rewriters;

   public AnnotationRewriter(@Nonnull Rewriters rewriters) {
      this.rewriters = rewriters;
   }

   @Nonnull
   public Annotation rewrite(@Nonnull Annotation value) {
      return new AnnotationRewriter.RewrittenAnnotation(value);
   }

   protected class RewrittenAnnotation extends BaseAnnotation {
      @Nonnull
      protected Annotation annotation;

      public RewrittenAnnotation(@Nonnull Annotation annotation) {
         this.annotation = annotation;
      }

      public int getVisibility() {
         return this.annotation.getVisibility();
      }

      @Nonnull
      public String getType() {
         return (String)AnnotationRewriter.this.rewriters.getTypeRewriter().rewrite(this.annotation.getType());
      }

      @Nonnull
      public Set<? extends AnnotationElement> getElements() {
         return RewriterUtils.rewriteSet(AnnotationRewriter.this.rewriters.getAnnotationElementRewriter(), this.annotation.getElements());
      }
   }
}
