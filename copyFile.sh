#!/usr/bin/env bash
cd /Users/yedongqin/Desktop/IOCDemo/taopiaopiao
#cd taopiaopiao
gradle  assembleRelease
adb push /Users/yedongqin/Desktop/IOCDemo/taopiaopiao/build/outputs/apk/release/taopiaopiao.apk /sdcard/


