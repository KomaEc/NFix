package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Random;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.FileUtils;

/** @deprecated */
public class Jikes {
   protected JikesOutputParser jop;
   protected String command;
   protected Project project;

   protected Jikes(JikesOutputParser jop, String command, Project project) {
      System.err.println("As of Ant 1.2 released in October 2000, the Jikes class");
      System.err.println("is considered to be dead code by the Ant developers and is unmaintained.");
      System.err.println("Don't use it!");
      this.jop = jop;
      this.command = command;
      this.project = project;
   }

   protected void compile(String[] args) {
      String[] commandArray = null;
      File tmpFile = null;

      try {
         String myos = System.getProperty("os.name");
         if (myos.toLowerCase().indexOf("windows") >= 0 && args.length > 250) {
            PrintWriter out = null;

            try {
               String tempFileName = "jikes" + (new Random(System.currentTimeMillis())).nextLong();
               tmpFile = new File(tempFileName);
               out = new PrintWriter(new FileWriter(tmpFile));

               for(int i = 0; i < args.length; ++i) {
                  out.println(args[i]);
               }

               out.flush();
               commandArray = new String[]{this.command, "@" + tmpFile.getAbsolutePath()};
            } catch (IOException var19) {
               throw new BuildException("Error creating temporary file", var19);
            } finally {
               FileUtils.close((Writer)out);
            }
         } else {
            commandArray = new String[args.length + 1];
            commandArray[0] = this.command;
            System.arraycopy(args, 0, commandArray, 1, args.length);
         }

         try {
            Execute exe = new Execute(this.jop);
            exe.setAntRun(this.project);
            exe.setWorkingDirectory(this.project.getBaseDir());
            exe.setCommandline(commandArray);
            exe.execute();
         } catch (IOException var18) {
            throw new BuildException("Error running Jikes compiler", var18);
         }
      } finally {
         if (tmpFile != null) {
            tmpFile.delete();
         }

      }

   }
}
