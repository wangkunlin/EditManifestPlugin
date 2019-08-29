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
import java.lang.reflect.Method;

import groovy.lang.GroovyClassLoader;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2018-04-13
 */
public class Processor {

    private ManifestExtension mConfig;
    @InputFile
    private File mManifest;
    @OutputFile
    private File mOutXml;

    private Logger mLogger;
    private boolean mDebuggable;

    public Processor(Project project, ManifestProcessorTask task, boolean debuggable) throws Throwable {
        mLogger = project.getLogger();
        mDebuggable = debuggable;

        GroovyClassLoader cl = new GroovyClassLoader();
        Class scriptClass = cl.loadClass("com.wkl.manifest.utils.GradleCompat");

        Method method = scriptClass.getDeclaredMethod("getManifestOutputFile", ManifestProcessorTask.class);
        mManifest = (File) method.invoke(null, task);

        mLogger.lifecycle("Found Manifest file {}.", mManifest.getAbsolutePath());

        mOutXml = new File(mManifest.getParentFile(), "EditAndroidManifest.xml");
        mLogger.lifecycle("Temp Manifest file {}.", mOutXml.getAbsolutePath());

        mConfig = ManifestExtension.getConfig(project);
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
