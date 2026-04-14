package at.jku.faw.neo4jdemo.service.pokemon;

public interface IPokemonDataLoader {
	void loadNodes();
	void loadRelationships();
	String getEntityName();
}
