#!/bin/bash
while pgrep -x "widgets" > /dev/null; do
    sleep 0.1
done

sleep 1

gtk-launch widgets-widgets
rm -f "${BASH_SOURCE[0]}"