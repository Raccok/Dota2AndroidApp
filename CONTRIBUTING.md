## About

This is a quick guide defining some conventions and helping with the basics of using Git.

## How to contribute

When contributing to this project, please follow some basic conventions listed below. This helps immensely when working on a project with multiple people. In a real job such conventions may just be mandatory as well, so, as a beginner, the sooner you get used to it the better.

In the following, replace `YourUsername` with your actual GitHub username. Provided are command line `git` commands. If you use Git in a GUI, you might still be able to use these commands there. Otherwise, you should easily be able to figure out how you can realize each command in the GUI.

### Setting up your development environment

- Create your own fork of this project
- On your system, inside the directory of your choice, use
```
git clone https://github.com/YourUsername/Dota2AndroidApp.git
```
(or `git clone git@github.com:YourUsername/Dota2AndroidApp.git` if you set up SSH authentication)
- Recommended: Set up Android Studio with a virtual Android device as described in `README.md`. Other setups are not officially tested/supported

### Implementing code changes

- (1) Whenever you add/change something (feature, refactoring or bug fix) there should be a corresponding open issue describing what should be done. If this isn't the case, simply open the issue yourself
- Typically you want to implement changes for the official `master` branch. However, in specific situations, use `git checkout <branch>` to use the corresponding branch as the basis for your changes
- Start a new branch with
```
git checkout -b <new branch>
```
where `<new branch>` must be named following the pattern `<task>/<description>-#<issue number>`, where
```
<task>: feature/refactoring/issue/organization, whichever of those fits best
<description>: about 1-3 words describing very briefly what is added/changed/fixed
<issue number>: the issue number from (1) above
```
(e.g. `feature/AwesomeNewFeature-#100`)
- Push the new branch to your fork with `git push -u origin <new branch>`
- When coding, follow the code style provided with the project for Android Studio. File > Settings > Editor > Code Style > Scheme should be set to Project after loading/opening the project
- Member variables must be named like `m<variable name>` and `<variable name>` must be written in camel case (e.g. `mMemberVariable`). The same goes for static variables and the `s<variable name>` convention (e.g. `sStaticVariable`).
- Avoid unnecessary whitespace. There must be no whitespace at the end of a line. There should be no unneeded whitespace between lines unless it improves readability significantly
- Lines should not be longer than 100 characters. In rare cases, it may be useful to exceed this limit **slightly** for better readability
- On your system, inside your fork cloned before, add new/changed files with `git add <file or entire directory>`
- Commit your additions/changes with `git commit -m "<message>"` where `<message>` describes briefly what has been added/changed. Try committing smaller intermediate steps instead of larger confusing blocks of code
- Push your additions/changes with `git push` to your fork

### Proposing code changes

- In your fork on GitHub, select the branch with additions/changes you want to propose and create a new pull request to the official `master` branch
- Whenever you add/change something later while the pull request is being discussed, implement the changes in a new commit and push it to the same branch in your fork as before. Then, these changes will be clearly visible in the pull request as an update
