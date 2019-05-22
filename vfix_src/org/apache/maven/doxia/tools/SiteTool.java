package org.apache.maven.doxia.tools;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.doxia.site.decoration.DecorationModel;
import org.apache.maven.project.MavenProject;

public interface SiteTool {
   String ROLE = SiteTool.class.getName();
   Locale DEFAULT_LOCALE = Locale.ENGLISH;

   Artifact getSkinArtifactFromRepository(ArtifactRepository var1, List var2, DecorationModel var3) throws SiteToolException;

   Artifact getDefaultSkinArtifact(ArtifactRepository var1, List var2) throws SiteToolException;

   String getRelativePath(String var1, String var2);

   File getSiteDescriptorFromBasedir(String var1, File var2, Locale var3);

   File getSiteDescriptorFromRepository(MavenProject var1, ArtifactRepository var2, List var3, Locale var4) throws SiteToolException;

   DecorationModel getDecorationModel(MavenProject var1, List var2, ArtifactRepository var3, List var4, String var5, Locale var6, String var7, String var8) throws SiteToolException;

   void populateReportsMenu(DecorationModel var1, Locale var2, Map var3);

   String getInterpolatedSiteDescriptorContent(Map var1, MavenProject var2, String var3, String var4, String var5) throws SiteToolException;

   MavenProject getParentProject(MavenProject var1, List var2, ArtifactRepository var3);

   void populateParentMenu(DecorationModel var1, Locale var2, MavenProject var3, MavenProject var4, boolean var5);

   /** @deprecated */
   void populateProjectParentMenu(DecorationModel var1, Locale var2, MavenProject var3, MavenProject var4, boolean var5);

   /** @deprecated */
   void populateModules(MavenProject var1, List var2, ArtifactRepository var3, DecorationModel var4, Locale var5, boolean var6) throws SiteToolException;

   void populateModulesMenu(MavenProject var1, List var2, ArtifactRepository var3, DecorationModel var4, Locale var5, boolean var6) throws SiteToolException;

   List getAvailableLocales(String var1);

   Locale codeToLocale(String var1);
}
