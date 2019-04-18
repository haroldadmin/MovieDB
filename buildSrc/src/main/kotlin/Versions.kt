import kotlin.String

/**
 * Find which updates are available by running
 *     `$ ./gradlew buildSrcVersions`
 * This will only update the comments.
 *
 * YOU are responsible for updating manually the dependency version.
 */
object Versions {
    const val appcompat: String = "1.1.0-alpha03" 

    const val constraintlayout: String = "2.0.0-alpha3" 

    const val core_ktx: String = "1.0.1" 

    const val androidx_legacy: String = "1.0.0" 

    const val lifecycle_extensions: String = "2.0.0" 

    const val androidx_navigation: String = "2.1.0-alpha02" 

    const val preference: String = "1.0.0" 

    const val androidx_room: String = "2.1.0-alpha05" 

    const val espresso_core: String = "3.1.1" 

    const val androidx_test_runner: String = "1.1.1" 

    const val com_airbnb_android: String = "3.4.1" 

    const val aapt2: String = "3.5.0-alpha12-5342889" 

    const val com_android_tools_build_gradle: String = "3.5.0-alpha12" 

    const val lint_gradle: String = "26.5.0-alpha12" 

    const val crashlytics: String = "2.9.9" 

    const val lemniscate: String = "2.0.0" 

    const val com_github_bumptech_glide: String = "4.9.0" 

    const val material_about_library: String = "2.4.2" 

    const val material: String = "1.0.0" 

    const val firebase_core: String = "16.0.8" 

    const val google_services: String = "4.2.0" 

    const val retrofit2_kotlin_coroutines_adapter: String = "0.9.2" 

    const val rxbinding: String = "2.2.0" 

    const val rxrelay: String = "2.1.0" 

    const val com_pierfrancescosoffritti_androidyoutubeplayer_core: String = "9.0.1" 
            // available: "10.0.3"

    const val com_squareup_moshi: String = "1.8.0" 

    const val com_squareup_okhttp3: String = "3.14.1" 

    const val com_squareup_retrofit2: String = "2.5.0" 

    const val de_fayard_buildsrcversions_gradle_plugin: String = "0.3.2" 

    const val io_fabric_tools_gradle: String = "1.28.1" 

    const val rxandroid: String = "2.1.1" 

    const val rxjava: String = "2.2.8" 

    const val rxkotlin: String = "2.3.0" 

    const val junit: String = "4.12" 

    const val kotlin_android_extensions_runtime: String = "1.3.30" 

    const val kotlin_android_extensions: String = "1.3.30" 

    const val kotlin_annotation_processing_gradle: String = "1.3.30" 

    const val kotlin_gradle_plugin: String = "1.3.30" 

    const val org_jetbrains_kotlin_kotlin_stdlib_jdk7: String = "1.3.21" // available: "1.3.30"

    const val org_jetbrains_kotlinx: String = "1.2.0" 

    const val org_koin: String = "2.0.0-rc-2" 

    const val mockito_core: String = "2.27.0" 

    /**
     *
     *   To update Gradle, edit the wrapper file at path:
     *      ./gradle/wrapper/gradle-wrapper.properties
     */
    object Gradle {
        const val runningVersion: String = "5.3.1"

        const val currentVersion: String = "5.4"

        const val nightlyVersion: String = "5.5-20190418152614+0000"

        const val releaseCandidate: String = ""
    }
}
