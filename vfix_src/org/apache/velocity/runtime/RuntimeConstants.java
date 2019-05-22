package org.apache.velocity.runtime;

public interface RuntimeConstants {
   String RUNTIME_LOG = "runtime.log";
   String RUNTIME_LOG_LOGSYSTEM = "runtime.log.logsystem";
   String RUNTIME_LOG_LOGSYSTEM_CLASS = "runtime.log.logsystem.class";
   /** @deprecated */
   String RUNTIME_LOG_ERROR_STACKTRACE = "runtime.log.error.stacktrace";
   /** @deprecated */
   String RUNTIME_LOG_WARN_STACKTRACE = "runtime.log.warn.stacktrace";
   /** @deprecated */
   String RUNTIME_LOG_INFO_STACKTRACE = "runtime.log.info.stacktrace";
   String RUNTIME_LOG_REFERENCE_LOG_INVALID = "runtime.log.invalid.references";
   /** @deprecated */
   String TRACE_PREFIX = " [trace] ";
   /** @deprecated */
   String DEBUG_PREFIX = " [debug] ";
   /** @deprecated */
   String INFO_PREFIX = "  [info] ";
   /** @deprecated */
   String WARN_PREFIX = "  [warn] ";
   /** @deprecated */
   String ERROR_PREFIX = " [error] ";
   /** @deprecated */
   String UNKNOWN_PREFIX = " [unknown] ";
   String COUNTER_NAME = "directive.foreach.counter.name";
   String COUNTER_INITIAL_VALUE = "directive.foreach.counter.initial.value";
   String MAX_NUMBER_LOOPS = "directive.foreach.maxloops";
   String SET_NULL_ALLOWED = "directive.set.null.allowed";
   String ERRORMSG_START = "directive.include.output.errormsg.start";
   String ERRORMSG_END = "directive.include.output.errormsg.end";
   String PARSE_DIRECTIVE_MAXDEPTH = "directive.parse.max.depth";
   String RESOURCE_MANAGER_CLASS = "resource.manager.class";
   String RESOURCE_MANAGER_CACHE_CLASS = "resource.manager.cache.class";
   String RESOURCE_MANAGER_DEFAULTCACHE_SIZE = "resource.manager.defaultcache.size";
   String RESOURCE_MANAGER_LOGWHENFOUND = "resource.manager.logwhenfound";
   String RESOURCE_LOADER = "resource.loader";
   String FILE_RESOURCE_LOADER_PATH = "file.resource.loader.path";
   String FILE_RESOURCE_LOADER_CACHE = "file.resource.loader.cache";
   String EVENTHANDLER_REFERENCEINSERTION = "eventhandler.referenceinsertion.class";
   String EVENTHANDLER_NULLSET = "eventhandler.nullset.class";
   String EVENTHANDLER_METHODEXCEPTION = "eventhandler.methodexception.class";
   String EVENTHANDLER_INCLUDE = "eventhandler.include.class";
   String EVENTHANDLER_INVALIDREFERENCES = "eventhandler.invalidreferences.class";
   String VM_LIBRARY = "velocimacro.library";
   String VM_LIBRARY_DEFAULT = "VM_global_library.vm";
   String VM_LIBRARY_AUTORELOAD = "velocimacro.library.autoreload";
   String VM_PERM_ALLOW_INLINE = "velocimacro.permissions.allow.inline";
   String VM_PERM_ALLOW_INLINE_REPLACE_GLOBAL = "velocimacro.permissions.allow.inline.to.replace.global";
   String VM_PERM_INLINE_LOCAL = "velocimacro.permissions.allow.inline.local.scope";
   String VM_MESSAGES_ON = "velocimacro.messages.on";
   String VM_CONTEXT_LOCALSCOPE = "velocimacro.context.localscope";
   String VM_ARGUMENTS_STRICT = "velocimacro.arguments.strict";
   String INTERPOLATE_STRINGLITERALS = "runtime.interpolate.string.literals";
   String INPUT_ENCODING = "input.encoding";
   String OUTPUT_ENCODING = "output.encoding";
   String ENCODING_DEFAULT = "ISO-8859-1";
   String UBERSPECT_CLASSNAME = "runtime.introspector.uberspect";
   String INTROSPECTOR_RESTRICT_PACKAGES = "introspector.restrict.packages";
   String INTROSPECTOR_RESTRICT_CLASSES = "introspector.restrict.classes";
   String PARSER_POOL_CLASS = "parser.pool.class";
   String PARSER_POOL_SIZE = "parser.pool.size";
   String DEFAULT_RUNTIME_PROPERTIES = "org/apache/velocity/runtime/defaults/velocity.properties";
   String DEFAULT_RUNTIME_DIRECTIVES = "org/apache/velocity/runtime/defaults/directive.properties";
   int NUMBER_OF_PARSERS = 20;
}
