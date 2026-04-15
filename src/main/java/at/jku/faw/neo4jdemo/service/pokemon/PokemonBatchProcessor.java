package at.jku.faw.neo4jdemo.service.pokemon;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PokemonBatchProcessor {

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void processNodeChunk(List<Map<String, Object>> rows, Consumer<List<Map<String, Object>>> batchMethod) {
		batchMethod.accept(rows);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void processRelationshipChunk(Runnable linkLogic) {
		linkLogic.run();
	}
}
