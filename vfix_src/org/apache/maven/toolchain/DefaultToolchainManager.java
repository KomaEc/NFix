package org.apache.maven.toolchain;

import hidden.org.codehaus.plexus.util.IOUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.project.MavenProject;
import org.apache.maven.toolchain.model.PersistedToolchains;
import org.apache.maven.toolchain.model.ToolchainModel;
import org.apache.maven.toolchain.model.io.xpp3.MavenToolchainsXpp3Reader;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;

public class DefaultToolchainManager extends AbstractLogEnabled implements ToolchainManager, ToolchainManagerPrivate, Contextualizable {
   private PlexusContainer container;

   public void contextualize(Context context) throws ContextException {
      this.container = (PlexusContainer)context.get("plexus");
   }

   public ToolchainPrivate[] getToolchainsForType(String type) throws MisconfiguredToolchainException {
      try {
         PersistedToolchains pers = this.readToolchainSettings();
         Map factories = this.container.lookupMap(ToolchainFactory.ROLE);
         List toRet = new ArrayList();
         if (pers != null) {
            List lst = pers.getToolchains();
            if (lst != null) {
               Iterator it = lst.iterator();

               while(it.hasNext()) {
                  ToolchainModel toolchainModel = (ToolchainModel)it.next();
                  ToolchainFactory fact = (ToolchainFactory)factories.get(toolchainModel.getType());
                  if (fact != null) {
                     toRet.add(fact.createToolchain(toolchainModel));
                  } else {
                     this.getLogger().error("Missing toolchain factory for type:" + toolchainModel.getType() + ". Possibly caused by misconfigured project.");
                  }
               }
            }
         }

         Iterator it = factories.values().iterator();

         while(it.hasNext()) {
            ToolchainFactory fact = (ToolchainFactory)it.next();
            ToolchainPrivate tool = fact.createDefaultToolchain();
            if (tool != null) {
               toRet.add(tool);
            }
         }

         ToolchainPrivate[] tc = new ToolchainPrivate[toRet.size()];
         return (ToolchainPrivate[])((ToolchainPrivate[])toRet.toArray(tc));
      } catch (ComponentLookupException var9) {
         this.getLogger().fatalError("Error in component lookup", var9);
         return new ToolchainPrivate[0];
      }
   }

   public Toolchain getToolchainFromBuildContext(String type, MavenSession session) {
      Map context = this.retrieveContext(session);
      if ("javac".equals(type)) {
         type = "jdk";
      }

      Object obj = context.get(getStorageKey(type));
      ToolchainModel model = (ToolchainModel)obj;
      if (model != null) {
         try {
            ToolchainFactory fact = (ToolchainFactory)this.container.lookup(ToolchainFactory.ROLE, type);
            return fact.createToolchain(model);
         } catch (ComponentLookupException var7) {
            this.getLogger().fatalError("Error in component lookup", var7);
         } catch (MisconfiguredToolchainException var8) {
            this.getLogger().error("Misconfigured toolchain.", var8);
         }
      }

      return null;
   }

   private MavenProject getCurrentProject(MavenSession session) {
      try {
         Method meth = session.getClass().getMethod("getCurrentProject");
         return (MavenProject)meth.invoke(session, (Object[])null);
      } catch (Exception var3) {
         return null;
      }
   }

   private Map retrieveContext(MavenSession session) {
      if (session == null) {
         return new HashMap();
      } else {
         PluginDescriptor desc = new PluginDescriptor();
         desc.setGroupId(PluginDescriptor.getDefaultPluginGroupId());
         desc.setArtifactId(PluginDescriptor.getDefaultPluginArtifactId("toolchains"));
         MavenProject current = this.getCurrentProject(session);
         return (Map)(current != null ? session.getPluginContext(desc, current) : new HashMap());
      }
   }

   public void storeToolchainToBuildContext(ToolchainPrivate toolchain, MavenSession session) {
      Map context = this.retrieveContext(session);
      context.put(getStorageKey(toolchain.getType()), toolchain.getModel());
   }

   public static final String getStorageKey(String type) {
      return "toolchain-" + type;
   }

   private PersistedToolchains readToolchainSettings() throws MisconfiguredToolchainException {
      File tch = new File(System.getProperty("user.home"), ".m2/toolchains.xml");
      if (tch.exists()) {
         MavenToolchainsXpp3Reader reader = new MavenToolchainsXpp3Reader();
         InputStreamReader in = null;

         PersistedToolchains var4;
         try {
            in = new InputStreamReader(new BufferedInputStream(new FileInputStream(tch)));
            var4 = reader.read((Reader)in);
         } catch (Exception var8) {
            throw new MisconfiguredToolchainException("Cannot read toolchains file at " + tch.getAbsolutePath(), var8);
         } finally {
            IOUtil.close(in);
         }

         return var4;
      } else {
         return null;
      }
   }
}
