package Repository;

import java.util.List;

public class AnimalRepository extends FileRepository {

    private static final String filePath = "d:/animals.csv";
    private static final DataRepository<List<String>> dataRepository = new AnimalRepository(filePath);

    private AnimalRepository(String filePath) {
        super(filePath);
    }

    public static List<String> getAnimals() {
        return dataRepository.findAll();
    }
}
