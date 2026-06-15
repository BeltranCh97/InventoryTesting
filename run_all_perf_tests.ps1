$ErrorActionPreference = "Stop"

$scriptsDir = "perf\scripts"
$resultsDir = "perf\results"

if (-Not (Test-Path -Path $resultsDir)) {
    New-Item -ItemType Directory -Path $resultsDir
}

Write-Host "Iniciando ejecución básica (baseline) según el taller..."
k6 run --env SCENARIO=baseline $scriptsDir\register_product_k6.js --out json=$resultsDir\baseline_basic.json

Write-Host "Iniciando escenario: Baseline..."
k6 run --env SCENARIO=baseline $scriptsDir\register_product_k6.js --out json=$resultsDir\baseline.json

Write-Host "Iniciando escenario: Carga..."
k6 run --env SCENARIO=load $scriptsDir\register_product_k6.js --out json=$resultsDir\load.json

Write-Host "Iniciando escenario: Estrés..."
k6 run --env SCENARIO=stress $scriptsDir\register_product_k6.js --out json=$resultsDir\stress.json

Write-Host "Iniciando escenario: Spike..."
k6 run --env SCENARIO=spike $scriptsDir\register_product_k6.js --out json=$resultsDir\spike.json

Write-Host "Iniciando escenario: Soak..."
k6 run --env SCENARIO=soak $scriptsDir\register_product_k6.js --out json=$resultsDir\soak.json

Write-Host "¡Todas las pruebas de rendimiento han finalizado exitosamente!"
