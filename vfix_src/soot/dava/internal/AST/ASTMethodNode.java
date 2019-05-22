package soot.dava.internal.AST;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.Local;
import soot.Type;
import soot.UnitPrinter;
import soot.Value;
import soot.dava.DavaBody;
import soot.dava.DavaUnitPrinter;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DVariableDeclarationStmt;
import soot.dava.toolkits.base.AST.ASTAnalysis;
import soot.dava.toolkits.base.AST.analysis.Analysis;
import soot.dava.toolkits.base.renamer.RemoveFullyQualifiedName;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.Stmt;
import soot.util.DeterministicHashMap;
import soot.util.IterableSet;

public class ASTMethodNode extends ASTNode {
   private List<Object> body;
   private DavaBody davaBody;
   private ASTStatementSequenceNode declarations;
   private List<Local> dontPrintLocals = new ArrayList();

   public ASTStatementSequenceNode getDeclarations() {
      return this.declarations;
   }

   public void setDeclarations(ASTStatementSequenceNode decl) {
      this.declarations = decl;
   }

   public void setDavaBody(DavaBody bod) {
      this.davaBody = bod;
   }

   public DavaBody getDavaBody() {
      return this.davaBody;
   }

   public void storeLocals(Body OrigBody) {
      if (!(OrigBody instanceof DavaBody)) {
         throw new RuntimeException("Only DavaBodies should invoke this method");
      } else {
         this.davaBody = (DavaBody)OrigBody;
         Map<Type, List<Local>> typeToLocals = new DeterministicHashMap(OrigBody.getLocalCount() * 2 + 1, 0.7F);
         HashSet params = new HashSet();
         params.addAll(this.davaBody.get_ParamMap().values());
         params.addAll(this.davaBody.get_CaughtRefs());
         HashSet<Object> thisLocals = this.davaBody.get_ThisLocals();
         Iterator localIt = OrigBody.getLocals().iterator();

         while(localIt.hasNext()) {
            Local local = (Local)localIt.next();
            if (!params.contains(local) && !thisLocals.contains(local)) {
               Type t = local.getType();
               String typeName = t.toString();
               Object localList;
               if (typeToLocals.containsKey(t)) {
                  localList = (List)typeToLocals.get(t);
               } else {
                  localList = new ArrayList();
                  typeToLocals.put(t, localList);
               }

               ((List)localList).add(local);
            }
         }

         List<AugmentedStmt> statementSequence = new ArrayList();
         Iterator typeIt = typeToLocals.keySet().iterator();

         while(typeIt.hasNext()) {
            Type typeObject = (Type)typeIt.next();
            String type = typeObject.toString();
            DVariableDeclarationStmt varStmt = null;
            varStmt = new DVariableDeclarationStmt(typeObject, this.davaBody);
            List<Local> localList = (List)typeToLocals.get(typeObject);
            Iterator var12 = localList.iterator();

            while(var12.hasNext()) {
               Local element = (Local)var12.next();
               varStmt.addLocal(element);
            }

            AugmentedStmt as = new AugmentedStmt(varStmt);
            statementSequence.add(as);
         }

         this.declarations = new ASTStatementSequenceNode(statementSequence);
         this.body.add(0, this.declarations);
         this.subBodies = new ArrayList();
         this.subBodies.add(this.body);
      }
   }

   public ASTMethodNode(List<Object> body) {
      this.body = body;
      this.subBodies.add(body);
   }

   public List getDeclaredLocals() {
      List toReturn = new ArrayList();
      Iterator var2 = this.declarations.getStatements().iterator();

      while(true) {
         Stmt s;
         do {
            if (!var2.hasNext()) {
               return toReturn;
            }

            AugmentedStmt as = (AugmentedStmt)var2.next();
            s = as.get_Stmt();
         } while(!(s instanceof DVariableDeclarationStmt));

         DVariableDeclarationStmt varStmt = (DVariableDeclarationStmt)s;
         List declarations = varStmt.getDeclarations();
         Iterator decIt = declarations.iterator();

         while(decIt.hasNext()) {
            toReturn.add(decIt.next());
         }
      }
   }

   public void removeDeclaredLocal(Local local) {
      Stmt s = null;
      Iterator var3 = this.declarations.getStatements().iterator();

      while(var3.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var3.next();
         s = as.get_Stmt();
         if (s instanceof DVariableDeclarationStmt) {
            DVariableDeclarationStmt varStmt = (DVariableDeclarationStmt)s;
            List declarations = varStmt.getDeclarations();
            Iterator decIt = declarations.iterator();
            boolean foundIt = false;

            while(decIt.hasNext()) {
               Local temp = (Local)decIt.next();
               if (temp.getName().compareTo(local.getName()) == 0) {
                  foundIt = true;
                  break;
               }
            }

            if (foundIt) {
               varStmt.removeLocal(local);
               break;
            }
         }
      }

      List<AugmentedStmt> newSequence = new ArrayList();
      Iterator var11 = this.declarations.getStatements().iterator();

      while(var11.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var11.next();
         s = as.get_Stmt();
         if (s instanceof DVariableDeclarationStmt) {
            DVariableDeclarationStmt varStmt = (DVariableDeclarationStmt)s;
            if (varStmt.getDeclarations().size() != 0) {
               newSequence.add(as);
            }
         }
      }

      this.declarations.setStatements(newSequence);
   }

   public void replaceBody(List<Object> body) {
      this.body = body;
      this.subBodies = new ArrayList();
      this.subBodies.add(body);
   }

   public Object clone() {
      ASTMethodNode toReturn = new ASTMethodNode(this.body);
      toReturn.setDeclarations((ASTStatementSequenceNode)this.declarations.clone());
      toReturn.setDontPrintLocals(this.dontPrintLocals);
      return toReturn;
   }

   public void setDontPrintLocals(List<Local> list) {
      this.dontPrintLocals = list;
   }

   public void addToDontPrintLocalsList(Local toAdd) {
      this.dontPrintLocals.add(toAdd);
   }

   public void perform_Analysis(ASTAnalysis a) {
      this.perform_AnalysisOnSubBodies(a);
   }

   public void toString(UnitPrinter up) {
      if (!(up instanceof DavaUnitPrinter)) {
         throw new RuntimeException("Only DavaUnitPrinter should be used to print DavaBody");
      } else {
         DavaUnitPrinter dup = (DavaUnitPrinter)up;
         if (this.davaBody != null) {
            InstanceInvokeExpr constructorExpr = this.davaBody.get_ConstructorExpr();
            if (constructorExpr != null) {
               boolean printCloseBrace = true;
               if (this.davaBody.getMethod().getDeclaringClass().getName().equals(constructorExpr.getMethodRef().declaringClass().toString())) {
                  dup.printString("        this(");
               } else if (constructorExpr.getArgCount() > 0) {
                  dup.printString("        super(");
               } else {
                  printCloseBrace = false;
               }

               Iterator ait = constructorExpr.getArgs().iterator();

               while(ait.hasNext()) {
                  Object arg = ait.next();
                  if (arg instanceof Value) {
                     dup.noIndent();
                     ((Value)arg).toString(dup);
                  } else {
                     dup.printString(arg.toString());
                  }

                  if (ait.hasNext()) {
                     dup.printString(", ");
                  }
               }

               if (printCloseBrace) {
                  dup.printString(");\n");
               }
            }

            up.newline();
         }

         this.printDeclarationsFollowedByBody(up, this.body);
      }
   }

   public void printDeclarationsFollowedByBody(UnitPrinter up, List<Object> body) {
      Iterator var3 = this.declarations.getStatements().iterator();

      while(true) {
         Stmt u;
         DVariableDeclarationStmt declStmt;
         List localDeclarations;
         boolean shouldContinue;
         do {
            label72:
            do {
               while(var3.hasNext()) {
                  AugmentedStmt as = (AugmentedStmt)var3.next();
                  u = as.get_Stmt();
                  if (u instanceof DVariableDeclarationStmt) {
                     declStmt = (DVariableDeclarationStmt)u;
                     localDeclarations = declStmt.getDeclarations();
                     shouldContinue = false;
                     Iterator declsIt = localDeclarations.iterator();

                     while(declsIt.hasNext()) {
                        if (!this.dontPrintLocals.contains(declsIt.next())) {
                           shouldContinue = true;
                           continue label72;
                        }
                     }
                     continue label72;
                  }

                  up.startUnit(u);
                  u.toString(up);
                  up.literal(";");
                  up.endUnit(u);
                  up.newline();
               }

               boolean printed = false;
               if (body.size() > 0) {
                  ASTNode firstNode = (ASTNode)body.get(0);
                  if (firstNode instanceof ASTStatementSequenceNode) {
                     List<AugmentedStmt> tempstmts = ((ASTStatementSequenceNode)firstNode).getStatements();
                     if (tempstmts.size() != 0) {
                        AugmentedStmt tempas = (AugmentedStmt)tempstmts.get(0);
                        Stmt temps = tempas.get_Stmt();
                        if (temps instanceof DVariableDeclarationStmt) {
                           printed = true;
                           this.body_toString(up, body.subList(1, body.size()));
                        }
                     }
                  }
               }

               if (!printed) {
                  this.body_toString(up, body);
               }

               return;
            } while(!shouldContinue);
         } while(localDeclarations.size() == 0);

         if (!(up instanceof DavaUnitPrinter)) {
            throw new RuntimeException("DavaBody should always be printed using the DavaUnitPrinter");
         }

         DavaUnitPrinter dup = (DavaUnitPrinter)up;
         dup.startUnit(u);
         String type = declStmt.getType().toString();
         if (type.equals("null_type")) {
            dup.printString("Object");
         } else {
            IterableSet importSet = this.davaBody.getImportList();
            if (!importSet.contains(type)) {
               this.davaBody.addToImportList(type);
            }

            type = RemoveFullyQualifiedName.getReducedName(this.davaBody.getImportList(), type, declStmt.getType());
            dup.printString(type);
         }

         dup.printString(" ");
         int number = 0;
         Iterator decIt = localDeclarations.iterator();

         while(decIt.hasNext()) {
            Local tempDec = (Local)decIt.next();
            if (!this.dontPrintLocals.contains(tempDec)) {
               if (number != 0) {
                  dup.printString(", ");
               }

               ++number;
               dup.printString(tempDec.getName());
            }
         }

         up.literal(";");
         up.endUnit(u);
         up.newline();
      }
   }

   public String toString() {
      StringBuffer b = new StringBuffer();
      if (this.davaBody != null) {
         InstanceInvokeExpr constructorExpr = this.davaBody.get_ConstructorExpr();
         if (constructorExpr != null) {
            if (this.davaBody.getMethod().getDeclaringClass().getName().equals(constructorExpr.getMethodRef().declaringClass().toString())) {
               b.append("        this(");
            } else {
               b.append("        super(");
            }

            boolean isFirst = true;

            for(Iterator var4 = constructorExpr.getArgs().iterator(); var4.hasNext(); isFirst = false) {
               Value val = (Value)var4.next();
               if (!isFirst) {
                  b.append(", ");
               }

               b.append(val.toString());
            }

            b.append(");\n\n");
         }
      }

      b.append(this.body_toString(this.body));
      return b.toString();
   }

   public void apply(Analysis a) {
      a.caseASTMethodNode(this);
   }
}
