package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class WorkGloves extends MeleeWeapon {

    {
        image = ItemSpriteSheet.GLOVES;
        hitSound = Assets.Sounds.HIT;
        hitSoundPitch = 1.3f; // 찰진 펀치 사운드

        tier = 1;
        DLY = 0.5f; // 핵심! 공격 속도 2배 -> 도그파이트 스택이 2배로 빨리 쌓임
        ACCURACY = 1.2f; // 테스팅 체감 위한 명중률 상승 보정
        
        bones = false;
    }

    // 기본 최대 공격력 공식 (기본 5, 강화 레벨당 +1)
    // 공속이 2배이므로 데미지가 낮아도 턴당 DPS는 우수함
    @Override
    public int max(int lvl) {
        return Math.round(2.5f * (tier + 1)) + lvl * Math.round(0.5f * (tier + 1));
    }
   
}
