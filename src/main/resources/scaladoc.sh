#!/bin/bash
#
#    Copyright 2021 Can Koluman
#
#    Licensed under the Apache License, Version 2.0 (the "License");
#    you may not use this file except in compliance with the License.
#    You may obtain a copy of the License at
#
#        http://www.apache.org/licenses/LICENSE-2.0
#
#    Unless required by applicable law or agreed to in writing, software
#    distributed under the License is distributed on an "AS IS" BASIS,
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#    See the License for the specific language governing permissions and
#    limitations under the License.
#

# This is a small shell script, which is run during the maven packaging phase.
# It must be run after the compile phase, and generates Scaladoc 3 documentation

# project name
PROJECT_NAME="$1"
# project version
PROJECT_VERSION="$2"
# copyright notice
PROJECT_FOOTER="$3"
# scm link
SCM_LINK="$4"
# scaladoc root
SCALADOC_ROOT="$5"
# set options
sed "s^CLASS_PATH^$(cat target/classes/dep-list)^g;s^PROJECT_NAME^$PROJECT_NAME^; \
      s^PROJECT_VERSION^$PROJECT_VERSION^;s^PROJECT_FOOTER^$PROJECT_FOOTER^; \
      s^SCM_LINK^$SCM_LINK^;" target/classes/options.tmp > target/classes/options
# enumerate docfiles
mkdir -p target/site/scaladocs
find target/classes -name "*.tasty" > target/classes/files
# SCALADOC_ROOT="${SCALADOC_ROOT#\~#HOME}"
SCALADOC_ROOT="$(echo "${SCALADOC_ROOT}" | sed "s#\~#$HOME#")"
CMD="$SCALADOC_ROOT/scala3-doc @target/classes/options @target/classes/files"
echo "running $CMD ..."
$CMD
