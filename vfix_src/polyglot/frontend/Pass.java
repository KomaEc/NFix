package polyglot.frontend;

import polyglot.util.Enum;

public interface Pass {
   Pass.ID PARSE = new Pass.ID("parse");
   Pass.ID BUILD_TYPES = new Pass.ID("build-types");
   Pass.ID BUILD_TYPES_ALL = new Pass.ID("build-types-barrier");
   Pass.ID CLEAN_SUPER = new Pass.ID("clean-super");
   Pass.ID CLEAN_SUPER_ALL = new Pass.ID("clean-super-barrier");
   Pass.ID CLEAN_SIGS = new Pass.ID("clean-sigs");
   Pass.ID ADD_MEMBERS = new Pass.ID("add-members");
   Pass.ID ADD_MEMBERS_ALL = new Pass.ID("add-members-barrier");
   Pass.ID DISAM = new Pass.ID("disam");
   Pass.ID DISAM_ALL = new Pass.ID("disam-barrier");
   Pass.ID TYPE_CHECK = new Pass.ID("type-check");
   Pass.ID SET_EXPECTED_TYPES = new Pass.ID("set-expected-types");
   Pass.ID EXC_CHECK = new Pass.ID("exc-check");
   Pass.ID FOLD = new Pass.ID("fold");
   Pass.ID INIT_CHECK = new Pass.ID("init-check");
   Pass.ID CONSTRUCTOR_CHECK = new Pass.ID("constructor-check");
   Pass.ID FWD_REF_CHECK = new Pass.ID("fwd-reference-check");
   Pass.ID REACH_CHECK = new Pass.ID("reach-check");
   Pass.ID EXIT_CHECK = new Pass.ID("exit-check");
   Pass.ID DUMP = new Pass.ID("dump");
   Pass.ID PRE_OUTPUT_ALL = new Pass.ID("pre-output-barrier");
   Pass.ID SERIALIZE = new Pass.ID("serialization");
   Pass.ID OUTPUT = new Pass.ID("output");
   Pass.ID FIRST_BARRIER = BUILD_TYPES_ALL;

   Pass.ID id();

   String name();

   boolean run();

   void resetTimers();

   void toggleTimers(boolean var1);

   long inclusiveTime();

   long exclusiveTime();

   public static class ID extends Enum {
      public ID(String name) {
         super(name);
      }
   }
}
