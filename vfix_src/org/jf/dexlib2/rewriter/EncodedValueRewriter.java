package org.jf.dexlib2.rewriter;

import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseAnnotationEncodedValue;
import org.jf.dexlib2.base.value.BaseArrayEncodedValue;
import org.jf.dexlib2.base.value.BaseEnumEncodedValue;
import org.jf.dexlib2.base.value.BaseFieldEncodedValue;
import org.jf.dexlib2.base.value.BaseMethodEncodedValue;
import org.jf.dexlib2.base.value.BaseTypeEncodedValue;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.value.AnnotationEncodedValue;
import org.jf.dexlib2.iface.value.ArrayEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.EnumEncodedValue;
import org.jf.dexlib2.iface.value.FieldEncodedValue;
import org.jf.dexlib2.iface.value.MethodEncodedValue;
import org.jf.dexlib2.iface.value.TypeEncodedValue;

public class EncodedValueRewriter implements Rewriter<EncodedValue> {
   @Nonnull
   protected final Rewriters rewriters;

   public EncodedValueRewriter(@Nonnull Rewriters rewriters) {
      this.rewriters = rewriters;
   }

   @Nonnull
   public EncodedValue rewrite(@Nonnull EncodedValue encodedValue) {
      switch(encodedValue.getValueType()) {
      case 24:
         return new EncodedValueRewriter.RewrittenTypeEncodedValue((TypeEncodedValue)encodedValue);
      case 25:
         return new EncodedValueRewriter.RewrittenFieldEncodedValue((FieldEncodedValue)encodedValue);
      case 26:
         return new EncodedValueRewriter.RewrittenMethodEncodedValue((MethodEncodedValue)encodedValue);
      case 27:
         return new EncodedValueRewriter.RewrittenEnumEncodedValue((EnumEncodedValue)encodedValue);
      case 28:
         return new EncodedValueRewriter.RewrittenArrayEncodedValue((ArrayEncodedValue)encodedValue);
      case 29:
         return new EncodedValueRewriter.RewrittenAnnotationEncodedValue((AnnotationEncodedValue)encodedValue);
      default:
         return encodedValue;
      }
   }

   protected class RewrittenAnnotationEncodedValue extends BaseAnnotationEncodedValue {
      @Nonnull
      protected AnnotationEncodedValue annotationEncodedValue;

      public RewrittenAnnotationEncodedValue(@Nonnull AnnotationEncodedValue annotationEncodedValue) {
         this.annotationEncodedValue = annotationEncodedValue;
      }

      @Nonnull
      public String getType() {
         return (String)EncodedValueRewriter.this.rewriters.getTypeRewriter().rewrite(this.annotationEncodedValue.getType());
      }

      @Nonnull
      public Set<? extends AnnotationElement> getElements() {
         return RewriterUtils.rewriteSet(EncodedValueRewriter.this.rewriters.getAnnotationElementRewriter(), this.annotationEncodedValue.getElements());
      }
   }

   protected class RewrittenArrayEncodedValue extends BaseArrayEncodedValue {
      @Nonnull
      protected ArrayEncodedValue arrayEncodedValue;

      public RewrittenArrayEncodedValue(@Nonnull ArrayEncodedValue arrayEncodedValue) {
         this.arrayEncodedValue = arrayEncodedValue;
      }

      @Nonnull
      public List<? extends EncodedValue> getValue() {
         return RewriterUtils.rewriteList(EncodedValueRewriter.this, this.arrayEncodedValue.getValue());
      }
   }

   protected class RewrittenMethodEncodedValue extends BaseMethodEncodedValue {
      @Nonnull
      protected MethodEncodedValue methodEncodedValue;

      public RewrittenMethodEncodedValue(@Nonnull MethodEncodedValue methodEncodedValue) {
         this.methodEncodedValue = methodEncodedValue;
      }

      @Nonnull
      public MethodReference getValue() {
         return (MethodReference)EncodedValueRewriter.this.rewriters.getMethodReferenceRewriter().rewrite(this.methodEncodedValue.getValue());
      }
   }

   protected class RewrittenEnumEncodedValue extends BaseEnumEncodedValue {
      @Nonnull
      protected EnumEncodedValue enumEncodedValue;

      public RewrittenEnumEncodedValue(@Nonnull EnumEncodedValue enumEncodedValue) {
         this.enumEncodedValue = enumEncodedValue;
      }

      @Nonnull
      public FieldReference getValue() {
         return (FieldReference)EncodedValueRewriter.this.rewriters.getFieldReferenceRewriter().rewrite(this.enumEncodedValue.getValue());
      }
   }

   protected class RewrittenFieldEncodedValue extends BaseFieldEncodedValue {
      @Nonnull
      protected FieldEncodedValue fieldEncodedValue;

      public RewrittenFieldEncodedValue(@Nonnull FieldEncodedValue fieldEncodedValue) {
         this.fieldEncodedValue = fieldEncodedValue;
      }

      @Nonnull
      public FieldReference getValue() {
         return (FieldReference)EncodedValueRewriter.this.rewriters.getFieldReferenceRewriter().rewrite(this.fieldEncodedValue.getValue());
      }
   }

   protected class RewrittenTypeEncodedValue extends BaseTypeEncodedValue {
      @Nonnull
      protected TypeEncodedValue typeEncodedValue;

      public RewrittenTypeEncodedValue(@Nonnull TypeEncodedValue typeEncodedValue) {
         this.typeEncodedValue = typeEncodedValue;
      }

      @Nonnull
      public String getValue() {
         return (String)EncodedValueRewriter.this.rewriters.getTypeRewriter().rewrite(this.typeEncodedValue.getValue());
      }
   }
}
