package org.jf.dexlib2.rewriter;

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseFieldReference;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.value.EncodedValue;

public class FieldRewriter implements Rewriter<Field> {
   @Nonnull
   protected final Rewriters rewriters;

   public FieldRewriter(@Nonnull Rewriters rewriters) {
      this.rewriters = rewriters;
   }

   @Nonnull
   public Field rewrite(@Nonnull Field field) {
      return new FieldRewriter.RewrittenField(field);
   }

   protected class RewrittenField extends BaseFieldReference implements Field {
      @Nonnull
      protected Field field;

      public RewrittenField(@Nonnull Field field) {
         this.field = field;
      }

      @Nonnull
      public String getDefiningClass() {
         return ((FieldReference)FieldRewriter.this.rewriters.getFieldReferenceRewriter().rewrite(this.field)).getDefiningClass();
      }

      @Nonnull
      public String getName() {
         return ((FieldReference)FieldRewriter.this.rewriters.getFieldReferenceRewriter().rewrite(this.field)).getName();
      }

      @Nonnull
      public String getType() {
         return ((FieldReference)FieldRewriter.this.rewriters.getFieldReferenceRewriter().rewrite(this.field)).getType();
      }

      public int getAccessFlags() {
         return this.field.getAccessFlags();
      }

      @Nullable
      public EncodedValue getInitialValue() {
         return (EncodedValue)RewriterUtils.rewriteNullable(FieldRewriter.this.rewriters.getEncodedValueRewriter(), this.field.getInitialValue());
      }

      @Nonnull
      public Set<? extends Annotation> getAnnotations() {
         return RewriterUtils.rewriteSet(FieldRewriter.this.rewriters.getAnnotationRewriter(), this.field.getAnnotations());
      }
   }
}
