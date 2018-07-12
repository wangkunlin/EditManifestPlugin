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
        classpath 'com.wkl.manifest.editor:plugin:0.0.6'
    }
}
```
之后，在模块的 build.gradle 中增加

```groovy
apply plugin: 'com.android.application' // required
apply plugin: 'com.wkl.manifest.editor'

editManifest {
    application {
        // remove true  // 支持 移除 application
        delAttr 'roundIcon'
        // 删除网络配置
        delAttr 'networkSecurityConfig'
        modAttr 'allowBackup', 'false'
        addAttr 'supportsRtl', 'false'
        // 使 release apk 具有 debug 能力
        addAttr 'debuggable', 'true'
        // ...

        metaData { // 若不存在，则自动创建
            // remove true // 支持移除
            name 'com.wkl.app.metadata.test'
            value 'app_test_metadata'
        }

        activity { // 由 application 平级，移动到 application 子级
            // 可以是全限定名，也可以省略包名
            name '.MainActivity'
            // remove true
            // 支持属性的 增删改
            addAttr 'theme', '@style/AppTheme'

            metaData { // 同上
                name 'com.wkl.activity.metadata.test'
                value 'activity_test_metadata'
            }
        }
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
remove(remove)|移除节点
metaData(closure)|配置 meta-data
### activity 节点

method | desc
-----|-----
delAttr(name)|删除指定属性
modAttr(name, value)|修改指定属性
addAttr(name, value)|增加一个属性
remove(remove)|移除节点
metaData(closure)|配置 meta-data
### meta-data 节点
method | desc
-----|-----
name(name)|指定 meta-data 的 name
rename(name)|重命名 meta-data
value(value)|设置 meta-data 的 value
resource(resource)|设置 meta-data 的 resource
remove(remove)|移除节点

## 兼容
理论上支持 com.android.tools.build:gradle:2.2.0 及以上版本，已经适配了 3.1.1

如有问题，请联系我修改