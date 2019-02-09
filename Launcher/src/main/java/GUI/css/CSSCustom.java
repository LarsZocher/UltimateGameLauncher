package GUI.css;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class CSSCustom {
	
	private ArrayList<Node> nodes = new ArrayList<>();
	private HashMap<Node, List<String>> ids = new HashMap<>();
	
	public void addNode(Node node, String name){
		nodes.add(node);
		addID(node, name);
		
		if(node instanceof Pane){
			Pane p = (Pane)node;
			for(Node nodes : p.getChildren()) {
				addNode(nodes, name);
			}
		}
	}
	public void addNode(Node node, String... names){
		nodes.add(node);
		for(String name : names) {
			addID(node, name);
		}
	}
	
	public void addID(Node node, String name){
		node.getStyleClass().add(name);
		List<String> names = new ArrayList<>();
		if(ids.containsKey(node)){
			names = ids.get(node);
		}
		names.add(name);
		ids.put(node, names);
	}
	
	private String getIDsAsString(Node node){
		if(ids.containsKey(node)){
			String id = "";
			for(String name : ids.get(node)) {
				id+=name+"-";
			}
			return id.substring(0, id.length()-1);
		}
		return "";
	}
	
	public void setID(Node node){
		if(ids.containsKey(node)){
			node.setId(getIDsAsString(node));
		}
	}
	
	public void setID(Node node, CSSState state){
		if(ids.containsKey(node)){
			node.setId(getIDsAsString(node)+"-"+state.name().toLowerCase());
		}
	}
	
	public void refreshAll(){
		for(Node node : nodes) {
			setID(node);
		}
	}
}
