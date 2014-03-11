package com.cs117.units;

public class Infantry extends Unit {
	
	private static final int INFANTRY_DEFAULT_HP = 10;
	private static final int INFANTRY_ATK_STR = 4;
	private static final int INFANTRY_ATK_RANGE = 3;
	private static final int INFANTRY_MOVE_RANGE = 3;
	
	public Infantry(int team, char orientation) {
		super(INFANTRY_DEFAULT_HP, team, orientation);
		this.name = "INF";
		this.attackStr = INFANTRY_ATK_STR;
		this.atkRange = INFANTRY_ATK_RANGE;
		this.moveRange = INFANTRY_MOVE_RANGE;
		this.maxHP = INFANTRY_DEFAULT_HP;
		this.mvCount = 1;
		this.atkCount = 1;
	}
	
	public Infantry(int hp, int team) {
		super(hp, team);
		this.name = "INF";
		this.attackStr = INFANTRY_ATK_STR;
		this.atkRange = INFANTRY_ATK_RANGE;
		this.moveRange = INFANTRY_MOVE_RANGE;
		this.maxHP = INFANTRY_DEFAULT_HP;
		this.mvCount = 1;
		this.atkCount = 1;
	}
	
	public void getAttacked(Unit attackingUnit) {
		int attackedHP = this.getHp();
		int attackingHP = attackingUnit.getHp();
		int attackingStr = attackingUnit.getAtkStr();
		double atkMaxHP = (double)attackingUnit.getMaxHP();
		
		if (attackedHP <= 0 || attackingHP <= 0)
			System.err.println("Invalid HP before attack");
		
		if (attackingUnit.getName().equals("INF")) {
			int damage = (int) (attackingStr * (attackingHP/atkMaxHP));
			if (damage < 1) 
				this.setHp(attackedHP - 1);
			else 
				this.setHp(attackedHP - damage);
		}
		else if (attackingUnit.getName().equals("TANK")) {
			int damage = (int) (attackingStr * (attackingHP/atkMaxHP) * 2.5);
			if (damage < 3)
				this.setHp(attackedHP - 3);
			else
				this.setHp(attackedHP - damage);
		}
		else if (attackingUnit.getName().equals("MECH")) 
		{
			int damage = (int) (attackingStr * (attackingHP/atkMaxHP));
			if(damage < 2)
				this.setHp(attackedHP - 2);
			else
				this.setHp(attackedHP - damage);
		}
		else {
			System.err.println("Invalid Unit attacking");
		}
	}
	
	public boolean isMntnClimber()
	{
		return true;
	}
	
}
