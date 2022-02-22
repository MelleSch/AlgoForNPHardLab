package game.board.oop.entities;

import game.board.oop.EEntity;
import game.board.oop.Tile;

public class Entity implements Cloneable {

	protected EEntity type;
	protected Tile tile;
	
	public Entity(EEntity type, Tile tile) {
		this.type = type;
		this.tile = tile;
	}

	public EEntity getType() {
		return type;
	}

	public int getTileX() {
		return tile.tileX;
	}

	public int getTileY() {
		return tile.tileY;
	}

	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile) {
		this.tile = tile;		
	}
	
	@Override
	public Entity clone() {
		return new Entity(type, tile);
	}

}