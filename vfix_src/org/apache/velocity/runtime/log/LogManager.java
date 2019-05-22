package org.apache.velocity.runtime.log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.util.ClassUtils;

public class LogManager {
   private static LogChute createLogChute(RuntimeServices rsvc) throws Exception {
      Log log = rsvc.getLog();
      Object o = rsvc.getProperty("runtime.log.logsystem");
      if (o != null) {
         if (o instanceof LogChute) {
            try {
               ((LogChute)o).init(rsvc);
               return (LogChute)o;
            } catch (Exception var11) {
               log.error("Could not init runtime.log.logsystem " + o, var11);
            }
         } else if (o instanceof LogSystem) {
            log.info("LogSystem has been deprecated. Please use a LogChute implementation.");

            try {
               LogChute chute = new LogChuteSystem((LogSystem)o);
               chute.init(rsvc);
               return chute;
            } catch (Exception var10) {
               log.error("Could not init runtime.log.logsystem " + o, var10);
            }
         } else {
            log.warn(o.getClass().getName() + " object set as runtime.log.logsystem is not a valid log implementation.");
         }
      }

      List classes = new ArrayList();
      Object obj = rsvc.getProperty("runtime.log.logsystem.class");
      if (obj instanceof List) {
         classes = (List)obj;
      } else if (obj instanceof String) {
         ((List)classes).add(obj);
      }

      Iterator ii = ((List)classes).iterator();

      while(ii.hasNext()) {
         String claz = (String)ii.next();
         if (claz != null && claz.length() > 0) {
            log.debug("Trying to use logger class " + claz);

            try {
               o = ClassUtils.getNewInstance(claz);
               if (o instanceof LogChute) {
                  ((LogChute)o).init(rsvc);
                  log.debug("Using logger class " + claz);
                  return (LogChute)o;
               }

               if (o instanceof LogSystem) {
                  log.info("LogSystem has been deprecated. Please use a LogChute implementation.");
                  LogChute chute = new LogChuteSystem((LogSystem)o);
                  chute.init(rsvc);
                  return chute;
               }

               log.error("The specifid logger class " + claz + " isn't a valid LogChute implementation.");
            } catch (NoClassDefFoundError var8) {
               log.debug("Couldn't find class " + claz + " or necessary supporting classes in classpath.", var8);
            } catch (Exception var9) {
               log.info("Failed to initialize an instance of " + claz + " with the current runtime configuration.", var9);
            }
         }
      }

      LogChute slc = new SystemLogChute();
      slc.init(rsvc);
      log.debug("Using SystemLogChute.");
      return slc;
   }

   public static void updateLog(Log log, RuntimeServices rsvc) throws Exception {
      LogChute newLogChute = createLogChute(rsvc);
      LogChute oldLogChute = log.getLogChute();
      if (oldLogChute instanceof HoldingLogChute) {
         HoldingLogChute hlc = (HoldingLogChute)oldLogChute;
         hlc.transferTo(newLogChute);
      }

      log.setLogChute(newLogChute);
   }
}
