public abstract class Player {
	protected int ID;
	protected int hp;

	public abstract int getHP();
	
	public abstract void setHP(int hh);

	public abstract void upgradHP();
	
	public abstract String getType();

	public int getID() {
		return this.ID;
	}
}

class mafiaPlayer extends Player {

	mafiaPlayer(int num) {
		this.hp = 2500;
		this.ID = num;
	}

	@Override
	public int getHP() {
		return this.hp;
	}

	@Override
	public void setHP(int hh) {
		this.hp=hh;
	}
	
	@Override
	public void upgradHP() {
		this.hp += 500;
	}
	
	public String getType() {
		return "mafiaPlayer";
	}
}

class Detective extends Player {

	Detective(int num) {
		this.hp = 800;
		this.ID = num;
	}

	@Override
	public int getHP() {
		return this.hp;
	}

	@Override
	public void setHP(int hh) {
		this.hp=hh;
	}
	
	@Override
	public void upgradHP() {
		this.hp += 500;
	}
	
	public String getType() {
		return "Detective";
	}
}

class Healer extends Player {

	Healer(int num) {
		this.hp = 800;
		this.ID = num;
	}

	@Override
	public int getHP() {
		return this.hp;
	}

	@Override
	public void setHP(int hh) {
		this.hp=hh;
	}
	
	@Override
	public void upgradHP() {
		this.hp += 500;
	}
	
	public String getType() {
		return "Healer";
	}
}

class Commoner extends Player {

	Commoner(int num) {
		this.hp = 1000;
		this.ID = num;
	}

	@Override
	public int getHP() {
		return this.hp;
	}

	@Override
	public void setHP(int hh) {
		this.hp=hh;
	}
	
	@Override
	public void upgradHP() {
		this.hp += 500;
	}
	
	public String getType() {
		return "Commoner";
	}
}
