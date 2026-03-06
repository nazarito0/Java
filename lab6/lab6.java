import java.util.*;

class Lake implements Comparable<Lake> {
    private String name;
    private Water water;

    public Lake(String name) {
        this.name = name;
        this.water = new Water();
    }

    public String getName() {
        return name;
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

        @Override
        public String toString() {
            return species;
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

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Lake)) return false;
        Lake other = (Lake) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public int compareTo(Lake other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return name;
    }

    public static void main(String[] args) {

        List<Lake> lakeList = new ArrayList<>();
        lakeList.add(new Lake("Blue Lake"));
        lakeList.add(new Lake("Green Lake"));
        lakeList.add(new Lake("Red Lake"));
        lakeList.add(new Lake("Blue Lake"));
        lakeList.add(new Lake("Yellow Lake"));

        System.out.println("Original ArrayList:");
        System.out.println(lakeList);

        Set<Lake> lakeHashSet = new HashSet<>(lakeList);
        System.out.println("\nHashSet (unique elements):");
        System.out.println(lakeHashSet);

        Collections.sort(lakeList);
        System.out.println("\nSorted ArrayList:");
        System.out.println(lakeList);

        Set<Lake> lakeTreeSet = new TreeSet<>(lakeList);
        System.out.println("\nTreeSet (unique & sorted):");
        System.out.println(lakeTreeSet);

        Map<String, Lake> lakeTreeMap = new TreeMap<>();
        for (Lake lake : lakeList) {
            lakeTreeMap.put(lake.getName(), lake);
        }
        System.out.println("\nTreeMap (key=name, value=Lake):");
        for (Map.Entry<String, Lake> entry : lakeTreeMap.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }

        LinkedList<Lake> lakeLinkedList = new LinkedList<>(lakeList);
        System.out.println("\nLinkedList:");
        System.out.println(lakeLinkedList);

        Queue<Lake> lakeQueue = new LinkedList<>(lakeList);
        System.out.println("\nQueue (FIFO):");
        while (!lakeQueue.isEmpty()) {
            System.out.println(lakeQueue.poll());
        }

        PriorityQueue<Lake> lakePriorityQueue = new PriorityQueue<>(lakeList);
        System.out.println("\nPriorityQueue (sorted by natural order):");
        while (!lakePriorityQueue.isEmpty()) {
            System.out.println(lakePriorityQueue.poll());
        }

        Lake.Fish salmon = new Lake.Fish("Salmon");
        salmon.swim();
    }
}
