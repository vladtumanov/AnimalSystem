package Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public abstract class FileRepository implements DataRepository<List<String>> {

    private final Path path;

    public FileRepository(String filePath) {
        this.path = Path.of(filePath);
    }

    @Override
    public final List<String> findAll() {
        try {
            return Files.lines(path, StandardCharsets.UTF_8)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            ex.printStackTrace();
            return List.of();
        }
    }
}
