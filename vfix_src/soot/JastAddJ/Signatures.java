package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;

public class Signatures {
   String data;
   int pos;
   protected List typeParameters;

   public Signatures(String s) {
      this.data = s;
      this.pos = 0;
   }

   public boolean next(String s) {
      for(int i = 0; i < s.length(); ++i) {
         if (this.data.charAt(this.pos + i) != s.charAt(i)) {
            return false;
         }
      }

      return true;
   }

   public void eat(String s) {
      for(int i = 0; i < s.length(); ++i) {
         if (this.data.charAt(this.pos + i) != s.charAt(i)) {
            this.error(s);
         }
      }

      this.pos += s.length();
   }

   public void error(String s) {
      throw new Error("Expected " + s + " but found " + this.data.substring(this.pos));
   }

   public String identifier() {
      int i;
      for(i = this.pos; Character.isJavaIdentifierPart(this.data.charAt(i)); ++i) {
      }

      String result = this.data.substring(this.pos, i);
      this.pos = i;
      return result;
   }

   public boolean eof() {
      return this.pos == this.data.length();
   }

   void formalTypeParameters() {
      this.eat("<");
      this.typeParameters = new List();

      do {
         this.typeParameters.add(this.formalTypeParameter());
      } while(!this.next(">"));

      this.eat(">");
   }

   TypeVariable formalTypeParameter() {
      String id = this.identifier();
      List bounds = new List();
      Access classBound = this.classBound();
      if (classBound != null) {
         bounds.add(classBound);
      }

      while(this.next(":")) {
         bounds.add(this.interfaceBound());
      }

      if (bounds.getNumChildNoTransform() == 0) {
         bounds.add(new TypeAccess("java.lang", "Object"));
      }

      return new TypeVariable(new Modifiers(new List()), id, new List(), bounds);
   }

   Access classBound() {
      this.eat(":");
      return this.nextIsFieldTypeSignature() ? this.fieldTypeSignature() : null;
   }

   Access interfaceBound() {
      this.eat(":");
      return this.fieldTypeSignature();
   }

   Access fieldTypeSignature() {
      if (this.next("L")) {
         return this.classTypeSignature();
      } else if (this.next("[")) {
         return this.arrayTypeSignature();
      } else if (this.next("T")) {
         return this.typeVariableSignature();
      } else {
         this.error("L or [ or T");
         return null;
      }
   }

   boolean nextIsFieldTypeSignature() {
      return this.next("L") || this.next("[") || this.next("T");
   }

   Access classTypeSignature() {
      this.eat("L");
      StringBuffer packageName = new StringBuffer();

      String typeName;
      for(typeName = this.identifier(); this.next("/"); typeName = this.identifier()) {
         this.eat("/");
         if (packageName.length() != 0) {
            packageName.append(".");
         }

         packageName.append(typeName);
      }

      Access a = typeName.indexOf(36) == -1 ? new TypeAccess(packageName.toString(), typeName) : new BytecodeTypeAccess(packageName.toString(), typeName);
      if (this.next("<")) {
         a = new ParTypeAccess((Access)a, this.typeArguments());
      }

      while(this.next(".")) {
         a = ((Access)a).qualifiesAccess(this.classTypeSignatureSuffix());
      }

      this.eat(";");
      return (Access)a;
   }

   Access classTypeSignatureSuffix() {
      this.eat(".");
      String id = this.identifier();
      Access a = id.indexOf(36) == -1 ? new TypeAccess(id) : new BytecodeTypeAccess("", id);
      if (this.next("<")) {
         a = new ParTypeAccess((Access)a, this.typeArguments());
      }

      return (Access)a;
   }

   Access typeVariableSignature() {
      this.eat("T");
      String id = this.identifier();
      this.eat(";");
      return new TypeAccess(id);
   }

   List typeArguments() {
      this.eat("<");
      List list = new List();

      do {
         list.add(this.typeArgument());
      } while(!this.next(">"));

      this.eat(">");
      return list;
   }

   Access typeArgument() {
      if (this.next("*")) {
         this.eat("*");
         return new Wildcard();
      } else if (this.next("+")) {
         this.eat("+");
         return new WildcardExtends(this.fieldTypeSignature());
      } else if (this.next("-")) {
         this.eat("-");
         return new WildcardSuper(this.fieldTypeSignature());
      } else {
         return this.fieldTypeSignature();
      }
   }

   Access arrayTypeSignature() {
      this.eat("[");
      return new ArrayTypeAccess(this.typeSignature());
   }

   Access typeSignature() {
      return this.nextIsFieldTypeSignature() ? this.fieldTypeSignature() : this.baseType();
   }

   Access baseType() {
      if (this.next("B")) {
         this.eat("B");
         return new PrimitiveTypeAccess("byte");
      } else if (this.next("C")) {
         this.eat("C");
         return new PrimitiveTypeAccess("char");
      } else if (this.next("D")) {
         this.eat("D");
         return new PrimitiveTypeAccess("double");
      } else if (this.next("F")) {
         this.eat("F");
         return new PrimitiveTypeAccess("float");
      } else if (this.next("I")) {
         this.eat("I");
         return new PrimitiveTypeAccess("int");
      } else if (this.next("J")) {
         this.eat("J");
         return new PrimitiveTypeAccess("long");
      } else if (this.next("S")) {
         this.eat("S");
         return new PrimitiveTypeAccess("short");
      } else if (this.next("Z")) {
         this.eat("Z");
         return new PrimitiveTypeAccess("boolean");
      } else {
         this.error("baseType");
         return null;
      }
   }

   public static class MethodSignature extends Signatures {
      protected Collection parameterTypes = new ArrayList();
      protected List exceptionList = new List();
      protected Access returnType = null;

      public MethodSignature(String s) {
         super(s);
         this.methodTypeSignature();
      }

      void methodTypeSignature() {
         if (this.next("<")) {
            this.formalTypeParameters();
         }

         this.eat("(");

         while(!this.next(")")) {
            this.parameterTypes.add(this.typeSignature());
         }

         this.eat(")");
         this.returnType = this.parseReturnType();

         while(!this.eof()) {
            this.exceptionList.add(this.throwsSignature());
         }

      }

      Access parseReturnType() {
         if (this.next("V")) {
            this.eat("V");
            return new PrimitiveTypeAccess("void");
         } else {
            return this.typeSignature();
         }
      }

      Access throwsSignature() {
         this.eat("^");
         return this.next("L") ? this.classTypeSignature() : this.typeVariableSignature();
      }

      public boolean hasFormalTypeParameters() {
         return this.typeParameters != null;
      }

      public List typeParameters() {
         return this.typeParameters;
      }

      public Collection parameterTypes() {
         return this.parameterTypes;
      }

      public List exceptionList() {
         return this.exceptionList;
      }

      public boolean hasExceptionList() {
         return this.exceptionList.getNumChildNoTransform() != 0;
      }

      public boolean hasReturnType() {
         return this.returnType != null;
      }

      public Access returnType() {
         return this.returnType;
      }
   }

   public static class FieldSignature extends Signatures {
      private Access fieldTypeAccess = this.fieldTypeSignature();

      public FieldSignature(String s) {
         super(s);
      }

      Access fieldTypeAccess() {
         return this.fieldTypeAccess;
      }
   }

   public static class ClassSignature extends Signatures {
      protected Access superclassSignature;
      protected List superinterfaceSignature = new List();

      public ClassSignature(String s) {
         super(s);
         this.classSignature();
      }

      void classSignature() {
         if (this.next("<")) {
            this.formalTypeParameters();
         }

         this.superclassSignature = this.parseSuperclassSignature();

         while(!this.eof()) {
            this.superinterfaceSignature.add(this.parseSuperinterfaceSignature());
         }

      }

      public boolean hasFormalTypeParameters() {
         return this.typeParameters != null;
      }

      public List typeParameters() {
         return this.typeParameters;
      }

      public boolean hasSuperclassSignature() {
         return this.superclassSignature != null;
      }

      public Access superclassSignature() {
         return this.superclassSignature;
      }

      public boolean hasSuperinterfaceSignature() {
         return this.superinterfaceSignature.getNumChildNoTransform() != 0;
      }

      public List superinterfaceSignature() {
         return this.superinterfaceSignature;
      }

      Access parseSuperclassSignature() {
         return this.classTypeSignature();
      }

      Access parseSuperinterfaceSignature() {
         return this.classTypeSignature();
      }
   }
}
