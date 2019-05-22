package soot.dava.toolkits.base.AST.structuredAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Local;
import soot.Value;
import soot.dava.internal.AST.ASTAggregatedCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTLabeledNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.AST.ASTUnaryBinaryCondition;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;

public abstract class StructuredAnalysis<E> {
   public static boolean DEBUG = false;
   public static boolean DEBUG_IF = false;
   public static boolean DEBUG_WHILE = false;
   public static boolean DEBUG_STATEMENTS = false;
   public static boolean DEBUG_TRY = false;
   DavaFlowSet<E> NOPATH = this.emptyFlowSet();
   public int MERGETYPE = 0;
   final int UNDEFINED = 0;
   final int UNION = 1;
   final int INTERSECTION = 2;
   HashMap<Object, DavaFlowSet<E>> beforeSets = new HashMap();
   HashMap<Object, DavaFlowSet<E>> afterSets = new HashMap();

   public StructuredAnalysis() {
      this.setMergeType();
      if (this.MERGETYPE == 0) {
         throw new RuntimeException("MERGETYPE UNDEFINED");
      }
   }

   public abstract void setMergeType();

   public abstract DavaFlowSet<E> newInitialFlow();

   public abstract DavaFlowSet<E> emptyFlowSet();

   public abstract DavaFlowSet<E> cloneFlowSet(DavaFlowSet<E> var1);

   public abstract DavaFlowSet<E> processStatement(Stmt var1, DavaFlowSet<E> var2);

   public abstract DavaFlowSet<E> processUnaryBinaryCondition(ASTUnaryBinaryCondition var1, DavaFlowSet<E> var2);

   public abstract DavaFlowSet<E> processSynchronizedLocal(Local var1, DavaFlowSet<E> var2);

   public abstract DavaFlowSet<E> processSwitchKey(Value var1, DavaFlowSet<E> var2);

   public void print(Object toPrint) {
      System.out.println(toPrint.toString());
   }

   public DavaFlowSet<E> processCondition(ASTCondition cond, DavaFlowSet<E> input) {
      if (cond instanceof ASTUnaryBinaryCondition) {
         return this.processUnaryBinaryCondition((ASTUnaryBinaryCondition)cond, input);
      } else if (cond instanceof ASTAggregatedCondition) {
         ASTCondition left = ((ASTAggregatedCondition)cond).getLeftOp();
         DavaFlowSet<E> output1 = this.processCondition(left, input);
         ASTCondition right = ((ASTAggregatedCondition)cond).getRightOp();
         DavaFlowSet<E> output2 = this.processCondition(right, output1);
         return this.merge(output1, output2);
      } else {
         throw new RuntimeException("Unknown ASTCondition found in structred flow analysis");
      }
   }

   public DavaFlowSet<E> process(Object body, DavaFlowSet<E> input) {
      DavaFlowSet result;
      if (body instanceof ASTNode) {
         this.beforeSets.put(body, input);
         result = this.processASTNode((ASTNode)body, input);
         this.afterSets.put(body, result);
         return result;
      } else if (body instanceof Stmt) {
         this.beforeSets.put(body, input);
         result = this.processAbruptStatements((Stmt)body, input);
         this.afterSets.put(body, result);
         return result;
      } else if (body instanceof AugmentedStmt) {
         AugmentedStmt as = (AugmentedStmt)body;
         Stmt s = as.get_Stmt();
         this.beforeSets.put(s, input);
         DavaFlowSet<E> result = this.processAbruptStatements(s, input);
         this.afterSets.put(s, result);
         return result;
      } else if (body instanceof List) {
         Iterator it = ((List)body).iterator();
         DavaFlowSet result = input;

         while(it.hasNext()) {
            Object temp = it.next();
            if (!(temp instanceof ASTNode)) {
               throw new RuntimeException("Body sent to be processed by StructuredAnalysis contains a list which does not have ASTNodes");
            }

            this.beforeSets.put(temp, result);
            result = this.processASTNode((ASTNode)temp, result);
            this.afterSets.put(temp, result);
         }

         return result;
      } else {
         throw new RuntimeException("Body sent to be processed by StructuredAnalysis is not a valid body");
      }
   }

   public DavaFlowSet<E> processASTNode(ASTNode node, DavaFlowSet<E> input) {
      if (node instanceof ASTDoWhileNode) {
         return this.processASTDoWhileNode((ASTDoWhileNode)node, input);
      } else if (node instanceof ASTForLoopNode) {
         return this.processASTForLoopNode((ASTForLoopNode)node, input);
      } else if (node instanceof ASTIfElseNode) {
         return this.processASTIfElseNode((ASTIfElseNode)node, input);
      } else if (node instanceof ASTIfNode) {
         return this.processASTIfNode((ASTIfNode)node, input);
      } else if (node instanceof ASTLabeledBlockNode) {
         return this.processASTLabeledBlockNode((ASTLabeledBlockNode)node, input);
      } else if (node instanceof ASTMethodNode) {
         return this.processASTMethodNode((ASTMethodNode)node, input);
      } else if (node instanceof ASTStatementSequenceNode) {
         return this.processASTStatementSequenceNode((ASTStatementSequenceNode)node, input);
      } else if (node instanceof ASTSwitchNode) {
         return this.processASTSwitchNode((ASTSwitchNode)node, input);
      } else if (node instanceof ASTSynchronizedBlockNode) {
         return this.processASTSynchronizedBlockNode((ASTSynchronizedBlockNode)node, input);
      } else if (node instanceof ASTTryNode) {
         return this.processASTTryNode((ASTTryNode)node, input);
      } else if (node instanceof ASTWhileNode) {
         return this.processASTWhileNode((ASTWhileNode)node, input);
      } else if (node instanceof ASTUnconditionalLoopNode) {
         return this.processASTUnconditionalLoopNode((ASTUnconditionalLoopNode)node, input);
      } else {
         throw new RuntimeException("processASTNode called using unknown node type");
      }
   }

   public final DavaFlowSet<E> processSingleSubBodyNode(ASTNode node, DavaFlowSet<E> input) {
      List<Object> subBodies = node.get_SubBodies();
      if (subBodies.size() != 1) {
         throw new RuntimeException("processSingleSubBodyNode called with a node without one subBody");
      } else {
         List subBody = (List)subBodies.get(0);
         return this.process(subBody, input);
      }
   }

   public String getLabel(ASTNode node) {
      if (node instanceof ASTLabeledNode) {
         Object temp = ((ASTLabeledNode)node).get_Label();
         if (temp != null) {
            return temp.toString();
         }
      }

      return null;
   }

   public DavaFlowSet<E> processAbruptStatements(Stmt s, DavaFlowSet<E> input) {
      if (!(s instanceof ReturnStmt) && !(s instanceof RetStmt) && !(s instanceof ReturnVoidStmt)) {
         if (s instanceof DAbruptStmt) {
            DAbruptStmt abStmt = (DAbruptStmt)s;
            if (!abStmt.is_Continue() && !abStmt.is_Break()) {
               throw new RuntimeException("Found a DAbruptStmt which is neither break nor continue!!");
            } else {
               DavaFlowSet<E> temp = this.NOPATH;
               SETNodeLabel nodeLabel = abStmt.getLabel();
               if (nodeLabel != null && nodeLabel.toString() != null) {
                  if (abStmt.is_Continue()) {
                     temp.addToContinueList(nodeLabel.toString(), input);
                  } else {
                     if (!abStmt.is_Break()) {
                        throw new RuntimeException("Found abruptstmt which is neither break nor continue");
                     }

                     temp.addToBreakList(nodeLabel.toString(), input);
                  }
               } else if (abStmt.is_Continue()) {
                  temp.addToImplicitContinues(abStmt, input);
               } else {
                  if (!abStmt.is_Break()) {
                     throw new RuntimeException("Found abruptstmt which is neither break nor continue");
                  }

                  temp.addToImplicitBreaks(abStmt, input);
               }

               return temp;
            }
         } else {
            return this.processStatement(s, input);
         }
      } else {
         return this.NOPATH;
      }
   }

   public DavaFlowSet<E> processASTMethodNode(ASTMethodNode node, DavaFlowSet<E> input) {
      DavaFlowSet<E> temp = this.processSingleSubBodyNode(node, input);
      return temp;
   }

   public DavaFlowSet<E> processASTStatementSequenceNode(ASTStatementSequenceNode node, DavaFlowSet<E> input) {
      DavaFlowSet<E> output = this.cloneFlowSet(input);
      Iterator var4 = node.getStatements().iterator();

      while(var4.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var4.next();
         Stmt s = as.get_Stmt();
         output = this.process(s, output);
         if (DEBUG_STATEMENTS) {
            System.out.println("After Processing statement " + s + output.toString());
         }
      }

      return output;
   }

   public DavaFlowSet<E> processASTLabeledBlockNode(ASTLabeledBlockNode node, DavaFlowSet<E> input) {
      DavaFlowSet<E> output1 = this.processSingleSubBodyNode(node, input);
      String label = this.getLabel(node);
      return this.handleBreak(label, output1, node);
   }

   public DavaFlowSet<E> processASTSynchronizedBlockNode(ASTSynchronizedBlockNode node, DavaFlowSet<E> input) {
      input = this.processSynchronizedLocal(node.getLocal(), input);
      DavaFlowSet<E> output = this.processSingleSubBodyNode(node, input);
      String label = this.getLabel(node);
      return this.handleBreak(label, output, node);
   }

   public DavaFlowSet<E> processASTIfNode(ASTIfNode node, DavaFlowSet<E> input) {
      input = this.processCondition(node.get_Condition(), input);
      DavaFlowSet<E> output1 = this.processSingleSubBodyNode(node, input);
      DavaFlowSet<E> output2 = this.merge(input, output1);
      String label = this.getLabel(node);
      DavaFlowSet<E> temp = this.handleBreak(label, output2, node);
      if (DEBUG_IF) {
         System.out.println("Exiting if node" + temp.toString());
      }

      return temp;
   }

   public DavaFlowSet<E> processASTIfElseNode(ASTIfElseNode node, DavaFlowSet<E> input) {
      List<Object> subBodies = node.get_SubBodies();
      if (subBodies.size() != 2) {
         throw new RuntimeException("processASTIfElseNode called with a node without two subBodies");
      } else {
         List subBodyOne = (List)subBodies.get(0);
         List subBodyTwo = (List)subBodies.get(1);
         input = this.processCondition(node.get_Condition(), input);
         DavaFlowSet<E> clonedInput = this.cloneFlowSet(input);
         DavaFlowSet<E> output1 = this.process(subBodyOne, clonedInput);
         clonedInput = this.cloneFlowSet(input);
         DavaFlowSet<E> output2 = this.process(subBodyTwo, clonedInput);
         DavaFlowSet<E> temp = this.merge(output1, output2);
         String label = this.getLabel(node);
         output1 = this.handleBreak(label, temp, node);
         return output1;
      }
   }

   public DavaFlowSet<E> processASTWhileNode(ASTWhileNode node, DavaFlowSet<E> input) {
      DavaFlowSet<E> lastin = null;
      DavaFlowSet<E> initialInput = this.cloneFlowSet(input);
      String label = this.getLabel(node);
      DavaFlowSet<E> output = null;
      input = this.processCondition(node.get_Condition(), input);
      if (DEBUG_WHILE) {
         System.out.println("Going int while (condition processed): " + input.toString());
      }

      do {
         lastin = this.cloneFlowSet(input);
         output = this.processSingleSubBodyNode(node, input);
         output = this.handleContinue(label, output, node);
         input = this.merge(initialInput, output);
         input = this.processCondition(node.get_Condition(), input);
      } while(this.isDifferent(lastin, input));

      DavaFlowSet<E> temp = this.handleBreak(label, input, node);
      if (DEBUG_WHILE) {
         System.out.println("Going out of while: " + temp.toString());
      }

      return temp;
   }

   public DavaFlowSet<E> processASTDoWhileNode(ASTDoWhileNode node, DavaFlowSet<E> input) {
      DavaFlowSet<E> lastin = null;
      DavaFlowSet<E> output = null;
      DavaFlowSet<E> initialInput = this.cloneFlowSet(input);
      String label = this.getLabel(node);
      if (DEBUG_WHILE) {
         System.out.println("Going into do-while: " + initialInput.toString());
      }

      do {
         lastin = this.cloneFlowSet(input);
         output = this.processSingleSubBodyNode(node, input);
         output = this.handleContinue(label, output, node);
         output = this.processCondition(node.get_Condition(), output);
         input = this.merge(initialInput, output);
      } while(this.isDifferent(lastin, input));

      DavaFlowSet<E> temp = this.handleBreak(label, output, node);
      if (DEBUG_WHILE) {
         System.out.println("Going out of do-while: " + temp.toString());
      }

      return temp;
   }

   public DavaFlowSet<E> processASTUnconditionalLoopNode(ASTUnconditionalLoopNode node, DavaFlowSet<E> input) {
      DavaFlowSet<E> initialInput = this.cloneFlowSet(input);
      DavaFlowSet<E> lastin = null;
      if (DEBUG_WHILE) {
         System.out.println("Going into while(true): " + initialInput.toString());
      }

      String label = this.getLabel(node);
      DavaFlowSet output = null;

      do {
         lastin = this.cloneFlowSet(input);
         output = this.processSingleSubBodyNode(node, input);
         output = this.handleContinue(label, output, node);
         input = this.merge(initialInput, output);
      } while(this.isDifferent(lastin, input));

      DavaFlowSet<E> temp = this.getMergedBreakList(label, output, node);
      if (DEBUG_WHILE) {
         System.out.println("Going out of while(true): " + temp.toString());
      }

      return temp;
   }

   public DavaFlowSet<E> processASTForLoopNode(ASTForLoopNode node, DavaFlowSet<E> input) {
      AugmentedStmt lastin;
      Stmt s;
      for(Iterator var3 = node.getInit().iterator(); var3.hasNext(); input = this.process(s, input)) {
         lastin = (AugmentedStmt)var3.next();
         s = lastin.get_Stmt();
      }

      DavaFlowSet<E> initialInput = this.cloneFlowSet(input);
      input = this.processCondition(node.get_Condition(), input);
      lastin = null;
      String label = this.getLabel(node);
      DavaFlowSet output2 = null;

      DavaFlowSet lastin;
      do {
         lastin = this.cloneFlowSet(input);
         DavaFlowSet<E> output1 = this.processSingleSubBodyNode(node, input);
         output1 = this.handleContinue(label, output1, node);
         output2 = this.cloneFlowSet(output1);

         Stmt s;
         for(Iterator var8 = node.getUpdate().iterator(); var8.hasNext(); output2 = this.process(s, output2)) {
            AugmentedStmt as = (AugmentedStmt)var8.next();
            s = as.get_Stmt();
         }

         input = this.merge(initialInput, output2);
         input = this.processCondition(node.get_Condition(), input);
      } while(this.isDifferent(lastin, input));

      return this.handleBreak(label, input, node);
   }

   public DavaFlowSet<E> processASTSwitchNode(ASTSwitchNode node, DavaFlowSet<E> input) {
      if (DEBUG) {
         System.out.println("Going into switch: " + input.toString());
      }

      List<Object> indexList = node.getIndexList();
      Map<Object, List<Object>> index2BodyList = node.getIndex2BodyList();
      Iterator<Object> it = indexList.iterator();
      input = this.processSwitchKey(node.get_Key(), input);
      DavaFlowSet<E> initialIn = this.cloneFlowSet(input);
      DavaFlowSet<E> out = null;
      DavaFlowSet<E> defaultOut = null;
      ArrayList toMergeBreaks = new ArrayList();

      Object currentIndex;
      while(it.hasNext()) {
         currentIndex = it.next();
         List body = (List)index2BodyList.get(currentIndex);
         if (body != null) {
            out = this.process(body, input);
            toMergeBreaks.add(this.cloneFlowSet(out));
            if (currentIndex instanceof String) {
               defaultOut = out;
            }

            input = this.merge(out, initialIn);
         }
      }

      currentIndex = null;
      DavaFlowSet output;
      if (out != null) {
         if (defaultOut != null) {
            output = this.merge(defaultOut, out);
         } else {
            output = this.merge(initialIn, out);
         }
      } else {
         output = initialIn;
      }

      String label = this.getLabel(node);
      List<DavaFlowSet<E>> outList = new ArrayList();
      Iterator var13 = toMergeBreaks.iterator();

      while(var13.hasNext()) {
         DavaFlowSet<E> mset = (DavaFlowSet)var13.next();
         outList.add(this.handleBreak(label, mset, node));
      }

      DavaFlowSet<E> finalOut = output;

      DavaFlowSet outIt;
      for(Iterator var19 = outList.iterator(); var19.hasNext(); finalOut = this.merge(finalOut, outIt)) {
         outIt = (DavaFlowSet)var19.next();
      }

      if (DEBUG) {
         System.out.println("Going out of switch: " + finalOut.toString());
      }

      return finalOut;
   }

   public DavaFlowSet<E> processASTTryNode(ASTTryNode node, DavaFlowSet<E> input) {
      if (DEBUG_TRY) {
         System.out.println("TRY START is:" + input);
      }

      List<Object> tryBody = node.get_TryBody();
      DavaFlowSet<E> tryBodyOutput = this.process(tryBody, input);
      DavaFlowSet<E> inputCatch = this.newInitialFlow();
      if (DEBUG_TRY) {
         System.out.println("TRY initialFLOW is:" + inputCatch);
      }

      List<Object> catchList = node.get_CatchList();
      Iterator<Object> it = catchList.iterator();
      ArrayList catchOutput = new ArrayList();

      DavaFlowSet out;
      while(it.hasNext()) {
         ASTTryNode.container catchBody = (ASTTryNode.container)it.next();
         List<E> body = (List)catchBody.o;
         out = this.process(body, this.cloneFlowSet(inputCatch));
         catchOutput.add(out);
      }

      String label = this.getLabel(node);
      List<DavaFlowSet<E>> outList = new ArrayList();
      outList.add(this.handleBreak(label, tryBodyOutput, node));

      DavaFlowSet co;
      for(Iterator var16 = catchOutput.iterator(); var16.hasNext(); outList.add(co)) {
         DavaFlowSet<E> co = (DavaFlowSet)var16.next();
         co = this.handleBreak(label, co, node);
         if (DEBUG_TRY) {
            System.out.println("TRY handling breaks is:" + co);
         }
      }

      out = tryBodyOutput;

      Iterator var17;
      for(var17 = outList.iterator(); var17.hasNext(); out = this.merge(out, co)) {
         co = (DavaFlowSet)var17.next();
      }

      if (DEBUG_TRY) {
         System.out.println("TRY after merge outList is:" + out);
      }

      for(var17 = catchOutput.iterator(); var17.hasNext(); out = this.merge(out, co)) {
         co = (DavaFlowSet)var17.next();
      }

      if (DEBUG_TRY) {
         System.out.println("TRY END RESULT is:" + out);
      }

      return out;
   }

   public DavaFlowSet<E> merge(DavaFlowSet<E> in1, DavaFlowSet<E> in2) {
      if (this.MERGETYPE == 0) {
         throw new RuntimeException("Use the setMergeType method to set the type of merge used in the analysis");
      } else {
         DavaFlowSet out;
         if (in1 == this.NOPATH && in2 != this.NOPATH) {
            out = in2.clone();
            out.copyInternalDataFrom(in1);
            return out;
         } else if (in1 != this.NOPATH && in2 == this.NOPATH) {
            out = in1.clone();
            out.copyInternalDataFrom(in2);
            return out;
         } else if (in1 == this.NOPATH && in2 == this.NOPATH) {
            out = in1.clone();
            out.copyInternalDataFrom(in2);
            return out;
         } else {
            out = this.emptyFlowSet();
            if (this.MERGETYPE == 1) {
               in1.union(in2, out);
            } else {
               if (this.MERGETYPE != 2) {
                  throw new RuntimeException("Merge type value" + this.MERGETYPE + " not recognized");
               }

               in1.intersection(in2, out);
            }

            out.copyInternalDataFrom(in1);
            out.copyInternalDataFrom(in2);
            return out;
         }
      }
   }

   public DavaFlowSet<E> mergeExplicitAndImplicit(String label, DavaFlowSet<E> output, List<DavaFlowSet<E>> explicitSet, List<DavaFlowSet<E>> implicitSet) {
      DavaFlowSet<E> toReturn = output.clone();
      Iterator it;
      if (label != null && explicitSet != null && explicitSet.size() != 0) {
         it = explicitSet.iterator();

         for(toReturn = this.merge(output, (DavaFlowSet)it.next()); it.hasNext(); toReturn = this.merge(toReturn, (DavaFlowSet)it.next())) {
         }
      }

      if (implicitSet != null) {
         for(it = implicitSet.iterator(); it.hasNext(); toReturn = this.merge(toReturn, (DavaFlowSet)it.next())) {
         }
      }

      return toReturn;
   }

   public DavaFlowSet<E> handleBreak(String label, DavaFlowSet<E> out, ASTNode node) {
      List<DavaFlowSet<E>> explicitSet = out.getBreakSet(label);
      if (node == null) {
         throw new RuntimeException("ASTNode sent to handleBreak was null");
      } else {
         List<DavaFlowSet<E>> implicitSet = out.getImplicitlyBrokenSets(node);
         return this.mergeExplicitAndImplicit(label, out, explicitSet, implicitSet);
      }
   }

   public DavaFlowSet<E> handleContinue(String label, DavaFlowSet<E> out, ASTNode node) {
      List<DavaFlowSet<E>> explicitSet = out.getContinueSet(label);
      if (node == null) {
         throw new RuntimeException("ASTNode sent to handleContinue was null");
      } else {
         List<DavaFlowSet<E>> implicitSet = out.getImplicitlyContinuedSets(node);
         return this.mergeExplicitAndImplicit(label, out, explicitSet, implicitSet);
      }
   }

   private DavaFlowSet<E> getMergedBreakList(String label, DavaFlowSet<E> output, ASTNode node) {
      List<DavaFlowSet<E>> breakSet = output.getBreakSet(label);
      DavaFlowSet<E> toReturn = null;
      if (breakSet == null) {
         toReturn = this.NOPATH;
      } else if (breakSet.size() == 0) {
         toReturn = this.NOPATH;
      } else {
         Iterator<DavaFlowSet<E>> it = breakSet.iterator();

         for(toReturn = (DavaFlowSet)it.next(); it.hasNext(); toReturn = this.merge(toReturn, (DavaFlowSet)it.next())) {
         }
      }

      List<DavaFlowSet<E>> implicitSet = output.getImplicitlyBrokenSets(node);
      if (implicitSet != null) {
         Iterator<DavaFlowSet<E>> it = implicitSet.iterator();
         if (implicitSet.size() > 0) {
            toReturn = (DavaFlowSet)it.next();
         }

         while(it.hasNext()) {
            toReturn = this.merge(toReturn, (DavaFlowSet)it.next());
         }
      }

      return toReturn;
   }

   public boolean isDifferent(DavaFlowSet<E> oldObj, DavaFlowSet<E> newObj) {
      return !oldObj.equals(newObj) || !oldObj.internalDataMatchesTo(newObj);
   }

   public DavaFlowSet<E> getBeforeSet(Object beforeThis) {
      return (DavaFlowSet)this.beforeSets.get(beforeThis);
   }

   public DavaFlowSet<E> getAfterSet(Object afterThis) {
      return (DavaFlowSet)this.afterSets.get(afterThis);
   }

   public void debug(String methodName, String debug) {
      if (DEBUG) {
         System.out.println("Class: StructuredAnalysis MethodName: " + methodName + "    DEBUG: " + debug);
      }

   }

   public void debug(String debug) {
      if (DEBUG) {
         System.out.println("Class: StructuredAnalysis DEBUG: " + debug);
      }

   }
}
