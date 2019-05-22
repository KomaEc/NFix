package org.codehaus.groovy.ant;

import groovyjarjarasm.asm.ClassReader;
import groovyjarjarasm.asm.Label;
import groovyjarjarasm.asm.MethodVisitor;
import groovyjarjarasm.asm.tree.AbstractInsnNode;
import groovyjarjarasm.asm.tree.ClassNode;
import groovyjarjarasm.asm.tree.MethodNode;
import groovyjarjarasm.asm.tree.analysis.Analyzer;
import groovyjarjarasm.asm.tree.analysis.Frame;
import groovyjarjarasm.asm.tree.analysis.SimpleVerifier;
import groovyjarjarasm.asm.util.CheckClassAdapter;
import groovyjarjarasm.asm.util.TraceMethodVisitor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MatchingTask;

public class VerifyClass extends MatchingTask {
   private String topDir = null;
   private boolean verbose = false;

   public void execute() throws BuildException {
      if (this.topDir == null) {
         throw new BuildException("no dir attribute is set");
      } else {
         File top = new File(this.topDir);
         if (!top.exists()) {
            throw new BuildException("the directory " + top + " does not exist");
         } else {
            this.log("top dir is " + top);
            int fails = this.execute(top);
            if (fails == 0) {
               this.log("no bytecode problems found");
            } else {
               this.log("found " + fails + " failing classes");
            }

         }
      }
   }

   public void setDir(String dir) throws BuildException {
      this.topDir = dir;
   }

   public void setVerbose(boolean v) {
      this.verbose = v;
   }

   private int execute(File dir) {
      int fails = 0;
      File[] files = dir.listFiles();

      for(int i = 0; i < files.length; ++i) {
         File f = files[i];
         if (f.isDirectory()) {
            fails += this.execute(f);
         } else if (f.getName().endsWith(".class")) {
            try {
               boolean ok = this.readClass(f.getCanonicalPath());
               if (!ok) {
                  ++fails;
               }
            } catch (IOException var7) {
               this.log(var7.getMessage());
               throw new BuildException(var7);
            }
         }
      }

      return fails;
   }

   private boolean readClass(String clazz) throws IOException {
      ClassReader cr = new ClassReader(new FileInputStream(clazz));
      ClassNode ca = new ClassNode() {
         public void visitEnd() {
         }
      };
      cr.accept(new CheckClassAdapter(ca), 1);
      boolean failed = false;
      List methods = ca.methods;

      for(int i = 0; i < methods.size(); ++i) {
         MethodNode method = (MethodNode)methods.get(i);
         if (method.instructions.size() > 0) {
            Analyzer a = new Analyzer(new SimpleVerifier());

            try {
               a.analyze(ca.name, method);
            } catch (Exception var13) {
               var13.printStackTrace();
               final Frame[] frames = a.getFrames();
               if (!failed) {
                  failed = true;
                  this.log("verifying of class " + clazz + " failed");
               }

               if (this.verbose) {
                  this.log(method.name + method.desc);
               }

               TraceMethodVisitor mv = new TraceMethodVisitor((MethodVisitor)null) {
                  public void visitMaxs(int maxStack, int maxLocals) {
                     StringBuffer buffer = new StringBuffer();

                     for(int i = 0; i < this.text.size(); ++i) {
                        String s;
                        for(s = frames[i] == null ? "null" : frames[i].toString(); s.length() < maxStack + maxLocals + 1; s = s + " ") {
                        }

                        buffer.append(Integer.toString(i + 100000).substring(1));
                        buffer.append(" ");
                        buffer.append(s);
                        buffer.append(" : ");
                        buffer.append(this.text.get(i));
                     }

                     if (VerifyClass.this.verbose) {
                        VerifyClass.this.log(buffer.toString());
                     }

                  }
               };

               for(int j = 0; j < method.instructions.size(); ++j) {
                  Object insn = method.instructions.get(j);
                  if (insn instanceof AbstractInsnNode) {
                     ((AbstractInsnNode)insn).accept(mv);
                  } else {
                     mv.visitLabel((Label)insn);
                  }
               }

               mv.visitMaxs(method.maxStack, method.maxLocals);
            }
         }
      }

      return !failed;
   }
}
