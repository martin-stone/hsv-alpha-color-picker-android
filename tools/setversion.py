import re

files = ["../README.md", "../colorpicker/build.gradle", "../demo_app/build.gradle"]

new_version_code = "30100"
new_version_string = "3.1.0"

for file in files:
    with open(file, "rt") as f:
        old = f.read()
    new = old
    new = re.sub("versionCode \d+", "versionCode "+new_version_code, new)
    new = re.sub("versionName '[\d.]+'", "versionName '{}'".format(new_version_string), new)
    new = re.sub("version = '[\d.]+'", "version = '{}'".format(new_version_string), new)
    new = re.sub("implementation 'com.github.martin-stone:hsv-alpha-color-picker-android:[\d.]+'",
        "implementation 'com.github.martin-stone:hsv-alpha-color-picker-android:{}'".format(new_version_string), new)

    with open(file, "wt") as f:
        f.write(new)