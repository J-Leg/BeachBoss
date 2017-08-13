import java.util.ArrayList;
import java.util.Random;

public class Level {
	private ObjectHandler handler;
	private Random r = new Random();
	private int limit = 3;
	private MODE m;
	private DIFFICULTY d;
	private HUD hud;
	private String modelPath;
	
	private ArrayList<Crate> crateList = new ArrayList<Crate>();
	private ArrayList<Goal> goalList = new ArrayList<Goal>();
	private ArrayList<Player> playerList = new ArrayList<Player>();
	private ArrayList<GameObject> objectList = new ArrayList<GameObject>();
	
	private int Dimension = Game.WIDTH/12;
	
	/**
	 * level initialises with a handler and a given difficulty,
	 * which changes how the level is built
	 * @param handler
	 * @param d
	 */
	public Level(ObjectHandler handler, DIFFICULTY d, String modelPath){
		this.handler = handler;
		this.d = d;
		this.modelPath = modelPath;
	}
	
	/**
	 * generates the level according to options chosen
	 * precondition: difficulty has been set
	 * postcondition: handler filled with objects representing level
	 */
	public void generate(){
		
		
		/*
		 * Add the boundary wall
		 */		
		for(int i = 0; i <= Game.WIDTH/90 - 1; i++){
						
			for(int j = 0; j <= Game.HEIGHT/90 - 1; j++){
								
				if(j == 0 || j == Game.HEIGHT/90 - 1){
					int x = i * Game.WIDTH/12;
					int y = j * Game.WIDTH/12;
					
					Wall w = new Wall(x, y, ID.Wall, handler);
					handler.addObject(w);
					objectList.add(w);
				}
				
				if(i == 0 || i == Game.WIDTH/90 - 1){
					int x = i * Game.WIDTH/12;
					int y = j * Game.WIDTH/12;
					
					Wall w = new Wall(x, y, ID.Wall, handler);
					handler.addObject(w);
					objectList.add(w);
				}
			}
		}
		

		if(d == DIFFICULTY.Hard){
			preGen();
			return;
		}
		
		
		int goalLimit = r.nextInt(limit) + 1;
		
		for(int i = 0; i < goalLimit; i++){
			int x = 0, y = 0;
			

			x = calcDiscretePos(r.nextInt(Game.WIDTH));
			y = calcDiscretePos(r.nextInt(Game.HEIGHT));
		
			
			
			while (!handler.checkSpaceFree(x, y)){	
				if(x == 90 || x == 900){
					y = calcDiscretePos(r.nextInt(Game.HEIGHT));
				}else if(y == 90 || y == 630){
					x = calcDiscretePos(r.nextInt(Game.WIDTH));
				}else{
					x = calcDiscretePos(r.nextInt(Game.WIDTH));
					y = calcDiscretePos(r.nextInt(Game.HEIGHT));
				}
			}

			Goal g = new Goal(x, y, ID.Goal, handler);
			handler.addObject(g);
			objectList.add(g);
			goalList.add(g);
		}
				
		
		/*
		 * If there is a crate along a wall, generate a goal along wall
		 * Refer to boundedCrateList
		 */
		//randomly place crates in level
		int boxLimit = goalLimit;
		for(int i = 0; i < boxLimit; i++){
			
			int x, y;
			
			
			x = calcDiscretePos(180 + r.nextInt(630));
			y = calcDiscretePos(180 + r.nextInt(450));
			
			while (!handler.checkSpaceFree(x, y)){
				x = calcDiscretePos(180 + r.nextInt(630));
				y = calcDiscretePos(180 + r.nextInt(450));
					
			}
					
			Crate w = new Crate(x, y, ID.Crate, handler);
								
			handler.addObject(w);
			objectList.add(w);
			crateList.add(w);
		}
	
		
		int xP = calcDiscretePos(r.nextInt(Game.WIDTH - 90));
		int yP = calcDiscretePos(r.nextInt(Game.HEIGHT - 90));
		
		while(!handler.checkSpaceFree(xP, yP)){
			xP = calcDiscretePos(r.nextInt(Game.WIDTH - 90));
			yP = calcDiscretePos(r.nextInt(Game.HEIGHT - 90));
		}
		
		if(m == MODE.SinglePlayer){
			spawnPlayer(1, xP, yP);
		}else if(m == MODE.MultiPlayer){
			spawnPlayer(1, xP, yP);
			spawnPlayer(2, xP, yP);
		}
		
		/*
		 * Generate Path
		 * v2
		 * 
		 * Will generate a Path from each crab to any goal
		 * Uses a new GameObject 'SearchStep' to hold position (prevents a wall to be placed ontop)
		 */
		generatePath();
		
		
		//randomly place extra walls in level
		int treeLimit = 25;

		for(int i = 0; i <= treeLimit; i++){
			
			int x = calcDiscretePos(r.nextInt(Game.WIDTH));
			int y = calcDiscretePos(r.nextInt(Game.HEIGHT));
			
			while (!handler.checkSpaceFree(x, y)){
				x = calcDiscretePos(r.nextInt(Game.WIDTH));
				y = calcDiscretePos(r.nextInt(Game.HEIGHT));
			}

			Wall w = new Wall(x, y, ID.Wall, handler);
			handler.addObject(w);
			objectList.add(w);
		}
	}
	
	/**
	 * Generates the path between the crates and goals, representing the path the player takes
	 * to complete the goal
	 */
	private void generatePath() {
		ArrayList<SearchStep> stepList = new ArrayList<SearchStep>();

		
		for(Player p : playerList){
			
			/*
			 * Initialise SearchStep
			 */
			 
			SearchStep s = new SearchStep(p.getX(), p.getY(), ID.Step);
			stepList.add(s);
			
			for(Crate c : crateList){	 
				SearchStep interS = new SearchStep(c.getX(), c.getY(), ID.Step);
				
				if(this.d == DIFFICULTY.Easy){
					bloom(interS);
				}
					
				/*
				 * Place a searchStep on coordinates of Crate
				 * add this searchStep to stepList
				 */
				stepList.add(interS);
			}
			
			for(Goal g : goalList){
				SearchStep interG = new SearchStep(g.getX(), g.getY(), ID.Step);
								
				/*
				 * Place a searchStep on coordinates of Goal
				 * add this searchStep to stepList
				 */
				stepList.add(interG);
			}
		}

		/*
		 * Now we connect all the SearchSteps in stepList (With more SearchStep Objects)
		 * This implies that the player can visit each crucial step (otherwise level is impossible)
		 */
		SearchStep prev = null;
		// int counter = 0;
		for(SearchStep s : stepList){
			
			//System.out.println(counter + " " + s);
			//counter++;
			
			handler.addObject(s);
			objectList.add(s);
			
			if(prev == null){
				prev = s;
			}
			
			/*
			 * Connect the steps by a path from prev to s
			 */
			SearchStep intermediate = prev;	

			while(intermediate.getX() != s.getX() || intermediate.getY() != s.getY()){
				//System.out.println("Intermediate : " + intermediate);

				int xDiff = intermediate.getX() - s.getX();
				int yDiff = intermediate.getY() - s.getY();
				
				//System.out.println("xDiff = " + xDiff + " yDiff = " + yDiff);
				
				boolean xMove = false;
				boolean yMove = false;
				
				int newX;
				int newY;
				
				if(xDiff > 0){
					newX = intermediate.getX() - 90;
					xMove = true;
				}else if(xDiff < 0){
					newX = intermediate.getX() + 90;
					xMove = true;
				}else{
					newX = intermediate.getX();
				}
				
				if(yDiff > 0){
					newY = intermediate.getY() - 90;
					yMove = true;
				}else if(yDiff < 0){
					newY = intermediate.getY() + 90;
					yMove = true;
				}else{
					newY = intermediate.getY();
				}
								
				if(xMove == true && yMove == true){
					
					int decider = r.nextInt(2);
					
					SearchStep interB = null;
					
					if(decider == 0){
						interB = new SearchStep(intermediate.getX(), newY, ID.Step);
					}else if(decider == 1){
						interB = new SearchStep(newX, intermediate.getY(), ID.Step);
					}
					handler.addObject(interB);
					objectList.add(interB);
				}
				
				intermediate = new SearchStep(newX, newY, ID.Step);
				handler.addObject(intermediate);
				objectList.add(intermediate);
			}
			//System.out.println("Found");
			prev = s;
		}
	}
	
	/**
	 * Expands the path so that the player is more able to manoeuvre crates
	 * @param s
	 */
	private void bloom(SearchStep s){
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				
				if(i == 1 && j == 1){
					continue;
				}
				
				if(!handler.checkSpaceFree(s.getX() + 90*(j - 1), s.getY() + 90*(i - 1)))
					continue;
				
				SearchStep bloomS = new SearchStep(s.getX() + 90*(j - 1), s.getY() + 90*(i - 1)
						, ID.Step);
				handler.addObject(bloomS);
				objectList.add(bloomS);
			}
		}
	}
	
	/**
	 * Returns the closest value to a multiple of the object size
	 * @return 
	 */
	private int calcDiscretePos(int x){
		int modVal = x % 90;
		int val;
		
		if(modVal < x/2){
			val = x - modVal;
		}else{
			val = x + (90 - modVal);
		}
		return val;
	}
	
	/**
	 * returns whether the location is a corner of the level
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isCorner(int x, int y){
		if((x == 90 && y == 90) || (x == 90 && y == 630)
				|| (x == 900 && y == 630) || (x == 900 && y == 90)){
			return true;
		}
		return false;
	}
	
	/**
	 * returns whether the goal has been completed by checking with each goal
	 * @return
	 */
	public boolean getGoalState(){
		if(goalList.isEmpty()){
			return false;
		}
		
		for(Goal g : goalList){
			if(g.getGoalState() == false){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * clears handler of all objects, removing the level
	 */
	public void clearLevel(){
		for(GameObject i : objectList){
			handler.removeObject(i);
		}
		objectList = null;
	}
	
	/**
	 * sets whether the game is multiplayer or not
	 * @param x
	 */
	public void setMode(MODE m){
		this.m = m;
	}
	
	private void preGen(){
		// taken from http://larc.unt.edu/ian/research/sokoban/strip.png
		int randomVal = r.nextInt(4);
		
		if(randomVal == 0){
			
			
			// vertical line with gap
	        for (int y = 90; y < 720; y+=90) {
	            if (y != 180) {
	                Wall w = new Wall(360, y, ID.Wall, handler);
	                handler.addObject(w);
	                objectList.add(w);
	            }
	        }
	        // horizontal cutoff
	        for (int x = 630; x < 990; x+=90) {
	            for (int y = 90; y < 720; y+=90) {
	                Wall w = new Wall(x, y, ID.Wall, handler);
	                handler.addObject(w);
	                objectList.add(w);
	            }
	        }
	        // 3x3 block
	        for (int y = 360; y < 720; y+=90) {
	            for (int x = 90; x < 360; x+=90) {
	                Wall w = new Wall(x, y, ID.Wall, handler);
	                handler.addObject(w);
	                objectList.add(w);
	            }
	        }
	        // 2x2 block
	        for (int x = 450; x < 630; x+=90) {
	            for (int y = 540; y < 720; y+=90) {
	                Wall w = new Wall(x, y, ID.Wall, handler);
	                handler.addObject(w);
	                objectList.add(w);
	            }
	        }
	        // goals
	        for (int y = 360; y < 540; y+=90) {
	            Goal g = new Goal(540, y, ID.Goal, handler);
	            handler.addObject(g);
	            objectList.add(g);
	            goalList.add(g);
	        }
	        // crates
	        Crate c1 = new Crate(450, 180, ID.Crate, handler);
	        handler.addObject(c1);
	        objectList.add(c1);
	        crateList.add(c1);
	        Crate c2 = new Crate(450, 360, ID.Crate, handler);
	        handler.addObject(c2);
	        objectList.add(c2);
	        crateList.add(c2);
	        
	        spawnPlayer(1, 450, 90);
		}else if(randomVal == 1){
			
			
			// vertical cutoff
	        for (int y = 360; y < 720; y+=90) {
	            for (int x = 90; x < 990; x+=90) {
	                Wall w = new Wall(x, y, ID.Wall, handler);
	                handler.addObject(w);
	                objectList.add(w);
	            }
	        }
	        // horizontal cutoff
	        for (int y = 90; y < 360; y+=90) {
	            for (int x = 630; x < 990; x+=90) {
	                Wall w = new Wall(x, y, ID.Wall, handler);
	                handler.addObject(w);
	                objectList.add(w);
	            }
	        }
	        // random jut
	        Wall w1 = new Wall(360, 90, ID.Wall, handler);
	        handler.addObject(w1);
	        objectList.add(w1);

	        // goals
	        for (int x = 90; x < 270; x+=90) {
	            Goal g = new Goal(x, 270, ID.Goal, handler);
	            handler.addObject(g);
	            objectList.add(g);
	            goalList.add(g);
	        }
	        
	        // crates
	        for (int x = 270; x < 540; x+=90) {
	            Crate c = new Crate(x, 180, ID.Crate, handler);
	            handler.addObject(c);
	            objectList.add(c);
	            crateList.add(c);
	        }
	        
	        spawnPlayer(1, 90, 90);
		}else if(randomVal == 2){
			
			
			// vertical cutoff
	        for (int x = 90; x < 990; x+=90) {
	            Wall w = new Wall(x, 630, ID.Wall, handler);
	            handler.addObject(w);
	            objectList.add(w);
	        }
	        // horizontal cutoff
	        for (int y = 90; y < 630; y+=90) {
	            Wall w = new Wall(900, y, ID.Wall, handler);
	            handler.addObject(w);
	            objectList.add(w);
	        }
	        // bottom jut
	        for (int x = 360; x < 630; x+=90) {
	            Wall w = new Wall(x, 540, ID.Wall, handler);
	            handler.addObject(w);
	            objectList.add(w);
	        }
	        // middle jut
	        for (int x = 360; x < 630; x+=90) {
	            Wall w = new Wall(x, 360, ID.Wall, handler);
	            handler.addObject(w);
	            objectList.add(w);
	        }
	        // top jut
	        for (int x = 540; x < 720; x+=90) {
	            for (int y = 90; y < 360; y+=90) {
	                Wall w = new Wall(x, y, ID.Wall, handler);
	                handler.addObject(w);
	                objectList.add(w);
	            }
	        }
	        // random walls
	        Wall w1 = new Wall(90, 270, ID.Wall, handler);
	        handler.addObject(w1);
	        objectList.add(w1);
	        Wall w2 = new Wall(270, 270, ID.Wall, handler);
	        handler.addObject(w2);
	        objectList.add(w2);
	        Wall w3 = new Wall(810, 360, ID.Wall, handler);
	        handler.addObject(w3);
	        objectList.add(w3);

	        // goals
	        Goal g1 = new Goal(180, 90, ID.Goal, handler);
	        handler.addObject(g1);
	        objectList.add(g1);
	        goalList.add(g1);
	        Goal g2 = new Goal(270, 90, ID.Goal, handler);
	        handler.addObject(g2);
	        objectList.add(g2);
	        goalList.add(g2);
	        Goal g3 = new Goal(450, 270, ID.Goal, handler);
	        handler.addObject(g3);
	        objectList.add(g3);
	        goalList.add(g3);
	        
	        // crates
	        Crate c1 = new Crate(180, 180, ID.Crate, handler);
	        handler.addObject(c1);
	        objectList.add(c1);
	        crateList.add(c1);
	        Crate c2 = new Crate(360, 180, ID.Crate, handler);
	        handler.addObject(c2);
	        objectList.add(c2);
	        crateList.add(c2);
	        Crate c3 = new Crate(180, 360, ID.Crate, handler);
	        handler.addObject(c3);
	        objectList.add(c3);
	        crateList.add(c3);
	        Crate c4 = new Crate(360, 450, ID.Crate, handler);
	        handler.addObject(c4);
	        objectList.add(c4);
	        crateList.add(c4);
	        
	        spawnPlayer(1, 90, 90);
		}else if(randomVal == 3){
			
			
			// vertical cutoff
	        for (int y = 540; y < 720; y+=90) {
	            for (int x = 90; x < 630; x+=90) {
	                Wall w = new Wall(x, y, ID.Wall, handler);
	                handler.addObject(w);
	                objectList.add(w);
	            }
	        }
	        // horizontal cutoff
	        for (int x = 630; x < 990; x+=90) {
	            for (int y = 90; y < 720; y+=90) {
	                Wall w = new Wall(x, y, ID.Wall, handler);
	                handler.addObject(w);
	                objectList.add(w);
	            }
	        }
	        // 4x2 block
	        for (int y = 360; y < 540; y+=90) {
	            for (int x = 270; x < 630; x+=90) {
	                Wall w = new Wall(x, y, ID.Wall, handler);
	                handler.addObject(w);
	                objectList.add(w);
	            }
	        }
	        // random walls
	        Wall w1 = new Wall(270, 90, ID.Wall, handler);
	        handler.addObject(w1);
	        objectList.add(w1);
	        Wall w2 = new Wall(540, 270, ID.Wall, handler);
	        handler.addObject(w2);
	        objectList.add(w2);
	        // goals
	        for (int x = 360; x < 630; x+=90) {
	            Goal g = new Goal(x, 90, ID.Goal, handler);
	            handler.addObject(g);
	            objectList.add(g);
	            goalList.add(g);
	        }
	        // crates
	        for (int x = 270; x < 450; x+=90) {
	            Crate c = new Crate(x, 180, ID.Crate, handler);
	            handler.addObject(c);
	            objectList.add(c);
	            crateList.add(c);
	        }
	        Crate c1 = new Crate(180, 270, ID.Crate, handler);
	        handler.addObject(c1);
	        objectList.add(c1);
	        crateList.add(c1);
	        
	        spawnPlayer(1, 90, 90);
		} 		
		// Change this yo spawnPlayer(1);
	}
	
	private void spawnPlayer(int i, int x, int y){
		
	
		Player p = new Player(x, y, ID.Player, handler, i);
	
		if(i == 1){
			if(modelPath != null){
				p.setImgPath(modelPath);
			}
		}

		handler.addObject(p);
		objectList.add(p);
		playerList.add(p);
	}
}
