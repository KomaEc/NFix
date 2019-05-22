package com.gzoltar.shaded.org.pitest.reloc.xstream.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ExplicitTypePermission implements TypePermission {
   final Set names;

   public ExplicitTypePermission(final Class[] types) {
      this(((<undefinedtype>)(new Object() {
         public String[] getNames() {
            if (types == null) {
               return null;
            } else {
               String[] names = new String[types.length];

               for(int i = 0; i < types.length; ++i) {
                  names[i] = types[i].getName();
               }

               return names;
            }
         }
      })).getNames());
   }

   public ExplicitTypePermission(String[] names) {
      this.names = (Set)(names == null ? Collections.EMPTY_SET : new HashSet(Arrays.asList(names)));
   }

   public boolean allows(Class type) {
      return type == null ? false : this.names.contains(type.getName());
   }
}
