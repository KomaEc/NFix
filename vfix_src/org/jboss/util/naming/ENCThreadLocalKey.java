package org.jboss.util.naming;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.LinkRef;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import org.jboss.logging.Logger;

public class ENCThreadLocalKey implements ObjectFactory {
   private static final Logger log = Logger.getLogger(ENCThreadLocalKey.class);
   private static final ThreadLocal key = new ThreadLocal();

   public static void setKey(String tlkey) {
      key.set(tlkey);
   }

   public static String getKey() {
      return (String)key.get();
   }

   public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws Exception {
      Reference ref = (Reference)obj;
      String reftype = (String)key.get();
      boolean trace = log.isTraceEnabled();
      if (reftype == null) {
         if (trace) {
            log.trace("using default in ENC");
         }

         reftype = "default";
      }

      RefAddr addr = ref.get(reftype);
      if (addr == null) {
         if (trace) {
            log.trace("using default in ENC");
         }

         addr = ref.get("default");
      }

      if (addr != null) {
         String target = (String)addr.getContent();
         if (trace) {
            log.trace("found Reference " + reftype + " with content " + target);
         }

         return new LinkRef(target);
      } else {
         return null;
      }
   }
}
