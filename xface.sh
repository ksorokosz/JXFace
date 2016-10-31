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

java -jar XFace.jar $face $language $pho_file $recording $animation &
animation_pid=$!
wait $animation_pid

# Converting to AVI format
ffmpeg -i $animation.ogv -ab 448k -ar 48000 -vb 448k -vf "crop=790:565:5:5" -y $animation.avi
rm $animation.ogv
