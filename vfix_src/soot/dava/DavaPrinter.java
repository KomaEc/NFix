package soot.dava;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.G;
import soot.IntType;
import soot.LongType;
import soot.Modifier;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.Singletons;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.UnitPrinter;
import soot.dava.internal.AST.ASTNode;
import soot.dava.toolkits.base.renamer.RemoveFullyQualifiedName;
import soot.options.Options;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.StringConstantValueTag;
import soot.tagkit.Tag;
import soot.util.Chain;
import soot.util.IterableSet;

public class DavaPrinter {
   public DavaPrinter(Singletons.Global g) {
   }

   public static DavaPrinter v() {
      return G.v().soot_dava_DavaPrinter();
   }

   private void printStatementsInBody(Body body, PrintWriter out) {
      if (Options.v().verbose()) {
         System.out.println("Printing " + body.getMethod().getName());
      }

      Chain<Unit> units = ((DavaBody)body).getUnits();
      if (units.size() != 1) {
         throw new RuntimeException("DavaBody AST doesn't have single root.");
      } else {
         UnitPrinter up = new DavaUnitPrinter((DavaBody)body);
         ((ASTNode)units.getFirst()).toString(up);
         out.print(up.toString());
      }
   }

   public void printTo(SootClass cl, PrintWriter out) {
      IterableSet importList = new IterableSet();
      String superClassName = cl.getJavaPackageName();
      if (!superClassName.equals("")) {
         out.println("package " + superClassName + ";");
         out.println();
      }

      if (cl.hasSuperclass()) {
         SootClass superClass = cl.getSuperclass();
         importList.add(superClass.toString());
      }

      Iterator interfaceIt = cl.getInterfaces().iterator();

      String declaration;
      while(interfaceIt.hasNext()) {
         declaration = ((SootClass)interfaceIt.next()).toString();
         if (!importList.contains(declaration)) {
            importList.add(declaration);
         }
      }

      Iterator methodIt = cl.methodIterator();

      Iterator pit;
      String val;
      String temp;
      while(methodIt.hasNext()) {
         SootMethod dm = (SootMethod)methodIt.next();
         if (dm.hasActiveBody()) {
            importList = importList.union(((DavaBody)dm.getActiveBody()).getImportList());
         }

         pit = dm.getExceptions().iterator();

         while(pit.hasNext()) {
            val = ((SootClass)pit.next()).toString();
            if (!importList.contains(val)) {
               importList.add(val);
            }
         }

         Iterator pit = dm.getParameterTypes().iterator();

         Type t;
         while(pit.hasNext()) {
            t = (Type)pit.next();
            if (t instanceof RefType) {
               temp = ((RefType)t).getSootClass().toString();
               if (!importList.contains(temp)) {
                  importList.add(temp);
               }
            }
         }

         t = dm.getReturnType();
         if (t instanceof RefType) {
            temp = ((RefType)t).getSootClass().toString();
            if (!importList.contains(temp)) {
               importList.add(temp);
            }
         }
      }

      Iterator fieldIt = cl.getFields().iterator();

      String temp;
      while(fieldIt.hasNext()) {
         SootField f = (SootField)fieldIt.next();
         if (!f.isPhantom()) {
            Type t = f.getType();
            if (t instanceof RefType) {
               temp = ((RefType)t).getSootClass().toString();
               if (!importList.contains(temp)) {
                  importList.add(temp);
               }
            }
         }
      }

      pit = importList.iterator();
      ArrayList toImport = new ArrayList();

      while(true) {
         do {
            do {
               if (!pit.hasNext()) {
                  Iterator it = toImport.iterator();

                  while(it.hasNext()) {
                     temp = (String)it.next();
                     if (RemoveFullyQualifiedName.containsMultiple(toImport.iterator(), temp, (Type)null)) {
                        if (temp.lastIndexOf(46) <= -1) {
                           throw new DecompilationException("Cant find the DOT . for fullyqualified name");
                        }

                        temp = temp.substring(0, temp.lastIndexOf(46));
                        out.println("import " + temp + ".*;");
                     } else if (temp.lastIndexOf(46) != -1) {
                        out.println("import " + temp + ";");
                     }
                  }

                  boolean addNewLine = false;
                  addNewLine = true;
                  if (addNewLine) {
                     out.println();
                  }

                  Dava.v().set_CurrentPackageContext(importList);
                  Dava.v().set_CurrentPackage(superClassName);
                  superClassName = "";
                  superClassName = superClassName + " " + Modifier.toString(cl.getModifiers());
                  superClassName = superClassName.trim();
                  if (!cl.isInterface()) {
                     superClassName = superClassName + " class";
                     superClassName = superClassName.trim();
                  }

                  out.print(superClassName + " " + cl.getShortJavaStyleName());
                  if (cl.hasSuperclass() && !cl.getSuperclass().getName().equals("java.lang.Object")) {
                     superClassName = cl.getSuperclass().getName();
                     superClassName = RemoveFullyQualifiedName.getReducedName(importList, superClassName, cl.getType());
                     out.print(" extends " + superClassName + "");
                  }

                  Iterator<SootClass> methodIt = cl.getInterfaces().iterator();
                  if (methodIt.hasNext()) {
                     if (cl.isInterface()) {
                        out.print(" extends ");
                     } else {
                        out.print(" implements ");
                     }

                     out.print("" + ((SootClass)methodIt.next()).getName() + "");

                     while(methodIt.hasNext()) {
                        out.print(", " + ((SootClass)methodIt.next()).getName() + "");
                     }
                  }

                  out.println();
                  out.println("{");
                  methodIt = cl.getFields().iterator();
                  if (methodIt.hasNext()) {
                     label222:
                     while(true) {
                        while(true) {
                           while(true) {
                              SootField f;
                              do {
                                 if (!methodIt.hasNext()) {
                                    break label222;
                                 }

                                 f = (SootField)methodIt.next();
                              } while(f.isPhantom());

                              declaration = null;
                              Type fieldType = f.getType();
                              String qualifiers = Modifier.toString(f.getModifiers()) + " ";
                              qualifiers = qualifiers + RemoveFullyQualifiedName.getReducedName(importList, fieldType.toString(), fieldType);
                              qualifiers = qualifiers.trim();
                              if (qualifiers.equals("")) {
                                 declaration = Scene.v().quotedNameOf(f.getName());
                              } else {
                                 declaration = qualifiers + " " + Scene.v().quotedNameOf(f.getName()) + "";
                              }

                              if (f.isFinal() && f.isStatic()) {
                                 if (fieldType instanceof DoubleType && f.hasTag("DoubleConstantValueTag")) {
                                    double val = ((DoubleConstantValueTag)f.getTag("DoubleConstantValueTag")).getDoubleValue();
                                    out.println("    " + declaration + " = " + val + ";");
                                 } else if (fieldType instanceof FloatType && f.hasTag("FloatConstantValueTag")) {
                                    float val = ((FloatConstantValueTag)f.getTag("FloatConstantValueTag")).getFloatValue();
                                    out.println("    " + declaration + " = " + val + "f;");
                                 } else if (fieldType instanceof LongType && f.hasTag("LongConstantValueTag")) {
                                    long val = ((LongConstantValueTag)f.getTag("LongConstantValueTag")).getLongValue();
                                    out.println("    " + declaration + " = " + val + "l;");
                                 } else {
                                    int val;
                                    if (fieldType instanceof CharType && f.hasTag("IntegerConstantValueTag")) {
                                       val = ((IntegerConstantValueTag)f.getTag("IntegerConstantValueTag")).getIntValue();
                                       out.println("    " + declaration + " = '" + (char)val + "';");
                                    } else if (fieldType instanceof BooleanType && f.hasTag("IntegerConstantValueTag")) {
                                       val = ((IntegerConstantValueTag)f.getTag("IntegerConstantValueTag")).getIntValue();
                                       if (val == 0) {
                                          out.println("    " + declaration + " = false;");
                                       } else {
                                          out.println("    " + declaration + " = true;");
                                       }
                                    } else if ((fieldType instanceof IntType || fieldType instanceof ByteType || fieldType instanceof ShortType) && f.hasTag("IntegerConstantValueTag")) {
                                       val = ((IntegerConstantValueTag)f.getTag("IntegerConstantValueTag")).getIntValue();
                                       out.println("    " + declaration + " = " + val + ";");
                                    } else if (f.hasTag("StringConstantValueTag")) {
                                       val = ((StringConstantValueTag)f.getTag("StringConstantValueTag")).getStringValue();
                                       out.println("    " + declaration + " = \"" + val + "\";");
                                    } else {
                                       out.println("    " + declaration + ";");
                                    }
                                 }
                              } else {
                                 out.println("    " + declaration + ";");
                              }
                           }
                        }
                     }
                  }

                  methodIt = cl.methodIterator();
                  if (methodIt.hasNext()) {
                     if (cl.getMethodCount() != 0) {
                        out.println();
                     }

                     label178:
                     while(true) {
                        while(true) {
                           SootMethod method;
                           do {
                              if (!methodIt.hasNext()) {
                                 break label178;
                              }

                              method = (SootMethod)methodIt.next();
                           } while(method.isPhantom());

                           if (!Modifier.isAbstract(method.getModifiers()) && !Modifier.isNative(method.getModifiers())) {
                              if (!method.hasActiveBody()) {
                                 throw new RuntimeException("method " + method.getName() + " has no active body!");
                              }

                              this.printTo(method.getActiveBody(), out);
                              if (methodIt.hasNext()) {
                                 out.println();
                              }
                           } else {
                              out.print("    ");
                              out.print(method.getDavaDeclaration());
                              out.println(";");
                              if (methodIt.hasNext()) {
                                 out.println();
                              }
                           }
                        }
                     }
                  }

                  if (G.v().SootClassNeedsDavaSuperHandlerClass.contains(cl)) {
                     out.println("\n    private static class DavaSuperHandler{");
                     out.println("         java.util.Vector myVector = new java.util.Vector();");
                     out.println("\n         public Object get(int pos){");
                     out.println("            return myVector.elementAt(pos);");
                     out.println("         }");
                     out.println("\n         public void store(Object obj){");
                     out.println("            myVector.add(obj);");
                     out.println("         }");
                     out.println("    }");
                  }

                  out.println("}");
                  return;
               }

               temp = (String)pit.next();
               if (temp.indexOf("java.lang") <= -1) {
                  break;
               }

               temp = RemoveFullyQualifiedName.getClassName(temp);
            } while(temp.equals("java.lang." + temp));
         } while(superClassName.length() > 0 && temp.indexOf(superClassName) > -1);

         if (!cl.toString().equals(temp)) {
            toImport.add(temp);
         }
      }
   }

   private void printTo(Body b, PrintWriter out) {
      b.validate();
      String decl = b.getMethod().getDavaDeclaration();
      out.println("    " + decl);
      Iterator tIt = b.getMethod().getTags().iterator();

      while(tIt.hasNext()) {
         Tag t = (Tag)tIt.next();
         if (Options.v().print_tags_in_output()) {
            out.println(t);
         }
      }

      out.println("    {");
      this.printStatementsInBody(b, out);
      out.println("    }");
   }
}
