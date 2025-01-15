param(
    [switch]$Verbose,
    [switch]$Remove,
    [switch]$RemoveQuotes
)

# .env 파일에서 환경변수 읽기
if (Test-Path .env) {
    $variables = Select-String -Path .env -Pattern '^\s*[^\s=#]+=[^\s]{0,}$' -Raw

    foreach ($var in $variables) {
        $keyVal = $var -split '=', 2
        $key = $keyVal[0].Trim()
        $val = $RemoveQuotes ? $keyVal[1].Trim("'").Trim('"') : $keyVal[1]
        [Environment]::SetEnvironmentVariable($key, $Remove ? '' : $val)

        if ($Verbose) {
            Write-Host "$key=$([Environment]::GetEnvironmentVariable($key))"
        }
    }
} else {
    Write-Host ".env 파일이 존재하지 않습니다. 환경 변수를 설정하지 않고 계속 진행합니다." -ForegroundColor Yellow
}

# JAR 파일 경로 확인 및 실행
$jarFile = 'build\libs\lora-server-0.0.1-SNAPSHOT.jar'
if (Test-Path $jarFile) {
    Write-Host "JAR 파일 실행: $jarFile"
    java -jar $jarFile --spring.config.location=classpath:/application.yml,classpath:/application-production.yml
} else {
    Write-Host "JAR 파일을 찾을 수 없습니다: $jarFile" -ForegroundColor Red
}
