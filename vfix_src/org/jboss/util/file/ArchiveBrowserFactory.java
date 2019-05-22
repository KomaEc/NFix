package org.jboss.util.file;

import java.net.URL;
import java.util.Iterator;

public interface ArchiveBrowserFactory {
   Iterator create(URL var1, ArchiveBrowser.Filter var2);
}
