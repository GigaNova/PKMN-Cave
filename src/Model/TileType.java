package Model;

public enum TileType {
	FLOOR(358, 511, "81", "32"),
	WALL(324, 511, "91", "06"),
	
	WALL_UL(324, 613, "88", "06"),
	WALL_UR(426, 613, "8A", "06"),
	WALL_LL(324, 664, "98", "06"),
	WALL_LR(426, 664, "9A", "06"),
	
	WALL_L(324, 630, "90", "06"),
	WALL_R(426, 630, "92", "06"),
	WALL_U(341, 528, "89", "06"),
	WALL_D(341, 664, "99", "06"),
	
	WALL_SPR(341, 562, "9B", "06"),
	WALL_SPL(358, 562, "9C", "06"),
	
	ROCK_1(341, 579, "82", "06"),
	ROCK_2(358, 579, "83", "06");
	
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
