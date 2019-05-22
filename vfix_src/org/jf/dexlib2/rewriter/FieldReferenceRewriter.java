package org.jf.dexlib2.rewriter;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseFieldReference;
import org.jf.dexlib2.iface.reference.FieldReference;

public class FieldReferenceRewriter implements Rewriter<FieldReference> {
   @Nonnull
   protected final Rewriters rewriters;

   public FieldReferenceRewriter(@Nonnull Rewriters rewriters) {
      this.rewriters = rewriters;
   }

   @Nonnull
   public FieldReference rewrite(@Nonnull FieldReference fieldReference) {
      return new FieldReferenceRewriter.RewrittenFieldReference(fieldReference);
   }

   protected class RewrittenFieldReference extends BaseFieldReference {
      @Nonnull
      protected FieldReference fieldReference;

      public RewrittenFieldReference(@Nonnull FieldReference fieldReference) {
         this.fieldReference = fieldReference;
      }

      @Nonnull
      public String getDefiningClass() {
         return (String)FieldReferenceRewriter.this.rewriters.getTypeRewriter().rewrite(this.fieldReference.getDefiningClass());
      }

      @Nonnull
      public String getName() {
         return this.fieldReference.getName();
      }

      @Nonnull
      public String getType() {
         return (String)FieldReferenceRewriter.this.rewriters.getTypeRewriter().rewrite(this.fieldReference.getType());
      }
   }
}
