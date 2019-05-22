package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class EnumDecl extends ClassDecl implements Cloneable {
   private boolean done = false;
   protected boolean isStatic_computed = false;
   protected boolean isStatic_value;
   protected boolean getSuperClassAccessOpt_computed = false;
   protected Opt getSuperClassAccessOpt_value;
   protected boolean enumConstants_computed = false;
   protected ArrayList enumConstants_value;
   protected boolean unimplementedMethods_computed = false;
   protected Collection unimplementedMethods_value;

   public void flushCache() {
      super.flushCache();
      this.isStatic_computed = false;
      this.getSuperClassAccessOpt_computed = false;
      this.getSuperClassAccessOpt_value = null;
      this.enumConstants_computed = false;
      this.enumConstants_value = null;
      this.unimplementedMethods_computed = false;
      this.unimplementedMethods_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public EnumDecl clone() throws CloneNotSupportedException {
      EnumDecl node = (EnumDecl)super.clone();
      node.isStatic_computed = false;
      node.getSuperClassAccessOpt_computed = false;
      node.getSuperClassAccessOpt_value = null;
      node.enumConstants_computed = false;
      node.enumConstants_value = null;
      node.unimplementedMethods_computed = false;
      node.unimplementedMethods_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public EnumDecl copy() {
      try {
         EnumDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public EnumDecl fullCopy() {
      EnumDecl tree = this.copy();
      if (this.children != null) {
         for(int i = 0; i < this.children.length; ++i) {
            switch(i) {
            case 4:
               tree.children[i] = new Opt();
               break;
            default:
               ASTNode child = this.children[i];
               if (child != null) {
                  child = child.fullCopy();
                  tree.setChild(child, i);
               }
            }
         }
      }

      return tree;
   }

   public void typeCheck() {
      super.typeCheck();
      Iterator iter = this.memberMethods("finalize").iterator();

      while(iter.hasNext()) {
         MethodDecl m = (MethodDecl)iter.next();
         if (m.getNumParameter() == 0 && m.hostType() == this) {
            this.error("an enum may not declare a finalizer");
         }
      }

      this.checkEnum(this);
   }

   private boolean done() {
      if (this.done) {
         return true;
      } else {
         this.done = true;
         return false;
      }
   }

   private void addValues() {
      int numConstants = this.enumConstants().size();
      List initValues = new List();
      Iterator iter = this.enumConstants().iterator();

      while(iter.hasNext()) {
         EnumConstant c = (EnumConstant)iter.next();
         initValues.add(c.createBoundFieldAccess());
      }

      FieldDeclaration values = new FieldDeclaration(new Modifiers((new List()).add(new Modifier("private")).add(new Modifier("static")).add(new Modifier("final")).add(new Modifier("synthetic"))), this.arrayType().createQualifiedAccess(), "$VALUES", new Opt(new ArrayCreationExpr(new ArrayTypeWithSizeAccess(this.createQualifiedAccess(), Literal.buildIntegerLiteral(this.enumConstants().size())), new Opt(new ArrayInit(initValues)))));
      this.addBodyDecl(values);
      this.addBodyDecl(new MethodDecl(new Modifiers((new List()).add(new Modifier("public")).add(new Modifier("static")).add(new Modifier("final")).add(new Modifier("synthetic"))), this.arrayType().createQualifiedAccess(), "values", new List(), new List(), new Opt(new Block((new List()).add(new ReturnStmt(new Opt(new CastExpr(this.arrayType().createQualifiedAccess(), values.createBoundFieldAccess().qualifiesAccess(new MethodAccess("clone", new List()))))))))));
      this.addBodyDecl(new MethodDecl(new Modifiers((new List()).add(new Modifier("public")).add(new Modifier("static")).add(new Modifier("synthetic"))), this.createQualifiedAccess(), "valueOf", (new List()).add(new ParameterDeclaration(new Modifiers(new List()), this.typeString().createQualifiedAccess(), "s")), new List(), new Opt(new Block((new List()).add(new ReturnStmt(new Opt(new CastExpr(this.createQualifiedAccess(), this.lookupType("java.lang", "Enum").createQualifiedAccess().qualifiesAccess(new MethodAccess("valueOf", (new List()).add(this.createQualifiedAccess().qualifiesAccess(new ClassAccess())).add(new VarAccess("s"))))))))))));
   }

   protected void checkEnum(EnumDecl enumDecl) {
      for(int i = 0; i < this.getNumBodyDecl(); ++i) {
         if (this.getBodyDecl(i) instanceof ConstructorDecl) {
            this.getBodyDecl(i).checkEnum(enumDecl);
         } else if (this.getBodyDecl(i) instanceof InstanceInitializer) {
            this.getBodyDecl(i).checkEnum(enumDecl);
         } else if (this.getBodyDecl(i) instanceof FieldDeclaration) {
            FieldDeclaration f = (FieldDeclaration)this.getBodyDecl(i);
            if (!f.isStatic() && f.hasInit()) {
               f.checkEnum(enumDecl);
            }
         }
      }

   }

   public void toString(StringBuffer s) {
      this.getModifiers().toString(s);
      s.append("enum " + this.name());
      int i;
      if (this.getNumImplements() > 0) {
         s.append(" implements ");
         this.getImplements(0).toString(s);

         for(i = 1; i < this.getNumImplements(); ++i) {
            s.append(", ");
            this.getImplements(i).toString(s);
         }
      }

      s.append(" {");

      for(i = 0; i < this.getNumBodyDecl(); ++i) {
         BodyDecl d = this.getBodyDecl(i);
         if (d instanceof EnumConstant) {
            d.toString(s);
            if (i + 1 < this.getNumBodyDecl() && !(this.getBodyDecl(i + 1) instanceof EnumConstant)) {
               s.append(this.indent() + ";");
            }
         } else if (d instanceof ConstructorDecl) {
            ConstructorDecl c = (ConstructorDecl)d;
            if (!c.isSynthetic()) {
               s.append(this.indent());
               c.getModifiers().toString(s);
               s.append(c.name() + "(");
               int j;
               if (c.getNumParameter() > 2) {
                  c.getParameter(2).toString(s);

                  for(j = 3; j < c.getNumParameter(); ++j) {
                     s.append(", ");
                     c.getParameter(j).toString(s);
                  }
               }

               s.append(")");
               if (c.getNumException() > 0) {
                  s.append(" throws ");
                  c.getException(0).toString(s);

                  for(j = 1; j < c.getNumException(); ++j) {
                     s.append(", ");
                     c.getException(j).toString(s);
                  }
               }

               s.append(" {");

               for(j = 0; j < c.getBlock().getNumStmt(); ++j) {
                  c.getBlock().getStmt(j).toString(s);
               }

               s.append(this.indent());
               s.append("}");
            }
         } else if (d instanceof MethodDecl) {
            MethodDecl m = (MethodDecl)d;
            if (!m.isSynthetic()) {
               m.toString(s);
            }
         } else if (d instanceof FieldDeclaration) {
            FieldDeclaration f = (FieldDeclaration)d;
            if (!f.isSynthetic()) {
               f.toString(s);
            }
         } else {
            d.toString(s);
         }
      }

      s.append(this.indent() + "}");
   }

   public void checkModifiers() {
      super.checkModifiers();
      if (!this.unimplementedMethods().isEmpty()) {
         StringBuffer s = new StringBuffer();
         s.append("" + this.name() + " lacks implementations in one or more enum constants for the following methods:\n");
         Iterator iter = this.unimplementedMethods().iterator();

         while(iter.hasNext()) {
            MethodDecl m = (MethodDecl)iter.next();
            s.append("  " + m.signature() + " in " + m.hostType().typeName() + "\n");
         }

         this.error(s.toString());
      }

   }

   public EnumDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[4];
      this.setChild(new List(), 1);
      this.setChild(new List(), 2);
      this.setChild(new Opt(), 3);
   }

   public EnumDecl(Modifiers p0, String p1, List<Access> p2, List<BodyDecl> p3) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
   }

   public EnumDecl(Modifiers p0, Symbol p1, List<Access> p2, List<BodyDecl> p3) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
   }

   protected int numChildren() {
      return 3;
   }

   public boolean mayHaveRewrite() {
      return true;
   }

   public void setModifiers(Modifiers node) {
      this.setChild(node, 0);
   }

   public Modifiers getModifiers() {
      return (Modifiers)this.getChild(0);
   }

   public Modifiers getModifiersNoTransform() {
      return (Modifiers)this.getChildNoTransform(0);
   }

   public void setID(String value) {
      this.tokenString_ID = value;
   }

   public void setID(Symbol symbol) {
      if (symbol.value != null && !(symbol.value instanceof String)) {
         throw new UnsupportedOperationException("setID is only valid for String lexemes");
      } else {
         this.tokenString_ID = (String)symbol.value;
         this.IDstart = symbol.getStart();
         this.IDend = symbol.getEnd();
      }
   }

   public String getID() {
      return this.tokenString_ID != null ? this.tokenString_ID : "";
   }

   public void setImplementsList(List<Access> list) {
      this.setChild(list, 1);
   }

   public int getNumImplements() {
      return this.getImplementsList().getNumChild();
   }

   public int getNumImplementsNoTransform() {
      return this.getImplementsListNoTransform().getNumChildNoTransform();
   }

   public Access getImplements(int i) {
      return (Access)this.getImplementsList().getChild(i);
   }

   public void addImplements(Access node) {
      List<Access> list = this.parent != null && state != null ? this.getImplementsList() : this.getImplementsListNoTransform();
      list.addChild(node);
   }

   public void addImplementsNoTransform(Access node) {
      List<Access> list = this.getImplementsListNoTransform();
      list.addChild(node);
   }

   public void setImplements(Access node, int i) {
      List<Access> list = this.getImplementsList();
      list.setChild(node, i);
   }

   public List<Access> getImplementss() {
      return this.getImplementsList();
   }

   public List<Access> getImplementssNoTransform() {
      return this.getImplementsListNoTransform();
   }

   public List<Access> getImplementsList() {
      List<Access> list = (List)this.getChild(1);
      list.getNumChild();
      return list;
   }

   public List<Access> getImplementsListNoTransform() {
      return (List)this.getChildNoTransform(1);
   }

   public void setBodyDeclList(List<BodyDecl> list) {
      this.setChild(list, 2);
   }

   public int getNumBodyDecl() {
      return this.getBodyDeclList().getNumChild();
   }

   public int getNumBodyDeclNoTransform() {
      return this.getBodyDeclListNoTransform().getNumChildNoTransform();
   }

   public BodyDecl getBodyDecl(int i) {
      return (BodyDecl)this.getBodyDeclList().getChild(i);
   }

   public void addBodyDecl(BodyDecl node) {
      List<BodyDecl> list = this.parent != null && state != null ? this.getBodyDeclList() : this.getBodyDeclListNoTransform();
      list.addChild(node);
   }

   public void addBodyDeclNoTransform(BodyDecl node) {
      List<BodyDecl> list = this.getBodyDeclListNoTransform();
      list.addChild(node);
   }

   public void setBodyDecl(BodyDecl node, int i) {
      List<BodyDecl> list = this.getBodyDeclList();
      list.setChild(node, i);
   }

   public List<BodyDecl> getBodyDecls() {
      return this.getBodyDeclList();
   }

   public List<BodyDecl> getBodyDeclsNoTransform() {
      return this.getBodyDeclListNoTransform();
   }

   public List<BodyDecl> getBodyDeclList() {
      List<BodyDecl> list = (List)this.getChild(2);
      list.getNumChild();
      return list;
   }

   public List<BodyDecl> getBodyDeclListNoTransform() {
      return (List)this.getChildNoTransform(2);
   }

   public void setSuperClassAccessOpt(Opt<Access> opt) {
      this.setChild(opt, 3);
   }

   public boolean hasSuperClassAccess() {
      return this.getSuperClassAccessOpt().getNumChild() != 0;
   }

   public Access getSuperClassAccess() {
      return (Access)this.getSuperClassAccessOpt().getChild(0);
   }

   public void setSuperClassAccess(Access node) {
      this.getSuperClassAccessOpt().setChild(node, 0);
   }

   public Opt<Access> getSuperClassAccessOptNoTransform() {
      return (Opt)this.getChildNoTransform(3);
   }

   protected int getSuperClassAccessOptChildPosition() {
      return 3;
   }

   public boolean isValidAnnotationMethodReturnType() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean isEnumDecl() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean isStatic() {
      if (this.isStatic_computed) {
         return this.isStatic_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isStatic_value = this.isStatic_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isStatic_computed = true;
         }

         return this.isStatic_value;
      }
   }

   private boolean isStatic_compute() {
      return this.isNestedType();
   }

   public Opt getSuperClassAccessOpt() {
      if (this.getSuperClassAccessOpt_computed) {
         return (Opt)this.getChild(this.getSuperClassAccessOptChildPosition());
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.getSuperClassAccessOpt_value = this.getSuperClassAccessOpt_compute();
         this.setSuperClassAccessOpt(this.getSuperClassAccessOpt_value);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.getSuperClassAccessOpt_computed = true;
         }

         return (Opt)this.getChild(this.getSuperClassAccessOptChildPosition());
      }
   }

   private Opt getSuperClassAccessOpt_compute() {
      return new Opt(new ParTypeAccess(new TypeAccess("java.lang", "Enum"), (new List()).add(this.createQualifiedAccess())));
   }

   public boolean isFinal() {
      ASTNode$State state = this.state();
      Iterator iter = this.enumConstants().iterator();

      ClassInstanceExpr e;
      do {
         if (!iter.hasNext()) {
            return true;
         }

         EnumConstant c = (EnumConstant)iter.next();
         e = (ClassInstanceExpr)c.getInit();
      } while(!e.hasTypeDecl());

      return false;
   }

   public ArrayList enumConstants() {
      if (this.enumConstants_computed) {
         return this.enumConstants_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.enumConstants_value = this.enumConstants_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.enumConstants_computed = true;
         }

         return this.enumConstants_value;
      }
   }

   private ArrayList enumConstants_compute() {
      ArrayList list = new ArrayList();

      for(int i = 0; i < this.getNumBodyDecl(); ++i) {
         if (this.getBodyDecl(i).isEnumConstant()) {
            list.add(this.getBodyDecl(i));
         }
      }

      return list;
   }

   public boolean isAbstract() {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getNumBodyDecl(); ++i) {
         if (this.getBodyDecl(i) instanceof MethodDecl) {
            MethodDecl m = (MethodDecl)this.getBodyDecl(i);
            if (m.isAbstract()) {
               return true;
            }
         }
      }

      return false;
   }

   public Collection unimplementedMethods() {
      if (this.unimplementedMethods_computed) {
         return this.unimplementedMethods_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.unimplementedMethods_value = this.unimplementedMethods_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.unimplementedMethods_computed = true;
         }

         return this.unimplementedMethods_value;
      }
   }

   private Collection unimplementedMethods_compute() {
      Collection<MethodDecl> methods = new LinkedList();
      Iterator iter = this.interfacesMethodsIterator();

      while(true) {
         MethodDecl method;
         SimpleSet set;
         MethodDecl n;
         Iterator iter;
         boolean missing;
         do {
            if (!iter.hasNext()) {
               iter = this.localMethodsIterator();

               while(iter.hasNext()) {
                  method = (MethodDecl)iter.next();
                  if (method.isAbstract()) {
                     methods.add(method);
                  }
               }

               Collection unimplemented = new ArrayList();
               Iterator var9 = methods.iterator();

               while(true) {
                  while(var9.hasNext()) {
                     MethodDecl method = (MethodDecl)var9.next();
                     if (this.enumConstants().isEmpty()) {
                        unimplemented.add(method);
                     } else {
                        missing = false;
                        iter = this.enumConstants().iterator();

                        while(iter.hasNext()) {
                           if (!((EnumConstant)iter.next()).implementsMethod(method)) {
                              missing = true;
                              break;
                           }
                        }

                        if (missing) {
                           unimplemented.add(method);
                        }
                     }
                  }

                  return unimplemented;
               }
            }

            method = (MethodDecl)iter.next();
            set = this.localMethodsSignature(method.signature());
            if (set.size() != 1) {
               break;
            }

            n = (MethodDecl)set.iterator().next();
         } while(!n.isAbstract());

         missing = false;
         set = this.ancestorMethods(method.signature());
         iter = set.iterator();

         while(iter.hasNext()) {
            MethodDecl n = (MethodDecl)iter.next();
            if (!n.isAbstract()) {
               missing = true;
               break;
            }
         }

         if (!missing) {
            methods.add(method);
         }
      }
   }

   public int sootTypeModifiers() {
      ASTNode$State state = this.state();
      return super.sootTypeModifiers() | 16384;
   }

   public TypeDecl typeString() {
      ASTNode$State state = this.state();
      TypeDecl typeString_value = this.getParent().Define_TypeDecl_typeString(this, (ASTNode)null);
      return typeString_value;
   }

   public boolean Define_boolean_mayBeAbstract(ASTNode caller, ASTNode child) {
      return caller == this.getModifiersNoTransform() ? false : super.Define_boolean_mayBeAbstract(caller, child);
   }

   public boolean Define_boolean_mayBeStatic(ASTNode caller, ASTNode child) {
      return caller == this.getModifiersNoTransform() ? this.isNestedType() : super.Define_boolean_mayBeStatic(caller, child);
   }

   public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
      return caller == this.getModifiersNoTransform() ? false : super.Define_boolean_mayBeFinal(caller, child);
   }

   public ASTNode rewriteTo() {
      if (!this.done()) {
         ++this.state().duringEnums;
         ASTNode result = this.rewriteRule0();
         --this.state().duringEnums;
         return result;
      } else {
         return super.rewriteTo();
      }
   }

   private EnumDecl rewriteRule0() {
      if (this.noConstructor()) {
         List parameterList = new List();
         parameterList.add(new ParameterDeclaration(new TypeAccess("java.lang", "String"), "p0"));
         parameterList.add(new ParameterDeclaration(new TypeAccess("int"), "p1"));
         this.addBodyDecl(new ConstructorDecl(new Modifiers((new List()).add(new Modifier("private")).add(new Modifier("synthetic"))), this.name(), parameterList, new List(), new Opt(new ExprStmt(new SuperConstructorAccess("super", (new List()).add(new VarAccess("p0")).add(new VarAccess("p1"))))), new Block(new List())));
      } else {
         this.transformEnumConstructors();
      }

      this.addValues();
      return this;
   }
}
