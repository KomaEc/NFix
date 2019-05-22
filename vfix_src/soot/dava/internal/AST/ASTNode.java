package soot.dava.internal.AST;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.AbstractUnit;
import soot.UnitPrinter;
import soot.dava.toolkits.base.AST.ASTAnalysis;
import soot.dava.toolkits.base.AST.analysis.Analysis;

public abstract class ASTNode extends AbstractUnit {
   public static final String TAB = "    ";
   public static final String NEWLINE = "\n";
   protected List<Object> subBodies = new ArrayList();

   public abstract void toString(UnitPrinter var1);

   protected void body_toString(UnitPrinter up, List<Object> body) {
      Iterator it = body.iterator();

      while(it.hasNext()) {
         ((ASTNode)it.next()).toString(up);
         if (it.hasNext()) {
            up.newline();
         }
      }

   }

   protected String body_toString(List<Object> body) {
      StringBuffer b = new StringBuffer();
      Iterator it = body.iterator();

      while(it.hasNext()) {
         b.append(((ASTNode)it.next()).toString());
         if (it.hasNext()) {
            b.append("\n");
         }
      }

      return b.toString();
   }

   public List<Object> get_SubBodies() {
      return this.subBodies;
   }

   public abstract void perform_Analysis(ASTAnalysis var1);

   protected void perform_AnalysisOnSubBodies(ASTAnalysis a) {
      Iterator sbit = this.subBodies.iterator();

      while(sbit.hasNext()) {
         Object subBody = sbit.next();
         Iterator it = null;
         if (this instanceof ASTTryNode) {
            it = ((List)((ASTTryNode.container)subBody).o).iterator();
         } else {
            it = ((List)subBody).iterator();
         }

         while(it.hasNext()) {
            ((ASTNode)it.next()).perform_Analysis(a);
         }
      }

      a.analyseASTNode(this);
   }

   public boolean fallsThrough() {
      return false;
   }

   public boolean branches() {
      return false;
   }

   public void apply(Analysis a) {
      throw new RuntimeException("Analysis invoked apply method on ASTNode");
   }
}
