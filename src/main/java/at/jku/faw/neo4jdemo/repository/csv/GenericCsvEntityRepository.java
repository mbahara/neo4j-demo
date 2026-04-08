package at.jku.faw.neo4jdemo.repository.csv;

import java.util.List;

public interface GenericCsvEntityRepository<T> {
	T getById(Long id);
	List<T> getAll();
}
