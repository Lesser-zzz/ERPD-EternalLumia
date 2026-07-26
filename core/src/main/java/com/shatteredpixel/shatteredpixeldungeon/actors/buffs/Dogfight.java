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
     * 스택을 1 증가시킨다.
     * Ready 상태에서는 더 이상 증가하지 않는다.
     */
    private void addStack() {

        if (ready) {
            return;
        }

        dogfightStack++;

        if (dogfightStack >= requiredHits) {
            ready = true;
        }

    }

    /**
     * 공격 명중 시 호출
     */
    public void onHitEnemy() {
        addStack();
    }

    /**
     * 피격 시 호출
     * (멧돼지 전직 후에만 사용할 예정)
     */
    public void onTakeDamage() {
        addStack();
    }

    /**
     * Ready 상태를 소비한다.
     * 성공적으로 소비했다면 true를 반환한다.
     */
    public boolean consumeReady() {

        if (!ready) {
            return false;
        }

        consume();

        return true;

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
     * 현재 Ready 상태인지 반환
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * 레벨업 특성 등으로 발동 요구치를 감소시킨다.
     */
    public void reduceRequirement(int amount) {

        requiredHits -= amount;

        if (requiredHits < 1) {
            requiredHits = 1;
        }

    }

    /**
     * 도그파이트를 소비하고 초기화한다.
     */
    public void consume() {

        dogfightStack = 0;
        ready = false;

    }

}
