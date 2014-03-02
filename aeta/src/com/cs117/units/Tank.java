package com.cs117.units;

public class Tank extends Unit {
	
	private static final int TANK_DEFAULT_HP = 20;
	private static final int TANK_ATK_STR = 6;
	
	public Tank() {
		super(TANK_DEFAULT_HP);
		this.name = "TANK";
		this.attackStr = TANK_ATK_STR;
	}
	
	public Tank(int hp) {
		super(hp);
		this.name = "TANK";
		this.attackStr = TANK_ATK_STR;
	}
	
	public void getAttacked(Unit attackingUnit)
	{
		int attackedHP = this.getHp();
		int attackingHP = attackingUnit.getHp();
		int attackingStr = attackingUnit.getAtkStr();
		
		if(attackedHP <= 0 || attackingHP <= 0)
			System.out.println("Invalid HP before attack");
		
		if(attackingUnit.getName() == "INF")
		{
			int damage = (int)(attackingStr * (attackingHP/10.0) * .6);
			if(damage < 1)
				this.setHp(attackedHP - 1);
			else
				this.setHp(attackedHP - damage);
			
		}
		else if(attackingUnit.getName() == "TANK")
		{
			int damage = (int)(attackingStr * (attackingHP/20.0));
			System.out.println(damage);
			if(damage < 2)
				this.setHp(attackedHP - 2);
			else
				this.setHp(attackedHP - damage);
		}
		else
			System.out.println("Invalid Unit attacking");
	}

}
