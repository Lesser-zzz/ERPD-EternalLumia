package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.charSprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages; // (추가) 다국어 처리용

public class Fury extends FlavourBuff {

    // 1. 유지 효과: 15턴 동안 이동속도 버프 (약 40% 증가)
    @Override
    public float speedFactor() {
        return 1.4f; 
    }

    // 2. 유지 효과: 15턴 동안 공격 피해량 증폭 (약 30% 증가)
    @Override
    public int attackFactor(int damage) {
        return Math.round(damage * 1.3f); 
    }

    // 3. 매 턴 체크: 15턴이 지나지 않았더라도 보호막이 깨지면 즉시 버프 종료!
    @Override
    public boolean act() {
        if (target instanceof Hero) {
            Hero hero = (Hero) target;
            // 현우의 방어막(Barrier) 상태를 확인합니다.
            Barrier barrier = hero.buff(Barrier.class);
            // 방어막이 아예 없거나, 수치가 0 이하라면 깨진 것으로 간주!
            if (barrier == null || barrier.level <= 0) { // (엔진 버전에 따라 level() 일 수도 있습니다)
                detach(); // 필생즉사 버프 강제 종료
                return true; 
            }
        }
        return super.act();
    }

    // 4. 종료 효과: 버프가 끝나거나(15턴 경과) 보호막이 깨져서 detach()될 때
    @Override
    public void detach() {
        super.detach();
        
        if (target instanceof Hero) {
            Hero hero = (Hero) target;
            
            // 종료 부작용 1: 5턴 간 시야 감소(Blindness)와 이동속도 감소(Cripple) 디버프 부여
            Buff.affect(hero, Blindness.class, 5f);
            Buff.affect(hero, Cripple.class, 5f);
            hero.sprite.showStatus(CharSprite.NEGATIVE, "탈진...");
            
            // 종료 부작용 2: 도그파이트 객체에 필생즉사 쿨다운 100턴 세기 시작!
            Dogfight dogfight = hero.buff(Dogfight.class);
            if (dogfight != null) {
                dogfight.cooldownTurns = 100;
                BuffIndicator.refreshHero(); // UI 갱신해서 쿨다운 숫자가 보이게 함
            }
        }
    }

    @Override
    public int icon() { 
        return BuffIndicator.FURY; 
    }
    
    // (선택) 여기에 기존 원본 코드처럼 toString()이나 desc()를 남겨두거나, 
    // Messages.get()을 사용하도록 세팅할 수 있습니다. 
    // 기본적으로 FlavourBuff를 상속하면 messages_ko.properties의 텍스트를 자동으로 읽어옵니다.
}
