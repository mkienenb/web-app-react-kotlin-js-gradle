@echo off
wmic process where "name='node.exe'" get ProcessId,Name,CommandLine > node_processes.txt
echo Node.js process list saved to node_processes.txt
pause
