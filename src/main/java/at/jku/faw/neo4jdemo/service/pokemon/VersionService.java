package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvVersionsRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.VersionRepository;
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
        csvVersionsRepository.getAll().forEach(csv -> {
            versionRepository.insertVersion(csv.getId(), csv.getIdentifier(), csv.getName());
        });
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
