package org.apache.maven.profiles;

import java.io.File;
import java.io.IOException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public interface MavenProfilesBuilder {
   String ROLE = MavenProfilesBuilder.class.getName();

   ProfilesRoot buildProfiles(File var1) throws IOException, XmlPullParserException;
}
