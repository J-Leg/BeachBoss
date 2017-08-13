import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class ObjectHandler {
	private LinkedList<GameObject> object = new LinkedList<GameObject>();
	
	/**
	 * Draw all the objects controlled by this handler
	 * @param g The graphics object to draw the objects onto
	 */
	public void render(Graphics g){
				
		for(int i = 0; i <= object.size() - 1; i++){
			GameObject tempObject = object.get(i);
			tempObject.render(g);
		}
	}
	
	/**
	 * Add an object to this handler
	 * @param object The object to add to the handler
	 */
	public void addObject(GameObject object){
		this.object.add(object);
	}
	
	/**
	 * Remove an object from this handler
	 * @param object The object to remove
	 */
	public void removeObject(GameObject object){
		this.object.remove(object);
	}

	/**
	 * Get a list of the objects controlled by this handler
	 * @return LinkedList A list of the objects controlled by this handler
	 */
	public LinkedList<GameObject> getList(){
		return object;
	}
	
	
	/**
	 * Add a path to the handler
	 * @param path The path to add to the handler
	 */
	/*
	public void addPath(Path path){
		//method checks if path already exists to avoid duplicate adds
		//paths are generated before objects except for boundary, so no need to check for collision
		for (Path currPath:this.path){
			if (currPath.getX() == path.getY()){
				if (currPath.getX() == path.getY()){
					return;
				}
			}
		}
		this.path.add(0,path);
	}
	*/
	/**
	 * Add a goal path to the handler
	 * @param path The goal path to add to the handler
	 */
	/*
	public void addGoalPath(Path path){
		this.path.add(0,path);
	}
	*/

	/**
	 * Check if a grid location is free
	 * @param x The x grid location to check
	 * @param y The y grid location to check
	 * @return boolean Whether the grid location is free
	 */
	public boolean checkSpaceFree(int x, int y){
		for (GameObject currObject:object){
			
			if (currObject.getX() == x){
				if (currObject.getY() == y){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Get the object at a location
	 * @param x The x coordinate of the object
	 * @param y The y coordinate of the object
	 * @return GameObject The object at the location
	 */
	public GameObject getObject(int x , int y){
		for (GameObject currObject:object){
			if (currObject.getX() == x){
				if (currObject.getY() == y){
					return currObject;
				}
			}
		}
		return null;
	}

	/**
	 * Get a path off the list of paths
	 * @return Path The path popped from the list of paths
	 */
	/*
	public Path popPath(){
		return this.path.poll();
	}*/

	/**
	 * Check if a path exists at a location
	 * @param x The x coordinate of the location to check
	 * @param y The y coordinate of the location to check
	 * @return boolean Whether there is a path at the location
	 */
	/*
	public boolean checkPathExists(int x, int y){
		for (Path currPath:this.path){
			if (currPath.getX() == x && currPath.getY() == y){
				return true;
			}
		}
		return false;
	}*/
}
