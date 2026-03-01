make-ls:
	@echo "Making ls..."
	@ls

download-open-api-sample:
	@echo "Downloading Open API sample..."
	@curl -o src/main/resources/open-api-sample.json https://petstore3.swagger.io/api/v3/openapi.json
