param (
    [Parameter(Mandatory=$true)]
    [string]$Username,
    [Parameter(Mandatory=$true)]
    [string]$Password,
    [Parameter(Mandatory=$true)]
    [string]$Content,
    [string]$FilePath,
    [string]$Type = "POST",
    [double]$Lat,
    [double]$Lng
)

$GatewayUrl = "http://localhost:8080"

# 1. Login to get Token
Write-Host "Logging in as $Username..." -ForegroundColor Cyan
$LoginBody = @{
    username = $Username
    password = $Password
} | ConvertTo-Json

try {
    $LoginResponse = Invoke-RestMethod -Uri "$GatewayUrl/api/auth/login" -Method Post -Body $LoginBody -ContentType "application/json"
    $Token = $LoginResponse.token
} catch {
    Write-Error "Login failed: $($_.Exception.Message)"
    exit
}

Write-Host "Login successful! Uploading story..." -ForegroundColor Green

# 2. Upload Story
$StoryJson = @{
    content = $Content
    type = $Type
} | ConvertTo-Json

$Headers = @{
    Authorization = "Bearer $Token"
}

$Boundary = [System.Guid]::NewGuid().ToString()
$LF = "`r`n"

$Body = ""
$Body += "--$Boundary$LF"
$Body += "Content-Disposition: form-data; name=`"story`"$LF"
$Body += "Content-Type: application/json$LF$LF"
$Body += "$StoryJson$LF"

if ($FilePath) {
    if (Test-Path $FilePath) {
        $FileName = [System.IO.Path]::GetFileName($FilePath)
        $FileContent = [System.IO.File]::ReadAllBytes($FilePath)
        $FileContentStr = [System.Text.Encoding]::GetEncoding("ISO-8859-1").GetString($FileContent)
        
        $Body += "--$Boundary$LF"
        $Body += "Content-Disposition: form-data; name=`"file`"; filename=`"$FileName`"$LF"
        $Body += "Content-Type: application/octet-stream$LF$LF"
        $Body += "$FileContentStr$LF"
    } else {
        Write-Warning "File not found: $FilePath. Uploading without media."
    }
}

$Body += "--$Boundary--$LF"

$Url = "$GatewayUrl/api/stories"
$Params = @()
if ($Lat) { $Params += "lat=$Lat" }
if ($Lng) { $Params += "lng=$Lng" }
if ($Params.Count -gt 0) { $Url += "?" + ($Params -join "&") }

try {
    $Response = Invoke-RestMethod -Uri $Url -Method Post -Headers $Headers -Body $Body -ContentType "multipart/form-data; boundary=$Boundary"
    Write-Host "Story uploaded successfully!" -ForegroundColor Green
    $Response | ConvertTo-Json
} catch {
    Write-Error "Upload failed: $($_.Exception.Message)"
}
