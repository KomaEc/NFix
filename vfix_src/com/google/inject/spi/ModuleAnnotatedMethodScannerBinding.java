package com.google.inject.spi;

import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.internal.Errors;

public final class ModuleAnnotatedMethodScannerBinding implements Element {
   private final Object source;
   private final ModuleAnnotatedMethodScanner scanner;

   public ModuleAnnotatedMethodScannerBinding(Object source, ModuleAnnotatedMethodScanner scanner) {
      this.source = Preconditions.checkNotNull(source, "source");
      this.scanner = (ModuleAnnotatedMethodScanner)Preconditions.checkNotNull(scanner, "scanner");
   }

   public Object getSource() {
      return this.source;
   }

   public ModuleAnnotatedMethodScanner getScanner() {
      return this.scanner;
   }

   public <T> T acceptVisitor(ElementVisitor<T> visitor) {
      return visitor.visit(this);
   }

   public void applyTo(Binder binder) {
      binder.withSource(this.getSource()).scanModulesForAnnotatedMethods(this.scanner);
   }

   public String toString() {
      String var1 = String.valueOf(String.valueOf(this.scanner));
      String var2 = String.valueOf(String.valueOf(this.scanner.annotationClasses()));
      String var3 = String.valueOf(String.valueOf(Errors.convert(this.source)));
      return (new StringBuilder(29 + var1.length() + var2.length() + var3.length())).append(var1).append(" which scans for ").append(var2).append(" (bound at ").append(var3).append(")").toString();
   }
}
