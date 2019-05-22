package soot.dava.toolkits.base.AST.interProcedural;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.PhaseOptions;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.dava.DavaBody;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.toolkits.base.AST.transformations.CPApplication;
import soot.dava.toolkits.base.AST.transformations.EliminateConditions;
import soot.dava.toolkits.base.AST.transformations.LocalVariableCleaner;
import soot.dava.toolkits.base.AST.transformations.SimplifyConditions;
import soot.dava.toolkits.base.AST.transformations.SimplifyExpressions;
import soot.dava.toolkits.base.AST.transformations.UnreachableCodeEliminator;
import soot.dava.toolkits.base.AST.transformations.UselessLabelFinder;
import soot.dava.toolkits.base.AST.transformations.VoidReturnRemover;
import soot.dava.toolkits.base.renamer.Renamer;
import soot.dava.toolkits.base.renamer.infoGatheringAnalysis;
import soot.util.Chain;

public class InterProceduralAnalyses {
   public static boolean DEBUG = false;

   public static void applyInterProceduralAnalyses() {
      Chain classes = Scene.v().getApplicationClasses();
      if (DEBUG) {
         System.out.println("\n\nInvoking redundantFielduseEliminator");
      }

      ConstantFieldValueFinder finder = new ConstantFieldValueFinder(classes);
      HashMap<String, Object> constantValueFields = finder.getFieldsWithConstantValues();
      if (DEBUG) {
         finder.printConstantValueFields();
      }

      Iterator it = classes.iterator();

      while(it.hasNext()) {
         SootClass s = (SootClass)it.next();
         Iterator methodIt = s.methodIterator();

         while(methodIt.hasNext()) {
            SootMethod m = (SootMethod)methodIt.next();
            DavaBody body = null;
            if (m.hasActiveBody()) {
               body = (DavaBody)m.getActiveBody();
               ASTNode AST = (ASTNode)body.getUnits().getFirst();
               if (AST instanceof ASTMethodNode) {
                  Map options = PhaseOptions.v().getPhaseOptions("db.deobfuscate");
                  boolean deobfuscate = PhaseOptions.getBoolean(options, "enabled");
                  if (deobfuscate) {
                     if (DEBUG) {
                        System.out.println("\nSTART CP Class:" + s.getName() + " Method: " + m.getName());
                     }

                     CPApplication CPApp = new CPApplication((ASTMethodNode)AST, constantValueFields, finder.getClassNameFieldNameToSootFieldMapping());
                     AST.apply(CPApp);
                     if (DEBUG) {
                        System.out.println("DONE CP for " + m.getName());
                     }
                  }

                  AST.apply(new SimplifyExpressions());
                  AST.apply(new SimplifyConditions());
                  AST.apply(new EliminateConditions((ASTMethodNode)AST));
                  AST.apply(new UnreachableCodeEliminator(AST));
                  AST.apply(new LocalVariableCleaner(AST));
                  if (deobfuscate) {
                     if (DEBUG) {
                        System.out.println("reinvoking analyzeAST");
                     }

                     UselessLabelFinder.DEBUG = false;
                     body.analyzeAST();
                  }

                  options = PhaseOptions.v().getPhaseOptions("db.renamer");
                  boolean renamer = PhaseOptions.getBoolean(options, "enabled");
                  if (renamer) {
                     applyRenamerAnalyses(AST, body);
                  }

                  VoidReturnRemover.cleanClass(s);
               }
            }
         }
      }

   }

   private static void applyRenamerAnalyses(ASTNode AST, DavaBody body) {
      infoGatheringAnalysis info = new infoGatheringAnalysis(body);
      AST.apply(info);
      Renamer renamer = new Renamer(info.getHeuristicSet(), (ASTMethodNode)AST);
      renamer.rename();
   }
}
