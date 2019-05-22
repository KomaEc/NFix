package org.apache.maven.toolchain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.maven.toolchain.model.ToolchainModel;
import org.codehaus.plexus.logging.Logger;

public abstract class DefaultToolchain implements Toolchain, ToolchainPrivate {
   private String type;
   private Map provides;
   public static final String KEY_TYPE = "type";
   private ToolchainModel model;
   private Logger logger;

   protected DefaultToolchain(ToolchainModel model, Logger logger) {
      this.provides = new HashMap();
      this.model = model;
      this.logger = logger;
   }

   protected DefaultToolchain(ToolchainModel model, String type, Logger logger) {
      this(model, logger);
      this.type = type;
   }

   public final String getType() {
      return this.type != null ? this.type : this.model.getType();
   }

   public final ToolchainModel getModel() {
      return this.model;
   }

   public final void addProvideToken(String type, RequirementMatcher matcher) {
      this.provides.put(type, matcher);
   }

   public boolean matchesRequirements(Map requirements) {
      Iterator it = requirements.keySet().iterator();

      String key;
      RequirementMatcher matcher;
      do {
         if (!it.hasNext()) {
            return true;
         }

         key = (String)it.next();
         matcher = (RequirementMatcher)this.provides.get(key);
         if (matcher == null) {
            this.getLog().debug("Toolchain " + this + " is missing required property: " + key);
            return false;
         }
      } while(matcher.matches((String)requirements.get(key)));

      this.getLog().debug("Toolchain " + this + " doesn't match required property: " + key);
      return false;
   }

   protected Logger getLog() {
      return this.logger;
   }
}
