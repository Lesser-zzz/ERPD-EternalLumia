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
			// [현우 전용 로직] 1616 해상도 적용 및 방어구 에러 방지
			SmartTexture tex = TextureCache.get( Dungeon.hero.heroClass.spritesheet() );
			TextureFilm warriorTiers = new TextureFilm( tex, tex.width, fh );
			
			// 갑옷을 입어도 없는 이미지를 찾다 튕기지 않도록 강제로 0번째 줄(기본 도트) 고정
			int safeTier = 0; 
			film = new TextureFilm( warriorTiers, safeTier, fw, fh );
			
			// 0번 프레임: 대기 (가만히 서 있음)
			idle = new Animation( 2, true );
			idle.frames( film, 0 );
			
			// 0번과 1번 프레임 번갈아 재생: 이동 (다리를 움직이며 뜀)
			run = new Animation( 10, true );
			run.frames( film, 0, 1 );
			
			// 3번 프레임: 사망 (비석으로 변함)
			die = new Animation( 2, false );
			die.frames( film, 3 ); 
			
			// 2번 프레임 후 0번 프레임: 공격 (주먹을 뻗었다가 다시 기본 자세로)
			attack = new Animation( 15, false );
			attack.frames( film, 2, 0 );
			
			zap = attack.clone();
			operate = idle.clone();
			fly = idle.clone();
			read = idle.clone();

		} else {
			// [기존 영웅 로직] 전사가 아니라면 원본 녹픽던의 애니메이션을 그대로 사용!
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
    	boolean isWarrior = (cl == HeroClass.WARRIOR);
    
    	Image avatar = new Image( cl.spritesheet() );
    
    	if (isWarrior) {
        	// [현우 전용 로직] 
        	// TextureFilm 버그를 피하고 16x16 영역을 uvRect로 직접 안전하게 자릅니다.
        	RectF frame = avatar.texture.uvRect( 0, 0, 16, 16 );
        	avatar.frame( frame );
    	} else {
        	// [기존 영웅 로직 유지]
        	RectF patch = tiers().get( armorTier );
        	RectF frame = avatar.texture.uvRect( 1, 0, FRAME_WIDTH, FRAME_HEIGHT );
        	frame.shift( patch.left, patch.top );
        	avatar.frame( frame );
    	}
    
    	return avatar;
	}
}
