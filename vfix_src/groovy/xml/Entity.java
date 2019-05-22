package groovy.xml;

import groovy.lang.Buildable;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class Entity implements Buildable, GroovyObject {
   public static final Entity nbsp = (Entity)$getCallSiteArray()[2].callConstructor($get$$class$groovy$xml$Entity(), (Object)"nbsp");
   public static final Entity iexcl = (Entity)$getCallSiteArray()[3].callConstructor($get$$class$groovy$xml$Entity(), (Object)"iexcl");
   public static final Entity cent = (Entity)$getCallSiteArray()[4].callConstructor($get$$class$groovy$xml$Entity(), (Object)"cent");
   public static final Entity pound = (Entity)$getCallSiteArray()[5].callConstructor($get$$class$groovy$xml$Entity(), (Object)"pound");
   public static final Entity curren = (Entity)$getCallSiteArray()[6].callConstructor($get$$class$groovy$xml$Entity(), (Object)"curren");
   public static final Entity yen = (Entity)$getCallSiteArray()[7].callConstructor($get$$class$groovy$xml$Entity(), (Object)"yen");
   public static final Entity brvbar = (Entity)$getCallSiteArray()[8].callConstructor($get$$class$groovy$xml$Entity(), (Object)"brvbar");
   public static final Entity sect = (Entity)$getCallSiteArray()[9].callConstructor($get$$class$groovy$xml$Entity(), (Object)"sect");
   public static final Entity uml = (Entity)$getCallSiteArray()[10].callConstructor($get$$class$groovy$xml$Entity(), (Object)"uml");
   public static final Entity copy = (Entity)$getCallSiteArray()[11].callConstructor($get$$class$groovy$xml$Entity(), (Object)"copy");
   public static final Entity ordf = (Entity)$getCallSiteArray()[12].callConstructor($get$$class$groovy$xml$Entity(), (Object)"ordf");
   public static final Entity laquo = (Entity)$getCallSiteArray()[13].callConstructor($get$$class$groovy$xml$Entity(), (Object)"laquo");
   public static final Entity not = (Entity)$getCallSiteArray()[14].callConstructor($get$$class$groovy$xml$Entity(), (Object)"not");
   public static final Entity shy = (Entity)$getCallSiteArray()[15].callConstructor($get$$class$groovy$xml$Entity(), (Object)"shy");
   public static final Entity reg = (Entity)$getCallSiteArray()[16].callConstructor($get$$class$groovy$xml$Entity(), (Object)"reg");
   public static final Entity macr = (Entity)$getCallSiteArray()[17].callConstructor($get$$class$groovy$xml$Entity(), (Object)"macr");
   public static final Entity deg = (Entity)$getCallSiteArray()[18].callConstructor($get$$class$groovy$xml$Entity(), (Object)"deg");
   public static final Entity plusmn = (Entity)$getCallSiteArray()[19].callConstructor($get$$class$groovy$xml$Entity(), (Object)"plusmn");
   public static final Entity sup2 = (Entity)$getCallSiteArray()[20].callConstructor($get$$class$groovy$xml$Entity(), (Object)"sup2");
   public static final Entity sup3 = (Entity)$getCallSiteArray()[21].callConstructor($get$$class$groovy$xml$Entity(), (Object)"sup3");
   public static final Entity acute = (Entity)$getCallSiteArray()[22].callConstructor($get$$class$groovy$xml$Entity(), (Object)"acute");
   public static final Entity micro = (Entity)$getCallSiteArray()[23].callConstructor($get$$class$groovy$xml$Entity(), (Object)"micro");
   public static final Entity para = (Entity)$getCallSiteArray()[24].callConstructor($get$$class$groovy$xml$Entity(), (Object)"para");
   public static final Entity middot = (Entity)$getCallSiteArray()[25].callConstructor($get$$class$groovy$xml$Entity(), (Object)"middot");
   public static final Entity cedil = (Entity)$getCallSiteArray()[26].callConstructor($get$$class$groovy$xml$Entity(), (Object)"cedil");
   public static final Entity sup1 = (Entity)$getCallSiteArray()[27].callConstructor($get$$class$groovy$xml$Entity(), (Object)"sup1");
   public static final Entity ordm = (Entity)$getCallSiteArray()[28].callConstructor($get$$class$groovy$xml$Entity(), (Object)"ordm");
   public static final Entity raquo = (Entity)$getCallSiteArray()[29].callConstructor($get$$class$groovy$xml$Entity(), (Object)"raquo");
   public static final Entity frac14 = (Entity)$getCallSiteArray()[30].callConstructor($get$$class$groovy$xml$Entity(), (Object)"frac14");
   public static final Entity frac12 = (Entity)$getCallSiteArray()[31].callConstructor($get$$class$groovy$xml$Entity(), (Object)"frac12");
   public static final Entity frac34 = (Entity)$getCallSiteArray()[32].callConstructor($get$$class$groovy$xml$Entity(), (Object)"frac34");
   public static final Entity iquest = (Entity)$getCallSiteArray()[33].callConstructor($get$$class$groovy$xml$Entity(), (Object)"iquest");
   public static final Entity Agrave = (Entity)$getCallSiteArray()[34].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Agrave");
   public static final Entity Aacute = (Entity)$getCallSiteArray()[35].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Aacute");
   public static final Entity Acirc = (Entity)$getCallSiteArray()[36].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Acirc");
   public static final Entity Atilde = (Entity)$getCallSiteArray()[37].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Atilde");
   public static final Entity Auml = (Entity)$getCallSiteArray()[38].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Auml");
   public static final Entity Aring = (Entity)$getCallSiteArray()[39].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Aring");
   public static final Entity AElig = (Entity)$getCallSiteArray()[40].callConstructor($get$$class$groovy$xml$Entity(), (Object)"AElig");
   public static final Entity Ccedil = (Entity)$getCallSiteArray()[41].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Ccedil");
   public static final Entity Egrave = (Entity)$getCallSiteArray()[42].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Egrave");
   public static final Entity Eacute = (Entity)$getCallSiteArray()[43].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Eacute");
   public static final Entity Ecirc = (Entity)$getCallSiteArray()[44].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Ecirc");
   public static final Entity Euml = (Entity)$getCallSiteArray()[45].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Euml");
   public static final Entity Igrave = (Entity)$getCallSiteArray()[46].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Igrave");
   public static final Entity Iacute = (Entity)$getCallSiteArray()[47].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Iacute");
   public static final Entity Icirc = (Entity)$getCallSiteArray()[48].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Icirc");
   public static final Entity Iuml = (Entity)$getCallSiteArray()[49].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Iuml");
   public static final Entity ETH = (Entity)$getCallSiteArray()[50].callConstructor($get$$class$groovy$xml$Entity(), (Object)"ETH");
   public static final Entity Ntilde = (Entity)$getCallSiteArray()[51].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Ntilde");
   public static final Entity Ograve = (Entity)$getCallSiteArray()[52].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Ograve");
   public static final Entity Oacute = (Entity)$getCallSiteArray()[53].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Oacute");
   public static final Entity Ocirc = (Entity)$getCallSiteArray()[54].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Ocirc");
   public static final Entity Otilde = (Entity)$getCallSiteArray()[55].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Otilde");
   public static final Entity Ouml = (Entity)$getCallSiteArray()[56].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Ouml");
   public static final Entity times = (Entity)$getCallSiteArray()[57].callConstructor($get$$class$groovy$xml$Entity(), (Object)"times");
   public static final Entity Oslash = (Entity)$getCallSiteArray()[58].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Oslash");
   public static final Entity Ugrave = (Entity)$getCallSiteArray()[59].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Ugrave");
   public static final Entity Uacute = (Entity)$getCallSiteArray()[60].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Uacute");
   public static final Entity Ucirc = (Entity)$getCallSiteArray()[61].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Ucirc");
   public static final Entity Uuml = (Entity)$getCallSiteArray()[62].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Uuml");
   public static final Entity Yacute = (Entity)$getCallSiteArray()[63].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Yacute");
   public static final Entity THORN = (Entity)$getCallSiteArray()[64].callConstructor($get$$class$groovy$xml$Entity(), (Object)"THORN");
   public static final Entity szlig = (Entity)$getCallSiteArray()[65].callConstructor($get$$class$groovy$xml$Entity(), (Object)"szlig");
   public static final Entity agrave = (Entity)$getCallSiteArray()[66].callConstructor($get$$class$groovy$xml$Entity(), (Object)"agrave");
   public static final Entity aacute = (Entity)$getCallSiteArray()[67].callConstructor($get$$class$groovy$xml$Entity(), (Object)"aacute");
   public static final Entity acirc = (Entity)$getCallSiteArray()[68].callConstructor($get$$class$groovy$xml$Entity(), (Object)"acirc");
   public static final Entity atilde = (Entity)$getCallSiteArray()[69].callConstructor($get$$class$groovy$xml$Entity(), (Object)"atilde");
   public static final Entity auml = (Entity)$getCallSiteArray()[70].callConstructor($get$$class$groovy$xml$Entity(), (Object)"auml");
   public static final Entity aring = (Entity)$getCallSiteArray()[71].callConstructor($get$$class$groovy$xml$Entity(), (Object)"aring");
   public static final Entity aelig = (Entity)$getCallSiteArray()[72].callConstructor($get$$class$groovy$xml$Entity(), (Object)"aelig");
   public static final Entity ccedil = (Entity)$getCallSiteArray()[73].callConstructor($get$$class$groovy$xml$Entity(), (Object)"ccedil");
   public static final Entity egrave = (Entity)$getCallSiteArray()[74].callConstructor($get$$class$groovy$xml$Entity(), (Object)"egrave");
   public static final Entity eacute = (Entity)$getCallSiteArray()[75].callConstructor($get$$class$groovy$xml$Entity(), (Object)"eacute");
   public static final Entity ecirc = (Entity)$getCallSiteArray()[76].callConstructor($get$$class$groovy$xml$Entity(), (Object)"ecirc");
   public static final Entity euml = (Entity)$getCallSiteArray()[77].callConstructor($get$$class$groovy$xml$Entity(), (Object)"euml");
   public static final Entity igrave = (Entity)$getCallSiteArray()[78].callConstructor($get$$class$groovy$xml$Entity(), (Object)"igrave");
   public static final Entity iacute = (Entity)$getCallSiteArray()[79].callConstructor($get$$class$groovy$xml$Entity(), (Object)"iacute");
   public static final Entity icirc = (Entity)$getCallSiteArray()[80].callConstructor($get$$class$groovy$xml$Entity(), (Object)"icirc");
   public static final Entity iuml = (Entity)$getCallSiteArray()[81].callConstructor($get$$class$groovy$xml$Entity(), (Object)"iuml");
   public static final Entity eth = (Entity)$getCallSiteArray()[82].callConstructor($get$$class$groovy$xml$Entity(), (Object)"eth");
   public static final Entity ntilde = (Entity)$getCallSiteArray()[83].callConstructor($get$$class$groovy$xml$Entity(), (Object)"ntilde");
   public static final Entity ograve = (Entity)$getCallSiteArray()[84].callConstructor($get$$class$groovy$xml$Entity(), (Object)"ograve");
   public static final Entity oacute = (Entity)$getCallSiteArray()[85].callConstructor($get$$class$groovy$xml$Entity(), (Object)"oacute");
   public static final Entity ocirc = (Entity)$getCallSiteArray()[86].callConstructor($get$$class$groovy$xml$Entity(), (Object)"ocirc");
   public static final Entity otilde = (Entity)$getCallSiteArray()[87].callConstructor($get$$class$groovy$xml$Entity(), (Object)"otilde");
   public static final Entity ouml = (Entity)$getCallSiteArray()[88].callConstructor($get$$class$groovy$xml$Entity(), (Object)"ouml");
   public static final Entity divide = (Entity)$getCallSiteArray()[89].callConstructor($get$$class$groovy$xml$Entity(), (Object)"divide");
   public static final Entity oslash = (Entity)$getCallSiteArray()[90].callConstructor($get$$class$groovy$xml$Entity(), (Object)"oslash");
   public static final Entity ugrave = (Entity)$getCallSiteArray()[91].callConstructor($get$$class$groovy$xml$Entity(), (Object)"ugrave");
   public static final Entity uacute = (Entity)$getCallSiteArray()[92].callConstructor($get$$class$groovy$xml$Entity(), (Object)"uacute");
   public static final Entity ucirc = (Entity)$getCallSiteArray()[93].callConstructor($get$$class$groovy$xml$Entity(), (Object)"ucirc");
   public static final Entity uuml = (Entity)$getCallSiteArray()[94].callConstructor($get$$class$groovy$xml$Entity(), (Object)"uuml");
   public static final Entity yacute = (Entity)$getCallSiteArray()[95].callConstructor($get$$class$groovy$xml$Entity(), (Object)"yacute");
   public static final Entity thorn = (Entity)$getCallSiteArray()[96].callConstructor($get$$class$groovy$xml$Entity(), (Object)"thorn");
   public static final Entity yuml = (Entity)$getCallSiteArray()[97].callConstructor($get$$class$groovy$xml$Entity(), (Object)"yuml");
   public static final Entity lt = (Entity)$getCallSiteArray()[98].callConstructor($get$$class$groovy$xml$Entity(), (Object)"lt");
   public static final Entity gt = (Entity)$getCallSiteArray()[99].callConstructor($get$$class$groovy$xml$Entity(), (Object)"gt");
   public static final Entity amp = (Entity)$getCallSiteArray()[100].callConstructor($get$$class$groovy$xml$Entity(), (Object)"amp");
   public static final Entity apos = (Entity)$getCallSiteArray()[101].callConstructor($get$$class$groovy$xml$Entity(), (Object)"apos");
   public static final Entity quot = (Entity)$getCallSiteArray()[102].callConstructor($get$$class$groovy$xml$Entity(), (Object)"quot");
   public static final Entity OElig = (Entity)$getCallSiteArray()[103].callConstructor($get$$class$groovy$xml$Entity(), (Object)"OElig");
   public static final Entity oelig = (Entity)$getCallSiteArray()[104].callConstructor($get$$class$groovy$xml$Entity(), (Object)"oelig");
   public static final Entity Scaron = (Entity)$getCallSiteArray()[105].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Scaron");
   public static final Entity scaron = (Entity)$getCallSiteArray()[106].callConstructor($get$$class$groovy$xml$Entity(), (Object)"scaron");
   public static final Entity Yuml = (Entity)$getCallSiteArray()[107].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Yuml");
   public static final Entity circ = (Entity)$getCallSiteArray()[108].callConstructor($get$$class$groovy$xml$Entity(), (Object)"circ");
   public static final Entity tilde = (Entity)$getCallSiteArray()[109].callConstructor($get$$class$groovy$xml$Entity(), (Object)"tilde");
   public static final Entity ensp = (Entity)$getCallSiteArray()[110].callConstructor($get$$class$groovy$xml$Entity(), (Object)"ensp");
   public static final Entity emsp = (Entity)$getCallSiteArray()[111].callConstructor($get$$class$groovy$xml$Entity(), (Object)"emsp");
   public static final Entity thinsp = (Entity)$getCallSiteArray()[112].callConstructor($get$$class$groovy$xml$Entity(), (Object)"thinsp");
   public static final Entity zwnj = (Entity)$getCallSiteArray()[113].callConstructor($get$$class$groovy$xml$Entity(), (Object)"zwnj");
   public static final Entity zwj = (Entity)$getCallSiteArray()[114].callConstructor($get$$class$groovy$xml$Entity(), (Object)"zwj");
   public static final Entity lrm = (Entity)$getCallSiteArray()[115].callConstructor($get$$class$groovy$xml$Entity(), (Object)"lrm");
   public static final Entity rlm = (Entity)$getCallSiteArray()[116].callConstructor($get$$class$groovy$xml$Entity(), (Object)"rlm");
   public static final Entity ndash = (Entity)$getCallSiteArray()[117].callConstructor($get$$class$groovy$xml$Entity(), (Object)"ndash");
   public static final Entity mdash = (Entity)$getCallSiteArray()[118].callConstructor($get$$class$groovy$xml$Entity(), (Object)"mdash");
   public static final Entity lsquo = (Entity)$getCallSiteArray()[119].callConstructor($get$$class$groovy$xml$Entity(), (Object)"lsquo");
   public static final Entity rsquo = (Entity)$getCallSiteArray()[120].callConstructor($get$$class$groovy$xml$Entity(), (Object)"rsquo");
   public static final Entity sbquo = (Entity)$getCallSiteArray()[121].callConstructor($get$$class$groovy$xml$Entity(), (Object)"sbquo");
   public static final Entity ldquo = (Entity)$getCallSiteArray()[122].callConstructor($get$$class$groovy$xml$Entity(), (Object)"ldquo");
   public static final Entity rdquo = (Entity)$getCallSiteArray()[123].callConstructor($get$$class$groovy$xml$Entity(), (Object)"rdquo");
   public static final Entity bdquo = (Entity)$getCallSiteArray()[124].callConstructor($get$$class$groovy$xml$Entity(), (Object)"bdquo");
   public static final Entity dagger = (Entity)$getCallSiteArray()[125].callConstructor($get$$class$groovy$xml$Entity(), (Object)"dagger");
   public static final Entity Dagger = (Entity)$getCallSiteArray()[126].callConstructor($get$$class$groovy$xml$Entity(), (Object)"Dagger");
   public static final Entity permil = (Entity)$getCallSiteArray()[127].callConstructor($get$$class$groovy$xml$Entity(), (Object)"permil");
   public static final Entity lsaquo = (Entity)$getCallSiteArray()[128].callConstructor($get$$class$groovy$xml$Entity(), (Object)"lsaquo");
   public static final Entity rsaquo = (Entity)$getCallSiteArray()[129].callConstructor($get$$class$groovy$xml$Entity(), (Object)"rsaquo");
   public static final Entity euro = (Entity)$getCallSiteArray()[130].callConstructor($get$$class$groovy$xml$Entity(), (Object)"euro");
   private final Object entity;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524203659L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203659 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$xml$Entity;

   public Entity(String name) {
      CallSite[] var2 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      this.entity = new GStringImpl(new Object[]{name}, new String[]{"&", ";"});
   }

   public Entity(int name) {
      CallSite[] var2 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      this.entity = new GStringImpl(new Object[]{DefaultTypeTransformation.box(name)}, new String[]{"&#", ";"});
   }

   public void build(GroovyObject builder) {
      CallSite[] var2 = $getCallSiteArray();
      var2[0].call(var2[1].callGetProperty(builder), this.entity);
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$xml$Entity()) {
         return ScriptBytecodeAdapter.initMetaClass(this);
      } else {
         ClassInfo var1 = $staticClassInfo;
         if (var1 == null) {
            $staticClassInfo = var1 = ClassInfo.getClassInfo(this.getClass());
         }

         return var1.getMetaClass();
      }
   }

   // $FF: synthetic method
   public Object this$dist$invoke$2(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$xml$Entity();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$xml$Entity(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$xml$Entity(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public MetaClass getMetaClass() {
      MetaClass var10000 = this.metaClass;
      if (var10000 != null) {
         return var10000;
      } else {
         this.metaClass = this.$getStaticMetaClass();
         return this.metaClass;
      }
   }

   // $FF: synthetic method
   public void setMetaClass(MetaClass var1) {
      this.metaClass = var1;
   }

   // $FF: synthetic method
   public Object invokeMethod(String var1, Object var2) {
      return this.getMetaClass().invokeMethod(this, var1, var2);
   }

   // $FF: synthetic method
   public Object getProperty(String var1) {
      return this.getMetaClass().getProperty(this, var1);
   }

   // $FF: synthetic method
   public void setProperty(String var1, Object var2) {
      this.getMetaClass().setProperty(this, var1, var2);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "leftShift";
      var0[1] = "unescaped";
      var0[2] = "<$constructor$>";
      var0[3] = "<$constructor$>";
      var0[4] = "<$constructor$>";
      var0[5] = "<$constructor$>";
      var0[6] = "<$constructor$>";
      var0[7] = "<$constructor$>";
      var0[8] = "<$constructor$>";
      var0[9] = "<$constructor$>";
      var0[10] = "<$constructor$>";
      var0[11] = "<$constructor$>";
      var0[12] = "<$constructor$>";
      var0[13] = "<$constructor$>";
      var0[14] = "<$constructor$>";
      var0[15] = "<$constructor$>";
      var0[16] = "<$constructor$>";
      var0[17] = "<$constructor$>";
      var0[18] = "<$constructor$>";
      var0[19] = "<$constructor$>";
      var0[20] = "<$constructor$>";
      var0[21] = "<$constructor$>";
      var0[22] = "<$constructor$>";
      var0[23] = "<$constructor$>";
      var0[24] = "<$constructor$>";
      var0[25] = "<$constructor$>";
      var0[26] = "<$constructor$>";
      var0[27] = "<$constructor$>";
      var0[28] = "<$constructor$>";
      var0[29] = "<$constructor$>";
      var0[30] = "<$constructor$>";
      var0[31] = "<$constructor$>";
      var0[32] = "<$constructor$>";
      var0[33] = "<$constructor$>";
      var0[34] = "<$constructor$>";
      var0[35] = "<$constructor$>";
      var0[36] = "<$constructor$>";
      var0[37] = "<$constructor$>";
      var0[38] = "<$constructor$>";
      var0[39] = "<$constructor$>";
      var0[40] = "<$constructor$>";
      var0[41] = "<$constructor$>";
      var0[42] = "<$constructor$>";
      var0[43] = "<$constructor$>";
      var0[44] = "<$constructor$>";
      var0[45] = "<$constructor$>";
      var0[46] = "<$constructor$>";
      var0[47] = "<$constructor$>";
      var0[48] = "<$constructor$>";
      var0[49] = "<$constructor$>";
      var0[50] = "<$constructor$>";
      var0[51] = "<$constructor$>";
      var0[52] = "<$constructor$>";
      var0[53] = "<$constructor$>";
      var0[54] = "<$constructor$>";
      var0[55] = "<$constructor$>";
      var0[56] = "<$constructor$>";
      var0[57] = "<$constructor$>";
      var0[58] = "<$constructor$>";
      var0[59] = "<$constructor$>";
      var0[60] = "<$constructor$>";
      var0[61] = "<$constructor$>";
      var0[62] = "<$constructor$>";
      var0[63] = "<$constructor$>";
      var0[64] = "<$constructor$>";
      var0[65] = "<$constructor$>";
      var0[66] = "<$constructor$>";
      var0[67] = "<$constructor$>";
      var0[68] = "<$constructor$>";
      var0[69] = "<$constructor$>";
      var0[70] = "<$constructor$>";
      var0[71] = "<$constructor$>";
      var0[72] = "<$constructor$>";
      var0[73] = "<$constructor$>";
      var0[74] = "<$constructor$>";
      var0[75] = "<$constructor$>";
      var0[76] = "<$constructor$>";
      var0[77] = "<$constructor$>";
      var0[78] = "<$constructor$>";
      var0[79] = "<$constructor$>";
      var0[80] = "<$constructor$>";
      var0[81] = "<$constructor$>";
      var0[82] = "<$constructor$>";
      var0[83] = "<$constructor$>";
      var0[84] = "<$constructor$>";
      var0[85] = "<$constructor$>";
      var0[86] = "<$constructor$>";
      var0[87] = "<$constructor$>";
      var0[88] = "<$constructor$>";
      var0[89] = "<$constructor$>";
      var0[90] = "<$constructor$>";
      var0[91] = "<$constructor$>";
      var0[92] = "<$constructor$>";
      var0[93] = "<$constructor$>";
      var0[94] = "<$constructor$>";
      var0[95] = "<$constructor$>";
      var0[96] = "<$constructor$>";
      var0[97] = "<$constructor$>";
      var0[98] = "<$constructor$>";
      var0[99] = "<$constructor$>";
      var0[100] = "<$constructor$>";
      var0[101] = "<$constructor$>";
      var0[102] = "<$constructor$>";
      var0[103] = "<$constructor$>";
      var0[104] = "<$constructor$>";
      var0[105] = "<$constructor$>";
      var0[106] = "<$constructor$>";
      var0[107] = "<$constructor$>";
      var0[108] = "<$constructor$>";
      var0[109] = "<$constructor$>";
      var0[110] = "<$constructor$>";
      var0[111] = "<$constructor$>";
      var0[112] = "<$constructor$>";
      var0[113] = "<$constructor$>";
      var0[114] = "<$constructor$>";
      var0[115] = "<$constructor$>";
      var0[116] = "<$constructor$>";
      var0[117] = "<$constructor$>";
      var0[118] = "<$constructor$>";
      var0[119] = "<$constructor$>";
      var0[120] = "<$constructor$>";
      var0[121] = "<$constructor$>";
      var0[122] = "<$constructor$>";
      var0[123] = "<$constructor$>";
      var0[124] = "<$constructor$>";
      var0[125] = "<$constructor$>";
      var0[126] = "<$constructor$>";
      var0[127] = "<$constructor$>";
      var0[128] = "<$constructor$>";
      var0[129] = "<$constructor$>";
      var0[130] = "<$constructor$>";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[131];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$xml$Entity(), var0);
   }

   // $FF: synthetic method
   private static CallSite[] $getCallSiteArray() {
      CallSiteArray var0;
      if ($callSiteArray == null || (var0 = (CallSiteArray)$callSiteArray.get()) == null) {
         var0 = $createCallSiteArray();
         $callSiteArray = new SoftReference(var0);
      }

      return var0.array;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$MetaClass() {
      Class var10000 = $class$groovy$lang$MetaClass;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$MetaClass = class$("groovy.lang.MetaClass");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$String() {
      Class var10000 = $class$java$lang$String;
      if (var10000 == null) {
         var10000 = $class$java$lang$String = class$("java.lang.String");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$xml$Entity() {
      Class var10000 = $class$groovy$xml$Entity;
      if (var10000 == null) {
         var10000 = $class$groovy$xml$Entity = class$("groovy.xml.Entity");
      }

      return var10000;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
