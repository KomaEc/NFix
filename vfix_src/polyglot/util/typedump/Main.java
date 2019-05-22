package polyglot.util.typedump;

import java.io.IOException;
import polyglot.frontend.ExtensionInfo;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;

public class Main {
   public static void main(String[] args) {
      String extension = "jl";
      if (args.length == 3 && args[0].equals("-ext")) {
         extension = args[1];
      }

      if (extension == null && args.length != 1 || extension != null && args.length != 3) {
         System.err.println("Usage: polyglot.util.typedump.Main [-ext <extension>] <classname>");
         System.exit(1);
      }

      String className;
      if (extension == null) {
         className = args[0];
      } else {
         className = args[2];
      }

      ExtensionInfo extInfo = null;
      String extClassName = "polyglot.ext." + extension + ".ExtensionInfo";
      Class extClass = null;

      try {
         extClass = Class.forName(extClassName);
      } catch (ClassNotFoundException var16) {
         System.err.println("Extension " + extension + " not found: could not find class " + extClassName + ".");
         System.exit(1);
      }

      try {
         extInfo = (ExtensionInfo)extClass.newInstance();
      } catch (Exception var15) {
         System.err.println("Extension " + extension + " could not be loaded: " + "could not instantiate " + extClassName + ".");
         System.exit(1);
      }

      try {
         TypeSystem ts = extInfo.typeSystem();
         TypeDumper t = TypeDumper.load(className, ts);
         CodeWriter cw = new CodeWriter(System.out, 72);
         t.dump(cw);
         cw.newline(0);

         try {
            cw.flush();
         } catch (IOException var10) {
            System.err.println(var10.getMessage());
         }
      } catch (IOException var11) {
         System.err.println("IO errors.");
         System.err.println(var11.getMessage());
      } catch (ClassNotFoundException var12) {
         System.err.println("Could not load .class: " + className);
         System.err.println(var12.getMessage());
      } catch (NoSuchFieldException var13) {
         System.err.println("Could not reflect jlc fields");
         System.err.println(var13.getMessage());
      } catch (SecurityException var14) {
         System.err.println("Security policy error.");
         System.err.println(var14.getMessage());
      }

   }
}
