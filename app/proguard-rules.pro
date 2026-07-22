# ── NexLedger ProGuard / R8 Rules ──

# Keep Room entities and DAOs
-keep class com.nexledger.core.database.entity.** { *; }
-keep class com.nexledger.core.database.dao.** { *; }

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Keep kotlinx.serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.nexledger.**$$serializer { *; }
-keepclassmembers class com.nexledger.** {
    *** Companion;
}
-keepclasseswithmembers class com.nexledger.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep navigation @Serializable routes
-keepnames @kotlinx.serialization.Serializable class com.nexledger.core.navigation.** { *; }

# Keep data classes for reflection
-keep class com.actaks.nexledger.core.model.** { *; }
-keep class com.actaks.nexledger.feature.backup.BackupData { *; }
-keep class com.actaks.nexledger.feature.backup.BackupTransaction { *; }
-keep class com.actaks.nexledger.feature.backup.BackupAccount { *; }
-keep class com.actaks.nexledger.feature.backup.BackupCategory { *; }

# General Android
-keepattributes Signature
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes EnclosingMethod
-keepattributes *Annotation*

# OkHttp (if added for future sync)
-dontwarn okhttp3.**
-dontwarn okio.**
