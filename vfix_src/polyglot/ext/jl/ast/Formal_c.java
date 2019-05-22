package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Formal;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.LocalInstance;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public class Formal_c extends Term_c implements Formal {
   protected LocalInstance li;
   protected Flags flags;
   protected TypeNode type;
   protected String name;
   boolean reachable;

   public Formal_c(Position pos, Flags flags, TypeNode type, String name) {
      super(pos);
      this.flags = flags;
      this.type = type;
      this.name = name;
   }

   public Type declType() {
      return this.type.type();
   }

   public Flags flags() {
      return this.flags;
   }

   public Formal flags(Flags flags) {
      Formal_c n = (Formal_c)this.copy();
      n.flags = flags;
      return n;
   }

   public TypeNode type() {
      return this.type;
   }

   public Formal type(TypeNode type) {
      Formal_c n = (Formal_c)this.copy();
      n.type = type;
      return n;
   }

   public String name() {
      return this.name;
   }

   public Formal name(String name) {
      Formal_c n = (Formal_c)this.copy();
      n.name = name;
      return n;
   }

   public LocalInstance localInstance() {
      return this.li;
   }

   public Formal localInstance(LocalInstance li) {
      Formal_c n = (Formal_c)this.copy();
      n.li = li;
      return n;
   }

   protected Formal_c reconstruct(TypeNode type) {
      if (this.type != type) {
         Formal_c n = (Formal_c)this.copy();
         n.type = type;
         return n;
      } else {
         return this;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      TypeNode type = (TypeNode)this.visitChild(this.type, v);
      return this.reconstruct(type);
   }

   public void addDecls(Context c) {
      c.addVariable(this.li);
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write(this.flags.translate());
      this.print(this.type, w, tr);
      w.write(" ");
      w.write(this.name);
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      Formal_c n = (Formal_c)super.buildTypes(tb);
      TypeSystem ts = tb.typeSystem();
      LocalInstance li = ts.localInstance(this.position(), Flags.NONE, ts.unknownType(this.position()), this.name());
      return n.localInstance(li);
   }

   public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
      if (this.declType().isCanonical() && !this.li.type().isCanonical()) {
         TypeSystem ts = ar.typeSystem();
         LocalInstance li = ts.localInstance(this.position(), this.flags(), this.declType(), this.name());
         return this.localInstance(li);
      } else {
         return this;
      }
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();

      try {
         ts.checkLocalFlags(this.flags());
         return this;
      } catch (SemanticException var4) {
         throw new SemanticException(var4.getMessage(), this.position());
      }
   }

   public Term entry() {
      return this;
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      return succs;
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

   public String toString() {
      return this.flags.translate() + this.type + " " + this.name;
   }
}
