# Android集成ReactNative

这篇教程是实践如何在Android原生代码中集成ReactNative，教程采用的react版本：`16.0.0`，react-native版本：`0.51.0`，编写时间：`2017.12.25`，内容仅供参考！

<!-- more -->

### 准备工作

> Android环境配置

参考官方文档：`https://facebook.github.io/react-native/docs/getting-started.html`

### 开始集成

1. 新建`package.json`到新创建的项目MixReactNative文件夹下，输入如下：

```json
{
  "name": "MixReactNative",
  "version": "0.0.1",
  "private": true,
  "scripts": {
    "start": "node node_modules/react-native/local-cli/cli.js start"
  },
  "dependencies": {
    "react": "16.0.0",
    "react-native": "0.51.0"
  },
  "devDependencies": {
  }
}
```
2. 回到根目录执行`npm install`安装npm相关依赖
3. 创建`index.js`文件，输入如下：

```javascript
import { AppRegistry } from 'react-native';
import App from './App';

AppRegistry.registerComponent('MixReactNative', () => App);
```
4. 创建`App.js`文件，输入如下：
```javascript
import React, {Component} from 'react';
import {
    Platform,
    StyleSheet,
    Text,
    View
} from 'react-native';

const instructions = Platform.select({
    ios: 'Press Cmd+R to reload,\n' +
    'Cmd+D or shake for dev menu',
    android: 'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

export default class App extends Component<{}> {
    render() {
        return (
            <View style={styles.container}>
                <Text style={styles.welcome}>
                    ReactNative集成成功！
                </Text>
                <Text style={styles.welcome}>
                    Welcome to React Native!
                </Text>
                <Text style={styles.instructions}>
                    To get started, edit App.js
                </Text>
                <Text style={styles.instructions}>
                    {instructions}
                </Text>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
    welcome: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
    instructions: {
        textAlign: 'center',
        color: '#333333',
        marginBottom: 5,
    },
});
```
5. 项目根目录下新建`android`文件夹，将原Android项目复制到该文件夹下
6. 修改Project下的`build.gradle`文件，参考如下：
```groovy
buildscript {
   ......
allprojects {
    repositories {
        maven {
            // All of React Native (JS, Android binaries) is installed from npm
            url "$rootDir/../node_modules/react-native/android"
        }
      ......
    }
}
```
> 注意：这里的url路径容易出错，需要留意是否是正确路径
7. 修改app下的`build.gradle`文件，参考如下：

```groovy
apply plugin: 'com.android.application'

android {
    compileSdkVersion 26
    buildToolsVersion "26.0.2"

    defaultConfig {
        applicationId "com.demo.androidnative"
        minSdkVersion 16
        targetSdkVersion 26
        versionCode 1
        versionName "1.0"
        ndk {
            abiFilters "armeabi-v7a", "x86"
        }
    }
    splits {
        abi {
            reset()
            enable false
            universalApk false  // If true, also generate a universal APK
            include "armeabi-v7a", "x86"
        }
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'com.android.support:appcompat-v7:26.1.0'
    implementation "com.facebook.react:react-native:+" // From node_modules.
}
```
8. 新建`MainApplication.java`文件继承`Application`并实现`ReactAppliaction`接口，输入如下:

```java
public class MainApplication extends Application implements ReactApplication {

    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
            return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.<ReactPackage>asList(
                    new MainReactPackage()
            );
        }

        @Override
        protected String getJSMainModuleName() {
            return "index";
        }
    };

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, /* native exopackage */ false);
    }
}
```
> 注意：记得修改AndroidManifest.xml文件中对应的位置
9. 新建一个继承`ReactActivity.java`的类，参考如下：

```java
public class ReactNativeActivity extends ReactActivity {

    private static final int OVERLAY_PERMISSION_REQ_CODE = 0000;

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "MixReactNative";
    }
}
```
> 注意：记得在AndroidManifest.xml文件中加入该Activity
```xml
<activity android:name=".ReactNativeActivity"/>
```

10. 在AndroidManifest.xml文件中加入ReactNative用于调试开发的Activitty，输入如下：

```xml
 <activity android:name="com.facebook.react.devsupport.DevSettingsActivity"/>
```
11. 在原生页面中编写跳转到`ReactNativeActivity`类的代码用于测试原生跳转RN页面是否正常，参考如下：

```java
public class MainActivity extends AppCompatActivity {

    private Button gotoRn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.gotoRn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ReactNativeActivity.class));
            }
        });
    }
}
```
12. 至此代码集成基本就绪，项目根目录下执行`react-native start`开启服务，然后在`Android Studio`中连接设备并安装运行app，点击跳转到对应的RN页面后通过摇晃手机进入开发调试页，配置同一网络环境下的ip和端口号，然后重新加载页面，不出意外的话就会成功看到RN页面成功加载。
