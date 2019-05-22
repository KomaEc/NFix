package soot.dava.toolkits.base.AST.structuredAnalysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.PrimType;
import soot.ShortType;
import soot.SootField;
import soot.Type;
import soot.Value;
import soot.dava.DavaFlowAnalysisException;
import soot.dava.internal.AST.ASTBinaryCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTUnaryBinaryCondition;
import soot.dava.internal.AST.ASTUnaryCondition;
import soot.dava.internal.javaRep.DNotExpr;
import soot.dava.toolkits.base.AST.interProcedural.ConstantFieldValueFinder;
import soot.jimple.BinopExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.Stmt;

public class CP extends StructuredAnalysis {
   ArrayList<CPTuple> constantFieldTuples = null;
   ArrayList<CPTuple> formals = null;
   ArrayList<CPTuple> locals = null;
   ArrayList<CPTuple> initialInput = null;
   ASTMethodNode methodNode = null;
   String localClassName = null;

   public CP(ASTMethodNode analyze, HashMap<String, Object> constantFields, HashMap<String, SootField> classNameFieldNameToSootFieldMapping) {
      this.methodNode = analyze;
      this.localClassName = analyze.getDavaBody().getMethod().getDeclaringClass().getName();
      this.createConstantFieldsList(constantFields, classNameFieldNameToSootFieldMapping);
      this.createInitialInput();
      CPFlowSet initialSet = new CPFlowSet();
      Iterator it = this.initialInput.iterator();

      while(it.hasNext()) {
         initialSet.add(it.next());
      }

      CPFlowSet result = (CPFlowSet)this.process(analyze, initialSet);
   }

   public void createInitialInput() {
      this.initialInput = new ArrayList();
      this.initialInput.addAll(this.constantFieldTuples);
      this.formals = new ArrayList();
      Collection col = this.methodNode.getDavaBody().get_ParamMap().values();
      Iterator it = col.iterator();

      while(it.hasNext()) {
         Object temp = it.next();
         if (temp instanceof Local) {
            Local tempLocal = (Local)temp;
            if (tempLocal.getType() instanceof PrimType) {
               CPVariable newVar = new CPVariable(tempLocal);
               CPTuple newTuple = new CPTuple(this.localClassName, newVar, true);
               this.initialInput.add(newTuple);
               this.formals.add(newTuple);
            }
         }
      }

      List decLocals = this.methodNode.getDeclaredLocals();
      it = decLocals.iterator();
      this.locals = new ArrayList();

      while(it.hasNext()) {
         Object temp = it.next();
         if (temp instanceof Local) {
            Local tempLocal = (Local)temp;
            Type localType = tempLocal.getType();
            if (localType instanceof PrimType) {
               CPVariable newVar = new CPVariable(tempLocal);
               Object value;
               if (localType instanceof BooleanType) {
                  value = new Boolean(false);
               } else if (localType instanceof ByteType) {
                  value = new Integer(0);
               } else if (localType instanceof CharType) {
                  value = new Integer(0);
               } else if (localType instanceof DoubleType) {
                  value = new Double(0.0D);
               } else if (localType instanceof FloatType) {
                  value = new Float(0.0F);
               } else if (localType instanceof IntType) {
                  value = new Integer(0);
               } else if (localType instanceof LongType) {
                  value = new Long(0L);
               } else {
                  if (!(localType instanceof ShortType)) {
                     throw new DavaFlowAnalysisException("Unknown PrimType");
                  }

                  value = new Integer(0);
               }

               CPTuple newTuple = new CPTuple(this.localClassName, newVar, value);
               this.locals.add(newTuple);
            }
         }
      }

   }

   private void createConstantFieldsList(HashMap<String, Object> constantFields, HashMap<String, SootField> classNameFieldNameToSootFieldMapping) {
      this.constantFieldTuples = new ArrayList();
      Iterator it = constantFields.keySet().iterator();

      while(it.hasNext()) {
         String combined = (String)it.next();
         int temp = combined.indexOf(ConstantFieldValueFinder.combiner, 0);
         if (temp <= 0) {
            throw new DavaFlowAnalysisException("Second argument of VariableValuePair not a variable");
         }

         String className = combined.substring(0, temp);
         SootField field = (SootField)classNameFieldNameToSootFieldMapping.get(combined);
         if (field.getType() instanceof PrimType) {
            Object value = constantFields.get(combined);
            CPVariable var = new CPVariable(field);
            CPTuple newTuples = new CPTuple(className, var, value);
            this.constantFieldTuples.add(newTuples);
         }
      }

   }

   public DavaFlowSet emptyFlowSet() {
      return new CPFlowSet();
   }

   public void setMergeType() {
      this.MERGETYPE = 2;
   }

   public DavaFlowSet newInitialFlow() {
      CPFlowSet flowSet = new CPFlowSet();
      ArrayList<CPTuple> localsAndFormals = new ArrayList();
      localsAndFormals.addAll(this.formals);
      localsAndFormals.addAll(this.locals);

      Iterator it;
      CPTuple tempTuple;
      for(it = localsAndFormals.iterator(); it.hasNext(); flowSet.add(tempTuple)) {
         tempTuple = ((CPTuple)it.next()).clone();
         if (!tempTuple.isTop()) {
            tempTuple.setTop();
         }
      }

      it = this.constantFieldTuples.iterator();

      while(it.hasNext()) {
         flowSet.add(it.next());
      }

      return flowSet;
   }

   public DavaFlowSet cloneFlowSet(DavaFlowSet flowSet) {
      if (flowSet instanceof CPFlowSet) {
         return ((CPFlowSet)flowSet).clone();
      } else {
         throw new RuntimeException("cloneFlowSet not implemented for other flowSet types" + flowSet.toString());
      }
   }

   public DavaFlowSet processUnaryBinaryCondition(ASTUnaryBinaryCondition cond, DavaFlowSet input) {
      if (!(input instanceof CPFlowSet)) {
         throw new RuntimeException("processCondition is not implemented for other flowSet types" + input.toString());
      } else {
         CPFlowSet inSet = (CPFlowSet)input;
         return inSet;
      }
   }

   public DavaFlowSet processSynchronizedLocal(Local local, DavaFlowSet input) {
      if (!(input instanceof CPFlowSet)) {
         throw new RuntimeException("processSynchronized  is not implemented for other flowSet types" + input.toString());
      } else {
         return input;
      }
   }

   public DavaFlowSet processSwitchKey(Value key, DavaFlowSet input) {
      if (!(input instanceof CPFlowSet)) {
         throw new RuntimeException("processCondition is not implemented for other flowSet types" + input.toString());
      } else {
         CPFlowSet inSet = (CPFlowSet)input;
         return inSet;
      }
   }

   public DavaFlowSet processStatement(Stmt s, DavaFlowSet input) {
      if (!(input instanceof CPFlowSet)) {
         throw new RuntimeException("processStatement is not implemented for other flowSet types");
      } else {
         CPFlowSet inSet = (CPFlowSet)input;
         if (inSet == this.NOPATH) {
            return inSet;
         } else if (!(s instanceof DefinitionStmt)) {
            return inSet;
         } else {
            DefinitionStmt defStmt = (DefinitionStmt)s;
            Value left = defStmt.getLeftOp();
            if (left instanceof Local && ((Local)left).getType() instanceof PrimType) {
               CPFlowSet toReturn = (CPFlowSet)this.cloneFlowSet(inSet);
               Object killedValue = this.killButGetValueForUse((Local)left, toReturn);
               Value right = defStmt.getRightOp();
               Object value = CPHelper.isAConstantValue(right);
               if (value != null) {
                  if (left.getType() instanceof BooleanType) {
                     Integer tempValue = (Integer)value;
                     if (tempValue == 0) {
                        value = new Boolean(false);
                     } else {
                        value = new Boolean(true);
                     }
                  }

                  this.addOrUpdate(toReturn, (Local)left, value);
               } else {
                  this.handleMathematical(toReturn, (Local)left, right, killedValue);
               }

               return toReturn;
            } else {
               return inSet;
            }
         }
      }
   }

   public Object killButGetValueForUse(Local left, CPFlowSet toReturn) {
      Iterator var3 = toReturn.iterator();

      CPTuple tempTuple;
      while(var3.hasNext()) {
         tempTuple = (CPTuple)var3.next();
         if (tempTuple.getSootClassName().equals(this.localClassName) && tempTuple.containsLocal()) {
            Local tempLocal = tempTuple.getVariable().getLocal();
            if (left.getName().equals(tempLocal.getName())) {
               Object killedValue = tempTuple.getValue();
               tempTuple.setTop();
               return killedValue;
            }
         }
      }

      CPVariable newVar = new CPVariable(left);
      tempTuple = new CPTuple(this.localClassName, newVar, false);
      toReturn.add(tempTuple);
      return null;
   }

   private void addOrUpdate(CPFlowSet toReturn, Local left, Object val) {
      CPVariable newVar = new CPVariable(left);
      CPTuple newTuple = new CPTuple(this.localClassName, newVar, val);
      toReturn.addIfNotPresent(newTuple);
   }

   private void handleMathematical(CPFlowSet toReturn, Local left, Value right, Object killedValue) {
      Object value = this.isANotTopConstantInInputSet(toReturn, right);
      if (value != null) {
         Object toSend = CPHelper.wrapperClassCloner(value);
         if (toSend != null) {
            this.addOrUpdate(toReturn, left, toSend);
         }

      } else {
         if (right instanceof BinopExpr) {
            Value op1 = ((BinopExpr)right).getOp1();
            Value op2 = ((BinopExpr)right).getOp2();
            Object op1Val = CPHelper.isAConstantValue(op1);
            Object op2Val = CPHelper.isAConstantValue(op2);
            if (op1Val == null) {
               op1Val = this.isANotTopConstantInInputSet(toReturn, op1);
            }

            if (op2Val == null) {
               op2Val = this.isANotTopConstantInInputSet(toReturn, op2);
            }

            if (op1 == left) {
               op1Val = killedValue;
            }

            if (op2 == left) {
               op2Val = killedValue;
            }

            if (op1Val != null && op2Val != null && left.getType() instanceof IntType && op1Val instanceof Integer && op2Val instanceof Integer) {
               int op1IntValue = (Integer)op1Val;
               int op2IntValue = (Integer)op2Val;
               String tempStr = ((BinopExpr)right).getSymbol();
               if (tempStr.length() > 1) {
                  char symbol = tempStr.charAt(1);
                  int newValue = 0;
                  boolean set = false;
                  switch(symbol) {
                  case '*':
                     newValue = op1IntValue * op2IntValue;
                     set = true;
                     break;
                  case '+':
                     newValue = op1IntValue + op2IntValue;
                     set = true;
                  case ',':
                  default:
                     break;
                  case '-':
                     newValue = op1IntValue - op2IntValue;
                     set = true;
                  }

                  if (set) {
                     Integer newValueObject = new Integer(newValue);
                     this.addOrUpdate(toReturn, left, newValueObject);
                     return;
                  }
               }
            }
         }

      }
   }

   private Object isANotTopConstantInInputSet(CPFlowSet set, Value toCheck) {
      if (toCheck instanceof Local || toCheck instanceof FieldRef) {
         String toCheckClassName = null;
         if (toCheck instanceof Local) {
            toCheckClassName = this.localClassName;
         } else {
            toCheckClassName = ((FieldRef)toCheck).getField().getDeclaringClass().getName();
         }

         Iterator var4 = set.iterator();

         CPTuple tempTuple;
         boolean tupleFound;
         do {
            do {
               if (!var4.hasNext()) {
                  return null;
               }

               tempTuple = (CPTuple)var4.next();
            } while(!tempTuple.getSootClassName().equals(toCheckClassName));

            tupleFound = false;
            if (tempTuple.containsLocal() && toCheck instanceof Local) {
               Local tempLocal = tempTuple.getVariable().getLocal();
               if (tempLocal.getName().equals(((Local)toCheck).getName())) {
                  tupleFound = true;
               }
            } else if (tempTuple.containsField() && toCheck instanceof FieldRef) {
               SootField toCheckField = ((FieldRef)toCheck).getField();
               SootField tempField = tempTuple.getVariable().getSootField();
               if (tempField.getName().equals(toCheckField.getName())) {
                  tupleFound = true;
               }
            }
         } while(!tupleFound);

         if (tempTuple.isTop()) {
            return null;
         } else {
            return tempTuple.getValue();
         }
      } else {
         return null;
      }
   }

   public DavaFlowSet processASTIfNode(ASTIfNode node, DavaFlowSet input) {
      if (DEBUG_IF) {
         System.out.println("Processing if node using over-ridden process if method" + input.toString());
      }

      input = this.processCondition(node.get_Condition(), input);
      if (!(input instanceof CPFlowSet)) {
         throw new DavaFlowAnalysisException("not a flow set");
      } else {
         CPFlowSet inputToBody = ((CPFlowSet)input).clone();
         CPTuple tuple = this.checkForValueHints(node.get_Condition(), inputToBody, false);
         if (tuple != null) {
            inputToBody.addIfNotPresentButDontUpdate(tuple);
         }

         DavaFlowSet output1 = this.processSingleSubBodyNode(node, inputToBody);
         if (DEBUG_IF) {
            System.out.println("\n\nINPUTS TO MERGE ARE input (original):" + input.toString() + "processingBody output:" + output1.toString() + "\n\n\n");
         }

         DavaFlowSet output2 = this.merge(input, output1);
         String label = this.getLabel(node);
         DavaFlowSet temp = this.handleBreak(label, output2, node);
         if (DEBUG_IF) {
            System.out.println("Exiting if node" + temp.toString());
         }

         return temp;
      }
   }

   public DavaFlowSet processASTIfElseNode(ASTIfElseNode node, DavaFlowSet input) {
      if (DEBUG_IF) {
         System.out.println("Processing IF-ELSE node using over-ridden process if method" + input.toString());
      }

      if (!(input instanceof CPFlowSet)) {
         throw new DavaFlowAnalysisException("not a flow set");
      } else {
         List<Object> subBodies = node.get_SubBodies();
         if (subBodies.size() != 2) {
            throw new RuntimeException("processASTIfElseNode called with a node without two subBodies");
         } else {
            List subBodyOne = (List)subBodies.get(0);
            List subBodyTwo = (List)subBodies.get(1);
            input = this.processCondition(node.get_Condition(), input);
            DavaFlowSet clonedInput = this.cloneFlowSet(input);
            CPTuple tuple = this.checkForValueHints(node.get_Condition(), (CPFlowSet)clonedInput, false);
            if (tuple != null) {
               ((CPFlowSet)clonedInput).addIfNotPresentButDontUpdate(tuple);
            }

            DavaFlowSet output1 = this.process(subBodyOne, clonedInput);
            clonedInput = this.cloneFlowSet(input);
            CPTuple tuple1 = this.checkForValueHints(node.get_Condition(), (CPFlowSet)clonedInput, true);
            if (tuple1 != null) {
               ((CPFlowSet)clonedInput).addIfNotPresentButDontUpdate(tuple1);
            }

            DavaFlowSet output2 = this.process(subBodyTwo, clonedInput);
            if (DEBUG_IF) {
               System.out.println("\n\n  IF-ELSE   INPUTS TO MERGE ARE input (if):" + output1.toString() + " else:" + output2.toString() + "\n\n\n");
            }

            DavaFlowSet temp = this.merge(output1, output2);
            String label = this.getLabel(node);
            output1 = this.handleBreak(label, temp, node);
            if (DEBUG_IF) {
               System.out.println("Exiting ifelse node" + output1.toString());
            }

            return output1;
         }
      }
   }

   public CPTuple checkForValueHints(ASTCondition cond, CPFlowSet input, boolean isElseBranch) {
      if (cond instanceof ASTUnaryCondition) {
         ASTUnaryCondition unary = (ASTUnaryCondition)cond;
         Value unaryValue = unary.getValue();
         boolean NOTTED = false;
         if (unaryValue instanceof DNotExpr) {
            unaryValue = ((DNotExpr)unaryValue).getOp();
            NOTTED = true;
         }

         if (!(unaryValue instanceof Local)) {
            return null;
         } else {
            CPVariable variable = new CPVariable((Local)unaryValue);
            Boolean boolVal;
            if (!isElseBranch) {
               boolVal = new Boolean(!NOTTED);
               return new CPTuple(this.localClassName, variable, boolVal);
            } else {
               boolVal = new Boolean(NOTTED);
               return new CPTuple(this.localClassName, variable, boolVal);
            }
         }
      } else if (cond instanceof ASTBinaryCondition) {
         ASTBinaryCondition binary = (ASTBinaryCondition)cond;
         ConditionExpr expr = binary.getConditionExpr();
         Boolean equal = null;
         String symbol = expr.getSymbol();
         if (symbol.indexOf("==") > -1) {
            equal = new Boolean(true);
         } else {
            if (symbol.indexOf("!=") <= -1) {
               return null;
            }

            equal = new Boolean(false);
         }

         Value a = expr.getOp1();
         Value b = expr.getOp2();
         CPTuple tuple = this.createCPTupleIfPossible(a, b, input);
         if (tuple == null) {
            return null;
         } else if (equal) {
            return !isElseBranch ? tuple : null;
         } else {
            return isElseBranch ? tuple : null;
         }
      } else {
         return null;
      }
   }

   public CPTuple createCPTupleIfPossible(Value a, Value b, CPFlowSet input) {
      Object aVal = CPHelper.isAConstantValue(a);
      Object bVal = CPHelper.isAConstantValue(b);
      if (aVal != null && bVal != null) {
         return null;
      } else {
         CPVariable cpVar = null;
         Object constantToUse = null;
         if (aVal == null && bVal == null) {
            Object av1 = this.isANotTopConstantInInputSet(input, a);
            Object av2 = this.isANotTopConstantInInputSet(input, b);
            if (av1 == null && av2 == null) {
               return null;
            }

            if (av1 == null && av2 != null) {
               if (!(a instanceof Local) || !(((Local)a).getType() instanceof PrimType)) {
                  return null;
               }

               cpVar = new CPVariable((Local)a);
               constantToUse = av2;
            } else if (av1 != null && av2 == null) {
               if (!(b instanceof Local) || !(((Local)b).getType() instanceof PrimType)) {
                  return null;
               }

               cpVar = new CPVariable((Local)b);
               constantToUse = av1;
            }
         } else if (aVal != null && bVal == null) {
            if (!(b instanceof Local) || !(((Local)b).getType() instanceof PrimType)) {
               return null;
            }

            cpVar = new CPVariable((Local)b);
            constantToUse = aVal;
         } else if (aVal == null && bVal != null) {
            if (!(a instanceof Local) || !(((Local)a).getType() instanceof PrimType)) {
               return null;
            }

            cpVar = new CPVariable((Local)a);
            constantToUse = bVal;
         }

         if (cpVar != null && constantToUse != null) {
            if (cpVar.getLocal().getType() instanceof BooleanType) {
               if (!(constantToUse instanceof Integer)) {
                  return null;
               }

               Integer tempValue = (Integer)constantToUse;
               if (tempValue == 0) {
                  constantToUse = new Boolean(false);
               } else {
                  constantToUse = new Boolean(true);
               }
            }

            return new CPTuple(this.localClassName, cpVar, constantToUse);
         } else {
            return null;
         }
      }
   }
}
