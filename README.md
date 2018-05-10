# EditManifestPlugin

AndroidManifest.xml 文件修改插件, 只能在 app 模块中使用

只支持修改 debuggable = false 的清单文件

## 使用方法

在工程的 build.gradle 中修改

```groovy
buildscript {

    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.wkl.manifest.editor:plugin:0.0.4'
    }
}
```
之后，在模块的 build.gradle 中增加

```groovy
apply plugin: 'com.android.application' // required
apply plugin: 'com.wkl.manifest.editor'

editManifest {
    application {
        delAttr 'roundIcon'
        // 删除网络配置
        delAttr 'networkSecurityConfig'
        modAttr 'allowBackup', 'false'
        addAttr 'supportsRtl', 'false'
        // 使 release apk 具有 debug 能力
        addAttr 'debuggable', 'true'
        // ...
    }

    activity {
        // 可以是全限定名，也可以省略包名
        name '.MainActivity'
        remove true
    }
}
```
## 当前支持的功能

### application 节点

method | desc
-----|-----
delAttr(name)|删除指定属性
modAttr(name, value)|修改指定属性
addAttr(name, value)|增加一个属性
### activity 节点
删除 activity

## 兼容
理论上支持 com.android.tools.build:gradle:2.2.0 及以上版本，已经适配了 3.1.1

如有问题，请联系我修改