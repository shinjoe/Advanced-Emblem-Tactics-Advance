package com.cs117.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.cs117.units.Unit;

public class SoundHandler {
	private Sound infantryAttack;
	private Sound mechAttack;
	private Sound tankAttack;
	
	public SoundHandler() {
		infantryAttack = Gdx.audio.newSound(Gdx.files.internal("sfx/infantry.mp3"));
		mechAttack = Gdx.audio.newSound(Gdx.files.internal("sfx/mech.mp3"));
		tankAttack = Gdx.audio.newSound(Gdx.files.internal("sfx/tank.mp3"));
	}
	
	public void dispose() {
		infantryAttack.dispose();
		mechAttack.dispose();
		tankAttack.dispose();
	}
	
	public void playAttackSound(Unit u) {
		String unitType = u.getName();
		if (unitType.equals("INF")) {
			infantryAttack.play();
		} else if (unitType.equals("MECH")) {
			mechAttack.play();
		} else if (unitType.equals("TANK")) {
			tankAttack.play();
		}
	}
}
