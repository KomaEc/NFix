package org.apache.maven.toolchain.java;

import hidden.org.codehaus.plexus.util.FileUtils;
import java.io.File;
import org.apache.maven.toolchain.MisconfiguredToolchainException;
import org.apache.maven.toolchain.RequirementMatcherFactory;
import org.apache.maven.toolchain.ToolchainFactory;
import org.apache.maven.toolchain.ToolchainPrivate;
import org.apache.maven.toolchain.model.ToolchainModel;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public class DefaultJavaToolchainFactory implements ToolchainFactory, LogEnabled {
   private Logger logger;

   public ToolchainPrivate createToolchain(ToolchainModel model) throws MisconfiguredToolchainException {
      if (model == null) {
         return null;
      } else {
         DefaultJavaToolChain jtc = new DefaultJavaToolChain(model, this.logger);
         Xpp3Dom dom = (Xpp3Dom)model.getConfiguration();
         Xpp3Dom javahome = dom.getChild("jdkHome");
         if (javahome == null) {
            throw new MisconfiguredToolchainException("Java toolchain without the jdkHome configuration element.");
         } else {
            File normal = new File(FileUtils.normalize(javahome.getValue()));
            if (normal.exists()) {
               jtc.setJavaHome(FileUtils.normalize(javahome.getValue()));
               dom = (Xpp3Dom)model.getProvides();
               Xpp3Dom[] provides = dom.getChildren();

               for(int i = 0; i < provides.length; ++i) {
                  String key = provides[i].getName();
                  String value = provides[i].getValue();
                  if (value == null) {
                     throw new MisconfiguredToolchainException("Provides token '" + key + "' doesn't have any value configured.");
                  }

                  if ("version".equals(key)) {
                     jtc.addProvideToken(key, RequirementMatcherFactory.createVersionMatcher(value));
                  } else {
                     jtc.addProvideToken(key, RequirementMatcherFactory.createExactMatcher(value));
                  }
               }

               return jtc;
            } else {
               throw new MisconfiguredToolchainException("Non-existing JDK home configuration at " + normal.getAbsolutePath());
            }
         }
      }
   }

   public ToolchainPrivate createDefaultToolchain() {
      return null;
   }

   protected Logger getLogger() {
      return this.logger;
   }

   public void enableLogging(Logger logger) {
      this.logger = logger;
   }
}
