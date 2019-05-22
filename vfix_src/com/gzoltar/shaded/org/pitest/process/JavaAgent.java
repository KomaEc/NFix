package com.gzoltar.shaded.org.pitest.process;

import com.gzoltar.shaded.org.pitest.functional.Option;

public interface JavaAgent {
   Option<String> getJarLocation();

   void close();
}
