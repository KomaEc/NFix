package corg.vfix.vd.diff;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import corg.vfix.Configure;
import corg.vfix.pg.FileOperationLib;
import difflib.ChangeDelta;
import difflib.Chunk;
import difflib.DeleteDelta;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.InsertDelta;
import difflib.Patch;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CodeDiff {
   private File newSrc = null;
   private File oldSrc = null;
   private String project;
   int id;
   int patchNum;

   public static void main(String[] args) throws IOException {
      String project = args[0];
      int id = Integer.valueOf(args[1]);
      int patchNum = Integer.valueOf(args[2]);
      CodeDiff cd = new CodeDiff(project, id, patchNum);
      System.out.println("Compare Java Files: ");
      cd.diff();
      cd.extractTwoFiles();
   }

   CodeDiff(String project, int id, int patchNum) {
      this.project = project;
      this.id = id;
      this.patchNum = patchNum;
   }

   public void extractTwoFiles() throws IOException {
      String runtime_id = Configure.getCurrentTimeUsingDate();
      String tgtOldFile = this.oldSrc.getAbsolutePath().replace("tmp", "java/" + runtime_id + "/original");
      FileOperationLib.copyFile(this.oldSrc.getAbsolutePath(), tgtOldFile);
      String tgtNewFile = this.oldSrc.getAbsolutePath().replace("tmp", "java/" + runtime_id + "/revised");
      FileOperationLib.copyFile(this.newSrc.getAbsolutePath(), tgtNewFile);
   }

   private void getTwoFiles() throws IOException {
      String runtimeID = this.getRuntimeID();
      String newJavaDir = System.getProperty("user.dir") + "/output/" + runtimeID + "/" + this.project + "-" + this.id + "/patchOutput/" + this.patchNum + "/java/";
      String oldJavaDir = System.getProperty("user.dir") + "/dataset/" + this.project + "-" + this.id + "/source/";
      this.searchSrcFiles(new File(newJavaDir));
      if (this.newSrc == null) {
         System.out.println("cannot find java file in " + newJavaDir);
         throw new IOException();
      } else {
         System.out.println("Patch: " + this.newSrc.getAbsolutePath());
         this.oldSrc = this.getOldFileFromNew(this.newSrc, newJavaDir, oldJavaDir);
         System.out.println("Original: " + this.oldSrc.getAbsolutePath());
         this.parseOldSrc();
      }
   }

   private void parseOldSrc() throws FileNotFoundException {
      CompilationUnit cu = JavaParser.parse(this.oldSrc);
      String filename = this.oldSrc.getAbsolutePath().replace("dataset", "tmp");
      this.outputJavaFile(cu, filename);
      this.oldSrc = new File(filename);
   }

   private void outputJavaFile(CompilationUnit cu, String filename) {
      if (cu != null) {
         FileOperationLib.createDir(filename);

         try {
            File writename = new File(filename);
            writename.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(cu.toString());
            out.flush();
            out.close();
         } catch (Exception var5) {
            var5.printStackTrace();
         }

      }
   }

   public void diff() throws IOException {
      this.getTwoFiles();
      List<String> original = this.fileToLines(this.oldSrc.getAbsolutePath());
      List<String> revised = this.fileToLines(this.newSrc.getAbsolutePath());
      Patch patch = DiffUtils.diff(original, revised);
      Iterator var5 = patch.getDeltas().iterator();

      while(var5.hasNext()) {
         Delta delta = (Delta)var5.next();
         if (delta instanceof ChangeDelta) {
            System.out.println("\nChange From:");
            this.printChunk(delta.getOriginal());
            System.out.println("\nTo:");
            this.printChunk(delta.getRevised());
         } else if (delta instanceof InsertDelta) {
            System.out.println("\nInserted Code:");
            this.printChunk(delta.getRevised());
         } else if (delta instanceof DeleteDelta) {
            System.out.println("\nDeleted Code:");
            this.printChunk(delta.getOriginal());
         }
      }

   }

   private void printChunk(Chunk chunk) {
      System.out.println("Line: " + chunk.getPosition());
      System.out.println("Size: " + chunk.size());
      Iterator var3 = chunk.getLines().iterator();

      while(var3.hasNext()) {
         Object line = var3.next();
         System.out.println(line);
      }

   }

   private List<String> fileToLines(String filename) {
      List<String> lines = new LinkedList();
      String line = "";

      try {
         BufferedReader in = new BufferedReader(new FileReader(filename));

         while((line = in.readLine()) != null) {
            lines.add(line);
         }
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      return lines;
   }

   private File getOldFileFromNew(File newFile, String newDir, String oldDir) throws FileNotFoundException {
      String path = newFile.getAbsolutePath().replace(newDir, "");
      File oldFile = new File(oldDir + path);
      if (!oldFile.exists()) {
         throw new FileNotFoundException();
      } else {
         return oldFile;
      }
   }

   private void searchSrcFiles(File dir) {
      File[] var5;
      int var4 = (var5 = dir.listFiles()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         File file = var5[var3];
         if (file.getName().endsWith(".java")) {
            this.newSrc = file;
            return;
         }

         if (file.isDirectory()) {
            this.searchSrcFiles(file);
         }
      }

   }

   private String getRuntimeID() throws IOException {
      String filename = System.getProperty("user.dir") + "/tmp/runtime-id.txt";
      File file = new File(filename);
      if (!file.exists()) {
         return "";
      } else {
         BufferedReader br = new BufferedReader(new FileReader(file));
         String id = br.readLine();
         return id;
      }
   }
}
