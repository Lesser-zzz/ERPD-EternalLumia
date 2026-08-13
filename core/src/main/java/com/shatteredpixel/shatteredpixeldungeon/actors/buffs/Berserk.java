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

	@Override
	public void damage(int damage) {
		// 피격 시 분노 게이지가 차오르는 원본 로직 원천 차단
	}

	public boolean berserking() {
		// 원본 광폭화 진입 판정 완전 차단 (현우는 Fury.java와 Hero.isAlive()로 대체됨)
		return false;
	}

	@Override
	public int icon() {
		// 버프 목록이나 UI에 아이콘이 아예 뜨지 않도록 -1 반환 (숨김 처리)
		return -1;
	}
}
