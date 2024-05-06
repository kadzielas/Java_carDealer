package com.car.dealer.service;

import com.car.dealer.common.Manufacturer;
import com.car.dealer.model.Car;
import com.car.dealer.model.CarList;
import com.car.dealer.model.Loan;
import com.car.dealer.validator.CarValidator;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;


public class CarService {

    public Car validateCar() {
        Car carValidate = new Car();

        Scanner scanner = new Scanner(System.in);
        CarValidator validator = new CarValidator();

        System.out.println("Provide manufacturer: ");
        carValidate.setManufacturer(validator.validateManufacturer());
        System.out.print("Provide model: ");
        carValidate.setModel(scanner.next());
        System.out.print("Provide engine: ");
        carValidate.setEngine(validator.validateEngine(carValidate.getEngine()));
        System.out.println("Provide type of fuel: ");
        carValidate.setFuel(validator.validateFuel(carValidate.getFuel()));
        System.out.print("Provide price: ");
        carValidate.setPrice(validator.validatePrice(carValidate.getPrice()));
        System.out.println("Provide currency: ");
        carValidate.setCurrency(validator.validateCurrency(carValidate.getCurrency()));
        return createCar(carValidate);
    }

    private Car createCar(Car car) {
        loadApplicationFile();
        Car newCar = Car.builder()
                .ID(loadLastId() + 1)
                .manufacturer(car.getManufacturer())
                .model(car.getModel())
                .engine(car.getEngine())
                .fuel(car.getFuel())
                .price(car.getPrice())
                .currency(car.getCurrency())
                .build();
        CarList.listForCarService.add(newCar);

        System.out.println("\nCar has been created with following details: " + "\nModel: " + car.getModel()
                + " " + "\nEngine: " + car.getEngine() + " " + car.getFuel() + " " + "\nPrice: " + car.getPrice() + " "
                + car.getCurrency());

        saveApplicationFile();
        saveLastId(newCar.getID());
        return newCar;
    }

    private static int loadLastId() {
        int lastId = 0;
        try {
            FileInputStream fileInputStream = new FileInputStream("src\\main\\resources\\lastId.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            lastId = (int) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error " + e.getMessage());
        }
        return lastId;
    }

    private static void saveLastId(int lastId) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("src\\main\\resources\\lastId.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(lastId);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    private static HashSet<Car> saveApplicationFile() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("src\\main\\resources\\carDealer.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            for (Car x : CarList.listForCarService) {
                objectOutputStream.writeObject(x);
            }
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException ioe) {
            System.err.println("Error saving to file");
        }
        return CarList.listForCarService;
    }

    public static HashSet<Car> loadApplicationFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream("src\\main\\resources\\carDealer.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            while (fileInputStream.available() > 0) {
                Car car = (Car) objectInputStream.readObject();
                CarList.listForCarService.add(car);
            }
            objectInputStream.close();
        } catch (Exception ex) {
            System.err.println("Error loading file " + ex.getMessage());
        }
        return CarList.listForCarService;
    }


    public void findByManufacturer() {

        System.out.println("Provide manufacturer name: ");
        CarValidator validator = new CarValidator();
        Manufacturer manufacturer = validator.validateManufacturer();

        for (Car selectedManufacturer : CarList.listForCarService) {
            if (selectedManufacturer.getManufacturer().equals(manufacturer)) {
                System.out.println("ID: " + selectedManufacturer.getID() + " " + "Model: " +
                        selectedManufacturer.getModel() + " " + "Engine: " + selectedManufacturer.getEngine() +
                        " " + selectedManufacturer.getFuel() + " " + "Price: " + selectedManufacturer.getPrice() +
                        " " + selectedManufacturer.getCurrency());
            }
        }

    }

    public void showAllCars() {
        List<Car> sortedListOfCars = new ArrayList<>(CarList.listForCarService);
        sortedListOfCars.sort(Comparator.comparingInt(Car::getID));

        sortedListOfCars.forEach(sortedCars -> System.out.println("ID: " + sortedCars.getID() + " | " + "Manufacturer: " +
                sortedCars.getManufacturer() + " | " + "Model: " + sortedCars.getModel() + " | " + "Engine: " + sortedCars.getEngine() +
                " " + sortedCars.getFuel() + " | " + "Price: " + sortedCars.getPrice() + " " + sortedCars.getCurrency()));
    }

    public Car selectCarToPrediction() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Which one you want to take on loan? Please provide ID of car: ");
        int selectedCar = scanner.nextInt();

        Car selectedCarObject = CarList.listForCarService.stream()
                .filter(x -> x.getID() == selectedCar)
                .findFirst()
                .orElse(null);

        if (selectedCarObject != null) {

            System.out.println("Selected car: " + "\nManufacturer: " + selectedCarObject.getManufacturer() +
                    " | " + "Model: " + selectedCarObject.getModel() + " | " + "Engine: " +
                    selectedCarObject.getEngine() + " " + selectedCarObject.getFuel() + " | " + "Price: " +
                    selectedCarObject.getPrice() + " " + selectedCarObject.getCurrency() + "\n");
            checkLoanPrice(selectedCarObject);

        } else {
            System.out.println("Can not find car");
        }

        return selectedCarObject;

    }

    private void checkLoanPrice(Car car){//tutaj moim zdaniem lepiej byloby miec oddzielny obiekt Loan, z tymi zmiennymi, niz wolac caly obiekt car
        //wrzuciłbym samo car.getPrice(),zamiast ladowac caly obiekt niepotrzebnie, optymalizacja kochanie i tu tez moze byc void, bo nie zwracasz modyfikowane
        // w zaden sposob obiektu car, wiec wrzucaj tylko potrzebne do metody rzeczy
        Loan loan = new Loan();
        BigDecimal yearCost = selectCarToPrediction().getPrice().divide(loan.getYear(), 2, BigDecimal.ROUND_UP);
        BigDecimal totalCost = yearCost.multiply(loan.getPercent());
        BigDecimal totalOneYear = yearCost.add(totalCost).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal totalTwoYeras = totalOneYear.divide(BigDecimal.valueOf(2), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal totalThreeYeras = totalOneYear.divide(BigDecimal.valueOf(3), 2, BigDecimal.ROUND_HALF_UP);

        System.out.println("One year loan cost per month: " + totalOneYear + " " + car.getCurrency() +
                "\nTwo years loan cost per month: " + totalTwoYeras + " " + car.getCurrency() +
                "\nThree years loan cost per month: " + totalThreeYeras + " " + car.getCurrency());

    }

    public HashSet<Car> editCar() {
        Scanner scanner = new Scanner(System.in);
        CarService availableCarsToEdit = new CarService();
        availableCarsToEdit.showAllCars();

        CarValidator validator = new CarValidator();

        int menu;

        System.out.println("Provide ID of car to edit");
        int selectedCar = scanner.nextInt();

        Car selectedCarObject = CarList.listForCarService.stream()
                .filter(x -> x.getID() == selectedCar)
                .findFirst()
                .orElse(null);

        if(selectedCarObject != null) {
            do {
                System.out.println("""
                        What you want to change?
                        1.Manufacuter
                        2.Model
                        3.Engine
                        4.Fuel
                        5.Price
                        6.Currency
                        0.Back to home page""");
                menu = scanner.nextInt();
                switch (menu) {
                    case 1 -> {
                        System.out.println("Provide new manufacutrer for selected car: ");
                        selectedCarObject.setManufacturer(validator.validateManufacturer());
                        System.out.println("Change has been saved");
                        System.out.println("\n");
                    }
                    case 2 -> {
                        System.out.println("Provide new model for selected car: ");
                        String editModel = scanner.next();
                        selectedCarObject.setModel(editModel);
                        System.out.println("Change has been saved");
                        System.out.println("\n");
                    }
                    case 3 -> {
                        System.out.println("Provide new engine for selected car: ");
                        selectedCarObject.setEngine(validator.validateEngine(selectedCarObject.getEngine()));
                        System.out.println("Change has been saved");
                        System.out.println("\n");
                    }
                    case 4 -> {
                        System.out.println("Provide new type of fuel for selected car: ");
                        selectedCarObject.setFuel(validator.validateFuel(selectedCarObject.getFuel()));
                        System.out.println("Change has been saved");
                        System.out.println("\n");
                    }
                    case 5 -> {
                        System.out.println("Provide new price for selected car: ");
                        selectedCarObject.setPrice(validator.validatePrice(selectedCarObject.getPrice()));
                        System.out.println("Change has been saved");
                        System.out.println("\n");
                    }
                    case 6 -> {
                        System.out.println("Provide new currency for selected car: ");
                        selectedCarObject.setCurrency(validator.validateCurrency(selectedCarObject.getCurrency()));
                        System.out.println("Change has been saved");
                        System.out.println("\n");
                    }
                    case 0 -> System.out.println("Back to home page");
                }
            } while (menu != 0);
        } else {
            System.out.println("Can not find car");
        }

        return saveApplicationFile();

    }

    public HashSet<Car> removeCar() {
        CarService availableCarsToRemove = new CarService();
        availableCarsToRemove.showAllCars();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Provide ID of car to remove");
        int selectedCar = scanner.nextInt();

        Iterator<Car> iterator = CarList.listForCarService.iterator();
        while (iterator.hasNext()) {

            Car carIterator = iterator.next();
            if (carIterator.getID().equals(selectedCar)) {
                iterator.remove();
                carIterator.setID(null);
                carIterator.setManufacturer(null);
                carIterator.setModel(null);
                carIterator.setEngine(null);
                carIterator.setPrice(null);
                carIterator.setCurrency(null);
            }
        }
        return saveApplicationFile();
    }
}

