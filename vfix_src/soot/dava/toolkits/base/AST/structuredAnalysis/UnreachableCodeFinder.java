package soot.dava.toolkits.base.AST.structuredAnalysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Local;
import soot.Value;
import soot.dava.DecompilationException;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.AST.ASTUnaryBinaryCondition;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.toolkits.scalar.FlowSet;

public class UnreachableCodeFinder extends StructuredAnalysis {
   public static boolean DEBUG = false;

   public UnreachableCodeFinder(Object analyze) {
      this.process(analyze, this.newInitialFlow());
   }

   public void setMergeType() {
      this.MERGETYPE = 2;
   }

   public DavaFlowSet newInitialFlow() {
      DavaFlowSet newSet = this.emptyFlowSet();
      newSet.add(new Boolean(true));
      return newSet;
   }

   public DavaFlowSet emptyFlowSet() {
      return new UnreachableCodeFinder.UnreachableCodeFlowSet();
   }

   public UnreachableCodeFinder.UnreachableCodeFlowSet cloneFlowSet(DavaFlowSet flowSet) {
      if (flowSet instanceof UnreachableCodeFinder.UnreachableCodeFlowSet) {
         return ((UnreachableCodeFinder.UnreachableCodeFlowSet)flowSet).clone();
      } else {
         throw new RuntimeException("Clone only implemented for UnreachableCodeFlowSet");
      }
   }

   public DavaFlowSet processUnaryBinaryCondition(ASTUnaryBinaryCondition cond, DavaFlowSet input) {
      return input;
   }

   public DavaFlowSet processSynchronizedLocal(Local local, DavaFlowSet input) {
      return input;
   }

   public DavaFlowSet processSwitchKey(Value key, DavaFlowSet input) {
      return input;
   }

   public DavaFlowSet processStatement(Stmt s, DavaFlowSet input) {
      return input;
   }

   public DavaFlowSet processAbruptStatements(Stmt s, DavaFlowSet input) {
      if (DEBUG) {
         System.out.println("processing stmt " + s);
      }

      if (!(s instanceof ReturnStmt) && !(s instanceof RetStmt) && !(s instanceof ReturnVoidStmt)) {
         if (s instanceof DAbruptStmt) {
            DAbruptStmt abStmt = (DAbruptStmt)s;
            if (!abStmt.is_Continue() && !abStmt.is_Break()) {
               throw new RuntimeException("Found a DAbruptStmt which is neither break nor continue!!");
            } else {
               DavaFlowSet temp = new UnreachableCodeFinder.UnreachableCodeFlowSet();
               SETNodeLabel nodeLabel = abStmt.getLabel();
               if (abStmt.is_Break()) {
                  if (nodeLabel != null && nodeLabel.toString() != null) {
                     temp.addToBreakList(nodeLabel.toString(), input);
                  } else {
                     temp.addToImplicitBreaks(abStmt, input);
                  }
               }

               temp.add(new Boolean(false));
               temp.copyInternalDataFrom(input);
               if (DEBUG) {
                  System.out.println("\tstmt is an abrupt stmt. Hence sending forward false");
               }

               return temp;
            }
         } else {
            if (DEBUG) {
               System.out.println("\tstmt is not an abrupt stmt.");
            }

            return this.processStatement(s, input);
         }
      } else {
         UnreachableCodeFinder.UnreachableCodeFlowSet toReturn = new UnreachableCodeFinder.UnreachableCodeFlowSet();
         toReturn.add(new Boolean(false));
         toReturn.copyInternalDataFrom(input);
         if (DEBUG) {
            System.out.println("\tstmt is a return stmt. Hence sending forward false");
         }

         return toReturn;
      }
   }

   public DavaFlowSet handleBreak(String label, DavaFlowSet output, ASTNode node) {
      if (DEBUG) {
         System.out.println("Handling break. Output contains" + ((UnreachableCodeFinder.UnreachableCodeFlowSet)output).size());
      }

      if (!(output instanceof UnreachableCodeFinder.UnreachableCodeFlowSet)) {
         throw new RuntimeException("handleBreak is only implemented for UnreachableCodeFlowSet type");
      } else {
         List explicitSet = output.getBreakSet(label);
         if (node == null) {
            throw new RuntimeException("ASTNode sent to handleBreak was null");
         } else {
            List implicitSet = output.getImplicitlyBrokenSets(node);
            DavaFlowSet toReturn = this.emptyFlowSet();
            toReturn.copyInternalDataFrom(output);
            if ((explicitSet == null || explicitSet.size() <= 0) && (implicitSet == null || implicitSet.size() <= 0)) {
               toReturn.add(new Boolean(false));
               if (DEBUG) {
                  System.out.println("\tBreak sets (implicit and explicit are empty hence forwarding merge of false with inset");
               }

               return this.merge(toReturn, output);
            } else {
               if (DEBUG) {
                  System.out.println("\tBreak sets (implicit and explicit are nonempty hence forwarding true");
               }

               toReturn.add(new Boolean(true));
               return toReturn;
            }
         }
      }
   }

   public boolean isReachable(Object input) {
      if (!(input instanceof UnreachableCodeFinder.UnreachableCodeFlowSet)) {
         throw new DecompilationException("Implemented only for UnreachableCodeFlowSet");
      } else {
         UnreachableCodeFinder.UnreachableCodeFlowSet checking = (UnreachableCodeFinder.UnreachableCodeFlowSet)input;
         if (checking.size() != 1) {
            throw new DecompilationException("unreachableCodeFlow set size should always be 1");
         } else if ((Boolean)checking.elements[0]) {
            if (DEBUG) {
               System.out.println("\t Reachable");
            }

            return true;
         } else {
            if (DEBUG) {
               System.out.println("\t NOT Reachable");
            }

            return false;
         }
      }
   }

   public DavaFlowSet processASTStatementSequenceNode(ASTStatementSequenceNode node, DavaFlowSet input) {
      if (DEBUG) {
         System.out.println("Processing statement sequence node");
      }

      return !this.isReachable(input) ? input : super.processASTStatementSequenceNode(node, input);
   }

   public DavaFlowSet processASTLabeledBlockNode(ASTLabeledBlockNode node, DavaFlowSet input) {
      if (DEBUG) {
         System.out.println("Processing labeled block node node");
      }

      return !this.isReachable(input) ? input : super.processASTLabeledBlockNode(node, input);
   }

   public DavaFlowSet processASTSynchronizedBlockNode(ASTSynchronizedBlockNode node, DavaFlowSet input) {
      if (DEBUG) {
         System.out.println("Processing synchronized block node");
      }

      return !this.isReachable(input) ? input : super.processASTSynchronizedBlockNode(node, input);
   }

   public DavaFlowSet processASTIfElseNode(ASTIfElseNode node, DavaFlowSet input) {
      if (DEBUG) {
         System.out.println("Processing ifelse node");
      }

      return !this.isReachable(input) ? input : super.processASTIfElseNode(node, input);
   }

   public DavaFlowSet ifNotReachableReturnInputElseProcessBodyAndReturnTrue(ASTNode node, DavaFlowSet input) {
      if (DEBUG) {
         System.out.println("Processing " + node.getClass() + " node");
      }

      if (!this.isReachable(input)) {
         return input;
      } else {
         DavaFlowSet output = this.processSingleSubBodyNode(node, input);
         UnreachableCodeFinder.UnreachableCodeFlowSet toReturn = new UnreachableCodeFinder.UnreachableCodeFlowSet();
         toReturn.add(new Boolean(true));
         toReturn.copyInternalDataFrom(output);
         return toReturn;
      }
   }

   public DavaFlowSet processASTIfNode(ASTIfNode node, DavaFlowSet input) {
      return this.ifNotReachableReturnInputElseProcessBodyAndReturnTrue(node, input);
   }

   public DavaFlowSet processASTWhileNode(ASTWhileNode node, DavaFlowSet input) {
      return this.ifNotReachableReturnInputElseProcessBodyAndReturnTrue(node, input);
   }

   public DavaFlowSet processASTDoWhileNode(ASTDoWhileNode node, DavaFlowSet input) {
      return this.ifNotReachableReturnInputElseProcessBodyAndReturnTrue(node, input);
   }

   public DavaFlowSet processASTUnconditionalLoopNode(ASTUnconditionalLoopNode node, DavaFlowSet input) {
      return this.ifNotReachableReturnInputElseProcessBodyAndReturnTrue(node, input);
   }

   public DavaFlowSet processASTForLoopNode(ASTForLoopNode node, DavaFlowSet input) {
      return this.ifNotReachableReturnInputElseProcessBodyAndReturnTrue(node, input);
   }

   public DavaFlowSet processASTSwitchNode(ASTSwitchNode node, DavaFlowSet input) {
      if (DEBUG) {
         System.out.println("Processing switch node");
      }

      if (!this.isReachable(input)) {
         return input;
      } else {
         List<Object> indexList = node.getIndexList();
         Map<Object, List<Object>> index2BodyList = node.getIndex2BodyList();
         DavaFlowSet initialIn = this.cloneFlowSet(input);
         DavaFlowSet out = null;
         DavaFlowSet defaultOut = null;
         List<DavaFlowSet> toMergeBreaks = new ArrayList();
         Iterator it = indexList.iterator();

         Object output;
         while(it.hasNext()) {
            output = it.next();
            List body = (List)index2BodyList.get(output);
            if (body != null) {
               out = this.process(body, this.cloneFlowSet(initialIn));
               toMergeBreaks.add(this.cloneFlowSet(out));
               if (output instanceof String) {
                  defaultOut = out;
               }
            }
         }

         output = initialIn;
         if (out != null) {
            if (defaultOut != null) {
               output = this.merge(defaultOut, out);
            } else {
               output = this.merge(initialIn, out);
            }
         }

         String label = this.getLabel(node);
         List<DavaFlowSet> outList = new ArrayList();
         Iterator var13 = toMergeBreaks.iterator();

         while(var13.hasNext()) {
            DavaFlowSet tmb = (DavaFlowSet)var13.next();
            outList.add(this.handleBreak(label, tmb, node));
         }

         DavaFlowSet finalOut = output;

         DavaFlowSet fo;
         for(Iterator var18 = outList.iterator(); var18.hasNext(); finalOut = this.merge((DavaFlowSet)finalOut, fo)) {
            fo = (DavaFlowSet)var18.next();
         }

         return (DavaFlowSet)finalOut;
      }
   }

   public DavaFlowSet processASTTryNode(ASTTryNode node, DavaFlowSet input) {
      if (DEBUG) {
         System.out.println("Processing try node");
      }

      if (!this.isReachable(input)) {
         return input;
      } else {
         List<Object> tryBody = node.get_TryBody();
         DavaFlowSet tryBodyOutput = this.process(tryBody, input);
         DavaFlowSet inputCatch = this.newInitialFlow();
         List<Object> catchList = node.get_CatchList();
         Iterator<Object> it = catchList.iterator();
         ArrayList catchOutput = new ArrayList();

         DavaFlowSet out;
         while(it.hasNext()) {
            ASTTryNode.container catchBody = (ASTTryNode.container)it.next();
            List body = (List)catchBody.o;
            out = this.process(body, this.cloneFlowSet(inputCatch));
            catchOutput.add(out);
         }

         String label = this.getLabel(node);
         List<DavaFlowSet> outList = new ArrayList();
         outList.add(this.handleBreak(label, tryBodyOutput, node));
         Iterator var16 = catchOutput.iterator();

         DavaFlowSet ce;
         while(var16.hasNext()) {
            DavaFlowSet co = (DavaFlowSet)var16.next();
            ce = this.handleBreak(label, co, node);
            outList.add(ce);
         }

         out = tryBodyOutput;

         Iterator var17;
         for(var17 = outList.iterator(); var17.hasNext(); out = this.merge(out, ce)) {
            ce = (DavaFlowSet)var17.next();
         }

         for(var17 = catchOutput.iterator(); var17.hasNext(); out = this.merge(out, ce)) {
            ce = (DavaFlowSet)var17.next();
         }

         return out;
      }
   }

   public boolean isConstructReachable(Object construct) {
      Object temp = this.getBeforeSet(construct);
      if (temp == null) {
         return true;
      } else {
         if (DEBUG) {
            System.out.println("Have before set");
         }

         Boolean reachable = (Boolean)((UnreachableCodeFinder.UnreachableCodeFlowSet)temp).elements[0];
         return reachable;
      }
   }

   public class UnreachableCodeFlowSet extends DavaFlowSet {
      public UnreachableCodeFinder.UnreachableCodeFlowSet clone() {
         if (this.size() != 1) {
            throw new DecompilationException("unreachableCodeFlow set size should always be 1");
         } else {
            Boolean temp = (Boolean)this.elements[0];
            UnreachableCodeFinder.UnreachableCodeFlowSet toReturn = UnreachableCodeFinder.this.new UnreachableCodeFlowSet();
            toReturn.add(new Boolean(temp));
            toReturn.copyInternalDataFrom(this);
            return toReturn;
         }
      }

      public void intersection(FlowSet otherFlow, FlowSet destFlow) {
         if (UnreachableCodeFinder.DEBUG) {
            System.out.println("In intersection");
         }

         if (otherFlow instanceof UnreachableCodeFinder.UnreachableCodeFlowSet && destFlow instanceof UnreachableCodeFinder.UnreachableCodeFlowSet) {
            UnreachableCodeFinder.UnreachableCodeFlowSet other = (UnreachableCodeFinder.UnreachableCodeFlowSet)otherFlow;
            UnreachableCodeFinder.UnreachableCodeFlowSet dest = (UnreachableCodeFinder.UnreachableCodeFlowSet)destFlow;
            UnreachableCodeFinder.UnreachableCodeFlowSet workingSet;
            if (dest != other && dest != this) {
               workingSet = dest;
               dest.clear();
            } else {
               workingSet = UnreachableCodeFinder.this.new UnreachableCodeFlowSet();
            }

            if (other.size() == 1 && this.size() == 1) {
               Boolean thisPath = (Boolean)this.elements[0];
               Boolean otherPath = (Boolean)other.elements[0];
               if (!thisPath && !otherPath) {
                  workingSet.add(new Boolean(false));
               } else {
                  workingSet.add(new Boolean(true));
               }

               workingSet.copyInternalDataFrom(this);
               if (otherFlow instanceof DavaFlowSet) {
                  workingSet.copyInternalDataFrom((DavaFlowSet)otherFlow);
               }

               if (workingSet != dest) {
                  workingSet.copy(dest);
               }

               if (UnreachableCodeFinder.DEBUG) {
                  System.out.println("destFlow contains size:" + ((UnreachableCodeFinder.UnreachableCodeFlowSet)destFlow).size());
               }

            } else {
               System.out.println("Other size = " + other.size());
               System.out.println("This size = " + this.size());
               throw new DecompilationException("UnreachableCodeFlowSet size should always be one");
            }
         } else {
            super.intersection(otherFlow, destFlow);
         }
      }
   }
}
