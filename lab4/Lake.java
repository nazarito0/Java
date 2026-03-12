
interface Swimable {
    void swim();
}


abstract class WaterBody {
    protected String name;

    public WaterBody(String name) {
        this.name = name;
    }

    public abstract void describe();
}


sealed class Water permits FreshWater, SaltWater {
    protected String waterType;

    public void showWaterType() {
        System.out.println("Water type: " + waterType);
    }
}


final class FreshWater extends Water {
    public FreshWater() {
        this.waterType = "Freshwater";
    }
}

final class SaltWater extends Water {
    public SaltWater() {
        this.waterType = "Saltwater";
    }
}


public class Lake extends WaterBody {

    private Water water;

    public Lake(String name) {
        super(name);
        this.water = new FreshWater();
    }

    @Override
    public void describe() {
        System.out.println("Lake name: " + name);
        water.showWaterType();
    }


    public static class Fish implements Swimable {
        private String species;

        public Fish(String species) {
            this.species = species;
        }

        @Override
        public void swim() {
            System.out.println(species + " is swimming!");
        }


        public static void fishFacts() {
            System.out.println("Most fish can breathe underwater using gills.");
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


        lake.describe();


        Fish fish = new Fish("Salmon");
        fish.swim();
        Fish.fishFacts();


        lake.createIsland();
    }
}