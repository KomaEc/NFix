package org.jf.dexlib2.rewriter;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.BaseAnnotationElement;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.value.EncodedValue;

public class AnnotationElementRewriter implements Rewriter<AnnotationElement> {
   @Nonnull
   protected final Rewriters rewriters;

   public AnnotationElementRewriter(@Nonnull Rewriters rewriters) {
      this.rewriters = rewriters;
   }

   @Nonnull
   public AnnotationElement rewrite(@Nonnull AnnotationElement annotationElement) {
      return new AnnotationElementRewriter.RewrittenAnnotationElement(annotationElement);
   }

   protected class RewrittenAnnotationElement extends BaseAnnotationElement {
      @Nonnull
      protected AnnotationElement annotationElement;

      public RewrittenAnnotationElement(@Nonnull AnnotationElement annotationElement) {
         this.annotationElement = annotationElement;
      }

      @Nonnull
      public String getName() {
         return this.annotationElement.getName();
      }

      @Nonnull
      public EncodedValue getValue() {
         return (EncodedValue)AnnotationElementRewriter.this.rewriters.getEncodedValueRewriter().rewrite(this.annotationElement.getValue());
      }
   }
}
