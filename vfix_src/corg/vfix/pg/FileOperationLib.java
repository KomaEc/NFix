package corg.vfix.pg;

import com.google.common.io.Files;
import corg.vfix.vd.VDMain;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;

public class FileOperationLib {
   public static void cleanOutput() throws IOException {
      String outputDir = System.getProperty("user.dir") + "/Output/";
      deleteDirectoryRecursion(Paths.get(outputDir));
   }

   public static void cleanFLData(String project, int id) throws IOException {
      String flDir = System.getProperty("user.dir") + "/dataset/" + project + "-" + id + "/gzoltar-data/";
      deleteDirectoryRecursion(Paths.get(flDir));
   }

   public static boolean createDir(String destDirName) {
      File dir = new File(destDirName);
      if (dir.getParentFile().exists()) {
         return true;
      } else if (dir.getParentFile().mkdirs()) {
         return true;
      } else {
         System.out.println("create directory fail!");
         return false;
      }
   }

   public static void printToFile(String filename, String content) {
      try {
         File writename = new File(filename);
         writename.createNewFile();
         BufferedWriter out = new BufferedWriter(new FileWriter(writename));
         out.write(content);
         out.flush();
         out.close();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void copyFile(String filename1, String filename2) throws IOException {
      createDir(filename2);
      File source = new File(filename1);
      File dest = new File(filename2);
      if (source.exists()) {
         Files.copy(source, dest);
         System.out.println("File Created: " + filename2);
      }
   }

   private static void deleteDirectoryRecursion(Path path) throws IOException {
      if (java.nio.file.Files.isDirectory(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
         Throwable var1 = null;
         Object var2 = null;

         try {
            DirectoryStream entries = java.nio.file.Files.newDirectoryStream(path);

            try {
               Iterator var5 = entries.iterator();

               while(var5.hasNext()) {
                  Path entry = (Path)var5.next();
                  deleteDirectoryRecursion(entry);
               }
            } finally {
               if (entries != null) {
                  entries.close();
               }

            }
         } catch (Throwable var11) {
            if (var1 == null) {
               var1 = var11;
            } else if (var1 != var11) {
               var1.addSuppressed(var11);
            }

            throw var1;
         }
      }

      java.nio.file.Files.delete(path);
   }

   public static void copyPatchToTmp(String project, int id, String runtime_id, int patch_id) {
      String src = System.getProperty("user.dir") + "/output/" + runtime_id + "/" + project + "-" + id + "/patchOutput/" + patch_id + "/classes";
      String tgt = System.getProperty("user.dir") + "/tmp/" + project + "-" + id + "/target/classes";
      File srcFile = new File(src);
      File tgtFile = new File(tgt);

      try {
         FileUtils.copyDirectory(srcFile, tgtFile);
      } catch (IOException var9) {
         var9.printStackTrace();
      }

   }

   public static void copyDataToTmp(String project, int id) {
      String src = System.getProperty("user.dir") + "/dataset/" + project + "-" + id + "/target";
      String tgt = System.getProperty("user.dir") + "/tmp/" + project + "-" + id + "/target";
      File srcFile = new File(src);
      File tgtFile = new File(tgt);
      if (!tgtFile.exists()) {
         try {
            FileUtils.copyDirectory(srcFile, tgtFile);
         } catch (IOException var7) {
            var7.printStackTrace();
         }

      }
   }

   public static void main(String[] args) throws Exception {
      String runtime_id = "12-31-18-48-42";
      String project = "chart";
      int id = 4;
      int patch_id = 1;
      VDMain.runOneTest(project, id, runtime_id, patch_id);
   }
}
