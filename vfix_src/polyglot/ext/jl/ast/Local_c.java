package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Local;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Term;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.LocalInstance;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public class Local_c extends Expr_c implements Local {
   protected String name;
   protected LocalInstance li;

   public Local_c(Position pos, String name) {
      super(pos);
      this.name = name;
   }

   public Precedence precedence() {
      return Precedence.LITERAL;
   }

   public String name() {
      return this.name;
   }

   public Local name(String name) {
      Local_c n = (Local_c)this.copy();
      n.name = name;
      return n;
   }

   public Flags flags() {
      return this.li.flags();
   }

   public LocalInstance localInstance() {
      return this.li;
   }

   public Local localInstance(LocalInstance li) {
      Local_c n = (Local_c)this.copy();
      n.li = li;
      return n;
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      Local_c n = (Local_c)super.buildTypes(tb);
      TypeSystem ts = tb.typeSystem();
      LocalInstance li = ts.localInstance(this.position(), Flags.NONE, ts.unknownType(this.position()), this.name);
      return n.localInstance(li);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      Context c = tc.context();
      LocalInstance li = c.findLocal(this.name);
      if (!c.isLocal(li.name()) && !li.flags().isFinal()) {
         throw new SemanticException("Local variable \"" + li.name() + "\" is accessed from an inner class, and must be declared " + "final.", this.position());
      } else {
         return this.localInstance(li).type(li.type());
      }
   }

   public Term entry() {
      return this;
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      return succs;
   }

   public String toString() {
      return this.name;
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write(this.name);
   }

   public void dump(CodeWriter w) {
      super.dump(w);
      if (this.li != null) {
         w.allowBreak(4, " ");
         w.begin(0);
         w.write("(instance " + this.li + ")");
         w.end();
      }

      w.allowBreak(4, " ");
      w.begin(0);
      w.write("(name " + this.name + ")");
      w.end();
   }

   public boolean isConstant() {
      return this.li.isConstant();
   }

   public Object constantValue() {
      return this.li.constantValue();
   }
}
