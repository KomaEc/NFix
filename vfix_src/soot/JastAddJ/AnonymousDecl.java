package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashSet;
import java.util.Iterator;

public class AnonymousDecl extends ClassDecl implements Cloneable {
   protected int isCircular_visited = -1;
   protected boolean isCircular_computed = false;
   protected boolean isCircular_initialized = false;
   protected boolean isCircular_value;
   protected boolean getSuperClassAccessOpt_computed = false;
   protected Opt getSuperClassAccessOpt_value;
   protected boolean getImplementsList_computed = false;
   protected List getImplementsList_value;

   public void flushCache() {
      super.flushCache();
      this.isCircular_visited = -1;
      this.isCircular_computed = false;
      this.isCircular_initialized = false;
      this.getSuperClassAccessOpt_computed = false;
      this.getSuperClassAccessOpt_value = null;
      this.getImplementsList_computed = false;
      this.getImplementsList_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public AnonymousDecl clone() throws CloneNotSupportedException {
      AnonymousDecl node = (AnonymousDecl)super.clone();
      node.isCircular_visited = -1;
      node.isCircular_computed = false;
      node.isCircular_initialized = false;
      node.getSuperClassAccessOpt_computed = false;
      node.getSuperClassAccessOpt_value = null;
      node.getImplementsList_computed = false;
      node.getImplementsList_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public AnonymousDecl copy() {
      try {
         AnonymousDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public AnonymousDecl fullCopy() {
      AnonymousDecl tree = this.copy();
      if (this.children != null) {
         for(int i = 0; i < this.children.length; ++i) {
            switch(i) {
            case 3:
               tree.children[i] = new Opt();
               break;
            case 4:
               tree.children[i] = new List();
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

   public AnonymousDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[4];
      this.setChild(new List(), 1);
      this.setChild(new Opt(), 2);
      this.setChild(new List(), 3);
   }

   public AnonymousDecl(Modifiers p0, String p1, List<BodyDecl> p2) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
   }

   public AnonymousDecl(Modifiers p0, Symbol p1, List<BodyDecl> p2) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
   }

   protected int numChildren() {
      return 2;
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

   public void setBodyDeclList(List<BodyDecl> list) {
      this.setChild(list, 1);
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
      List<BodyDecl> list = (List)this.getChild(1);
      list.getNumChild();
      return list;
   }

   public List<BodyDecl> getBodyDeclListNoTransform() {
      return (List)this.getChildNoTransform(1);
   }

   public void setSuperClassAccessOpt(Opt<Access> opt) {
      this.setChild(opt, 2);
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
      return (Opt)this.getChildNoTransform(2);
   }

   protected int getSuperClassAccessOptChildPosition() {
      return 2;
   }

   public void setImplementsList(List<Access> list) {
      this.setChild(list, 3);
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

   public List<Access> getImplementsListNoTransform() {
      return (List)this.getChildNoTransform(3);
   }

   protected int getImplementsListChildPosition() {
      return 3;
   }

   protected List constructorParameterList(ConstructorDecl decl) {
      List parameterList = new List();

      for(int i = 0; i < decl.getNumParameter(); ++i) {
         ParameterDeclaration param = decl.getParameter(i);
         if (param instanceof VariableArityParameterDeclaration) {
            parameterList.add(new VariableArityParameterDeclaration(new Modifiers(new List()), ((ArrayDecl)param.type()).componentType().createBoundAccess(), param.name()));
         } else {
            parameterList.add(new ParameterDeclaration(param.type().createBoundAccess(), param.name()));
         }
      }

      return parameterList;
   }

   public boolean isCircular() {
      if (this.isCircular_computed) {
         return this.isCircular_value;
      } else {
         ASTNode$State state = this.state();
         if (!this.isCircular_initialized) {
            this.isCircular_initialized = true;
            this.isCircular_value = true;
         }

         if (state.IN_CIRCLE) {
            if (this.isCircular_visited != state.CIRCLE_INDEX) {
               this.isCircular_visited = state.CIRCLE_INDEX;
               if (state.RESET_CYCLE) {
                  this.isCircular_computed = false;
                  this.isCircular_initialized = false;
                  this.isCircular_visited = -1;
                  return this.isCircular_value;
               } else {
                  boolean new_isCircular_value = this.isCircular_compute();
                  if (new_isCircular_value != this.isCircular_value) {
                     state.CHANGE = true;
                  }

                  this.isCircular_value = new_isCircular_value;
                  return this.isCircular_value;
               }
            } else {
               return this.isCircular_value;
            }
         } else {
            state.IN_CIRCLE = true;
            int num = state.boundariesCrossed;
            boolean isFinal = this.is$Final();

            do {
               this.isCircular_visited = state.CIRCLE_INDEX;
               state.CHANGE = false;
               boolean new_isCircular_value = this.isCircular_compute();
               if (new_isCircular_value != this.isCircular_value) {
                  state.CHANGE = true;
               }

               this.isCircular_value = new_isCircular_value;
               ++state.CIRCLE_INDEX;
            } while(state.CHANGE);

            if (isFinal && num == this.state().boundariesCrossed) {
               this.isCircular_computed = true;
            } else {
               state.RESET_CYCLE = true;
               this.isCircular_compute();
               state.RESET_CYCLE = false;
               this.isCircular_computed = false;
               this.isCircular_initialized = false;
            }

            state.IN_CIRCLE = false;
            return this.isCircular_value;
         }
      }
   }

   private boolean isCircular_compute() {
      return false;
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
      return this.superType().isInterfaceDecl() ? new Opt(this.typeObject().createQualifiedAccess()) : new Opt(this.superType().createBoundAccess());
   }

   public List getImplementsList() {
      if (this.getImplementsList_computed) {
         return (List)this.getChild(this.getImplementsListChildPosition());
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.getImplementsList_value = this.getImplementsList_compute();
         this.setImplementsList(this.getImplementsList_value);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.getImplementsList_computed = true;
         }

         return (List)this.getChild(this.getImplementsListChildPosition());
      }
   }

   private List getImplementsList_compute() {
      return this.superType().isInterfaceDecl() ? (new List()).add(this.superType().createBoundAccess()) : new List();
   }

   public TypeDecl superType() {
      ASTNode$State state = this.state();
      TypeDecl superType_value = this.getParent().Define_TypeDecl_superType(this, (ASTNode)null);
      return superType_value;
   }

   public ConstructorDecl constructorDecl() {
      ASTNode$State state = this.state();
      ConstructorDecl constructorDecl_value = this.getParent().Define_ConstructorDecl_constructorDecl(this, (ASTNode)null);
      return constructorDecl_value;
   }

   public TypeDecl typeNullPointerException() {
      ASTNode$State state = this.state();
      TypeDecl typeNullPointerException_value = this.getParent().Define_TypeDecl_typeNullPointerException(this, (ASTNode)null);
      return typeNullPointerException_value;
   }

   public ASTNode rewriteTo() {
      if (this.noConstructor()) {
         ++this.state().duringAnonymousClasses;
         ASTNode result = this.rewriteRule0();
         --this.state().duringAnonymousClasses;
         return result;
      } else {
         return super.rewriteTo();
      }
   }

   private AnonymousDecl rewriteRule0() {
      this.setModifiers(new Modifiers((new List()).add(new Modifier("final"))));
      ConstructorDecl decl = this.constructorDecl();
      Modifiers modifiers = decl.getModifiers().fullCopy();
      String anonName = "Anonymous" + this.nextAnonymousIndex();
      ConstructorDecl constructor = new ConstructorDecl(modifiers, anonName, this.constructorParameterList(decl), new List(), new Opt(), new Block());
      constructor.setDefaultConstructor();
      this.addBodyDecl(constructor);
      this.setID(anonName);
      List argList = new List();

      for(int i = 0; i < constructor.getNumParameter(); ++i) {
         argList.add(new VarAccess(constructor.getParameter(i).name()));
      }

      constructor.setConstructorInvocation(new ExprStmt(new SuperConstructorAccess("super", argList)));
      HashSet set = new HashSet();

      for(int i = 0; i < this.getNumBodyDecl(); ++i) {
         if (this.getBodyDecl(i) instanceof InstanceInitializer) {
            InstanceInitializer init = (InstanceInitializer)this.getBodyDecl(i);
            set.addAll(init.exceptions());
         } else if (this.getBodyDecl(i) instanceof FieldDeclaration) {
            FieldDeclaration f = (FieldDeclaration)this.getBodyDecl(i);
            if (f.isInstanceVariable()) {
               set.addAll(f.exceptions());
            }
         }
      }

      List exceptionList = new List();

      TypeDecl exceptionType;
      for(Iterator iter = set.iterator(); iter.hasNext(); exceptionList.add(exceptionType.createQualifiedAccess())) {
         exceptionType = (TypeDecl)iter.next();
         if (exceptionType.isNull()) {
            exceptionType = this.typeNullPointerException();
         }
      }

      constructor.setExceptionList(exceptionList);
      return this;
   }
}
