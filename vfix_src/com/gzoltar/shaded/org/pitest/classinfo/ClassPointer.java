package com.gzoltar.shaded.org.pitest.classinfo;

import com.gzoltar.shaded.org.pitest.functional.Option;

interface ClassPointer {
   Option<ClassInfo> fetch();
}
