package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.SootClass;
import soot.SootMethod;
import soot.dava.DavaBody;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.util.Chain;

public class RemoveEmptyBodyDefaultConstructor {
   public static boolean DEBUG = false;

   public static void checkAndRemoveDefault(SootClass s) {
      debug("\n\nRemoveEmptyBodyDefaultConstructor----" + s.getName());
      List methods = s.getMethods();
      Iterator it = methods.iterator();
      ArrayList constructors = new ArrayList();

      SootMethod constructor;
      while(it.hasNext()) {
         constructor = (SootMethod)it.next();
         debug("method name is" + constructor.getName());
         if (constructor.getName().indexOf("<init>") > -1) {
            constructors.add(constructor);
         }
      }

      if (constructors.size() != 1) {
         debug("class has more than one constructors cant do anything");
      } else {
         constructor = (SootMethod)constructors.get(0);
         if (constructor.getParameterCount() != 0) {
            debug("constructor is not the default constructor");
         } else {
            debug("Check that the body is empty....and call to super contains no arguments and delete");
            if (!constructor.hasActiveBody()) {
               debug("No active body found for the default constructor");
            } else {
               Body body = constructor.getActiveBody();
               Chain units = ((DavaBody)body).getUnits();
               if (units.size() != 1) {
                  debug(" DavaBody AST does not have single root");
               } else {
                  ASTNode AST = (ASTNode)units.getFirst();
                  if (!(AST instanceof ASTMethodNode)) {
                     throw new RuntimeException("Starting node of DavaBody AST is not an ASTMethodNode");
                  } else {
                     ASTMethodNode methodNode = (ASTMethodNode)AST;
                     debug("got methodnode check body is empty and super has nothing in it");
                     List<Object> subBodies = methodNode.get_SubBodies();
                     if (subBodies.size() != 1) {
                        debug("Method node does not have one subBody!!!");
                     } else {
                        List methodBody = (List)subBodies.get(0);
                        if (methodBody.size() != 0) {
                           debug("Method body size is greater than 1 so cant do nothing");
                        } else {
                           debug("Method body is empty...check super call is empty");
                           if (((DavaBody)body).get_ConstructorExpr().getArgCount() != 0) {
                              debug("call to super not empty");
                           } else {
                              debug("REMOVE METHOD");
                              s.removeMethod(constructor);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public static void debug(String debug) {
      if (DEBUG) {
         System.out.println("DEBUG: " + debug);
      }

   }
}
