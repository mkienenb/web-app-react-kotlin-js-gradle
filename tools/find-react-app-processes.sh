for pid in $(pgrep java); do
  cwd=$(pwdx $pid 2>/dev/null | awk '{print $2}')
  if [ "$cwd" = "/home/mkienenb/IdeaProjects/gvea/web-app-react-kotlin-js-gradle" ]; then
    echo $pid
  fi
done | xargs -r ps -o pid,ppid,cmd ww -p
