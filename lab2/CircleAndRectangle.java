public class CircleAndRectangle {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("введіть радіус круга як аргумент командного рядка.");
            return;
        }

        double radius = Double.parseDouble(args[0]);

        double areaCircle = Math.PI * radius * radius;

        double areaRectangle = 2 * radius * radius;

        System.out.println("Площа круга: " + areaCircle);
        System.out.println("Площа вписаного прямокутника: " + areaRectangle);
    }
}
