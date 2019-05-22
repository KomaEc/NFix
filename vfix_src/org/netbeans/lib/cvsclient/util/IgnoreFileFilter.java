package org.netbeans.lib.cvsclient.util;

import java.io.File;

public interface IgnoreFileFilter {
   boolean shouldBeIgnored(File var1, String var2);
}
