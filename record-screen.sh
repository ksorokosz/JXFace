#!/bin/bash -ue
# Command line interface for XFace
# It allows to create an animation for specified pho and recording files and store them as AVI file
#
animation=$1; shift

handler()
{
	kill -s SIGINT $recording_pid
	echo -e "killed $recording_pid" >&2
}

window_id=`xwininfo -name xface-j | grep "Window id" | perl -pe "s|.*Window id: (.*?) .*|\1|"`
recordmydesktop --v_bitrate 2000000 --s_quality 10 --no-wm-check --fps 120 --no-cursor --overwrite --windowid $window_id --device pulse_monitor -o $animation &
recording_pid=$!

trap handler SIGINT
wait $recording_pid
echo -e "wait recording pid" >&2
