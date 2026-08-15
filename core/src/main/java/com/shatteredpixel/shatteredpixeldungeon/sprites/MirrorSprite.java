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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.PointF;

public class MirrorSprite extends MobSprite {
	
	private static final int FRAME_WIDTH	= 12;
	private static final int FRAME_HEIGHT	= 15;
	
	public MirrorSprite() {
		super();
		
		texture( Dungeon.hero != null ? Dungeon.hero.heroClass.spritesheet() : HeroClass.WARRIOR.spritesheet() );
		updateArmor( 0 );
		idle();
	}
	
	@Override
	public void link( Char ch ) {
		super.link( ch );
		updateArmor();
	}

	@Override
	public void bloodBurstA(PointF from, int damage) {
		//do nothing
	}

	public void updateArmor(){
		updateArmor( ((MirrorImage)ch).armTier );
	}
	
	public void updateArmor( int tier ) {
		boolean isWarrior = (Dungeon.hero != null && Dungeon.hero.heroClass == HeroClass.WARRIOR);

		int fw = isWarrior ? 16 : FRAME_WIDTH;
		int fh = isWarrior ? 16 : FRAME_HEIGHT;

		TextureFilm film;

		if (isWarrior) {
			// 🥊 [현우 전용 로직: HeroSprite와 완벽하게 동일한 절삭 및 애니메이션 적용]
			boolean isBerserker = (Dungeon.hero.subClass == HeroSubClass.BERSERKER);
			boolean isGladiator = (Dungeon.hero.subClass == HeroSubClass.GLADIATOR);

			int safeTier = 0; 
			if (isBerserker) safeTier = 0; 
			else if (isGladiator) safeTier = 0; 

			SmartTexture tex = TextureCache.get( texture );
			TextureFilm warriorTiers = new TextureFilm( tex, tex.width, fh );
			
			film = new TextureFilm( warriorTiers, safeTier, fw, fh );
			
			// 현우용 애니메이션 프레임 할당
			idle = new Animation( 2, true );
			idle.frames( film, 0 );
			
			run = new Animation( 10, true );
			run.frames( film, 0, 1 );
			
			die = new Animation( 2, false );
			die.frames( film, 3 ); 
			
			attack = new Animation( 15, false );
			attack.frames( film, 2, 0 );

		} else {
			// 🧙 [기존 영웅 로직: 현우가 아닌 다른 직업일 경우 원본 거울상 유지]
			film = new TextureFilm( HeroSprite.tiers(), tier, FRAME_WIDTH, FRAME_HEIGHT );
			
			idle = new Animation( 1, true );
			idle.frames( film, 0, 0, 0, 1, 0, 0, 1, 1 );
			
			run = new Animation( 20, true );
			run.frames( film, 2, 3, 4, 5, 6, 7 );
			
			die = new Animation( 20, false );
			die.frames( film, 0 );
			
			attack = new Animation( 15, false );
			attack.frames( film, 13, 14, 15, 0 );
		}
		
		idle();
	}
}
