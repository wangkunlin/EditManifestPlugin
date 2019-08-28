package com.wkl.manifest.process;

import com.android.build.gradle.tasks.ManifestProcessorTask;
import com.wkl.manifest.plugin.ManifestExtension;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputFile;

import java.io.File;
import java.io.FileWriter;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-04-13
 */
public class Processor {
    private static final String ANDROID_PREFIX = "android:";
    private static final String ANDROID_MANIFEST = "AndroidManifest.xml";
    private ManifestExtension mConfig;
    @InputFile
    private File mManifest;
    @OutputFile
    private File mOutXml;

    //    private boolean mIsUp3_0;
//    private Project mProject;
    private Logger mLogger;
    private boolean mDebuggable;

    @SuppressWarnings("deprecation")
    public Processor(Project project, ManifestProcessorTask task, boolean isUp3_0, boolean debuggable) {
        if (isUp3_0) { // 3.0 以上版本 getManifestOutputFile() 被弃用
            mOutXml = new File(task.getManifestOutputDirectory(), "Edit" + ANDROID_MANIFEST);
            mManifest = new File(task.getManifestOutputDirectory(), ANDROID_MANIFEST);
        } else {
            mOutXml = new File(task.getManifestOutputFile().getParentFile(), "Edit" + ANDROID_MANIFEST);
            mManifest = new File(task.getManifestOutputFile().getAbsolutePath());
        }
        mConfig = ManifestExtension.getConfig(project);
//        mIsUp3_0 = isUp3_0;
//        mProject = project;
        mLogger = project.getLogger();

        mDebuggable = debuggable;
    }

    public void run() throws Throwable {
        mLogger.lifecycle("EditManifest: start.");
        if (mOutXml.exists()) {
            mOutXml.delete();
        }
        // 使用 dom4j 处理 xml
        SAXReader reader = new SAXReader();
        Document document = reader.read(mManifest);

        DocumentContainer container = new DocumentContainer(reader, document);

        new ExtensionProcess(container, mConfig).process(mLogger, mDebuggable);

        // 输出到文件
        FileWriter out = new FileWriter(mOutXml);
        container.document.write(out);
        out.flush();
        out.close();
        File manifestTmp = new File(mManifest.getAbsolutePath() + ".tmp");
        mManifest.renameTo(manifestTmp);
        boolean result;
        if (mOutXml.renameTo(mManifest)) {
            manifestTmp.delete();
            result = true;
        } else {
            manifestTmp.renameTo(mManifest);
            mOutXml.delete();
            result = false;
//            throw new RuntimeException("失败");
        }
        mLogger.lifecycle("EditManifest: done, with result {}.", result);
    }

    static class DocumentContainer {
        SAXReader reader;
        Document document;

        DocumentContainer(SAXReader reader, Document document) {
            this.reader = reader;
            this.document = document;
        }
    }

}
