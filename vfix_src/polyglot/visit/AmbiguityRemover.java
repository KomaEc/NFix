package polyglot.visit;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.ClassType;
import polyglot.types.ParsedClassType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.Enum;

public class AmbiguityRemover extends ContextVisitor {
   public static final AmbiguityRemover.Kind SUPER = new AmbiguityRemover.Kind("disam-super");
   public static final AmbiguityRemover.Kind SIGNATURES = new AmbiguityRemover.Kind("disam-sigs");
   public static final AmbiguityRemover.Kind FIELDS = new AmbiguityRemover.Kind("disam-fields");
   public static final AmbiguityRemover.Kind ALL = new AmbiguityRemover.Kind("disam-all");
   protected AmbiguityRemover.Kind kind;

   public AmbiguityRemover(Job job, TypeSystem ts, NodeFactory nf, AmbiguityRemover.Kind kind) {
      super(job, ts, nf);
      this.kind = kind;
   }

   public AmbiguityRemover.Kind kind() {
      return this.kind;
   }

   protected NodeVisitor enterCall(Node n) throws SemanticException {
      if (Report.should_report((String)"visit", 2)) {
         Report.report(2, ">> " + this.kind + "::enter " + n);
      }

      NodeVisitor v = n.del().disambiguateEnter(this);
      if (Report.should_report((String)"visit", 2)) {
         Report.report(2, "<< " + this.kind + "::enter " + n + " -> " + v);
      }

      return v;
   }

   protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
      if (Report.should_report((String)"visit", 2)) {
         Report.report(2, ">> " + this.kind + "::leave " + n);
      }

      Node m = n.del().disambiguate((AmbiguityRemover)v);
      if (Report.should_report((String)"visit", 2)) {
         Report.report(2, "<< " + this.kind + "::leave " + n + " -> " + m);
      }

      return m;
   }

   public void addSuperDependencies(ClassType ct) {
      Set seen = new HashSet();
      Stack s = new Stack();
      s.push(ct);

      while(true) {
         ClassType classt;
         do {
            Type t;
            do {
               if (s.isEmpty()) {
                  return;
               }

               t = (Type)s.pop();
            } while(!t.isClass());

            classt = t.toClass();
         } while(seen.contains(classt));

         seen.add(classt);
         if (classt instanceof ParsedClassType) {
            this.job().extensionInfo().addDependencyToCurrentJob(((ParsedClassType)classt).fromSource());
         }

         Iterator i = classt.interfaces().iterator();

         while(i.hasNext()) {
            s.push(i.next());
         }

         if (classt.superType() != null) {
            s.push(classt.superType());
         }
      }
   }

   public String toString() {
      return super.toString() + "(" + this.kind + ")";
   }

   public static class Kind extends Enum {
      protected Kind(String name) {
         super(name);
      }
   }
}
