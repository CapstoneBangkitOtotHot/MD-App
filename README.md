# FreshMeter android application

This is the main repository for maintaining android application for the FreshMeter project

## Plugins

```kts
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
}
```

## Dependencies
```kts
dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // for api connection
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.10.0")

    // for image picker
    implementation("com.github.dhaval2404:imagepicker:2.1")

    // for refresh session token
    implementation("androidx.work:work-runtime-ktx:2.7.1")

    // to decode the provided token string into a JWT object.
    implementation("com.auth0.android:jwtdecode:2.0.0")

    // to display image from api response
    implementation("com.squareup.picasso:picasso:2.71828")

}
```
