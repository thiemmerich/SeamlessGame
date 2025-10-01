# Java Seamless Game
### A 2D platformer game built with Java and LibGDX featuring seamless world exploration, character animations, and RPG elements.

#### Features
* Seamless World: Tibia-style continuous world with multiple interconnected rooms
* Character Animations: 4-directional movement animations (front, back, left, right)
* RPG Elements: Health system with visual health bars and name tags
* Smooth Camera: Camera follows player with boundary clamping
* Collision System: Smart room transitions and boundary detection
* Memory Optimized: Efficient texture caching and resource management

#### Controls
* WASD - Move character
* Q - Take damage (debug)
* E - Heal (debug)

#### Assets
* Character sprites: 64x96 PNG with 4x4 animation grid
* Room backgrounds: Programmatically generated colored areas
* UI elements: Health bars and name tags rendered dynamically

#### Technical Details
* Engine: LibGDX 1.12.1
* Rendering: Orthographic camera with sprite batching
* Animation: Frame-based animation system
* Memory: Optimized texture caching and disposal
* Physics: Custom collision and boundary detection

#### World Layout
##### The game features a grid-based world with different themed rooms:
* Forest (0,0) - Starting area
* Cave (1,0) - East of forest
* Beach (0,1) - North of forest
* Volcano (-1,0) - West of forest
* Desert (0,-1) - South of forest

#### Performance
* Target: 60 FPS
* Memory: < 100MB typical usage
* Rendering: Only visible rooms rendered
* Textures: Cached and shared across instances

#### License
This project is licensed under the MIT License