package com.wkl.manifest.plugin;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.tasks.ManifestProcessorTask;
import com.wkl.manifest.Processer;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.ProjectConfigurationException;

import java.lang.reflect.Field;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-04-16
 */
public class ManifestPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        // 只应用于 application 模块
        if (!project.getPlugins().hasPlugin("com.android.application")) {
            throw new ProjectConfigurationException("Plugin requires the 'com.android.application' plugin to be configured.", null);
        }
        String version = null;
        try {
            Class<?> clazz = Class.forName("com.android.builder.Version");
            Field field = clazz.getDeclaredField("ANDROID_GRADLE_PLUGIN_VERSION");
            field.setAccessible(true);
            version = (String) field.get(null);
        } catch (Exception ignore) {
        }
        if (version == null) {
            try {
                Class<?> clazz = Class.forName("com.android.builder.model.Version");
                Field field = clazz.getDeclaredField("ANDROID_GRADLE_PLUGIN_VERSION");
                field.setAccessible(true);
                version = (String) field.get(null);
            } catch (Exception ignore) {
            }
        }

        // 比较版本
        if (version == null || versionCompare(version, "2.2.0") < 0) {
            throw new ProjectConfigurationException("Plugin requires the 'com.android.tools.build:gradle' version 2.2.0 or above to be configured.", null);
        }

        boolean is3_0 = versionCompare(version, "3.0.0") >= 0;

        createExtension(project);

        project.afterEvaluate(project1 -> {
            // 找到 android 的  AppExtension
            AppExtension appExtension = project1.getExtensions().getByType(AppExtension.class);
            // 遍历所有的 构建变体
            appExtension.getApplicationVariants().forEach(variant -> {
                String name = variant.getName();
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                boolean debuggable = variant.getBuildType().isDebuggable();
                // 如果 debuggable = false
                if (!debuggable) {
                    // 找打 形如 processReleaseManifest 的 tast
                    ManifestProcessorTask manifestTask = (ManifestProcessorTask) project1.getTasks().getByName("process" + name + "Manifest");
                    // 在 ManifestProcessorTask 完成后 追加 doLast 处理 Manifest 文件
                    manifestTask.doLast(task -> {
                        Processer processer = new Processer(project1, manifestTask, is3_0);
                        try {
                            processer.run();
                        } catch (Exception e) {
                            throw new RuntimeException("处理 Manifest 失败", e);
                        }
                    });
                }
            });
        });
    }

    private void createExtension(Project project) {
        project.getExtensions().create("editManifest", ManifestExtension.class, project);
    }

    private static int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("-")[0].split("\\.");
        String[] vals2 = str2.split("-")[0].split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }

        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }

        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        else {
            return Integer.signum(vals1.length - vals2.length);
        }
    }
}
