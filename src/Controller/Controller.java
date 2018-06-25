package Controller;

import Model.Map;
import View.GUI;

public class Controller {

	private SaveController saveController;
	private GUI gui;
	private Map map;
	
	public Controller() {
		this.map = new Map();
		this.gui = new GUI(this, this.map);
		this.saveController = new SaveController(this.gui, this.map);
	}
	
	public void saveMap() {
		this.saveController.saveMap();
	}
	
	public void setHeight(int height) {
		this.gui.updateTitle();
		this.map.setHeight(height);
	}
	
	public void setWidth(int width) {
		this.gui.updateTitle();
		this.map.setWidth(width);
	}

	public void generateMap() {
		this.map.generateMap();
	}

	public void setPermissions(boolean selected) {
		this.map.setPermissions(selected);
	}

	public void setSmoothness(int value) {
		this.map.setSmoothingSteps(value);
	}
	
	public void setRocks(int value) {
		this.map.setMaxRocks(value);
	}
}
