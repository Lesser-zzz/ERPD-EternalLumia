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
		// 피격 시 분노 게이지 차오르는 원본 로직 무력화
	}

	public boolean berserking() {
		// 원본 광폭화 진입 무력화 (무조건 false)
		return false;
	}

	// ========================================================
	// [컴파일 에러 방어용 더미(Dummy) 메서드들]
	// 다른 클래스들이 광전사의 스탯 변화를 요구할 때, 아무 변화 없이 원본 값 그대로 돌려줍니다.
	// ========================================================

	// 1. Char.java 에서 찾는 데미지 증폭 메서드 (데미지 그대로 반환)
	public float damageFactor(float dmg) {
		return dmg;
	}

	// 2. Hero.java 에서 찾는 체력 회복 시 분노 관리 메서드 (아무 일도 안 함)
	public void recover(float percent) {
		// Do nothing
	}

	// 3. Weapon.java 에서 찾는 인챈트 확률 보정 메서드 (확률 그대로 반환)
	public float enchantFactor(float multi) {
		return multi;
	}

	@Override
	public int icon() {
		// UI에 아이콘이 아예 뜨지 않도록 숨김 처리
		return -1;
	}
}
