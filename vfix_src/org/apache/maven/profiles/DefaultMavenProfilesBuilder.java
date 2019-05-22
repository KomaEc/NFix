package org.apache.maven.profiles;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import org.apache.maven.profiles.io.xpp3.ProfilesXpp3Reader;
import org.codehaus.plexus.interpolation.EnvarBasedValueSource;
import org.codehaus.plexus.interpolation.RegexBasedInterpolator;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.XmlStreamReader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class DefaultMavenProfilesBuilder extends AbstractLogEnabled implements MavenProfilesBuilder {
   private static final String PROFILES_XML_FILE = "profiles.xml";

   public ProfilesRoot buildProfiles(File basedir) throws IOException, XmlPullParserException {
      File profilesXml = new File(basedir, "profiles.xml");
      ProfilesRoot profilesRoot = null;
      if (profilesXml.exists()) {
         ProfilesXpp3Reader reader = new ProfilesXpp3Reader();
         XmlStreamReader profileReader = null;

         try {
            profileReader = ReaderFactory.newXmlReader(profilesXml);
            StringWriter sWriter = new StringWriter();
            IOUtil.copy((Reader)profileReader, (Writer)sWriter);
            String rawInput = sWriter.toString();

            try {
               RegexBasedInterpolator interpolator = new RegexBasedInterpolator();
               interpolator.addValueSource(new EnvarBasedValueSource());
               rawInput = interpolator.interpolate(rawInput, "settings");
            } catch (Exception var12) {
               this.getLogger().warn("Failed to initialize environment variable resolver. Skipping environment substitution in profiles.xml.");
               this.getLogger().debug("Failed to initialize envar resolver. Skipping resolution.", var12);
            }

            StringReader sReader = new StringReader(rawInput);
            profilesRoot = reader.read((Reader)sReader);
         } finally {
            IOUtil.close((Reader)profileReader);
         }
      }

      return profilesRoot;
   }
}
