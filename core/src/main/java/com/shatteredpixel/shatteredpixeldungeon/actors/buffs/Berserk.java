package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Berserk extends ShieldBuff implements ActionIndicator.Action {

	{
		type = buffType.POSITIVE;
		detachesAtZero = false;
		shieldUsePriority = -1;
	}

	@Override
	public boolean act() {
		// 원본 분노 감소/회복 로직 무력화
		spend(TICK);
		return true;
	}

	@Override
	public void damage(int damage) {
		// 피격 시 분노 게이지 차오르는 로직 무력화
	}

	public boolean berserking() {
		// 원본 광폭화 진입 완전 차단 (우리는 Fury.java와 Hero.isAlive()로 대체함)
		return false;
	}

	@Override
	public String actionName() {
		return "";
	}

	@Override
	public int actionIcon() {
		return 0;
	}

	@Override
	public int indicatorColor() {
		return 0;
	}

	@Override
	public void doAction() {
		// 하단 액티브 버튼 동작 무력화
	}

	@Override
	public int icon() {
		return BuffIndicator.BERSERK;
	}

	@Override
	public String name() {
		return "멧돼지 현우";
	}

	@Override
	public String desc() {
		return "실전 압축 근육 현우의 패시브 상태입니다.";
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
	}
}
