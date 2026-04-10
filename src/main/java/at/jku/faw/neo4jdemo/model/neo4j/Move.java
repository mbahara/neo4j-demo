package at.jku.faw.neo4jdemo.model.neo4j;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@Node("Move")
public class Move {
	@Id
	private Long id;

	private String name;
	private int power;
	private int pp;
	private int accuracy;
	private int priority;
	private int effectChance;

	@Relationship(type = "HAS_TYPE")
	private Type type;

	@Relationship(type = "INTRODUCED_IN")
	private Generation generation;

	@Relationship(type = "DAMAGE_CATEGORY")
	private DamageClass damageClass;

	@Relationship(type = "HAS_EFFECT")
	private MoveEffect effect;

	@Relationship(type = "TARGETS")
	private MoveTarget target;

	@Relationship(type = "HAS_FLAG")
	private List<MoveFlag> flags;

	@Relationship(type = "HAS_META")
	private MoveMeta meta;

	@Relationship(type = "HAS_HISTORY")
	private List<MoveChange> history;

	@Relationship(type = "CONTEST_STYLE")
	private ContestType contestType;

	@Relationship(type = "HAS_CONTEST_EFFECT")
	private ContestEffect contestEffect;

	@Relationship(type = "COMBOS_WITH")
	private List<Move> combos;

	// (:Move)-[:SUPER_CONTEST_COMBO_NEXT]->(:Move)
	// The first move in a combo points to the second
	@Relationship(type = "COMBOS_IN_SUPER_CONTEST")
	private List<Move> comboFollowUps;
}