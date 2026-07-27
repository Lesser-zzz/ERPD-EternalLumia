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

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    // 현재 스택
    private int dogfightStack = 0;

    // 발동 요구치
    private int requiredHits = 10;

    // 발동 준비 완료 여부
    private boolean ready = false;

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

    /**
     * Ready 상태의 일반 공격이
     * 실제로 명중했을 때 호출된다.
     */
    public void attackSuccess(Hero hero, Char enemy) {

        if (!ready) {
            return;
        }

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
    @Override
    public String desc() {

        if (target instanceof Hero) {

            Hero hero = (Hero) target;

            if (hero.subClass == HeroSubClass.BERSERKER) {

                return Messages.get(this, "boar_desc",
                        requiredHits);

            }

        }

        return Messages.get(this, "desc",
                requiredHits);    
    }

    /**
     * 레벨업 특성 등으로
     * 발동 요구치를 감소시킨다.
     */
    public void reduceRequirement(int amount) {

        requiredHits -= amount;

        if (requiredHits < 1) {
            requiredHits = 1;
        }

        // 이미 현재 스택이 요구치를 만족하면 즉시 Ready
        if (!ready && dogfightStack >= requiredHits) {
            ready = true;
        }

    }

    //게임 껏다 켜도 스택이 유지되도록 해야함

    private static final String STACK = "dogfight_stack";
    private static final String REQUIRED = "dogfight_required";
    private static final String READY = "dogfight_ready";


    @Override
    public void storeInBundle(Bundle bundle) {

        super.storeInBundle(bundle);

        bundle.put(STACK, dogfightStack);
        bundle.put(REQUIRED, requiredHits);
        bundle.put(READY, ready);

    }


    @Override
    public void restoreFromBundle(Bundle bundle) {

        super.restoreFromBundle(bundle);

        dogfightStack = bundle.getInt(STACK);
        requiredHits = bundle.getInt(REQUIRED);
        ready = bundle.getBoolean(READY);

    }

    

    /**
     * 도그파이트 소비
     */
    public void consume() {

        dogfightStack = 0;
        ready = false;

        BuffIndicator.refreshHero();

    }

}
