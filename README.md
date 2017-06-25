# NyaaPantsu android app [![Build Status](https://travis-ci.org/NyaaPantsu/NyaaPantsu-android-app.svg?branch=master)](https://travis-ci.org/NyaaPantsu/NyaaPantsu-android-app)

Android app built for NyaaPantsu

## Build
Clone the git repo
`git clone https://github.com/NyaaPantsu/NyaaPantsu-android-app.git`

Build with gradle:
`cd NyaaPantsu-android-app & ./gradlew clean build`

You'll find the unsigned apk in /app/build/outputs/apk/releases/

## Contributing
You can contribute by forking this repo and make Pull Request. Please do understand that it is a side project, therefore the review might take a little longer.

## Improve or add translation
Two possibilities

### With Android Studio (easy)
You can tranlsate the app easily with the translation editor in Android Studio. First, clone the repo and open it with Android Studio (version >=3). Then you just need to PR the changed files.

### By XML
If you want to edit or add a translation, you must know the androidLanguageCode of your language. It can be found here  https://github.com/championswimmer/android-locales

To edit, go to /app/src/main/res/values-{androidLanguageCode} and download `strings.xml`. Each translation are inside a string xml tag with a __name__ attribute which corresponds to the string id. The __name__ attribute must be the same in every language. When adding a new tag with a new __name__ attribute, you have to add its english couterpart in /app/src/main/res/values/strings.xml, so every translator can know which translation they are missing.

To add a new language, you have to download /app/src/main/res/values/strings.xml, translate every text inside xml tag and you mustn't touch to the attribute __name__! When you have done that, create a new folder in /app/src/main/res/ name as `values-{androidLanguageCode}` and upload your translated strings.xml in that folder.

As you can see, there is already a translation for en-rUS. You see how it works.
