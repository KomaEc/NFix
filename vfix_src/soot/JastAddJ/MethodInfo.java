package soot.JastAddJ;

import java.util.Iterator;

public class MethodInfo {
   private BytecodeParser p;
   String name;
   int flags;
   private MethodDescriptor methodDescriptor;
   private Attributes.MethodAttributes attributes;

   public MethodInfo(BytecodeParser parser) {
      this.p = parser;
      this.flags = this.p.u2();
      int name_index = this.p.u2();
      CONSTANT_Info info = this.p.constantPool[name_index];
      if (info == null || !(info instanceof CONSTANT_Utf8_Info)) {
         System.err.println("Expected CONSTANT_Utf8_Info but found: " + info.getClass().getName());
      }

      this.name = ((CONSTANT_Utf8_Info)info).string();
      this.methodDescriptor = new MethodDescriptor(this.p, this.name);
      this.attributes = new Attributes.MethodAttributes(this.p);
   }

   public BodyDecl bodyDecl() {
      Signatures.MethodSignature s = this.attributes.methodSignature;
      Access returnType = s != null && s.hasReturnType() ? s.returnType() : this.methodDescriptor.type();
      List parameterList;
      int i;
      Access a;
      int i;
      if (this.isConstructor() && this.p.isInnerClass) {
         parameterList = this.methodDescriptor.parameterListSkipFirst();
         if (s != null) {
            Iterator iter = s.parameterTypes().iterator();
            if (iter.hasNext()) {
               iter.next();
            }

            for(i = 0; iter.hasNext(); ++i) {
               a = (Access)iter.next();
               ((ParameterDeclaration)parameterList.getChildNoTransform(i)).setTypeAccess(a);
            }
         }
      } else {
         parameterList = this.methodDescriptor.parameterList();
         if (s != null) {
            i = 0;

            for(Iterator iter = s.parameterTypes().iterator(); iter.hasNext(); ++i) {
               a = (Access)iter.next();
               ((ParameterDeclaration)parameterList.getChildNoTransform(i)).setTypeAccess(a);
            }
         }
      }

      if ((this.flags & 128) != 0) {
         i = parameterList.getNumChildNoTransform() - 1;
         ParameterDeclaration p = (ParameterDeclaration)parameterList.getChildNoTransform(i);
         parameterList.setChild(new VariableArityParameterDeclaration(p.getModifiersNoTransform(), ((ArrayTypeAccess)p.getTypeAccessNoTransform()).getAccessNoTransform(), p.getID()), i);
      }

      List exceptionList = s != null && s.hasExceptionList() ? s.exceptionList() : this.attributes.exceptionList();
      if (this.attributes.parameterAnnotations != null) {
         for(i = 0; i < this.attributes.parameterAnnotations.length; ++i) {
            ParameterDeclaration p = (ParameterDeclaration)parameterList.getChildNoTransform(i);
            Iterator iter = this.attributes.parameterAnnotations[i].iterator();

            while(iter.hasNext()) {
               Modifier m = (Modifier)iter.next();
               p.getModifiersNoTransform().addModifier(m);
            }
         }
      }

      Object b;
      if (this.isConstructor()) {
         b = new ConstructorDecl(BytecodeParser.modifiers(this.flags), this.name, parameterList, exceptionList, new Opt(), new Block());
      } else if (this.attributes.elementValue() != null) {
         b = new AnnotationMethodDecl(BytecodeParser.modifiers(this.flags), returnType, this.name, parameterList, exceptionList, new Opt(new Block()), new Opt(this.attributes.elementValue()));
      } else if (s != null && s.hasFormalTypeParameters()) {
         b = new GenericMethodDecl(BytecodeParser.modifiers(this.flags), returnType, this.name, parameterList, exceptionList, new Opt(new Block()), s.typeParameters());
      } else {
         b = new MethodDecl(BytecodeParser.modifiers(this.flags), returnType, this.name, parameterList, exceptionList, new Opt(new Block()));
      }

      if (this.attributes.annotations != null) {
         Iterator iter = this.attributes.annotations.iterator();

         while(iter.hasNext()) {
            if (b instanceof MethodDecl) {
               ((MethodDecl)b).getModifiers().addModifier((Modifier)iter.next());
            } else if (b instanceof ConstructorDecl) {
               ((ConstructorDecl)b).getModifiers().addModifier((Modifier)iter.next());
            }
         }
      }

      return (BodyDecl)b;
   }

   private boolean isConstructor() {
      return this.name.equals("<init>");
   }

   public boolean isSynthetic() {
      return this.attributes.isSynthetic() || (this.flags & 4096) != 0;
   }
}
