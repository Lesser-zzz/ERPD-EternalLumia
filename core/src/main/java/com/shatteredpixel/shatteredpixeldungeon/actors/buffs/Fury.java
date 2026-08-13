package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Fury extends FlavourBuff {

    // [복구 1] 15턴 동안 공격력(피해량) 30% 증폭! 
    // 구버전 엔진에서 공격력을 올릴 때 쓰는 기본 제공 메서드입니다.
    @Override
    public int attackFactor() {
        return 130; // 기본 100 기준 130%
    }

    // [복구 2] 15턴 동안 이동 속도 40% 증가! (턴 소모량 감소)
    @Override
    public float speedFactor() {
        return 1.4f; // 이동 속도 1.4배
    }

    // [복구 3] 매 턴 체크: 보호막이 깨지면 15턴이 안 지났어도 버프 즉시 종료!
    @Override
    public boolean act() {
        if (target instanceof Hero) {
            Hero hero = (Hero) target;
            
            // 현우에게 방어막 버프가 아예 떨어져 나갔는지(null) 확인합니다.
            // (구버전은 쉴드가 0이 되면 Barrier 버프 객체 자체가 사라집니다)
            if (hero.buff(Barrier.class) == null) {
                detach(); // 보호막 깨짐 = 필생즉사 강제 종료!
                return true; 
            }
        }
        return super.act();
    }

    // [복구 4] 버프 종료 시 효과 (15턴 경과 or 쉴드 파괴 시)
    @Override
    public void detach() {
        super.detach();
        
        if (target instanceof Hero) {
            Hero hero = (Hero) target;
            
            // 탈진: 5턴 간 시야 감소(Blindness)와 이동속도 감소(Cripple) 디버프 부여
            Buff.affect(hero, Blindness.class, 5f);
            Buff.affect(hero, Cripple.class, 5f);
            
            if (hero.sprite != null) {
                hero.sprite.showStatus(CharSprite.NEGATIVE, "탈진...");
            }
            
            // 100턴 쿨다운 세기 시작!
            Dogfight dogfight = hero.buff(Dogfight.class);
            if (dogfight != null) {
                dogfight.cooldownTurns = 100;
                BuffIndicator.refreshHero(); // UI 갱신
            }
        }
    }

    @Override
    public int icon() { 
        return BuffIndicator.FURY; 
    }
}
