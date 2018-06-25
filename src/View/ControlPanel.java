package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import Controller.Controller;
import Model.Map;

public class ControlPanel extends JPanel {
	private Controller controller;
	
	public ControlPanel(Controller controller, Map map) {
		GridBagConstraints gbc = new GridBagConstraints();
		
		this.controller = controller;
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder("MainSettings"));
		
		gbc.gridy = 0;
		this.add(new JLabel("Width"), gbc);
		JSlider mapWidthSlider = new JSlider(JSlider.HORIZONTAL, Map.MIN_MAP_SIZE, Map.MAX_MAP_SIZE, map.getWidth());
		mapWidthSlider.addChangeListener((e) ->{
			controller.setWidth(mapWidthSlider.getValue());
		});
		mapWidthSlider.setMajorTickSpacing(10);
		mapWidthSlider.setMinorTickSpacing(1);
		mapWidthSlider.setPaintTicks(true);
		mapWidthSlider.setPaintLabels(true);
		
		gbc.gridy = 1;
		this.add(mapWidthSlider, gbc);
		
		gbc.gridy = 2;
		this.add(new JLabel("Height"), gbc);
		JSlider mapHeightSlider = new JSlider(JSlider.HORIZONTAL, Map.MIN_MAP_SIZE, Map.MAX_MAP_SIZE, map.getHeight());
		mapHeightSlider.addChangeListener((e) ->{
			controller.setHeight(mapHeightSlider.getValue());
		});
		mapHeightSlider.setMajorTickSpacing(10);
		mapHeightSlider.setMinorTickSpacing(1);
		mapHeightSlider.setPaintTicks(true);
		mapHeightSlider.setPaintLabels(true);
	
		gbc.gridy = 3;
		this.add(mapHeightSlider, gbc);
		
		gbc.gridy = 4;
		this.add(new JLabel("Smoothing"), gbc);
		JSlider smoothingSlider = new JSlider(JSlider.HORIZONTAL, Map.MIN_SMOOTHING, Map.MAX_SMOOTHING, map.getSmoothingSteps());
		smoothingSlider.addChangeListener((e) ->{
			controller.setSmoothness(smoothingSlider.getValue());
		});
		smoothingSlider.setMajorTickSpacing(2);
		smoothingSlider.setMinorTickSpacing(1);
		smoothingSlider.setPaintTicks(true);
		smoothingSlider.setPaintLabels(true);
	
		gbc.gridy = 5;
		this.add(smoothingSlider, gbc);
		
		gbc.gridy = 6;
		this.add(new JLabel("Max Rocks"), gbc);
		JSlider rocksSlider = new JSlider(JSlider.HORIZONTAL, Map.MIN_ROCKS, Map.MAX_ROCKS, map.getSmoothingSteps());
		rocksSlider.addChangeListener((e) ->{
			controller.setRocks(rocksSlider.getValue());
		});
		rocksSlider.setMajorTickSpacing(10);
		rocksSlider.setMinorTickSpacing(1);
		rocksSlider.setPaintTicks(true);
		rocksSlider.setPaintLabels(true);
	
		gbc.gridy = 7;
		this.add(rocksSlider, gbc);
		
		JCheckBox PCheckbox = new JCheckBox("Show Permissions");
		PCheckbox.setSelected(false);
		PCheckbox.addActionListener((e)->{
			controller.setPermissions(PCheckbox.isSelected());
		});
		gbc.gridy = 8;
		this.add(PCheckbox, gbc);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		
		JButton generateButton = new JButton("Generate");
		generateButton.addActionListener((e) ->{
			controller.generateMap();
		});
		gbc.gridy = 11;
		buttonPanel.add(generateButton, gbc);
		
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener((e) ->{
			controller.saveMap();
		});
		gbc.gridy = 12;
		buttonPanel.add(saveButton, gbc);
		
		gbc.gridy = 13;
		this.add(buttonPanel, gbc);
	}
	
}
