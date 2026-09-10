#!/bin/sh
LIB=/usr/lib/x86_64-linux-gnu/libmimalloc.so
if [ -f "$LIB" ]; then
    export LD_PRELOAD="$LIB"
fi
exec LD_PRELOAD=/usr/lib/x86_64-linux-gnu/libmimalloc.so /opt/widgets/bin/widgets "$@"