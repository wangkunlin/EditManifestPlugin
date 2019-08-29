package com.wkl.manifest.utils

import com.android.build.gradle.tasks.ManifestProcessorTask

class GradleCompat {

    public static final String ANDROID_MANIFEST = "AndroidManifest.xml"

    static File getManifestOutputFile(ManifestProcessorTask task) {
        try {
            return get(task.getManifestOutputFile())
        } catch (Throwable e) {
//            e.printStackTrace()
            try {
                return new File(task.getManifestOutputDirectory().get().getAsFile(), ANDROID_MANIFEST)
            } catch (Throwable e2) {
//                e2.printStackTrace()
                try {
                    return task.getManifestOutputFile()
                } catch (Throwable e3) {
//                    e3.printStackTrace()
                    return new File(task.getManifestOutputDirectory(), ANDROID_MANIFEST)
                }
            }
        }
    }

    def static get(def provider) {
        return provider == null ? null : provider.get()
    }
}