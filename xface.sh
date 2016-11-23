#!/bin/bash -ue
# Command line interface for XFace
# It allows to create an animation for specified pho and recording files and store them as AVI file
#
# Example usage: 
# ./xface.sh karol polish men tests/karol.pho tests/karol.wav tests/karol

current_dir=`dirname $0`
cd $current_dir

face=$1; shift
language=$1; shift
sex=$1; shift
pho_file=$1; shift
recording=$1; shift
animation=$1; shift

# men: 0 64 128
# women: 128 0 0

if [ "$sex" == "men" ]; then
	red=0
	green=64
	blue=128
fi

if [ "$sex" == "women" ]; then
	red=128
	green=0
	blue=0
fi

java -jar XFace.jar $face $language $pho_file $recording $animation $red $green $blue &
animation_pid=$!
wait $animation_pid

# Converting to AVI format
ffmpeg -i $animation.ogv -ab 448k -ar 48000 -vb 448k -vf "crop=790:565:5:5" -ss 1 -y $animation.avi
rm $animation.ogv
