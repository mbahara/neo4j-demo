package at.jku.faw.neo4jdemo.service;

public interface IDataLoader {
	void loadNodes();
	void loadRelationships();
	String getEntityName();
}
