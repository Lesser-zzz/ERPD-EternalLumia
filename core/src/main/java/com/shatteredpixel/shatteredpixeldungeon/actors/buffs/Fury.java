package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Fury extends FlavourBuff {

    // 매 턴 체크: 15턴이 지나지 않았더라도 보호막이 깨지면 즉시 버프 종료!
    @Override
    public boolean act() {
        if (target instanceof Hero) {
            Hero hero = (Hero) target;
            
            // 현우에게 방어막이 완전히 사라졌는지(null) 체크
            if (hero.buff(Barrier.class) == null) {
                detach(); // 보호막 깨짐 = 필사즉생 강제 종료!
                return true; 
            }
        }
        return super.act();
    }

    // 버프 종료 시 효과 (15턴 경과 or 쉴드 파괴 시)
    @Override
    public void detach() {
        super.detach();
        
        if (target instanceof Hero) {
            Hero hero = (Hero) target;
            
            // ⭐ [복구 1] 뻥튀기했던 근력(STR) 5 다시 원상복구
            hero.STR -= 5;
            
            // ⭐ [복구 2] 쉴드가 깨져서 조기 종료된 경우, 남은 신속(Haste) 버프도 강제로 삭제
            Haste haste = hero.buff(Haste.class);
            if (haste != null) haste.detach();

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
