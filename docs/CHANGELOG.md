# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Tome to "Tools and Utilities" tab in creative mode (#42)

### Fixed

- Tome missing from creative mode and JEI search (#42)

### Blacklisted Items

- Minecraft book (#40)

## [1.10.1] - 2023-06-11

### Added

- Korean translation. Thanks to [smoong951](https://github.com/smoong951)!

## [1.10.0] - 2023-04-30

### Added

- Whitelisted Astrodux from Ad Astra!
- Tag-based whitelist (#37)

## [1.9.1] - 2022-12-09

### Updated

- Tome texture

## [1.9.0] - 2022-10-13

### Added

- Close tome screen using inventory key (#28)

Thanks [NyanMC](https://github.com/NyanMC) for the following additions to the default config

Blacklisted items

- Blue Skies Trader Journal

### Removed

- Checking for Minecraft items when determining which items can be attached to the Tome (#30)

## [1.9.0-beta] - 2022-09-17

### Added

- Logging to aid in the hunt for #25

### Changed

- Check if tome is converted before showing overlay (#26)

## [1.8.0] - 2022-07-31

Thanks [NyanMC](https://github.com/NyanMC) for the following additions to the default config

### Added

Whitelisted items

- Runic Tablet from Roots Classic
- The Acknowledgment from Enigmatic Legacy

Blacklisted items

- Enchanted Book from Minecraft
- Tome from Projecte
- Ancient Tome from Quark

## [1.7.0] - 2022-07-23

### Fixed

- Fixed exclusions for Dark Utilities in 1.16.5 (#22, #23). Thanks [NyanMC](https://github.com/NyanMC)!

## [1.7.0-beta.2] - 2022-07-23

### Changed

- Exclude blocks from being added to the Tome (#21)

## [1.7.0-beta.1] - 2022-07-23

### Fixed

- Fixed breaking changes from Forge 41.0.110 (#20)

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
