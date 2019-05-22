package soot.JastAddJ;

import beaver.Symbol;
import soot.SootClass;

public abstract class AbstractWildcardType extends TypeDecl implements Cloneable {
   protected boolean getSootClassDecl_computed = false;
   protected SootClass getSootClassDecl_value;

   public void flushCache() {
      super.flushCache();
      this.getSootClassDecl_computed = false;
      this.getSootClassDecl_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public AbstractWildcardType clone() throws CloneNotSupportedException {
      AbstractWildcardType node = (AbstractWildcardType)super.clone();
      node.getSootClassDecl_computed = false;
      node.getSootClassDecl_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public Access createQualifiedAccess() {
      return this.createBoundAccess();
   }

   public AbstractWildcardType() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
      this.setChild(new List(), 1);
   }

   public AbstractWildcardType(Modifiers p0, String p1, List<BodyDecl> p2) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
   }

   public AbstractWildcardType(Modifiers p0, Symbol p1, List<BodyDecl> p2) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
   }

   protected int numChildren() {
      return 2;
   }

   public boolean mayHaveRewrite() {
      return false;
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

   public boolean isWildcard() {
      ASTNode$State state = this.state();
      return true;
   }

   public SootClass getSootClassDecl() {
      if (this.getSootClassDecl_computed) {
         return this.getSootClassDecl_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.getSootClassDecl_value = this.getSootClassDecl_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.getSootClassDecl_computed = true;
         }

         return this.getSootClassDecl_value;
      }
   }

   private SootClass getSootClassDecl_compute() {
      return this.typeObject().getSootClassDecl();
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
