public class Lake {

    private String name;
    private Water water;

    public Lake(String name) {
        this.name = name;
        this.water = new Water();
    }

    public void describeLake() {
        System.out.println("Lake name: " + name);
        water.showWaterType();
    }

    private class Water {
        private String waterType = "Freshwater";

        public void showWaterType() {
            System.out.println("Water type: " + waterType);
        }
    }

    public static class Fish {
        private String species;

        public Fish(String species) {
            this.species = species;
        }

        public void swim() {
            System.out.println(species + " is swimming!");
        }
    }

    public void createIsland() {
        class Island {
            private String islandName;

            public Island(String islandName) {
                this.islandName = islandName;
            }

            public void showIslandInfo() {
                System.out.println("Island name: " + islandName + " located in the lake.");
            }
        }

        Island island = new Island("Dream Island");
        island.showIslandInfo();
    }

    public static void main(String[] args) {
        Lake lake = new Lake("Blue Lake");

        lake.describeLake();

        Fish fish = new Fish("Salmon");
        fish.swim();

        lake.createIsland();
    }
}
