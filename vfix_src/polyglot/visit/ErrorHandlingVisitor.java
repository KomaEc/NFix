package polyglot.visit;

import polyglot.ast.ClassDecl;
import polyglot.ast.ClassMember;
import polyglot.ast.Import;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.SourceFile;
import polyglot.ast.Stmt;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.ErrorQueue;
import polyglot.util.Position;

public class ErrorHandlingVisitor extends HaltingVisitor {
   protected boolean error;
   protected Job job;
   protected TypeSystem ts;
   protected NodeFactory nf;

   public ErrorHandlingVisitor(Job job, TypeSystem ts, NodeFactory nf) {
      this.job = job;
      this.ts = ts;
      this.nf = nf;
   }

   public Job job() {
      return this.job;
   }

   public NodeVisitor begin() {
      this.error = false;
      return super.begin();
   }

   public ErrorQueue errorQueue() {
      return this.job.compiler().errorQueue();
   }

   public NodeFactory nodeFactory() {
      return this.nf;
   }

   public TypeSystem typeSystem() {
      return this.ts;
   }

   protected NodeVisitor enterCall(Node parent, Node n) throws SemanticException {
      if (Report.should_report((String)"visit", 3)) {
         Report.report(3, "enter: " + parent + " -> " + n);
      }

      return this.enterCall(n);
   }

   protected NodeVisitor enterCall(Node n) throws SemanticException {
      return this;
   }

   protected NodeVisitor enterError(Node n) {
      return this;
   }

   protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
      return this.leaveCall(n);
   }

   protected Node leaveCall(Node n) throws SemanticException {
      return n;
   }

   protected boolean catchErrors(Node n) {
      return n instanceof Stmt || n instanceof ClassMember || n instanceof ClassDecl || n instanceof Import || n instanceof SourceFile;
   }

   public NodeVisitor enter(Node parent, Node n) {
      if (Report.should_report((String)"visit", 5)) {
         Report.report(5, "enter(" + n + ")");
      }

      if (this.catchErrors(n)) {
         this.error = false;
      }

      try {
         return (ErrorHandlingVisitor)this.enterCall(parent, n);
      } catch (SemanticException var5) {
         if (var5.getMessage() != null) {
            Position position = var5.position();
            if (position == null) {
               position = n.position();
            }

            this.errorQueue().enqueue(5, var5.getMessage(), position);
         }

         if (!this.catchErrors(n)) {
            this.error = true;
         }

         return this.enterError(n);
      }
   }

   public Node leave(Node parent, Node old, Node n, NodeVisitor v) {
      try {
         if (v instanceof ErrorHandlingVisitor && ((ErrorHandlingVisitor)v).error) {
            if (Report.should_report((String)"visit", 5)) {
               Report.report(5, "leave(" + n + "): error below");
            }

            if (this.catchErrors(n)) {
               this.error = false;
               ((ErrorHandlingVisitor)v).error = false;
            } else {
               this.error = true;
            }

            return n;
         } else {
            if (Report.should_report((String)"visit", 5)) {
               Report.report(5, "leave(" + n + "): calling leaveCall");
            }

            return this.leaveCall(old, n, v);
         }
      } catch (SemanticException var7) {
         if (var7.getMessage() != null) {
            Position position = var7.position();
            if (position == null) {
               position = n.position();
            }

            this.errorQueue().enqueue(5, var7.getMessage(), position);
         }

         if (this.catchErrors(n)) {
            this.error = false;
            ((ErrorHandlingVisitor)v).error = false;
         } else {
            this.error = true;
            ((ErrorHandlingVisitor)v).error = true;
         }

         return n;
      }
   }
}
