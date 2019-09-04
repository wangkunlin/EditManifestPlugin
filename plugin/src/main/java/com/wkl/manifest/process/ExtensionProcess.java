package com.wkl.manifest.process;

import com.wkl.manifest.config.ApplicationConfig;
import com.wkl.manifest.config.RunWhere;
import com.wkl.manifest.plugin.ManifestExtension;
import com.wkl.manifest.utils.Pair;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.gradle.api.logging.Logger;

import java.io.StringReader;
import java.util.Map;

/**
 * Created by <a href="mailto:wangkunlin1992@gmail.com">Wang kunlin</a>
 * <p>
 * On 2019-08-28
 */
public class ExtensionProcess extends AbsProcess {

    private ManifestExtension mExtension;
    private Processor.DocumentContainer mContainer;

    ExtensionProcess(Processor.DocumentContainer container, ManifestExtension extension) {
        super(null, null);
        mContainer = container;
        this.mExtension = extension;
    }

    @Override
    protected void onHandle(Logger logger, boolean debuggable) {

        Document document = mContainer.document;

        String xml = document.asXML();
        boolean replaced = false;
        Map<String, Pair<String, RunWhere>> toReplace = mExtension.getToReplace();
        for (Map.Entry<String, Pair<String, RunWhere>> replace : toReplace.entrySet()) {
            String name = replace.getKey();
            Pair<String, RunWhere> value = replace.getValue();
            if (shouldRun(debuggable, value.second)) {
                logger.info("Replace {} to {}", name, value.first);
                xml = xml.replace(name, value.first);
                replaced = true;
            }
        }

        if (replaced) {
            try (StringReader reader = new StringReader(xml)) {
                document = mContainer.reader.read(reader);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            mContainer.document = document;
        }

        Element rootElement = document.getRootElement();
        Namespace namespace = rootElement.getNamespace();
        Attribute packageAttr = rootElement.attribute("package");
        String pkg = packageAttr.getValue();
        logger.info("Manifest package: {}", pkg);

        String targetPackageName = mExtension.getPackageName();
        if (targetPackageName != null && targetPackageName.length() > 0) {
            if (shouldRun(debuggable, mExtension.getPackageRunWhere())) {
                pkg = targetPackageName;
                logger.info("Change Manifest package to: {}", pkg);
                packageAttr.setValue(pkg);
            }
        }

        ApplicationConfig application = mExtension.getApplicationConfig();
        new ApplicationProcess(pkg, namespace, rootElement, application).process(logger, debuggable);
    }
}
