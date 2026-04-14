package at.jku.faw.neo4jdemo.model.neo4j;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Node("HeldItem")
public class HeldItem {
	@Id
	@GeneratedValue
	private Long id;

	@Property("rarity")
	private int rarity;

	@Relationship(type = "ITEM_HELD")
	private Item item;

	@Relationship(type = "IN_VERSION")
	private Version version;
}
