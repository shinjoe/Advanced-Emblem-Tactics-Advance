package com.cs117.units;

public class Tank extends Unit {
	
	private static final int TANK_DEFAULT_HP = 15;
	private static final int TANK_ATK_STR = 6;
	private static final int TANK_ATK_RANGE = 2;
	private static final int TANK_MOVE_RANGE = 2;
	
	public Tank(int team) {
		super(TANK_DEFAULT_HP, team);
		this.name = "TANK";
		this.attackStr = TANK_ATK_STR;
		this.atkRange = TANK_ATK_RANGE;
		this.moveRange = TANK_MOVE_RANGE;
		this.maxHP = TANK_DEFAULT_HP;
	}
	
	public Tank(int hp, int team) {
		super(hp, team);
		this.name = "TANK";
		this.attackStr = TANK_ATK_STR;
		this.atkRange = TANK_ATK_RANGE;
		this.moveRange = TANK_MOVE_RANGE;
		this.maxHP = TANK_DEFAULT_HP;
	}
	
	public void getAttacked(Unit attackingUnit) {
		int attackedHP = this.getHp();
		int attackingHP = attackingUnit.getHp();
		int attackingStr = attackingUnit.getAtkStr();
		double atkMaxHP = (double) attackingUnit.getMaxHP();
		
		if (attackedHP <= 0 || attackingHP <= 0) {
			System.out.println("Invalid HP before attack");
		}
		
		if (attackingUnit.getName().equals("INF")) {
			int damage = (int) (attackingStr * (attackingHP/atkMaxHP) * .6);
			if(damage < 1)
				this.setHp(attackedHP - 1);
			else
				this.setHp(attackedHP - damage);
			
		}
		else if (attackingUnit.getName().equals("TANK")) {
			int damage = (int) (attackingStr * (attackingHP/atkMaxHP));
			System.out.println(damage);
			if (damage < 2)
				this.setHp(attackedHP - 2);
			else
				this.setHp(attackedHP - damage);
		}
		else if (attackingUnit.getName().equals("MECH")) 
		{
			int damage = (int) (attackingStr * (attackingHP/atkMaxHP)*2.5);
			if(damage < 3)
				this.setHp(attackedHP - 3);
			else
				this.setHp(attackedHP - damage);
		}
		else
			System.out.println("Invalid Unit attacking");
	}
	
	public boolean isMntnClimber()
	{
		return false;
	}

}
