# Exploratory Projects

These projects are explorations into various algorithms I would like to know more about. Each directory has its own
README.md file which gives more information about the project. An overview of the different projects is given below.

All projects can be built using `./gradlew build`

# library
This is a May 2026 rollup of different code I have written. It has the flexiblity of being reusable module should the
need arise.

Run `./gradlew build` in the utils directory of the library project to build it. The library is located in
library/utils/build/libs directory.

## sorting_app
This is a standalone application for estimating timing of the various sorting algorithms in the library project.

To build this program, use `./gradlew build` in the sorting_app directory. Note, you must have first built the library
project. The runnable jar file is located in the sorting_app/app/build/libs directory.

This is heavily modified code written in May 2026 of code from 2019.

## strings
This is code written in May 2026. It will be moving into the library project where tests will be added.

To build this program, use `./gradlew build` in the strings directory. The runnable jar file is located in the
strings/build/libs directory.
