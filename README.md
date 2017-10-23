# SCSmart_01
网络请求的框架
使用方法后面添加
Step 1. Add the JitPack repository to your build file

gradle
maven
sbt
leiningen
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}Copy
Step 2. Add the dependency

	dependencies {
	        compile 'com.github.breaveZhao:SCSmart_01:1.0.0'
	}
Copy
Share this release:
