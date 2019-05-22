package soot.jimple.spark.solver;

import java.util.Iterator;
import soot.Context;
import soot.Local;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.Type;
import soot.jimple.IntConstant;
import soot.jimple.NewArrayExpr;
import soot.jimple.spark.pag.AllocDotField;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ArrayElement;
import soot.jimple.spark.pag.MethodPAG;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.StringConstantNode;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.CallGraphBuilder;
import soot.jimple.toolkits.callgraph.ContextManager;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.OnFlyCallGraphBuilder;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.util.queue.QueueReader;

public class OnFlyCallGraph {
   protected final OnFlyCallGraphBuilder ofcgb;
   protected final ReachableMethods reachableMethods;
   protected final QueueReader<MethodOrMethodContext> reachablesReader;
   protected final QueueReader<Edge> callEdges;
   protected final CallGraph callGraph;
   private PAG pag;

   public ReachableMethods reachableMethods() {
      return this.reachableMethods;
   }

   public CallGraph callGraph() {
      return this.callGraph;
   }

   public OnFlyCallGraph(PAG pag, boolean appOnly) {
      this.pag = pag;
      this.callGraph = Scene.v().internalMakeCallGraph();
      Scene.v().setCallGraph(this.callGraph);
      ContextManager cm = CallGraphBuilder.makeContextManager(this.callGraph);
      this.reachableMethods = Scene.v().getReachableMethods();
      this.ofcgb = new OnFlyCallGraphBuilder(cm, this.reachableMethods, appOnly);
      this.reachablesReader = this.reachableMethods.listener();
      this.callEdges = cm.callGraph().listener();
   }

   public void build() {
      this.ofcgb.processReachables();
      this.processReachables();
      this.processCallEdges();
   }

   private void processReachables() {
      this.reachableMethods.update();

      while(this.reachablesReader.hasNext()) {
         MethodOrMethodContext m = (MethodOrMethodContext)this.reachablesReader.next();
         MethodPAG mpag = MethodPAG.v(this.pag, m.method());
         mpag.build();
         mpag.addToPAG(m.context());
      }

   }

   private void processCallEdges() {
      while(this.callEdges.hasNext()) {
         Edge e = (Edge)this.callEdges.next();
         MethodPAG amp = MethodPAG.v(this.pag, e.tgt());
         amp.build();
         amp.addToPAG(e.tgtCtxt());
         this.pag.addCallTarget(e);
      }

   }

   public OnFlyCallGraphBuilder ofcgb() {
      return this.ofcgb;
   }

   public void updatedFieldRef(final AllocDotField df, PointsToSetInternal ptsi) {
      if (df.getField() == ArrayElement.v()) {
         if (this.ofcgb.wantArrayField(df)) {
            ptsi.forall(new P2SetVisitor() {
               public void visit(Node n) {
                  OnFlyCallGraph.this.ofcgb.addInvokeArgType((AllocDotField)df, (Context)null, n.getType());
               }
            });
         }

      }
   }

   public void updatedNode(VarNode vn) {
      Object r = vn.getVariable();
      if (r instanceof Local) {
         final Local receiver = (Local)r;
         final Context context = vn.context();
         PointsToSetInternal p2set = vn.getP2Set().getNewSet();
         if (this.ofcgb.wantTypes(receiver)) {
            p2set.forall(new P2SetVisitor() {
               public final void visit(Node n) {
                  if (n instanceof AllocNode) {
                     OnFlyCallGraph.this.ofcgb.addType(receiver, context, n.getType(), (AllocNode)n);
                  }

               }
            });
         }

         if (this.ofcgb.wantStringConstants(receiver)) {
            p2set.forall(new P2SetVisitor() {
               public final void visit(Node n) {
                  if (n instanceof StringConstantNode) {
                     String constant = ((StringConstantNode)n).getString();
                     OnFlyCallGraph.this.ofcgb.addStringConstant(receiver, context, constant);
                  } else {
                     OnFlyCallGraph.this.ofcgb.addStringConstant(receiver, context, (String)null);
                  }

               }
            });
         }

         if (this.ofcgb.wantInvokeArg(receiver)) {
            p2set.forall(new P2SetVisitor() {
               public void visit(Node n) {
                  if (n instanceof AllocNode) {
                     AllocNode an = (AllocNode)n;
                     OnFlyCallGraph.this.ofcgb.addInvokeArgDotField(receiver, an.dot(ArrayElement.v()));

                     assert an.getNewExpr() instanceof NewArrayExpr;

                     NewArrayExpr nae = (NewArrayExpr)an.getNewExpr();
                     if (!(nae.getSize() instanceof IntConstant)) {
                        OnFlyCallGraph.this.ofcgb.setArgArrayNonDetSize(receiver, context);
                     } else {
                        IntConstant sizeConstant = (IntConstant)nae.getSize();
                        OnFlyCallGraph.this.ofcgb.addPossibleArgArraySize(receiver, sizeConstant.value, context);
                     }
                  }

               }
            });
            Iterator var6 = this.pag.reachingObjectsOfArrayElement(p2set).possibleTypes().iterator();

            while(var6.hasNext()) {
               Type ty = (Type)var6.next();
               this.ofcgb.addInvokeArgType(receiver, context, ty);
            }
         }

      }
   }

   public void mergedWith(Node n1, Node n2) {
   }
}
