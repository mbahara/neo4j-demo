package at.jku.faw.neo4jdemo.repository;

public interface GenericNeo4jRepository<T> {
	T save(T entity);
}
