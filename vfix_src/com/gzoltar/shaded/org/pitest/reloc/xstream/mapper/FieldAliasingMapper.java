package com.gzoltar.shaded.org.pitest.reloc.xstream.mapper;

import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.FastField;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class FieldAliasingMapper extends MapperWrapper {
   protected final Map fieldToAliasMap = new HashMap();
   protected final Map aliasToFieldMap = new HashMap();
   protected final Set fieldsToOmit = new HashSet();
   protected final Set unknownFieldsToIgnore = new LinkedHashSet();

   public FieldAliasingMapper(Mapper wrapped) {
      super(wrapped);
   }

   public void addFieldAlias(String alias, Class type, String fieldName) {
      this.fieldToAliasMap.put(this.key(type, fieldName), alias);
      this.aliasToFieldMap.put(this.key(type, alias), fieldName);
   }

   public void addFieldsToIgnore(Pattern pattern) {
      this.unknownFieldsToIgnore.add(pattern);
   }

   private Object key(Class type, String name) {
      return new FastField(type, name);
   }

   public String serializedMember(Class type, String memberName) {
      String alias = this.getMember(type, memberName, this.fieldToAliasMap);
      return alias == null ? super.serializedMember(type, memberName) : alias;
   }

   public String realMember(Class type, String serialized) {
      String real = this.getMember(type, serialized, this.aliasToFieldMap);
      return real == null ? super.realMember(type, serialized) : real;
   }

   private String getMember(Class type, String name, Map map) {
      String member = null;

      for(Class declaringType = type; member == null && declaringType != Object.class && declaringType != null; declaringType = declaringType.getSuperclass()) {
         member = (String)map.get(this.key(declaringType, name));
      }

      return member;
   }

   public boolean shouldSerializeMember(Class definedIn, String fieldName) {
      if (this.fieldsToOmit.contains(this.key(definedIn, fieldName))) {
         return false;
      } else {
         if (definedIn == Object.class && !this.unknownFieldsToIgnore.isEmpty()) {
            Iterator iter = this.unknownFieldsToIgnore.iterator();

            while(iter.hasNext()) {
               Pattern pattern = (Pattern)iter.next();
               if (pattern.matcher(fieldName).matches()) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public void omitField(Class definedIn, String fieldName) {
      this.fieldsToOmit.add(this.key(definedIn, fieldName));
   }
}
