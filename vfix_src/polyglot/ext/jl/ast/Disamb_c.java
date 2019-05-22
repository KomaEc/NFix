package polyglot.ext.jl.ast;

import polyglot.ast.Ambiguous;
import polyglot.ast.Disamb;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.PackageNode;
import polyglot.ast.Prefix;
import polyglot.ast.QualifierNode;
import polyglot.ast.Receiver;
import polyglot.ast.TypeNode;
import polyglot.types.ClassType;
import polyglot.types.Context;
import polyglot.types.FieldInstance;
import polyglot.types.LocalInstance;
import polyglot.types.Named;
import polyglot.types.NoClassException;
import polyglot.types.NoMemberException;
import polyglot.types.Qualifier;
import polyglot.types.Resolver;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.types.VarInstance;
import polyglot.util.Position;
import polyglot.visit.ContextVisitor;

public class Disamb_c implements Disamb {
   protected ContextVisitor v;
   protected Position pos;
   protected Prefix prefix;
   protected String name;
   protected NodeFactory nf;
   protected TypeSystem ts;
   protected Context c;
   protected Ambiguous amb;

   public Node disambiguate(Ambiguous amb, ContextVisitor v, Position pos, Prefix prefix, String name) throws SemanticException {
      this.v = v;
      this.pos = pos;
      this.prefix = prefix;
      this.name = name;
      this.amb = amb;
      this.nf = v.nodeFactory();
      this.ts = v.typeSystem();
      this.c = v.context();
      if (prefix instanceof Ambiguous) {
         throw new SemanticException("Cannot disambiguate node with ambiguous prefix.");
      } else if (prefix instanceof PackageNode) {
         PackageNode pn = (PackageNode)prefix;
         return this.disambiguatePackagePrefix(pn);
      } else if (prefix instanceof TypeNode) {
         TypeNode tn = (TypeNode)prefix;
         return this.disambiguateTypeNodePrefix(tn);
      } else if (prefix instanceof Expr) {
         Expr e = (Expr)prefix;
         return this.disambiguateExprPrefix(e);
      } else {
         return prefix == null ? this.disambiguateNoPrefix() : null;
      }
   }

   protected Node disambiguatePackagePrefix(PackageNode pn) throws SemanticException {
      Resolver pc = this.ts.packageContextResolver(this.c.outerResolver(), pn.package_());
      Named n = pc.find(this.name);
      Qualifier q = null;
      if (n instanceof Qualifier) {
         q = (Qualifier)n;
         if (q.isPackage() && this.packageOK()) {
            return this.nf.PackageNode(this.pos, q.toPackage());
         } else {
            return q.isType() && this.typeOK() ? this.nf.CanonicalTypeNode(this.pos, q.toType()) : null;
         }
      } else {
         return null;
      }
   }

   protected Node disambiguateTypeNodePrefix(TypeNode tn) throws SemanticException {
      Type t = tn.type();
      if (t.isReference() && this.exprOK()) {
         try {
            FieldInstance fi = this.ts.findField(t.toReference(), this.name, this.c);
            return this.nf.Field(this.pos, tn, this.name).fieldInstance(fi);
         } catch (NoMemberException var6) {
            if (var6.getKind() != 3) {
               throw var6;
            }
         }
      }

      if (t.isClass() && this.typeOK()) {
         Resolver tc = this.ts.classContextResolver(t.toClass());
         Named n = tc.find(this.name);
         if (n instanceof Type) {
            Type type = (Type)n;
            return this.nf.CanonicalTypeNode(this.pos, type);
         }
      }

      return null;
   }

   protected Node disambiguateExprPrefix(Expr e) throws SemanticException {
      return this.exprOK() ? this.nf.Field(this.pos, e, this.name) : null;
   }

   protected Node disambiguateNoPrefix() throws SemanticException {
      VarInstance vi = this.c.findVariableSilent(this.name);
      if (vi != null && this.exprOK()) {
         Node n = this.disambiguateVarInstance(vi);
         if (n != null) {
            return n;
         }
      }

      if (this.typeOK()) {
         try {
            Named n = this.c.find(this.name);
            if (n instanceof Type) {
               Type type = (Type)n;
               return this.nf.CanonicalTypeNode(this.pos, type);
            }
         } catch (NoClassException var4) {
            if (!this.name.equals(var4.getClassName())) {
               throw var4;
            }
         }
      }

      return this.packageOK() ? this.nf.PackageNode(this.pos, this.ts.packageForName(this.name)) : null;
   }

   protected Node disambiguateVarInstance(VarInstance vi) throws SemanticException {
      if (vi instanceof FieldInstance) {
         FieldInstance fi = (FieldInstance)vi;
         Receiver r = this.makeMissingFieldTarget(fi);
         return this.nf.Field(this.pos, r, this.name).fieldInstance(fi).targetImplicit(true);
      } else if (vi instanceof LocalInstance) {
         LocalInstance li = (LocalInstance)vi;
         return this.nf.Local(this.pos, this.name).localInstance(li);
      } else {
         return null;
      }
   }

   protected Receiver makeMissingFieldTarget(FieldInstance fi) throws SemanticException {
      Object r;
      if (fi.flags().isStatic()) {
         r = this.nf.CanonicalTypeNode(this.pos, fi.container());
      } else {
         ClassType scope = this.c.findFieldScope(this.name);
         if (!this.ts.equals(scope, this.c.currentClass())) {
            r = this.nf.This(this.pos, this.nf.CanonicalTypeNode(this.pos, scope));
         } else {
            r = this.nf.This(this.pos);
         }
      }

      return (Receiver)r;
   }

   protected boolean typeOK() {
      return !(this.amb instanceof Expr) && (this.amb instanceof TypeNode || this.amb instanceof QualifierNode || this.amb instanceof Receiver || this.amb instanceof Prefix);
   }

   protected boolean packageOK() {
      return !(this.amb instanceof Receiver) && (this.amb instanceof QualifierNode || this.amb instanceof Prefix);
   }

   protected boolean exprOK() {
      return !(this.amb instanceof QualifierNode) && (this.amb instanceof Expr || this.amb instanceof Receiver || this.amb instanceof Prefix);
   }
}
