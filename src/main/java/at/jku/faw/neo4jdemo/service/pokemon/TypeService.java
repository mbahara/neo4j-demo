package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvTypeEfficacy;
import at.jku.faw.neo4jdemo.model.csv.CsvTypeGameIndices;
import at.jku.faw.neo4jdemo.repository.csv.CsvTypeEfficacyRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvTypeGameIndicesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvTypesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.GameIndexRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.TypeEfficacyRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.TypeRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TypeService implements IPokemonDataLoader {

    private final CsvTypesRepositoryImpl csvTypesRepository;
    private final TypeRepository typeRepository;
    private final CsvTypeEfficacyRepositoryImpl csvTypeEfficacyRepositoryImpl;
    private final CsvTypeGameIndicesRepositoryImpl csvTypeGameIndicesRepositoryImpl;
    private final GameIndexRepository gameIndexRepository;
    private final TypeEfficacyRepository typeEfficacyRepository;

    public TypeService(CsvTypesRepositoryImpl csvTypesRepository,
                       TypeRepository typeRepository, CsvTypeEfficacyRepositoryImpl csvTypeEfficacyRepositoryImpl,
                       CsvTypeGameIndicesRepositoryImpl csvTypeGameIndicesRepositoryImpl,
                       GameIndexRepository gameIndexRepository, TypeEfficacyRepository typeEfficacyRepository) {
        this.csvTypesRepository = csvTypesRepository;
        this.typeRepository = typeRepository;
        this.csvTypeEfficacyRepositoryImpl = csvTypeEfficacyRepositoryImpl;
        this.csvTypeGameIndicesRepositoryImpl = csvTypeGameIndicesRepositoryImpl;
        this.gameIndexRepository = gameIndexRepository;
        this.typeEfficacyRepository = typeEfficacyRepository;
    }

    @Override
    public String getEntityName() { return "Type"; }

    @Override
    @Transactional
    public void loadNodes() {
        List<Map<String, Object>> rows = csvTypesRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", csv.getId());
                    row.put("identifier", csv.getIdentifier());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = typeRepository.batchInsertTypes(rows);
            System.out.println("Successfully loaded " + count + " Types nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        Map<Long, List<CsvTypeEfficacy>> efficacyMap = csvTypeEfficacyRepositoryImpl.getAll().stream()
                .collect(Collectors.groupingBy(CsvTypeEfficacy::getDamageTypeId));

        Map<Long, List<CsvTypeGameIndices>> gameIndexMap = csvTypeGameIndicesRepositoryImpl.getAll().stream()
                .collect(Collectors.groupingBy(CsvTypeGameIndices::getTypeId));

        csvTypesRepository.getAll().forEach(csv -> {
            Long typeId = csv.getId();

            if (csv.getGenerationId() != null) {
                typeRepository.linkTypeToGeneration(typeId, csv.getGenerationId());
            }

            if (csv.getDamageClassId() != null) {
                typeRepository.linkTypeToDamageClass(typeId, csv.getDamageClassId());
            }

            List<CsvTypeEfficacy> efficacyList = efficacyMap.get(typeId);
            if (efficacyList != null) {
                efficacyList.forEach(eff ->
                        typeEfficacyRepository.linkTypeToType(typeId, eff.getTargetTypeId(), eff.getDamageFactor()));
            }

            List<CsvTypeGameIndices> indices = gameIndexMap.get(typeId);
            if (indices != null) {
                indices.forEach(idx ->
                        gameIndexRepository.linkTypeHasIndex(typeId, idx.getGenerationId(), idx.getGameIndex()));
            }
        });
    }
}
