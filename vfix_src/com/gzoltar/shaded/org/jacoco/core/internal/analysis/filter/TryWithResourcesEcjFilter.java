package com.gzoltar.shaded.org.jacoco.core.internal.analysis.filter;

import com.gzoltar.shaded.org.objectweb.asm.tree.AbstractInsnNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.JumpInsnNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.LabelNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.MethodInsnNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.MethodNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.TryCatchBlockNode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class TryWithResourcesEcjFilter implements IFilter {
   public void filter(String className, String superClassName, MethodNode methodNode, IFilterOutput output) {
      if (!methodNode.tryCatchBlocks.isEmpty()) {
         TryWithResourcesEcjFilter.Matcher matcher = new TryWithResourcesEcjFilter.Matcher(output);
         Iterator i$ = methodNode.tryCatchBlocks.iterator();

         while(i$.hasNext()) {
            TryCatchBlockNode t = (TryCatchBlockNode)i$.next();
            if (t.type == null) {
               matcher.start(t.handler);
               if (!matcher.matchEcj()) {
                  matcher.start(t.handler);
                  matcher.matchEcjNoFlowOut();
               }
            }
         }

      }
   }

   static class Matcher extends AbstractMatcher {
      private final IFilterOutput output;
      private final Map<String, String> owners = new HashMap();
      private final Map<String, LabelNode> labels = new HashMap();
      private AbstractInsnNode start;

      Matcher(IFilterOutput output) {
         this.output = output;
      }

      private void start(AbstractInsnNode start) {
         this.start = start;
         this.cursor = start.getPrevious();
         this.vars.clear();
         this.labels.clear();
         this.owners.clear();
      }

      private boolean matchEcj() {
         this.nextIsVar(58, "primaryExc");
         this.nextIsEcjCloseAndThrow("r0");
         int resources = 1;
         String r = "r" + resources;

         AbstractInsnNode c;
         for(c = this.cursor; this.nextIsEcjClose(r); c = this.cursor) {
            this.nextIsJump(167, r + ".end");
            this.nextIsEcjSuppress(r);
            this.nextIsEcjCloseAndThrow(r);
            ++resources;
            r = "r" + resources;
         }

         this.cursor = c;
         this.nextIsEcjSuppress("last");
         this.nextIsVar(25, "primaryExc");
         this.nextIs(191);
         if (this.cursor == null) {
            return false;
         } else {
            AbstractInsnNode end = this.cursor;
            AbstractInsnNode startOnNonExceptionalPath = this.start.getPrevious();
            this.cursor = startOnNonExceptionalPath;

            do {
               if (this.nextIsEcjClose("r0")) {
                  startOnNonExceptionalPath = startOnNonExceptionalPath.getNext();
                  this.next();
                  if (this.cursor != null && this.cursor.getOpcode() == 167) {
                     this.output.ignore(startOnNonExceptionalPath, this.cursor);
                     this.output.ignore(this.start, end);
                     return true;
                  }

                  return false;
               }

               startOnNonExceptionalPath = startOnNonExceptionalPath.getPrevious();
               this.cursor = startOnNonExceptionalPath;
            } while(this.cursor != null);

            return false;
         }
      }

      private boolean matchEcjNoFlowOut() {
         this.nextIsVar(58, "primaryExc");
         int resources = 0;
         String r = "r" + resources;

         AbstractInsnNode c;
         for(c = this.cursor; this.nextIsEcjCloseAndThrow(r) && this.nextIsEcjSuppress(r); c = this.cursor) {
            ++resources;
            r = "r" + resources;
         }

         this.cursor = c;
         this.nextIsVar(25, "primaryExc");
         this.nextIs(191);
         if (this.cursor == null) {
            return false;
         } else {
            AbstractInsnNode end = this.cursor;
            AbstractInsnNode startOnNonExceptionalPath = this.start.getPrevious();
            this.cursor = startOnNonExceptionalPath;

            do {
               if (this.nextIsEcjClose("r0")) {
                  startOnNonExceptionalPath = startOnNonExceptionalPath.getNext();

                  for(int i = 1; i < resources; ++i) {
                     if (!this.nextIsEcjClose("r" + i)) {
                        return false;
                     }
                  }

                  this.output.ignore(startOnNonExceptionalPath, this.cursor);
                  this.output.ignore(this.start, end);
                  return true;
               }

               startOnNonExceptionalPath = startOnNonExceptionalPath.getPrevious();
               this.cursor = startOnNonExceptionalPath;
            } while(this.cursor != null);

            return false;
         }
      }

      private boolean nextIsEcjClose(String name) {
         this.nextIsVar(25, name);
         this.nextIsJump(198, name + ".end");
         this.nextIsClose(name);
         return this.cursor != null;
      }

      private boolean nextIsEcjCloseAndThrow(String name) {
         this.nextIsVar(25, name);
         this.nextIsJump(198, name);
         this.nextIsClose(name);
         this.nextIsLabel(name);
         this.nextIsVar(25, "primaryExc");
         this.nextIs(191);
         return this.cursor != null;
      }

      private boolean nextIsEcjSuppress(String name) {
         String suppressedExc = name + ".t";
         String startLabel = name + ".suppressStart";
         String endLabel = name + ".suppressEnd";
         this.nextIsVar(58, suppressedExc);
         this.nextIsVar(25, "primaryExc");
         this.nextIsJump(199, startLabel);
         this.nextIsVar(25, suppressedExc);
         this.nextIsVar(58, "primaryExc");
         this.nextIsJump(167, endLabel);
         this.nextIsLabel(startLabel);
         this.nextIsVar(25, "primaryExc");
         this.nextIsVar(25, suppressedExc);
         this.nextIsJump(165, endLabel);
         this.nextIsVar(25, "primaryExc");
         this.nextIsVar(25, suppressedExc);
         this.nextIsAddSuppressed();
         this.nextIsLabel(endLabel);
         return this.cursor != null;
      }

      private void nextIsClose(String name) {
         this.nextIsVar(25, name);
         this.next();
         if (this.cursor != null) {
            if (this.cursor.getOpcode() != 185 && this.cursor.getOpcode() != 182) {
               this.cursor = null;
            } else {
               MethodInsnNode m = (MethodInsnNode)this.cursor;
               if ("close".equals(m.name) && "()V".equals(m.desc)) {
                  String actual = m.owner;
                  String expected = (String)this.owners.get(name);
                  if (expected == null) {
                     this.owners.put(name, actual);
                  } else if (!expected.equals(actual)) {
                     this.cursor = null;
                  }

               } else {
                  this.cursor = null;
               }
            }
         }
      }

      private void nextIsJump(int opcode, String name) {
         this.nextIs(opcode);
         if (this.cursor != null) {
            LabelNode actual = ((JumpInsnNode)this.cursor).label;
            LabelNode expected = (LabelNode)this.labels.get(name);
            if (expected == null) {
               this.labels.put(name, actual);
            } else if (expected != actual) {
               this.cursor = null;
            }

         }
      }

      private void nextIsLabel(String name) {
         if (this.cursor != null) {
            this.cursor = this.cursor.getNext();
            if (this.cursor.getType() != 8) {
               this.cursor = null;
            } else {
               LabelNode actual = (LabelNode)this.cursor;
               LabelNode expected = (LabelNode)this.labels.get(name);
               if (expected != actual) {
                  this.cursor = null;
               }

            }
         }
      }
   }
}
