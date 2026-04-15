package at.jku.faw.neo4jdemo.repository.neo4j;

import at.jku.faw.neo4jdemo.model.neo4j.Machine;
import java.util.List;
import java.util.Map;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends Neo4jRepository<Machine, Long> {
    @Query("""
    UNWIND $rows AS row
    MERGE (n:Machine)
    SET n.machineNumber = row.machineNumber
    RETURN count(n)
    """)
    Integer batchInsertMachines(@Param("rows") List<Map<String, Object>> rows);

    @Query("""
        MERGE (n:Machine)
        ON CREATE SET n.machineNumber = $machineNumber
        ON MATCH  SET n.machineNumber = $machineNumber
        RETURN n
        """)
    Machine insertMachine(@Param("machineNumber") int machineNumber);

    @Query("""
        MATCH (s:Machine {id: $machineId})
        MATCH (t:Move {id: $moveId})
        MERGE (s)-[:TEACHES_MOVE]->(t)
        """)
    void linkMachineToMove(@Param("machineId") Long machineId,
                        @Param("moveId") Long moveId);

    @Query("""
        MATCH (s:Machine {id: $machineId})
        MATCH (t:VersionGroup {id: $versionGroupId})
        MERGE (s)-[:IN_VERSION_GROUP]->(t)
        """)
    void linkMachineToVersionGroup(@Param("machineId") Long machineId,
                        @Param("versionGroupId") Long versionGroupId);


    @Query("CREATE INDEX machine_id_idx IF NOT EXISTS FOR (n:Machine) ON (n.id)")
    void createIdIndex();
}
