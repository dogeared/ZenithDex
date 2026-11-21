# Changelog

All notable changes to ZenithDex are documented here. Versions match the git tags.

## Unreleased

### Added
- Tabletop (laptop) posture layout for foldables such as iPhone Duo: the card and its details fill the upright half of the screen, and the navigation arrows, alphabet bar, and filters move to the flat half.

### Changed
- Card sprite positions are percentage based, so the card image can scale to fit the screen.

## 0.7.0 - 2026-11-21

### Added
- CI workflow: build and test on every push; publish the image to GHCR and deploy to the NAS from `main`.
- Backend and browserless Vaadin frontend tests, with JaCoCo enforcing 90% line coverage.
- Page footer.
- README with stack, usage, and status badges.

### Changed
- Upgraded to Spring Boot 4.1 and Vaadin 25.3, migrating the Jackson and Vaadin theming APIs.
- Moved all CSS into an external stylesheet.

### Fixed
- Browsers too old for Vaadin 25 saw a blank 403 page instead of the "browser too old" fallback; the root route and `/error` are now permitted.

## 0.6.1 - 2026-05-06

### Fixed
- CSS fix.

## 0.6 - 2026-05-06

### Changed
- Filters are collapsed by default to save screen space.

## 0.5 - 2026-05-06

### Added
- Alphabet bar to jump to cards by their first letter.

## 0.4 - 2026-05-06

### Changed
- On mobile, navigation buttons sit to the left and right of the card image.

## 0.3 - 2026-03-18

### Added
- `docker-compose.yml`.

## 0.2 - 2026-03-18

### Changed
- Style tweaks for mobile.

## 0.1 - 2026-02-25

### Added
- Initial release: card browser as the front door, with sorting, mobile styling, externalized styles, and a Dockerfile.
