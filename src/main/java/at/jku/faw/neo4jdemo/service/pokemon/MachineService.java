package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.neo4j.Machine;
import at.jku.faw.neo4jdemo.repository.csv.CsvMachineRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MachineRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        List<Map<String, Object>> rows = csvMachineRepository.getAll().stream()
                .map(csv -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("machineNumber", csv.getMachineNumber());
                    return row;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            Integer count = machineRepository.batchInsertMachines(rows);
            //machineRepository.createMachineIndex();
            System.out.println("Successfully loaded " + count + " Machines nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        Map<Integer, List<Machine>> machineMap = machineRepository.findAll().stream()
                .collect(Collectors.groupingBy(Machine::getMachineNumber));

        csvMachineRepository.getAll().forEach(csv -> {
            if (csv.getMoveId() != null) {
                List<Machine> matchingMachines = machineMap.get(csv.getMachineNumber());

                if (matchingMachines != null) {
                    matchingMachines.forEach(machine -> {
                        Long internalId = machine.getId();
                        machineRepository.linkMachineToMove(internalId, csv.getMoveId());
                        machineRepository.linkMachineToVersionGroup(internalId, csv.getVersionGroupId());
                    });
                }
            }
        });
    }
}
