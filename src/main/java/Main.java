import Service.AnimalService;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        AtomicInteger number = new AtomicInteger();
        AnimalService animalService = new AnimalService(resultCount ->
                System.out.printf("Result rules #%d: %d%n", number.incrementAndGet(), resultCount));
        animalService.run();
    }
}
