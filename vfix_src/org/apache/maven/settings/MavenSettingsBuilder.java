package org.apache.maven.settings;

import java.io.File;
import java.io.IOException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public interface MavenSettingsBuilder {
   String ROLE = MavenSettingsBuilder.class.getName();
   String ALT_USER_SETTINGS_XML_LOCATION = "org.apache.maven.user-settings";
   String ALT_GLOBAL_SETTINGS_XML_LOCATION = "org.apache.maven.global-settings";
   String ALT_LOCAL_REPOSITORY_LOCATION = "maven.repo.local";

   Settings buildSettings() throws IOException, XmlPullParserException;

   Settings buildSettings(boolean var1) throws IOException, XmlPullParserException;

   Settings buildSettings(File var1) throws IOException, XmlPullParserException;

   Settings buildSettings(File var1, boolean var2) throws IOException, XmlPullParserException;
}
