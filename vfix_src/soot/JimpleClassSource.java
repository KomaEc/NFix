package soot;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.javaToJimple.IInitialResolver;
import soot.jimple.JimpleMethodSource;
import soot.jimple.parser.JimpleAST;
import soot.jimple.parser.lexer.LexerException;
import soot.jimple.parser.parser.ParserException;
import soot.options.Options;

public class JimpleClassSource extends ClassSource {
   private static final Logger logger = LoggerFactory.getLogger(JimpleClassSource.class);
   private FoundFile foundFile;

   public JimpleClassSource(String className, FoundFile foundFile) {
      super(className);
      if (foundFile == null) {
         throw new IllegalStateException("Error: The FoundFile must not be null.");
      } else {
         this.foundFile = foundFile;
      }
   }

   public IInitialResolver.Dependencies resolve(SootClass sc) {
      if (Options.v().verbose()) {
         logger.debug("resolving [from .jimple]: " + this.className);
      }

      InputStream classFile = null;

      IInitialResolver.Dependencies var46;
      try {
         classFile = this.foundFile.inputStream();
         JimpleAST jimpAST = new JimpleAST(classFile);
         jimpAST.getSkeleton(sc);
         JimpleMethodSource mtdSrc = new JimpleMethodSource(jimpAST);
         Iterator mtdIt = sc.methodIterator();

         while(mtdIt.hasNext()) {
            SootMethod sm = (SootMethod)mtdIt.next();
            sm.setSource(mtdSrc);
         }

         String outerClassName = null;
         String className = sc.getName();
         if (!sc.hasOuterClass() && className.contains("$")) {
            if (className.contains("$-")) {
               outerClassName = className.substring(0, className.indexOf("$-"));
            } else {
               outerClassName = className.substring(0, className.lastIndexOf(36));
            }

            sc.setOuterClass(SootResolver.v().makeClassRef(outerClassName));
         }

         IInitialResolver.Dependencies deps = new IInitialResolver.Dependencies();
         Iterator var9 = jimpAST.getCstPool().iterator();

         while(var9.hasNext()) {
            String t = (String)var9.next();
            deps.typesToSignature.add(RefType.v(t));
         }

         if (outerClassName != null) {
            deps.typesToSignature.add(RefType.v(outerClassName));
         }

         var46 = deps;
      } catch (IOException var41) {
         throw new RuntimeException("Error: Failed to create JimpleAST from source input stream for class " + this.className + ".", var41);
      } catch (ParserException var42) {
         throw new RuntimeException("Error: Failed when parsing class " + this.className + ".", var42);
      } catch (LexerException var43) {
         throw new RuntimeException("Error: Failed when lexing class " + this.className + ".", var43);
      } finally {
         try {
            if (classFile != null) {
               classFile.close();
               classFile = null;
            }
         } catch (IOException var39) {
            throw new RuntimeException("Error: Failed to close source input stream.", var39);
         } finally {
            this.close();
         }

      }

      return var46;
   }

   public void close() {
      if (this.foundFile != null) {
         this.foundFile.close();
         this.foundFile = null;
      }

   }
}
