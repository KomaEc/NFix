package jasmin;

import jas.jasError;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Main {
   public static final String version = Main.class.getPackage().getImplementationVersion() == null ? "trunk" : Main.class.getPackage().getImplementationVersion();

   public static void assemble(InputStream in, OutputStream out, boolean number_lines) {
      ClassFile classFile = new ClassFile();

      try {
         InputStream inp = new BufferedInputStream(in);
         classFile.readJasmin(inp, "Jasmin", number_lines);
         inp.close();
         if (classFile.errorCount() > 0) {
            System.err.println("Jasmin: Found " + classFile.errorCount() + " errors");
            return;
         }

         classFile.write(out);
         out.flush();
      } catch (FileNotFoundException var5) {
         System.err.println("Jasmin: file not found");
         System.exit(-1);
      } catch (jasError var6) {
         classFile.report_error("JAS Error " + var6.getMessage());
         var6.printStackTrace();
      } catch (Exception var7) {
         classFile.report_error("Jasmin: exception - <" + var7.getClass().getName() + "> " + var7.getMessage() + ".");
         var7.printStackTrace();
      }

      if (classFile.errorCount() > 0) {
         System.err.println("Jasmin: Found " + classFile.errorCount() + " errors");
      }

   }

   public static void assemble(String dest_dir, String fname, boolean number_lines) {
      File file = new File(fname);
      File out_file = null;
      ClassFile classFile = new ClassFile();

      try {
         InputStream inp = new BufferedInputStream(new FileInputStream(fname));
         classFile.readJasmin(inp, file.getName(), number_lines);
         inp.close();
         if (classFile.errorCount() > 0) {
            System.err.println(fname + ": Found " + classFile.errorCount() + " errors");
            return;
         }

         String[] class_path = ScannerUtils.splitClassField(classFile.getClassName());
         String class_name = class_path[1];
         if (class_path[0] != null) {
            String class_dir = ScannerUtils.convertChars(class_path[0], "./", File.separatorChar);
            if (dest_dir != null) {
               dest_dir = dest_dir + File.separator + class_dir;
            } else {
               dest_dir = class_dir;
            }
         }

         if (dest_dir == null) {
            out_file = new File(class_name + ".class");
         } else {
            out_file = new File(dest_dir, class_name + ".class");
            File dest = new File(dest_dir);
            if (!dest.exists()) {
               dest.mkdirs();
            }

            if (!dest.isDirectory()) {
               throw new IOException("Cannot create directory");
            }
         }

         FileOutputStream outp = new FileOutputStream(out_file);
         classFile.write(outp);
         outp.close();
      } catch (FileNotFoundException var10) {
         System.err.println(fname + ": file not found");
         System.exit(-1);
      } catch (jasError var11) {
         classFile.report_error("JAS Error " + var11.getMessage());
         var11.printStackTrace();
      } catch (Exception var12) {
         classFile.report_error(fname + ": exception - <" + var12.getClass().getName() + "> " + var12.getMessage() + ".");
         var12.printStackTrace();
      }

      if (classFile.errorCount() > 0) {
         System.err.println(fname + ": Found " + classFile.errorCount() + " errors");
      }

   }

   public static void main(String[] args) {
      String dest_dir = null;
      boolean debug = false;
      String[] files = new String[args.length];
      int num_files = 0;
      if (args.length == 0) {
         System.err.println("usage: jasmin [-d <directory>] [-version] <file> [<file> ...]");
         System.exit(-1);
      }

      int i;
      for(i = 0; i < args.length; ++i) {
         if (args[i].equals("-d")) {
            dest_dir = args[i + 1];
            ++i;
         } else if (args[i].equals("-g")) {
            debug = true;
         } else if (args[i].equals("-version")) {
            System.out.println("Jasmin version: " + version);
            System.exit(0);
         } else {
            files[num_files++] = args[i];
         }
      }

      for(i = 0; i < num_files; ++i) {
         assemble(dest_dir, files[i], debug);
      }

   }
}
