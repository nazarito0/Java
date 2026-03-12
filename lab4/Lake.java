// Інтерфейс для живих істот у водоймі
interface Swimable {
    void swim();
}

// Абстрактний клас для водойм
abstract class WaterBody {
    protected String name;

    public WaterBody(String name) {
        this.name = name;
    }

    public abstract void describe();  // Абстрактний метод
}

// Sealed class для обмеження типів води
sealed class Water permits FreshWater, SaltWater {
    protected String waterType;

    public void showWaterType() {
        System.out.println("Water type: " + waterType);
    }
}

// Конкретні типи води
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

// Основний клас озера
public class Lake extends WaterBody {

    private Water water;  // Композиція: озеро має воду

    public Lake(String name) {
        super(name);
        this.water = new FreshWater();  // Озеро завжди має прісну воду
    }

    @Override
    public void describe() {
        System.out.println("Lake name: " + name);
        water.showWaterType();
    }

    // Вкладений статичний клас для риби
    public static class Fish implements Swimable {
        private String species;

        public Fish(String species) {
            this.species = species;
        }

        @Override
        public void swim() {
            System.out.println(species + " is swimming!");
        }

        // Статичний метод для загальної інформації про риб
        public static void fishFacts() {
            System.out.println("Most fish can breathe underwater using gills.");
        }
    }

    // Локальний клас для острова
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

        // Використання абстрактного методу
        lake.describe();

        // Використання статичного класу та інтерфейсу
        Fish fish = new Fish("Salmon");
        fish.swim();
        Fish.fishFacts();

        // Використання локального класу
        lake.createIsland();
    }
}