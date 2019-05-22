package soot.dava.toolkits.base.renamer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import soot.ArrayType;
import soot.Local;
import soot.RefLikeType;
import soot.SootClass;
import soot.SootField;
import soot.Type;
import soot.dava.internal.AST.ASTMethodNode;
import soot.util.Chain;

public class Renamer {
   public final boolean DEBUG = false;
   heuristicSet heuristics;
   List locals;
   Chain fields;
   ASTMethodNode methodNode;
   List<String> forLoopNames;
   HashMap<Local, Boolean> changedOrNot;

   public Renamer(heuristicSet info, ASTMethodNode node) {
      this.heuristics = info;
      this.locals = null;
      this.methodNode = node;
      this.changedOrNot = new HashMap();
      Iterator localIt = info.getLocalsIterator();

      while(localIt.hasNext()) {
         this.changedOrNot.put(localIt.next(), new Boolean(false));
      }

      this.forLoopNames = new ArrayList();
      this.forLoopNames.add("i");
      this.forLoopNames.add("j");
      this.forLoopNames.add("k");
      this.forLoopNames.add("l");
   }

   public void rename() {
      this.debug("rename", "Renaming started");
      this.mainMethodArgument();
      this.forLoopIndexing();
      this.exceptionNaming();
      this.arraysGetTypeArray();
      this.assignedFromAField();
      this.newClassName();
      this.castedObject();
      this.objectsGetClassName();
      this.removeDollarSigns();
   }

   private void arraysGetTypeArray() {
      Iterator it = this.heuristics.getLocalsIterator();

      while(true) {
         Local tempLocal;
         Type type;
         do {
            do {
               if (!it.hasNext()) {
                  return;
               }

               tempLocal = (Local)it.next();
            } while(this.alreadyChanged(tempLocal));

            this.debug("arraysGetTypeArray", "checking " + tempLocal);
            type = tempLocal.getType();
         } while(!(type instanceof ArrayType));

         this.debug("arraysGetTypeArray", "Local:" + tempLocal + " is an Array Type: " + type.toString());
         String tempClassName = type.toString();
         if (tempClassName.indexOf(91) >= 0) {
            tempClassName = tempClassName.substring(0, tempClassName.indexOf(91));
         }

         if (tempClassName.indexOf(46) != -1) {
            tempClassName = tempClassName.substring(tempClassName.lastIndexOf(46) + 1);
         }

         String newName = tempClassName.toLowerCase();
         newName = newName + "Array";
         int count = 0;
         newName = newName + count;

         for(int count = count + 1; !this.isUniqueName(newName); ++count) {
            newName = newName.substring(0, newName.length() - 1) + count;
         }

         this.setName(tempLocal, newName);
      }
   }

   private void objectsGetClassName() {
      Iterator it = this.heuristics.getLocalsIterator();

      while(true) {
         Local tempLocal;
         Type type;
         do {
            do {
               do {
                  if (!it.hasNext()) {
                     return;
                  }

                  tempLocal = (Local)it.next();
               } while(this.alreadyChanged(tempLocal));

               this.debug("objectsGetClassName", "checking " + tempLocal);
               type = tempLocal.getType();
            } while(type instanceof ArrayType);
         } while(!(type instanceof RefLikeType));

         this.debug("objectsGetClassName", "Local:" + tempLocal + " Type: " + type.toString());
         String tempClassName = type.toString();
         if (tempClassName.indexOf(46) != -1) {
            tempClassName = tempClassName.substring(tempClassName.lastIndexOf(46) + 1);
         }

         String newName = tempClassName.toLowerCase();
         int count = 0;
         newName = newName + count;

         for(int count = count + 1; !this.isUniqueName(newName); ++count) {
            newName = newName.substring(0, newName.length() - 1) + count;
         }

         this.setName(tempLocal, newName);
      }
   }

   private void castedObject() {
      this.debug("castedObject", "");
      Iterator it = this.heuristics.getLocalsIterator();

      while(true) {
         Local tempLocal;
         String classNameToUse;
         String tempClassName;
         do {
            do {
               if (!it.hasNext()) {
                  return;
               }

               tempLocal = (Local)it.next();
            } while(this.alreadyChanged(tempLocal));

            this.debug("castedObject", "checking " + tempLocal);
            List<String> classes = this.heuristics.getCastStrings(tempLocal);
            Iterator<String> itClass = classes.iterator();
            classNameToUse = null;

            while(itClass.hasNext()) {
               tempClassName = (String)itClass.next();
               if (tempClassName.indexOf(46) != -1) {
                  tempClassName = tempClassName.substring(tempClassName.lastIndexOf(46) + 1);
               }

               if (classNameToUse == null) {
                  classNameToUse = tempClassName;
               } else if (!classNameToUse.equals(tempClassName)) {
                  classNameToUse = null;
                  break;
               }
            }
         } while(classNameToUse == null);

         this.debug("castedObject", "found a classNametoUse through cast expr");
         tempClassName = classNameToUse.toLowerCase();
         int count = 0;
         tempClassName = tempClassName + count;

         for(int count = count + 1; !this.isUniqueName(tempClassName); ++count) {
            tempClassName = tempClassName.substring(0, tempClassName.length() - 1) + count;
         }

         this.setName(tempLocal, tempClassName);
      }
   }

   private void newClassName() {
      this.debug("newClassName", "");
      Iterator it = this.heuristics.getLocalsIterator();

      while(true) {
         Local tempLocal;
         String classNameToUse;
         String tempClassName;
         do {
            do {
               if (!it.hasNext()) {
                  return;
               }

               tempLocal = (Local)it.next();
            } while(this.alreadyChanged(tempLocal));

            this.debug("newClassName", "checking " + tempLocal);
            List<String> classes = this.heuristics.getObjectClassName(tempLocal);
            Iterator<String> itClass = classes.iterator();
            classNameToUse = null;

            while(itClass.hasNext()) {
               tempClassName = (String)itClass.next();
               if (tempClassName.indexOf(46) != -1) {
                  tempClassName = tempClassName.substring(tempClassName.lastIndexOf(46) + 1);
               }

               if (classNameToUse == null) {
                  classNameToUse = tempClassName;
               } else if (!classNameToUse.equals(tempClassName)) {
                  classNameToUse = null;
                  break;
               }
            }
         } while(classNameToUse == null);

         this.debug("newClassName", "found a classNametoUse");
         tempClassName = classNameToUse.toLowerCase();
         int count = 0;
         tempClassName = tempClassName + count;

         for(int count = count + 1; !this.isUniqueName(tempClassName); ++count) {
            tempClassName = tempClassName.substring(0, tempClassName.length() - 1) + count;
         }

         this.setName(tempLocal, tempClassName);
      }
   }

   private void assignedFromAField() {
      Iterator it = this.heuristics.getLocalsIterator();

      while(true) {
         Local tempLocal;
         List fieldNames;
         do {
            do {
               do {
                  if (!it.hasNext()) {
                     return;
                  }

                  tempLocal = (Local)it.next();
               } while(this.alreadyChanged(tempLocal));

               this.debug("assignedFromField", "checking " + tempLocal);
               fieldNames = this.heuristics.getFieldName(tempLocal);
            } while(fieldNames.size() > 1);
         } while(fieldNames.size() != 1);

         String fieldName = (String)fieldNames.get(0);

         for(int count = 0; !this.isUniqueName(fieldName); ++count) {
            if (count == 0) {
               fieldName = fieldName + count;
            } else {
               fieldName = fieldName.substring(0, fieldName.length() - 1) + count;
            }
         }

         this.setName(tempLocal, fieldName);
      }
   }

   private void removeDollarSigns() {
      Iterator it = this.heuristics.getLocalsIterator();

      while(it.hasNext()) {
         Local tempLocal = (Local)it.next();
         String currentName = tempLocal.getName();
         int dollarIndex = currentName.indexOf(36);
         if (dollarIndex == 0) {
            String newName = currentName.substring(1, currentName.length());
            if (this.isUniqueName(newName)) {
               this.setName(tempLocal, newName);
            }
         }
      }

   }

   private void exceptionNaming() {
      Iterator it = this.heuristics.getLocalsIterator();

      while(true) {
         Local tempLocal;
         String typeString;
         do {
            if (!it.hasNext()) {
               return;
            }

            tempLocal = (Local)it.next();
            Type localType = tempLocal.getType();
            typeString = localType.toString();
         } while(typeString.indexOf("Exception") < 0);

         this.debug("exceptionNaming", "Type is an exception" + tempLocal);
         String newName = "";

         int count;
         for(count = 0; count < typeString.length(); ++count) {
            char character = typeString.charAt(count);
            if (Character.isUpperCase(character)) {
               newName = newName + Character.toLowerCase(character);
            }
         }

         count = 0;
         if (!this.isUniqueName(newName)) {
            ++count;

            while(!this.isUniqueName(newName + count)) {
               ++count;
            }
         }

         if (count != 0) {
            newName = newName + count;
         }

         this.setName(tempLocal, newName);
      }
   }

   private void forLoopIndexing() {
      Iterator it = this.heuristics.getLocalsIterator();

      while(true) {
         Local tempLocal;
         do {
            if (!it.hasNext()) {
               return;
            }

            tempLocal = (Local)it.next();
            this.debug("foeLoopIndexing", "Checking local" + tempLocal.getName());
         } while(!this.heuristics.getHeuristic(tempLocal, 9));

         int count = -1;

         String newName;
         do {
            ++count;
            if (count >= this.forLoopNames.size()) {
               newName = null;
               break;
            }

            newName = (String)this.forLoopNames.get(count);
         } while(!this.isUniqueName(newName));

         if (newName != null) {
            this.setName(tempLocal, newName);
         }
      }
   }

   private void mainMethodArgument() {
      Iterator it = this.heuristics.getLocalsIterator();

      Local tempLocal;
      do {
         if (!it.hasNext()) {
            return;
         }

         tempLocal = (Local)it.next();
      } while(!this.heuristics.getHeuristic(tempLocal, 7));

      String newName = "args";

      for(int count = 0; !this.isUniqueName(newName); ++count) {
         if (count == 0) {
            newName = newName + count;
         } else {
            newName = newName.substring(0, newName.length() - 1) + count;
         }
      }

      this.setName(tempLocal, newName);
   }

   private void setName(Local var, String newName) {
      Object truthValue = this.changedOrNot.get(var);
      if (truthValue == null) {
         this.changedOrNot.put(var, new Boolean(false));
      } else if ((Boolean)truthValue) {
         this.debug("setName", "Var: " + var + " had already been renamed");
         return;
      }

      this.debug("setName", "Changed " + var.getName() + " to " + newName);
      var.setName(newName);
      this.changedOrNot.put(var, new Boolean(true));
   }

   private boolean alreadyChanged(Local var) {
      Object truthValue = this.changedOrNot.get(var);
      if (truthValue == null) {
         this.changedOrNot.put(var, new Boolean(false));
         return false;
      } else if ((Boolean)truthValue) {
         this.debug("alreadyChanged", "Var: " + var + " had already been renamed");
         return true;
      } else {
         return false;
      }
   }

   private boolean isUniqueName(String name) {
      Iterator it = this.getScopedLocals();

      while(it.hasNext()) {
         Local tempLocal = (Local)it.next();
         if (tempLocal.getName().equals(name)) {
            this.debug("isUniqueName", "New Name " + name + " is not unique (matches some local)..changing");
            return false;
         }

         this.debug("isUniqueName", "New Name " + name + " is different from local " + tempLocal.getName());
      }

      it = this.getScopedFields();

      while(it.hasNext()) {
         SootField tempField = (SootField)it.next();
         if (tempField.getName().equals(name)) {
            this.debug("isUniqueName", "New Name " + name + " is not unique (matches field)..changing");
            return false;
         }

         this.debug("isUniqueName", "New Name " + name + " is different from field " + tempField.getName());
      }

      return true;
   }

   private Iterator getScopedFields() {
      SootClass sootClass = this.methodNode.getDavaBody().getMethod().getDeclaringClass();
      this.fields = sootClass.getFields();
      return this.fields.iterator();
   }

   private Iterator getScopedLocals() {
      Iterator<Local> it = this.heuristics.getLocalsIterator();
      this.locals = new ArrayList();

      while(it.hasNext()) {
         this.locals.add(it.next());
      }

      return this.locals.iterator();
   }

   public void debug(String methodName, String debug) {
   }
}
