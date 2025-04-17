# Get all node processes
$nodeProcesses = Get-CimInstance Win32_Process | Where-Object { $_.Name -eq "node.exe" }

foreach ($proc in $nodeProcesses) {
    if ($proc.CommandLine -like "*webpack.js serve*") {
        Write-Host "Killing PID $($proc.ProcessId) - $($proc.CommandLine)"
        Stop-Process -Id $proc.ProcessId -Force
    }
}

Write-Host "Done."
pause
