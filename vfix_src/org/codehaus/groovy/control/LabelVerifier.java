package org.codehaus.groovy.control;

import java.util.Iterator;
import java.util.LinkedList;
import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.stmt.BreakStatement;
import org.codehaus.groovy.ast.stmt.ContinueStatement;
import org.codehaus.groovy.ast.stmt.DoWhileStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.SwitchStatement;
import org.codehaus.groovy.ast.stmt.WhileStatement;

public class LabelVerifier extends ClassCodeVisitorSupport {
   private SourceUnit source;
   private LinkedList visitedLabels;
   private LinkedList continueLabels;
   private LinkedList breakLabels;
   boolean inLoop = false;
   boolean inSwitch = false;

   public LabelVerifier(SourceUnit src) {
      this.source = src;
   }

   protected SourceUnit getSourceUnit() {
      return this.source;
   }

   private void init() {
      this.visitedLabels = new LinkedList();
      this.continueLabels = new LinkedList();
      this.breakLabels = new LinkedList();
      this.inLoop = false;
      this.inSwitch = false;
   }

   protected void visitClassCodeContainer(Statement code) {
      this.init();
      super.visitClassCodeContainer(code);
      this.assertNoLabelsMissed();
   }

   public void visitStatement(Statement statement) {
      String label = statement.getStatementLabel();
      if (label != null) {
         Iterator iter = this.breakLabels.iterator();

         while(iter.hasNext()) {
            BreakStatement element = (BreakStatement)iter.next();
            if (element.getLabel().equals(label)) {
               iter.remove();
            }
         }

         iter = this.continueLabels.iterator();

         while(iter.hasNext()) {
            ContinueStatement element = (ContinueStatement)iter.next();
            if (element.getLabel().equals(label)) {
               iter.remove();
            }
         }

         this.visitedLabels.add(label);
      }

      super.visitStatement(statement);
   }

   public void visitForLoop(ForStatement forLoop) {
      boolean oldInLoop = this.inLoop;
      this.inLoop = true;
      super.visitForLoop(forLoop);
      this.inLoop = oldInLoop;
   }

   public void visitDoWhileLoop(DoWhileStatement loop) {
      boolean oldInLoop = this.inLoop;
      this.inLoop = true;
      super.visitDoWhileLoop(loop);
      this.inLoop = oldInLoop;
   }

   public void visitWhileLoop(WhileStatement loop) {
      boolean oldInLoop = this.inLoop;
      this.inLoop = true;
      super.visitWhileLoop(loop);
      this.inLoop = oldInLoop;
   }

   public void visitBreakStatement(BreakStatement statement) {
      String label = statement.getLabel();
      boolean hasNamedLabel = label != null;
      if (!hasNamedLabel && !this.inLoop && !this.inSwitch) {
         this.addError("the break statement is only allowed inside loops or switches", statement);
      } else if (hasNamedLabel && !this.inLoop) {
         this.addError("the break statement with named label is only allowed inside loops", statement);
      }

      if (label != null) {
         boolean found = false;
         Iterator iter = this.visitedLabels.iterator();

         while(iter.hasNext()) {
            String element = (String)iter.next();
            if (element.equals(label)) {
               found = true;
               break;
            }
         }

         if (!found) {
            this.breakLabels.add(statement);
         }
      }

      super.visitBreakStatement(statement);
   }

   public void visitContinueStatement(ContinueStatement statement) {
      String label = statement.getLabel();
      boolean hasNamedLabel = label != null;
      if (!hasNamedLabel && !this.inLoop) {
         this.addError("the continue statement is only allowed inside loops", statement);
      }

      if (label != null) {
         boolean found = false;
         Iterator iter = this.visitedLabels.iterator();

         while(iter.hasNext()) {
            String element = (String)iter.next();
            if (element.equals(label)) {
               found = true;
               break;
            }
         }

         if (!found) {
            this.continueLabels.add(statement);
         }
      }

      super.visitContinueStatement(statement);
   }

   protected void assertNoLabelsMissed() {
      Iterator iter = this.continueLabels.iterator();

      while(iter.hasNext()) {
         ContinueStatement element = (ContinueStatement)iter.next();
         this.addError("continue to missing label", element);
      }

      iter = this.breakLabels.iterator();

      while(iter.hasNext()) {
         BreakStatement element = (BreakStatement)iter.next();
         this.addError("break to missing label", element);
      }

   }

   public void visitSwitch(SwitchStatement statement) {
      boolean oldInSwitch = this.inSwitch;
      this.inSwitch = true;
      super.visitSwitch(statement);
      this.inSwitch = oldInSwitch;
   }
}
