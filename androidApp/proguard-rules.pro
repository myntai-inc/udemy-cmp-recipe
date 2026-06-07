# Most libraries (Room, Ktor, Coil, Koin) ship their own consumer R8 rules.
# kotlinx.serialization needs the @Serializable model classes and their
# generated serializers kept so reflective lookups keep working after shrinking.

-keepattributes *Annotation*, InnerClasses

# Keep `serializer()` companions and generated $$serializer classes for our models.
-keepclassmembers class jp.myntai.udemy.recipe.** {
    *** Companion;
}
-keepclasseswithmembers class jp.myntai.udemy.recipe.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class jp.myntai.udemy.recipe.**$$serializer { *; }
