package com.wkl.manifest.plugin;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.tasks.ManifestProcessorTask;
import com.wkl.manifest.process.Processor;
import com.wkl.manifest.utils.Utils;

import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.ProjectConfigurationException;
import org.gradle.api.tasks.TaskInputs;

import java.lang.reflect.Field;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-04-16
 */
@NonNullApi
public class ManifestPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        // 只应用于 application 模块
        if (!project.getPlugins().hasPlugin("com.android.application")) {
            Throwable e = new RuntimeException();
            throw new ProjectConfigurationException("Plugin requires the 'com.android.application' plugin to be configured.", e);
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
        if (version == null || Utils.versionCompare(version, "2.2.0") < 0) {
            Throwable e = new RuntimeException();
            throw new ProjectConfigurationException("Plugin requires the 'com.android.tools.build:gradle' version 2.2.0 or above to be configured.", e);
        }

//        boolean is3_0 = Utils.versionCompare(version, "3.0.0") >= 0;

        ManifestExtension extension = createExtension(project);

        project.afterEvaluate(project1 -> {
            // 找到 android 的  AppExtension
            AppExtension appExtension = project1.getExtensions().getByType(AppExtension.class);
            // 遍历所有的 构建变体
            appExtension.getApplicationVariants().forEach(variant -> {
                boolean debuggable = variant.getBuildType().isDebuggable();
                // 如果 debuggable = false
//                if (!debuggable) {
                String name = variant.getName();
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                // 找到 形如 processReleaseManifest 的 task
                ManifestProcessorTask manifestTask = (ManifestProcessorTask) project1.getTasks().getByName("process" + name + "Manifest");
                // 为 task 添加一个属性，使 edit manifest 的 配置发生变化时，ManifestProcessorTask 会重新执行.
                TaskInputs inputs = manifestTask.getInputs();
                inputs.property("editManifestExtension", extension.parseProperty());
                // 在 ManifestProcessorTask 完成后 追加 doLast 处理 Manifest 文件
                manifestTask.doLast(task -> {
                    Processor processor;
                    try {
                        processor = new Processor(project1, manifestTask, debuggable);
                        processor.run();
                    } catch (Throwable e) {
                        throw new RuntimeException("处理 Manifest 失败", e);
                    }
                });
//                }
            });
        });
    }

    private ManifestExtension createExtension(Project project) {
        return project.getExtensions().create("editManifest", ManifestExtension.class, project);
    }

}
