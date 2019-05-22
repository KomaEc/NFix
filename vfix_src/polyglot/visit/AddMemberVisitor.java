package polyglot.visit;

import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;

public class AddMemberVisitor extends ContextVisitor {
   public AddMemberVisitor(Job job, TypeSystem ts, NodeFactory nf) {
      super(job, ts, nf);
   }

   protected NodeVisitor enterCall(Node n) throws SemanticException {
      if (Report.should_report((String)"visit", 4)) {
         Report.report(4, ">> AddMemberVisitor::enter " + n);
      }

      return n.del().addMembersEnter(this);
   }

   protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
      if (Report.should_report((String)"visit", 4)) {
         Report.report(4, "<< AddMemberVisitor::leave " + n);
      }

      return n.del().addMembers((AddMemberVisitor)v);
   }
}
