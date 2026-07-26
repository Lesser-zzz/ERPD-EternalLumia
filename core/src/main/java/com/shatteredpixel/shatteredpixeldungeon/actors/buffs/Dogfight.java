/**
 * Hyunwoo's passive skill.
 *
 * Gain stacks when attacking.
 * Heal after reaching the required number of stacks.
 */


package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

public class Dogfight extends Buff {

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    // 현재 쌓인 도그파이트 스택
    private int dogfightStack = 0;

    // 발동까지 필요한 스택
    private int requiredHits = 10;

    // 도그파이트 활성화 여부
    private boolean ready = false;

    /**
     * 공격 명중 시 호출
     */
    public void onHitEnemy() {

        if (ready) {
            return;
        }

        dogfightStack++;

        if (dogfightStack >= requiredHits) {
            ready = true;
        }

    }

    /**
     * 피격 시 호출
     * (멧돼지 전직 후에만 사용할 예정)
     */
    public void onTakeDamage() {

        if (ready) {
            return;
        }

        dogfightStack++;

        if (dogfightStack >= requiredHits) {
            ready = true;
        }

    }

    /**
     * 현재 스택
     */
    public int getDogfightStack() {
        return dogfightStack;
    }

    /**
     * 현재 요구치
     */
    public int getRequiredHits() {
        return requiredHits;
    }

    /**
     * 도그파이트 준비 여부
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * 레벨업 특성 등으로 요구치 감소
     */
    public void reduceRequirement(int amount) {

        requiredHits -= amount;

        if (requiredHits < 1) {
            requiredHits = 1;
        }

    }

    /**
     * 도그파이트를 소비하고 초기화
     * (다음 공격에서 추가 피해 + 회복 후 호출될 예정)
     */
    public void consume() {

        ready = false;
        dogfightStack = 0;

    }

}
