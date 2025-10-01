package br.com.emmerich.environment;

public class World {
//    private Map<String, Room> rooms;
//    private Room currentRoom;
//    private String currentRoomId;
//
//    public World() {
//        this.rooms = new HashMap<>();
//        initializeWorld();
//    }
//
//    private void initializeWorld() {
//        // Create LARGER rooms for exploration (1600x1200 instead of 800x600)
//        Room room1 = new Room("forest", 1600, 1200, new Color(0.2f, 0.6f, 0.3f, 1f));
//        Room room2 = new Room("cave", 1600, 1200, new Color(0.3f, 0.3f, 0.4f, 1f));
//        Room room3 = new Room("beach", 1600, 1200, new Color(0.8f, 0.8f, 0.4f, 1f));
//        Room room4 = new Room("volcano", 1600, 1200, new Color(0.6f, 0.2f, 0.1f, 1f));
//
//        // Connect rooms to this world instance
//        room1.setWorld(this);
//        room2.setWorld(this);
//        room3.setWorld(this);
//        room4.setWorld(this);
//
//        // Set up exits - adjust positions for larger rooms
//        // Room 1 (forest) exits
//        room1.setNorthExit(750, 100, "cave");      // Top center exit to cave
//        room1.setEastExit(550, 100, "beach");      // Right center exit to beach
//        room1.setSouthExit(750, 100, "volcano");   // Bottom center exit to volcano
//
//        // Room 2 (cave) exits
//        room2.setSouthExit(750, 100, "forest");
//
//        // Room 3 (beach) exits
//        room3.setWestExit(550, 100, "forest");
//
//        // Room 4 (volcano) exits
//        room4.setNorthExit(750, 100, "forest");
//
//        // Add rooms to world
//        rooms.put("forest", room1);
//        rooms.put("cave", room2);
//        rooms.put("beach", room3);
//        rooms.put("volcano", room4);
//
//        // Start in forest
//        currentRoomId = "forest";
//        currentRoom = room1;
//    }
//
//    public void update(float deltaTime) {
//        if (currentRoom != null) {
//            currentRoom.update(deltaTime);
//        }
//    }
//
//    public void render(SpriteBatch batch) {
//        if (currentRoom != null) {
//            currentRoom.render(batch);
//        }
//    }
//
//    public void changeRoom(String newRoomId, String fromDirection) {
//        Room newRoom = rooms.get(newRoomId);
//        if (newRoom != null && !newRoomId.equals(currentRoomId)) { // Prevent switching to same room
//            // Transfer player to new room
//            MainPlayer player = currentRoom.getPlayer();
//            if (player != null) {
//                newRoom.setPlayer(player);
//                newRoom.positionPlayerFromExit(fromDirection);
//            }
//
//            // Update current room
//            currentRoom = newRoom;
//            currentRoomId = newRoomId;
//
//            System.out.println("SUCCESS: Transitioned to " + newRoomId + " from " + fromDirection);
//        } else {
//            System.out.println("FAILED: Cannot transition to " + newRoomId);
//        }
//    }
//
//    public void setPlayer(MainPlayer player) {
//        if (currentRoom != null) {
//            currentRoom.setPlayer(player);
//        }
//    }
//
//    public String getCurrentRoomId() {
//        return currentRoomId;
//    }
//
//    public Room getCurrentRoom() {
//        return currentRoom;
//    }
//
//    public void dispose() {
//        for (Room room : rooms.values()) {
//            room.dispose();
//        }
//    }
}
