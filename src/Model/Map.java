package Model;

import java.util.Observable;
import java.util.Random;

public class Map extends Observable {
	public final static int MAX_MAP_SIZE = 64;
	public final static int MIN_MAP_SIZE = 16;
	public final static int TILE_SIZE = 16;

	public static final int MIN_SMOOTHING = 2;
	public static final int MAX_SMOOTHING = 12;
	
	public static final int MIN_ROCKS = 0;
	public static final int MAX_ROCKS = 60;
	
	private final static double ALIVE_CHANCE = 0.5;
	private final static int DEATH_LIMIT = 3;
	private final static int BIRTH_LIMIT = 5;

	private int height;
	private int width;
	private int smoothingSteps;
	private int maxRocks;
	private boolean showPerm;
	
	private TileType[][] tiles;
	private Permission[][] permissions;

	public Map() {
		this.height = (MAX_MAP_SIZE / 2) + (MIN_MAP_SIZE / 2);
		this.width = (MAX_MAP_SIZE / 2) + (MIN_MAP_SIZE / 2);
		this.smoothingSteps = 6;
		this.maxRocks = 10;
		this.showPerm = false;
		this.resetTiles();
	}

	private void resetTiles() {
		this.tiles = new TileType[this.width][this.height];
		this.permissions = new Permission[this.width][this.height];

		for (int i = 0; i < this.width; ++i) {
			for (int j = 0; j < this.height; ++j) {
				this.tiles[i][j] = TileType.FLOOR;
				this.permissions[i][j] = Permission.FOUR;
			}
		}
	}

	public void generateMap() {
		Random random = new Random(System.nanoTime());

		this.resetTiles();
		boolean[][] cellmap = new boolean[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (random.nextDouble() < ALIVE_CHANCE) {
					cellmap[x][y] = true;
				}
			}
		}

		for (int i = 0; i < smoothingSteps; i++) {
			cellmap = doSimulationStep(cellmap);
		}

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if(!cellmap[x][y]) {
					this.tiles[x][y] = TileType.FLOOR;
				}
				else {
					this.tiles[x][y] = TileType.WALL;
				}
			}
		}
		
		for(int i = 0; i < smoothingSteps; ++i) {
			this.cleanUp();
		}
		
		this.addWall();
		this.setWalls();
		this.setSpecialWalls();
		this.placeRocks();
		
		this.buildPermissionLayer();
		this.setChanged();
		this.notifyObservers();
	}

	private void placeRocks() {
		Random random = new Random();
		for(int i = 0; i < this.maxRocks; ++i) {
			int randX = random.nextInt(this.width - 2);
			int randY = random.nextInt(this.height - 2);
			int rock_version = (Math.random() <= 0.5) ? 1 : 2;
			
			if(tiles[randX][randY] == TileType.FLOOR) {
				if(rock_version == 1) {
					tiles[randX][randY] = TileType.ROCK_1;
				}
				else {
					tiles[randX][randY] = TileType.ROCK_2;
				}	
			}
		}
	}

	private void setSpecialWalls() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if(tiles[x][y] == TileType.FLOOR || tiles[x][y] != TileType.WALL) continue;
				
				TileType tileU = null;
				TileType tileL = null;
				TileType tileD = null;
				TileType tileR = null;

				if (x - 1 >= 0) {
					tileL = tiles[x - 1][y];
				}
				if (y - 1 >= 0) {
					tileU = tiles[x][y - 1];
				}
				if (x + 1 < this.width) {
					tileR = tiles[x + 1][y];
				}
				if (y + 1 < this.height) {
					tileD = tiles[x][y + 1];
				}
				
				if(tileR == TileType.WALL_D && tileD == TileType.WALL_LR
				|| tileR == TileType.WALL_LR && tileD == TileType.WALL_R
				|| tileR == TileType.WALL_LR && tileD == TileType.WALL_LR
				|| tileR == TileType.WALL_D && tileD == TileType.WALL_R) {
					tiles[x][y] = TileType.WALL_SPR;
				}
				else if(tileL == TileType.WALL_D && tileD == TileType.WALL_LL
				|| tileL == TileType.WALL_LL && tileD == TileType.WALL_L
				|| tileL == TileType.WALL_LL && tileD == TileType.WALL_LL
				|| tileL == TileType.WALL_D && tileD == TileType.WALL_L) {
					tiles[x][y] = TileType.WALL_SPL;
				}
				else if(tileU != TileType.FLOOR && tileU != TileType.WALL && tileU != TileType.WALL_U && tileU != null) {
					tiles[x][y] = TileType.WALL_U;
				}
			}
		}
	}

	private void setWalls() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if(tiles[x][y] == TileType.FLOOR) continue;
				
				TileType tileU = null;
				TileType tileL = null;
				TileType tileD = null;
				TileType tileR = null;

				if (x - 1 >= 0) {
					tileL = tiles[x - 1][y];
				}
				if (y - 1 >= 0) {
					tileU = tiles[x][y - 1];
				}
				if (x + 1 < this.width) {
					tileR = tiles[x + 1][y];
				}
				if (y + 1 < this.height) {
					tileD = tiles[x][y + 1];
				}
				
				if(tileU == TileType.FLOOR && tileR == TileType.FLOOR) {
					tiles[x][y] = TileType.WALL_UR;
				}
				else if(tileU == TileType.FLOOR && tileL == TileType.FLOOR) {
					tiles[x][y] = TileType.WALL_UL;
				}
				else if(tileD == TileType.FLOOR && tileR == TileType.FLOOR) {
					tiles[x][y] = TileType.WALL_LR;
				}
				else if(tileD == TileType.FLOOR && tileL == TileType.FLOOR) {
					tiles[x][y] = TileType.WALL_LL;
				}
				else if(tileR == TileType.FLOOR) {
					tiles[x][y] = TileType.WALL_R;
				}
				else if(tileL == TileType.FLOOR) {
					tiles[x][y] = TileType.WALL_L;
				}
				else if(tileU == TileType.FLOOR) {
					tiles[x][y] = TileType.WALL_U;
				}
				else if(tileD == TileType.FLOOR) {
					tiles[x][y] = TileType.WALL_D;
				}
			}
		}
	}

	private void cleanUp() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				TileType tileU = null;
				TileType tileL = null;
				TileType tileD = null;
				TileType tileR = null;

				int s = 0;
				int t = 0;
				if (x - 1 >= 0) {
					if(tiles[x - 1][y] == TileType.FLOOR) {
						++s;
					}
				}
				if (y - 1 >= 0) {
					if(tiles[x][y - 1] == TileType.FLOOR) {
						++t;
					}
				}
				if (x + 1 < this.width) {
					if(tiles[x + 1][y] == TileType.FLOOR) {
						++s;
					}
				}
				if (y + 1 < this.height) {
					if(tiles[x][y + 1] == TileType.FLOOR) {
						++t;
					}
				}
			
				if(s == 2 || t == 2) {
					tiles[x][y] = TileType.FLOOR;
				}
			}
		}
	}

	private void addWall() {
		for (int x = 0; x < width; x++) {
			this.tiles[x][0] = TileType.WALL;
		}
		for (int y = 0; y < height; y++) {
			this.tiles[0][y] = TileType.WALL;
		}	
		for (int x = 0; x < width; x++) {
			this.tiles[x][height - 1] = TileType.WALL;
		}
		for (int y = 0; y < height; y++) {
			this.tiles[width - 1][y] = TileType.WALL;
		}	
	}

	public boolean[][] doSimulationStep(boolean[][] oldMap) {
		boolean[][] newMap = new boolean[width][height];
		for (int x = 0; x < oldMap.length; x++) {
			for (int y = 0; y < oldMap[0].length; y++) {
				int nbs = countAliveNeighbours(oldMap, x, y);
				if (oldMap[x][y]) {
					if (nbs < DEATH_LIMIT) {
						newMap[x][y] = false;
					} else {
						newMap[x][y] = true;
					}
				} else {
					if (nbs > BIRTH_LIMIT) {
						newMap[x][y] = true;
					} else {
						newMap[x][y] = false;
					}
				}
			}
		}
		return newMap;
	}

	public int countAliveNeighbours(boolean[][] map, int x, int y) {
		int count = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				int neighbour_x = x + i;
				int neighbour_y = y + j;

				if (i == 0 && j == 0) {
					continue;
				} else if (neighbour_x < 0 || neighbour_y < 0 || neighbour_x >= map.length
						|| neighbour_y >= map[0].length) {
					count = count + 1;
				} else if (map[neighbour_x][neighbour_y]) {
					count = count + 1;
				}
			}
		}
		return count;
	}

	private void buildPermissionLayer() {
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				if(tiles[x][y] == TileType.FLOOR) {
					this.permissions[x][y] = Permission.C;
				}
				else {
					this.permissions[x][y] = Permission.ONE;
				}
			}
		}
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		this.resetTiles();
		this.setChanged();
		this.notifyObservers();
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		this.resetTiles();
		this.setChanged();
		this.notifyObservers();
	}

	public TileType[][] getTiles() {
		return this.tiles;
	}

	public Permission[][] getPermissions() {
		return this.permissions;
	}

	public int getSmoothingSteps() {
		return smoothingSteps;
	}

	public void setSmoothingSteps(int smoothingSteps) {
		this.smoothingSteps = smoothingSteps;
	}

	public boolean showPermissions() {
		return this.showPerm;
	}

	public void setPermissions(boolean selected) {
		this.showPerm = selected;
	}

	public int getMaxRocks() {
		return maxRocks;
	}

	public void setMaxRocks(int maxRocks) {
		this.maxRocks = maxRocks;
	}
}

