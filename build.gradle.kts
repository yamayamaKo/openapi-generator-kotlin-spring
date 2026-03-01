plugins {
	kotlin("jvm") version "2.2.21"
	kotlin("plugin.spring") version "2.2.21"
	id("org.springframework.boot") version "4.0.3"
	id("io.spring.dependency-management") version "1.1.7"

	// openapi-generator用のプラグイン
	id("org.openapi.generator") version "7.19.0"
}

group = "com.yamada"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("tools.jackson.module:jackson-module-kotlin")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

openApiGenerate {
	generatorName.set("kotlin-spring")
	inputSpec.set("$rootDir/src/main/resources/openapi-sample.yaml")
	outputDir.set("${layout.buildDirectory.get()}/generated")

	val packageName = "com.yamada.openapi_generator"
	apiPackage.set("${packageName}.api")
	modelPackage.set("${packageName}.model")

	// 不要なテスト・ドキュメントの生成を抑制
	generateModelTests.set(false)
	generateApiTests.set(false)
	generateApiDocumentation.set(false)
	generateModelDocumentation.set(false)

	// その他オプション設定
	configOptions.set(
		mapOf(
			"library" to "spring-declarative-http-interface",
			"gradleBuildFile" to "false",
			"useResponseEntity" to "false"
		)
	)

	// 生成前に
}