package Model;

public enum TileType {
	FLOOR(358, 511, "2b", "11"),
	WALL(324, 511, "2b", "11"),
	
	WALL_UL(324, 613, "2b", "11"),
	WALL_UR(426, 613, "2b", "11"),
	WALL_LL(324, 664, "2b", "11"),
	WALL_LR(426, 664, "2b", "11"),
	
	WALL_L(324, 630, "2b", "11"),
	WALL_R(426, 630, "2b", "11"),
	WALL_U(341, 528, "2b", "11"),
	WALL_D(341, 664, "2b", "11"),
	
	WALL_SPR(341, 562, "2b", "11"),
	WALL_SPL(358, 562, "2b", "11"),
	
	ROCK_1(341, 579, "2b", "11"),
	ROCK_2(358, 579, "2b", "11");
	
	private final int x;
	private final int y;
	private final String tileByte;
	private final String perByte;

	private TileType(int x, int y, String hexOne, String hexTwo) {
		this.x = x;
		this.y = y;
		this.tileByte = hexOne;
		this.perByte = hexTwo;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public String getTileByte() {
		return tileByte;
	}

	public String getPerByte() {
		return perByte;
	}
}
