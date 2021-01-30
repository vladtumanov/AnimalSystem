package Repository;

import java.util.List;

public class RulesRepository extends FileRepository {

    private static final String filePath = "d:/rules.txt";
    private static final DataRepository<List<String>> dataRepository = new RulesRepository(filePath);

    private RulesRepository(String filePath) {
        super(filePath);
    }

    public static List<String> getRules() {
        return dataRepository.findAll();
    }
}
