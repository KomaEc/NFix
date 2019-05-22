package soot.jimple.parser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Scene;
import soot.SootClass;
import soot.SootResolver;
import soot.jimple.parser.lexer.Lexer;
import soot.jimple.parser.lexer.LexerException;
import soot.jimple.parser.node.Start;
import soot.jimple.parser.node.Switch;
import soot.jimple.parser.parser.Parser;
import soot.jimple.parser.parser.ParserException;
import soot.util.EscapedReader;

/** @deprecated */
@Deprecated
public class Parse {
   private static final Logger logger = LoggerFactory.getLogger(Parse.class);
   private static final String EXT = ".jimple";
   private static final String USAGE = "usage: java Parse [options] jimple_file [jimple_file ...]";

   public static SootClass parse(InputStream istream, SootClass sc) {
      Start tree = null;
      Parser p = new Parser(new Lexer(new PushbackReader(new EscapedReader(new BufferedReader(new InputStreamReader(istream))), 1024)));

      try {
         tree = p.parse();
      } catch (ParserException var5) {
         throw new RuntimeException("Parser exception occurred: " + var5);
      } catch (LexerException var6) {
         throw new RuntimeException("Lexer exception occurred: " + var6);
      } catch (IOException var7) {
         throw new RuntimeException("IOException occurred: " + var7);
      }

      Object w;
      if (sc == null) {
         w = new Walker((SootResolver)null);
      } else {
         w = new BodyExtractorWalker(sc, (SootResolver)null, new HashMap());
      }

      tree.apply((Switch)w);
      return ((Walker)w).getSootClass();
   }

   public static void main(String[] args) throws Exception {
      boolean verbose = false;
      if (args.length < 1) {
         logger.debug("usage: java Parse [options] jimple_file [jimple_file ...]");
         System.exit(0);
      }

      Scene.v().setPhantomRefs(true);
      String[] var3 = args;
      int var4 = args.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String arg = var3[var5];
         if (arg.startsWith("-")) {
            arg = arg.substring(1);
            if (!arg.equals("d") && arg.equals("v")) {
               verbose = true;
            }
         } else {
            Object inFile;
            try {
               if (verbose) {
                  logger.debug(" ... looking for " + arg);
               }

               inFile = new FileInputStream(arg);
            } catch (FileNotFoundException var10) {
               if (arg.endsWith(".jimple")) {
                  logger.debug(" *** can't find " + arg);
                  continue;
               }

               arg = arg + ".jimple";

               try {
                  if (verbose) {
                     logger.debug(" ... looking for " + arg);
                  }

                  inFile = new BufferedInputStream(new FileInputStream(arg));
               } catch (FileNotFoundException var9) {
                  logger.debug(" *** can't find " + arg);
                  continue;
               }
            }

            Parser p = new Parser(new Lexer(new PushbackReader(new InputStreamReader((InputStream)inFile), 1024)));
            Start tree = p.parse();
            tree.apply(new Walker((SootResolver)null));
         }
      }

   }
}
