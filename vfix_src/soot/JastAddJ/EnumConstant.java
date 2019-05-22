package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashMap;
import java.util.Iterator;

public class EnumConstant extends FieldDeclaration implements Cloneable {
   protected boolean getTypeAccess_computed;
   protected Access getTypeAccess_value;
   protected boolean localMethodsSignatureMap_computed;
   protected HashMap localMethodsSignatureMap_value;

   public void flushCache() {
      super.flushCache();
      this.getTypeAccess_computed = false;
      this.getTypeAccess_value = null;
      this.localMethodsSignatureMap_computed = false;
      this.localMethodsSignatureMap_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public EnumConstant clone() throws CloneNotSupportedException {
      EnumConstant node = (EnumConstant)super.clone();
      node.getTypeAccess_computed = false;
      node.getTypeAccess_value = null;
      node.localMethodsSignatureMap_computed = false;
      node.localMethodsSignatureMap_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public EnumConstant copy() {
      try {
         EnumConstant node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public EnumConstant fullCopy() {
      EnumConstant tree = this.copy();
      if (this.children != null) {
         for(int i = 0; i < this.children.length; ++i) {
            switch(i) {
            case 4:
               tree.children[i] = null;
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

   public EnumConstant(Modifiers mods, String name, List<Expr> args, List<BodyDecl> bds) {
      this(mods, name, args, new Opt(new EnumInstanceExpr(createOptAnonymousDecl(bds))));
   }

   private static Opt<TypeDecl> createOptAnonymousDecl(List<BodyDecl> bds) {
      return bds.getNumChildNoTransform() == 0 ? new Opt() : new Opt(new AnonymousDecl(new Modifiers(), "Anonymous", bds));
   }

   public int getNumBodyDecl() {
      int cnt = 0;
      ClassInstanceExpr init = (ClassInstanceExpr)this.getInit();
      if (!init.hasTypeDecl()) {
         return 0;
      } else {
         Iterator var3 = init.getTypeDecl().getBodyDecls().iterator();

         while(var3.hasNext()) {
            BodyDecl bd = (BodyDecl)var3.next();
            if (!(bd instanceof ConstructorDecl)) {
               ++cnt;
            }
         }

         return cnt;
      }
   }

   public BodyDecl getBodyDecl(int i) {
      ClassInstanceExpr init = (ClassInstanceExpr)this.getInit();
      if (init.hasTypeDecl()) {
         Iterator var3 = init.getTypeDecl().getBodyDecls().iterator();

         while(var3.hasNext()) {
            BodyDecl bd = (BodyDecl)var3.next();
            if (!(bd instanceof ConstructorDecl) && i-- == 0) {
               return bd;
            }
         }
      }

      throw new ArrayIndexOutOfBoundsException(i);
   }

   public void toString(StringBuffer s) {
      s.append(this.indent());
      this.getModifiers().toString(s);
      s.append(this.getID());
      s.append("(");
      int i;
      if (this.getNumArg() > 0) {
         this.getArg(0).toString(s);

         for(i = 1; i < this.getNumArg(); ++i) {
            s.append(", ");
            this.getArg(i).toString(s);
         }
      }

      s.append(")");
      if (this.getNumBodyDecl() > 0) {
         s.append(" {");

         for(i = 0; i < this.getNumBodyDecl(); ++i) {
            BodyDecl d = this.getBodyDecl(i);
            d.toString(s);
         }

         s.append(this.indent() + "}");
      }

      s.append(",\n");
   }

   public EnumConstant() {
      this.getTypeAccess_computed = false;
      this.localMethodsSignatureMap_computed = false;
   }

   public void init$Children() {
      this.children = new ASTNode[4];
      this.setChild(new List(), 1);
      this.setChild(new Opt(), 2);
   }

   public EnumConstant(Modifiers p0, String p1, List<Expr> p2, Opt<Expr> p3) {
      this.getTypeAccess_computed = false;
      this.localMethodsSignatureMap_computed = false;
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
   }

   public EnumConstant(Modifiers p0, Symbol p1, List<Expr> p2, Opt<Expr> p3) {
      this.getTypeAccess_computed = false;
      this.localMethodsSignatureMap_computed = false;
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
   }

   protected int numChildren() {
      return 3;
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

   public void setArgList(List<Expr> list) {
      this.setChild(list, 1);
   }

   public int getNumArg() {
      return this.getArgList().getNumChild();
   }

   public int getNumArgNoTransform() {
      return this.getArgListNoTransform().getNumChildNoTransform();
   }

   public Expr getArg(int i) {
      return (Expr)this.getArgList().getChild(i);
   }

   public void addArg(Expr node) {
      List<Expr> list = this.parent != null && state != null ? this.getArgList() : this.getArgListNoTransform();
      list.addChild(node);
   }

   public void addArgNoTransform(Expr node) {
      List<Expr> list = this.getArgListNoTransform();
      list.addChild(node);
   }

   public void setArg(Expr node, int i) {
      List<Expr> list = this.getArgList();
      list.setChild(node, i);
   }

   public List<Expr> getArgs() {
      return this.getArgList();
   }

   public List<Expr> getArgsNoTransform() {
      return this.getArgListNoTransform();
   }

   public List<Expr> getArgList() {
      List<Expr> list = (List)this.getChild(1);
      list.getNumChild();
      return list;
   }

   public List<Expr> getArgListNoTransform() {
      return (List)this.getChildNoTransform(1);
   }

   public void setInitOpt(Opt<Expr> opt) {
      this.setChild(opt, 2);
   }

   public boolean hasInit() {
      return this.getInitOpt().getNumChild() != 0;
   }

   public Expr getInit() {
      return (Expr)this.getInitOpt().getChild(0);
   }

   public void setInit(Expr node) {
      this.getInitOpt().setChild(node, 0);
   }

   public Opt<Expr> getInitOpt() {
      return (Opt)this.getChild(2);
   }

   public Opt<Expr> getInitOptNoTransform() {
      return (Opt)this.getChildNoTransform(2);
   }

   public void setTypeAccess(Access node) {
      this.setChild(node, 3);
   }

   public Access getTypeAccessNoTransform() {
      return (Access)this.getChildNoTransform(3);
   }

   protected int getTypeAccessChildPosition() {
      return 3;
   }

   public boolean isEnumConstant() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean isPublic() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean isStatic() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean isFinal() {
      ASTNode$State state = this.state();
      return true;
   }

   public Access getTypeAccess() {
      if (this.getTypeAccess_computed) {
         return (Access)this.getChild(this.getTypeAccessChildPosition());
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.getTypeAccess_value = this.getTypeAccess_compute();
         this.setTypeAccess(this.getTypeAccess_value);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.getTypeAccess_computed = true;
         }

         return (Access)this.getChild(this.getTypeAccessChildPosition());
      }
   }

   private Access getTypeAccess_compute() {
      return this.hostType().createQualifiedAccess();
   }

   public SimpleSet localMethodsSignature(String signature) {
      ASTNode$State state = this.state();
      SimpleSet set = (SimpleSet)this.localMethodsSignatureMap().get(signature);
      return set != null ? set : SimpleSet.emptySet;
   }

   public HashMap localMethodsSignatureMap() {
      if (this.localMethodsSignatureMap_computed) {
         return this.localMethodsSignatureMap_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.localMethodsSignatureMap_value = this.localMethodsSignatureMap_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.localMethodsSignatureMap_computed = true;
         }

         return this.localMethodsSignatureMap_value;
      }
   }

   private HashMap localMethodsSignatureMap_compute() {
      HashMap map = new HashMap(this.getNumBodyDecl());

      for(int i = 0; i < this.getNumBodyDecl(); ++i) {
         if (this.getBodyDecl(i) instanceof MethodDecl) {
            MethodDecl decl = (MethodDecl)this.getBodyDecl(i);
            map.put(decl.signature(), decl);
         }
      }

      return map;
   }

   public boolean implementsMethod(MethodDecl method) {
      ASTNode$State state = this.state();
      SimpleSet set = this.localMethodsSignature(method.signature());
      if (set.size() == 1) {
         MethodDecl n = (MethodDecl)set.iterator().next();
         if (!n.isAbstract()) {
            return true;
         }
      }

      return false;
   }

   public int sootTypeModifiers() {
      ASTNode$State state = this.state();
      return super.sootTypeModifiers() | 16384;
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getTypeAccessNoTransform() ? NameType.TYPE_NAME : super.Define_NameType_nameType(caller, child);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
