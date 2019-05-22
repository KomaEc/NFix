package com.google.inject.spi;

import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.Errors;
import com.google.inject.matcher.Matcher;

public final class TypeConverterBinding implements Element {
   private final Object source;
   private final Matcher<? super TypeLiteral<?>> typeMatcher;
   private final TypeConverter typeConverter;

   public TypeConverterBinding(Object source, Matcher<? super TypeLiteral<?>> typeMatcher, TypeConverter typeConverter) {
      this.source = Preconditions.checkNotNull(source, "source");
      this.typeMatcher = (Matcher)Preconditions.checkNotNull(typeMatcher, "typeMatcher");
      this.typeConverter = (TypeConverter)Preconditions.checkNotNull(typeConverter, "typeConverter");
   }

   public Object getSource() {
      return this.source;
   }

   public Matcher<? super TypeLiteral<?>> getTypeMatcher() {
      return this.typeMatcher;
   }

   public TypeConverter getTypeConverter() {
      return this.typeConverter;
   }

   public <T> T acceptVisitor(ElementVisitor<T> visitor) {
      return visitor.visit(this);
   }

   public void applyTo(Binder binder) {
      binder.withSource(this.getSource()).convertToTypes(this.typeMatcher, this.typeConverter);
   }

   public String toString() {
      String var1 = String.valueOf(String.valueOf(this.typeConverter));
      String var2 = String.valueOf(String.valueOf(this.typeMatcher));
      String var3 = String.valueOf(String.valueOf(Errors.convert(this.source)));
      return (new StringBuilder(27 + var1.length() + var2.length() + var3.length())).append(var1).append(" which matches ").append(var2).append(" (bound at ").append(var3).append(")").toString();
   }
}
