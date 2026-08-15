	/**
 * Hyunwoo's passive skill.
 *
 * Gain stacks by landing attacks.
 * (Boar subclass will also gain stacks when hit.)
 *
 * When enough stacks are collected,
 * Dogfight becomes Ready.
 *
 * The next successful normal attack:
 * - Deals bonus damage
 * - Heals the hero
 * - Consumes Dogfight
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Visual;
import com.watabou.utils.Bundle;

public class Dogfight extends Buff {

    public Dogfight() {
        type = buffType.POSITIVE;
        announced = true;
    }

    // 현재 스택
    private int dogfightStack = 0;

    // 발동 요구치
    private int requiredHits = 10;

    // 발동 준비 완료 여부
    private boolean ready = false;

	public int totalActivations = 0;
    public int cooldownTurns = 0; 

    private static final String STACK = "dogfightStack";
    private static final String READY = "ready";
	private static final String REQUIRED = "requiredHits"; // <- 아래에 있던 걸 위로 가져와 통합!
    // 👉 세이브 키 값도 추가합니다!
    private static final String TOTAL_ACT = "totalActivations";
    private static final String CD_TURNS = "cooldownTurns";

    // icon 추가
    @Override
    public int icon() {
        return BuffIndicator.COMBO;
    }

    @Override
    public String iconTextDisplay() {

        if (ready) {
            return "!";
        }

        return Integer.toString(dogfightStack);
    }


    public Visual secondaryVisual() {

        BitmapText txt = new BitmapText(PixelScene.pixelFont);

        if (ready) {
            txt.text("!");
        } else {
            txt.text(Integer.toString(dogfightStack));
        }

        txt.hardlight(CharSprite.POSITIVE);
        txt.measure();

        return txt;
    }

    /**
     * 스택 증가
     * Ready 상태에서는 더 이상 증가하지 않는다.
     */
    private void addStack() {

		// [🌟핵심 추가 부분🌟] 스택을 올리거나 검사하기 직전에 무조건 실시간 갱신!
        // 이 버프가 달려있는 대상(target)이 영웅(Hero)이라면, 특성을 확인해서 요구치를 다시 계산합니다.
        if (this.target instanceof Hero) {
            this.updateRequirementFromTalent((Hero) this.target);
        }

        if (activatedThisAttack) {
            activatedThisAttack = false;
            return;
        }

        if (ready) {
            return;
        }

        dogfightStack++;

        if (dogfightStack >= requiredHits) {
            dogfightStack = requiredHits;
            ready = true;
        }   

        BuffIndicator.refreshHero();
    }

    /**
     * 공격 명중 시 호출
     */
    public void onHitEnemy() {
        addStack();
    }

    /**
     * 피격(명중) 시 호출
     * (멧돼지 전직 이후 사용)
     */
    public void onTakeDamage() {
        addStack();
    }

    /**
     * Ready 상태인지 확인 후
     * 성공적으로 소비하면 true 반환
     */
    public boolean consumeReady() {

        if (!ready) {
            return false;
        }

        consume();
        return true;
    }
//독파 공격 시 바로 스택 1 되는 걸 막기위한 무시로그
    private boolean activatedThisAttack = false;

    /**
     * Ready 상태의 일반 공격이
     * 실제로 명중했을 때 호출된다.
     */
    public void attackSuccess(Hero hero, Char enemy) {

        if (!ready) {
            return;
        }
//중복 방지
        activatedThisAttack = true;

         // Dogfight 발동 연출
        playActivationEffect(hero);

        int bonusDamage = calculateBonusDamage(hero);

        if (bonusDamage > 0) {
            enemy.damage(bonusDamage, hero);

            CellEmitter.center(enemy.pos).start(
                    Speck.factory(Speck.YELLOW_LIGHT),
                    0.15f,
                    3
            );
        }
        
    

        healHero(hero, bonusDamage);

        GLog.p(Messages.get(this, "activated"));

        consume();
    }

    // Dogfight 발동 연출 모음

    private void playActivationEffect(Hero hero) {

        Sample.INSTANCE.play(
                Assets.Sounds.HIT_STRONG
        );

        hero.sprite.flash();

        CellEmitter.center(hero.pos).start(
                Speck.factory(Speck.YELLOW_LIGHT),
                0.1f,
                8
            );

    }

    /**
     * 추가 피해 계산
     *
     * 현재 프로토타입 값.
     * 이후 레벨/무기/특성 연동 예정.
     */
    private int calculateBonusDamage(Hero hero) {

        return 5;

    }

    /**
     * 도그파이트 회복 처리
     *
     * 현재는 추가 피해만큼 회복.
     * 이후 밸런스 조정 예정.
     */
    private void healHero(Hero hero, int amount) {

        if (amount <= 0) {
            return;
        }

        hero.HP = Math.min(hero.HT, hero.HP + amount);

        hero.sprite.showStatusWithIcon(
            CharSprite.POSITIVE,
            Integer.toString(amount),
            FloatingText.HEALING
        );

    }
    /**
     * 현재 스택
     */
    public int getDogfightStack() {
        return dogfightStack;
    }

    /**
     * 현재 발동 요구치
     */
    public int getRequiredHits() {
        return requiredHits;
    }

    /**
     * Ready 상태 여부
     */
    public boolean isReady() {
        return ready;
    }


    // 독파 설명    
            // Dogfight.java 내부의 desc() 메서드 수정

    @Override
    public String desc() {
        // 기존: 영웅이 광전사인지 확인하고 설명을 다르게 출력하는 로직
        if (target instanceof Hero) {
            Hero hero = (Hero) target;

            if (hero.subClass == HeroSubClass.BERSERKER) {
                // 1. 기본 설명 (기존의 boar_desc를 가져옴)
                String baseDesc = Messages.get(this, "boar_desc", requiredHits);
                
                // 2. [추가] 영구 스탯 상승 표기
                String statsDesc = "\n\n[현재 상태]\n- 누적 발동 횟수: " + totalActivations + "회\n- 증가한 최대 체력: " + totalActivations + " HT";
                
                // 3. [추가] 필생즉사 쿨다운 표기
                if (cooldownTurns > 0) {
                    statsDesc += "\n\n 필생즉사 쿨다운: " + cooldownTurns + "턴 남음";
                } else {
                    statsDesc += "\n\n 필생즉사 발동 가능 (사망 위기 시 자동 발동)";
                }
                
                return baseDesc + statsDesc;
            }
        }

        // 광전사가 아닐 때의 기본 설명
        return Messages.get(this, "desc", requiredHits);   
    }

   

    /**
	 * 영웅이 찍은 DOGFIGHT 특성 레벨을 바탕으로 발동 요구치를 동적으로 계산
	 */
	public void updateRequirementFromTalent(Hero hero) {
		if (hero == null) return;
		
		int baseRequirement = 10; // 기본 필요 타격 횟수
		
		// 영웅이 찍은 T2 도그파이트 특성 포인트 (0, 1, 2)
		int talentPoints = hero.pointsInTalent(com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.DOGFIGHT);
		
		// 특성 1레벨당 1씩 감소 (최대 2 감소하여 8회에 발동 등 원하는 밸런스 설정)
		int reduction = talentPoints * 1;
		
		requiredHits = Math.max(4, baseRequirement - reduction);
		
		// 요구치가 줄어들었을 때 현재 스택이 만족한다면 즉시 Ready
		if (!ready && dogfightStack >= requiredHits) {
			    dogfightStack = requiredHits;
			    ready = true;
		    }
	    }

    //게임 껏다 켜도 스택이 유지되도록 해야함


    @Override
    public void storeInBundle(Bundle bundle) {

        super.storeInBundle(bundle);

        bundle.put(STACK, dogfightStack);
        bundle.put(REQUIRED, requiredHits);
        bundle.put(READY, ready);
		bundle.put(TOTAL_ACT, totalActivations);
		bundle.put(CD_TURNS, cooldownTurns);

    }


    @Override
    public void restoreFromBundle(Bundle bundle) {

        super.restoreFromBundle(bundle);

        dogfightStack = bundle.getInt(STACK);
        requiredHits = bundle.getInt(REQUIRED);
        ready = bundle.getBoolean(READY);
		totalActivations = bundle.getInt(TOTAL_ACT);
		cooldownTurns = bundle.getInt(CD_TURNS);

    }
	// 필생즉사 쿨다운 매 턴 감소
	@Override
	public boolean act() {
		if (cooldownTurns > 0) {
			cooldownTurns--;
			
			// 매 턴 UI를 새로고침하면 렉이 걸릴 수 있으므로, 쿨다운이 0이 딱 되었을 때만 UI를 갱신합니다!
			if (cooldownTurns == 0) {
				com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator.refreshHero();
				// ⭐ [추가] 쿨다운이 끝나는 순간 긍정적인(초록색) 시스템 로그 출력!
				com.shatteredpixel.shatteredpixeldungeon.utils.GLog.p("탈진에서 회복했습니다. 이제 다시 필생즉사가 발동할 수 있습니다!");
			}
		}
		
		// ⭐ [핵심] 엔진에게 "1턴(TICK)이 지날 때마다 나를 다시 불러줘!" 라고 예약하는 필수 코드
		spend( TICK );
		return true; // 버프가 사라지지 않고 계속 유지되도록 true 반환
	}
    

    /**
     * 도그파이트 소비
     */
    public void consume() {

        dogfightStack = 0;
        ready = false;

		// 영웅이 광전사(멧돼지 현우)로 전직한 상태라면 전직 후부터 체력 스택 증가
	    if (target instanceof com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero) {
	        com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero hero = (com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero) target;
	
	        if (hero.subClass == com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass.BERSERKER) {
	            totalActivations++;
	            hero.HT += 1; // 최대 체력 1 증가
	
	        }
	    }
		
        BuffIndicator.refreshHero();

    }

}
