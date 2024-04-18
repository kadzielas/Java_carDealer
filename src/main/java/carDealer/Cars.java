package carDealer;

import java.math.BigDecimal;
import java.util.Scanner;

public class BuyCar extends ListOfCars{
    public static void CarList(){
        Scanner scanner = new Scanner(System.in);
        int menu;

        CreateCar test1 = new CreateCar();
        test1.setID(12);
        test1.setManufacturer("Audi");
        test1.setModel("a7");
        test1.setPrice2(new BigDecimal("96000.00"));
        test1.setEngine2(new BigDecimal("3.5"));
        test1.setCurrency(Currency.PLN);
        test1.addCar(test1);

        CreateCar test2 = new CreateCar();
        test2.setID(5);
        test2.setManufacturer("Seat");
        test2.setModel("Ibiza");
        test2.setPrice2(new BigDecimal("8000.00"));
        test2.setEngine2(new BigDecimal("1.6"));
        test2.setCurrency(Currency.EUR);
        test2.addCar(test2);

        CreateCar test3 = new CreateCar();
        test3.setID(16);
        test3.setManufacturer("Skoda");
        test3.setModel("Fabia");
        test3.setPrice2(new BigDecimal("7000.00"));
        test3.setEngine2(new BigDecimal("1.4"));
        test3.setCurrency(Currency.USD);
        test3.addCar(test3);

        do {
            System.out.println("1.Show list of available cars");
            System.out.println("2.Find cars by manufacturer");
            System.out.println("3.Check price on loan");
            System.out.println("4.Home page\n");
            System.out.print("Where are we going?: ");
            menu = scanner.nextByte();
            System.out.println("\n");

            switch (menu){

                case 1:
                    carDealer.ListOfCars.showList();
                    System.out.println("\n");
                    break;

                case 2:
                    carDealer.ListOfCars.carStream();
                    System.out.println("\n");
                    break;

                case 3:
                    System.out.println("\n");
                    break;

                case 4:
            }
        } while (menu != 4);
    }
}