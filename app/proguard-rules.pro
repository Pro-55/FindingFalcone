### Classes
-keep class com.example.omdb.models.** { *; }
-keepnames class com.example.omdb.util.** { *; }
-keepnames class **.*Fragment*

### ServiceLoader Support
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
### Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembernames class kotlinx.** { volatile <fields>; }

### Core Components
-keep class androidx.core.app.** { *; }

### Ktor
-keep class io.ktor.** { *; }
-dontwarn groovy.lang.GroovyObject
-dontwarn groovy.lang.MetaClass
-dontwarn java.lang.management.ManagementFactory
-dontwarn javax.management.InstanceNotFoundException
-dontwarn javax.management.MBeanRegistrationException
-dontwarn javax.management.MBeanServer
-dontwarn javax.management.MalformedObjectNameException
-dontwarn javax.management.ObjectInstance
-dontwarn javax.management.ObjectName
-dontwarn javax.naming.Context
-dontwarn javax.naming.InitialContext
-dontwarn javax.naming.NamingException
-dontwarn javax.servlet.ServletContainerInitializer
-dontwarn kotlin.Experimental$Level
-dontwarn kotlin.Experimental
-dontwarn org.codehaus.groovy.reflection.ClassInfo
-dontwarn org.codehaus.groovy.runtime.BytecodeInterface8
-dontwarn org.codehaus.groovy.runtime.ScriptBytecodeAdapter
-dontwarn org.codehaus.groovy.runtime.callsite.CallSite
-dontwarn org.codehaus.groovy.runtime.callsite.CallSiteArray
-dontwarn org.codehaus.janino.ClassBodyEvaluator
-dontwarn sun.reflect.Reflection