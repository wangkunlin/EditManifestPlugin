package com.wkl.manifest.process;

import com.wkl.manifest.config.ApplicationConfig;
import com.wkl.manifest.plugin.ManifestExtension;

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
        Map<String, String> toReplace = mExtension.getToReplace();
        for (Map.Entry<String, String> replace : toReplace.entrySet()) {
            String name = replace.getKey();
            String value = replace.getValue();
            logger.info("Replace {} to {}", name, value);
            xml = xml.replace(name, value);
        }

        try (StringReader reader = new StringReader(xml)) {
            document = mContainer.reader.read(reader);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        mContainer.document = document;

        Element rootElement = document.getRootElement();
        Namespace namespace = rootElement.getNamespace();
        Attribute packageAttr = rootElement.attribute("package");
        String pkg = packageAttr.getValue();
        logger.info("Manifest package: {}", pkg);

        String targetPackageName = mExtension.getPackageName();
        if (targetPackageName != null && targetPackageName.length() > 0) {
            pkg = targetPackageName;
            logger.info("Change Manifest package to: {}", pkg);
            packageAttr.setValue(pkg);
        }

        ApplicationConfig application = mExtension.getApplicationConfig();
        new ApplicationProcess(pkg, namespace, rootElement, application).process(logger, debuggable);
    }
}
