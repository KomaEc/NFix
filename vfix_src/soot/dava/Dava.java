package soot.dava;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.CompilationDeathException;
import soot.G;
import soot.Local;
import soot.Singletons;
import soot.SootMethod;
import soot.Type;
import soot.jimple.Jimple;
import soot.util.IterableSet;

public class Dava {
   private static final Logger logger = LoggerFactory.getLogger(Dava.class);
   private static final String LOG_TO_FILE = null;
   private static final PrintStream LOG_TO_SCREEN = null;
   private Writer iOut = null;
   private IterableSet currentPackageContext = null;
   private String currentPackage;

   public Dava(Singletons.Global g) {
   }

   public static Dava v() {
      return G.v().soot_dava_Dava();
   }

   public void set_CurrentPackage(String cp) {
      this.currentPackage = cp;
   }

   public String get_CurrentPackage() {
      return this.currentPackage;
   }

   public void set_CurrentPackageContext(IterableSet cpc) {
      this.currentPackageContext = cpc;
   }

   public IterableSet get_CurrentPackageContext() {
      return this.currentPackageContext;
   }

   public DavaBody newBody(SootMethod m) {
      return new DavaBody(m);
   }

   public DavaBody newBody(Body b) {
      return new DavaBody(b);
   }

   public Local newLocal(String name, Type t) {
      return Jimple.v().newLocal(name, t);
   }

   public void log(String s) {
      if (LOG_TO_SCREEN != null) {
         LOG_TO_SCREEN.println(s);
         LOG_TO_SCREEN.flush();
      }

      if (LOG_TO_FILE != null) {
         if (this.iOut == null) {
            try {
               this.iOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(LOG_TO_FILE), "US-ASCII"));
            } catch (FileNotFoundException var4) {
               logger.debug("Unable to open " + LOG_TO_FILE);
               logger.error((String)var4.getMessage(), (Throwable)var4);
               throw new CompilationDeathException(0);
            } catch (UnsupportedEncodingException var5) {
               logger.debug("This system doesn't support US-ASCII encoding!!");
               logger.error((String)var5.getMessage(), (Throwable)var5);
               throw new CompilationDeathException(0);
            }
         }

         try {
            this.iOut.write(s);
            this.iOut.write("\n");
            this.iOut.flush();
         } catch (IOException var3) {
            logger.debug("Unable to write to " + LOG_TO_FILE);
            logger.error((String)var3.getMessage(), (Throwable)var3);
            throw new CompilationDeathException(0);
         }
      }

   }
}
