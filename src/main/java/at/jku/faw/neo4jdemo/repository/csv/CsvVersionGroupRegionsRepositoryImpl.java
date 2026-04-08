package at.jku.faw.neo4jdemo.repository.csv;

import at.jku.faw.neo4jdemo.model.csv.CsvVersionGroupRegions;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class CsvVersionGroupRegionsRepositoryImpl extends GenericCsvRepositoryImpl implements GenericCsvEntityRepository<CsvVersionGroupRegions> {

    @Value("classpath:data/version_group_regions.csv")
    private Resource csvFile;
    
    private final List<CsvVersionGroupRegions> csvVersionGroupRegions = new ArrayList<>();

    @Override
    public CsvVersionGroupRegions getById(Long id) {
        throw new UnsupportedOperationException("csvversiongroupregions.csv has no single ID.");
    }

    @Override
    public List<CsvVersionGroupRegions> getAll() {
        if (csvVersionGroupRegions.isEmpty()) {
            try {
                csvVersionGroupRegions.addAll(getCsvEntities(csvFile, CsvVersionGroupRegions.class));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read CSV: data/version_group_regions.csv", e);
            }
        }
        return csvVersionGroupRegions;
    }

    public List<CsvVersionGroupRegions> getByVersionGroupId(Long versionGroupId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.versionGroupId(), versionGroupId))
                .toList();
    }

    public List<CsvVersionGroupRegions> getByRegionId(Long regionId) {
        return getAll().stream()
                .filter(e -> Objects.equals(e.regionId(), regionId))
                .toList();
    }
}
