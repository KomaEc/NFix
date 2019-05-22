package com.gzoltar.shaded.org.pitest.reloc.xstream.mapper;

public class OuterClassMapper extends MapperWrapper {
   private final String alias;

   public OuterClassMapper(Mapper wrapped) {
      this(wrapped, "outer-class");
   }

   public OuterClassMapper(Mapper wrapped, String alias) {
      super(wrapped);
      this.alias = alias;
   }

   public String serializedMember(Class type, String memberName) {
      return memberName.equals("this$0") ? this.alias : super.serializedMember(type, memberName);
   }

   public String realMember(Class type, String serialized) {
      return serialized.equals(this.alias) ? "this$0" : super.realMember(type, serialized);
   }
}
