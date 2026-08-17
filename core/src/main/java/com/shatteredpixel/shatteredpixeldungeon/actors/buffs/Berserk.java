/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/
 */
package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

public class Berserk extends ShieldBuff {

	{
		type = buffType.POSITIVE;
		detachesAtZero = false;
	}

	@Override
	public boolean act() {
		spend(TICK);
		return true;
	}

	public void damage(int damage) { }
	public boolean berserking() { return false; }
	public float damageFactor(float dmg) { return dmg; }
	public void recover(float percent) { }
	public float enchantFactor(float multi) { return multi; }

	// ========================================================
	// [필사즉생 UI 전용 모니터 로직]
	// ========================================================

	@Override
	public int icon() {
		Dogfight dogfight = target.buff(Dogfight.class);
		if (dogfight == null) return -1; // 도그파이트가 없으면 숨김

		// 개발자님 기획: 쿨다운일 때는 안 띄우고 싶다면 아래 주석을 해제하세요!
		// if (dogfight.cooldownTurns > 0) return -1; 
		
		return BuffIndicator.BERSERK; // 항상 띄울 경우 광전사 아이콘 유지
	}

	@Override
	public String name() {
		Dogfight dogfight = target.buff(Dogfight.class);
		// 쿨다운 중일 때
		if (dogfight != null && dogfight.cooldownTurns > 0) {
			return Messages.get(this, "recovering");
		}
		// 대기 중일 때
		return Messages.get(this, "angered");
	}

	@Override
	public String desc() {
		Dogfight dogfight = target.buff(Dogfight.class);
		// 쿨다운 중일 때
		if (dogfight != null && dogfight.cooldownTurns > 0) {
			return Messages.get(this, "recovering_desc") + "\n\n" + 
				   Messages.get(this, "recovering_desc_turns", dogfight.cooldownTurns);
		}
		// 대기 중일 때
		return Messages.get(this, "angered_desc");
	}
}
