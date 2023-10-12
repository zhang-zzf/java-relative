## 构建历史

### jar-basic-archetype 1.0-RELEASE

- lombok
- junit5 / junit4 / mockito / assertJ
- slf4j/logback 日志打印到控制台

## maven 常用命令

- mvn clean compile
- mvn clean package
- mvn clean install

- mvn source:jar 源码包
- mvn dependency:tree 显示依赖树
- mvn shade:shade 构建 uber-jar

## 项目环境

1. 配置maven-resources-plugin的编码为`UTF-8`

   ```xml
    <properties>
    	<!-- maven-resources-plugin 会自动读取此配置 -->
    	<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
   ```

2. 配置maven-compiler-plugin（IDE 编译时会读取此配置）

   ```xml
   <properties>
   	<!-- maven-compiler-plugin 会自动读取此配置 -->
   	<maven.compiler.source>1.8</maven.compiler.source>
   	<maven.compiler.target>1.8</maven.compiler.target>
   </properties>
   ```

## 制作本地 archetype

```text
mvn archetype:create-from-project
rm -rf target/generated-sources/archetype/src/main/resources/archetype-resources/.idea 
cp .gitignore target/generated-sources/archetype/src/main/resources/archetype-resources
#  <Main-Class>${groupId}.Application</Main-Class>
vim target/generated-sources/archetype/src/main/resources/archetype-resources/pom.xml

# cp .gitignore to the target jar
# add below config to maven-archetype-plugin
# <configuration><useDefaultExcludes>false</useDefaultExcludes></configuration>
vim target/generated-sources/archetype/pom.xml

cd target/generated-sources/archetype
mvn clean install
mvn archetype:crawl
# 推送到远程仓库
# 添加 <distributionManagement>
vim target/generated-sources/archetype/pom.xml
mvn clean deploy
```

## 从 archetype 创建项目

```text

mvn archetype:generate \
-DarchetypeCatalog=local \
-DarchetypeGroupId=com.github.zzf \
-DarchetypeArtifactId=jar-basic-archetype \
-DarchetypeVersion=1.0-RELEASE \
-DinteractiveMode=true


mvn archetype:generate \
-DarchetypeCatalog=local \
-DarchetypeGroupId=com.github.zzf \
-DarchetypeArtifactId=jar-basic-archetype \
-DinteractiveMode=true
-DarchetypeVersion=1.1-RELEASE \
-DinteractiveMode=true

```