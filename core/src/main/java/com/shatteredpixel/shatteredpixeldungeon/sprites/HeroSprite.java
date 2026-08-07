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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HeroDisguise;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass; // 💡 이 줄을 추가!
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.RectF;

public class HeroSprite extends CharSprite {
	
	private static final int FRAME_WIDTH	= 12;
	private static final int FRAME_HEIGHT	= 15;
	
	private static final int RUN_FRAMERATE	= 20;
	
	private static TextureFilm tiers;
	
	private Animation fly;
	private Animation read;

	public HeroSprite() {
		super();
		
		texture( Dungeon.hero.heroClass.spritesheet() );
		updateArmor();
		
		link( Dungeon.hero );

		if (ch.isAlive())
			idle();
		else
			die();
	}

	public void disguise(HeroClass cls){
		texture( cls.spritesheet() );
		updateArmor();
	}
	
	public void updateArmor() {
		boolean isWarrior = (Dungeon.hero.heroClass == HeroClass.WARRIOR);

		int fw = isWarrior ? 16 : FRAME_WIDTH;
		int fh = isWarrior ? 16 : FRAME_HEIGHT;

		TextureFilm film;
		
		if (isWarrior) {
			// 🥊 [현우 전용 로직: 무기 루트에 따른 도트 변경]
			boolean isBerserker = (Dungeon.hero.subClass == HeroSubClass.BERSERKER); // 글러브
			boolean isGladiator = (Dungeon.hero.subClass == HeroSubClass.GLADIATOR); // 톤파

			// 1. 현재 무기 루트(전직)에 따라 스프라이트 시트의 줄(Tier)을 결정합니다.
			int safeTier = 0; // 🧑 기본 현우 (1~9층, 0번째 줄)
			if (isBerserker) {
				safeTier = 0; // 🥊 글러브(광전사) 현우 (10층~, 1번째 줄) - 여기도 0통일 아직 없으니
			} else if (isGladiator) {
				safeTier = 0; // 🏏 톤파(검투사) 현우 (10층~, 2번째 줄) - 아직 없으니 0 통일
			}

			// 2. 결정된 safeTier를 바탕으로 텍스처를 잘라옵니다.
			SmartTexture tex = TextureCache.get( Dungeon.hero.heroClass.spritesheet() );
			TextureFilm warriorTiers = new TextureFilm( tex, tex.width, fh );
			
			film = new TextureFilm( warriorTiers, safeTier, fw, fh );
			
			// 3. 애니메이션 프레임 세팅 (현우 계열 공통)
			idle = new Animation( 2, true );
			idle.frames( film, 0 );
			
			run = new Animation( 10, true );
			run.frames( film, 0, 1 );
			
			die = new Animation( 2, false );
			die.frames( film, 3 ); 
			
			attack = new Animation( 15, false );
			attack.frames( film, 2, 0 );
			
			zap = attack.clone();
			operate = idle.clone();
			fly = idle.clone();
			read = idle.clone();

		} else {
			// 🧙 [기존 영웅 로직] 전사가 아니라면 원본 녹픽던의 애니메이션을 그대로 사용!
			film = new TextureFilm( tiers(), Dungeon.hero.tier(), FRAME_WIDTH, FRAME_HEIGHT );
			
			idle = new Animation( 1, true );
			idle.frames( film, 0, 0, 0, 1, 0, 0, 1, 1 );
			
			run = new Animation( RUN_FRAMERATE, true );
			run.frames( film, 2, 3, 4, 5, 6, 7 );
			
			die = new Animation( 20, false );
			die.frames( film, 8, 9, 10, 11, 12, 11 );
			
			attack = new Animation( 15, false );
			attack.frames( film, 13, 14, 15, 0 );
			
			zap = attack.clone();
			
			operate = new Animation( 8, false );
			operate.frames( film, 16, 17, 16, 17 );
			
			fly = new Animation( 1, true );
			fly.frames( film, 18 );

			read = new Animation( 20, false );
			read.frames( film, 19, 20, 20, 20, 20, 20, 20, 20, 20, 19 );
		}
		
		if (Dungeon.hero.isAlive())
			idle();
		else
			die();
	}
	
	@Override
	public void place( int p ) {
		super.place( p );
		if (Game.scene() instanceof GameScene) Camera.main.panFollow(this, 5f);
	}

	@Override
	public void move( int from, int to ) {
		super.move( from, to );
		if (ch != null && ch.flying) {
			play( fly );
		}
		Camera.main.panFollow(this, 20f);
	}

	@Override
	public void idle() {
		super.idle();
		if (ch != null && ch.flying) {
			play( fly );
		}
	}

	@Override
	public void jump( int from, int to, float height, float duration,  Callback callback ) {
		super.jump( from, to, height, duration, callback );
		play( fly );
		Camera.main.panFollow(this, 20f);
	}

	public synchronized void read() {
		animCallback = new Callback() {
			@Override
			public void call() {
				idle();
				ch.onOperateComplete();
			}
		};
		play( read );
	}

	@Override
	public void bloodBurstA(PointF from, int damage) {
		//Does nothing.

		/*
		 * This is both for visual clarity, and also for content ratings regarding violence
		 * towards human characters. The heroes are the only human or human-like characters which
		 * participate in combat, so removing all blood associated with them is a simple way to
		 * reduce the violence rating of the game.
		 */
	}

	@Override
	public void update() {
		sleeping = ch.isAlive() && ((Hero)ch).resting;
		
		super.update();
	}
	
	public void sprint( float speed ) {
		run.delay = 1f / speed / RUN_FRAMERATE;
	}
	
	public static TextureFilm tiers() {
		if (tiers == null) {
			SmartTexture texture = TextureCache.get( Assets.Sprites.ROGUE );
			tiers = new TextureFilm( texture, texture.width, FRAME_HEIGHT );
		}
		
		return tiers;
	}

	public static Image avatar( Hero hero ){
		if (hero.buff(HeroDisguise.class) != null){
			return avatar(hero.buff(HeroDisguise.class).getDisguise(), hero.tier());
		} else {
			return avatar(hero.heroClass, hero.tier());
		}
	}
	
	public static Image avatar( HeroClass cl, int armorTier ) {
	    Image avatar = new Image( cl.spritesheet() );
	    
	    if (cl == HeroClass.WARRIOR) {
	        // [현우 전용] 전체 영역(64x16) 중 딱 1/4 (첫 얼굴 프레임)만 오려냄
	        RectF frame = new RectF( avatar.frame() );
	        frame.right = frame.left + (frame.width() / 4f); 
	        avatar.frame( frame );
	    } else {
	        // [원본 유지]
	        RectF patch = tiers().get( armorTier );
	        RectF frame = avatar.texture.uvRect( 1, 0, FRAME_WIDTH, FRAME_HEIGHT );
	        frame.shift( patch.left, patch.top );
	        avatar.frame( frame );
	    }
	    return avatar;
	}
}
