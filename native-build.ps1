<#
.SYNOPSIS
    Builds the JARcade standalone native executable with GraalVM Native Image.

.DESCRIPTION
    GraalVM Native Image on Windows must run inside the MSVC (Visual Studio)
    developer environment, and the Gradle native build tools need to know where
    GraalVM lives. This script wires both up so you can just run it.

    Steps it performs:
      1. Resolves the GraalVM JDK (from -GraalVmHome, $env:GRAALVM_HOME, or the
         'native-image' command on PATH).
      2. Locates the VS 2022 (or newer) C++ toolchain via vswhere and loads its
         vcvars64.bat developer environment.
      3. Runs `gradlew <Task>` (default: nativeCompile) with GRAALVM_HOME set.

    Output: build\native\nativeCompile\JArcade.exe

.PARAMETER GraalVmHome
    Path to a GraalVM JDK. Defaults to $env:GRAALVM_HOME, then auto-detection.

.PARAMETER Task
    Gradle task to run. Defaults to 'nativeCompile'.

.PARAMETER ExtraArgs
    Any additional arguments are forwarded to Gradle.

.EXAMPLE
    ./native-build.ps1

.EXAMPLE
    ./native-build.ps1 -GraalVmHome "C:\tools\graalvm-jdk-21"

.NOTES
    If PowerShell blocks the script, run it as:
        powershell -ExecutionPolicy Bypass -File .\native-build.ps1
#>
[CmdletBinding()]
param(
    [string]$GraalVmHome = $env:GRAALVM_HOME,
    [string]$Task = 'nativeCompile',
    [Parameter(ValueFromRemainingArguments = $true)]
    [string[]]$ExtraArgs
)

$ErrorActionPreference = 'Stop'

function Stop-WithError([string]$Message) {
    Write-Host "ERROR: $Message" -ForegroundColor Red
    exit 1
}

# 1. Resolve GraalVM -----------------------------------------------------------
if (-not $GraalVmHome) {
    $nativeImage = Get-Command native-image -ErrorAction SilentlyContinue
    if ($nativeImage) {
        # ...\bin\native-image.cmd -> strip 'bin' and the exe name
        $GraalVmHome = Split-Path (Split-Path $nativeImage.Source -Parent) -Parent
    }
}

if (-not $GraalVmHome) {
    Stop-WithError "GraalVM not found. Set GRAALVM_HOME or pass -GraalVmHome <path>."
}

$nativeImageCmd = Join-Path $GraalVmHome 'bin\native-image.cmd'
if (-not (Test-Path $nativeImageCmd)) {
    Stop-WithError "'$GraalVmHome' does not look like a GraalVM JDK (missing bin\native-image.cmd)."
}
$GraalVmHome = (Resolve-Path $GraalVmHome).Path

# 2. Resolve the MSVC developer environment ------------------------------------
$vswhere = Join-Path ${env:ProgramFiles(x86)} 'Microsoft Visual Studio\Installer\vswhere.exe'
$vcvars = $null

if (Test-Path $vswhere) {
    $vsPath = & $vswhere -latest -products * `
        -requires Microsoft.VisualStudio.Component.VC.Tools.x86.x64 `
        -property installationPath 2>$null

    if ($vsPath) {
        $candidate = Join-Path $vsPath 'VC\Auxiliary\Build\vcvars64.bat'
        if (Test-Path $candidate) { $vcvars = $candidate }
    }
}

if (-not $vcvars) {
    Stop-WithError ("Visual Studio Build Tools with the C++ (MSVC) workload were not found. " +
        "Install the 'Desktop development with C++' workload.")
}

# 3. Run Gradle inside that environment ----------------------------------------
$gradleArgs = @($Task) + $ExtraArgs
$cmdLine = 'call "{0}" >nul && set "GRAALVM_HOME={1}" && gradlew.bat {2}' -f `
    $vcvars, $GraalVmHome, ($gradleArgs -join ' ')

Write-Host "GraalVM : $GraalVmHome"
Write-Host "vcvars  : $vcvars"
Write-Host "Gradle  : $($gradleArgs -join ' ')"
Write-Host ""

Push-Location $PSScriptRoot
try {
    & cmd.exe /c $cmdLine
    $exitCode = $LASTEXITCODE
}
finally {
    Pop-Location
}

exit $exitCode
