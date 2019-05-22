package soot.jimple.toolkits.callgraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Context;
import soot.EntryPoints;
import soot.Local;
import soot.MethodOrMethodContext;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.Scene;
import soot.Type;
import soot.Value;
import soot.jimple.IntConstant;
import soot.jimple.NewArrayExpr;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ArrayElement;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.pointer.DumbPointerAnalysis;
import soot.util.queue.QueueReader;

public final class CallGraphBuilder {
   private static final Logger logger = LoggerFactory.getLogger(CallGraphBuilder.class);
   private PointsToAnalysis pa;
   private final ReachableMethods reachables;
   private final OnFlyCallGraphBuilder ofcgb;
   private final CallGraph cg;

   public CallGraph getCallGraph() {
      return this.cg;
   }

   public ReachableMethods reachables() {
      return this.reachables;
   }

   public static ContextManager makeContextManager(CallGraph cg) {
      return new ContextInsensitiveContextManager(cg);
   }

   public CallGraphBuilder(PointsToAnalysis pa) {
      this.pa = pa;
      this.cg = Scene.v().internalMakeCallGraph();
      Scene.v().setCallGraph(this.cg);
      this.reachables = Scene.v().getReachableMethods();
      ContextManager cm = makeContextManager(this.cg);
      this.ofcgb = new OnFlyCallGraphBuilder(cm, this.reachables);
   }

   public CallGraphBuilder() {
      logger.warn("using incomplete callgraph containing only application classes.");
      this.pa = DumbPointerAnalysis.v();
      this.cg = Scene.v().internalMakeCallGraph();
      Scene.v().setCallGraph(this.cg);
      List<MethodOrMethodContext> entryPoints = new ArrayList();
      entryPoints.addAll(EntryPoints.v().methodsOfApplicationClasses());
      entryPoints.addAll(EntryPoints.v().implicit());
      this.reachables = new ReachableMethods(this.cg, entryPoints);
      ContextManager cm = new ContextInsensitiveContextManager(this.cg);
      this.ofcgb = new OnFlyCallGraphBuilder(cm, this.reachables, true);
   }

   public void build() {
      QueueReader worklist = this.reachables.listener();

      label102:
      while(true) {
         final MethodOrMethodContext momc;
         Iterator stringConstantIt;
         List stringConstants;
         do {
            this.ofcgb.processReachables();
            this.reachables.update();
            if (!worklist.hasNext()) {
               return;
            }

            momc = (MethodOrMethodContext)worklist.next();
            List<Local> receivers = (List)this.ofcgb.methodToReceivers().get(momc.method());
            if (receivers != null) {
               Iterator receiverIt = receivers.iterator();

               while(receiverIt.hasNext()) {
                  Local receiver = (Local)receiverIt.next();
                  PointsToSet p2set = this.pa.reachingObjects(receiver);
                  stringConstantIt = p2set.possibleTypes().iterator();

                  while(stringConstantIt.hasNext()) {
                     Type type = (Type)stringConstantIt.next();
                     this.ofcgb.addType(receiver, momc.context(), type, (Context)null);
                  }
               }
            }

            List<Local> bases = (List)this.ofcgb.methodToInvokeArgs().get(momc.method());
            if (bases != null) {
               Iterator var14 = bases.iterator();

               while(var14.hasNext()) {
                  Local base = (Local)var14.next();
                  PointsToSet pts = this.pa.reachingObjects(base);
                  Iterator var21 = pts.possibleTypes().iterator();

                  while(var21.hasNext()) {
                     Type ty = (Type)var21.next();
                     this.ofcgb.addBaseType(base, momc.context(), ty);
                  }
               }
            }

            List<Local> argArrays = (List)this.ofcgb.methodToInvokeBases().get(momc.method());
            if (argArrays != null) {
               Iterator var17 = argArrays.iterator();

               while(var17.hasNext()) {
                  final Local argArray = (Local)var17.next();
                  PointsToSet pts = this.pa.reachingObjects(argArray);
                  if (pts instanceof PointsToSetInternal) {
                     PointsToSetInternal ptsi = (PointsToSetInternal)pts;
                     ptsi.forall(new P2SetVisitor() {
                        public void visit(Node n) {
                           assert n instanceof AllocNode;

                           AllocNode an = (AllocNode)n;
                           Object newExpr = an.getNewExpr();
                           CallGraphBuilder.this.ofcgb.addInvokeArgDotField(argArray, an.dot(ArrayElement.v()));
                           if (newExpr instanceof NewArrayExpr) {
                              NewArrayExpr nae = (NewArrayExpr)newExpr;
                              Value size = nae.getSize();
                              if (size instanceof IntConstant) {
                                 IntConstant arrSize = (IntConstant)size;
                                 CallGraphBuilder.this.ofcgb.addPossibleArgArraySize(argArray, arrSize.value, momc.context());
                              } else {
                                 CallGraphBuilder.this.ofcgb.setArgArrayNonDetSize(argArray, momc.context());
                              }
                           }

                        }
                     });
                  }

                  Iterator var25 = this.pa.reachingObjectsOfArrayElement(pts).possibleTypes().iterator();

                  while(var25.hasNext()) {
                     Type t = (Type)var25.next();
                     this.ofcgb.addInvokeArgType(argArray, momc.context(), t);
                  }
               }
            }

            stringConstants = (List)this.ofcgb.methodToStringConstants().get(momc.method());
         } while(stringConstants == null);

         stringConstantIt = stringConstants.iterator();

         while(true) {
            while(true) {
               if (!stringConstantIt.hasNext()) {
                  continue label102;
               }

               Local stringConstant = (Local)stringConstantIt.next();
               PointsToSet p2set = this.pa.reachingObjects(stringConstant);
               Collection<String> possibleStringConstants = p2set.possibleStringConstants();
               if (possibleStringConstants == null) {
                  this.ofcgb.addStringConstant(stringConstant, momc.context(), (String)null);
               } else {
                  Iterator constantIt = possibleStringConstants.iterator();

                  while(constantIt.hasNext()) {
                     String constant = (String)constantIt.next();
                     this.ofcgb.addStringConstant(stringConstant, momc.context(), constant);
                  }
               }
            }
         }
      }
   }
}
