package groovyjarjarasm.asm.commons;

import groovyjarjarasm.asm.signature.SignatureVisitor;

public class RemappingSignatureAdapter implements SignatureVisitor {
   private final SignatureVisitor v;
   private final Remapper remapper;
   private String className;

   public RemappingSignatureAdapter(SignatureVisitor var1, Remapper var2) {
      this.v = var1;
      this.remapper = var2;
   }

   public void visitClassType(String var1) {
      this.className = var1;
      this.v.visitClassType(this.remapper.mapType(var1));
   }

   public void visitInnerClassType(String var1) {
      this.className = this.className + '$' + var1;
      String var2 = this.remapper.mapType(this.className);
      this.v.visitInnerClassType(var2.substring(var2.lastIndexOf(36) + 1));
   }

   public void visitFormalTypeParameter(String var1) {
      this.v.visitFormalTypeParameter(var1);
   }

   public void visitTypeVariable(String var1) {
      this.v.visitTypeVariable(var1);
   }

   public SignatureVisitor visitArrayType() {
      this.v.visitArrayType();
      return this;
   }

   public void visitBaseType(char var1) {
      this.v.visitBaseType(var1);
   }

   public SignatureVisitor visitClassBound() {
      this.v.visitClassBound();
      return this;
   }

   public SignatureVisitor visitExceptionType() {
      this.v.visitExceptionType();
      return this;
   }

   public SignatureVisitor visitInterface() {
      this.v.visitInterface();
      return this;
   }

   public SignatureVisitor visitInterfaceBound() {
      this.v.visitInterfaceBound();
      return this;
   }

   public SignatureVisitor visitParameterType() {
      this.v.visitParameterType();
      return this;
   }

   public SignatureVisitor visitReturnType() {
      this.v.visitReturnType();
      return this;
   }

   public SignatureVisitor visitSuperclass() {
      this.v.visitSuperclass();
      return this;
   }

   public void visitTypeArgument() {
      this.v.visitTypeArgument();
   }

   public SignatureVisitor visitTypeArgument(char var1) {
      this.v.visitTypeArgument(var1);
      return this;
   }

   public void visitEnd() {
      this.v.visitEnd();
   }
}
