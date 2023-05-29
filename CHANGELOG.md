# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [3.0.0] - 2023-05-28

### Added

- Tooltip for stinger sword
- Tooltip for Antenna
- Compatibility with Bumblezone
	- You can enable or disable Antenna at bumblezone dimension
	- You can use the_bumblezone:bee_stinger to craft the Stinger Sword
- Compatibility with JEI
	- Itens have a description available
	- Stinger to poison potion brewing recipe appears at JEI
- Compatibility with Productive Bees
	- Antenna works for all productive bees
- Stinger sword at Minecraft swords tag
- New Queen Bee spawn egg texture
- Much more configs

### Fixed 

- Fix sound not following Queen Bee entity when flying
- Fix bees leaving bee_nest/beehive when using a bottle or shears when antenna is equipped

### Changed

- Complete rewrite of the mod, now I don't use MCreator anymore
- Bee loot table is now handled by Global Loot modifier
- Queen Bee changes texture when angry
- Queen Bee changes ambient sound when angry
- Poison nimbus color fits the new color of poison potion (1.19.4)
- Queen Bee poison nimbus has a cooldown of 10 seconds before being able to use again
- Queen Bee can summon angry bees if there is bees around but without stinger
- Queen Bee will check in a larger area for bees before trying to summon angry bees
- Queen Bee now has a cooldown before being able to summon angry bees instead of a chance
- Queen Bee now is faster (Same speed as bees)
- When a bee is angry it also makes Queen Bee angry
- Bees get angry with entities that are not the Player when Queen Bee is angry to that entity
- Poison nimbus radius changed from 4 to 7
- Poison nimbus only spawns when you are 7 blocks or less of distance from Queen Bee (Before was 8)
- Changed xp reward from 10 to 20
- Changed advancement background from stone to honeycomb block
- Improved beehive structure generation
- Changed structure name from bee_hive to beehive
- Changed bee_antenna to antenna
