#!/bin/bash

WINDOW_NAME="$1"

if [ -z "$WINDOW_NAME" ]; then
  exit 1
fi

TIMEOUT=30
INTERVAL=2
ELAPSED=0
WIN_ID=""

while [ $ELAPSED -lt $TIMEOUT ]; do
  WIN_ID=$(wmctrl -l | grep -i "$WINDOW_NAME" | awk '{print $1}' | head -n 1)

  if [ -n "$WIN_ID" ]; then
    break
  fi

  sleep $INTERVAL
  ELAPSED=$((ELAPSED + INTERVAL))
done

if [ -z "$WIN_ID" ]; then
  exit 1
fi

xprop -id "$WIN_ID" -f _NET_WM_WINDOW_TYPE 32a \
  -set _NET_WM_WINDOW_TYPE _NET_WM_WINDOW_TYPE_DESKTOP

xprop -id "$WIN_ID" -f _NET_WM_STATE 32a -set _NET_WM_STATE \
"_NET_WM_STATE_SKIP_TASKBAR, _NET_WM_STATE_SKIP_PAGER, _NET_WM_STATE_BELOW, _NET_WM_STATE_STICKY"

