#!/bin/bash -ue
# Command line interface for XFace
# It allows to create an animation for specified pho and recording files and store them as AVI file
#

current_dir=`dirname $0`
cd $current_dir

face=$1; shift
language=$1; shift
pho_file=$1; shift
recording=$1; shift
animation=$1; shift

java -jar XFace.jar $face $language $pho_file $recording &
animation_pid=$!
sleep 9

window_id=`xwininfo -name xface-j | grep "Window id" | perl -pe "s|.*Window id: (.*?) .*|\1|"`
recordmydesktop --v_bitrate 2000000 --s_quality 10 --no-wm-check --fps 120 --no-cursor --overwrite --windowid $window_id --device pulse_monitor -o $animation &
recording_pid=$!

wait $animation_pid
kill $recording_pid
wait $recording_pid

# Converting to AVI format
ffmpeg -i $animation.ogv -ab 448k -ar 48000 -vb 448k -vf "crop=790:565:5:5" $animation.avi
rm $animation.ogv