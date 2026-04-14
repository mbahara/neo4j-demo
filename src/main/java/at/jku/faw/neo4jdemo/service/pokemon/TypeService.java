package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvTypeEfficacyRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvTypeGameIndicesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.csv.CsvTypesRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.GameIndexRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.TypeEfficacyRepository;
import at.jku.faw.neo4jdemo.repository.neo4j.TypeRepository;
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
        csvTypesRepository.getAll().forEach(csv -> {
            typeRepository.insertType(csv.getId(), csv.getIdentifier());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvTypesRepository.getAll().forEach(csv -> {
            if (csv.getGenerationId() != null) {
                typeRepository.linkTypeToGeneration(csv.getId(), csv.getGenerationId());
            }

            csvTypeEfficacyRepositoryImpl.getByDamageTypeId(csv.getId()).forEach(csvTypeEfficacy ->
                    typeEfficacyRepository.linkTypeToType(csv.getId(), csvTypeEfficacy.getTargetTypeId(), csvTypeEfficacy.getDamageFactor()));
            csvTypeGameIndicesRepositoryImpl.getByTypeId(csv.getId()).forEach(csvTypeGameIndex ->
                    gameIndexRepository.linkTypeHasIndex(csvTypeGameIndex.getTypeId(), csvTypeGameIndex.getGenerationId(), csvTypeGameIndex.getGameIndex()));

            if (csv.getDamageClassId() != null) {
                typeRepository.linkTypeToDamageClass(csv.getId(), csv.getDamageClassId());
            }
        });
    }
}
