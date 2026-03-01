make-ls:
	@echo "Making ls..."
	@ls

download-open-api-sample:
	@echo "Downloading Open API sample..."
	@curl -o src/main/resources/open-api-sample.json https://petstore3.swagger.io/api/v3/openapi.json

filter-and-generate-openapi:
	@echo "Filtering Open API..."
	@cd src/main/resources && \
		npx -y openapi-format openapi-sample.yaml \
		-o openapi-sample.filtered.yaml \
		--filterFile filter-config.yaml \
		--sortComponentsProps
	@echo "Generating code from Open API..."
	@./gradlew clean generateOpenApi
	@echo "Cleaning up filtered yaml"
	@rm -f src/main/resources/openapi-sample.filtered.yaml
	@echo "Generated Successfully"
