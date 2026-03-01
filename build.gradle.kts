import org.jetbrains.kotlin.gradle.targets.js.npm.importedPackageDir
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
	kotlin("jvm") version "2.2.21"
	kotlin("plugin.spring") version "2.2.21"
	id("org.springframework.boot") version "4.0.3"
	id("io.spring.dependency-management") version "1.1.7"

	// openapi-generator用のプラグイン
	id("org.openapi.generator") version "7.19.0"
}

group = "com.yamayamako"
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

	// swagger-annotationsの追加
	implementation("jakarta.validation:jakarta.validation-api")
	implementation("io.swagger.core.v3:swagger-annotations:2.2.20")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register<GenerateTask>("generateOpenApi") {
	generatorName.set("kotlin-spring")
	inputSpec.set("$rootDir/src/main/resources/openapi-sample.yaml")
	outputDir.set("${layout.buildDirectory.get()}/generated")

	val packageName = "com.yamayamako.openapi_generator"
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
			"useResponseEntity" to "false",
			"useTags" to "true",
		)
	)

	globalProperties.set(
		mapOf(
			"apis" to "Pet", // petのみ生成
			"models" to ""
		)
	)

	doFirst {
		// 生成前に出力ディレクトリをクリーンアップ
		delete(outputDir.get())
	}

	doLast {
		// 既存のコードを削除
		delete("src/main/kotlin/com/yamayamako/gen")

		// 生成したコードをコピー
		copy {
			from("${outputDir.get()}/src/main/kotlin/com/yamayamako/openapi_generator")
			into("src/main/kotlin/com/yamayamako/gen")
		}
	}
}
