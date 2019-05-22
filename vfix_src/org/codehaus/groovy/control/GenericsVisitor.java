package org.codehaus.groovy.control;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;

public class GenericsVisitor extends ClassCodeVisitorSupport {
   private SourceUnit source;

   public GenericsVisitor(SourceUnit source) {
      this.source = source;
   }

   protected SourceUnit getSourceUnit() {
      return this.source;
   }

   public void visitClass(ClassNode node) {
      boolean error = this.checkWildcard(node);
      if (!error) {
         this.checkGenericsUsage(node.getUnresolvedSuperClass(false), node.getSuperClass());
         ClassNode[] interfaces = node.getInterfaces();

         for(int i = 0; i < interfaces.length; ++i) {
            this.checkGenericsUsage(interfaces[i], interfaces[i].redirect());
         }

         node.visitContents(this);
      }
   }

   public void visitField(FieldNode node) {
      ClassNode type = node.getType();
      this.checkGenericsUsage(type, type.redirect());
   }

   public void visitMethod(MethodNode node) {
      Parameter[] parameters = node.getParameters();
      Parameter[] arr$ = parameters;
      int len$ = parameters.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Parameter param = arr$[i$];
         ClassNode paramType = param.getType();
         this.checkGenericsUsage(paramType, paramType.redirect());
      }

      ClassNode returnType = node.getReturnType();
      this.checkGenericsUsage(returnType, returnType.redirect());
   }

   private boolean checkWildcard(ClassNode cn) {
      ClassNode sn = cn.getUnresolvedSuperClass(false);
      if (sn == null) {
         return false;
      } else {
         GenericsType[] generics = sn.getGenericsTypes();
         if (generics == null) {
            return false;
         } else {
            boolean error = false;

            for(int i = 0; i < generics.length; ++i) {
               if (generics[i].isWildcard()) {
                  this.addError("A supertype may not specifiy a wildcard type", sn);
                  error = true;
               }
            }

            return error;
         }
      }
   }

   private void checkGenericsUsage(ClassNode n, ClassNode cn) {
      if (!n.isGenericsPlaceHolder()) {
         GenericsType[] nTypes = n.getGenericsTypes();
         GenericsType[] cnTypes = cn.getGenericsTypes();
         if (nTypes != null) {
            if (cnTypes == null) {
               this.addError("The class " + n.getName() + " refers to the class " + cn.getName() + " and uses " + nTypes.length + " parameters, but the referred class takes no parameters", n);
            } else if (nTypes.length != cnTypes.length) {
               this.addError("The class " + n.getName() + " refers to the class " + cn.getName() + " and uses " + nTypes.length + " parameters, but the refered class needs " + cnTypes.length, n);
            } else {
               for(int i = 0; i < nTypes.length; ++i) {
                  ClassNode nType = nTypes[i].getType();
                  ClassNode cnType = cnTypes[i].getType();
                  if (!nType.isDerivedFrom(cnType) && (!cnType.isInterface() || !nType.declaresInterface(cnType))) {
                     this.addError("The type " + nTypes[i].getName() + " is not a valid substitute for the bounded parameter <" + this.getPrintName(cnTypes[i]) + ">", n);
                  }
               }

            }
         }
      }
   }

   private String getPrintName(GenericsType gt) {
      String ret = gt.getName();
      ClassNode[] upperBounds = gt.getUpperBounds();
      ClassNode lowerBound = gt.getLowerBound();
      if (upperBounds != null) {
         ret = ret + " extends ";

         for(int i = 0; i < upperBounds.length; ++i) {
            ret = ret + this.getPrintName(upperBounds[i]);
            if (i + 1 < upperBounds.length) {
               ret = ret + " & ";
            }
         }
      } else if (lowerBound != null) {
         ret = ret + " super " + this.getPrintName(lowerBound);
      }

      return ret;
   }

   private String getPrintName(ClassNode cn) {
      String ret = cn.getName();
      GenericsType[] gts = cn.getGenericsTypes();
      if (gts != null) {
         ret = ret + "<";

         for(int i = 0; i < gts.length; ++i) {
            if (i != 0) {
               ret = ret + ",";
            }

            ret = ret + this.getPrintName(gts[i]);
         }

         ret = ret + ">";
      }

      return ret;
   }

   private void checkBounds(ClassNode[] given, ClassNode[] restrictions) {
      if (restrictions != null) {
         for(int i = 0; i < given.length; ++i) {
            for(int j = 0; j < restrictions.length; ++j) {
               if (!given[i].isDerivedFrom(restrictions[j])) {
               }
            }
         }

      }
   }
}
