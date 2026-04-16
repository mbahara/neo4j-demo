package at.jku.faw.neo4jdemo.service.pokemon;

import at.jku.faw.neo4jdemo.model.csv.CsvMachine;
import at.jku.faw.neo4jdemo.repository.csv.CsvMachineRepositoryImpl;
import at.jku.faw.neo4jdemo.repository.neo4j.MachineRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        List<CsvMachine> csvMachine = csvMachineRepository.getAll();
        long idCounter = 1;
        List<Map<String, Object>> rows = new ArrayList<>();

        for (CsvMachine csv : csvMachine) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", idCounter++);
            row.put("machineNumber", csv.getMachineNumber());
            rows.add(row);
        }

        if (!rows.isEmpty()) {
            Integer count = machineRepository.batchInsertMachines(rows);
            machineRepository.createMachineIndex();
            System.out.println("Successfully loaded " + count + " Machines nodes.");
        }
    }

    @Override
    @Transactional
    public void loadRelationships() {
        List<CsvMachine> allCsv = csvMachineRepository.getAll();

        long idCounter = 1;

        for (CsvMachine csv : allCsv) {
            long currentMachineId = idCounter++;
            if (csv.getMoveId() != null) {
                machineRepository.linkMachineToMove(
                        currentMachineId,
                        csv.getMoveId()
                );
            }
            if (csv.getVersionGroupId() != null) {
                machineRepository.linkMachineToVersionGroup(
                        currentMachineId,
                        csv.getVersionGroupId()
                );
            }
        }
    }
}
