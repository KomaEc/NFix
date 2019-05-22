package soot;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.options.Options;
import soot.tagkit.JimpleLineNumberTag;
import soot.tagkit.Tag;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.Chain;
import soot.util.DeterministicHashMap;

public class Printer {
   private static final Logger logger = LoggerFactory.getLogger(Printer.class);
   private static final char fileSeparator = System.getProperty("file.separator").charAt(0);
   public static final int USE_ABBREVIATIONS = 1;
   public static final int ADD_JIMPLE_LN = 16;
   int options = 0;
   int jimpleLnNum = 0;

   public Printer(Singletons.Global g) {
   }

   public static Printer v() {
      return G.v().soot_Printer();
   }

   public boolean useAbbreviations() {
      return (this.options & 1) != 0;
   }

   public boolean addJimpleLn() {
      return (this.options & 16) != 0;
   }

   public void setOption(int opt) {
      this.options |= opt;
   }

   public void clearOption(int opt) {
      this.options &= ~opt;
   }

   public int getJimpleLnNum() {
      return this.jimpleLnNum;
   }

   public void setJimpleLnNum(int newVal) {
      this.jimpleLnNum = newVal;
   }

   public void incJimpleLnNum() {
      ++this.jimpleLnNum;
   }

   public void printTo(SootClass cl, PrintWriter out) {
      this.setJimpleLnNum(1);
      StringTokenizer st = new StringTokenizer(Modifier.toString(cl.getModifiers()));

      while(true) {
         String tok;
         do {
            if (!st.hasMoreTokens()) {
               tok = "";
               if (!cl.isInterface()) {
                  tok = tok + " class";
                  tok = tok.trim();
               }

               out.print(tok + " " + Scene.v().quotedNameOf(cl.getName()) + "");
               if (cl.hasSuperclass()) {
                  out.print(" extends " + Scene.v().quotedNameOf(cl.getSuperclass().getName()) + "");
               }

               Iterator<SootClass> methodIt = cl.getInterfaces().iterator();
               if (methodIt.hasNext()) {
                  out.print(" implements ");
                  out.print("" + Scene.v().quotedNameOf(((SootClass)methodIt.next()).getName()) + "");

                  while(methodIt.hasNext()) {
                     out.print(",");
                     out.print(" " + Scene.v().quotedNameOf(((SootClass)methodIt.next()).getName()) + "");
                  }
               }

               out.println();
               this.incJimpleLnNum();
               out.println("{");
               this.incJimpleLnNum();
               if (Options.v().print_tags_in_output()) {
                  methodIt = cl.getTags().iterator();

                  while(methodIt.hasNext()) {
                     Tag t = (Tag)methodIt.next();
                     out.print("/*");
                     out.print(t.toString());
                     out.println("*/");
                  }
               }

               methodIt = cl.getFields().iterator();
               Iterator mTagIterator;
               Tag t;
               if (methodIt.hasNext()) {
                  label114:
                  while(true) {
                     SootField f;
                     do {
                        if (!methodIt.hasNext()) {
                           break label114;
                        }

                        f = (SootField)methodIt.next();
                     } while(f.isPhantom());

                     if (Options.v().print_tags_in_output()) {
                        mTagIterator = f.getTags().iterator();

                        while(mTagIterator.hasNext()) {
                           t = (Tag)mTagIterator.next();
                           out.print("/*");
                           out.print(t.toString());
                           out.println("*/");
                        }
                     }

                     out.println("    " + f.getDeclaration() + ";");
                     if (this.addJimpleLn()) {
                        this.setJimpleLnNum(this.addJimpleLnTags(this.getJimpleLnNum(), f));
                     }
                  }
               }

               methodIt = cl.methodIterator();
               if (methodIt.hasNext()) {
                  if (cl.getMethodCount() != 0) {
                     out.println();
                     this.incJimpleLnNum();
                  }

                  label94:
                  while(true) {
                     while(true) {
                        SootMethod method;
                        do {
                           if (!methodIt.hasNext()) {
                              break label94;
                           }

                           method = (SootMethod)methodIt.next();
                        } while(method.isPhantom());

                        if (!Modifier.isAbstract(method.getModifiers()) && !Modifier.isNative(method.getModifiers())) {
                           if (!method.hasActiveBody()) {
                              method.retrieveActiveBody();
                              if (!method.hasActiveBody()) {
                                 throw new RuntimeException("method " + method.getName() + " has no active body!");
                              }
                           } else if (Options.v().print_tags_in_output()) {
                              mTagIterator = method.getTags().iterator();

                              while(mTagIterator.hasNext()) {
                                 t = (Tag)mTagIterator.next();
                                 out.print("/*");
                                 out.print(t.toString());
                                 out.println("*/");
                              }
                           }

                           this.printTo(method.getActiveBody(), out);
                           if (methodIt.hasNext()) {
                              out.println();
                              this.incJimpleLnNum();
                           }
                        } else {
                           if (Options.v().print_tags_in_output()) {
                              mTagIterator = method.getTags().iterator();

                              while(mTagIterator.hasNext()) {
                                 t = (Tag)mTagIterator.next();
                                 out.print("/*");
                                 out.print(t.toString());
                                 out.println("*/");
                              }
                           }

                           out.print("    ");
                           out.print(method.getDeclaration());
                           out.println(";");
                           this.incJimpleLnNum();
                           if (methodIt.hasNext()) {
                              out.println();
                              this.incJimpleLnNum();
                           }
                        }
                     }
                  }
               }

               out.println("}");
               this.incJimpleLnNum();
               return;
            }

            tok = st.nextToken();
         } while(cl.isInterface() && tok.equals("abstract"));

         out.print(tok + " ");
      }
   }

   public void printTo(Body b, PrintWriter out) {
      boolean isPrecise = !this.useAbbreviations();
      String decl = b.getMethod().getDeclaration();
      out.println("    " + decl);
      if (!this.addJimpleLn()) {
      }

      if (this.addJimpleLn()) {
         this.setJimpleLnNum(this.addJimpleLnTags(this.getJimpleLnNum(), b.getMethod()));
      }

      out.println("    {");
      this.incJimpleLnNum();
      UnitGraph unitGraph = new BriefUnitGraph(b);
      Object up;
      if (isPrecise) {
         up = new NormalUnitPrinter(b);
      } else {
         up = new BriefUnitPrinter(b);
      }

      if (this.addJimpleLn()) {
         ((LabeledUnitPrinter)up).setPositionTagger(new AttributesUnitPrinter(this.getJimpleLnNum()));
      }

      this.printLocalsInBody(b, (UnitPrinter)up);
      this.printStatementsInBody(b, out, (LabeledUnitPrinter)up, unitGraph);
      out.println("    }");
      this.incJimpleLnNum();
   }

   private void printStatementsInBody(Body body, PrintWriter out, LabeledUnitPrinter up, UnitGraph unitGraph) {
      Chain<Unit> units = body.getUnits();
      Iterator trapIt = units.iterator();

      while(true) {
         Unit currentStmt;
         do {
            if (!trapIt.hasNext()) {
               out.print(up.toString());
               if (this.addJimpleLn()) {
                  this.setJimpleLnNum(up.getPositionTagger().getEndLn());
               }

               trapIt = body.getTraps().iterator();
               if (trapIt.hasNext()) {
                  out.println();
                  this.incJimpleLnNum();
               }

               while(trapIt.hasNext()) {
                  Trap trap = (Trap)trapIt.next();
                  out.println("        catch " + Scene.v().quotedNameOf(trap.getException().getName()) + " from " + (String)up.labels().get(trap.getBeginUnit()) + " to " + (String)up.labels().get(trap.getEndUnit()) + " with " + (String)up.labels().get(trap.getHandlerUnit()) + ";");
                  this.incJimpleLnNum();
               }

               return;
            }

            currentStmt = (Unit)trapIt.next();
            if (currentStmt != units.getFirst()) {
               if (unitGraph.getSuccsOf(currentStmt).size() == 1 && unitGraph.getPredsOf(currentStmt).size() == 1 && !up.labels().containsKey(currentStmt)) {
                  List<Unit> succs = unitGraph.getSuccsOf(currentStmt);
                  if (succs.get(0) != currentStmt) {
                     up.newline();
                  }
               } else {
                  up.newline();
               }
            }

            if (up.labels().containsKey(currentStmt)) {
               up.unitRef(currentStmt, true);
               up.literal(":");
               up.newline();
            }

            if (up.references().containsKey(currentStmt)) {
               up.unitRef(currentStmt, false);
            }

            up.startUnit(currentStmt);
            currentStmt.toString(up);
            up.endUnit(currentStmt);
            up.literal(";");
            up.newline();
         } while(!Options.v().print_tags_in_output());

         Iterator tagIterator = currentStmt.getTags().iterator();

         while(tagIterator.hasNext()) {
            Tag t = (Tag)tagIterator.next();
            up.noIndent();
            up.literal("/*");
            up.literal(t.toString());
            up.literal("*/");
            up.newline();
         }
      }
   }

   private int addJimpleLnTags(int lnNum, SootMethod meth) {
      meth.addTag(new JimpleLineNumberTag(lnNum));
      ++lnNum;
      return lnNum;
   }

   private int addJimpleLnTags(int lnNum, SootField f) {
      f.addTag(new JimpleLineNumberTag(lnNum));
      ++lnNum;
      return lnNum;
   }

   private void printLocalsInBody(Body body, UnitPrinter up) {
      Map<Type, List<Local>> typeToLocals = new DeterministicHashMap(body.getLocalCount() * 2 + 1, 0.7F);

      Iterator typeIt;
      Local local;
      Object localList;
      for(typeIt = body.getLocals().iterator(); typeIt.hasNext(); ((List)localList).add(local)) {
         local = (Local)typeIt.next();
         Type t = local.getType();
         if (typeToLocals.containsKey(t)) {
            localList = (List)typeToLocals.get(t);
         } else {
            localList = new ArrayList();
            typeToLocals.put(t, localList);
         }
      }

      typeIt = typeToLocals.keySet().iterator();

      while(typeIt.hasNext()) {
         Type type = (Type)typeIt.next();
         List<Local> localList = (List)typeToLocals.get(type);
         Object[] locals = localList.toArray();
         up.type(type);
         up.literal(" ");

         for(int k = 0; k < locals.length; ++k) {
            if (k != 0) {
               up.literal(", ");
            }

            up.local((Local)locals[k]);
         }

         up.literal(";");
         up.newline();
      }

      if (!typeToLocals.isEmpty()) {
         up.newline();
      }

   }
}
