package com.gzoltar.shaded.org.jacoco.core.internal.analysis.filter;

import com.gzoltar.shaded.org.objectweb.asm.tree.AbstractInsnNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.MethodInsnNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.MethodNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.TryCatchBlockNode;
import java.util.Iterator;

public final class TryWithResourcesJavacFilter implements IFilter {
   public void filter(String className, String superClassName, MethodNode methodNode, IFilterOutput output) {
      if (!methodNode.tryCatchBlocks.isEmpty()) {
         TryWithResourcesJavacFilter.Matcher matcher = new TryWithResourcesJavacFilter.Matcher(output);
         Iterator i$ = methodNode.tryCatchBlocks.iterator();

         while(true) {
            TryCatchBlockNode t;
            do {
               if (!i$.hasNext()) {
                  return;
               }

               t = (TryCatchBlockNode)i$.next();
            } while(!"java/lang/Throwable".equals(t.type));

            TryWithResourcesJavacFilter.Matcher.JavacPattern[] arr$ = TryWithResourcesJavacFilter.Matcher.JavacPattern.values();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               TryWithResourcesJavacFilter.Matcher.JavacPattern p = arr$[i$];
               matcher.start(t.handler);
               if (matcher.matchJavac(p)) {
                  break;
               }
            }
         }
      }
   }

   static class Matcher extends AbstractMatcher {
      private final IFilterOutput output;
      private String expectedOwner;
      private AbstractInsnNode start;

      Matcher(IFilterOutput output) {
         this.output = output;
      }

      private void start(AbstractInsnNode start) {
         this.start = start;
         this.cursor = start.getPrevious();
         this.vars.clear();
         this.expectedOwner = null;
      }

      private boolean matchJavac(TryWithResourcesJavacFilter.Matcher.JavacPattern p) {
         this.nextIsVar(58, "t1");
         this.nextIsVar(25, "t1");
         this.nextIsVar(58, "primaryExc");
         this.nextIsVar(25, "t1");
         this.nextIs(191);
         this.nextIsVar(58, "t2");
         this.nextIsJavacClose(p, "e");
         this.nextIsVar(25, "t2");
         this.nextIs(191);
         if (this.cursor == null) {
            return false;
         } else {
            AbstractInsnNode end = this.cursor;
            AbstractInsnNode startOnNonExceptionalPath = this.start.getPrevious();
            this.cursor = startOnNonExceptionalPath;

            do {
               if (this.nextIsJavacClose(p, "n")) {
                  startOnNonExceptionalPath = startOnNonExceptionalPath.getNext();
                  AbstractInsnNode m = this.cursor;
                  this.next();
                  if (this.cursor.getOpcode() != 167) {
                     this.cursor = m;
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

      private boolean nextIsJavacClose(TryWithResourcesJavacFilter.Matcher.JavacPattern p, String ctx) {
         switch(p) {
         case METHOD:
         case FULL:
            this.nextIsVar(25, "r");
            this.nextIs(198);
         default:
            switch(p) {
            case METHOD:
            case OPTIMAL:
               this.nextIsVar(25, "primaryExc");
               this.nextIsVar(25, "r");
               this.nextIs(184);
               if (this.cursor != null) {
                  MethodInsnNode m = (MethodInsnNode)this.cursor;
                  if ("$closeResource".equals(m.name) && "(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V".equals(m.desc)) {
                     return true;
                  }

                  this.cursor = null;
               }

               return false;
            case FULL:
            case OMITTED_NULL_CHECK:
               this.nextIsVar(25, "primaryExc");
               this.nextIs(198);
               this.nextIsClose();
               this.nextIs(167);
               this.nextIsVar(58, ctx + "t");
               this.nextIsVar(25, "primaryExc");
               this.nextIsVar(25, ctx + "t");
               this.nextIsAddSuppressed();
               this.nextIs(167);
               this.nextIsClose();
               return this.cursor != null;
            default:
               throw new AssertionError();
            }
         }
      }

      private void nextIsClose() {
         this.nextIsVar(25, "r");
         this.next();
         if (this.cursor != null) {
            if (this.cursor.getOpcode() != 185 && this.cursor.getOpcode() != 182) {
               this.cursor = null;
            } else {
               MethodInsnNode m = (MethodInsnNode)this.cursor;
               if ("close".equals(m.name) && "()V".equals(m.desc)) {
                  String actual = m.owner;
                  if (this.expectedOwner == null) {
                     this.expectedOwner = actual;
                  } else if (!this.expectedOwner.equals(actual)) {
                     this.cursor = null;
                  }

               } else {
                  this.cursor = null;
               }
            }
         }
      }

      private static enum JavacPattern {
         OPTIMAL,
         FULL,
         OMITTED_NULL_CHECK,
         METHOD;
      }
   }
}
