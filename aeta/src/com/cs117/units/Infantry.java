package com.cs117.units;

public class Infantry extends Unit {
	
	private static final int INFANTRY_DEFAULT_HP = 10;
	private static final int INFANTRY_ATK_STR = 4;
	
	public Infantry() {
		super(INFANTRY_DEFAULT_HP);
		this.name = "INF";
		this.attackStr = INFANTRY_ATK_STR;
	}
	
	public Infantry(int hp) {
		super(hp);
		this.name = "INF";
		this.attackStr = INFANTRY_ATK_STR;
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
			int damage = (int)(attackingStr * (attackingHP/10.0));
			if(damage < 1)
				this.setHp(attackedHP - 1);
			else
				this.setHp(attackedHP - damage);
		}
		else if(attackingUnit.getName() == "TANK")
		{
			int damage = (int)(attackingStr * (attackingHP/20.0) * 2.5);
			if(damage < 3)
				this.setHp(attackedHP - 3);
			else
				this.setHp(attackedHP - damage);
		}
		else
			System.out.println("Invalid Unit attacking");
	}

}
