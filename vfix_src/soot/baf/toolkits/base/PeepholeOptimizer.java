package soot.baf.toolkits.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;

public class PeepholeOptimizer extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(PeepholeOptimizer.class);
   private final String packageName = "soot.baf.toolkits.base";
   private static boolean peepholesLoaded = false;
   private static final Object loaderLock = new Object();
   private final Map<String, Class<?>> peepholeMap = new HashMap();

   public PeepholeOptimizer(Singletons.Global g) {
   }

   public static PeepholeOptimizer v() {
      return G.v().soot_baf_toolkits_base_PeepholeOptimizer();
   }

   protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
      LinkedList peepholes;
      if (!peepholesLoaded) {
         synchronized(loaderLock) {
            if (!peepholesLoaded) {
               peepholesLoaded = true;
               InputStream peepholeListingStream = null;
               peepholeListingStream = PeepholeOptimizer.class.getResourceAsStream("/peephole.dat");
               if (peepholeListingStream == null) {
                  throw new RuntimeException("could not open file peephole.dat!");
               }

               BufferedReader reader = new BufferedReader(new InputStreamReader(peepholeListingStream));
               String line = null;
               peepholes = new LinkedList();

               try {
                  for(line = reader.readLine(); line != null; line = reader.readLine()) {
                     if (line.length() > 0 && line.charAt(0) != '#') {
                        peepholes.add(line);
                     }
                  }
               } catch (IOException var18) {
                  throw new RuntimeException("IO error occured while reading file:  " + line + System.getProperty("line.separator") + var18);
               }

               try {
                  reader.close();
                  peepholeListingStream.close();
               } catch (IOException var17) {
                  logger.debug((String)var17.getMessage(), (Throwable)var17);
               }

               Iterator var9 = peepholes.iterator();

               while(var9.hasNext()) {
                  String peepholeName = (String)var9.next();
                  if ((Class)this.peepholeMap.get(peepholeName) == null) {
                     Class peepholeClass;
                     try {
                        peepholeClass = Class.forName("soot.baf.toolkits.base." + peepholeName);
                     } catch (ClassNotFoundException var16) {
                        throw new RuntimeException(var16.toString());
                     }

                     this.peepholeMap.put(peepholeName, peepholeClass);
                  }
               }
            }
         }
      }

      boolean changed = true;

      while(changed) {
         changed = false;
         Iterator var20 = this.peepholeMap.keySet().iterator();

         while(var20.hasNext()) {
            String peepholeName = (String)var20.next();
            boolean peepholeWorked = true;

            while(peepholeWorked) {
               peepholeWorked = false;
               peepholes = null;

               Peephole p;
               try {
                  p = (Peephole)((Class)this.peepholeMap.get(peepholeName)).newInstance();
               } catch (IllegalAccessException var14) {
                  throw new RuntimeException(var14.toString());
               } catch (InstantiationException var15) {
                  throw new RuntimeException(var15.toString());
               }

               if (p.apply(body)) {
                  peepholeWorked = true;
                  changed = true;
               }
            }
         }
      }

   }
}
