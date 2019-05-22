package soot.javaToJimple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import polyglot.ast.Call;
import polyglot.ast.ClassDecl;
import polyglot.ast.ClassMember;
import polyglot.ast.ConstructorCall;
import polyglot.ast.Field;
import polyglot.ast.FieldDecl;
import polyglot.ast.Initializer;
import polyglot.ast.LocalClassDecl;
import polyglot.ast.New;
import polyglot.ast.Node;
import polyglot.ast.ProcedureDecl;
import polyglot.ast.SourceFile;
import polyglot.frontend.Compiler;
import polyglot.frontend.ExtensionInfo;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.LocalInstance;
import polyglot.types.ParsedClassType;
import polyglot.util.IdentityKey;
import soot.FastHierarchy;
import soot.G;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.tagkit.InnerClassTag;
import soot.tagkit.Tag;

public class InitialResolver implements IInitialResolver {
   private Node astNode;
   private Compiler compiler;
   private BiMap anonClassMap;
   private HashMap<IdentityKey, String> anonTypeMap;
   private BiMap localClassMap;
   private HashMap<IdentityKey, String> localTypeMap;
   private int privateAccessCounter = 0;
   private HashMap<IdentityKey, AnonLocalClassInfo> finalLocalInfo;
   private HashMap<String, Node> sootNameToAST = null;
   private ArrayList hasOuterRefInInit;
   private HashMap<String, String> classToSourceMap;
   private HashMap<SootClass, SootClass> specialAnonMap;
   private HashMap<IdentityKey, SootMethod> privateFieldGetAccessMap;
   private HashMap<IdentityKey, SootMethod> privateFieldSetAccessMap;
   private HashMap<IdentityKey, SootMethod> privateMethodGetAccessMap;
   private ArrayList<String> interfacesList;
   private ArrayList<Node> cCallList;
   private HashMap<New, ConstructorInstance> anonConstructorMap;
   private FastHierarchy hierarchy;
   private AbstractJBBFactory jbbFactory = new JimpleBodyBuilderFactory();
   private static final int NO_MATCH = 0;
   private HashMap<SootClass, InnerClassInfo> innerClassInfoMap;

   public void addToAnonConstructorMap(New anonNew, ConstructorInstance ci) {
      if (this.anonConstructorMap == null) {
         this.anonConstructorMap = new HashMap();
      }

      this.anonConstructorMap.put(anonNew, ci);
   }

   public ConstructorInstance getConstructorForAnon(New anonNew) {
      return this.anonConstructorMap == null ? null : (ConstructorInstance)this.anonConstructorMap.get(anonNew);
   }

   public void setJBBFactory(AbstractJBBFactory jbbFactory) {
      this.jbbFactory = jbbFactory;
   }

   public AbstractJBBFactory getJBBFactory() {
      return this.jbbFactory;
   }

   public boolean hasASTForSootName(String name) {
      if (this.sootNameToAST == null) {
         return false;
      } else {
         return this.sootNameToAST.containsKey(name);
      }
   }

   public void setASTForSootName(String name) {
      if (!this.hasASTForSootName(name)) {
         throw new RuntimeException("Can only set AST for name if it exists. You should probably not be calling this method unless you know what you're doing!");
      } else {
         this.setAst((Node)this.sootNameToAST.get(name));
      }
   }

   public InitialResolver(Singletons.Global g) {
   }

   public static InitialResolver v() {
      return G.v().soot_javaToJimple_InitialResolver();
   }

   public void formAst(String fullPath, List<String> locations, String className) {
      JavaToJimple jtj = new JavaToJimple();
      ExtensionInfo extInfo = jtj.initExtInfo(fullPath, locations);
      if (this.compiler == null) {
         this.compiler = new Compiler(extInfo);
      }

      this.astNode = jtj.compile(this.compiler, fullPath, extInfo);
      this.resolveAST();
   }

   public void setAst(Node ast) {
      this.astNode = ast;
   }

   public Node getAst() {
      return this.astNode;
   }

   private void makeASTMap() {
      ClassDeclFinder finder = new ClassDeclFinder();
      this.astNode.visit(finder);

      ParsedClassType type;
      for(Iterator it = finder.declsFound().iterator(); it.hasNext(); this.addNameToAST(Util.getSootType(type).toString())) {
         ClassDecl decl = (ClassDecl)it.next();
         type = decl.type();
         if (type.flags().isInterface()) {
            if (this.interfacesList == null) {
               this.interfacesList = new ArrayList();
            }

            this.interfacesList.add(Util.getSootType(type).toString());
         }
      }

   }

   protected void addNameToAST(String name) {
      if (this.sootNameToAST == null) {
         this.sootNameToAST = new HashMap();
      }

      this.sootNameToAST.put(name, this.astNode);
   }

   public void resolveAST() {
      this.buildInnerClassInfo();
      if (this.astNode instanceof SourceFile) {
         this.createClassToSourceMap((SourceFile)this.astNode);
      }

   }

   public IInitialResolver.Dependencies resolveFromJavaFile(SootClass sc) {
      IInitialResolver.Dependencies dependencies = new IInitialResolver.Dependencies();
      ClassResolver cr = new ClassResolver(sc, dependencies.typesToSignature);
      if (this.astNode instanceof SourceFile) {
         cr.createSource((SourceFile)this.astNode);
      }

      cr.addSourceFileTag(sc);
      this.makeASTMap();
      return dependencies;
   }

   private void createClassToSourceMap(SourceFile src) {
      String srcName = src.source().path();
      String srcFileName = null;
      if (src.package_() != null) {
         String slashedPkg = src.package_().package_().fullName().replaceAll(".", System.getProperty("file.separator"));
         srcFileName = srcName.substring(srcName.lastIndexOf(slashedPkg));
      } else {
         srcFileName = srcName.substring(srcName.lastIndexOf(System.getProperty("file.separator")) + 1);
      }

      new ArrayList();
      Iterator it = src.decls().iterator();

      while(it.hasNext()) {
         ClassDecl nextDecl = (ClassDecl)it.next();
         this.addToClassToSourceMap(Util.getSootType(nextDecl.type()).toString(), srcFileName);
      }

   }

   private void createLocalAndAnonClassNames(ArrayList<Node> anonBodyList, ArrayList<Node> localClassDeclList) {
      Iterator anonBodyIt = anonBodyList.iterator();

      while(anonBodyIt.hasNext()) {
         this.createAnonClassName((New)anonBodyIt.next());
      }

      Iterator localClassDeclIt = localClassDeclList.iterator();

      while(localClassDeclIt.hasNext()) {
         this.createLocalClassName((LocalClassDecl)localClassDeclIt.next());
      }

   }

   protected int getNextAnonNum() {
      return this.anonTypeMap == null ? 1 : this.anonTypeMap.size() + 1;
   }

   private void createAnonClassName(New nextNew) {
      if (this.anonClassMap == null) {
         this.anonClassMap = new BiMap();
      }

      if (this.anonTypeMap == null) {
         this.anonTypeMap = new HashMap();
      }

      if (!this.anonClassMap.containsKey(nextNew)) {
         int nextAvailNum = 1;

         ClassType outerToMatch;
         for(outerToMatch = nextNew.anonType().outer(); outerToMatch.isNested(); outerToMatch = outerToMatch.outer()) {
         }

         if (!this.anonTypeMap.isEmpty()) {
            Iterator matchIt = this.anonTypeMap.keySet().iterator();

            while(matchIt.hasNext()) {
               ClassType pType = (ClassType)((IdentityKey)matchIt.next()).object();

               ClassType outerMatch;
               for(outerMatch = pType.outer(); outerMatch.isNested(); outerMatch = outerMatch.outer()) {
               }

               if (outerMatch.equals(outerToMatch)) {
                  int numFound = this.getAnonClassNum((String)this.anonTypeMap.get(new IdentityKey(pType)));
                  if (numFound >= nextAvailNum) {
                     nextAvailNum = numFound + 1;
                  }
               }
            }
         }

         String realName = outerToMatch.fullName() + "$" + nextAvailNum;
         this.anonClassMap.put(nextNew, realName);
         this.anonTypeMap.put(new IdentityKey(nextNew.anonType()), realName);
         this.addNameToAST(realName);
      }

   }

   private void createLocalClassName(LocalClassDecl lcd) {
      if (this.localClassMap == null) {
         this.localClassMap = new BiMap();
      }

      if (this.localTypeMap == null) {
         this.localTypeMap = new HashMap();
      }

      if (!this.localClassMap.containsKey(lcd)) {
         int nextAvailNum = 1;

         ClassType outerToMatch;
         for(outerToMatch = lcd.decl().type().outer(); outerToMatch.isNested(); outerToMatch = outerToMatch.outer()) {
         }

         if (!this.localTypeMap.isEmpty()) {
            Iterator matchIt = this.localTypeMap.keySet().iterator();

            while(matchIt.hasNext()) {
               ClassType pType = (ClassType)((IdentityKey)matchIt.next()).object();

               ClassType outerMatch;
               for(outerMatch = pType.outer(); outerMatch.isNested(); outerMatch = outerMatch.outer()) {
               }

               if (outerMatch.equals(outerToMatch)) {
                  int numFound = this.getLocalClassNum((String)this.localTypeMap.get(new IdentityKey(pType)), lcd.decl().name());
                  if (numFound >= nextAvailNum) {
                     nextAvailNum = numFound + 1;
                  }
               }
            }
         }

         String realName = outerToMatch.fullName() + "$" + nextAvailNum + lcd.decl().name();
         this.localClassMap.put(lcd, realName);
         this.localTypeMap.put(new IdentityKey(lcd.decl().type()), realName);
         this.addNameToAST(realName);
      }

   }

   private int getLocalClassNum(String realName, String simpleName) {
      int dIndex = realName.indexOf("$");
      int nIndex = realName.indexOf(simpleName, dIndex);
      if (nIndex == -1) {
         return 0;
      } else if (dIndex == -1) {
         throw new RuntimeException("Matching an incorrectly named local inner class: " + realName);
      } else {
         String numString = realName.substring(dIndex + 1, nIndex);

         for(int i = 0; i < numString.length(); ++i) {
            if (!Character.isDigit(numString.charAt(i))) {
               return 0;
            }
         }

         return new Integer(numString);
      }
   }

   private int getAnonClassNum(String realName) {
      int dIndex = realName.indexOf("$");
      if (dIndex == -1) {
         throw new RuntimeException("Matching an incorrectly named anon inner class: " + realName);
      } else {
         return new Integer(realName.substring(dIndex + 1));
      }
   }

   private void addToClassToSourceMap(String className, String sourceName) {
      if (this.classToSourceMap == null) {
         this.classToSourceMap = new HashMap();
      }

      this.classToSourceMap.put(className, sourceName);
   }

   public boolean hasClassInnerTag(SootClass sc, String innerName) {
      Iterator it = sc.getTags().iterator();

      while(it.hasNext()) {
         Tag t = (Tag)it.next();
         if (t instanceof InnerClassTag) {
            InnerClassTag tag = (InnerClassTag)t;
            if (tag.getInnerClass().equals(innerName)) {
               return true;
            }
         }
      }

      return false;
   }

   private void buildInnerClassInfo() {
      InnerClassInfoFinder icif = new InnerClassInfoFinder();
      this.astNode.visit(icif);
      this.createLocalAndAnonClassNames(icif.anonBodyList(), icif.localClassDeclList());
      this.buildFinalLocalMap(icif.memberList());
   }

   private void buildFinalLocalMap(ArrayList<Node> memberList) {
      Iterator it = memberList.iterator();

      while(it.hasNext()) {
         this.handleFinalLocals((ClassMember)it.next());
      }

   }

   private void handleFinalLocals(ClassMember member) {
      MethodFinalsChecker mfc = new MethodFinalsChecker();
      member.visit(mfc);
      if (this.cCallList == null) {
         this.cCallList = new ArrayList();
      }

      this.cCallList.addAll(mfc.ccallList());
      AnonLocalClassInfo alci = new AnonLocalClassInfo();
      if (member instanceof ProcedureDecl) {
         ProcedureDecl procedure = (ProcedureDecl)member;
         alci.finalLocalsAvail(mfc.finalLocals());
         if (procedure.flags().isStatic()) {
            alci.inStaticMethod(true);
         }
      } else if (member instanceof FieldDecl) {
         alci.finalLocalsAvail(new ArrayList());
         if (((FieldDecl)member).flags().isStatic()) {
            alci.inStaticMethod(true);
         }
      } else if (member instanceof Initializer) {
         alci.finalLocalsAvail(mfc.finalLocals());
         if (((Initializer)member).flags().isStatic()) {
            alci.inStaticMethod(true);
         }
      }

      if (this.finalLocalInfo == null) {
         this.finalLocalInfo = new HashMap();
      }

      Iterator it = mfc.inners().iterator();

      while(it.hasNext()) {
         ClassType cType = (ClassType)((IdentityKey)it.next()).object();
         HashMap<IdentityKey, ArrayList<IdentityKey>> typeToLocalUsed = mfc.typeToLocalsUsed();
         ArrayList<IdentityKey> localsUsed = new ArrayList();
         if (typeToLocalUsed.containsKey(new IdentityKey(cType))) {
            ArrayList localsNeeded = (ArrayList)typeToLocalUsed.get(new IdentityKey(cType));
            Iterator usesIt = localsNeeded.iterator();

            while(usesIt.hasNext()) {
               LocalInstance li = (LocalInstance)((IdentityKey)usesIt.next()).object();
               if (alci.finalLocalsAvail().contains(new IdentityKey(li))) {
                  localsUsed.add(new IdentityKey(li));
               }
            }
         }

         AnonLocalClassInfo info = new AnonLocalClassInfo();
         info.inStaticMethod(alci.inStaticMethod());
         info.finalLocalsAvail(localsUsed);
         if (!this.finalLocalInfo.containsKey(new IdentityKey(cType))) {
            this.finalLocalInfo.put(new IdentityKey(cType), info);
         }
      }

   }

   public boolean isAnonInCCall(ClassType anonType) {
      Iterator it = this.cCallList.iterator();

      while(it.hasNext()) {
         ConstructorCall cCall = (ConstructorCall)it.next();
         Iterator argsIt = cCall.arguments().iterator();

         while(argsIt.hasNext()) {
            Object next = argsIt.next();
            if (next instanceof New && ((New)next).anonType() != null && ((New)next).anonType().equals(anonType)) {
               return true;
            }
         }
      }

      return false;
   }

   public BiMap getAnonClassMap() {
      return this.anonClassMap;
   }

   public BiMap getLocalClassMap() {
      return this.localClassMap;
   }

   public HashMap<IdentityKey, String> getAnonTypeMap() {
      return this.anonTypeMap;
   }

   public HashMap<IdentityKey, String> getLocalTypeMap() {
      return this.localTypeMap;
   }

   public HashMap<IdentityKey, AnonLocalClassInfo> finalLocalInfo() {
      return this.finalLocalInfo;
   }

   public int getNextPrivateAccessCounter() {
      int res = this.privateAccessCounter++;
      return res;
   }

   public ArrayList getHasOuterRefInInit() {
      return this.hasOuterRefInInit;
   }

   public void setHasOuterRefInInit(ArrayList list) {
      this.hasOuterRefInInit = list;
   }

   public HashMap<SootClass, SootClass> specialAnonMap() {
      return this.specialAnonMap;
   }

   public void setSpecialAnonMap(HashMap<SootClass, SootClass> map) {
      this.specialAnonMap = map;
   }

   public void hierarchy(FastHierarchy fh) {
      this.hierarchy = fh;
   }

   public FastHierarchy hierarchy() {
      return this.hierarchy;
   }

   public HashMap<SootClass, InnerClassInfo> getInnerClassInfoMap() {
      return this.innerClassInfoMap;
   }

   public void setInnerClassInfoMap(HashMap<SootClass, InnerClassInfo> map) {
      this.innerClassInfoMap = map;
   }

   protected HashMap<String, String> classToSourceMap() {
      return this.classToSourceMap;
   }

   public void addToPrivateFieldGetAccessMap(Field field, SootMethod meth) {
      if (this.privateFieldGetAccessMap == null) {
         this.privateFieldGetAccessMap = new HashMap();
      }

      this.privateFieldGetAccessMap.put(new IdentityKey(field.fieldInstance()), meth);
   }

   public HashMap<IdentityKey, SootMethod> getPrivateFieldGetAccessMap() {
      return this.privateFieldGetAccessMap;
   }

   public void addToPrivateFieldSetAccessMap(Field field, SootMethod meth) {
      if (this.privateFieldSetAccessMap == null) {
         this.privateFieldSetAccessMap = new HashMap();
      }

      this.privateFieldSetAccessMap.put(new IdentityKey(field.fieldInstance()), meth);
   }

   public HashMap<IdentityKey, SootMethod> getPrivateFieldSetAccessMap() {
      return this.privateFieldSetAccessMap;
   }

   public void addToPrivateMethodGetAccessMap(Call call, SootMethod meth) {
      if (this.privateMethodGetAccessMap == null) {
         this.privateMethodGetAccessMap = new HashMap();
      }

      this.privateMethodGetAccessMap.put(new IdentityKey(call.methodInstance()), meth);
   }

   public HashMap<IdentityKey, SootMethod> getPrivateMethodGetAccessMap() {
      return this.privateMethodGetAccessMap;
   }

   public ArrayList<String> getInterfacesList() {
      return this.interfacesList;
   }
}
