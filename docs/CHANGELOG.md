# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.7.0-beta] - 2022-07-09

### Added

- Chinese translation. Thanks to [ZHAY10086](https://github.com/ZHAY10086)!

## [1.6.0] - 2022-06-16

### Added
- New (and old) books to whitelist (#15). Thanks [62832](https://github.com/62832)!
- Russian translation. Thanks to [DEV14NCE](https://www.curseforge.com/members/dev14nce)!

### Changed
- Existing aliases (#15). Thanks [62832](https://github.com/62832)!

## [1.5.0] - 2022-04-25
### Added
- Blacklist some books from the mods Ars Nouveau and Corail Tombstone (#14). Thanks [62832](https://github.com/62832)!

## [1.4.0] - 2022-04-24
### Changed
- Split common and forge into separate projects

## [1.3.1] - 2022-04-12
### Changed
- Moved client event registration into separate method (#12)

## [1.3.0] - 2022-04-11
### Added
- Overlay for previewing tome conversion
### Changed
- Organized subpackages

## [1.2.1] - 2022-04-09
### Added
- Default item blacklist thanks to @jeremiahwinsley (#11)
### Changed
- Switched from `defineList` to `defineListAllowEmpty` in configuration

## [1.2.0] - 2022-04-08
### Added
- Added configurable item blacklist (#11)

## [1.1.0] - 2022-04-06
### Changed
- Fixed `Shift` + `Right-click` for converting tome (#10)

## [1.0.4] - 2022-04-05
### Changed
- Allowed multiple books from the same mod to be stored in the Tome (#7,#8)
- Fixed issue where removed mods stayed in the Tome and would turn it into air (#6)

## [1.0.3] - 2022-04-03
### Changed
- Fixed issue where books stored within the tome would "pile" on top of each other in the GUI (#5)

## [1.0.2] - 2022-04-03
### Changed
- Fixed issue preventing mod from being run on a dedicated server (#4)

## [1.0.1] - 2022-04-03
### Changed
- Fixed issue causing certain Patchouli guide books to overwrite each other (#3)

## [1.0.0] - 2022-03-23
