package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvVersionsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.VersionRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VersionService implements IPokemonDataLoader {

    private final CsvVersionsRepositoryImpl csvVersionsRepository;
    private final VersionRepository versionRepository;

    public VersionService(CsvVersionsRepositoryImpl csvVersionsRepository,
                           VersionRepository versionRepository) {
        this.csvVersionsRepository = csvVersionsRepository;
        this.versionRepository = versionRepository;
    }

    @Override
    public String getEntityName() { return "Version"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvVersionsRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    row.put("name", csv.getName());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            versionRepository.batchInsertVersions(rows);
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvVersionsRepository.getAll().forEach(csvVersions -> {
            if (csvVersions.getVersionGroupId() != null) {
                versionRepository.linkVersionToVersionGroup(csvVersions.getId(), csvVersions.getVersionGroupId());
            }
        });
    }
}
