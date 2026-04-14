package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.repository.csv.CsvMachineRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MachineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MachineService implements IPokemonDataLoader {

    private final CsvMachineRepositoryImpl csvMachineRepository;
    private final MachineRepository machineRepository;

    public MachineService(CsvMachineRepositoryImpl csvMachineRepository,
                           MachineRepository machineRepository) {
        this.csvMachineRepository = csvMachineRepository;
        this.machineRepository = machineRepository;
    }

    @Override
    public String getEntityName() { return "Machine"; }

    @Override
    @Transactional
    public void loadNodes() {
        csvMachineRepository.getAll().forEach(csv -> {
            machineRepository.insertMachine(csv.machineNumber());
        });
    }

    @Override
    @Transactional
    public void loadRelationships() {
        csvMachineRepository.getAll().forEach(csv -> {
            if (csv.moveId() != null) {
                machineRepository.findAll().stream()
                        .filter(machine -> csv.machineNumber() == machine.getMachineNumber())
                        .forEach(machine -> {
                            machineRepository.linkMachineToMove(machine.getId(), csv.moveId());
                            machineRepository.linkMachineToVersionGroup(machine.getId(), csv.versionGroupId());
                        });
            }
        });
    }
}
