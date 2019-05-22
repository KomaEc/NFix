package soot.dava.internal.SET;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import soot.Value;
import soot.dava.DavaBody;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.MonitorStmt;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.util.IterableSet;

public class SETStatementSequenceNode extends SETNode {
   private DavaBody davaBody;
   private boolean hasContinue;

   public SETStatementSequenceNode(IterableSet body, DavaBody davaBody) {
      super(body);
      this.add_SubBody(body);
      this.davaBody = davaBody;
      this.hasContinue = false;
   }

   public SETStatementSequenceNode(IterableSet body) {
      this(body, (DavaBody)null);
   }

   public boolean has_Continue() {
      return this.hasContinue;
   }

   public IterableSet get_NaturalExits() {
      IterableSet c = new IterableSet();
      AugmentedStmt last = (AugmentedStmt)this.get_Body().getLast();
      if (last.csuccs != null && !last.csuccs.isEmpty()) {
         c.add(last);
      }

      return c;
   }

   public ASTNode emit_AST() {
      List<AugmentedStmt> l = new LinkedList();
      boolean isStaticInitializer = this.davaBody.getMethod().getName().equals("<clinit>");
      Iterator it = this.get_Body().iterator();

      while(true) {
         AugmentedStmt as;
         while(true) {
            if (!it.hasNext()) {
               if (l.isEmpty()) {
                  return null;
               }

               return new ASTStatementSequenceNode(l);
            }

            as = (AugmentedStmt)it.next();
            Stmt s = as.get_Stmt();
            if (this.davaBody == null) {
               break;
            }

            if ((!(s instanceof ReturnVoidStmt) || !isStaticInitializer) && !(s instanceof GotoStmt) && !(s instanceof MonitorStmt)) {
               if (s == this.davaBody.get_ConstructorUnit()) {
               }

               if (!(s instanceof IdentityStmt)) {
                  break;
               }

               IdentityStmt ids = (IdentityStmt)s;
               Value rightOp = ids.getRightOp();
               Value leftOp = ids.getLeftOp();
               if (!this.davaBody.get_ThisLocals().contains(leftOp) && !(rightOp instanceof ParameterRef) && !(rightOp instanceof CaughtExceptionRef)) {
                  break;
               }
            }
         }

         l.add(as);
      }
   }

   public AugmentedStmt get_EntryStmt() {
      return (AugmentedStmt)this.get_Body().getFirst();
   }

   public void insert_AbruptStmt(DAbruptStmt stmt) {
      if (!this.hasContinue) {
         this.get_Body().addLast(new AugmentedStmt(stmt));
         this.hasContinue = stmt.is_Continue();
      }
   }

   protected boolean resolve(SETNode parent) {
      throw new RuntimeException("Attempting auto-nest a SETStatementSequenceNode.");
   }
}
