package com.cs117.units;

public class Mech extends Unit{

	private static final int MECH_DEFAULT_HP = 10;
	private static final int MECH_ATK_STR = 6;
	private static final int MECH_ATK_RANGE = 2;
	private static final int MECH_MOVE_RANGE = 2;
	
	public Mech(int team, char orientation) {
		super(MECH_DEFAULT_HP, team, orientation);
		this.name = "MECH";
		this.attackStr = MECH_ATK_STR;
		this.atkRange = MECH_ATK_RANGE;
		this.moveRange = MECH_MOVE_RANGE;
		this.maxHP = MECH_DEFAULT_HP;
		this.mvCount = 1;
		this.atkCount = 1;
	}
	
	public Mech(int hp, int team) {
		super(hp, team);
		this.name = "MECH";
		this.attackStr = MECH_ATK_STR;
		this.atkRange = MECH_ATK_RANGE;
		this.moveRange = MECH_MOVE_RANGE;
		this.maxHP = MECH_DEFAULT_HP;
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
			int damage = (int) (attackingStr * (attackingHP/atkMaxHP)*1.5);
			if (damage < 1) 
				this.setHp(attackedHP - 1);
			else 
				this.setHp(attackedHP - damage);
		}
		else if (attackingUnit.getName().equals("TANK")) {
			int damage = (int) (attackingStr * (attackingHP/atkMaxHP));
			if (damage < 2)
				this.setHp(attackedHP - 2);
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
