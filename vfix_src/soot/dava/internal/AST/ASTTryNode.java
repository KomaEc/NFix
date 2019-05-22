package soot.dava.internal.AST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Local;
import soot.SootClass;
import soot.UnitPrinter;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.toolkits.base.AST.ASTAnalysis;
import soot.dava.toolkits.base.AST.TryContentsFinder;
import soot.dava.toolkits.base.AST.analysis.Analysis;

public class ASTTryNode extends ASTLabeledNode {
   private List<Object> tryBody;
   private List<Object> catchList;
   private Map<Object, Object> exceptionMap;
   private Map<Object, Object> paramMap;
   private ASTTryNode.container tryBodyContainer;

   public ASTTryNode(SETNodeLabel label, List<Object> tryBody, List<Object> catchList, Map<Object, Object> exceptionMap, Map<Object, Object> paramMap) {
      super(label);
      this.tryBody = tryBody;
      this.tryBodyContainer = new ASTTryNode.container(tryBody);
      this.catchList = new ArrayList();
      Iterator cit = catchList.iterator();

      while(cit.hasNext()) {
         this.catchList.add(new ASTTryNode.container(cit.next()));
      }

      this.exceptionMap = new HashMap();
      cit = this.catchList.iterator();

      ASTTryNode.container c;
      while(cit.hasNext()) {
         c = (ASTTryNode.container)cit.next();
         this.exceptionMap.put(c, exceptionMap.get(c.o));
      }

      this.paramMap = new HashMap();
      cit = this.catchList.iterator();

      while(cit.hasNext()) {
         c = (ASTTryNode.container)cit.next();
         this.paramMap.put(c, paramMap.get(c.o));
      }

      this.subBodies.add(this.tryBodyContainer);
      cit = this.catchList.iterator();

      while(cit.hasNext()) {
         this.subBodies.add(cit.next());
      }

   }

   public void replaceTryBody(List<Object> tryBody) {
      this.tryBody = tryBody;
      this.tryBodyContainer = new ASTTryNode.container(tryBody);
      List<Object> oldSubBodies = this.subBodies;
      this.subBodies = new ArrayList();
      this.subBodies.add(this.tryBodyContainer);
      Iterator<Object> oldIt = oldSubBodies.iterator();
      oldIt.next();

      while(oldIt.hasNext()) {
         this.subBodies.add(oldIt.next());
      }

   }

   protected void perform_AnalysisOnSubBodies(ASTAnalysis a) {
      if (a instanceof TryContentsFinder) {
         Iterator sbit = this.subBodies.iterator();

         while(sbit.hasNext()) {
            ASTTryNode.container subBody = (ASTTryNode.container)sbit.next();
            Iterator it = ((List)subBody.o).iterator();

            while(it.hasNext()) {
               ASTNode n = (ASTNode)it.next();
               n.perform_Analysis(a);
               TryContentsFinder.v().add_ExceptionSet(subBody, TryContentsFinder.v().get_ExceptionSet(n));
            }
         }

         a.analyseASTNode(this);
      } else {
         super.perform_AnalysisOnSubBodies(a);
      }

   }

   public boolean isEmpty() {
      return this.tryBody.isEmpty();
   }

   public List<Object> get_TryBody() {
      return this.tryBody;
   }

   public ASTTryNode.container get_TryBodyContainer() {
      return this.tryBodyContainer;
   }

   public List<Object> get_CatchList() {
      return this.catchList;
   }

   public Map<Object, Object> get_ExceptionMap() {
      return this.exceptionMap;
   }

   public Map<Object, Object> get_ParamMap() {
      return this.paramMap;
   }

   public Set<Object> get_ExceptionSet() {
      HashSet<Object> s = new HashSet();
      Iterator it = this.catchList.iterator();

      while(it.hasNext()) {
         s.add(this.exceptionMap.get(it.next()));
      }

      return s;
   }

   public Object clone() {
      ArrayList<Object> newCatchList = new ArrayList();
      Iterator it = this.catchList.iterator();

      while(it.hasNext()) {
         newCatchList.add(((ASTTryNode.container)it.next()).o);
      }

      return new ASTTryNode(this.get_Label(), this.tryBody, newCatchList, this.exceptionMap, this.paramMap);
   }

   public void toString(UnitPrinter up) {
      this.label_toString(up);
      up.literal("try");
      up.newline();
      up.literal("{");
      up.newline();
      up.incIndent();
      this.body_toString(up, this.tryBody);
      up.decIndent();
      up.literal("}");
      up.newline();
      Iterator cit = this.catchList.iterator();

      while(cit.hasNext()) {
         ASTTryNode.container catchBody = (ASTTryNode.container)cit.next();
         up.literal("catch");
         up.literal(" ");
         up.literal("(");
         up.type(((SootClass)this.exceptionMap.get(catchBody)).getType());
         up.literal(" ");
         up.local((Local)this.paramMap.get(catchBody));
         up.literal(")");
         up.newline();
         up.literal("{");
         up.newline();
         up.incIndent();
         this.body_toString(up, (List)catchBody.o);
         up.decIndent();
         up.literal("}");
         up.newline();
      }

   }

   public String toString() {
      StringBuffer b = new StringBuffer();
      b.append(this.label_toString());
      b.append("try");
      b.append("\n");
      b.append("{");
      b.append("\n");
      b.append(this.body_toString(this.tryBody));
      b.append("}");
      b.append("\n");
      Iterator cit = this.catchList.iterator();

      while(cit.hasNext()) {
         ASTTryNode.container catchBody = (ASTTryNode.container)cit.next();
         b.append("catch (");
         b.append(((SootClass)this.exceptionMap.get(catchBody)).getName());
         b.append(" ");
         b.append(((Local)this.paramMap.get(catchBody)).getName());
         b.append(")");
         b.append("\n");
         b.append("{");
         b.append("\n");
         b.append(this.body_toString((List)catchBody.o));
         b.append("}");
         b.append("\n");
      }

      return b.toString();
   }

   public void apply(Analysis a) {
      a.caseASTTryNode(this);
   }

   public class container {
      public Object o;

      public container(Object o) {
         this.o = o;
      }

      public void replaceBody(Object newBody) {
         this.o = newBody;
      }
   }
}
