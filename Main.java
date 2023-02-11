public class Main {
    public static void main(String[] args){
        Movie HarryPotter = new Movie("Harry Potter", 0);
        Rental harryRental = new Rental(HarryPotter, 3);
        Customer johnny = new Customer("Johnny");
        johnny.addRental(harryRental);
        System.out.println(johnny.statement());
        System.out.println(johnny.htmlStatement());
    }
}