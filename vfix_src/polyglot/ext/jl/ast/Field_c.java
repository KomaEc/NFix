package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.List;
import polyglot.ast.AmbReceiver;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Receiver;
import polyglot.ast.Special;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.types.Context;
import polyglot.types.FieldInstance;
import polyglot.types.Flags;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.types.VarInstance;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public class Field_c extends Expr_c implements Field {
   protected Receiver target;
   protected String name;
   protected FieldInstance fi;
   protected boolean targetImplicit;

   public Field_c(Position pos, Receiver target, String name) {
      super(pos);
      this.target = target;
      this.name = name;
      this.targetImplicit = false;
      if (target == null) {
         throw new InternalCompilerError("Cannot create a field with a null target.  Use AmbExpr or prefix with the appropriate type node or this.");
      }
   }

   public Precedence precedence() {
      return Precedence.LITERAL;
   }

   public Receiver target() {
      return this.target;
   }

   public Field target(Receiver target) {
      Field_c n = (Field_c)this.copy();
      n.target = target;
      return n;
   }

   public String name() {
      return this.name;
   }

   public Field name(String name) {
      Field_c n = (Field_c)this.copy();
      n.name = name;
      return n;
   }

   public Flags flags() {
      return this.fi.flags();
   }

   public FieldInstance fieldInstance() {
      return this.fi;
   }

   public Field fieldInstance(FieldInstance fi) {
      Field_c n = (Field_c)this.copy();
      n.fi = fi;
      return n;
   }

   public boolean isTargetImplicit() {
      return this.targetImplicit;
   }

   public Field targetImplicit(boolean implicit) {
      Field_c n = (Field_c)this.copy();
      n.targetImplicit = implicit;
      return n;
   }

   protected Field_c reconstruct(Receiver target) {
      if (target != this.target) {
         Field_c n = (Field_c)this.copy();
         n.target = target;
         return n;
      } else {
         return this;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      Receiver target = (Receiver)this.visitChild(this.target, v);
      return this.reconstruct(target);
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      Field_c n = (Field_c)super.buildTypes(tb);
      TypeSystem ts = tb.typeSystem();
      FieldInstance fi = ts.fieldInstance(this.position(), ts.Object(), Flags.NONE, ts.unknownType(this.position()), this.name);
      return n.fieldInstance(fi);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      Context c = tc.context();
      TypeSystem ts = tc.typeSystem();
      if (!this.target.type().isReference()) {
         throw new SemanticException("Cannot access field \"" + this.name + "\" " + (this.target instanceof Expr ? "on an expression " : "") + "of non-reference type \"" + this.target.type() + "\".", this.target.position());
      } else {
         FieldInstance fi = ts.findField(this.target.type().toReference(), this.name, c.currentClass());
         if (fi == null) {
            throw new InternalCompilerError("Cannot access field on node of type " + this.target.getClass().getName() + ".");
         } else {
            Field_c f = (Field_c)this.fieldInstance(fi).type(fi.type());
            f.checkConsistency(c);
            return f;
         }
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      return (Type)(child == this.target ? this.fi.container() : child.type());
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      if (!this.targetImplicit) {
         if (this.target instanceof Expr) {
            this.printSubExpr((Expr)this.target, w, tr);
         } else if (this.target instanceof TypeNode || this.target instanceof AmbReceiver) {
            this.print(this.target, w, tr);
         }

         w.write(".");
      }

      w.write(this.name);
   }

   public void dump(CodeWriter w) {
      super.dump(w);
      w.allowBreak(4, " ");
      w.begin(0);
      w.write("(name \"" + this.name + "\")");
      w.end();
   }

   public Term entry() {
      return (Term)(this.target instanceof Expr ? ((Expr)this.target).entry() : this);
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      if (this.target instanceof Expr) {
         v.visitCFG((Expr)this.target, (Term)this);
      }

      return succs;
   }

   public String toString() {
      return (this.target != null && !this.targetImplicit ? this.target + "." : "") + this.name;
   }

   public List throwTypes(TypeSystem ts) {
      return this.target instanceof Expr && !(this.target instanceof Special) ? Collections.singletonList(ts.NullPointerException()) : Collections.EMPTY_LIST;
   }

   public boolean isConstant() {
      return this.fi == null || !(this.target instanceof TypeNode) && (!(this.target instanceof Special) || !this.targetImplicit) ? false : this.fi.isConstant();
   }

   public Object constantValue() {
      return this.isConstant() ? this.fi.constantValue() : null;
   }

   protected void checkConsistency(Context c) {
      if (this.targetImplicit) {
         VarInstance vi = c.findVariableSilent(this.name);
         if (vi instanceof FieldInstance) {
            FieldInstance rfi = (FieldInstance)vi;
            if (c.typeSystem().equals(rfi, this.fi)) {
               return;
            }
         }

         throw new InternalCompilerError("Field " + this + " has an " + "implicit target, but the name " + this.name + " resolves to " + vi + " instead of " + this.target, this.position());
      }
   }
}
