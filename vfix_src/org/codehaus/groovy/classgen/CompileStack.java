package org.codehaus.groovy.classgen;

import groovyjarjarasm.asm.Label;
import groovyjarjarasm.asm.MethodVisitor;
import groovyjarjarasm.asm.Opcodes;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.VariableScope;

public class CompileStack implements Opcodes {
   private boolean clear = true;
   private VariableScope scope;
   private Label continueLabel;
   private Label breakLabel;
   private Map stackVariables = new HashMap();
   private int currentVariableIndex = 1;
   private int nextVariableIndex = 1;
   private final LinkedList temporaryVariables = new LinkedList();
   private final LinkedList usedVariables = new LinkedList();
   private Map superBlockNamedLabels = new HashMap();
   private Map currentBlockNamedLabels = new HashMap();
   private LinkedList<CompileStack.BlockRecorder> finallyBlocks = new LinkedList();
   private LinkedList<CompileStack.BlockRecorder> visitedBlocks = new LinkedList();
   private Label thisStartLabel;
   private Label thisEndLabel;
   private MethodVisitor mv;
   private BytecodeHelper helper;
   private final LinkedList stateStack = new LinkedList();
   private int localVariableOffset;
   private final Map namedLoopBreakLabel = new HashMap();
   private final Map namedLoopContinueLabel = new HashMap();
   private String className;
   private LinkedList<CompileStack.ExceptionTableEntry> typedExceptions = new LinkedList();
   private LinkedList<CompileStack.ExceptionTableEntry> untypedExceptions = new LinkedList();

   protected void pushState() {
      this.stateStack.add(new CompileStack.StateStackElement());
      this.stackVariables = new HashMap(this.stackVariables);
      this.finallyBlocks = new LinkedList(this.finallyBlocks);
   }

   private void popState() {
      if (this.stateStack.size() == 0) {
         throw new GroovyBugError("Tried to do a pop on the compile stack without push.");
      } else {
         CompileStack.StateStackElement element = (CompileStack.StateStackElement)this.stateStack.removeLast();
         this.scope = element.scope;
         this.continueLabel = element.continueLabel;
         this.breakLabel = element.breakLabel;
         this.currentVariableIndex = element.lastVariableIndex;
         this.stackVariables = element.stackVariables;
         this.nextVariableIndex = element.nextVariableIndex;
         this.finallyBlocks = element.finallyBlocks;
      }
   }

   public Label getContinueLabel() {
      return this.continueLabel;
   }

   public Label getBreakLabel() {
      return this.breakLabel;
   }

   public void removeVar(int tempIndex) {
      Variable head = (Variable)this.temporaryVariables.removeFirst();
      if (head.getIndex() != tempIndex) {
         throw new GroovyBugError("CompileStack#removeVar: tried to remove a temporary variable in wrong order");
      } else {
         this.currentVariableIndex = head.getPrevIndex();
         this.nextVariableIndex = tempIndex;
      }
   }

   private void setEndLabels() {
      Label endLabel = new Label();
      this.mv.visitLabel(endLabel);
      Iterator iter = this.stackVariables.values().iterator();

      while(iter.hasNext()) {
         Variable var = (Variable)iter.next();
         var.setEndLabel(endLabel);
      }

      this.thisEndLabel = endLabel;
   }

   public void pop() {
      this.setEndLabels();
      this.popState();
   }

   public VariableScope getScope() {
      return this.scope;
   }

   public int defineTemporaryVariable(org.codehaus.groovy.ast.Variable var, boolean store) {
      return this.defineTemporaryVariable(var.getName(), var.getType(), store);
   }

   public Variable getVariable(String variableName) {
      return this.getVariable(variableName, true);
   }

   public Variable getVariable(String variableName, boolean mustExist) {
      if (variableName.equals("this")) {
         return Variable.THIS_VARIABLE;
      } else if (variableName.equals("super")) {
         return Variable.SUPER_VARIABLE;
      } else {
         Variable v = (Variable)this.stackVariables.get(variableName);
         if (v == null && mustExist) {
            throw new GroovyBugError("tried to get a variable with the name " + variableName + " as stack variable, but a variable with this name was not created");
         } else {
            return v;
         }
      }
   }

   public int defineTemporaryVariable(String name, boolean store) {
      return this.defineTemporaryVariable(name, ClassHelper.DYNAMIC_TYPE, store);
   }

   public int defineTemporaryVariable(String name, ClassNode node, boolean store) {
      Variable answer = this.defineVar(name, node, false);
      this.temporaryVariables.addFirst(answer);
      this.usedVariables.removeLast();
      if (store) {
         this.mv.visitVarInsn(58, this.currentVariableIndex);
      }

      return answer.getIndex();
   }

   private void resetVariableIndex(boolean isStatic) {
      if (!isStatic) {
         this.currentVariableIndex = 1;
         this.nextVariableIndex = 1;
      } else {
         this.currentVariableIndex = 0;
         this.nextVariableIndex = 0;
      }

   }

   public void clear() {
      if (this.stateStack.size() > 1) {
         int size = this.stateStack.size() - 1;
         throw new GroovyBugError("the compile stack contains " + size + " more push instruction" + (size == 1 ? "" : "s") + " than pops.");
      } else {
         this.clear = true;
         if (this.thisEndLabel == null) {
            this.setEndLabels();
         }

         if (!this.scope.isInStaticContext()) {
            this.mv.visitLocalVariable("this", this.className, (String)null, this.thisStartLabel, this.thisEndLabel, 0);
         }

         Iterator i$ = this.usedVariables.iterator();

         while(i$.hasNext()) {
            Variable v = (Variable)i$.next();
            String type = BytecodeHelper.getTypeDescription(v.getType());
            Label start = v.getStartLabel();
            Label end = v.getEndLabel();
            this.mv.visitLocalVariable(v.getName(), type, (String)null, start, end, v.getIndex());
         }

         i$ = this.typedExceptions.iterator();

         CompileStack.ExceptionTableEntry ep;
         while(i$.hasNext()) {
            ep = (CompileStack.ExceptionTableEntry)i$.next();
            this.mv.visitTryCatchBlock(ep.start, ep.end, ep.goal, ep.sig);
         }

         i$ = this.untypedExceptions.iterator();

         while(i$.hasNext()) {
            ep = (CompileStack.ExceptionTableEntry)i$.next();
            this.mv.visitTryCatchBlock(ep.start, ep.end, ep.goal, ep.sig);
         }

         this.pop();
         this.typedExceptions.clear();
         this.untypedExceptions.clear();
         this.stackVariables.clear();
         this.usedVariables.clear();
         this.scope = null;
         this.finallyBlocks.clear();
         this.mv = null;
         this.resetVariableIndex(false);
         this.superBlockNamedLabels.clear();
         this.currentBlockNamedLabels.clear();
         this.namedLoopBreakLabel.clear();
         this.namedLoopContinueLabel.clear();
         this.continueLabel = null;
         this.breakLabel = null;
         this.helper = null;
         this.thisStartLabel = null;
         this.thisEndLabel = null;
      }
   }

   public void addExceptionBlock(Label start, Label end, Label goal, String sig) {
      CompileStack.ExceptionTableEntry ep = new CompileStack.ExceptionTableEntry();
      ep.start = start;
      ep.end = end;
      ep.sig = sig;
      ep.goal = goal;
      if (sig == null) {
         this.untypedExceptions.add(ep);
      } else {
         this.typedExceptions.add(ep);
      }

   }

   protected void init(VariableScope el, Parameter[] parameters, MethodVisitor mv, ClassNode cn) {
      if (!this.clear) {
         throw new GroovyBugError("CompileStack#init called without calling clear before");
      } else {
         this.clear = false;
         this.pushVariableScope(el);
         this.mv = mv;
         this.helper = new BytecodeHelper(mv);
         this.defineMethodVariables(parameters, el.isInStaticContext());
         this.className = BytecodeHelper.getTypeDescription(cn);
      }
   }

   protected void pushVariableScope(VariableScope el) {
      this.pushState();
      this.scope = el;
      this.superBlockNamedLabels = new HashMap(this.superBlockNamedLabels);
      this.superBlockNamedLabels.putAll(this.currentBlockNamedLabels);
      this.currentBlockNamedLabels = new HashMap();
   }

   protected void pushLoop(VariableScope el, String labelName) {
      this.pushVariableScope(el);
      this.initLoopLabels(labelName);
   }

   private void initLoopLabels(String labelName) {
      this.continueLabel = new Label();
      this.breakLabel = new Label();
      if (labelName != null) {
         this.namedLoopBreakLabel.put(labelName, this.breakLabel);
         this.namedLoopContinueLabel.put(labelName, this.continueLabel);
      }

   }

   protected void pushLoop(String labelName) {
      this.pushState();
      this.initLoopLabels(labelName);
   }

   protected Label getNamedBreakLabel(String name) {
      Label label = this.getBreakLabel();
      Label endLabel = null;
      if (name != null) {
         endLabel = (Label)this.namedLoopBreakLabel.get(name);
      }

      if (endLabel != null) {
         label = endLabel;
      }

      return label;
   }

   protected Label getNamedContinueLabel(String name) {
      Label label = this.getLabel(name);
      Label endLabel = null;
      if (name != null) {
         endLabel = (Label)this.namedLoopContinueLabel.get(name);
      }

      if (endLabel != null) {
         label = endLabel;
      }

      return label;
   }

   protected Label pushSwitch() {
      this.pushState();
      this.breakLabel = new Label();
      return this.breakLabel;
   }

   protected void pushBooleanExpression() {
      this.pushState();
   }

   private Variable defineVar(String name, ClassNode type, boolean methodParameterUsedInClosure) {
      int prevCurrent = this.currentVariableIndex;
      this.makeNextVariableID(type);
      int index = this.currentVariableIndex;
      if (methodParameterUsedInClosure) {
         index = this.localVariableOffset++;
         type = ClassHelper.getWrapper(type);
      }

      Variable answer = new Variable(index, type, name, prevCurrent);
      this.usedVariables.add(answer);
      answer.setHolder(methodParameterUsedInClosure);
      return answer;
   }

   private void makeLocalVariablesOffset(Parameter[] paras, boolean isInStaticContext) {
      this.resetVariableIndex(isInStaticContext);

      for(int i = 0; i < paras.length; ++i) {
         this.makeNextVariableID(paras[i].getType());
      }

      this.localVariableOffset = this.nextVariableIndex;
      this.resetVariableIndex(isInStaticContext);
   }

   private void defineMethodVariables(Parameter[] paras, boolean isInStaticContext) {
      Label startLabel = new Label();
      this.thisStartLabel = startLabel;
      this.mv.visitLabel(startLabel);
      this.makeLocalVariablesOffset(paras, isInStaticContext);
      boolean hasHolder = false;

      for(int i = 0; i < paras.length; ++i) {
         String name = paras[i].getName();
         ClassNode type = paras[i].getType();
         Variable answer;
         if (paras[i].isClosureSharedVariable()) {
            answer = this.defineVar(name, type, true);
            this.helper.load(type, this.currentVariableIndex);
            this.helper.box(type);
            Label newStart = new Label();
            this.mv.visitLabel(newStart);
            Variable var = new Variable(this.currentVariableIndex, paras[i].getOriginType(), name, this.currentVariableIndex);
            var.setStartLabel(startLabel);
            var.setEndLabel(newStart);
            this.usedVariables.add(var);
            answer.setStartLabel(newStart);
            this.createReference(answer);
            hasHolder = true;
         } else {
            answer = this.defineVar(name, type, false);
            answer.setStartLabel(startLabel);
         }

         this.stackVariables.put(name, answer);
      }

      if (hasHolder) {
         this.nextVariableIndex = this.localVariableOffset;
      }

   }

   private void createReference(Variable reference) {
      this.mv.visitTypeInsn(187, "groovy/lang/Reference");
      this.mv.visitInsn(90);
      this.mv.visitInsn(95);
      this.mv.visitMethodInsn(183, "groovy/lang/Reference", "<init>", "(Ljava/lang/Object;)V");
      this.mv.visitVarInsn(58, reference.getIndex());
   }

   public Variable defineVariable(org.codehaus.groovy.ast.Variable v, boolean initFromStack) {
      String name = v.getName();
      Variable answer = this.defineVar(name, v.getType(), false);
      if (v.isClosureSharedVariable()) {
         answer.setHolder(true);
      }

      this.stackVariables.put(name, answer);
      Label startLabel = new Label();
      answer.setStartLabel(startLabel);
      if (answer.isHolder()) {
         if (!initFromStack) {
            this.mv.visitInsn(1);
         }

         this.createReference(answer);
      } else {
         if (!initFromStack) {
            this.mv.visitInsn(1);
         }

         this.mv.visitVarInsn(58, this.currentVariableIndex);
      }

      this.mv.visitLabel(startLabel);
      return answer;
   }

   public boolean containsVariable(String name) {
      return this.stackVariables.containsKey(name);
   }

   private void makeNextVariableID(ClassNode type) {
      this.currentVariableIndex = this.nextVariableIndex;
      if (type == ClassHelper.long_TYPE || type == ClassHelper.double_TYPE) {
         ++this.nextVariableIndex;
      }

      ++this.nextVariableIndex;
   }

   public Label getLabel(String name) {
      if (name == null) {
         return null;
      } else {
         Label l = (Label)this.superBlockNamedLabels.get(name);
         if (l == null) {
            l = this.createLocalLabel(name);
         }

         return l;
      }
   }

   public Label createLocalLabel(String name) {
      Label l = (Label)this.currentBlockNamedLabels.get(name);
      if (l == null) {
         l = new Label();
         this.currentBlockNamedLabels.put(name, l);
      }

      return l;
   }

   public void applyFinallyBlocks(Label label, boolean isBreakLabel) {
      CompileStack.StateStackElement result = null;
      ListIterator iter = this.stateStack.listIterator(this.stateStack.size());

      while(iter.hasPrevious()) {
         CompileStack.StateStackElement element = (CompileStack.StateStackElement)iter.previous();
         if (!element.currentBlockNamedLabels.values().contains(label)) {
            if (isBreakLabel && element.breakLabel != label) {
               result = element;
               break;
            }

            if (!isBreakLabel && element.continueLabel != label) {
               result = element;
               break;
            }
         }
      }

      Object blocksToRemove;
      if (result == null) {
         blocksToRemove = Collections.EMPTY_LIST;
      } else {
         blocksToRemove = result.finallyBlocks;
      }

      List<CompileStack.BlockRecorder> blocks = new LinkedList(this.finallyBlocks);
      blocks.removeAll((Collection)blocksToRemove);
      this.applyBlockRecorder(blocks);
   }

   private void applyBlockRecorder(List<CompileStack.BlockRecorder> blocks) {
      if (blocks.size() != 0 && blocks.size() != this.visitedBlocks.size()) {
         Label end = new Label();
         this.mv.visitInsn(0);
         this.mv.visitLabel(end);
         Label newStart = new Label();
         Iterator i$ = blocks.iterator();

         while(i$.hasNext()) {
            CompileStack.BlockRecorder fb = (CompileStack.BlockRecorder)i$.next();
            if (!this.visitedBlocks.contains(fb)) {
               fb.closeRange(end);
               fb.excludedStatement.run();
               fb.startRange(newStart);
            }
         }

         this.mv.visitInsn(0);
         this.mv.visitLabel(newStart);
      }
   }

   public void applyBlockRecorder() {
      this.applyBlockRecorder(this.finallyBlocks);
   }

   public boolean hasBlockRecorder() {
      return !this.finallyBlocks.isEmpty();
   }

   public void pushBlockRecorder(CompileStack.BlockRecorder recorder) {
      this.pushState();
      this.finallyBlocks.addFirst(recorder);
   }

   public void pushBlockRecorderVisit(CompileStack.BlockRecorder finallyBlock) {
      this.visitedBlocks.add(finallyBlock);
   }

   public void popBlockRecorderVisit(CompileStack.BlockRecorder finallyBlock) {
      this.visitedBlocks.remove(finallyBlock);
   }

   public void writeExceptionTable(CompileStack.BlockRecorder block, Label goal, String sig) {
      if (!block.isEmpty) {
         Iterator i$ = block.ranges.iterator();

         while(i$.hasNext()) {
            CompileStack.LabelRange range = (CompileStack.LabelRange)i$.next();
            this.mv.visitTryCatchBlock(range.start, range.end, goal, sig);
         }

      }
   }

   private class StateStackElement {
      final VariableScope scope;
      final Label continueLabel;
      final Label breakLabel;
      final int lastVariableIndex;
      final int nextVariableIndex;
      final Map stackVariables;
      final Map currentBlockNamedLabels;
      final LinkedList<CompileStack.BlockRecorder> finallyBlocks;

      StateStackElement() {
         this.scope = CompileStack.this.scope;
         this.continueLabel = CompileStack.this.continueLabel;
         this.breakLabel = CompileStack.this.breakLabel;
         this.lastVariableIndex = CompileStack.this.currentVariableIndex;
         this.stackVariables = CompileStack.this.stackVariables;
         this.nextVariableIndex = CompileStack.this.nextVariableIndex;
         this.currentBlockNamedLabels = CompileStack.this.currentBlockNamedLabels;
         this.finallyBlocks = CompileStack.this.finallyBlocks;
      }
   }

   private class ExceptionTableEntry {
      Label start;
      Label end;
      Label goal;
      String sig;

      private ExceptionTableEntry() {
      }

      // $FF: synthetic method
      ExceptionTableEntry(Object x1) {
         this();
      }
   }

   protected static class BlockRecorder {
      private boolean isEmpty;
      public Runnable excludedStatement;
      public LinkedList<CompileStack.LabelRange> ranges;

      public BlockRecorder() {
         this.isEmpty = true;
         this.ranges = new LinkedList();
      }

      public BlockRecorder(Runnable excludedStatement) {
         this();
         this.excludedStatement = excludedStatement;
      }

      public void startRange(Label start) {
         CompileStack.LabelRange range = new CompileStack.LabelRange();
         range.start = start;
         this.ranges.add(range);
         this.isEmpty = false;
      }

      public void closeRange(Label end) {
         ((CompileStack.LabelRange)this.ranges.getLast()).end = end;
      }
   }

   protected static class LabelRange {
      public Label start;
      public Label end;
   }
}
